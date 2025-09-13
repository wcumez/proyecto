@echo off
set JFLEX_JAR=jflex-1.8.2.jar
if not exist %JFLEX_JAR% (
  echo No se encontro %JFLEX_JAR%. Descargue JFlex y colóquelo aquí o ajuste la variable.
  pause
  exit /b 1
)
java -jar %JFLEX_JAR% -d . proyecto.flex
javac *.java
set INPUT=%1
if "%INPUT%"=="" set INPUT=ejemplo_input.txt
java Main %INPUT%
