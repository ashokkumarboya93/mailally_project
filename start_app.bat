@echo off
echo =======================================================
echo Starting MailAlly Full Application (Backend + Frontend)
echo =======================================================

cd /d "%~dp0"

echo 1. Launching Spring Boot Backend (Port 8081)...
start "MailAlly Backend" cmd /k "cd mailally-backend && mvnw.cmd spring-boot:run"

echo 2. Launching React Vite Frontend (Port 5173)...
start "MailAlly Frontend" cmd /k "cd mailally-frontend && npm run dev"

echo 3. Opening Browser at http://localhost:5173...
timeout /t 5 >nul
start http://localhost:5173

echo =======================================================
echo Both Backend and Frontend are starting in separate windows!
echo Backend:  http://localhost:8081
echo Frontend: http://localhost:5173
echo =======================================================
pause
