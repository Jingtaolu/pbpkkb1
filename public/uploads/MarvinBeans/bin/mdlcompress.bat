@ECHO OFF
SETLOCAL

REM # HEAP_LIMIT: the maximum amount of memory to be allocated by the
REM # Java Virtual Machine (JVM) in megabytes.
REM # The -Xmx(size) command line parameter of Java can also overwrite it.
set HEAP_LIMIT=512

GOTO MAIN

:getJavaPath
set configfile=%~dp0%java.ini
IF EXIST "%configfile%". (
for /f "tokens=2 delims==" %%a in ('%SystemRoot%\system32\find.exe "Java Path=" "%configfile%"') do (
set JVMPATH="%%a%bin\java"
)
) ELSE (
  set JVMPATH=java
)
goto:eof

:MAIN
call:getJavaPath
SET CLASSPATH="%~dp0..\lib\MarvinBeans.jar"
SET XMX_MODE=-Xmx%HEAP_LIMIT%m

REM Filter JVM parameters
set JVMPARAM=
set JVM_X_PARAM=

:START_JVMPARAM
if [%1]==[] GOTO END_JVMPARAM
:: Remove quotes
set nqparam=%1
   SET nqparam=###%nqparam%###
   SET nqparam=%nqparam:"###=%
   SET nqparam=%nqparam:###"=%
   SET nqparam=%nqparam:###=%
REM check first two characters of the parameter
set pam=%nqparam%
set hpam=%pam:~0,2%
if "%hpam%"=="-X" (
		if "%pam:~0,4%"=="-XMX" (
			REM reset default settings
			set XMX_MODE=
		)
        set JVMPARAM=%JVMPARAM% %1
        set JVM_X_PARAM=%JVM_X_PARAM% %1
        SHIFT
        GOTO START_JVMPARAM
)
if %1==-client (
    set JVMPARAM=%JVMPARAM% %1
    SHIFT
    GOTO START_JVMPARAM
)
if %1==-server (
    set JVMPARAM=%JVMPARAM% %1
    SHIFT
    GOTO START_PARAM
)
:END_JVMPARAM

if not [%XMX_MODE%]==[] (
    set JVMPARAM=%JVMPARAM% %XMX_MODE%
)

shift
set P1=%0
shift
set P2=%0
shift
set P3=%0
shift
set P4=%0
shift
set P5=%0
shift
set P6=%0
shift
set P7=%0
shift
set P8=%0
shift
set P9=%0
shift
set P10=%0
shift
set P11=%0
shift
set P12=%0
shift
set P13=%0
shift
set P14=%0
shift
set P15=%0
shift
set P16=%0
shift
set P17=%0
shift
set P18=%0
shift
set P19=%0
shift
set P20=%0
shift
set P21=%0
shift
set P22=%0
shift
set P23=%0
shift
set P24=%0
shift
set P25=%0
shift
set P26=%0
shift
set P27=%0
shift
set P28=%0
shift
set P29=%0
shift
set P30=%0
shift
set P31=%0
shift
set P32=%0
shift
set P33=%0
shift
set P34=%0
shift
set P35=%0
shift
set P36=%0
shift
set P37=%0
shift
set P38=%0
shift
set P39=%0
shift
set P40=%0
shift
set ENDTEST=%0
if not _%ENDTEST%==_ goto Error

REM ---------------- End of Marvin batch header -----------------------------



%JVMPATH% %JVMPARAM% -classpath %CLASSPATH% chemaxon.formats.MdlCompressor %*%

REM ----------------- Begin of Marvin batch footer --------------------------

goto End

:Error
echo Error: Too many parameters. You are probably not using quotes for parameters
echo that contain more words
:End

ENDLOCAL

