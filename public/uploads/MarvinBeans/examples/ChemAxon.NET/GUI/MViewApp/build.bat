@echo off

call ..\..\init.bat %%1
IF %ERRORLEVEL% GTR 1 GOTO bye

SET MARVIN_JNB_PROXY_LIB_PATH=%MARVIN_JNB_PROXY_LIB%

IF EXIST "JNBShare.dll" GOTO compile
echo "JNBShare.dll is not in the current directory"
GOTO fail

IF EXIST "%MARVIN_JNB_PROXY_LIB_PATH%" GOTO compile
echo "The file %MARVIN_JNB_PROXY_LIB_PATH% does not exist"
SET ERRORLEVEL=1
GOTO bye

:compile
@echo on
csc /r:JNBShare.dll /r:%MARVIN_JNB_PROXY_LIB_PATH% %PROC_TYPE% /debug /target:winexe /out:MViewApp.exe Form1.cs MViewControl.cs Program.cs
@echo off

:fail
