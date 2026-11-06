@echo off
cd /d "%~dp0"
set "COMPRAS_JAVA=java"
if exist "%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe" set "COMPRAS_JAVA=%ProgramFiles%\Android\Android Studio\jbr\bin\java.exe"
"%COMPRAS_JAVA%" -cp "ComprasProveedores.jar;lib/*" com.aos.compras.Aplicacion
if errorlevel 1 pause
