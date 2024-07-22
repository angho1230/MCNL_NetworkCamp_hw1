package textIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextReader {
    private BufferedReader br = null;
    private String currentLine = null;
    private int index = 0;
    public TextReader(String path) {
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isEnd(){
        if(currentLine == null) return false;
        return (index == currentLine.length());
    }
    public String nextLine() {
        try {
            index = 0;
            currentLine = br.readLine();
            //System.out.println("Read : " + currentLine);
            return currentLine;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }     
    }
    public char next(){
        if (index < currentLine.length()) {
            return currentLine.charAt(index++);
        } else {
            return '\0';
        }
    }
    public int getIndex() {
        return index;
    }
    public boolean setIndex(int index) {
        if (index >= 0 && index < currentLine.length()) {
            this.index = index;
            return true;
        } else {
            return false;
        }
    }
    public String getCurrentLine() {
        return currentLine;
    }
    public boolean isCurrentLineNull() {return currentLine == null;}
    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
