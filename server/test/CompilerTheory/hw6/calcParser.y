%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void yyerror(const char *s);
extern int yylex(void);

typedef struct {
    char *sval;
    int ival;
} YYSTYPE;

#define YYSTYPE YYSTYPE
%}

%token BEGIN END IF ELSE ELSE_IF INT PLUS MINUS TIMES DIVIDE EQ NUMBER ASSIGN GT LT SEMICOLON COMMA LPAREN RPAREN PRINT_LINE IDENTIFIER STRING COMMENT

%%

program:
    BEGIN statements END
    ;

statements:
    statement
    | statements statement
    ;

statement:
    declaration
    | assignment
    | if_else_statement
    ;

declaration:
    INT var_list SEMICOLON
    ;

var_list:
    IDENTIFIER
    | var_list COMMA IDENTIFIER
    ;

assignment:
    IDENTIFIER ASSIGN expr SEMICOLON
    ;

expr:
    NUMBER
    | IDENTIFIER
    ;

if_else_statement:
    IF LPAREN condition RPAREN BEGIN statements END ELSE BEGIN statements END
    ;

condition:
    IDENTIFIER GT expr
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Error: %s\n", s);
}

int main(void) {
    return yyparse();
}
