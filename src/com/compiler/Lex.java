package com.compiler;

public class Lex {
    private static int index = 0;

    public static TokenLexeme isRelationalOperator(String s) {
        int state = 1;
        while (true) {
            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (ch == '<') {
                        state = 2;
                        index++;
                        break;
                    } else if (ch == '>') {
                        state = 5;
                        index++;
                        break;
                    } else if (ch == '=') {
                        state = 8;
                        index++;
                        break;
                    } else {
                        //halt
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 2:
                    if (ch == '=') {
                        state = 3;
                        index++;
                        break;
                    } else {
                        state = 4;
                        index++;
                        break;
                    }

                case 3:
                    return new TokenLexeme(Boolean.TRUE, "RO", "LE");

                case 4:
                    index--;
                    return new TokenLexeme(Boolean.TRUE, "RO", "LT");

                case 5:
                    if (ch == '=') {
                        state = 6;
                        index++;
                        break;
                    } else {
                        state = 7;
                        index++;
                        break;
                    }

                case 6:
                    return new TokenLexeme(Boolean.TRUE, "RO", "GE");

                case 7:
                    index--;
                    return new TokenLexeme(Boolean.TRUE, "RO", "GT");

                case 8:
                    if (ch == '=') {
                        state = 9;
                        break;
                    } else {
                        //halt
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 9:
                    return new TokenLexeme(Boolean.TRUE, "RO", "ET");
            }


        }
    }
}


