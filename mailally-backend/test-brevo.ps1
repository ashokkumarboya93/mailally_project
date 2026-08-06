# =============================================
# MailAlly - Brevo Email Integration Test
# =============================================
# Run: powershell -ExecutionPolicy Bypass -File test-brevo.ps1
# =============================================

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  MailAlly - Brevo Email Integration Test" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan

# ----------------------------------------
# STEP 1: Direct Brevo API Test
# ----------------------------------------
Write-Host "`n[STEP 1] Sending test email via Brevo API directly..." -ForegroundColor Yellow

$headers = @{
    "api-key"      = "YOUR_BREVO_API_KEY"
    "accept"       = "application/json"
    "content-type" = "application/json"
}

$emailBody = @{
    sender = @{
        email = "info@marcamor.com"
        name  = "MailAlly"
    }
    to = @(
        @{
            email = "ashokkumarboya93@gmail.com"
            name  = "Ashok"
        }
    )
    subject     = "MailAlly Test - Brevo Integration Verified"
    htmlContent = @"
<html><body>
<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
  <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px; border-radius: 12px; text-align: center;">
    <h1 style="color: white; margin: 0;">MailAlly</h1>
    <p style="color: rgba(255,255,255,0.9); margin: 10px 0 0;">Email Engine Test</p>
  </div>
  <div style="padding: 30px; background: #f8f9fa; border-radius: 0 0 12px 12px;">
    <h2 style="color: #333;">Brevo Integration Working!</h2>
    <p style="color: #555; line-height: 1.6;">This test email confirms that your MailAlly backend is successfully connected to the Brevo email service.</p>
    <div style="background: #d4edda; border: 1px solid #c3e6cb; border-radius: 8px; padding: 15px; margin: 20px 0;">
      <p style="color: #155724; margin: 0;"><strong>Provider:</strong> Brevo (Sendinblue) API v3</p>
      <p style="color: #155724; margin: 5px 0 0;"><strong>Sender:</strong> info@marcamor.com</p>
      <p style="color: #155724; margin: 5px 0 0;"><strong>Status:</strong> DKIM + DMARC Verified</p>
      <p style="color: #155724; margin: 5px 0 0;"><strong>Timestamp:</strong> $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')</p>
    </div>
    <p style="color: #888; font-size: 12px; text-align: center; margin-top: 30px;">Sent via MailAlly Email Engine</p>
  </div>
</div>
</body></html>
"@
} | ConvertTo-Json -Depth 5

try {
    $response = Invoke-RestMethod -Uri "https://api.brevo.com/v3/smtp/email" -Method Post -Headers $headers -Body $emailBody
    Write-Host "[SUCCESS] Email sent via Brevo API!" -ForegroundColor Green
    Write-Host "Message ID: $($response.messageId)" -ForegroundColor Green
    Write-Host "Check inbox: ashokkumarboya93@gmail.com" -ForegroundColor Green
}
catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    Write-Host "[ERROR] Brevo API returned HTTP $statusCode" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    try {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errBody = $reader.ReadToEnd()
        Write-Host "Response: $errBody" -ForegroundColor Yellow
    } catch {}
}

# ----------------------------------------
# STEP 2: Test via MailAlly App (if running)
# ----------------------------------------
Write-Host "`n[STEP 2] Testing via MailAlly App (localhost:8081)..." -ForegroundColor Yellow

try {
    # Login
    $loginBody = @{ email = "admin@mailally.com"; password = "password123" } | ConvertTo-Json
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/auth/login" -Method Post -ContentType "application/json" -Body $loginBody
    
    if ($loginResponse.success -and $loginResponse.data.token) {
        $token = $loginResponse.data.token
        Write-Host "[LOGIN] Authenticated as admin@mailally.com" -ForegroundColor Green
        
        # Send test email
        $sendHeaders = @{
            "Authorization" = "Bearer $token"
            "Content-Type"  = "application/json"
        }
        $sendBody = @{
            recipientEmail = "ashokkumarboya93@gmail.com"
            recipientName  = "Ashok"
            subject        = "MailAlly App Test - Brevo Provider Active"
            htmlBody       = "<html><body><h1 style='color:#667eea;'>Hello from MailAlly App!</h1><p>This email was sent through the MailAlly backend API using Brevo as the active provider.</p><p><strong>Timestamp:</strong> $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')</p></body></html>"
            provider       = "BREVO"
        } | ConvertTo-Json
        
        $sendResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/emails/send" -Method Post -Headers $sendHeaders -Body $sendBody
        Write-Host "[SUCCESS] Email sent via MailAlly App!" -ForegroundColor Green
        Write-Host "Response: $($sendResponse | ConvertTo-Json -Depth 3)" -ForegroundColor White
    }
    else {
        Write-Host "[WARN] Login succeeded but no token found in response" -ForegroundColor Yellow
        Write-Host "Response: $($loginResponse | ConvertTo-Json -Depth 3)" -ForegroundColor White
    }
}
catch {
    if ($_.Exception.Message -like "*Unable to connect*" -or $_.Exception.Message -like "*connection*refused*") {
        Write-Host "[SKIP] App not running on localhost:8081. Start the app first:" -ForegroundColor Yellow
        Write-Host "  cd d:\JDBCSW\MailAlly\mailally-backend\mailally-backend" -ForegroundColor White
        Write-Host "  .\mvnw.cmd spring-boot:run" -ForegroundColor White
    }
    else {
        Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "  Test Complete!" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
