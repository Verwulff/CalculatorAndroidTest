package com.example.calculatortest;

public class Checker {

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVISION = "/";
    public static final String OPEN_PARENTHESES = "(";
    public static final String CLOSE_PARENTHESES = ")";
    public static final String EQUAL = "=";
    public static final String DOTE = ".";
    //должно быть 25, потом поправлю
    public static final int MAX_NUMBER_LENGTH = 5;

    public static boolean isNumber(String s)
    {
        if (s.equals(PLUS) || s.equals(MINUS) || s.equals(MULTIPLY) || s.equals(DIVISION) || s.equals(DOTE)
            || s.equals(OPEN_PARENTHESES) || s.equals(CLOSE_PARENTHESES) || s.equals(EQUAL))
            return false;
        else
            return true;
    }

    public static boolean outOfDigits(String s) {
        if (s.length()==MAX_NUMBER_LENGTH)
            return false;
        else
            return true;
    }

}
