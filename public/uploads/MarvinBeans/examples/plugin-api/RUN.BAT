@ECHO OFF
REM
REM WIN/DOS SHELL SCRIPT FOR RUNNING CALCULATOR PLUGIN API EXAMPLES
REM

SET W=%CLASSPATH%
SET CLASSPATH=..\beans\examples.jar;..\..\lib\MarvinBeans.jar;%CLASSPATH%

echo
echo 'Running ElementalAnalyserPluginExample ...'
echo 'java ElementalAnalyserPluginExample example_mols.sdf
java ElementalAnalyserPluginExample example_mols.sdf

echo
echo 'Running PluginExample ...'
echo 'java PluginExample example_mols.sdf > results.sdf'
java PluginExample example_mols.sdf > results.sdf
echo 'Results are written to results.sdf file.'

REM Environment variables are global in DOS, so we must restore the
REM original CLASSPATH.
SET CLASSPATH=%W%
