#!/bin/bash
#
# Unix shell script for MyExporter and MyImporter
#
# Also works in MSWINDOWS+Cygwin if you run it with the command "sh run.sh".
#

if [ -z "$OSTYPE" ]; then
	which cygpath 1>/dev/null 2>/dev/null
	if [ $? = 0 ]; then
		OSTYPE=cygwin
	fi
fi

case "$OSTYPE" in
cygwin*)
	dir=`dirname $0`
        dir=`cygpath.exe -w $dir`
	CLASSPATH="$dir\\..\\beans\\examples.jar;$dir\\..\\..\\lib\\MarvinBeans.jar;$CLASSPATH"
	;;
*)
	dir=`/usr/bin/dirname $0`
	CLASSPATH="$dir/../beans/examples.jar:$dir/../../lib/MarvinBeans.jar:$CLASSPATH"
	;;
esac
export CLASSPATH

java MyExporter
java MyImporter test.myf
