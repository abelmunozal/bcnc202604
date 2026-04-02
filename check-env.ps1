# PowerShell script to verify that Gradle is using the embedded JDK 21
# Author: Senior Software Architect
# Purpose: Ensure build environment uses ./jdk21 instead of system JDK

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  JDK Environment Verification Script  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Get the project root directory
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$expectedJdkPath = Join-Path $projectRoot "jdk21"

Write-Host "Project Root: $projectRoot" -ForegroundColor Yellow
Write-Host "Expected JDK: $expectedJdkPath" -ForegroundColor Yellow
Write-Host ""

# Check if jdk21 folder exists
if (-Not (Test-Path $expectedJdkPath)) {
    Write-Host "[ERROR] JDK 21 folder not found at: $expectedJdkPath" -ForegroundColor Red
    Write-Host "Please extract the Eclipse Temurin JDK 21 ZIP into the ./jdk21 folder" -ForegroundColor Red
    exit 1
}

Write-Host "[OK] JDK 21 folder exists" -ForegroundColor Green
Write-Host ""

# Run gradlew -v and capture output
Write-Host "Running: ./gradlew -v" -ForegroundColor Cyan
Write-Host "----------------------------------------" -ForegroundColor Gray

$gradleOutput = & "$projectRoot\gradlew.bat" -v 2>&1 | Out-String

Write-Host $gradleOutput

# Parse JVM location from output
$jvmLine = $gradleOutput -split "`n" | Where-Object { $_ -match "JVM:" } | Select-Object -First 1

if ($jvmLine) {
    Write-Host "----------------------------------------" -ForegroundColor Gray
    Write-Host "JVM Detection: $jvmLine" -ForegroundColor Yellow
    
    # Normalize paths for comparison (convert to absolute and resolve)
    $normalizedExpected = (Resolve-Path $expectedJdkPath).Path.ToLower()
    
    # Check if JVM path contains our jdk21 folder
    if ($jvmLine -match [regex]::Escape($normalizedExpected) -or $jvmLine -match "jdk21") {
        Write-Host ""
        Write-Host "[SUCCESS] Gradle is using the embedded JDK 21!" -ForegroundColor Green
        Write-Host "Build environment is correctly configured." -ForegroundColor Green
        exit 0
    } else {
        Write-Host ""
        Write-Host "[WARNING] Gradle may be using a different JDK!" -ForegroundColor Red
        Write-Host "Expected path to contain: $normalizedExpected" -ForegroundColor Red
        Write-Host "Please verify gradle.properties configuration." -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host ""
    Write-Host "[ERROR] Could not detect JVM information from gradle -v output" -ForegroundColor Red
    exit 1
}
