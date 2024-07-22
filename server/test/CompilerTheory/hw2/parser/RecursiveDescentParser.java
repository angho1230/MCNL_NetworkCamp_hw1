package parser;

import parser.types.NonTerminalTypes;
import scanner.SmallLexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;

public class RecursiveDescentParser {

    SmallLexer lex;

    public RecursiveDescentParser(String path){
        this.lex = new SmallLexer(path);
    }

    public boolean parse(){
        return s();
    }

    private boolean s() {
        //System.out.println("s");
        Token curr = lex.next();
        if (curr == null || curr.getValue().compareTo("program") != 0) {
            System.out.println("Parsing failed\nprogram missing");
            return false;
        }
        curr = lex.next();
        if (curr == null || curr.getType() != TokenType.IDENTIFIER) {
            System.out.println("Parsing failed\nidentifier missing");
            return false;
        }
        curr = lex.next();
        if (curr == null || curr.getValue().compareTo("begin") != 0) {
            System.out.println("Parsing failed\nbegin missing");
            return false;
        }
        if (!proc()) return false;
        curr = lex.next();
        if (curr == null || curr.getValue().compareTo("end") != 0) {
            System.out.println("Parsing failed\nend missing");
            return false;
        }
        if ((curr = lex.next()) != null){
            System.out.println("Parse failed\nRemaining token");
            do{
                System.out.println(curr.toString());
            }while((curr = lex.next()) != null);
            return false;
        }
        return true;
    }

    private boolean proc(){
        //System.out.println("p");
        Token curr = lex.next();
        if(curr == null) return true;
        if(TokenMatcher.matchToken(curr, NonTerminalTypes.STMT)){
            lex.saveToken(curr);
            if(!stmt()) return false;
            curr = lex.next();
            if(curr == null || curr.getType() != TokenType.SEMICOLON){
                System.out.println("Parsing failed\nsemicolon missing");
                return false;
            }
            return proc();
        }
        else if(TokenMatcher.matchToken(curr, NonTerminalTypes.IF_STMT)){
            lex.saveToken(curr);
            if(!if_stmt()) return false;
            return proc();
        }
        else if(TokenMatcher.matchToken(curr, NonTerminalTypes.FUNC)){
            lex.saveToken(curr);
            if(!func()) return false;
            curr = lex.next();
            if(curr == null || curr.getType() != TokenType.SEMICOLON){
                System.out.println("Parsing failed\nsemicolon missing");
                return false;
            }
            return proc();
        }
        lex.saveToken(curr);
        return true;
    }

    private boolean stmt(){
        //System.out.println("stmt");
        Token curr = lex.next();
        if(curr == null){
            System.out.println("Parsing failed\nstmt cannot disappear");
            return false;
        }
        if(TokenMatcher.matchToken(curr, NonTerminalTypes.TYPE)){
            lex.saveToken(curr);
            return type() && id() && dec_exp() && stmt_seq();
        }
        else if(TokenMatcher.matchToken(curr, NonTerminalTypes.ID)){
            lex.saveToken(curr);
            if(!id()) return false;
            curr = lex.next();
            if(curr.getType() != TokenType.ASSIGNMENT){
                System.out.println("Parsing failed\nassignment opperator missing");
                return false;
            }
            return exp();
        }
        System.out.println("Parsing failed\nstmt cannot disappear");
        return false;
    }

    private boolean stmt_seq(){
        //System.out.println("stmt_seq");
        Token curr = lex.next();
        if(curr == null) return true;
        if(curr.getType() == TokenType.COMMA){
            return id() && dec_exp() && stmt_seq();
        }
        lex.saveToken(curr);
        return true; //epsilon
    }

    private boolean dec_exp(){
        Token curr = lex.next();
        if(curr == null) return true;
        if(curr.getType() == TokenType.ASSIGNMENT){
            return exp();
        }
        lex.saveToken(curr);
        return true;
    }

    private boolean exp(){
        Token curr = lex.next();
        if(!TokenMatcher.matchToken(curr, NonTerminalTypes.ID) && !TokenMatcher.matchToken(curr, NonTerminalTypes.NUMBER)){
            System.out.println("Parsing failed\nmissing number or id");
            return false;
        }
        curr = lex.next();
        if(TokenMatcher.isOP(curr.getType())){
            return exp();
        }
        lex.saveToken(curr);
        return true;
    }

