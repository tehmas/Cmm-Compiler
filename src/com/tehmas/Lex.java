package com.tehmas;

import java.io.*;
import java.util.Hashtable;

public class Lex {

    Hashtable<String, Tuple<String, String>> keywords = new Hashtable<String, Tuple<String, String>>();

    public int readKeywordsList(String file_name) {

        try {
            FileInputStream fstream = new FileInputStream(file_name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            String line;

            try {
                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String symbol = parts[0];
                    String token = parts[1];
                    String lexeme = parts[2];
                    keywords.put(symbol, new Tuple<String, String>(token, lexeme));
                }

                bufferedReader.close();

            } catch (IOException e) {
                return e.hashCode();
            }

        } catch (FileNotFoundException e) {
            return e.hashCode();
        }

        return 0;
    }
}


