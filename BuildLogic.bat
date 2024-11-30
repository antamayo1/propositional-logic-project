@echo off
cls
:: Show the installation process...
echo ---------------------------------------------------------------------
echo BuildLogic.bat set-ups LOGIC in your machine with the following steps
echo ---------------------------------------------------------------------
echo 1. Check and creation of \bin directory. 
echo ---------------------------------------------------------------------
if not exist "%CD%\bin" (
  echo Message: \bin directory does not exists.
  mkdir "%CD%\bin"
  echo Message: \bin directory created.
) else (
  echo Message: \bin directory already exists.
)
echo.
echo Directory Creation Successful!
echo ---------------------------------------------------------------------
echo 2. Compiling Java Files to the \bin directory.
echo ---------------------------------------------------------------------
echo Message: Compiling Java files
javac -d "%CD%\bin" *.java
if errorlevel 1 (
  echo.
  echo Compilation failed!
  echo Press Enter to continue...
  pause >nul
  exit /b 1
)
echo.
echo Compilation Successful!
echo ---------------------------------------------------------------------
echo 3. Creating LOGIC command
echo ---------------------------------------------------------------------
echo Message: Creating batch file as connection to LOGIC
(
  echo @echo off
  echo cd /d "%~dp0"
  echo java -cp "%CD%\bin" Main %%1
) > "%CD%\bin\LOGIC.bat"
echo.
echo LOGIC command created successfully!
echo ---------------------------------------------------------------------
echo 4. Setting up LOGIC to PATH
echo ---------------------------------------------------------------------
set "NEW_DIR=%CD%\bin"
for /f "tokens=2*" %%A in ('reg query "HKEY_CURRENT_USER\Environment" /v PATH 2^>nul') do set "USER_PATH=%%B"
echo %USER_PATH% | find /i "%NEW_DIR%" >nul
if not errorlevel 1 (
  echo Message: LOGIC is already installed in PATH.
  echo.
  echo The operation completed successfully.
  echo ---------------------------------------------------------------------
  echo Installation Successful!
  echo ---------------------------------------------------------------------
  echo Press Enter to exit...
  pause >nul
  exit /b 1
) 
echo Message: Adding LOGIC directory to user PATH variable.
echo.
set "UPDATED_PATH=%USER_PATH%;%NEW_DIR%"
reg add "HKEY_CURRENT_USER\Environment" /v PATH /t REG_EXPAND_SZ /d "%UPDATED_PATH%" /f
echo ---------------------------------------------------------------------
echo Installation Successful!
echo ---------------------------------------------------------------------
echo Please restart your computer to apply changes in PATH.
echo Press Enter to exit...
pause >nul