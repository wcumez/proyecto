/* proyecto.flex - JFlex specification for Swift lexical analyzer */
%%

%class Lexer
%unicode
%public
%implements java.io.Serializable
%line
%column

%{
import java.io.*;
%}

/* macros */
ALPHA = [A-Za-z_]
DIGIT = [0-9]
ID = {ALPHA}({ALPHA}|{DIGIT})*
WS = [ \t\r\f]+
STRING_CHAR = [^"\n\\]
ESC = \\.
STRING = \"({STRING_CHAR}|{ESC})*\"

%%

/* skip whitespace */
{WS}            { /* ignore */ }

/* Swift keywords */
"if"            { TokenCounter.addToken("KEYWORD", yytext()); }
"else"          { TokenCounter.addToken("KEYWORD", yytext()); }
"for"           { TokenCounter.addToken("KEYWORD", yytext()); }
"while"         { TokenCounter.addToken("KEYWORD", yytext()); }
"func"          { TokenCounter.addToken("KEYWORD", yytext()); }
"let"           { TokenCounter.addToken("KEYWORD", yytext()); }
"var"           { TokenCounter.addToken("KEYWORD", yytext()); }
"return"        { TokenCounter.addToken("KEYWORD", yytext()); }
"class"         { TokenCounter.addToken("KEYWORD", yytext()); }
"struct"        { TokenCounter.addToken("KEYWORD", yytext()); }
"enum"          { TokenCounter.addToken("KEYWORD", yytext()); }
"protocol"      { TokenCounter.addToken("KEYWORD", yytext()); }
"extension"     { TokenCounter.addToken("KEYWORD", yytext()); }
"import"        { TokenCounter.addToken("KEYWORD", yytext()); }
"in"            { TokenCounter.addToken("KEYWORD", yytext()); }
"do"            { TokenCounter.addToken("KEYWORD", yytext()); }
"try"           { TokenCounter.addToken("KEYWORD", yytext()); }
"catch"         { TokenCounter.addToken("KEYWORD", yytext()); }
"guard"         { TokenCounter.addToken("KEYWORD", yytext()); }
"switch"        { TokenCounter.addToken("KEYWORD", yytext()); }
"case"          { TokenCounter.addToken("KEYWORD", yytext()); }
"default"       { TokenCounter.addToken("KEYWORD", yytext()); }
"break"         { TokenCounter.addToken("KEYWORD", yytext()); }
"continue"      { TokenCounter.addToken("KEYWORD", yytext()); }
"true"          { TokenCounter.addToken("KEYWORD", yytext()); }
"false"         { TokenCounter.addToken("KEYWORD", yytext()); }
"nil"           { TokenCounter.addToken("KEYWORD", yytext()); }

/* identifiers */
{ID}            { TokenCounter.addToken("IDENTIFIER", yytext()); }

/* numbers (decimal, hex, binary, octal, with underscores) */
(0x[0-9A-Fa-f_]+)   { TokenCounter.addToken("NUMBER", yytext()); }
(0b[01_]+)          { TokenCounter.addToken("NUMBER", yytext()); }
(0o[0-7_]+)         { TokenCounter.addToken("NUMBER", yytext()); }
({DIGIT}+(_?{DIGIT})*)(\.{DIGIT}+(_?{DIGIT})*)?([eE][+-]?{DIGIT}+)? {
                    TokenCounter.addToken("NUMBER", yytext()); }

/* operators and symbols */
"{"             { TokenCounter.addToken("OPEN_BRACE", yytext()); }
"}"             { TokenCounter.addToken("CLOSE_BRACE", yytext()); }
"("             { TokenCounter.addToken("OPEN_PAREN", yytext()); }
")"             { TokenCounter.addToken("CLOSE_PAREN", yytext()); }
"["             { TokenCounter.addToken("OPEN_BRACKET", yytext()); }
"]"             { TokenCounter.addToken("CLOSE_BRACKET", yytext()); }
";"             { TokenCounter.addToken("SEMICOLON", yytext()); }
","             { TokenCounter.addToken("COMMA", yytext()); }
":"             { TokenCounter.addToken("COLON", yytext()); }
"="             { TokenCounter.addToken("ASSIGN", yytext()); }
"=="            { TokenCounter.addToken("EQ", yytext()); }
"!="            { TokenCounter.addToken("NEQ", yytext()); }
">="            { TokenCounter.addToken("GE", yytext()); }
"<="            { TokenCounter.addToken("LE", yytext()); }
">"             { TokenCounter.addToken("GT", yytext()); }
"<"             { TokenCounter.addToken("LT", yytext()); }
"+"             { TokenCounter.addToken("PLUS", yytext()); }
"-"             { TokenCounter.addToken("MINUS", yytext()); }
"*"             { TokenCounter.addToken("MUL", yytext()); }
"/"             { TokenCounter.addToken("DIV", yytext()); }
"%"             { TokenCounter.addToken("MOD", yytext()); }
"+="            { TokenCounter.addToken("PLUS_ASSIGN", yytext()); }
"-="            { TokenCounter.addToken("MINUS_ASSIGN", yytext()); }

/* string literals (single-line) */
\"([^\"\n\\]|\\.)*\"   { TokenCounter.addToken("STRING", yytext()); }

/* multi-line string delimiter for Swift: triple double-quotes */
""""" + "([\s\S]*?)" + """"" + "  { TokenCounter.addToken("STRING_MULTILINE", yytext()); }

/* comments */
"//".*          { /* ignore */ }
"/\*"([^*]|\*+[^*/])*\*+\/"   { /* ignore */ }

/* any other character */
.               { TokenCounter.addToken("ERROR", yytext()); }

/* EOF */
<<EOF>>         { return 0; }

