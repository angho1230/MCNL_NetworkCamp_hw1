package parser;

import parser.types.NonTerminalTypes;
import scanner.lexer.Lexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;
//program	id	begin	end	;	,	=	int	+	-	*	number	function_id	string	(	)	for	while	if	else_if	else	$

public class TokenMatcher {
    public static int getTokenIdx(Token t){
        if(t == null) return 21;
        switch(t.getValue()){
            case "program":
                return 0;
            case "begin":
                return 2;
            case "end":
                return 3;
            case ";":
                return 4;
            case ",":
                return 5;
            case "=":
                return 6;
            case "int":
            case "integer":
                return 7;
            case "+":
                return 8;
            case "-":
                return 9;
            case "*":
            case ">":
            case "<":
            case "==":
                return 10;
            case "print_line":
            case "display":
                return 12;
            case "(":
                return 14;
            case ")":
                return 15;
            case "for":
                return 16;
            case "while":
                return 17;
            case "if":
                return 18;
            case "else_if":
                return 19;
            case "else":
                return 20;
            case "break":
                return 22;
            default:
                if(t.getType() == TokenType.IDENTIFIER) return 1;
                if(t.getType() == TokenType.NUMBER) return 11;
                if(t.getType() == TokenType.STRING) return 13;
                return -1;
        }
    }

    public static boolean isOP(TokenType type) {
        switch (type) {
            case ADDITION:
            case SUBTRACTION:
            case MULTIPLICATION:
            case DIVISION:
            case EQUALS:
            case GREATER_THAN:
            case LESS_THAN:
            case NOT_EQUALS:
            case GREATER_THAN_OR_EQUAL_TO:
            case LESS_THAN_OR_EQUAL_TO:
                return true;
            default:
                return false;
        }
    }
    public static boolean isFunctionId(Token type){
        switch(type.getValue()){
            case "display":
            case "print_line":
                return true;
        }
        return false;
    }

    public static boolean isKeyword(Token type){
        switch(type.getValue()){
            case "display":
            case "print_line":
            case "program":
            case "begin":
            case "end":
            case "int":
            case "if":
            case "else_if":
            case "else":
            case "while":
            case "for":
            case "integer":
            case "break":
                return true;
        }
        return false;
    }
}