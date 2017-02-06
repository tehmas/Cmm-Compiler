package com.compiler;

import com.sun.org.apache.xpath.internal.operations.Bool;

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
                    } else if (ch == '>') {
                        state = 10;
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

                case 10:
                    return new TokenLexeme(Boolean.TRUE, "RO", "NE");
            }


        }
    }

    public static TokenLexeme isArithmeticOperator(String s) {
        int state = 1;
        while (true) {
            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (ch == '+') {
                        state = 2;
                        index++;
                        break;
                    } else if (ch == '-') {
                        state = 3;
                        index++;
                        break;
                    } else if (ch == '*') {
                        state = 4;
                        index++;
                        break;
                    } else if (ch == '/') {
                        state = 5;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(Boolean.FALSE, null, null);

                case 2:
                    return new TokenLexeme(Boolean.TRUE, "AO", "ADD");

                case 3:
                    return new TokenLexeme(Boolean.TRUE, "AO", "SUB");

                case 4:
                    return new TokenLexeme(Boolean.TRUE, "AO", "MUL");

                case 5:
                    return new TokenLexeme(Boolean.TRUE, "AO", "DIV");

            }
        }
    }

    public static TokenLexeme isKeyword(String s) {
        int state = 1;

        while (true) {

            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (ch == 'i') {
                        state = 2;
                        index++;
                        break;
                    } else if (ch == 'v') {
                        state = 5;
                        index++;
                        break;
                    } else if (ch == 'c') {
                        state = 9;
                        index++;
                        break;
                    } else if (ch == 'e') {
                        state = 19;
                        index++;
                        break;
                    } else if (ch == 'w') {
                        state = 23;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 2:
                    if (ch == 'n') {
                        state = 3;
                        index++;
                        break;
                    } else if (ch == 'f') {
                        state = 18;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 3:
                    if (ch == 't') {
                        state = 4;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }


                case 4:
                    return new TokenLexeme(Boolean.TRUE, "INT", null);

                case 5:
                    if (ch == 'o') {
                        state = 6;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 6:
                    if (ch == 'i') {
                        state = 7;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 7:
                    if (ch == 'd') {
                        state = 8;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 8:
                    return new TokenLexeme(Boolean.TRUE, "VOID", null);

                case 9:
                    if (ch == 'h') {
                        state = 10;
                        index++;
                        break;
                    } else if (ch == 'i') {
                        state = 13;
                        index++;
                        break;
                    } else if (ch == 'o') {
                        state = 15;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 10:
                    if (ch == 'a') {
                        state = 11;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 11:
                    if (ch == 'r') {
                        state = 12;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 12:
                    return new TokenLexeme(Boolean.TRUE, "CHAR", null);

                case 13:
                    if (ch == 'n') {
                        state = 14;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 14:
                    return new TokenLexeme(Boolean.TRUE, "CIN", null);

                case 15:
                    if (ch == 'u') {
                        state = 16;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 16:
                    if (ch == 't') {
                        state = 17;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 17:
                    return new TokenLexeme(Boolean.TRUE, "COUT", null);

                case 18:
                    return new TokenLexeme(Boolean.TRUE, "IF", null);

                case 19:
                    if (ch == 'l') {
                        state = 20;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 20:
                    if (ch == 's') {
                        state = 21;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 21:
                    if (ch == 'e') {
                        state = 22;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 22:
                    return new TokenLexeme(Boolean.TRUE, "ELSE", null);

                case 23:
                    if (ch == 'h') {
                        state = 24;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 24:
                    if (ch == 'i') {
                        state = 25;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 25:
                    if (ch == 'l') {
                        state = 26;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 26:
                    if (ch == 'e') {
                        state = 27;
                        index++;
                        break;
                    } else {
                        return new TokenLexeme(Boolean.FALSE, null, null);
                    }

                case 27:
                    return new TokenLexeme(Boolean.TRUE, "WHILE", null);
            }
        }
    }
}


