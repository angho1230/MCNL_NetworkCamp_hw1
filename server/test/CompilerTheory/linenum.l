%{
    #include <stdio.h>X
    int lineno = 1;
%}
line .*\n
%option noyywrap
%%
{line} {printf("%5d %s", lineno++, yytext);}
%%
int main()
{yylex(); return 0;}
