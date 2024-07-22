package lexer;

import lexer.types.CharacterType;
public enum State{
    START(0),
    NUM(1),
    ACCEPT(2),
    RELATIONOP(3),
    ID(4),
    STRING(5),
    OPCOMMENT(6),
    COMMENT(7),
    ILLEGAL(8);
    
    /*LETTER(0),
    DIGIT(1),
    AOPERATOR(2),
    ROPERATOR(3),
    EQUALS(4),
    BAR(5),
    DELIMITER(6),
    DOUBLEQUOTE(7),
    UNDERBAR(8),
    CONTROL(9),
    EOL(10),
    OTHER(11);*/
    private static State[][] stateTable = {
    {ID, NUM, ACCEPT, RELATIONOP, RELATIONOP, OPCOMMENT, ACCEPT, STRING, ILLEGAL, START, START, ILLEGAL},//START
    {ILLEGAL, NUM, START, START, START, START, START, START, ILLEGAL, START, START, ILLEGAL},//NUM
    {START, START, START, START, START, START, START, START, START, START, START, START},//ACCEPT
    {START, START, START, START, ACCEPT, START, START, START, START, START, START, START},//RELATIONOP
    {ID, ID, START, START, START, START, START, START, ID, START, START, ILLEGAL},//ID
    {STRING, STRING, STRING, STRING, STRING, STRING, STRING, ACCEPT, STRING, STRING, STRING, STRING},//STRING
    {START, START, START, START, START, COMMENT, START, START, START, START, START, START},//OPCOMMENT
    {COMMENT, COMMENT, COMMENT, COMMENT, COMMENT, COMMENT, COMMENT, COMMENT, COMMENT, COMMENT, START, COMMENT},//COMMENT
    {ILLEGAL, ILLEGAL, START, START, START, START, START, START, ILLEGAL, START, START, ILLEGAL},//ILLEGAL
    };
    private final int number;
    State(int number){
        this.number = number;
    }
    public int getNumber(){
        return number;
    }
    public State nextState(char c){
        return stateTable[number][CharacterType.OTHER.getCharType(c).getNumber()];
    }
}
