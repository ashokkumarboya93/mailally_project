@echo off
REM =============================================
REM MailAlly Brevo Email Test Script
REM =============================================
REM This script tests the Brevo API integration
REM and the full MailAlly app email sending flow
REM =============================================

echo =============================================
echo   MailAlly - Brevo Email Integration Test
echo =============================================
echo.

REM Step 1: Direct Brevo API Test (no app needed)
echo [STEP 1] Testing Brevo API directly...
echo.

curl -s -w "\nHTTP_STATUS:%%{http_code}" ^
  -X POST "https://api.brevo.com/v3/smtp/email" ^
  -H "accept: application/json" ^
  -H "api-key: YOUR_BREVO_API_KEY" ^
  -H "content-type: application/json" ^
  -d "{\"sender\":{\"email\":\"info@marcamor.com\",\"name\":\"MailAlly\"},\"to\":[{\"email\":\"ashokkumarboya93@gmail.com\",\"name\":\"Ashok\"}],\"subject\":\"MailAlly Test - Brevo Integration Verified\",\"htmlContent\":\"<html><body><div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'><div style='background:linear-gradient(135deg,#667eea 0%%,#764ba2 100%%);padding:30px;border-radius:12px;text-align:center'><h1 style='color:white;margin:0'>MailAlly</h1><p style='color:rgba(255,255,255,0.9);margin:10px 0 0'>Email Engine Test</p></div><div style='padding:30px;background:#f8f9fa;border-radius:0 0 12px 12px'><h2 style='color:#333'>Brevo Integration Working!</h2><p style='color:#555;line-height:1.6'>This test email confirms that your MailAlly backend is successfully connected to the Brevo email service.</p><div style='background:#d4edda;border:1px solid #c3e6cb;border-radius:8px;padding:15px;margin:20px 0'><p style='color:#155724;margin:0'><b>Provider:</b> Brevo API v3</p><p style='color:#155724;margin:5px 0 0'><b>Sender:</b> info@marcamor.com</p><p style='color:#155724;margin:5px 0 0'><b>Status:</b> DKIM + DMARC Verified</p></div></div></div></body></html>\"}"

echo.
echo.
echo [STEP 1] Complete. Check ashokkumarboya93@gmail.com inbox!
echo.
echo =============================================
echo.

REM Step 2: Test via MailAlly App (requires app running on port 8081)
echo [STEP 2] Testing via MailAlly App API...
echo.

REM Login first to get JWT token
echo [2a] Logging in as admin@mailally.com ...
echo.

for /f "tokens=*" %%a in ('curl -s -X POST "http://localhost:8081/api/v1/auth/login" -H "Content-Type: application/json" -d "{\"email\":\"admin@mailally.com\",\"password\":\"password123\"}"') do set LOGIN_RESPONSE=%%a

echo Login Response: %LOGIN_RESPONSE%
echo.

REM Extract token (rough extraction - check manually if needed)
echo [2b] Sending test email via MailAlly /api/v1/emails/send ...
echo.

REM Note: You may need to manually copy the JWT token from the login response above
REM and paste it in place of YOUR_JWT_TOKEN below

echo IMPORTANT: Copy the "token" value from login response above
echo and run this curl manually:
echo.
echo curl -X POST "http://localhost:8081/api/v1/emails/send" ^
echo   -H "Content-Type: application/json" ^
echo   -H "Authorization: Bearer YOUR_JWT_TOKEN" ^
echo   -d "{\"recipientEmail\":\"ashokkumarboya93@gmail.com\",\"recipientName\":\"Ashok\",\"subject\":\"MailAlly App Test - Brevo Provider\",\"htmlBody\":\"<h1>Hello from MailAlly!</h1><p>This email was sent through the MailAlly app using Brevo provider.</p>\",\"provider\":\"BREVO\"}"
echo.
echo =============================================

pause
