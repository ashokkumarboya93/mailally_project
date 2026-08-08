@echo off
echo =======================================================
echo Pushing MailAlly Entire Codebase (Backend + Frontend)
echo Target Repo: https://github.com/ashokkumarboya93/mailally_project.git
echo =======================================================

cd /d "%~dp0"

echo 1. Cleaning project log files...
call clean_project.bat 2>nul

echo 2. Removing inner frontend .git folder if present to ensure tracking...
if exist "mailally-frontend\.git" rmdir /s /q "mailally-frontend\.git"

echo 3. Removing submodule cached reference if any...
git rm --cached mailally-frontend 2>nul

echo 4. Setting remote repository URL...
git remote set-url origin https://github.com/ashokkumarboya93/mailally_project.git

echo 5. Staging all files (backend + frontend + docs)...
git add .

echo 6. Committing changes...
git commit -m "Clean project repository, structure documentation and organize launch scripts"

echo 7. Ensuring main branch...
git branch -M main

echo 8. Pushing to GitHub...
git push -u origin main

echo =======================================================
echo Finished! Check repository at https://github.com/ashokkumarboya93/mailally_project.git
echo =======================================================
pause
