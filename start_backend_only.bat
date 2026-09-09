@echo off
echo =======================================================
echo Starting MailAlly Spring Boot Backend (Port 8081)...
echo =======================================================
if exist "C:\Program Files\Java\jdk-21.0.10" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
    set "PATH=C:\Program Files\Java\jdk-21.0.10\bin;%PATH%"
)
cd /d "%~dp0mailally-backend"
mvnw.cmd spring-boot:run
pause
