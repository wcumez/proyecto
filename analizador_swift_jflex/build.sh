#!/bin/bash
JFLEX_JAR="./jflex-1.8.2.jar"
if [ ! -f "$JFLEX_JAR" ]; then
  echo "No se encontró $JFLEX_JAR. Descarga JFlex y colócalo aquí o ajusta la ruta."
  exit 1
fi
# generate lexer
java -jar "$JFLEX_JAR" -d . proyecto.flex
# compile
javac *.java
# run (take first arg as input file)
INPUT=${1:-ejemplo_input.txt}
java Main "$INPUT"
