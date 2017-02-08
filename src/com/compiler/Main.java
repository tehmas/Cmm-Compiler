package com.compiler;

public class Main {

    public static void main(String[] args) {

        Lex lex = new Lex();
        lex.analyze("code.txt");
    }
}
