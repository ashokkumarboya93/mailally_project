Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "Cleaning .metadata and Pushing Updates to GitHub" -ForegroundColor Cyan
Write-Host "=======================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

Write-Host "1. Untracking .metadata folder..." -ForegroundColor Yellow
git rm -r --cached .metadata 2>$null

Write-Host "2. Staging updated .gitignore..." -ForegroundColor Yellow
git add .

Write-Host "3. Committing changes..." -ForegroundColor Yellow
git commit -m "Remove .metadata from git tracking and ignore IDE metadata"

Write-Host "4. Pushing to GitHub main..." -ForegroundColor Yellow
git push origin main

Write-Host "=======================================================" -ForegroundColor Green
Write-Host "Done! .metadata removed from git tracking." -ForegroundColor Green
