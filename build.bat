
@echo off
echo Cleaning build directory...
if exist build rmdir /s /q build
mkdir build
mkdir build\classes
mkdir build\test-results

echo Compiling main source...
javac -cp "lib/*" -d build/classes src/main/java/main/*.java
if %errorlevel% neq 0 (
    echo Main compilation failed!
    exit /b 1
)

echo Compiling test runners...
javac -cp "lib/*;build/classes" -d build/classes src/test/java/runners/*.java
if %errorlevel% neq 0 (
    echo Test runner compilation failed!
    exit /b 1
)

echo Compiling step definitions...
javac -cp "lib/*;build/classes" -d build/classes src/test/java/steps/*.java
if %errorlevel% neq 0 (
    echo Step definitions compilation failed!
    exit /b 1
)

echo Copying test resources...
xcopy /E /I /Y src\test\resources build\test\resources

echo Running Cucumber tests with manual paths...
java ^
    -cp "lib/*;build/classes;build/test/resources" ^
    io.cucumber.core.cli.Main ^
    --glue src.test.java.steps ^
    --glue src.test.java.runners.CucumberTestRunner ^
    --plugin pretty ^
    --plugin html:build/test-results/cucumber-report.html ^
    --plugin json:build/test-results/cucumber-report.json ^
    build/test/resources/features

echo Build complete!