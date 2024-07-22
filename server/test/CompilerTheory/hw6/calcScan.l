%{
    #include <stdio.h>
    #include <stdlib.h>
    #include "y.tab.h"
    void yyerror(char *s);
%}

%%

"begin"                 { return BEGIN; }
"end"                   { return END; }
"if"                    { return IF; }
"else if"               { return ELSE_IF; }
"else"                  { return ELSE; }
"int"                   { return INT; }
[0-9]+                  { yylval.ival = atoi(yytext); return NUMBER; }
"+"                     { return PLUS; }
"-"                     { return MINUS; }
"*"                     { return TIMES; }
"/"                     { return DIVIDE; }
"=="                    { return EQ; }
"="                     { return ASSIGN; }
">"                     { return GT; }
"<"                     { return LT; }
";"                     { return SEMICOLON; }
","                     { return COMMA; }
"\("                    { return LPAREN; }
"\)"                    { return RPAREN; }
"print_line"            { return PRINT_LINE; }
--.*                    { yylval.sval = strdup(yytext); return COMMENT; }
\".*\"                  { yylval.sval = strdup(yytext); return STRING; }
[a-zA-Z_$][a-zA-Z0-9_$]*  { yylval.sval = strdup(yytext); return IDENTIFIER; }
[ \t\n]                 {}
.                   { yyerror("illegal character"); }

%%

int yywrap(void) {
    return 1;
}
