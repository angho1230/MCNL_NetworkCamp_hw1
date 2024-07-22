package scanner.lexer;

import scanner.textIO.TextReader;

public class Lexer{
    TextReader tr;
    public Lexer(String path){
        tr = new TextReader(path);
        tr.nextLine();
    }
    public Token next(){
        if(tr.isCurrentLineNull()){
            return null;
        }
        Token token = new Token();
        while(true){
            State s;
            char c;
            if(tr.isEnd()){
                c = '\n';
                tr.nextLine();
            }
            else if(tr.isCurrentLineNull()){
                token.setType();
                return token;
            }
            else{
                c = tr.next();
            }
            if((s = token.getNextState(c)) == State.START){
                if(token.getState() == State.START) {
                    token.addValue(c);
                    token.setType();
                    return token;
                }
                tr.setIndex(tr.getIndex()-1);
                token.setType();
                return token;
            }
            token.addValue(c);
        }
    }
    public void close(){
        tr.close();
    }
}


/*
        while(!tr.isEnd() || tr.nextLine() != null) {
            char c;
            while ((c = tr.next()) != '\0') {
                if (token.addValue(c) == -1) {
                    tr.setIndex(tr.getIndex() - 1);
                    token.setType();
                    return token;
                }
            }
        }
        return null;*/