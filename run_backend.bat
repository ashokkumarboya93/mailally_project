@echo off
if exist "C:\Program Files\Java\jdk-21.0.10" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
    set "PATH=C:\Program Files\Java\jdk-21.0.10\bin;%PATH%"
)
cd /d "d:\JDBCSW\MailAlly\mailally-backend\mailally-backend"
call mvnw.cmd spring-boot:run
