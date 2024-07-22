package parser;

import parser.element.Element;
import parser.exceptions.ParsingFailedException;
import parser.syntaxDirectedTranslation.Translator;
import parser.table.IdentifierTable;
import parser.types.NonTerminalTypes;
import scanner.SmallLexer;
import scanner.lexer.Token;
import scanner.lexer.types.TokenType;

import java.util.Stack;
public class Parser {
    private SmallLexer lex;

    public Parser(String path){
        this.lex = new SmallLexer(path);
    }

    private Stack<Element> parsingStack = new Stack<>();
    private IdentifierTable identifierTable = new IdentifierTable();
    public void printParsingStack(){
        System.out.print("[");
        for(int i = 0; i < parsingStack.size(); i++){
            if(i != 0) System.out.print(", ");
            if(parsingStack.elementAt(i).getType() == NonTerminalTypes.TERMINALS)
                System.out.print(parsingStack.elementAt(i).getValue());
            else{
                System.out.print(parsingStack.elementAt(i).getType().getName());
            }
        }
        System.out.print("]");
    }
    public void parse() throws ParsingFailedException{
        parsingStack.push(new Element(NonTerminalTypes.S));
        Element start = parsingStack.firstElement();
        while(true){
            Token input = lex.next();
            if(input == null && parsingStack.empty()) {
                break;
            }else if(input == null){
                System.out.println("Error) input reached EOS while parsing stack remaining");
                throw new ParsingFailedException();
            }
            else if(parsingStack.empty()){
                System.out.println("Error) parsing stack is empty while input token is remaining");
                throw new ParsingFailedException();
            }
            //printParsingStack();
            Element top = parsingStack.lastElement();
            //System.out.println("\t" + input.getValue());

            switch(top.getType()){
                case S :
                    matchS(input);
                    break;
                case BEGIN_BLOCK:
                    matchBeginBlock(input);
                    break;
                case PGM_NAME:
                    matchPGMName(input);
                    break;
                case BODY:
                    matchBody(input);
                    break;
                case PROGRAM_BLOCK:
                    matchProgramBlock(input);
                    break;
                case STATEMENT:
                    matchStatement(input);
                    break;
                case EXPRESSION_UNARY:
                    matchExpressionUnary(input);
                    break;
                case UNARY:
                    matchUnary(input);
                    break;
                case DECLARATION_ID:
                    matchDeclarationId(input);
                    break;
                case CALL_ID:
                    matchCallId(input);
                    break;
                case TYPE:
                    matchType(input);
                    break;
                case DECLARATION_EXPRESSION:
                    matchDeclarationExpression(input);
                    break;
                case STATEMENT_SEQUENCE:
                    matchStatementSequence(input);
                    break;
                case EXPRESSION:
                    matchExpression(input);
                    break;
                case EXPRESSION2:
                    matchExpression2(input);
                    break;
                case OPERATOR:
                    matchOperator(input);
                    break;
                case OPERAND:
                    matchOperand(input);
                    break;
                case PARAMETER:
                    matchParameter(input);
                    break;
                case IF_STATEMENT:
                    matchIfStatement(input);
                    break;
                case WHILE_STATEMENT:
                    matchWhileStatement(input);
                    break;
                case FOR_STATEMENT:
                    matchForStatement(input);
                    break;
                case ELSE_STATEMENT:
                    matchElseStatement(input);
                    break;
                case FUNCTION:
                    matchFunction(input);
                    break;
                case ID:
                case STRING:
                case NUMBER:
                case TERMINALS:
                    matchTerminal(input);
                    break;
            }
        }
        Translator translator = new Translator(identifierTable, start);
        translator.translate(start);
    }

