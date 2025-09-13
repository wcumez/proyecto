# Analizador léxico de Swift con JFlex

Este proyecto implementa un analizador léxico para el lenguaje **Swift** usando **JFlex**.
El analizador clasifica palabras reservadas, identificadores, números, strings, símbolos,
comentarios y errores.

## Archivos incluidos
- `proyecto.flex` — especificación JFlex para Swift.
- `Main.java` — programa principal.
- `TokenCounter.java` — clase auxiliar para el conteo de tokens y reporte.
- `ejemplo_input.txt` — archivo con código Swift de ejemplo.
- `build.sh` / `build.bat` — scripts de compilación y ejecución.

## Uso
1. Descargar JFlex (`jflex-1.8.2.jar`) y colocarlo en la carpeta del proyecto.
2. Linux/macOS:
   ```bash
   ./build.sh ejemplo_input.txt
   ```
3. Windows:
   ```cmd
   build.bat ejemplo_input.txt
   ```
Esto generará `Salida.txt` con el análisis.
