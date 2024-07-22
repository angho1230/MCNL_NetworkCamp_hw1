package parser.types;

public enum NonTerminalTypes{
    S("S"),
    BEGIN_BLOCK("BEGIN_BLOCK"),
    PGM_NAME("PGM_NAME"),
    BODY("BODY"),
    PROGRAM_BLOCK("PROGRAM_BLOCK"),
    STATEMENT("STATEMENT"),
    UNARY("UNARY"),
    TYPE("TYPE"),
    DECLARATION_EXPRESSION("DECLARATION_EXPRESSION"),
    STATEMENT_SEQUENCE("STATEMENT_SEQUNCE"),
    EXPRESSION("EXPRESSION"),
    EXPRESSION2("EXPRESSION2"),
    DECLARATION_ID("DECLARATION_ID"),
    CALL_ID("CALL_ID"),
    OPERATOR("OPERATOR"),
    OPERAND("OPERAND"),
    FUNCTION("FUNCTION"),
    PARAMETER("PARAMETER"),
    IF_STATEMENT("IF_STATEMENT"),
    WHILE_STATEMENT("WHILE_STATEMENT"),
    FOR_STATEMENT("FOR_STATEMENT"),
    ELSE_STATEMENT("ELSE_STATEMENT"),
    EXPRESSION_UNARY("EXPRESSION_UNARY"),
    ID("ID"),
    NUMBER("NUMBER"),
    STRING("STRING"),
    TERMINALS("TERMINALS");

    private final String name;

    NonTerminalTypes(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
