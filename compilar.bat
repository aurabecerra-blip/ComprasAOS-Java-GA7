@echo off
cd /d "%~dp0"
if not exist build mkdir build
javac -encoding UTF-8 -d build src\com\aos\compras\*.java
if errorlevel 1 goto error
jar --create --file ComprasProveedores.jar -C build .
if errorlevel 1 goto error
echo Compilacion finalizada. Ejecute iniciar.bat
exit /b 0
:error
echo No fue posible compilar. Requiere JDK 17 o superior.
pause
exit /b 1
