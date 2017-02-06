package com.tehmas;

public class Main {

    static String KEYWORDS_FILE = "keywords.csv";

    public static void main(String[] args) {
        Lex lex = new Lex();

        int e = lex.readKeywordsList(KEYWORDS_FILE);

        if (e != 0) {
            System.out.println(e);
        }

    }
}
