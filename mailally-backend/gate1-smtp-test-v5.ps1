# =============================================
# Gate 1 (v5): Raw SMTP Protocol Authentication Test
# =============================================
# Run: powershell -ExecutionPolicy Bypass -File gate1-smtp-test-v5.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1 (v5): Raw SMTP Protocol Test" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$smtpHost       = "smtp-relay.brevo.com"
$smtpPort       = 587
$smtpUsername    = "b2fbd6001@smtp-brevo.com"
$smtpPassword   = "YOUR_BREVO_SMTP_PASSWORD"
$senderEmail    = "info@marcamor.com"
$recipientEmail = "ashokkumarboya93@gmail.com"

Write-Host "`n--- Connecting to ${smtpHost}:${smtpPort} ---`n" -ForegroundColor Yellow

try {
    $tcp = New-Object System.Net.Sockets.TcpClient($smtpHost, $smtpPort)
    $stream = $tcp.GetStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $writer = New-Object System.IO.StreamWriter($stream)
    $writer.AutoFlush = $true
    $reader.BaseStream.ReadTimeout = 15000

    # --- STEP 1: Read Banner ---
    Write-Host "[STEP 1] Banner:" -ForegroundColor Yellow
    $line = $reader.ReadLine()
    Write-Host "  S: $line" -ForegroundColor Gray

    # --- STEP 2: EHLO ---
    Write-Host "`n[STEP 2] EHLO:" -ForegroundColor Yellow
    $writer.WriteLine("EHLO mailally.test")
    do {
        $line = $reader.ReadLine()
        Write-Host "  S: $line" -ForegroundColor Gray
    } while ($line.Length -ge 4 -and $line[3] -eq '-')

    # --- STEP 3: STARTTLS ---
    Write-Host "`n[STEP 3] STARTTLS:" -ForegroundColor Yellow
    $writer.WriteLine("STARTTLS")
    $line = $reader.ReadLine()
    Write-Host "  S: $line" -ForegroundColor Gray
    $code = $line.Substring(0, 3)
    Write-Host "  >> Code: $code" -ForegroundColor White

    if ($code -ne "220") {
        Write-Host "  >> STARTTLS rejected!" -ForegroundColor Red
        $tcp.Close()
        exit 1
    }
    Write-Host "  >> STARTTLS accepted. Upgrading..." -ForegroundColor Green

    # --- STEP 4: TLS Upgrade ---
    $sslStream = New-Object System.Net.Security.SslStream($stream, $false)
    $sslStream.AuthenticateAsClient($smtpHost)
    Write-Host "  >> TLS: $($sslStream.SslProtocol)" -ForegroundColor Green

    $reader = New-Object System.IO.StreamReader($sslStream)
    $writer = New-Object System.IO.StreamWriter($sslStream)
    $writer.AutoFlush = $true

    # --- STEP 5: EHLO post-TLS ---
    Write-Host "`n[STEP 4] EHLO (post-TLS):" -ForegroundColor Yellow
    $writer.WriteLine("EHLO mailally.test")
    do {
        $line = $reader.ReadLine()
        Write-Host "  S: $line" -ForegroundColor Gray
    } while ($line.Length -ge 4 -and $line[3] -eq '-')

    # --- STEP 6: AUTH LOGIN ---
    Write-Host "`n[STEP 5] AUTH LOGIN:" -ForegroundColor Yellow
    $writer.WriteLine("AUTH LOGIN")
    $line = $reader.ReadLine()
    Write-Host "  S: $line" -ForegroundColor Gray
    $code = $line.Substring(0, 3)

    if ($code -eq "334") {
        # Send username
        $userB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpUsername))
        Write-Host "  C: [username base64]" -ForegroundColor DarkGray
        $writer.WriteLine($userB64)
        $line = $reader.ReadLine()
        Write-Host "  S: $line" -ForegroundColor Gray

        # Send password
        $passB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpPassword))
        Write-Host "  C: [password base64]" -ForegroundColor DarkGray
        $writer.WriteLine($passB64)
        $line = $reader.ReadLine()
        Write-Host "  S: $line" -ForegroundColor Gray
        $code = $line.Substring(0, 3)

        if ($code -eq "235") {
            Write-Host "`n=============================================" -ForegroundColor Green
            Write-Host "  AUTH RESULT: 235 AUTHENTICATED" -ForegroundColor Green
            Write-Host "=============================================" -ForegroundColor Green

            # --- STEP 7: Send test email ---
            Write-Host "`n[STEP 6] Sending test email..." -ForegroundColor Yellow

            $writer.WriteLine("MAIL FROM:<$senderEmail>")
            $line = $reader.ReadLine()
            Write-Host "  S: $line" -ForegroundColor Gray

            $writer.WriteLine("RCPT TO:<$recipientEmail>")
            $line = $reader.ReadLine()
            Write-Host "  S: $line" -ForegroundColor Gray

            $writer.WriteLine("DATA")
            $line = $reader.ReadLine()
            Write-Host "  S: $line" -ForegroundColor Gray

            $ts = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $writer.WriteLine("From: MailAlly <$senderEmail>")
            $writer.WriteLine("To: $recipientEmail")
            $writer.WriteLine("Subject: MailAlly Gate 1 PASS - SMTP Verified $ts")
            $writer.WriteLine("Content-Type: text/html")
            $writer.WriteLine("")
            $writer.WriteLine("<h1 style='color:green'>Gate 1 PASSED</h1><p>SMTP auth verified at $ts</p>")
            $writer.WriteLine(".")
            $line = $reader.ReadLine()
            Write-Host "  S: $line" -ForegroundColor Gray
            $code = $line.Substring(0, 3)

            if ($code -eq "250") {
                Write-Host "`n=============================================" -ForegroundColor Green
                Write-Host "  GATE 1 FINAL: PASS" -ForegroundColor Green
                Write-Host "  Auth:  235 OK" -ForegroundColor Green
                Write-Host "  Send:  250 Accepted" -ForegroundColor Green
                Write-Host "  Check: $recipientEmail" -ForegroundColor Green
                Write-Host "=============================================" -ForegroundColor Green
            } else {
                Write-Host "  Send failed ($code): $line" -ForegroundColor Yellow
            }

        } elseif ($code -eq "535") {
            Write-Host "`n=============================================" -ForegroundColor Red
            Write-Host "  GATE 1: FAIL - 535 AUTH FAILED" -ForegroundColor Red
            Write-Host "  $line" -ForegroundColor Red
            Write-Host "=============================================" -ForegroundColor Red
            Write-Host "  Credentials INVALID. Regenerate in Brevo." -ForegroundColor Yellow
        } else {
            Write-Host "  Unexpected ($code): $line" -ForegroundColor Yellow
        }
    } else {
        Write-Host "  AUTH rejected ($code): $line" -ForegroundColor Red
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
Write-Host "  Gate 1 (v5) Complete" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
