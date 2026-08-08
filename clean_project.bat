@echo off
echo Cleaning temporary files and organizing repository...

cd /d "%~dp0"

if exist "MailAlly Backend - mvnw.cmd  spring-bootrun.txt" del /f /q "MailAlly Backend - mvnw.cmd  spring-bootrun.txt"
if exist "MailAlly-mvnw.cmd  spring-bootrun.txt" del /f /q "MailAlly-mvnw.cmd  spring-bootrun.txt"
if exist "DEBUG_REPORT.md" del /f /q "DEBUG_REPORT.md"
if exist "PROJECT_ANALYSIS_AND_REVIEW.txt" del /f /q "PROJECT_ANALYSIS_AND_REVIEW.txt"
if exist "blueprint.md" del /f /q "blueprint.md"
if exist "email_engine_analysis_report.md" del /f /q "email_engine_analysis_report.md"
if exist "MailAlly_PPT_Presentation_10_Slides.md" del /f /q "MailAlly_PPT_Presentation_10_Slides.md"
if exist "Contacts.csv" del /f /q "Contacts.csv"
if exist "Contacts.xlsx" del /f /q "Contacts.xlsx"
if exist "marcamor_employee_list.csv" del /f /q "marcamor_employee_list.csv"
if exist "run_backend.bat" del /f /q "run_backend.bat"
if exist "run_frontend.bat" del /f /q "run_frontend.bat"
if exist "push_to_github.ps1" del /f /q "push_to_github.ps1"

if exist "mailally-backend\spring-boot-run.out.log" del /f /q "mailally-backend\spring-boot-run.out.log"
if exist "mailally-backend\spring-boot-run.err.log" del /f /q "mailally-backend\spring-boot-run.err.log"
if exist "mailally-backend\gate1-smtp-test-v2.ps1" del /f /q "mailally-backend\gate1-smtp-test-v2.ps1"
if exist "mailally-backend\gate1-smtp-test-v3.ps1" del /f /q "mailally-backend\gate1-smtp-test-v3.ps1"
if exist "mailally-backend\gate1-smtp-test-v4.ps1" del /f /q "mailally-backend\gate1-smtp-test-v4.ps1"
if exist "mailally-backend\gate1-smtp-test-v5.ps1" del /f /q "mailally-backend\gate1-smtp-test-v5.ps1"

if exist "mailally-frontend\copy_hero.js" del /f /q "mailally-frontend\copy_hero.js"

echo Cleanup completed successfully!
