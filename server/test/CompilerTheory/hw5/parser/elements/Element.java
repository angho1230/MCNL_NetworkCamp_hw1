package parser.elements;

public class Element {
    private int state ;
    private String value;
    private boolean isState;

    public Element(int state){
        this.isState = true;
        this.state = state;
    }

    public Element(String value){
        this.isState = false;
        this.value = value;
    }

    public String toString(){
        if(this.isState){
            return String.format("S%d", this.state);
        }
        return this.value;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }
}
