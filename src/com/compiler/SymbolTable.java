package com.compiler;


import java.util.Hashtable;

public class SymbolTable{

    Hashtable<String, String> addressTable;
    Hashtable<String, String> typeTable;

    int count;

    public SymbolTable(){
        addressTable = new Hashtable<String, String>();
        typeTable = new Hashtable<String, String>();
        count = 0;
    }

    public boolean addSymbol(String symbol, String type){
        if(addressTable.containsKey(symbol))
            return false;

        addressTable.put(symbol, String.valueOf(count));
        typeTable.put(symbol, type);
        count++;
        return true;
    }

    public String getAddress(String symbol){
        if(addressTable.containsKey(symbol))
            return addressTable.get(symbol);

        return null;
    }

    public Hashtable<String, String> getAddressTable(){
        return addressTable;
    }

    public Hashtable<String, String> getTypeTable(){
        return typeTable;
    }
}