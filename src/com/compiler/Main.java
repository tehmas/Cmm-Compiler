package com.compiler;

public class Main {

    static String KEYWORDS_FILE = "keywords.csv";

    public static void main(String[] args) {

        Lex lex = new Lex();
        TokenLexeme p = Lex.isRelationalOperator("==");
        System.out.print(p.lexeme);
    }
}
