@echo off
setlocal

echo Cleaning build directory...
if exist build rmdir /s /q build
mkdir build
mkdir build\classes
mkdir build\test-results
mkdir build\test\resources

echo Compiling main source...
javac -cp "lib/cucumber_lib/*" -source 8 -target 8 -d build/classes src/main/java/main/*.java
if %errorlevel% neq 0 (
    echo Main compilation failed!
    exit /b 1
)

echo Compiling test runners...
javac -cp "lib/cucumber_lib/*;build/classes" -source 8 -target 8 -d build/classes src/test/java/runners/*.java
if %errorlevel% neq 0 (
    echo Test runner compilation failed!
    exit /b 1
)

echo Compiling step definitions...
javac -cp "lib/cucumber_lib/*;build/classes" -source 8 -target 8 -d build/classes src/test/java/steps/*.java
if %errorlevel% neq 0 (
    echo Step definitions compilation failed!
    exit /b 1
)

echo Copying test resources...
xcopy /E /I /Y src\test\resources build\test\resources

echo Creating test-results directory if not exists...
if not exist build\test-results mkdir build\test-results

echo Debugging: Listing all compiled classes...
if exist build\classes (
    dir /s build\classes
) else (
    echo ERROR: build\classes directory does not exist!
)

echo Debugging: Checking specific paths...
if exist build\classes\main (
    echo Found build\classes\main directory
    dir build\classes\main
) else (
    echo WARNING: build\classes\main directory does not exist!
)

if exist build\classes\main\Book.class (
    echo Found Book.class in build\classes\main
) else (
    echo WARNING: Book.class not found in build\classes\main
)

echo Instrumenting classes with Cobertura...
if not exist build\classes\main (
    echo ERROR: build\classes\main directory does not exist! Cannot instrument.
    echo Please check your compilation step.
    exit /b 1
)

java -cp lib\cobertura_lib\cobertura-2.1.1-jar-with-dependencies.jar net.sourceforge.cobertura.instrument.InstrumentMain ^
  --datafile build\cobertura.ser ^
  --destination build\instrumented ^
  --ignore-trivial ^
  build\classes\main
if %errorlevel% neq 0 (
    echo Cobertura instrumentation failed!
    exit /b 1
)

echo Running Cucumber tests with Cobertura-instrumented classes...
"C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot\bin\java" ^
  -Dnet.sourceforge.cobertura.datafile=build\cobertura.ser ^
  -cp "lib/cucumber_lib/*;lib/cobertura_lib/*;build/instrumented;build/classes;build/test/resources" ^
  io.cucumber.core.cli.Main ^
  --glue steps ^
  --plugin pretty ^
  --plugin html:build/test-results/cucumber-report.html ^
  --plugin json:build/test-results/cucumber-report.json ^
  build/test/resources/features
if %errorlevel% neq 0 (
    echo Test execution failed!
)

echo Generating Cobertura HTML report...
java -cp lib\cobertura_lib\cobertura-2.1.1-jar-with-dependencies.jar net.sourceforge.cobertura.reporting.ReportMain ^
  --datafile build\cobertura.ser ^
  --format html ^
  --destination build\cobertura-report ^
  --source src\main\java
if %errorlevel% neq 0 (
    echo Failed to generate Cobertura report!
    exit /b 1
)

echo Build and test complete!
start build\test-results\cucumber-report.html
start build\cobertura-report\index.html

endlocal