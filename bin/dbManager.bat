@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  myHearthstone startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line
set CLASSPATH="%APP_HOME%\lib\hsqldb-2.6.0.jar" 
set RUN_CLASS="org.hsqldb.util.DatabaseManagerSwing"
set DRIVER_OPT="--driver org.hsqldb.jdbc.JDBCDriver"
set URL_OPT="--url jdbc:hsqldb:file:%APP_HOME%\data\MyHearthstoneDB"
set USER_OPT="--user SA"

@rem Execute myHearthstone
"%JAVA_EXE%" -cp %CLASSPATH% %RUN_CLASS% "%DRIVER_OPT%" "%URL_OPT%" "%USER_OPT%"

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable MY_HEARTHSTONE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%MY_HEARTHSTONE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega

