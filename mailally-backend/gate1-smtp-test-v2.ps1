# =============================================
# Gate 1 (v2): Standalone SMTP Test
# =============================================
# Uses PowerShell Send-MailMessage (different TLS handling than SmtpClient)
# AND a raw SMTP protocol test to diagnose auth negotiation
#
# Run: powershell -ExecutionPolicy Bypass -File gate1-smtp-test-v2.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1 (v2): SMTP Authentication Diagnosis" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

$smtpHost     = "smtp-relay.brevo.com"
$smtpPort     = 587
$smtpUsername  = "info@marcamor.com"
$smtpPassword  = "YOUR_BREVO_SMTP_PASSWORD"
$senderEmail  = "info@marcamor.com"
$recipientEmail = "ashokkumarboya93@gmail.com"

Write-Host "`n--- Configuration ---" -ForegroundColor Yellow
Write-Host "Host:     $smtpHost" -ForegroundColor White
Write-Host "Port:     $smtpPort" -ForegroundColor White
Write-Host "Username: $smtpUsername" -ForegroundColor White
Write-Host "TLS:      STARTTLS" -ForegroundColor White

# ----------------------------------------
# TEST A: TCP Connectivity Check
# ----------------------------------------
Write-Host "`n[TEST A] TCP Connectivity to ${smtpHost}:${smtpPort}..." -ForegroundColor Yellow
try {
    $tcp = New-Object System.Net.Sockets.TcpClient
    $tcp.Connect($smtpHost, $smtpPort)
    if ($tcp.Connected) {
        Write-Host "[TEST A] PASS - TCP connection established" -ForegroundColor Green
        $stream = $tcp.GetStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $reader.BaseStream.ReadTimeout = 5000
        $banner = $reader.ReadLine()
        Write-Host "  Server Banner: $banner" -ForegroundColor White
        $tcp.Close()
    }
} catch {
    Write-Host "[TEST A] FAIL - Cannot connect: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "  ACTION: Check network/firewall. Do NOT modify MailAlly." -ForegroundColor Yellow
    exit 1
}

