@echo off
setlocal

:: ================================
:: Startup script for saddlesync.jar
:: ================================

:: Check if Java is installed and available in PATH
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not found in PATH.
    echo Please install Java from https://www.java.com and try again.
    pause
    exit /b
)

:: Get the directory of this batch file
set JARDIR=%~dp0

:: Set the jar file name
set JARFILE=writersblockcms.jar

echo [INFO] Launching %JARFILE% from folder: %JARDIR%
cd /d "%JARDIR%"

:: Run the jar file with memory settings and log output
java -Xms256m -Xmx1024m -jar "%JARFILE%" %* >> "%JARDIR%log.txt" 2>&1

:: Check if the program exited with an error
if errorlevel 1 (
    echo [ERROR] %JARFILE% terminated with an error. Check log.txt for details.
    pause
) else (
    echo [INFO] %JARFILE% finished successfully.
    pause
)

endlocal
exit /b