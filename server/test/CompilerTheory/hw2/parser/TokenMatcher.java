package parser;

import parser.types.NonTerminalTypes;
import scanner.lexer.Lexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;

public class TokenMatcher {
    public static boolean matchToken(Token token, NonTerminalTypes t){
        switch(t){
            case S:
            case PROC: 
            case STMT_SEQ: 
            case DEC_EXP:
            case ELSE_STMT:
                return true;
            case STMT:
                return matchToken(token, NonTerminalTypes.TYPE) || matchToken(token, NonTerminalTypes.ID);
            case EXP:
                return matchToken(token, NonTerminalTypes.ID) || matchToken(token, NonTerminalTypes.NUMBER);
            case FUNC:
                return token.getValue().compareTo("print_line") == 0;
            case IF_STMT:
                return token.getValue().compareTo("if") == 0;
            case TYPE:
                return token.getValue().compareTo("int") == 0;
            case OP:
                return isOP(token.getType());
            case NUMBER:
                return token.getType() == TokenType.NUMBER;
            case ID:
                return token.getType() == TokenType.IDENTIFIER;
            case STRING:
                return token.getType() == TokenType.STRING;
            default:
                return false;

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
}
/*
switch (type) {
            case ASSIGNMENT:
                break;
            case ADDITION:
                break;
            case SUBTRACTION:
                break;
            case MULTIPLICATION:
                break;
            case DIVISION:
                break;
            case EQUALS:
                break;
            case GREATER_THAN:
                break;
            case LESS_THAN:
                break;
            case NOT_EQUALS:
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                break;
            case LESS_THAN_OR_EQUAL_TO:
                break;
            case LPAREN:
                break;
            case RPAREN:
                break;
            case LBRACE:
                break;
            case RBRACKET:
                break;
            case LBRACKET:
                break;
            case SEMICOLON:
                break;
            case COMMA:
                break;
            case IDENTIFIER:
                break;
            case KEYWORD:
                break;
            case NUMBER:
                break;
            case STRING:
                break;
            default:
                return false;
        }
 */
