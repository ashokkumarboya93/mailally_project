# =============================================
# Gate 1 (v4): Raw SMTP Protocol Authentication Test
# =============================================
# Run: powershell -ExecutionPolicy Bypass -File gate1-smtp-test-v4.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1 (v4): Raw SMTP Protocol Test" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$smtpHost     = "smtp-relay.brevo.com"
$smtpPort     = 587
$smtpUsername  = "info@marcamor.com"
$smtpPassword  = "YOUR_BREVO_SMTP_PASSWORD"
$senderEmail  = "info@marcamor.com"
$recipientEmail = "ashokkumarboya93@gmail.com"

function Read-SmtpLines($rdr) {
    $lines = @()
    do {
        $line = $rdr.ReadLine()
        if ($null -eq $line) { break }
        $lines += $line
        Write-Host "  S: $line" -ForegroundColor Gray
    } while ($line.Length -ge 4 -and $line[3] -eq '-')
    return $lines
}

function Get-SmtpCode($lines) {
    if ($lines -and $lines.Count -gt 0) {
        $last = $lines[-1]
        if ($last.Length -ge 3) {
            return $last.Substring(0, 3)
        }
    }
    return "000"
}

Write-Host "`n--- Connecting to ${smtpHost}:${smtpPort} ---" -ForegroundColor Yellow

try {
    $tcp = New-Object System.Net.Sockets.TcpClient($smtpHost, $smtpPort)
    $stream = $tcp.GetStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $writer = New-Object System.IO.StreamWriter($stream)
    $writer.AutoFlush = $true
    $reader.BaseStream.ReadTimeout = 15000

    # Step 1: Banner
    Write-Host "`n[STEP 1] Server Banner:" -ForegroundColor Yellow
    $banner = Read-SmtpLines $reader

    # Step 2: EHLO
    Write-Host "`n[STEP 2] EHLO (pre-TLS):" -ForegroundColor Yellow
    $writer.WriteLine("EHLO mailally.test")
    $ehlo = Read-SmtpLines $reader

    # Step 3: STARTTLS
    Write-Host "`n[STEP 3] STARTTLS:" -ForegroundColor Yellow
    $writer.WriteLine("STARTTLS")
    $tlsResp = Read-SmtpLines $reader
    $tlsCode = Get-SmtpCode $tlsResp

    if ($tlsCode -ne "220") {
        Write-Host "  >> STARTTLS rejected (code $tlsCode)" -ForegroundColor Red
        $tcp.Close()
        exit 1
    }
    Write-Host "  >> STARTTLS accepted (220). Upgrading to TLS..." -ForegroundColor Green

    # Step 4: TLS Handshake
    $sslStream = New-Object System.Net.Security.SslStream($stream, $false)
    $sslStream.AuthenticateAsClient($smtpHost)
    Write-Host "  >> TLS established: $($sslStream.SslProtocol)" -ForegroundColor Green

    $reader = New-Object System.IO.StreamReader($sslStream)
    $writer = New-Object System.IO.StreamWriter($sslStream)
    $writer.AutoFlush = $true

    # Step 5: EHLO post-TLS
    Write-Host "`n[STEP 4] EHLO (post-TLS):" -ForegroundColor Yellow
    $writer.WriteLine("EHLO mailally.test")
    $ehlo2 = Read-SmtpLines $reader

    # Step 6: AUTH LOGIN
    Write-Host "`n[STEP 5] AUTH LOGIN:" -ForegroundColor Yellow
    $writer.WriteLine("AUTH LOGIN")
    $authResp = Read-SmtpLines $reader
    $authCode = Get-SmtpCode $authResp

    if ($authCode -eq "334") {
        # Username
        $userB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpUsername))
        Write-Host "  C: [username base64]" -ForegroundColor DarkGray
        $writer.WriteLine($userB64)
        $userResp = Read-SmtpLines $reader

        # Password
        $passB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpPassword))
        Write-Host "  C: [password base64]" -ForegroundColor DarkGray
        $writer.WriteLine($passB64)
        $passResp = Read-SmtpLines $reader
        $passCode = Get-SmtpCode $passResp

        if ($passCode -eq "235") {
            Write-Host "`n=============================================" -ForegroundColor Green
            Write-Host "  SMTP AUTH RESULT: 235 - AUTHENTICATED" -ForegroundColor Green
            Write-Host "  Response: $($passResp[-1])" -ForegroundColor Green
            Write-Host "=============================================" -ForegroundColor Green

            # Send test email
            Write-Host "`n[STEP 6] Sending test email..." -ForegroundColor Yellow

            $writer.WriteLine("MAIL FROM:<$senderEmail>")
            $mfResp = Read-SmtpLines $reader

            $writer.WriteLine("RCPT TO:<$recipientEmail>")
            $rcptResp = Read-SmtpLines $reader

            $writer.WriteLine("DATA")
            $dataResp = Read-SmtpLines $reader

            $ts = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $writer.WriteLine("From: MailAlly <$senderEmail>")
            $writer.WriteLine("To: $recipientEmail")
            $writer.WriteLine("Subject: MailAlly Gate 1 PASS - SMTP Verified $ts")
            $writer.WriteLine("Content-Type: text/html")
            $writer.WriteLine("")
            $writer.WriteLine("<h1 style='color:green'>Gate 1 PASSED</h1><p>SMTP auth verified at $ts</p>")
            $writer.WriteLine(".")
            $sendResp = Read-SmtpLines $reader
            $sendCode = Get-SmtpCode $sendResp

            if ($sendCode -eq "250") {
                Write-Host "`n=============================================" -ForegroundColor Green
                Write-Host "  GATE 1 FINAL RESULT: PASS" -ForegroundColor Green
                Write-Host "  Auth:  235 Authenticated" -ForegroundColor Green
                Write-Host "  Send:  250 Accepted" -ForegroundColor Green
                Write-Host "  Inbox: $recipientEmail" -ForegroundColor Green
                Write-Host "=============================================" -ForegroundColor Green
            } else {
                Write-Host "  Send response ($sendCode): $($sendResp[-1])" -ForegroundColor Yellow
            }

        } elseif ($passCode -eq "535") {
            Write-Host "`n=============================================" -ForegroundColor Red
            Write-Host "  GATE 1 RESULT: FAIL" -ForegroundColor Red
            Write-Host "  535 - AUTHENTICATION FAILED" -ForegroundColor Red
            Write-Host "  Response: $($passResp[-1])" -ForegroundColor Red
            Write-Host "=============================================" -ForegroundColor Red
            Write-Host "  Credentials are INVALID." -ForegroundColor Yellow
            Write-Host "  Go to Brevo Dashboard -> SMTP & API" -ForegroundColor Yellow
            Write-Host "  Generate a new SMTP key." -ForegroundColor Yellow
        } else {
            Write-Host "  Unexpected auth response ($passCode): $($passResp[-1])" -ForegroundColor Yellow
        }
    } else {
        Write-Host "  AUTH LOGIN rejected ($authCode): $($authResp[-1])" -ForegroundColor Red
    }

    $writer.WriteLine("QUIT")
    $tcp.Close()

} catch {
    Write-Host "`n[ERROR] $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.InnerException) {
        Write-Host "  Inner: $($_.Exception.InnerException.Message)" -ForegroundColor Red
    }
}

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1 (v4) Complete" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
