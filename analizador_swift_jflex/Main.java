import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
    
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = "Salida.txt";

        TokenCounter.init(outputPath);

        // Pasar el archivo completo al lexer
        FileReader fr = new FileReader(inputPath);
        Lexer lexer = new Lexer(fr);
        lexer.yylex(); // Aquí se procesan todos los tokens del archivo

        fr.close();

        // Guardar resultados en Salida.txt
        TokenCounter.writeOutput();
        System.out.println("Análisis completado. Archivo generado: " + outputPath);
    }
}