# ----------------------------------------
# TEST B: Send-MailMessage (PowerShell native)
# ----------------------------------------
Write-Host "`n[TEST B] Send-MailMessage with STARTTLS..." -ForegroundColor Yellow
try {
    $secPassword = ConvertTo-SecureString $smtpPassword -AsPlainText -Force
    $credential = New-Object System.Management.Automation.PSCredential($smtpUsername, $secPassword)

    Send-MailMessage `
        -SmtpServer $smtpHost `
        -Port $smtpPort `
        -UseSsl `
        -Credential $credential `
        -From $senderEmail `
        -To $recipientEmail `
        -Subject "MailAlly Gate 1 - SMTP Test $(Get-Date -Format 'HH:mm:ss')" `
        -Body "Gate 1 SMTP authentication test from MailAlly recovery plan." `
        -ErrorAction Stop

    Write-Host "[TEST B] PASS - Email sent successfully!" -ForegroundColor Green
    Write-Host "  Check inbox: $recipientEmail" -ForegroundColor Green
} catch {
    Write-Host "[TEST B] FAIL" -ForegroundColor Red
    Write-Host "  Exception: $($_.Exception.GetType().FullName)" -ForegroundColor Red
    Write-Host "  Message:   $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.InnerException) {
        Write-Host "  Inner:     $($_.Exception.InnerException.Message)" -ForegroundColor Red
    }
}

# ----------------------------------------
# TEST C: .NET MailKit-style raw SMTP (explicit STARTTLS)
# ----------------------------------------
Write-Host "`n[TEST C] Raw SMTP EHLO + STARTTLS + AUTH LOGIN test..." -ForegroundColor Yellow
try {
    $tcp = New-Object System.Net.Sockets.TcpClient($smtpHost, $smtpPort)
    $stream = $tcp.GetStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $writer = New-Object System.IO.StreamWriter($stream)
    $writer.AutoFlush = $true
    $reader.BaseStream.ReadTimeout = 10000

    # Read banner
    $banner = $reader.ReadLine()
    Write-Host "  S: $banner" -ForegroundColor Gray

    # EHLO
    $writer.WriteLine("EHLO mailally.test")
    Start-Sleep -Milliseconds 500
    while ($stream.DataAvailable) {
        $line = $reader.ReadLine()
        Write-Host "  S: $line" -ForegroundColor Gray
    }

    # STARTTLS
    $writer.WriteLine("STARTTLS")
    Start-Sleep -Milliseconds 500
    $tlsResponse = $reader.ReadLine()
    Write-Host "  S: $tlsResponse" -ForegroundColor Gray

    if ($tlsResponse -like "220*") {
        Write-Host "  STARTTLS accepted. Upgrading to TLS..." -ForegroundColor Green

        # Upgrade to TLS
        $sslStream = New-Object System.Net.Security.SslStream($stream, $false)
        $sslStream.AuthenticateAsClient($smtpHost)
        Write-Host "  TLS Version: $($sslStream.SslProtocol)" -ForegroundColor Green

        $reader = New-Object System.IO.StreamReader($sslStream)
        $writer = New-Object System.IO.StreamWriter($sslStream)
        $writer.AutoFlush = $true
        $reader.BaseStream.ReadTimeout = 10000

        # EHLO again over TLS
        $writer.WriteLine("EHLO mailally.test")
        Start-Sleep -Milliseconds 500
        $authSupported = $false
        while ($stream.DataAvailable -or $sslStream.CanRead) {
            try {
                $line = $reader.ReadLine()
                Write-Host "  S: $line" -ForegroundColor Gray
                if ($line -match "AUTH") { $authSupported = $true }
                if ($line -match "^250 ") { break }
            } catch { break }
        }

        if ($authSupported) {
            Write-Host "  AUTH mechanisms supported by server." -ForegroundColor Green
        }

        # AUTH LOGIN
        $writer.WriteLine("AUTH LOGIN")
        Start-Sleep -Milliseconds 500
        $authResp = $reader.ReadLine()
        Write-Host "  S: $authResp" -ForegroundColor Gray

        if ($authResp -like "334*") {
            # Send username (base64)
            $userB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpUsername))
            $writer.WriteLine($userB64)
            Start-Sleep -Milliseconds 500
            $userResp = $reader.ReadLine()
            Write-Host "  S: $userResp" -ForegroundColor Gray

            # Send password (base64)
            $passB64 = [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes($smtpPassword))
            $writer.WriteLine($passB64)
            Start-Sleep -Milliseconds 500
            $passResp = $reader.ReadLine()
            Write-Host "  S: $passResp" -ForegroundColor Gray

            if ($passResp -like "235*") {
                Write-Host "`n  =============================================" -ForegroundColor Green
                Write-Host "  AUTH LOGIN RESULT: PASS (235 Authenticated)" -ForegroundColor Green
                Write-Host "  =============================================" -ForegroundColor Green
            } elseif ($passResp -like "535*") {
                Write-Host "`n  =============================================" -ForegroundColor Red
                Write-Host "  AUTH LOGIN RESULT: FAIL (535 Auth Failed)" -ForegroundColor Red
                Write-Host "  SMTP Response: $passResp" -ForegroundColor Red
                Write-Host "  =============================================" -ForegroundColor Red
                Write-Host "  ACTION: SMTP credentials are invalid." -ForegroundColor Yellow
                Write-Host "  Go to Brevo Dashboard and regenerate SMTP key." -ForegroundColor Yellow
            } else {
                Write-Host "`n  AUTH LOGIN RESULT: UNEXPECTED ($passResp)" -ForegroundColor Yellow
            }
        }

        $writer.WriteLine("QUIT")
    } else {
        Write-Host "  STARTTLS rejected: $tlsResponse" -ForegroundColor Red
    }

    $tcp.Close()
} catch {
    Write-Host "[TEST C] Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1 (v2) Diagnosis Complete" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
