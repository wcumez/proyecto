import java.io.*;
import java.util.*;

public class TokenCounter {
    private static Map<String, Map<String, Integer>> categoryCounts = new LinkedHashMap<>();
    private static PrintWriter out = null;

    // Inicializar salida
    public static void init(String outputPath) throws IOException {
        categoryCounts.clear();
        out = new PrintWriter(new FileWriter(outputPath));
    }

    // Guardar token con categoría y lexema
    public static void addToken(String type, String lexeme) {
        categoryCounts.putIfAbsent(type, new LinkedHashMap<>());
        Map<String,Integer> counts = categoryCounts.get(type);
        counts.put(lexeme, counts.getOrDefault(lexeme, 0) + 1);
    }

    public static void writeOutput() {
        if (out == null) {
            System.err.println("TokenCounter: output not initialized.");
            return;
        }

        // Solo mostrar tabla final
        out.println("=== Resumen de Conteo por Palabra ===");
        out.printf("%-25s %-20s %-10s%n", "Elemento", "Palabra", "Conteo");

        for (Map.Entry<String, Map<String,Integer>> e : categoryCounts.entrySet()) {
            String categoria = mapCategory(e.getKey());
            for (Map.Entry<String,Integer> sub : e.getValue().entrySet()) {
                out.printf("%-25s %-20s %-10d%n", categoria, sub.getKey(), sub.getValue());
            }
        }

        out.flush();
        out.close();
    }

    // Mapear tipos técnicos a nombres más amigables (como en tu tabla)
    private static String mapCategory(String type) {
        switch (type) {
            case "KEYWORD": return "Palabras Reservadas";
            case "IDENTIFIER": return "Variables";
            case "NUMBER": return "Números";
            case "STRING": return "Cadenas";
            case "STRING_MULTILINE": return "Cadenas Multilínea";
            case "OPEN_PAREN": case "CLOSE_PAREN":
            case "OPEN_BRACE": case "CLOSE_BRACE":
            case "OPEN_BRACKET": case "CLOSE_BRACKET":
                return "Signos de Agrupación";
            case "PLUS": case "MINUS": case "MUL": case "DIV": case "MOD":
            case "ASSIGN": case "EQ": case "NEQ": case "GT": case "LT": case "GE": case "LE":
                return "Operadores Aritméticos";
            default: return type; // fallback
        }
    }

    // Versión auxiliar si solo se pasa el tipo
    public static void addToken(String type) {
        addToken(type, type);
    }
}
