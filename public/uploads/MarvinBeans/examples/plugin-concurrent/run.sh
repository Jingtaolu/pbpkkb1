#!/bin/bash
#
# Unix shell script for running concurrent plugin test applications.
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

echo
echo "logD at pH=5.2:"
echo "java logDPluginApplication 5.2 test.smiles"
java logDPluginApplication 5.2 test.smiles

echo
echo "2 strongest acidic and basic pKa values:"
echo "java pKaPluginApplication 2 test.smiles"
java pKaPluginApplication 2 test.smiles

echo
echo "Tautomer with minimal logD:"
echo "java TautomerizationPluginApplication 16 test.smiles"
java TautomerizationPluginApplication 16 test.smiles
