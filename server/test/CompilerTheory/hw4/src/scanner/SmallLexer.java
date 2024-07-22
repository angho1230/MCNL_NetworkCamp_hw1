package scanner;

import scanner.lexer.Lexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;

public class SmallLexer {
    private Lexer lex;
    private Token saved = null;

    public SmallLexer(String path){
        lex = new Lexer(path);
    }
    public Token next(){
        if(saved != null){
            Token r = saved;
            saved = null;
            return r;
        }
        Token t = lex.next();
        if(t == null) return null;
        if(t.getType() == TokenType.COMMENT || t.getType() == TokenType.CONTROL){
            return next();
        }
        return t;
    }
    public void saveToken(Token token){
        this.saved = token;
    }
}
