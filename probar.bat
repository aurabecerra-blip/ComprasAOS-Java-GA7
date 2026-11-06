@echo off
cd /d "%~dp0"
call compilar.bat
if errorlevel 1 exit /b 1
javac -encoding UTF-8 -cp "build;lib/*" -d build test\com\aos\compras\PruebaCrud.java
if errorlevel 1 exit /b 1
java -cp "build;lib/*" com.aos.compras.PruebaCrud
pause
