package parser.element;

import parser.table.IdentifierTable;
import parser.types.NonTerminalTypes;

public class Element {
    private int intVal;
    private VarType in;
    private String value;
    private NonTerminalTypes type;
    public Element[] elements;
    private IdentifierTable.Id id;

    public Element(String value){
        elements = new Element[0];
        this.value = value;
        type = NonTerminalTypes.TERMINALS;
    }
    public Element(NonTerminalTypes type){
        this.type = type;
        elements = new Element[10];
        for(int i = 0; i < elements.length; i++){
            elements[i] = null;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public NonTerminalTypes getType() {
        return type;
    }

    public void setType(NonTerminalTypes type) {
        this.type = type;
    }

    public void print(){
        System.out.println(this.type.getName());
        for(int i = elements.length-1; i >= 0; i--){
            if(elements[i] != null) elements[i].print();
        }

    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public VarType getIn() {
        return in;
    }

    public void setIn(VarType in) {
        this.in = in;
    }

    public void setIdValue(int n){
        id.setValue(n);
    }
    public int getIdValue(){
        return id.getValue();
    }
}
