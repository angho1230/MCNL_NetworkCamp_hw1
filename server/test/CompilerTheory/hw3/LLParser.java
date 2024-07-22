import parser.Parser;
import parser.exceptions.ParsingFailedException;

public class LLParser {
    public static void main(String[] args) {
        LLParser rp = new LLParser();
        rp.run(args);
        System.exit(0);
    }

    public void run(String[] args) {
        if (args.length == 0) {
            System.out.println("Need argument for path");
            System.exit(0);
        }
        Parser parser = new Parser(args[0]);
        try {
            parser.parse();
            System.out.println("Parsing OK");
        }catch (ParsingFailedException e){
            System.out.println("Parsing Failed");
        }
    }
}
