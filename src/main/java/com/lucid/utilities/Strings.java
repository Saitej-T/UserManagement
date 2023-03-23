package com.lucid.utilities;

public class Strings {

    public static boolean isNullOrEmpty(String value){
        if(value == null || value.trim().isEmpty())
            return true;
        else
            return false;
    }
}
