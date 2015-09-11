#!/bin/bash
#
# Unix shell script for running calculator plugin API examples.
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
echo "Running ElementalAnalyserPluginExample ..."
echo "java ElementalAnalyserPluginExample example_mols.sdf"
java ElementalAnalyserPluginExample example_mols.sdf

echo
echo "Running PluginExample ..."
echo "java PluginExample example_mols.sdf > results.sdf"
java PluginExample example_mols.sdf > results.sdf
echo "Results are written to results.sdf file."
