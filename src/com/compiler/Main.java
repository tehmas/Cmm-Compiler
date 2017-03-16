package com.compiler;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Lex lex = new Lex();
        lex.analyze("input.txt");

        ArrayList<String> tokenList = lex.tokenList;

        Parser parser = new Parser();

        parser.tokenList = tokenList;
        parser.parse();

    }
}
