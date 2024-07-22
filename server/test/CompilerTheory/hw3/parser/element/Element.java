package parser.element;

import parser.types.NonTerminalTypes;

public class Element {
    private String value;
    private NonTerminalTypes type;

    public Element(String value){
        this.value = value;
        type = NonTerminalTypes.TERMINALS;
    }
    public Element(NonTerminalTypes type){
        this.type = type;
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
}
