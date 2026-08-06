Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "Resetting Git History to Remove Old Secret Commit" -ForegroundColor Cyan
Write-Host "Target Repo: https://github.com/ashokkumarboya93/mailally_project.git" -ForegroundColor Cyan
Write-Host "=======================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

if (Test-Path "mailally-frontend\.git") {
    Write-Host "1. Removing nested .git directory in mailally-frontend..." -ForegroundColor Yellow
    Remove-Item -Recurse -Force "mailally-frontend\.git"
}

Write-Host "2. Creating a clean orphan branch to erase past secret history..." -ForegroundColor Yellow
git checkout --orphan fresh-main

Write-Host "3. Staging all sanitized project files..." -ForegroundColor Yellow
git add .

Write-Host "4. Creating clean initial commit..." -ForegroundColor Yellow
git commit -m "Initial commit: MailAlly full codebase (backend + frontend)"

Write-Host "5. Setting main branch..." -ForegroundColor Yellow
git branch -M main

Write-Host "6. Setting origin URL..." -ForegroundColor Yellow
git remote set-url origin https://github.com/ashokkumarboya93/mailally_project.git

Write-Host "7. Pushing fresh clean codebase to origin main..." -ForegroundColor Yellow
git push -f -u origin main

Write-Host "=======================================================" -ForegroundColor Green
Write-Host "SUCCESS! Code pushed to https://github.com/ashokkumarboya93/mailally_project.git" -ForegroundColor Green
