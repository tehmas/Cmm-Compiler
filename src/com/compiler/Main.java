package com.compiler;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*
        VirtualMachine vm = new VirtualMachine();
        vm.readFile("three-address-code.txt", "relative-addresses.txt");
        vm.executeCode();
        */


        Lex lex = new Lex();
        lex.analyze("sample.txt");


        ArrayList<String> tokenList = lex.tokenList;
        ArrayList<String> lexemeList = lex.lexemeList;

        /*
        Parser parser = new Parser();

        parser.tokenList = tokenList;
        parser.parse();
        */

        Translator translator = new Translator();
        if(translator.setInput(tokenList, lexemeList))
            translator.translate();


        VirtualMachine vm = new VirtualMachine();
        vm.readFile("machine_code.txt", "symbol_table.txt");
        vm.executeCode();


    }
}
