import parser.RecursiveDescentParser;
import scanner.lexer.*;
import scanner.lexer.types.*;

import java.util.Stack;

public class RecurParser {
    public static void main(String[] args) {
        RecurParser rp = new RecurParser();
        rp.run(args);
        System.exit(0);
    }

    public void run(String[] args) {
        if (args.length == 0) {
            System.out.println("Need argument for path");
            System.exit(0);
        }
        RecursiveDescentParser parser = new RecursiveDescentParser(args[0]);
        if(parser.parse()){
            System.out.println("Parsing OK");
        }
    }
}
