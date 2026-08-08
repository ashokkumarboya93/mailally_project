Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "Starting MailAlly Full Application (Backend + Frontend)" -ForegroundColor Cyan
Write-Host "=======================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

Write-Host "1. Launching Spring Boot Backend (Port 8081)..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList '/k', 'cd mailally-backend && mvnw.cmd spring-boot:run'

Write-Host "2. Launching React Vite Frontend (Port 5173)..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList '/k', 'cd mailally-frontend && npm run dev'

Write-Host "3. Opening Browser at http://localhost:5173 in 5 seconds..." -ForegroundColor Yellow
Start-Sleep -Seconds 5
Start-Process "http://localhost:5173"

Write-Host "=======================================================" -ForegroundColor Green
Write-Host "Both Backend & Frontend launched in separate windows!" -ForegroundColor Green
Write-Host "Backend URL:  http://localhost:8081" -ForegroundColor Green
Write-Host "Frontend URL: http://localhost:5173" -ForegroundColor Green
Write-Host "=======================================================" -ForegroundColor Green
