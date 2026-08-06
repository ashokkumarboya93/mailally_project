# =============================================
# Gate 1: Standalone SMTP Authentication Test
# =============================================
# Tests SMTP connectivity and authentication
# against smtp-relay.brevo.com:587 independently
# of the MailAlly application.
#
# Run: powershell -ExecutionPolicy Bypass -File gate1-smtp-test.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Gate 1: Standalone SMTP Provider Test" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

# Configuration (from application.properties)
$smtpHost     = "smtp-relay.brevo.com"
$smtpPort     = 587
$smtpUsername  = "info@marcamor.com"
$smtpPassword  = "YOUR_BREVO_SMTP_PASSWORD"
$senderEmail  = "info@marcamor.com"
$senderName   = "MailAlly Gate 1 Test"
$recipientEmail = "ashokkumarboya93@gmail.com"
$recipientName  = "Ashok"

Write-Host "`n--- SMTP Configuration ---" -ForegroundColor Yellow
Write-Host "Host:     $smtpHost" -ForegroundColor White
Write-Host "Port:     $smtpPort" -ForegroundColor White
Write-Host "Username: $smtpUsername" -ForegroundColor White
Write-Host "Password: $($smtpPassword.Substring(0,15))..." -ForegroundColor White
Write-Host "TLS:      Enabled (STARTTLS)" -ForegroundColor White
Write-Host "Sender:   $senderEmail" -ForegroundColor White
Write-Host "To:       $recipientEmail" -ForegroundColor White
Write-Host ""

try {
    Write-Host "[1/4] Creating SMTP client..." -ForegroundColor Yellow
    $smtp = New-Object System.Net.Mail.SmtpClient($smtpHost, $smtpPort)
    $smtp.EnableSsl = $true
    $smtp.Credentials = New-Object System.Net.NetworkCredential($smtpUsername, $smtpPassword)
    $smtp.Timeout = 30000

    Write-Host "[2/4] Building test email..." -ForegroundColor Yellow
    $mail = New-Object System.Net.Mail.MailMessage
    $mail.From = New-Object System.Net.Mail.MailAddress($senderEmail, $senderName)
    $mail.To.Add((New-Object System.Net.Mail.MailAddress($recipientEmail, $recipientName)))
    $mail.Subject = "MailAlly Gate 1 - SMTP Authentication Verified - $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
    $mail.IsBodyHtml = $true
    $mail.Body = @"
<html><body>
<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
  <div style="background: linear-gradient(135deg, #28a745 0%, #20c997 100%); padding: 30px; border-radius: 12px; text-align: center;">
    <h1 style="color: white; margin: 0;">&#10004; Gate 1 PASSED</h1>
    <p style="color: rgba(255,255,255,0.9); margin: 10px 0 0;">SMTP Authentication Verified</p>
  </div>
  <div style="padding: 30px; background: #f8f9fa; border-radius: 0 0 12px 12px;">
    <h2 style="color: #333;">MailAlly SMTP Test Successful</h2>
    <p style="color: #555;">This email was sent directly via SMTP relay, bypassing the MailAlly application, to prove SMTP authentication works.</p>
    <div style="background: #d4edda; border: 1px solid #c3e6cb; border-radius: 8px; padding: 15px; margin: 20px 0;">
      <p style="color: #155724; margin: 0;"><strong>Host:</strong> $smtpHost</p>
      <p style="color: #155724; margin: 5px 0 0;"><strong>Port:</strong> $smtpPort</p>
      <p style="color: #155724; margin: 5px 0 0;"><strong>Auth:</strong> STARTTLS + Credentials</p>
      <p style="color: #155724; margin: 5px 0 0;"><strong>Timestamp:</strong> $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')</p>
    </div>
  </div>
</div>
</body></html>
"@

    Write-Host "[3/4] Connecting and authenticating with $smtpHost`:$smtpPort..." -ForegroundColor Yellow
    $smtp.Send($mail)

    Write-Host "[4/4] COMPLETE" -ForegroundColor Green
    Write-Host ""
    Write-Host "=============================================" -ForegroundColor Green
    Write-Host "  GATE 1 RESULT: PASS" -ForegroundColor Green
    Write-Host "=============================================" -ForegroundColor Green
    Write-Host "  SMTP Authentication: Successful" -ForegroundColor Green
    Write-Host "  Email Dispatched:    Yes" -ForegroundColor Green
    Write-Host "  Check Inbox:         $recipientEmail" -ForegroundColor Green
    Write-Host "=============================================" -ForegroundColor Green
}
catch {
    Write-Host ""
    Write-Host "=============================================" -ForegroundColor Red
    Write-Host "  GATE 1 RESULT: FAIL" -ForegroundColor Red
    Write-Host "=============================================" -ForegroundColor Red
    Write-Host "  Exception Type:    $($_.Exception.GetType().FullName)" -ForegroundColor Red
    Write-Host "  Error Message:     $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.InnerException) {
        Write-Host "  Inner Exception:   $($_.Exception.InnerException.Message)" -ForegroundColor Red
    }
    Write-Host "  SMTP Host:         $smtpHost" -ForegroundColor Yellow
    Write-Host "  SMTP Port:         $smtpPort" -ForegroundColor Yellow
    Write-Host "  Username:          $smtpUsername" -ForegroundColor Yellow
    Write-Host "=============================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "  ACTION: Fix SMTP credentials before proceeding." -ForegroundColor Yellow
    Write-Host "  Do NOT modify MailAlly code." -ForegroundColor Yellow
}
finally {
    if ($smtp) { $smtp.Dispose() }
    if ($mail) { $mail.Dispose() }
}
