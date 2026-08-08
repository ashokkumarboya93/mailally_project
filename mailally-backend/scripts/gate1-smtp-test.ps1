# =============================================
# MailAlly - SMTP Diagnostic Test Script
# =============================================
# Run: powershell -ExecutionPolicy Bypass -File scripts/gate1-smtp-test.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  MailAlly: Raw SMTP Protocol Test" -ForegroundColor Cyan
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
        $userB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpUsername))
        $writer.WriteLine($userB64)
        $line = $reader.ReadLine()

        $passB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpPassword))
        $writer.WriteLine($passB64)
        $line = $reader.ReadLine()
        $code = $line.Substring(0, 3)

        if ($code -eq "235") {
            Write-Host "`n=============================================" -ForegroundColor Green
            Write-Host "  AUTH RESULT: 235 AUTHENTICATED" -ForegroundColor Green
            Write-Host "=============================================" -ForegroundColor Green
        } else {
            Write-Host "  AUTH failed ($code): $line" -ForegroundColor Red
        }
    }

    $writer.WriteLine("QUIT")
    $tcp.Close()

} catch {
    Write-Host "`n[ERROR] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Diagnostic Test Complete" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
