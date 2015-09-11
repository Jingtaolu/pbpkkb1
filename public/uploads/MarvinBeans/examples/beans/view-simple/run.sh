#!/bin/bash
#
# Unix shell script for MarvinView Simple Bean Example
#
# It can also be used with MSWINDOWS+Cygwin if it is started
# with the command "sh run.sh".
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
	CLASSPATH="$dir\\..\\examples.jar;$dir\\..\\..\\..\\lib\\MarvinBeans.jar;$CLASSPATH"
	;;
*)
	dir=`/usr/bin/dirname $0`
	CLASSPATH="$dir/../examples.jar:$dir/../../../lib/MarvinBeans.jar:$CLASSPATH"
	;;
esac
export CLASSPATH

exec java ViewSimple $@