    private boolean func(){
        Token curr = lex.next();
        if(curr == null || !TokenMatcher.matchToken(curr, NonTerminalTypes.FUNC)){
            System.out.println("Parsing failed\nfunc cannot disappear");
            return false;
        }
        curr = lex.next();
        if(curr == null || curr.getType() != TokenType.LPAREN){
            System.out.println("Parsing failed\nleft parenthesis missing");
            return false;
        }
        curr = lex.next();
        if(curr == null || curr.getType() != TokenType.STRING){
            System.out.println("Parsing failed\nstring missing");
            return false;
        }
        curr = lex.next();
        if(curr == null || curr.getType() != TokenType.RPAREN){
            System.out.println("Parsing failed\nright parenthesis missing");
            return false;
        }
        return true;
    }

    private boolean if_stmt(){
        Token curr = lex.next();
        if(curr == null || curr.getValue().compareTo("if") != 0){
            System.out.println("Parsing failed\nif_stmt cannot disappear");
            return false;
        }
        curr = lex.next();
        if(curr == null || curr.getType() != TokenType.LPAREN){
            System.out.println("Parsing failed\n left parenthesis missing");
            return false;
        }
        if(!exp()) return false;
        curr = lex.next();
        if(curr == null || curr.getType() != TokenType.RPAREN){
            System.out.println("Parsing failed\nright parenthesis missing");
            return false;
        }
        curr = lex.next();
        if(curr == null || curr.getValue().compareTo("begin") != 0){
            System.out.println("Parsing failed\nbegin missing");
            return false;
        }
        if(!proc()) return false;
        curr = lex.next();
        if(curr == null || curr.getValue().compareTo("end") != 0){
            System.out.println("Parsing failed\nend missing");
            return false;
        }
        return else_stmt();
    }

    private boolean else_stmt(){
        Token curr = lex.next();
        if(curr == null) return true;
        if(curr.getValue().compareTo("else_if") == 0){
            curr = lex.next();
            if(curr == null || curr.getType() != TokenType.LPAREN){
                System.out.println("Parsing failed\nleft parenthesis missing");
                return false;
            }
            if(!exp()) return false;
            curr = lex.next();
            if(curr == null || curr.getType() != TokenType.RPAREN){
                System.out.println("Parsing failed\nright parenthesis missing");
                return false;
            }
            curr = lex.next();
            if(curr == null || curr.getValue().compareTo("begin") != 0){
                System.out.println("Parsing failed\nbegin missing");
                return false;
            }
            if(!proc()) return false;
            curr = lex.next();
            if(curr == null || curr.getValue().compareTo("end") != 0){
                System.out.println("Parsing failed\nend missing");
                return false;
            }
            return else_stmt();
        }
        else if(curr.getValue().compareTo("else") == 0){
            curr = lex.next();
            if(curr == null || curr.getValue().compareTo("begin") != 0){
                System.out.println("Parsing failed\nbegin missing");
                return false;
            }
            if(!proc()) return false;
            curr = lex.next();
            if(curr == null || curr.getValue().compareTo("end") != 0){
                System.out.println("Parsing failed\nend missing");
                return false;
            }
            return true;
        }else{
            lex.saveToken(curr);
            return true;
        }
    }

    private boolean id(){
        Token curr = lex.next();
        if(curr == null || curr.getType() != TokenType.IDENTIFIER){
            lex.saveToken(curr);
            System.out.println("Parsing failed\nid cannot disappear");
            return false;
        }
        return true;
    }

    private boolean number(){
        Token curr = lex.next();
        if(curr == null || curr.getType() != TokenType.NUMBER){
            lex.saveToken(curr);
            System.out.println("Parsing failed\nnumber cannot disappear");
            return false;
        }
        return true;
    }

    private boolean string(){
        Token curr = lex.next();
        if(curr == null || curr.getType() != TokenType.STRING){
            lex.saveToken(curr);
            System.out.println("Parsing failed\nstring cannot disappear");
            return false;
        }
        return true;
    }

    private boolean type(){
        Token curr = lex.next();
        if(curr == null || curr.getValue().compareTo("int") != 0){
            lex.saveToken(curr);
            System.out.println("Parsing failed\ntype cannot disappear");
            return false;
        }
        return true;
    }

    private boolean op(){
        Token curr = lex.next();
        if(curr == null || !TokenMatcher.matchToken(curr, NonTerminalTypes.OP)){
            lex.saveToken(curr);
            System.out.println("Parsing failed\nop cannot disappear");
            return false;
        }
        return true;
    }
}
