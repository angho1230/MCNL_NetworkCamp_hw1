package parser.element;

public enum VarType {
    INTEGER("int"),
    STRING("string");

    private final String name;
    VarType(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
