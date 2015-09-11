@ECHO OFF

REM param1: output dir for log file, working dir is the default, give the directory without enclosing quotation marks

if "%*"=="" (
	msiexec /i JChem_NET_API.Marvin.x86.msi /quiet /L*V "installdotnetapi.log"
) else (
	if exist "%*." GOTO AFTER_MKDIR
	mkdir "%*"

	:AFTER_MKDIR
	msiexec /i JChem_NET_API.Marvin.x86.msi /quiet /L*V "%*\installdotnetapi.log"
)
