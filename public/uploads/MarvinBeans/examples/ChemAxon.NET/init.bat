SET PROCESSOR_TYPE=x86

REM Configuration parameters default to .NET 1.1 values:
SET PROC_TYPE=
SET MARVIN_JNB_PROXY_LIB=marvin15.8.10.0_JnbProxies_1.1.dll
SET DEFINES=/define:CSVER_1_1

csc.exe | %SYSTEMROOT%\system32\find.exe /n "Framework version 1.1"
REM Adjust configuration parameters if necessary:
IF %ERRORLEVEL% EQU 0 GOTO bye
SET PROC_TYPE=/platform:%PROCESSOR_TYPE%
SET MARVIN_JNB_PROXY_LIB=marvin15.8.10.0_JnbProxies_2.0.dll
SET DEFINES=

IF "%1" == "" GOTO bye
SET MARVIN_JNB_PROXY_LIB="%1"

:bye
