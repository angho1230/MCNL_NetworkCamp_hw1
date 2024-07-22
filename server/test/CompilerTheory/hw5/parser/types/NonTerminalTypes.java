package parser.types;

public enum NonTerminalTypes{
    S("S"),
    BEGIN_BLOCK("BEGIN_BLOCK"),
    PGM_NAME("PGM_NAME"),
    BODY("BODY"),
    PROGRAM_BLOCK("PROGRAM_BLOCK"),
    STATEMENT("STATEMENT"),
    STATEMENT_F("STATEMENT_F"),
    UNARY("UNARY"),
    TYPE("TYPE"),
    DECLARATION_EXPRESSION("DECLARATION_EXPRESSION"),
    DECLARATION_STATEMENT("DECLARATION_STATEMENT"),
    EXPRESSION("EXPRESSION"),
    OPERATOR("OPERATOR"),
    OPERAND("OPERAND"),
    FUNCTION("FUNCTION"),
    PARAMETER("PARAMETER"),
    IF_STATEMENT("IF_STATEMENT"),
    WHILE_STATEMENT("WHILE_STATEMENT"),
    FOR_STATEMENT("FOR_STATEMENT"),
    ELSE_STATEMENT("ELSE_STATEMENT"),
    EXPRESSION_UNARY("EXPRESSION_UNARY");

    private final String name;

    NonTerminalTypes(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
