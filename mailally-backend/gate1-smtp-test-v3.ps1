# =============================================
# Gate 1 (v3): Raw SMTP Protocol Authentication Test
# =============================================
# Performs a proper EHLO -> STARTTLS -> AUTH LOGIN
# sequence with correct multi-line response parsing.
#
# Run: powershell -ExecutionPolicy Bypass -File gate1-smtp-test-v3.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1 (v3): Raw SMTP Protocol Test" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$smtpHost     = "smtp-relay.brevo.com"
$smtpPort     = 587
$smtpUsername  = "info@marcamor.com"
$smtpPassword  = "YOUR_BREVO_SMTP_PASSWORD"
$senderEmail  = "info@marcamor.com"
$recipientEmail = "ashokkumarboya93@gmail.com"

function Read-SmtpResponse($reader) {
    $lines = @()
    do {
        $line = $reader.ReadLine()
        $lines += $line
        Write-Host "  S: $line" -ForegroundColor Gray
    } while ($line -match '^\d{3}-')  # Multi-line responses have dash after code
    return $lines
}

Write-Host "`n--- Connecting to ${smtpHost}:${smtpPort} ---" -ForegroundColor Yellow

try {
    # Connect
    $tcp = New-Object System.Net.Sockets.TcpClient($smtpHost, $smtpPort)
    $stream = $tcp.GetStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $writer = New-Object System.IO.StreamWriter($stream)
    $writer.AutoFlush = $true
    $reader.BaseStream.ReadTimeout = 15000

    # Read server banner
    Write-Host "`n[STEP 1] Server Banner:" -ForegroundColor Yellow
    $banner = Read-SmtpResponse $reader

    # EHLO (before TLS)
    Write-Host "`n[STEP 2] EHLO (pre-TLS):" -ForegroundColor Yellow
    $writer.WriteLine("EHLO mailally.test")
    $ehloResp = Read-SmtpResponse $reader

    # Check if STARTTLS is advertised
    $starttlsSupported = ($ehloResp | Where-Object { $_ -match "STARTTLS" }).Count -gt 0
    if ($starttlsSupported) {
        Write-Host "  >> STARTTLS is advertised" -ForegroundColor Green
    } else {
        Write-Host "  >> STARTTLS NOT advertised!" -ForegroundColor Red
        Write-Host "  Server capabilities: $($ehloResp -join ' | ')" -ForegroundColor Yellow
    }

    # STARTTLS
    Write-Host "`n[STEP 3] STARTTLS:" -ForegroundColor Yellow
    $writer.WriteLine("STARTTLS")
    $tlsResp = Read-SmtpResponse $reader
    $tlsCode = if ($tlsResp[-1] -match '^(\d{3})') { $Matches[1] } else { "???" }

    if ($tlsCode -ne "220") {
        Write-Host "  >> STARTTLS REJECTED (code $tlsCode)" -ForegroundColor Red
        $tcp.Close()
        exit 1
    }
    Write-Host "  >> STARTTLS accepted, upgrading to TLS..." -ForegroundColor Green

    # Upgrade to TLS
    $sslStream = New-Object System.Net.Security.SslStream($stream, $false)
    $sslStream.AuthenticateAsClient($smtpHost)
    Write-Host "  >> TLS established: $($sslStream.SslProtocol)" -ForegroundColor Green

    $reader = New-Object System.IO.StreamReader($sslStream)
    $writer = New-Object System.IO.StreamWriter($sslStream)
    $writer.AutoFlush = $true

    # EHLO again (post-TLS, required by SMTP spec)
    Write-Host "`n[STEP 4] EHLO (post-TLS):" -ForegroundColor Yellow
    $writer.WriteLine("EHLO mailally.test")
    $ehlo2 = Read-SmtpResponse $reader

    $authMechanisms = ($ehlo2 | Where-Object { $_ -match "AUTH" })
    if ($authMechanisms) {
        Write-Host "  >> Auth mechanisms: $authMechanisms" -ForegroundColor Green
    }

    # AUTH LOGIN
    Write-Host "`n[STEP 5] AUTH LOGIN:" -ForegroundColor Yellow
    $writer.WriteLine("AUTH LOGIN")
    $authResp = Read-SmtpResponse $reader
    $authCode = if ($authResp[-1] -match '^(\d{3})') { $Matches[1] } else { "???" }

    if ($authCode -eq "334") {
        # Send username (base64)
        $userB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpUsername))
        Write-Host "  C: [username base64]" -ForegroundColor DarkGray
        $writer.WriteLine($userB64)
        $userResp = Read-SmtpResponse $reader

        # Send password (base64)
        $passB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpPassword))
        Write-Host "  C: [password base64]" -ForegroundColor DarkGray
        $writer.WriteLine($passB64)
        $passResp = Read-SmtpResponse $reader
        $passCode = if ($passResp[-1] -match '^(\d{3})') { $Matches[1] } else { "???" }

        if ($passCode -eq "235") {
            Write-Host "`n=============================================" -ForegroundColor Green
            Write-Host "  GATE 1 RESULT: PASS" -ForegroundColor Green
            Write-Host "  SMTP Authentication: 235 Successful" -ForegroundColor Green
            Write-Host "  Response: $($passResp[-1])" -ForegroundColor Green
            Write-Host "=============================================" -ForegroundColor Green

            # Try sending a test email now that we're authenticated
            Write-Host "`n[STEP 6] Sending test email..." -ForegroundColor Yellow
            $writer.WriteLine("MAIL FROM:<$senderEmail>")
            $mailFromResp = Read-SmtpResponse $reader

            $writer.WriteLine("RCPT TO:<$recipientEmail>")
            $rcptResp = Read-SmtpResponse $reader

            $writer.WriteLine("DATA")
            $dataResp = Read-SmtpResponse $reader

            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $writer.WriteLine("From: MailAlly <$senderEmail>")
            $writer.WriteLine("To: $recipientEmail")
            $writer.WriteLine("Subject: MailAlly Gate 1 PASS - SMTP Verified $timestamp")
            $writer.WriteLine("Content-Type: text/html")
            $writer.WriteLine("")
            $writer.WriteLine("<h1 style='color:green'>Gate 1 PASSED</h1><p>SMTP authentication verified at $timestamp</p>")
            $writer.WriteLine(".")
            $sendResp = Read-SmtpResponse $reader
            $sendCode = if ($sendResp[-1] -match '^(\d{3})') { $Matches[1] } else { "???" }

            if ($sendCode -eq "250") {
                Write-Host "`n  EMAIL DELIVERED SUCCESSFULLY" -ForegroundColor Green
                Write-Host "  Check inbox: $recipientEmail" -ForegroundColor Green
            } else {
                Write-Host "  Email send response: $($sendResp[-1])" -ForegroundColor Yellow
            }

        } elseif ($passCode -eq "535") {
            Write-Host "`n=============================================" -ForegroundColor Red
            Write-Host "  GATE 1 RESULT: FAIL" -ForegroundColor Red
            Write-Host "  535 Authentication Failed" -ForegroundColor Red
            Write-Host "  Response: $($passResp[-1])" -ForegroundColor Red
            Write-Host "=============================================" -ForegroundColor Red
            Write-Host "  ACTION: Credentials are INVALID." -ForegroundColor Yellow
            Write-Host "  Go to Brevo Dashboard -> SMTP & API -> Generate new SMTP key" -ForegroundColor Yellow
        } else {
            Write-Host "`n  UNEXPECTED AUTH RESPONSE: $($passResp[-1])" -ForegroundColor Yellow
        }
    } else {
        Write-Host "  AUTH LOGIN not accepted: $($authResp[-1])" -ForegroundColor Red
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
Write-Host "  Gate 1 (v3) Complete" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
