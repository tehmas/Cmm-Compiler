package com.compiler;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.util.Hashtable;

public class Lex {
    private static Hashtable<String, String> keywords = new Hashtable<String, String>();
    private static int index = 0;

    public Lex() {
        keywords.put("int", "INT");
        keywords.put("void", "VOID");
        keywords.put("char", "CHAR");
        keywords.put("while", "WHILE");
        keywords.put("if", "IF");
        keywords.put("else", "ELSE");
        keywords.put("return", "RET");
        keywords.put("cout", "COUT");
        keywords.put("cin", "CIN");
    }

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
                        return new TokenLexeme(false, null, null);
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
                    return new TokenLexeme(true, "RO", "LE");

                case 4:
                    //index--;
                    return new TokenLexeme(true, "RO", "LT");

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
                    return new TokenLexeme(true, "RO", "GE");

                case 7:
                    //index--;
                    return new TokenLexeme(true, "RO", "GT");

                case 8:
                    if (ch == '=') {
                        state = 9;
                        break;
                    } else {
                        //halt
                        return new TokenLexeme(false, null, null);
                    }

                case 9:
                    return new TokenLexeme(true, "RO", "ET");

                case 10:
                    return new TokenLexeme(true, "RO", "NE");
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
                    } else if (ch == '=') {
                        state = 6;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 2:
                    return new TokenLexeme(true, "+", null);

                case 3:
                    return new TokenLexeme(true, "-", null);

                case 4:
                    if (ch == '/') {
                        state = 9;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(true, "*", null);

                case 5:
                    if (ch == '/') {
                        state = 7;
                        index++;
                        break;
                    } else if (ch == '*') {
                        state = 8;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(true, "/", null);

                case 6:
                    return new TokenLexeme(true, "=", null);

                case 7:
                    return new TokenLexeme(true, "SCOM", null);

                case 8:
                    return new TokenLexeme(true, "MCOM", null);

                case 9:
                    return new TokenLexeme(true, "CCOM", null);

            }
        }
    }

    public static TokenLexeme isKeyword(String s) {
        int state = 1;
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {

            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (((int) ch >= 65 && (int) ch <= 90) || ((int) ch >= 97 && (int) ch <= 122)) {
                        state = 2;
                        stringBuilder.append(ch);
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 2:
                    if (((int) ch >= 65 && (int) ch <= 90) || ((int) ch >= 97 && (int) ch <= 122)) {
                        state = 2;
                        stringBuilder.append(ch);
                        index++;
                        break;
                    } else {
                        //index--;
                        String key = stringBuilder.toString();
                        if (keywords.containsKey(key))
                            return new TokenLexeme(true, keywords.get(key), null);
                        else
                            return new TokenLexeme(false, null, null);
                    }
            }
        }
    }

    public static TokenLexeme isNumericConstant(String s) {
        int state = 1;
        StringBuilder numberBuilder = new StringBuilder();
        while (true) {
            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (ch == '0' || ch == '1' || ch == '2' || ch == '3'
                            || ch == '4' || ch == '5' || ch == '6'
                            || ch == '7' || ch == '8' || ch == '9') {
                        state = 2;
                        numberBuilder.append(ch);
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 2:
                    if (ch == '0' || ch == '1' || ch == '2' || ch == '3'
                            || ch == '4' || ch == '5' || ch == '6'
                            || ch == '7' || ch == '8' || ch == '9') {
                        state = 2;
                        numberBuilder.append(ch);
                        index++;
                        break;
                    } else {
                        //index--;
                        return new TokenLexeme(true, "NC", numberBuilder.toString());
                    }
            }
        }
    }

    public static TokenLexeme isIdentifier(String s) {
        int state = 1;
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (((int) ch >= 65 && (int) ch <= 90) || ((int) ch >= 97 && (int) ch <= 122)) {
                        state = 2;
                        stringBuilder.append(ch);
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 2:
                    if (((int) ch >= 65 && (int) ch <= 90) || ((int) ch >= 97 && (int) ch <= 122) || (((int) ch >= 48 && (int) ch <= 57))) {
                        state = 2;
                        stringBuilder.append(ch);
                        index++;
                        break;
                    } else {
                        //index--;
                        return new TokenLexeme(true, "ID", stringBuilder.toString());
                    }
            }
        }
    }

    public static TokenLexeme isLiteralConstant(String s) {
        int state = 1;
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';


            switch (state) {
                case 1:
                    if (ch == '\'') {
                        state = 2;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 2:
                    if (((int) ch >= 65 && (int) ch <= 90) || ((int) ch >= 97 && (int) ch <= 122)) {
                        state = 3;
                        stringBuilder.append(ch);
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 3:
                    if (ch == '\'') {
                        state = 4;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 4:
                    return new TokenLexeme(true, "LC", stringBuilder.toString());
            }
        }
    }

    public static TokenLexeme isPunctuation(String s) {
        int state = 1;
        while (true) {
            Character ch;
            if (index < s.length())
                ch = s.charAt(index);
            else
                ch = ' ';

            switch (state) {
                case 1:
                    if (ch == '{') {
                        state = 2;
                        index++;
                        break;
                    } else if (ch == '}') {
                        state = 3;
                        index++;
                        break;
                    } else if (ch == '(') {
                        state = 4;
                        index++;
                        break;
                    } else if (ch == ')') {
                        state = 5;
                        index++;
                        break;
                    } else if (ch == '[') {
                        state = 6;
                        index++;
                        break;
                    } else if (ch == ']') {
                        state = 7;
                        index++;
                        break;
                    } else if (ch == ';') {
                        state = 8;
                        index++;
                        break;
                    } else if (ch == ',') {
                        state = 9;
                        index++;
                        break;
                    } else
                        return new TokenLexeme(false, null, null);

                case 2:
                    return new TokenLexeme(true, "{", null);

                case 3:
                    return new TokenLexeme(true, "}", null);

                case 4:
                    return new TokenLexeme(true, "(", null);

                case 5:
                    return new TokenLexeme(true, ")", null);

                case 6:
                    return new TokenLexeme(true, "[", null);

                case 7:
                    return new TokenLexeme(true, "]", null);

                case 8:
                    return new TokenLexeme(true, ";", null);

                case 9:
                    return new TokenLexeme(true, ",", null);

            }

        }
    }

    public int analyze(String fileName) {
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            FileOutputStream wordsFile = new FileOutputStream("words.txt");
            Writer wordsWriter = new BufferedWriter(new OutputStreamWriter(wordsFile));
            FileOutputStream tableFile = new FileOutputStream("table.txt");
            Writer tableWriter = new BufferedWriter(new OutputStreamWriter(tableFile));

            StringBuilder stringBuilder = new StringBuilder();
            try {
                String line;
                int lineNum = 0;

                boolean mComment = false;
                while ((line = bufferedReader.readLine()) != null) {

                    int pointer = 0;

                    boolean sComment = false;
                    while (pointer < line.length()) {
                        index = pointer;
                        boolean pComment = false;
                        if (line.charAt(index) != ' ' && line.charAt(index) != '\t') {
                            TokenLexeme p = isKeyword(line);
                            if (!p.valid) {
                                index = pointer;
                                p = isIdentifier(line);
                            }

                            if (!p.valid) {
                                index = pointer;
                                p = isLiteralConstant(line);
                            }

                            if (!p.valid) {
                                index = pointer;
                                p = isNumericConstant(line);
                            }

                            if (!p.valid) {
                                index = pointer;
                                p = isArithmeticOperator(line);

                                if (p.valid && p.token == "SCOM") {
                                    line = "";
                                    p.valid = false;
                                    sComment = true;
                                } else if (p.valid && p.token == "MCOM") {
                                    p.valid = false;
                                    mComment = true;
                                } else if (p.valid && p.token == "CCOM") {
                                    p.valid = false;
                                    mComment = false;
                                    pComment = true;

                                }
                            }

                            if (!p.valid) {
                                index = pointer;
                                p = isRelationalOperator(line);
                            }

                            if (!p.valid) {
                                index = pointer;
                                p = isPunctuation(line);
                            }

                            if (!p.valid && !sComment && !mComment) {
                                System.out.println("Error: Unrecognized token at line " + lineNum);
                                pointer = index + 1;
                            } else if (!sComment && !mComment && !pComment) {
                                stringBuilder.setLength(0);
                                stringBuilder.append("(");
                                stringBuilder.append(p.token);
                                stringBuilder.append(",");

                                if (p.lexeme == null)
                                    stringBuilder.append("null");

                                else
                                    stringBuilder.append(p.lexeme);
                                stringBuilder.append(")\n");
                                wordsWriter.write(stringBuilder.toString());

                                if (p.token == "ID") {
                                    tableWriter.write(p.lexeme + "\n");
                                }

                                pointer = index;
                            }
                        } else
                            pointer += 1;
                    }

                    lineNum += 1;
                }

                bufferedReader.close();
                wordsWriter.close();
                tableWriter.close();
            } catch (IOException e) {
                return e.hashCode();
            }

        } catch (FileNotFoundException e) {
            return e.hashCode();
        }

        return 0;
    }
}


