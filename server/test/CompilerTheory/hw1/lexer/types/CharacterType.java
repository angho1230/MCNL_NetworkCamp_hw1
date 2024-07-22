package lexer.types;

public enum CharacterType{
    LETTER(0),
    DIGIT(1),
    AOPERATOR(2),
    ROPERATOR(3),
    EQUALS(4),
    BAR(5),
    DELIMITER(6),
    DOUBLEQUOTE(7),
    UNDERBARDOT(8),
    CONTROL(9),
    EOL(10),
    OTHER(11);
    
    private final int number;

    CharacterType(int number){
        this.number = number;
    }
    public int getNumber(){
        return number;
    }
    public CharacterType getCharType(char c){
        switch(c){
            case '$':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                return LETTER;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return DIGIT;
            case '=':
                return EQUALS;
            case '-':
                return BAR;
            case '+':
            case '*':
            case '/':
                return AOPERATOR;
            case '>':
            case '<':
                return ROPERATOR;
            case ';':
            case ')':
            case '}':
            case ']':
            case ',':
            case '(':
            case '{':
            case '[':
                return DELIMITER;
            case ' ':
            case '\t':
            case '\r':
                return CONTROL;
            case '\n':
            case '\0':
                return EOL;
            case '"':
                return DOUBLEQUOTE;
            case '_':
            case '.':
                return UNDERBARDOT;
            default:
                return OTHER;
        }
    }
}