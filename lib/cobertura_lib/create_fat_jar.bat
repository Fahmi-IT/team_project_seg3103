@echo off
REM Manual Fat JAR Creation Script for Cobertura (Windows)
REM This script combines multiple JAR files into a single fat JAR

REM Set variables
set OUTPUT_JAR=cobertura-2.1.1-jar-with-dependencies.jar
set TEMP_DIR=temp_jar_extraction
set JAR_FOLDER=.

echo Creating fat JAR: %OUTPUT_JAR%
echo Current directory: %CD%

REM Check if JAR files exist
echo Checking for JAR files...
dir /b "%JAR_FOLDER%\*.jar" >nul 2>&1
if errorlevel 1 (
    echo ERROR: No JAR files found in %JAR_FOLDER%
    echo Please make sure you have JAR files in the current directory
    pause
    exit /b 1
)

REM List JAR files found
echo JAR files found:
for %%f in ("%JAR_FOLDER%\*.jar") do echo   %%~nxf

REM Create temporary directory
if exist %TEMP_DIR% rmdir /s /q %TEMP_DIR%
mkdir %TEMP_DIR%
pushd %TEMP_DIR%

REM Extract all JAR files
echo Extracting JAR files...
for %%f in ("..\*.jar") do (
    echo Extracting: %%~nxf
    jar xf "%%f"
    if errorlevel 1 (
        echo ERROR: Failed to extract %%~nxf
        popd
        pause
        exit /b 1
    )
)

REM Check if extraction worked
echo Checking extracted content...
dir /b >nul 2>&1
if errorlevel 1 (
    echo ERROR: No files were extracted
    popd
    pause
    exit /b 1
)

echo Extracted files:
dir /b

REM Remove duplicate META-INF files that might conflict
echo Cleaning up META-INF conflicts...
if exist META-INF\*.SF del /q META-INF\*.SF
if exist META-INF\*.DSA del /q META-INF\*.DSA
if exist META-INF\*.RSA del /q META-INF\*.RSA

REM Create the fat JAR
echo Creating fat JAR...
jar cf "..\%OUTPUT_JAR%" .
if errorlevel 1 (
    echo ERROR: Failed to create fat JAR
    popd
    pause
    exit /b 1
)

REM Clean up
popd
rmdir /s /q %TEMP_DIR%

REM Verify the JAR was created
if exist "%OUTPUT_JAR%" (
    echo Fat JAR created successfully: %OUTPUT_JAR%
    echo File size: 
    dir "%OUTPUT_JAR%" | findstr "%OUTPUT_JAR%"
) else (
    echo ERROR: Fat JAR was not created
)

REM Optional: Set main class in manifest (uncomment if needed)
REM echo Setting main class...
REM jar ufe "%OUTPUT_JAR%" net.sourceforge.cobertura.instrument.Main

pause