Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "Pushing MailAlly Codebase & Startup Scripts to GitHub" -ForegroundColor Cyan
Write-Host "=======================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

git add .
git commit -m "Add start_app scripts for one-click launching of backend and frontend"
git push origin main

Write-Host "=======================================================" -ForegroundColor Green
Write-Host "Done! Code & Scripts pushed to GitHub." -ForegroundColor Green
