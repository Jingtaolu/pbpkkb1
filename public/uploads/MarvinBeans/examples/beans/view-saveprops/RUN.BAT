@ECHO OFF
REM
REM WIN/DOS SHELL SCRIPT FOR MViewSaveProperties
REM

SET W=%CLASSPATH%
SET CLASSPATH=..\examples.jar;..\..\..\lib\MarvinBeans.jar;%CLASSPATH%

java MViewSaveProperties %1 %2 %3 %4 %5 %6 %7 %8 %9

REM Environment variables are global in DOS, so we must restore the
REM original CLASSPATH.
SET CLASSPATH=%W%
