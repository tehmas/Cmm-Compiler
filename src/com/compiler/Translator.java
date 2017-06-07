package com.compiler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Translator {

    ArrayList<String> tokenList;
    ArrayList<String> lexemeList;

    Hashtable<String, String> opCodes;

    SymbolTable symbolTable;

    FileOutputStream codeFile;
    FileOutputStream tableFile;

    String curr;
    int tokenIndex;

    String SPACE = " ";
    String DELIMITER = "\n";

    Temp temp;

    int lineNo = 0;

    public class Temp{
        int intCount;

        public Temp(){
            intCount = 0;
        }

        public String newIntTemp(){
            String symbol = "t" + String.valueOf(intCount);
            intCount++;
            symbolTable.addSymbol(symbol, "INT");

            return symbol;
        }
    }

    public Translator(){
        symbolTable = new SymbolTable();

        opCodes = new Hashtable<String, String>();
        opCodes.put("in", "0");
        opCodes.put("out","1");
        opCodes.put("goto","2");
        opCodes.put("IFGE","3");
        opCodes.put("*","42");
        opCodes.put("+","43");
        opCodes.put("-","45");
        opCodes.put("/","47");
        opCodes.put("=","61");

        temp = new Temp();
        tokenIndex = 0;
    }

    public boolean setInput(ArrayList<String> tokenList, ArrayList<String> lexemeList){
        if(tokenList.size() != lexemeList.size())
            return false;

        this.tokenList = tokenList;
        this.lexemeList = lexemeList;

        return true;
    }

    void match(String token) {
        if (curr.equals(token)) {
            tokenIndex += 1;
            if (tokenIndex < tokenList.size())
                curr = tokenList.get(tokenIndex);
        } else {
            System.out.print("Error!");
        }
    }

    public void translate(){
        try {
            codeFile = new FileOutputStream("machine_code.txt");
            curr = tokenList.get(tokenIndex);

            Program();

            codeFile.close();

            tableFile = new FileOutputStream("symbol_table.txt");

            Hashtable addressTable = symbolTable.getAddressTable();
            Set<String> addressKeys = addressTable.keySet();

            Hashtable typeTable = symbolTable.getTypeTable();
            Set<String> typeKeys = typeTable.keySet();

            Iterator<String> addressIterator = addressKeys.iterator();
            Iterator<String> typeIterator = typeKeys.iterator();

            while (addressIterator.hasNext()) {

                String name = addressIterator.next();
                String type = typeIterator.next();
                String address = symbolTable.getAddress(name);

                // Write name
                tableFile.write(name.getBytes());
                tableFile.write(SPACE.getBytes());

                // Write address
                tableFile.write(address.getBytes());
                tableFile.write(DELIMITER.getBytes());

            }
            tableFile.close();

        }

        catch(IOException e){
            e.printStackTrace();
        }
    }

    void Type() {

        if (curr.equals("VOID")) {
            match("VOID");
        } else if (curr.equals("INT")) {
            match("INT");
        } else if (curr.equals("CHAR")) {
            match("CHAR");
        }
        
    }

    void VariableDeclaration() {

        String type = tokenList.get(tokenIndex-1);
        Type();
        if (curr.equals("ID")) {
            match("ID");
            symbolTable.addSymbol(lexemeList.get(tokenIndex-1),type);
            if (curr.equals(";")) {
                match(";");
            }
        }
        
    }

    void AssignmentStatement(String input) {

        if (curr.equals("AS")) {
            match("AS");
            String t = Expression();
            Emit("=",t, input);
            if (curr.equals(";")) {
                match(";");
            }
        }
        
    }

    String Expression() {

        String term = Term();
        String expression = ExpressionPrime(term);

        return expression;
        
    }

    String ExpressionPrime(String input) {

        if (curr.equals("+")) {
            match("+");
            String term = Term();
            String t = temp.newIntTemp();
            Emit("+",input, term,t);
            return ExpressionPrime(t);
        } else if (curr.equals("-")) {
            match("-");
            String term = Term();
            String t = temp.newIntTemp();
            Emit("-",input, term,t);
            return ExpressionPrime(t);
        } else ;

        return input;
    }

    String Term() {

        String factor = Factor();
        String t = TermPrime(factor);
        return t;
        
    }

    String Factor() {
        
        if (curr.equals("ID")) {
            match("ID");
            return lexemeList.get(tokenIndex-1);
        } else if (curr.equals("NC")) {
            match("NC");
            return lexemeList.get(tokenIndex-1);
        } else if (curr.equals("(")) {
            match("(");
            String expression = Expression();
            if (curr.equals(")")) {
                match(")");
                return expression;
            }
        }
        return "error in Factor()";
    }

    String TermPrime(String input) {

        if (curr.equals("*")) {
            match("*");
            String factor = Factor();
            String t = temp.newIntTemp();
            Emit("*",input, factor, t);
            return TermPrime(t);
        } else if (curr.equals("/")) {
            match("/");
            String factor = Factor();
            String t = temp.newIntTemp();
            Emit("/",input, factor, t);
            return TermPrime(t);
        } else ;

        return input;
    }

    void BooleanExpression() {
        // TRUE and FALSE are just for future purposes
        // because currently there is only int, char and void types

        if (curr.equals("TRUE")) {
            match("TRUE");
        } else if (curr.equals("FALSE")) {
            match("FALSE");
        } else {
            String lExpression = Expression();
            if (curr.equals("RO")) {
                match("RO");
                String rExpression = Expression();
                EmitContinued("IFGE", lExpression, rExpression, lineNo);
            }
        }
        
    }

    void ConditionalStatement() {

        if (curr.equals("IF")) {
            match("IF");
            if (curr.equals("(")) {
                match("(");
                BooleanExpression();
                int ifLine = lineNo;
                Emit("goto");
                int gotoLine = lineNo;
                if (curr.equals(")")) {
                    match(")");
                    CodeChoice();
                    //Emit("goto");
                    //int gotoLine2 = lineNo;
                    //backPatch(gotoLine2, ifLine);
                    backPatch(gotoLine, lineNo+1+1);
                    OptionalElse();
                }
            }
        }
        
    }

    void backPatch(int s, int e){

        try {
            codeFile.close();

            Path path = Paths.get("machine_code.txt");
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            lines.set(s, lines.get(s).replaceAll("WAITING", String.valueOf(e)));

            codeFile = new FileOutputStream("machine_code.txt");
            int size = lines.size();
            for(int z = 0; z < size; z++){
                codeFile.write(lines.get(z).getBytes());
                codeFile.write(DELIMITER.getBytes());
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    void OptionalElse() {

        if (curr.equals("ELSE")) {
            match("ELSE");
            ExtendedCondition();
        } else ;
        
    }

    void ExtendedCondition() {

        if (curr.equals("IF")) {
            ConditionalStatement();
        } else if (curr.equals("{")) {
            CodeChoice();
        }
        
    }

    void Loop() {

        if (curr.equals("WHILE")) {
            match("WHILE");
            if (curr.equals("(")) {
                match("(");
                BooleanExpression();
                int ifLine = lineNo;
                Emit("goto");
                int gotoLine = lineNo;
                if (curr.equals(")")) {
                    match(")");
                    CodeChoice();
                    Emit("goto");
                    int gotoLine2 = lineNo;
                    backPatch(gotoLine2, ifLine +1);
                    backPatch(gotoLine, lineNo+1+1);
                }
            }
        }
        
    }

    void Input() {

        if (curr.equals("CIN")) {
            match("CIN");

                if (curr.equals("ID")) {
                    match("ID");

                    Emit("in",lexemeList.get(tokenIndex-1));
                    if (curr.equals(";")) {
                        match(";");
                    }
                }

        }
        
    }

    void Emit(String command, String symbol) {
        String str = opCodes.get(command) + SPACE + symbolTable.getAddress(symbol) + DELIMITER;

        try{
            codeFile.write(str.getBytes());
        }

        catch (IOException e){
            e.printStackTrace();
        }

        lineNo += 1;
    }

    void Emit(String command, String source, String dest) {
        String str = opCodes.get(command) + SPACE + symbolTable.getAddress(source);
        str +=  SPACE + symbolTable.getAddress(dest) + DELIMITER;

        try{
            codeFile.write(str.getBytes());
        }

        catch (IOException e){
            e.printStackTrace();
        }

        lineNo += 1;
    }

    void Emit(String command, String var1, String var2, String var3){
        String address1 = symbolTable.getAddress(var1);
        String address2 = symbolTable.getAddress(var2);
        String address3 = symbolTable.getAddress(var3);

        String str = opCodes.get(command) + SPACE + address1 + SPACE;
        str += address2 + SPACE;
        str += address3 + DELIMITER;

        try{
            codeFile.write(str.getBytes());
        }

        catch (IOException e){
            e.printStackTrace();
        }

        lineNo += 1;
    }

    void Emit(String command){
        if(command == "goto"){
            String str = opCodes.get("goto") + SPACE + "WAITING" + DELIMITER;

            try{
                codeFile.write(str.getBytes());
            }

            catch (IOException e){
                e.printStackTrace();
            }

            lineNo += 1;
        }
    }
    void EmitContinued(String command, String var1, String var2, int lineNo){
        String address1 = symbolTable.getAddress(var1);
        String address2 = symbolTable.getAddress(var2);

        String str = opCodes.get("IFGE") + SPACE + address1;
        str += SPACE + address2;
        //skip curr and next line and the lines are starting from 1
        str += SPACE + String.valueOf(lineNo + 2 + 1) + DELIMITER;

        try{
            codeFile.write(str.getBytes());
        }

        catch (IOException e){
            e.printStackTrace();
        }

        lineNo += 1;
    }

    void Output() {

        if (curr.equals("COUT")) {
            match("COUT");

            String output = OutputChoices();

            if(curr.equals(";")){
                match(";");
                Emit("out",output);
            }

        }

    }

    String OutputChoices() {

        return Expression();
        
    }

    void ReturnStatement() {

        if (curr.equals("RETURN")) {
            match("RETURN");
            ReturnChoices();
            if (curr.equals(";")) {
                match(";");
            }
        }
        

    }

    void ReturnChoices() {

        if (curr.equals("ID") || curr.equals("LC") || curr.equals("(")) {
            Expression();
        } else ;
        
    }

    void FunctionCall() {

        if (curr.equals("(")) {
            match("(");
            ArgumentList();
            if (curr.equals(")")) {
                match(")");

                if (curr.equals(";")) {
                    match(";");
                }
            }
        }
        
    }

    void ArgumentList() {

        if (curr.equals("ID")) {
            match("ID");
            AdditionalArgument();
        }
        
    }

    void AdditionalArgument() {

        if (curr.equals(",")) {
            match(",");
            ArgumentList();
        } else ;

        
    }

    void FunctionDefinition() {

        Type();
        if (curr.equals("ID")) {
            match("ID");

            if (curr.equals("(")) {
                match("(");
                ParameterList();

                if (curr.equals(")")) {
                    match(")");
                    if (curr.equals("{")) {
                        match("{");
                        Multiline();

                        if (curr.equals("}")) {
                            match("}");
                        }
                    }
                }
            }
        }

        
    }

    void ParameterList() {

        Type();
        OptinalArrayType();
        if (curr.equals("ID")) {
            match("ID");
            AdditionalParameters();
        } else ;
        
    }

    void OptinalArrayType() {

        if (curr.equals("[")) {
            match("[");

            if (curr.equals("]")) {
                match("]");
            }
        } else ;
        
    }

    void AdditionalParameters() {

        if (curr.equals(",")) {
            match(",");
            Type();
            OptinalArrayType();
            if (curr.equals("ID")) {
                match("ID");
                AdditionalParameters();
            }
        } else ;

        
    }

    void Program() {

        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR")) {
            FunctionDefinition();
            Program();
        }
        else ;
        
    }

    void CodeChoice() {

        if (curr.equals("{")) {
            match("{");
            Multiline();

            if (curr.equals("}")) {
                match("}");
            }
        } else if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR") ||
                curr.equals("WHILE") || curr.equals("CIN") || curr.equals("COUT") ||
                curr.equals("RETURN") || curr.equals("IF") || curr.equals("ID")) {
            SingleLine();
        }
        
    }

    void Multiline() {

        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR") ||
                curr.equals("WHILE") || curr.equals("CIN") || curr.equals("COUT") ||
                curr.equals("RETURN") || curr.equals("IF") || curr.equals("ID")) {
            SingleLine();
            Multiline();
        } else ;
    }

    void SingleLine() {

        if (curr.equals("INT") || curr.equals("VOID") || curr.equals("CHAR")) {
            VariableDeclaration();
        } else if (curr.equals("WHILE")) {
            Loop();
        } else if (curr.equals("CIN")) {
            Input();
        } else if (curr.equals("COUT")) {
            Output();
        } else if (curr.equals("RETURN")) {
            ReturnStatement();
        } else if (curr.equals("IF")) {
            ConditionalStatement();
        } else if (curr.equals("ID")) {
            IDStatement();
        }
        
    }

    void IDStatement() {

        if (curr.equals("ID")) {
            match("ID");

            String t = lexemeList.get(tokenIndex-1);
            IDStatementChoice(t);
        }
        
    }

    void IDStatementChoice(String input) {
        
        if (curr.equals("AS")) {
            AssignmentStatement(input);

        } else if (curr.equals("(")) {
            FunctionCall();
        }
        
    }
}