    public void matchS(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("program") != 0){
            System.out.println("Cannot find keyword \"program\"");
            throw new ParsingFailedException();
        }
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.BODY);
        e.elements[1] = new Element(NonTerminalTypes.BEGIN_BLOCK);
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
    }

    public void matchBeginBlock(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("program") != 0){
            System.out.println("Cannot find keyword \"program\"");
            throw new ParsingFailedException();
        }
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.PGM_NAME);
        e.elements[1] = new Element("program");
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);

    }

    public void matchPGMName(Token input) throws ParsingFailedException{
        if(input.getType() != TokenType.IDENTIFIER){
            System.out.println("Cannot find \"identifier\"");
            throw new ParsingFailedException();
        }
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.ID);
        for(int i = 0; i < e.elements.length; i++) {
            if (e.elements[i] == null) {
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
    }

    public void matchBody(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("begin") != 0){
            System.out.println("Cannot find keyword \"begin\"");
            throw new ParsingFailedException();
        }
        Element e = parsingStack.pop();
        e.elements[0] = new Element("end");
        e.elements[1] = new Element(NonTerminalTypes.PROGRAM_BLOCK);
        e.elements[2] = new Element("begin");
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
    }

    public void matchProgramBlock(Token input) throws ParsingFailedException{
        if(input.getType() == TokenType.IDENTIFIER
                || input.getValue().compareTo("int") == 0
                || input.getValue().compareTo("integer") == 0
                || TokenMatcher.isFunctionId(input)){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.PROGRAM_BLOCK);
            e.elements[1] = new Element(";");
            e.elements[2] = new Element(NonTerminalTypes.STATEMENT);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("end") == 0){
            parsingStack.pop();
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("if")==0){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.PROGRAM_BLOCK);
            e.elements[1] = new Element(NonTerminalTypes.IF_STATEMENT);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("for")==0){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.PROGRAM_BLOCK);
            e.elements[1] = new Element(NonTerminalTypes.FOR_STATEMENT);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("while")==0){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.PROGRAM_BLOCK);
            e.elements[1] = new Element(NonTerminalTypes.WHILE_STATEMENT);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("break")==0){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.PROGRAM_BLOCK);
            e.elements[1] = new Element(";");
            e.elements[2] = new Element("break");
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else{
            System.out.println("No matching Token for \"PROGRAM_BLOCK\"");
            throw new ParsingFailedException();
        }
    }
    public void matchDeclarationId(Token input) throws ParsingFailedException{
        if(input.getType() != TokenType.IDENTIFIER){
            System.out.println("No matching Token for \"Declaration_ID\"");
            throw new ParsingFailedException();
        }
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.ID);
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
        identifierTable.addTable(input.getValue());
    }

    public void matchCallId(Token input) throws ParsingFailedException{
        if(input.getType() != TokenType.IDENTIFIER){
            System.out.println("No matching Token for \"CALL_ID\"");
            throw new ParsingFailedException();
        }
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.ID);
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
        if(!identifierTable.searchTable(input.getValue())){
            System.out.println("identifier " + input.getValue() + " is not declared");
            throw new ParsingFailedException();
        }
    }
    public void matchStatement(Token input) throws ParsingFailedException{
        if(TokenMatcher.isFunctionId(input)){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.FUNCTION);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        //TYPE id DECLARATION_EXPRESSION STATEMENT_SEQUENCE
        else if(input.getValue().compareTo("int") == 0 ||
                input.getValue().compareTo("integer") == 0){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.STATEMENT_SEQUENCE);
            e.elements[1] = new Element(NonTerminalTypes.DECLARATION_EXPRESSION);
            e.elements[2] = new Element(NonTerminalTypes.DECLARATION_ID);
            e.elements[3] = new Element(NonTerminalTypes.TYPE);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        //id EXPRESSION_UNARY
        else if(input.getType() == TokenType.IDENTIFIER) {
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.EXPRESSION_UNARY);
            e.elements[1] = new Element(NonTerminalTypes.CALL_ID);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else{
            System.out.println("No matching Token for \"STATEMENT\"");
            throw new ParsingFailedException();
        }
    }

    public void matchUnary(Token input) throws ParsingFailedException{
        if(input.getType() == TokenType.ADDITION){
            Element e = parsingStack.pop();
            e.elements[0] = new Element("+");
            e.elements[1] = new Element("+");
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }else if(input.getType() == TokenType.SUBTRACTION){
            Element e = parsingStack.pop();
            e.elements[0] = new Element("-");
            e.elements[1] = new Element("-");
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }else{
            System.out.println("No matching Token for \"UNARY\"");
            throw new ParsingFailedException();
        }
    }

    public void matchType(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("int") == 0 ){
            Element e = parsingStack.pop();
            e.elements[0] = new Element("int");
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        if(input.getValue().compareTo("integer") == 0 ){
            Element e = parsingStack.pop();
            e.elements[0] = new Element("integer");
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
    }

    public void matchDeclarationExpression(Token input) throws ParsingFailedException{
        switch(input.getType()){
            case ASSIGNMENT:
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.EXPRESSION);
                e.elements[1] = new Element("=");
                for(int i = 0; i < e.elements.length; i++){
                    if(e.elements[i] == null){
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            case SEMICOLON:
            case COMMA:
            case RPAREN:
                lex.saveToken(input);
                parsingStack.pop();
                break;
            default:
                System.out.println("No matching Token for \"DECLARATION_EXPRESSION\"");
                throw new ParsingFailedException();
        }
    }

    public void matchStatementSequence(Token input) throws ParsingFailedException{
        switch(input.getType()){
            //, id DECLARATION_EXPRESSION STATEMENT_SEQUENCE
            case COMMA:
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.STATEMENT_SEQUENCE);
                e.elements[1] = new Element(NonTerminalTypes.DECLARATION_EXPRESSION);
                e.elements[2] = new Element(NonTerminalTypes.DECLARATION_ID);
                e.elements[3] = new Element(",");
                for(int i = 0; i < e.elements.length; i++){
                    if(e.elements[i] == null){
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                };
                lex.saveToken(input);
                break;
            case SEMICOLON:
            case RPAREN:
            case IDENTIFIER:
                parsingStack.pop();
                lex.saveToken(input);
                break;
            default:
                System.out.println("No matching Token for \"STATEMENT_SEQUENCE\"");
                throw new ParsingFailedException();
        }
    }

    public void matchExpression(Token input) throws ParsingFailedException{
        switch(input.getType()){
            //OPERAND EXPRESSION2
            case NUMBER:
            case IDENTIFIER:
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.EXPRESSION2);
                e.elements[1] = new Element(NonTerminalTypes.OPERAND);
                for(int i = 0; i < e.elements.length; i++){
                    if(e.elements[i] == null){
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            default:
                System.out.println("No matching Token for \"EXPRESSION\"");
                throw new ParsingFailedException();
        }
    }

    public void matchExpression2(Token input) throws ParsingFailedException{
        //OPERATOR OPERAND EXPRESSION2
        if(TokenMatcher.isOP(input.getType())){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(NonTerminalTypes.EXPRESSION2);
            e.elements[1] = new Element(NonTerminalTypes.OPERAND);
            e.elements[2] = new Element(NonTerminalTypes.OPERATOR);
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }else if(input.getType() == TokenType.COMMA || input.getType() == TokenType.RPAREN || input.getType() == TokenType.SEMICOLON){
            parsingStack.pop();
            lex.saveToken(input);
        }else{
            System.out.println("No matching Token for \"EXPRESSION2\"");
            throw new ParsingFailedException();
        }
    }

    public void matchOperator(Token input) throws ParsingFailedException{
        if(TokenMatcher.isOP(input.getType())){
            Element e = parsingStack.pop();
            e.elements[0] = new Element(input.getValue());
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }else{
            System.out.println("No matching Token for \"OPERATOR\"");
            throw new ParsingFailedException();
        }
    }

    public void matchOperand(Token input) throws ParsingFailedException{
        switch(input.getType()){
            case IDENTIFIER: {
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.CALL_ID);
                for (int i = 0; i < e.elements.length; i++) {
                    if (e.elements[i] == null) {
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            }
            case NUMBER:{
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.NUMBER);
                for(int i = 0; i < e.elements.length; i++){
                    if(e.elements[i] == null){
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
                }
            default:
                System.out.println("No matching Token for \"OPERAND\"");
                throw new ParsingFailedException();
        }
    }

    public void matchParameter(Token input) throws ParsingFailedException{
        switch(input.getType()){
            case IDENTIFIER: {
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.CALL_ID);
                for (int i = 0; i < e.elements.length; i++) {
                    if (e.elements[i] == null) {
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            }
            case STRING: {
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.STRING);
                for (int i = 0; i < e.elements.length; i++) {
                    if (e.elements[i] == null) {
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            }
            default:
                System.out.println("No matching Token for \"PARAMETER\"");
                throw new ParsingFailedException();
        }
    }

    public void matchIfStatement(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("if") != 0){
            System.out.println("No matching Token for \"IF_STATEMENT\"");
            throw new ParsingFailedException();
        }
        //if ( EXPRESSION ) BODY ELSE-STATEMENT
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.ELSE_STATEMENT);
        e.elements[1] = new Element(NonTerminalTypes.BODY);
        e.elements[2] = new Element(")");
        e.elements[3] = new Element(NonTerminalTypes.EXPRESSION);
        e.elements[4] = new Element("(");
        e.elements[5] = new Element("if");
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
    }

    public void matchForStatement(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("for") != 0){
            System.out.println("No matching Token for \"FOR_STATEMENT\"");
            throw new ParsingFailedException();
        }
        //for ( STATEMENT ; STATEMENT ; STATEMENT ) BODY
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.BODY);
        e.elements[1] = new Element(")");
        e.elements[2] = new Element(NonTerminalTypes.STATEMENT);
        e.elements[3] = new Element(";");
        e.elements[4] = new Element(NonTerminalTypes.EXPRESSION);
        e.elements[5] = new Element(";");
        e.elements[6] = new Element(NonTerminalTypes.STATEMENT);
        e.elements[7] = new Element("(");
        e.elements[8] = new Element("for");
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
    }

    public void matchWhileStatement(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("while") != 0){
            System.out.println("No matching Token for \"WHILE_STATEMENT\"");
            throw new ParsingFailedException();
        }
        //while ( EXPRESSION ) BODY
        Element e = parsingStack.pop();
        e.elements[0] = new Element(NonTerminalTypes.BODY);
        e.elements[1] = new Element(")");
        e.elements[2] = new Element(NonTerminalTypes.EXPRESSION);
        e.elements[3] = new Element("(");
        e.elements[4] = new Element("while");
        for(int i = 0; i < e.elements.length; i++){
            if(e.elements[i] == null){
                break;
            }
            parsingStack.push(e.elements[i]);
        }
        lex.saveToken(input);
    }

    public void matchElseStatement(Token input) throws ParsingFailedException{
        if(TokenMatcher.isFunctionId(input) || input.getType() == TokenType.IDENTIFIER){
            parsingStack.pop();
        }
        switch(input.getValue()){
            case "int":
            case "while":
            case "if":
            case "for":
            case "end":
                lex.saveToken(input);
                parsingStack.pop();
                break;
            case "else_if": { //else_if ( EXPRESSION ) BODY ELSE-STATEMENT
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.ELSE_STATEMENT);
                e.elements[1] = new Element(NonTerminalTypes.BODY);
                e.elements[2] = new Element(")");
                e.elements[3] = new Element(NonTerminalTypes.EXPRESSION);
                e.elements[4] = new Element("(");
                e.elements[5] = new Element("else_if");
                for (int i = 0; i < e.elements.length; i++) {
                    if (e.elements[i] == null) {
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            }
            case "else": {//else BODY
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.BODY);
                e.elements[1] = new Element("else");
                for(int i = 0; i < e.elements.length; i++){
                    if(e.elements[i] == null){
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
                }
            default:
                System.out.println("No matching Token for \"ELSE_STATEMENT\"");
                throw new ParsingFailedException();
        }
    }

    public void matchExpressionUnary(Token input) throws ParsingFailedException{
        switch(input.getValue()){
            case "=": {
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.EXPRESSION);
                e.elements[1] = new Element("=");
                for (int i = 0; i < e.elements.length; i++) {
                    if (e.elements[i] == null) {
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                break;
            }
            case "+":
            case "-": {
                Element e = parsingStack.pop();
                e.elements[0] = new Element(NonTerminalTypes.UNARY);
                for(int i = 0; i < e.elements.length; i++){
                    if(e.elements[i] == null){
                        break;
                    }
                    parsingStack.push(e.elements[i]);
                }
                lex.saveToken(input);
                lex.saveToken(input);
                break;
                }
            default:
                System.out.println("No matching Token for \"EXPRESSION_UNARY\"");
                throw new ParsingFailedException();
        }
    }

    public void matchFunction(Token input) throws ParsingFailedException{
        if(TokenMatcher.isFunctionId(input)){
            //function_id ( PARAMETER )
            Element e = parsingStack.pop();
            e.elements[0] = new Element(")");
            e.elements[1] = new Element(NonTerminalTypes.PARAMETER);
            e.elements[2] = new Element("(");
            e.elements[3] = new Element(input.getValue());
            for(int i = 0; i < e.elements.length; i++){
                if(e.elements[i] == null){
                    break;
                }
                parsingStack.push(e.elements[i]);
            }
            lex.saveToken(input);
        }
        else{
            System.out.println("No matching Token for \"FUNCTION\"");
            throw new ParsingFailedException();
        }
    }



    public void matchTerminal(Token input) throws ParsingFailedException{
        Element top = parsingStack.lastElement();
        top.setValue(input.getValue());
        switch (top.getType()) {
            case ID:
                if (input.getType() != TokenType.IDENTIFIER) {
                    System.out.println("Cannot find identifier");
                    throw new ParsingFailedException();
                }
                //System.out.println("Matched Identifier : " + input.getValue());
                break;
            case STRING:
                if (input.getType() != TokenType.STRING) {
                    System.out.println("Cannot find string");
                    throw new ParsingFailedException();
                }
                //System.out.println("Matched String : " + input.getValue());
                break;
            case NUMBER:
                if (input.getType() != TokenType.NUMBER) {
                    System.out.println("Cannot find number");
                    throw new ParsingFailedException();
                }
                //System.out.println("Matched Number : " + input.getValue());
                break;
            case TERMINALS:
                if (top.getValue().compareTo(input.getValue()) != 0) {
                    System.out.println("Cannot find \"" + top.getValue() + "\"");
                    throw new ParsingFailedException();
                }
                if(TokenMatcher.isKeyword(input)) {
                    //System.out.println("Matched Keyword : " + input.getValue());
                }
                else if(TokenMatcher.isOP(input.getType()) || input.getType() == TokenType.ASSIGNMENT){
                    //System.out.println("Matched Operator : " + input.getValue());
                }
                else{
                    //System.out.println("Matched Special Character : " + input.getValue());
                }
                break;
        }
        parsingStack.pop();
    }
}
