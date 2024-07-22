package parser;

import parser.elements.Element;
import parser.exceptions.ParsingFailedException;
import parser.tables.Tables;
import parser.types.NonTerminalTypes;
import scanner.SmallLexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;

import java.util.Stack;
public class Parser {
    private SmallLexer lex;

    public Parser(String path){
        this.lex = new SmallLexer(path);
    }

    private Stack<Element> parsingStack = new Stack<>();
    public void printParsingStack(){
        for(int i = 0; i < parsingStack.size(); i++) {
            System.out.print(parsingStack.elementAt(i).toString() + " ");
        }
        System.out.println();
    }

    public void parse() throws ParsingFailedException {
        parsingStack.push(new Element(2));
        System.out.print("[S] ");
        printParsingStack();
        while (!parsingStack.empty()){
            Element top = parsingStack.lastElement();
            int stateIdx = top.getState() - 1;
            Token tok = lex.next();
            int tokIdx = TokenMatcher.getTokenIdx(tok);
            if(tokIdx < 0){
                System.out.println("Error - no matching state for character " + tok.getValue());
                throw new ParsingFailedException();
            }
            int val = Tables.rtable[stateIdx][tokIdx];
            if(val != 0){
                for(int i = 0; i < deletion(val) * 2; i++){
                    parsingStack.pop();
                }
                if(val == -1){
                    parsingStack.pop();
                }
                if(parsingStack.empty()){
                    System.out.print("[R] ");
                    printParsingStack();
                    continue;
                }
                stateIdx = parsingStack.lastElement().getState()-1;
                parsingStack.push(new Element(getNonTerm(val)));
                int newState = Tables.gotable[stateIdx][nonTermVal(getNonTerm(val))];
                if(newState == 0){
                    printParsingStack();
                    System.out.println("no parsing rule for " + getNonTerm(val) + "in state");
                    throw new ParsingFailedException();
                }
                parsingStack.push(new Element(newState));
                lex.saveToken(tok);
                System.out.print("[R] ");
                printParsingStack();
                continue;
            }
            int nextState = Tables.stable[stateIdx][tokIdx];
            if(nextState == 0){
                System.out.println("No next state for " + (tok != null ? tok.getValue() : "$"));
                throw new ParsingFailedException();
            }
            parsingStack.push(new Element(tok.getValue()));
            parsingStack.push(new Element(nextState));
            System.out.print("[S] ");
            printParsingStack();
        }
    }

    //S	BEGIN_BLOCK	PGM_NAME	BODY	PRGBLK	STMT	DECSTMT	EXPUNARY	UNARY	TYPE	DECEXP	EXP	OP	OPERAND	FUNCTION	PARAM	IFSTMT	WHILESTMT	FORSTMT	ELSESTMT STMTF
    public int nonTermVal(String nt){
        switch(nt){
            case "S":
                return 0;
            case "BEGIN_BLOCK":
                return 1;
            case "PGM_NAME":
                return 2;
            case "BODY":
                return 3;
            case "PROGRAM_BLOCK":
                return 4;
            case "STATEMENT":
                return 5;
            case "DECLARATION_STATEMENT":
                return 6;
            case "EXPRESSION_UNARY":
                return 7;
            case "UNARY":
                return 8;
            case "TYPE":
                return 9;
            case "DECLARATION_EXPRESSION":
                return 10;
            case "EXPRESSION":
                return 11;
            case "OPERATOR":
                return 12;
            case "OPERAND":
                return 13;
            case "FUNCTION":
                return 14;
            case "PARAMETER":
                return 15;
            case "IF-STATEMENT":
                return 16;
            case "WHILE-STATEMENT":
                return 17;
            case "FOR-STATEMENT":
                return 18;
            case "ELSE-STATEMENT":
                return 19;
            case "STATEMENT_F":
                return 20;
            case "S'":
                return 21;
            default:
                System.out.println("Error - no rule " + nt);
                System.exit(1);
        }
        return -1;
    }
    public int deletion(int rule){
        switch(rule){
            case -1:
            case 3:
            case 10:
            case 12:
            case 16:
            case 19:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 30:
            case 31:
            case 39:
            case 40:
                return 1;
            case 1:
            case 2:
            case 6:
            case 7:
            case 8:
            case 11:
            case 15:
            case 17:
            case 18:
            case 20:
            case 36:
            case 38:
                return 2;
            case 4:
            case 5:
            case 13:
            case 22:
                return 3;
            case 14:
            case 29:
                return 4;
            case 33:
                return 5;
            case 32:
            case 35:
                return 6;
            case 34:
                return 9;
            case 9:
            case 21:
            case 37:
                return 0;
            default:
                return -1;
        }
    }
    public String getNonTerm(int rule){
        switch(rule){
            case -1:
                return "S'";
            case 1:
                return "S";
            case 2:
                return "BEGIN_BLOCK";
            case 3:
                return "PGM_NAME";
            case 4:
                return "BODY";
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return "PROGRAM_BLOCK";
            case 10:
            case 11:
            case 12:
            case 40:
                return "STATEMENT";
            case 13:
            case 14:
                return "DECLARATION_STATEMENT";
            case 15:
            case 16:
                return "EXPRESSION_UNARY";
            case 17:
            case 18:
                return "UNARY";
            case 19:
                return "TYPE";
            case 20:
            case 21:
                return "DECLARATION_EXPRESSION";
            case 22:
            case 23:
                return "EXPRESSION";
            case 24:
            case 25:
            case 26:
                return "OPERATOR";
            case 27:
            case 28:
                return "OPERAND";
            case 29:
                return "FUNCTION";
            case 30:
            case 31:
                return "PARAMETER";
            case 32:
                return "IF-STATEMENT";
            case 33:
                return "WHILE-STATEMENT";
            case 34:
                return "FOR-STATEMENT";
            case 35:
            case 36:
            case 37:
                return "ELSE-STATEMENT";
            case 38:
            case 39:
                return "STATEMENT_F";
            default:
                System.out.println("Error - no rule #" + rule);
                System.exit(1);
        }
        return "";
    }

}
