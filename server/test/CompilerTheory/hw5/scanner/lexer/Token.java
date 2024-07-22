package scanner.lexer;

import scanner.lexer.types.TokenType;
import scanner.lexer.types.CharacterType;

public class Token {
    private TokenType type;
    private State state;
    private String value;

    public Token(){
        this.value = "";
        this.state = State.START;
    }
    public TokenType getType(){
        return type;
    }
    public String getValue(){
        return value;
    }
    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }
    public String toString(){
        return String.format("%-20s\t%s", value, type.getName());
    }
    public void setValue(String value){
        this.value = value;
    }

    public State getNextState(char c){
        return this.state.nextState(c);
    }
    public void addValue(char c){
        this.state = this.state.nextState(c);
        this.value += c;
    }

/*
    START(0),
    NUM(1),
    ACCEPT(2),
    RELATIONOP(3),
    ID(4),
    STRING(5),
    OPCOMMENT(6),
    COMMENT(7),
    ILLEGAL(8);
*/
    public void setType(){
        switch(this.state){
            case START:
                this.type = TokenType.CONTROL;
                break;
            case NUM:
                this.type = TokenType.NUMBER;
                break;
            case ACCEPT:
                this.type = getACCEPT();
                break;
            case RELATIONOP:
                this.type = getRELATIONOP();
                break;
            case ID:
                this.type = getID();
                break;
            case STRING:
                this.type = TokenType.STRING;
                break;
            case OPCOMMENT:
                this.type = TokenType.SUBTRACTION;
                break;
            case COMMENT:
                this.type = TokenType.COMMENT;
                break;
            case ILLEGAL:
                this.type = getILLEGAL();
                break;
        }
    }

    public TokenType getRELATIONOP(){
        switch(this.value){
            case ">":
                return TokenType.GREATER_THAN;
            case "<":
                return TokenType.LESS_THAN;
            case "=":
                return TokenType.ASSIGNMENT;
            default:
                System.out.println("Error in getRELATIONOP (" + this.value + ")");
                return TokenType.ILLEGALID;
        }
    }

    public TokenType getACCEPT(){
        if(this.value.charAt(0) == '"'){
            return TokenType.STRING;
        }
        switch(this.value){
            case ",":
                return TokenType.COMMA;
            case ";":
                return TokenType.SEMICOLON;
            case "==":
                return TokenType.EQUALS;
            case "+":
                return TokenType.ADDITION;
            case "-":
                return TokenType.SUBTRACTION;
            case "*":
                return TokenType.MULTIPLICATION;
            case "/":
                return TokenType.DIVISION;
            case "(":
                return TokenType.LPAREN;
            case ")":
                return TokenType.RPAREN;
            case "{":
                return TokenType.LBRACE;
            case "}":
                return TokenType.RBRACE;
            case "[":
                return TokenType.LBRACKET;
            case "]":
                return TokenType.RBRACKET;
            case "<=":
                return TokenType.LESS_THAN_OR_EQUAL_TO;
            case ">=":
                return TokenType.GREATER_THAN_OR_EQUAL_TO;
            default:
                System.out.println("Error in getACCEPT (" + this.value + ")");
                return TokenType.ILLEGALID;
        }
    }

    public TokenType getILLEGAL(){
        CharacterType firstCharType = CharacterType.OTHER.getCharType(this.value.charAt(0));
        switch(firstCharType){
            case DIGIT:
                return TokenType.ILLEGALD;
            case OTHER:
            case UNDERBARDOT:
                return TokenType.ILLEGALW;
            default:
                return TokenType.ILLEGALID;
        }
    }

    public TokenType getID(){
        if(Keyword.isKeyword(this.value)){
            return TokenType.KEYWORD;
        }
        else {
            return TokenType.IDENTIFIER;
        }
    } 
    private static class Keyword{
        private static String[] keyword = {
            "int",
                "integer",
            "program",
            "begin",
            "end",
            "if",
            "else",
            "else_if",
            "while",
            "print_line",
                "display",
                "for",
                "break"
        };
        public static boolean isKeyword(String word){
            for(int i = 0; i < keyword.length; i++){
                if(keyword[i].equals(word)){
                    return true;
                }
            }
            return false;
        }
    }
}   
