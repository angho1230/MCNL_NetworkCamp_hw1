package parser;

import parser.types.NonTerminalTypes;
import scanner.lexer.Lexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;

public class TokenMatcher {

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