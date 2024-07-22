%{
    #include <stdio.h>
    #include <stdlib.h>
    int count = 0;
%}
digit [0-9]
number {digit}+
%option noyywrap
%%
{number} { int n = atoi(yytext); if(n > 9){count++;} printf("%x", n);}
%%
int main()
{yylex();
fprintf(stderr, "number of replacements: %d\n", count);
return 0;}
