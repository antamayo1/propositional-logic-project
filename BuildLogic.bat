@echo off
set BIN_DIR=bin
if not exist "%BIN_DIR%" (
  mkdir "%BIN_DIR%"
)

echo Compiling Java files...
javac -d "%BIN_DIR%" *.java
if errorlevel 1 (
  echo Compilation failed!
  echo Press Enter to continue...
  pause >nul
  exit /b 1
)
echo Compilation successful!
echo Creating LOGIC command...
(
  echo @echo off
  echo cd /d "%~dp0"
  echo java -cp "%BIN_DIR%" Main
) > "%BIN_DIR%\LOGIC.bat"
echo LOGIC command created successfully!

:: Step 4: Add the bin directory to the PATH
setx PATH "%~dp0%BIN_DIR%;%PATH%"
if errorlevel 1 (
    echo Failed to update PATH. You may need to add it manually.
    echo Press Enter to continue...
    pause >nul
    exit /b 1
)
echo PATH updated. You can now use "LOGIC sentence.pl" globally.

:: Done
echo Setup complete. Use the command: LOGIC sentence.pl
echo Press Enter to continue...
pause >nul