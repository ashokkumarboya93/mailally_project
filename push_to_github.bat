@echo off
echo =======================================================
echo Pushing MailAlly Entire Codebase (Backend + Frontend)
echo Target Repo: https://github.com/ashokkumarboya93/mailally_project.git
echo =======================================================

cd /d "%~dp0"

echo 1. Removing inner frontend .git folder if present to ensure frontend files are tracked...
if exist "mailally-frontend\.git" rmdir /s /q "mailally-frontend\.git"

echo 2. Removing submodule cached reference if any...
git rm --cached mailally-frontend 2>nul

echo 3. Setting remote repository URL...
git remote set-url origin https://github.com/ashokkumarboya93/mailally_project.git

echo 4. Staging all files (backend + frontend)...
git add .

echo 5. Committing changes...
git commit -m "Push full MailAlly codebase: Backend, Frontend, and Documentation"

echo 6. Ensuring main branch...
git branch -M main

echo 7. Pushing to GitHub...
git push -u origin main

echo =======================================================
echo Finished! Check repository at https://github.com/ashokkumarboya93/mailally_project.git
echo =======================================================
pause
