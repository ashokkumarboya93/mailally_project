Write-Host "=======================================================" -ForegroundColor Cyan
Write-Host "Pushing Fix for Auto Table Creation to GitHub" -ForegroundColor Cyan
Write-Host "=======================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

git add .
git commit -m "Set spring.jpa.hibernate.ddl-auto=update for automatic schema initialization on new machines"
git push origin main

Write-Host "=======================================================" -ForegroundColor Green
Write-Host "Done! Fix pushed to GitHub." -ForegroundColor Green
