package parser.syntaxDirectedTranslation;

import parser.element.Element;
import parser.element.VarType;
import parser.table.IdentifierTable;
import parser.types.NonTerminalTypes;

public class Translator {
    private IdentifierTable symbolTable;
    private Element initialState;
    public Translator(IdentifierTable symbolTable, Element initialState){
        this.initialState = initialState;
        this.symbolTable = symbolTable;
    }
    public void translate(Element e){
        switch(e.getType()){
            case S:
                translate(e.elements[0]);
                break;
            case ID:
                e.setIntVal(symbolTable.getValue(e.getValue()));
                e.setIn(VarType.INTEGER);
                break;
            case BODY:
                translate(e.elements[1]);
                break;
            case PROGRAM_BLOCK:
                if(e.elements[0] == null){
                    return;
                }
                else if(e.elements[2].getType() == NonTerminalTypes.STATEMENT){
                    translate(e.elements[2]);
                    translate(e.elements[0]);
                }else{
                    //skip while/for/if
                    translate(e.elements[0]);
                }
                break;
            case TYPE:
                if(e.elements[0].getValue().compareTo("int") == 0){
                    e.setIn(VarType.INTEGER);
                }
                break;
            case UNARY:
                //skip
                break;
            case NUMBER:
                e.setIntVal(Integer.parseInt(e.getValue()));
                e.setIn(VarType.INTEGER);
                break;
            case STRING:
                e.setIn(VarType.STRING);
                break;
            case CALL_ID:
                translate(e.elements[0]);
                e.setIn(e.elements[0].getIn());
                e.setIntVal(e.elements[0].getIntVal());
                e.setValue(e.elements[0].getValue());
                break;
            case OPERAND:
                translate(e.elements[0]);
                e.setIntVal(e.elements[0].getIntVal());
                break;
            case FUNCTION:
                if(e.elements[3].getValue().compareTo("print_line") == 0){
                    translate(e.elements[1]);
                    if(e.elements[1].getIn()==VarType.STRING) System.out.println(e.elements[1].getValue());
                    else{
                        System.out.println(e.elements[1].getIntVal());
                    }
                }
                break;
            case OPERATOR:
            case PGM_NAME:
                break;
            case PARAMETER:
                translate(e.elements[0]);
                if(e.elements[0].getIn() == VarType.INTEGER){
                    e.setIn(VarType.INTEGER);
                    e.setIntVal(e.elements[0].getIntVal());
                }else{
                    e.setValue(e.elements[0].getValue());
                    e.setIn(VarType.STRING);
                }
                break;
            case STATEMENT:
                if(e.elements[0].getType() == NonTerminalTypes.FUNCTION){
                    translate(e.elements[0]);
                }
                else if(e.elements[1].getType() == NonTerminalTypes.CALL_ID){
                    translate(e.elements[1]);
                    translate(e.elements[0]);
                    symbolTable.setValue(e.elements[1].getValue(), e.elements[0].getIntVal());
                } else{
                    translate(e.elements[3]);
                    e.elements[2].setIn(e.elements[3].getIn());
                    e.elements[0].setIn(e.elements[3].getIn());
                    translate(e.elements[1]);
                    //System.out.println(e.elements[1].getIntVal());
                    translate(e.elements[2]);
                    symbolTable.setValue(e.elements[2].getValue(), e.elements[1].getIntVal());
                    translate(e.elements[0]);
                }
                break;
            case TERMINALS:
                break;
            case EXPRESSION:
                translate(e.elements[1]);
                translate(e.elements[0], e.elements[1].getIntVal());
                e.setIntVal(e.elements[0].getIntVal());
                break;
            case BEGIN_BLOCK:
            case EXPRESSION2:
            case DECLARATION_ID:
                translate(e.elements[0]);
                e.setValue(e.elements[0].getValue());
                break;
            case EXPRESSION_UNARY:
                translate(e.elements[0]);
                e.setIntVal(e.elements[0].getIntVal());
                break;
            case STATEMENT_SEQUENCE:
                if(e.elements[0] == null){
                    break;
                }
                translate(e.elements[2]);
                translate(e.elements[1]);
                symbolTable.setValue(e.elements[2].getValue(), e.elements[1].getIntVal());
                translate(e.elements[0]);
                break;
            case DECLARATION_EXPRESSION:
                if(e.elements[0] == null){
                    e.setIntVal(0);
                    break;
                }
                translate(e.elements[0]);
                e.setIntVal(e.elements[0].getIntVal());
                break;
        }
    }

    public void translate(Element e, int n){
        //System.out.println(e.getType().getName());
        switch(e.getType()){
            case EXPRESSION2:
                if(e.elements[0]==null){
                    e.setIntVal(n);
                    break;
                }
                translate(e.elements[1]);
                int c = compute(n, e.elements[1].getIntVal(), e.elements[2].elements[0].getValue());
                translate(e.elements[0], c);
                e.setIntVal(e.elements[0].getIntVal());
        }
    }

    public int compute(int n1, int n2, String op){
        switch (op){
            case "+":
                return n1 + n2;
            case "-":
                return n1 - n2;
            case "*":
                return n1 * n2;
            case "/":
                return n1 / n2;
            default:
                System.out.println("invalid operator");
                return 0;
        }
    }
}
