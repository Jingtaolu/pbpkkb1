@ECHO OFF
REM
REM WIN/DOS SHELL SCRIPT FOR MyExporter and MyImporter
REM

SET W=%CLASSPATH%
SET CLASSPATH=..\beans\examples.jar;..\..\lib\MarvinBeans.jar;%CLASSPATH%

java MyExporter
java MyImporter test.myf

REM Environment variables are global in DOS, so we must restore the
REM original CLASSPATH.
SET CLASSPATH=%W%
