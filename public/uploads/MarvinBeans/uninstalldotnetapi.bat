@ECHO OFF

REM param1: output dir for log file, working dir is the default, give the directory without enclosing quotation marks

if "%*"=="" (
	msiexec /x JChem_NET_API.Marvin.x86.msi /quiet /L*V "uninstalldotnetapi.log"
) else (
	if exist "%*." GOTO AFTER_MKDIR
	mkdir "%*"

	:AFTER_MKDIR
	msiexec /x JChem_NET_API.Marvin.x86.msi /quiet /L*V "%*\uninstalldotnetapi.log"
)
