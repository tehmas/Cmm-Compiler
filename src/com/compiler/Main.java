package com.compiler;

public class Main {

    public static void main(String[] args) {

        Lex lex = new Lex();

        TokenLexeme p = Lex.isKeyword("char");

        System.out.print(p.token);
        System.out.print(p.lexeme);
    }
}
