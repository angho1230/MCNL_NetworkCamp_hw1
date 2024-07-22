package lexer.types;

public enum TokenType {
    ASSIGNMENT("assignment operator"),
    ADDITION("addition operator"),
    SUBTRACTION("subtraction operator"),
    MULTIPLICATION("multiplication operator"),
    DIVISION("division operator"),
    EQUALS("equals operator"),
    GREATER_THAN("greater than operator"),
    LESS_THAN("less than operator"),
    NOT_EQUALS("not equals operator"),
    GREATER_THAN_OR_EQUAL_TO("greater than or equal to operator"),
    LESS_THAN_OR_EQUAL_TO("less than or equal to operator"),
    LPAREN("left parenthesis"),
    RPAREN("right parenthesis"),
    LBRACE("left brace"),
    RBRACE("right brace"),
    LBRACKET("left bracket"),
    RBRACKET("right bracket"),
    SEMICOLON("statement terminator"),
    COMMA("punctuation - comma"),
    IDENTIFIER("identifier"),
    KEYWORD("keyword"),
    NUMBER("number literal"),
    STRING("string literal"),
    COMMENT("comment"),
    CONTROL("control operator"),
    ILLEGALD("illegal ID starting with digit"),
    ILLEGALW("illegal ID starting with wrong character"),
    ILLEGALID("illegal ID containing illegal character");

    private final String name;

    TokenType(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
