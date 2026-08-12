@echo off
echo =======================================================
echo Starting MailAlly Spring Boot Backend (Port 8081)...
echo =======================================================
cd /d "%~dp0mailally-backend"
mvnw.cmd spring-boot:run
pause
