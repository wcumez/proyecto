import java.io.*;
import java.util.*;

/**
 * Lexer.java - Simplified JFlex-style lexer for Swift (handwritten, readable).
 */
public class Lexer {
    private final String input;
    private int pos;
    private final int len;

    // position tracking (approximate)
    public int yyline = 0;
    public int yycolumn = 0;

    private static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(
        "if","else","for","while","func","let","var","return","class","struct","enum",
        "protocol","extension","import","in","do","try","catch","guard","switch","case",
        "default","break","continue","true","false","nil","where","defer","associatedtype"
    ));

    public Lexer(Reader r) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(r);
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        this.input = sb.toString();
        this.len = input.length();
        this.pos = 0;
        this.yyline = 0;
        this.yycolumn = 0;
    }

    private boolean startsWith(String s) {
        return input.startsWith(s, pos);
    }

    private void advance(int n) {
        for (int i = 0; i < n && pos < len; i++) {
            if (input.charAt(pos) == '\n') {
                yyline++;
                yycolumn = 0;
            } else {
                yycolumn++;
            }
            pos++;
        }
    }

    public int yylex() throws IOException {
        while (pos < len) {
            char c = input.charAt(pos);

            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                advance(1);
                continue;
            }

            if (startsWith("//")) {
                while (pos < len && input.charAt(pos) != '\n') pos++;
                continue;
            }

            if (startsWith("/*")) {
                int end = input.indexOf("*/", pos + 2);
                if (end >= 0) pos = end + 2;
                else pos = len;
                continue;
            }

            if (startsWith("\"\"\"")) {
                int start = pos;
                pos += 3;
                while (pos < len) {
                    if (startsWith("\"\"\"")) {
                        pos += 3;
                        break;
                    } else {
                        if (input.charAt(pos) == '\n') { yyline++; yycolumn = 0; } else { yycolumn++; }
                        pos++;
                    }
                }
                String lexeme = input.substring(start, Math.min(pos, len));
                TokenCounter.addToken("STRING_MULTILINE", lexeme);
                continue;
            }

            if (c == '"') {
                pos++; yycolumn++;
                StringBuilder sb = new StringBuilder();
                sb.append('"');
                while (pos < len) {
                    char ch = input.charAt(pos++);
                    sb.append(ch);
                    if (ch == '\\') { if (pos < len) { sb.append(input.charAt(pos++)); } }
                    else if (ch == '"') { break; }
                    else if (ch == '\n') { yyline++; yycolumn = 0; }
                    else { yycolumn++; }
                }
                TokenCounter.addToken("STRING", sb.toString());
                continue;
            }

            if (pos + 1 < len) {
                String two = input.substring(pos, pos + 2);
                if (two.equals("==")) { advance(2); TokenCounter.addToken("EQ", "=="); continue; }
                if (two.equals("!=")) { advance(2); TokenCounter.addToken("NEQ", "!="); continue; }
                if (two.equals(">=")) { advance(2); TokenCounter.addToken("GE", ">="); continue; }
                if (two.equals("<=")) { advance(2); TokenCounter.addToken("LE", "<="); continue; }
                if (two.equals("+=")) { advance(2); TokenCounter.addToken("PLUS_ASSIGN", "+="); continue; }
                if (two.equals("-=")) { advance(2); TokenCounter.addToken("MINUS_ASSIGN", "-="); continue; }
                if (two.equals("->")) { advance(2); TokenCounter.addToken("ARROW", "->"); continue; }
            }

            switch (c) {
                case '{': advance(1); TokenCounter.addToken("OPEN_BRACE", "{"); continue;
                case '}': advance(1); TokenCounter.addToken("CLOSE_BRACE", "}"); continue;
                case '(': advance(1); TokenCounter.addToken("OPEN_PAREN", "("); continue;
                case ')': advance(1); TokenCounter.addToken("CLOSE_PAREN", ")"); continue;
                case '[': advance(1); TokenCounter.addToken("OPEN_BRACKET", "["); continue;
                case ']': advance(1); TokenCounter.addToken("CLOSE_BRACKET", "]"); continue;
                case ';': advance(1); TokenCounter.addToken("SEMICOLON", ";"); continue;
                case ',': advance(1); TokenCounter.addToken("COMMA", ","); continue;
                case ':': advance(1); TokenCounter.addToken("COLON", ":"); continue;
                case '+': advance(1); TokenCounter.addToken("PLUS", "+"); continue;
                case '-': advance(1); TokenCounter.addToken("MINUS", "-"); continue;
                case '*': advance(1); TokenCounter.addToken("MUL", "*"); continue;
                case '/': advance(1); TokenCounter.addToken("DIV", "/"); continue;
                case '%': advance(1); TokenCounter.addToken("MOD", "%"); continue;
                case '=': advance(1); TokenCounter.addToken("ASSIGN", "="); continue;
                case '>': advance(1); TokenCounter.addToken("GT", ">"); continue;
                case '<': advance(1); TokenCounter.addToken("LT", "<"); continue;
                case '!': advance(1); TokenCounter.addToken("NOT", "!"); continue;
                default: break;
            }

            if (Character.isDigit(c)) {
                int start = pos;
                if (c == '0' && pos + 1 < len) {
                    char c1 = Character.toLowerCase(input.charAt(pos + 1));
                    if (c1 == 'x') { pos += 2; while (pos < len && isHexDigitOrUnderscore(input.charAt(pos))) pos++; TokenCounter.addToken("NUMBER", input.substring(start, pos)); continue; }
                    if (c1 == 'b') { pos += 2; while (pos < len && (input.charAt(pos) == '0' || input.charAt(pos) == '1' || input.charAt(pos) == '_')) pos++; TokenCounter.addToken("NUMBER", input.substring(start, pos)); continue; }
                    if (c1 == 'o') { pos += 2; while (pos < len && "01234567_".indexOf(input.charAt(pos)) >= 0) pos++; TokenCounter.addToken("NUMBER", input.substring(start, pos)); continue; }
                }
                while (pos < len && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '_')) pos++;
                if (pos < len && input.charAt(pos) == '.') { pos++; while (pos < len && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '_')) pos++; }
                if (pos < len && (input.charAt(pos) == 'e' || input.charAt(pos) == 'E')) { pos++; if (pos < len && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) pos++; while (pos < len && Character.isDigit(input.charAt(pos))) pos++; }
                TokenCounter.addToken("NUMBER", input.substring(start, pos));
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                int start = pos; pos++;
                while (pos < len && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) pos++;
                String lex = input.substring(start, pos);
                if (KEYWORDS.contains(lex)) TokenCounter.addToken("KEYWORD", lex); else TokenCounter.addToken("IDENTIFIER", lex);
                continue;
            }

            advance(1);
            TokenCounter.addToken("ERROR", String.valueOf(c));
        }
        return 0;
    }

    private static boolean isHexDigitOrUnderscore(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F') || ch == '_';
    }
}
