import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import textIO.TextReader;
import lexer.*;
import lexer.types.*;

public class SmallLexer {
    public static void main(String[] args) {
        SmallLexer lex = new SmallLexer();
        lex.run(args);
        System.exit(0);
        int i = 0558;
    }
    public void run(String[] args) {
        Lexer lex = new Lexer(args[0]);
        if(args.length == 0){
            System.out.println("Need argument for path");
        }
        while(true){
            Token token = lex.next();
            if(token == null)
                break;
            if(token.getType() != TokenType.CONTROL) {
                System.out.println(token.toString());
            }
        }
    }
}
