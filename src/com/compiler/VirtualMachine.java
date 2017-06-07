package com.compiler;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VirtualMachine {

    List<String> codeLines;
    Hashtable<String, String> numericCodes;
    Hashtable<String, Integer> relativeAddresses;
    int storage[];

    public VirtualMachine(){
        numericCodes = new Hashtable<String, String>();
        numericCodes.put("0", "in");
        numericCodes.put("1", "out");
        numericCodes.put("2", "goto");
        numericCodes.put("3", "IFGE");
        numericCodes.put("42", "*");
        numericCodes.put("43", "+");
        numericCodes.put("45", "-");
        numericCodes.put("47", "/");
        numericCodes.put("61", "=");
    }

    public void readFile(String codeFile, String addressFile){
        codeLines = new ArrayList<String>();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(codeFile))){
            codeLines = br.lines().collect(Collectors.toList());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        List<String> addressLines = new ArrayList<String>();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(addressFile))){
            addressLines = br.lines().collect(Collectors.toList());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        relativeAddresses = new Hashtable<String, Integer>();
        int i = 0;
        while (i < addressLines.size()){
            String[] words = addressLines.get(i).split(" ");

            relativeAddresses.put(words[0], Integer.parseInt(words[1]));
            i++;
        }

        storage = new int[relativeAddresses.size()];
    }

    public void executeCode()
    {
        for(int i = 0; i < codeLines.size(); i++)
        {
            String [] words = codeLines.get(i).split(" ");

            if(numericCodes.containsKey(words[0]))
            {
                if(numericCodes.get(words[0]).equals("in"))
                {
                    Scanner scanner = new Scanner(System.in);
                    int input = scanner.nextInt();
                    storage[Integer.parseInt(words[1])]= input;
                }

                else if(numericCodes.get(words[0]).equals("out"))
                {
                    System.out.println(storage[Integer.parseInt(words[1])]);
                }

                else if(numericCodes.get(words[0]).equals("+"))
                {
                    storage[Integer.parseInt(words[3])] = storage[Integer.parseInt(words[1])] + storage[Integer.parseInt(words[2])];
                }

                else if(numericCodes.get(words[0]).equals("-"))
                {
                    storage[Integer.parseInt(words[3])] = storage[Integer.parseInt(words[1])] - storage[Integer.parseInt(words[2])];
                }

                else if(numericCodes.get(words[0]).equals("/"))
                {
                    storage[Integer.parseInt(words[3])] = storage[Integer.parseInt(words[1])] / storage[Integer.parseInt(words[2])];
                }

                else if(numericCodes.get(words[0]).equals("*"))
                {
                    storage[Integer.parseInt(words[3])] = storage[Integer.parseInt(words[1])] * storage[Integer.parseInt(words[2])];
                }

                else if(numericCodes.get(words[0]).equals("="))
                {
                    storage[Integer.parseInt(words[2])] = storage[Integer.parseInt(words[1])];
                }


                else if(numericCodes.get(words[0]).equals("IFGE"))
                {
                    if(storage[Integer.parseInt(words[1])] > storage[Integer.parseInt(words[2])])
                    {
                        i = Integer.parseInt(words[3]) - 1 - 1;
                    }
                }

                else if(numericCodes.get(words[0]).equals("goto"))
                {
                    i = Integer.parseInt(words[1]) - 1 - 1;
                }
            }

            else{
                System.out.print("unrecognized keyword");
            }

        }
    }
}
