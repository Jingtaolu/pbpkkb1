@ECHO OFF
REM
REM WIN/DOS SHELL SCRIPT FOR RUNNING CONCURRENT PLUGIN TEST APPLICATIONS
REM

SET W=%CLASSPATH%
SET CLASSPATH=..\beans\examples.jar;..\..\lib\MarvinBeans.jar;%CLASSPATH%

echo .
echo . 'logD at pH=5.2:'
echo . 'java logDPluginApplication 5.2 test.smiles'
java logDPluginApplication 5.2 test.smiles

echo .
echo . '2 strongest acidic and basic pKa values:'
echo . 'java pKaPluginApplication 2 test.smiles'
java pKaPluginApplication 2 test.smiles

echo .
echo . 'Tautomer with minimal logD:'
echo . 'java TautomerizationPluginApplication 16 test.smiles'
java TautomerizationPluginApplication 16 test.smiles

REM Environment variables are global in DOS, so we must restore the
REM original CLASSPATH.
SET CLASSPATH=%W%
