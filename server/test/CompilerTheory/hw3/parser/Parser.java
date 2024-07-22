package parser;

import parser.element.Element;
import parser.exceptions.ParsingFailedException;
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
        System.out.print("Generated Parsing Stack : [");
        for(int i = 0; i < parsingStack.size(); i++){
            if(i != 0) System.out.print(", ");
            if(parsingStack.elementAt(i).getType() == NonTerminalTypes.TERMINALS)
                System.out.print(parsingStack.elementAt(i).getValue());
            else{
                System.out.print(parsingStack.elementAt(i).getType().getName());
            }
        }
        System.out.println("]");
    }
    public void parse() throws ParsingFailedException{
        parsingStack.push(new Element(NonTerminalTypes.S));
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
            Element top = parsingStack.lastElement();

            switch(top.getType()){
                case S :
                    matchS(input);
                    printParsingStack();
                    break;
                case BEGIN_BLOCK:
                    matchBeginBlock(input);
                    printParsingStack();
                    break;
                case PGM_NAME:
                    matchPGMName(input);
                    printParsingStack();
                    break;
                case BODY:
                    matchBody(input);
                    printParsingStack();
                    break;
                case PROGRAM_BLOCK:
                    matchProgramBlock(input);
                    printParsingStack();
                    break;
                case STATEMENT:
                    matchStatement(input);
                    printParsingStack();
                    break;
                case EXPRESSION_UNARY:
                    matchExpressionUnary(input);
                    printParsingStack();
                    break;
                case UNARY:
                    matchUnary(input);
                    printParsingStack();
                    break;
                case DECLARATION_ID:
                    matchDeclarationId(input);
                    printParsingStack();
                    break;
                case CALL_ID:
                    matchCallId(input);
                    printParsingStack();
                    break;
                case TYPE:
                    matchType(input);
                    printParsingStack();
                    break;
                case DECLARATION_EXPRESSION:
                    matchDeclarationExpression(input);
                    printParsingStack();
                    break;
                case STATEMENT_SEQUENCE:
                    matchStatementSequence(input);
                    printParsingStack();
                    break;
                case EXPRESSION:
                    matchExpression(input);
                    printParsingStack();
                    break;
                case EXPRESSION2:
                    matchExpression2(input);
                    printParsingStack();
                    break;
                case OPERATOR:
                    matchOperator(input);
                    printParsingStack();
                    break;
                case OPERAND:
                    matchOperand(input);
                    printParsingStack();
                    break;
                case PARAMETER:
                    matchParameter(input);
                    printParsingStack();
                    break;
                case IF_STATEMENT:
                    matchIfStatement(input);
                    printParsingStack();
                    break;
                case WHILE_STATEMENT:
                    matchWhileStatement(input);
                    printParsingStack();
                    break;
                case FOR_STATEMENT:
                    matchForStatement(input);
                    printParsingStack();
                    break;
                case ELSE_STATEMENT:
                    matchElseStatement(input);
                    printParsingStack();
                    break;
                case FUNCTION:
                    matchFunction(input);
                    printParsingStack();
                    break;
                case ID:
                case STRING:
                case NUMBER:
                case TERMINALS:
                    matchTerminal(input);
                    break;
            }
        }
    }

    public void matchS(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("program") != 0){
            System.out.println("Cannot find keyword \"program\"");
            throw new ParsingFailedException();
        }
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.BODY));
        parsingStack.push(new Element(NonTerminalTypes.BEGIN_BLOCK));
        lex.saveToken(input);
    }

    public void matchBeginBlock(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("program") != 0){
            System.out.println("Cannot find keyword \"program\"");
            throw new ParsingFailedException();
        }
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.PGM_NAME));
        parsingStack.push(new Element("program"));
        lex.saveToken(input);

    }

    public void matchPGMName(Token input) throws ParsingFailedException{
        if(input.getType() != TokenType.IDENTIFIER){
            System.out.println("Cannot find \"identifier\"");
            throw new ParsingFailedException();
        }
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.ID));
        lex.saveToken(input);
    }

    public void matchBody(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("begin") != 0){
            System.out.println("Cannot find keyword \"begin\"");
            throw new ParsingFailedException();
        }
        parsingStack.pop();
        parsingStack.push(new Element("end"));
        parsingStack.push(new Element(NonTerminalTypes.PROGRAM_BLOCK));
        parsingStack.push(new Element("begin"));
        lex.saveToken(input);
    }

    public void matchProgramBlock(Token input) throws ParsingFailedException{
        if(input.getType() == TokenType.IDENTIFIER
                || input.getValue().compareTo("int") == 0
                || input.getValue().compareTo("integer") == 0
                || TokenMatcher.isFunctionId(input)){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.PROGRAM_BLOCK));
            parsingStack.push(new Element(";"));
            parsingStack.push(new Element(NonTerminalTypes.STATEMENT));
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("end") == 0){
            parsingStack.pop();
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("if")==0){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.PROGRAM_BLOCK));
            parsingStack.push(new Element(NonTerminalTypes.IF_STATEMENT));
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("for")==0){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.PROGRAM_BLOCK));
            parsingStack.push(new Element(NonTerminalTypes.FOR_STATEMENT));
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("while")==0){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.PROGRAM_BLOCK));
            parsingStack.push(new Element(NonTerminalTypes.WHILE_STATEMENT));
            lex.saveToken(input);
        }
        else if(input.getValue().compareTo("break")==0){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.PROGRAM_BLOCK));
            parsingStack.push(new Element(";"));
            parsingStack.push(new Element("break"));
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
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.ID));
        lex.saveToken(input);
        identifierTable.addTable(input.getValue());
    }

    public void matchCallId(Token input) throws ParsingFailedException{
        if(input.getType() != TokenType.IDENTIFIER){
            System.out.println("No matching Token for \"CALL_ID\"");
            throw new ParsingFailedException();
        }
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.ID));
        lex.saveToken(input);
        if(!identifierTable.searchTable(input.getValue())){
            System.out.println("identifier " + input.getValue() + " is not declared");
            throw new ParsingFailedException();
        }
    }
    public void matchStatement(Token input) throws ParsingFailedException{
        if(TokenMatcher.isFunctionId(input)){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.FUNCTION));
            lex.saveToken(input);
        }
        //TYPE id DECLARATION_EXPRESSION STATEMENT_SEQUENCE
        else if(input.getValue().compareTo("int") == 0 ||
                input.getValue().compareTo("integer") == 0){
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.STATEMENT_SEQUENCE));
            parsingStack.push(new Element(NonTerminalTypes.DECLARATION_EXPRESSION));
            parsingStack.push(new Element(NonTerminalTypes.DECLARATION_ID));
            parsingStack.push(new Element(NonTerminalTypes.TYPE));
            lex.saveToken(input);
        }
        //id EXPRESSION_UNARY
        else if(input.getType() == TokenType.IDENTIFIER) {
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.EXPRESSION_UNARY));
            parsingStack.push(new Element(NonTerminalTypes.CALL_ID));
            lex.saveToken(input);
        }
        else{
            System.out.println("No matching Token for \"STATEMENT\"");
            throw new ParsingFailedException();
        }
    }

    public void matchUnary(Token input) throws ParsingFailedException{
        if(input.getType() == TokenType.ADDITION){
            parsingStack.pop();
            parsingStack.push(new Element("+"));
            parsingStack.push(new Element("+"));
            lex.saveToken(input);
        }else if(input.getType() == TokenType.SUBTRACTION){
            parsingStack.pop();
            parsingStack.push(new Element("-"));
            parsingStack.push(new Element("-"));
            lex.saveToken(input);
        }else{
            System.out.println("No matching Token for \"UNARY\"");
            throw new ParsingFailedException();
        }
    }

    public void matchType(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("int") == 0 ){
            parsingStack.pop();
            parsingStack.push(new Element("int"));
            lex.saveToken(input);
        }
        if(input.getValue().compareTo("integer") == 0 ){
            parsingStack.pop();
            parsingStack.push(new Element("integer"));
            lex.saveToken(input);
        }
    }

    public void matchDeclarationExpression(Token input) throws ParsingFailedException{
        switch(input.getType()){
            case ASSIGNMENT:
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.EXPRESSION));
                parsingStack.push(new Element("="));
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
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.STATEMENT_SEQUENCE));
                parsingStack.push(new Element(NonTerminalTypes.DECLARATION_EXPRESSION));
                parsingStack.push(new Element(NonTerminalTypes.DECLARATION_ID));
                parsingStack.push(new Element(","));
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
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.EXPRESSION2));
                parsingStack.push(new Element(NonTerminalTypes.OPERAND));
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
            parsingStack.pop();
            parsingStack.push(new Element(NonTerminalTypes.EXPRESSION2));
            parsingStack.push(new Element(NonTerminalTypes.OPERAND));
            parsingStack.push(new Element(NonTerminalTypes.OPERATOR));
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
            parsingStack.pop();
            parsingStack.push(new Element(input.getValue()));
            lex.saveToken(input);
        }else{
            System.out.println("No matching Token for \"OPERATOR\"");
            throw new ParsingFailedException();
        }
    }

    public void matchOperand(Token input) throws ParsingFailedException{
        switch(input.getType()){
            case IDENTIFIER:
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.CALL_ID));
                lex.saveToken(input);
                break;
            case NUMBER:
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.NUMBER));
                lex.saveToken(input);
                break;
            default:
                System.out.println("No matching Token for \"OPERAND\"");
                throw new ParsingFailedException();
        }
    }

    public void matchParameter(Token input) throws ParsingFailedException{
        switch(input.getType()){
            case IDENTIFIER:
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.CALL_ID));
                lex.saveToken(input);
                break;
            case STRING:
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.STRING));
                lex.saveToken(input);
                break;
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
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.ELSE_STATEMENT));
        parsingStack.push(new Element(NonTerminalTypes.BODY));
        parsingStack.push(new Element(")"));
        parsingStack.push(new Element(NonTerminalTypes.EXPRESSION));
        parsingStack.push(new Element("("));
        parsingStack.push(new Element("if"));
        lex.saveToken(input);
    }

    public void matchForStatement(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("for") != 0){
            System.out.println("No matching Token for \"FOR_STATEMENT\"");
            throw new ParsingFailedException();
        }
        //for ( STATEMENT ; STATEMENT ; STATEMENT ) BODY
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.BODY));
        parsingStack.push(new Element(")"));
        parsingStack.push(new Element(NonTerminalTypes.STATEMENT));
        parsingStack.push(new Element(";"));
        parsingStack.push(new Element(NonTerminalTypes.EXPRESSION));
        parsingStack.push(new Element(";"));
        parsingStack.push(new Element(NonTerminalTypes.STATEMENT));
        parsingStack.push(new Element("("));
        parsingStack.push(new Element("for"));
        lex.saveToken(input);
    }

    public void matchWhileStatement(Token input) throws ParsingFailedException{
        if(input.getValue().compareTo("while") != 0){
            System.out.println("No matching Token for \"WHILE_STATEMENT\"");
            throw new ParsingFailedException();
        }
        //while ( EXPRESSION ) BODY
        parsingStack.pop();
        parsingStack.push(new Element(NonTerminalTypes.BODY));
        parsingStack.push(new Element(")"));
        parsingStack.push(new Element(NonTerminalTypes.EXPRESSION));
        parsingStack.push(new Element("("));
        parsingStack.push(new Element("while"));
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
            case "else_if": //else_if ( EXPRESSION ) BODY ELSE-STATEMENT
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.ELSE_STATEMENT));
                parsingStack.push(new Element(NonTerminalTypes.BODY));
                parsingStack.push(new Element(")"));
                parsingStack.push(new Element(NonTerminalTypes.EXPRESSION));
                parsingStack.push(new Element("("));
                parsingStack.push(new Element("else_if"));
                lex.saveToken(input);
                break;
            case "else": //else BODY
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.BODY));
                parsingStack.push(new Element("else"));
                lex.saveToken(input);
                break;
            default:
                System.out.println("No matching Token for \"ELSE_STATEMENT\"");
                throw new ParsingFailedException();
        }
    }

    public void matchExpressionUnary(Token input) throws ParsingFailedException{
        switch(input.getValue()){
            case "=":
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.EXPRESSION));
                parsingStack.push(new Element("="));
                lex.saveToken(input);
                break;
            case "+":
            case "-":
                parsingStack.pop();
                parsingStack.push(new Element(NonTerminalTypes.UNARY));
                lex.saveToken(input);
                break;
            default:
                System.out.println("No matching Token for \"EXPRESSION_UNARY\"");
                throw new ParsingFailedException();
        }
    }

    public void matchFunction(Token input) throws ParsingFailedException{
        if(TokenMatcher.isFunctionId(input)){
            //function_id ( PARAMETER )
            parsingStack.pop();
            parsingStack.push(new Element(")"));
            parsingStack.push(new Element(NonTerminalTypes.PARAMETER));
            parsingStack.push(new Element("("));
            parsingStack.push(new Element(input.getValue()));
            lex.saveToken(input);
        }
        else{
            System.out.println("No matching Token for \"FUNCTION\"");
            throw new ParsingFailedException();
        }
    }



    public void matchTerminal(Token input) throws ParsingFailedException{
        Element top = parsingStack.lastElement();
        switch (top.getType()) {
            case ID:
                if (input.getType() != TokenType.IDENTIFIER) {
                    System.out.println("Cannot find identifier");
                    throw new ParsingFailedException();
                }
                System.out.println("Matched Identifier : " + input.getValue());
                break;
            case STRING:
                if (input.getType() != TokenType.STRING) {
                    System.out.println("Cannot find string");
                    throw new ParsingFailedException();
                }
                System.out.println("Matched String : " + input.getValue());
                break;
            case NUMBER:
                if (input.getType() != TokenType.NUMBER) {
                    System.out.println("Cannot find number");
                    throw new ParsingFailedException();
                }
                System.out.println("Matched Number : " + input.getValue());
                break;
            case TERMINALS:
                if (top.getValue().compareTo(input.getValue()) != 0) {
                    System.out.println("Cannot find \"" + top.getValue() + "\"");
                    throw new ParsingFailedException();
                }
                if(TokenMatcher.isKeyword(input)) {
                    System.out.println("Matched Keyword : " + input.getValue());
                }
                else if(TokenMatcher.isOP(input.getType()) || input.getType() == TokenType.ASSIGNMENT){
                    System.out.println("Matched Operator : " + input.getValue());
                }
                else{
                    System.out.println("Matched Special Character : " + input.getValue());
                }
                break;
        }
        parsingStack.pop();
    }
}
