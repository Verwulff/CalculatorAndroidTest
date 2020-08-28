package com.example.calculatortest;

public class Checker {

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVISION = "/";
    public static final String OPEN_PARENTHESES = "(";
    public static final String CLOSE_PARENTHESES = ")";
    public static final String EQUAL = "=";

    public static boolean isNumber(String s)
    {
        if (s.equals(PLUS) || s.equals(MINUS) || s.equals(MULTIPLY) || s.equals(DIVISION)
            || s.equals(OPEN_PARENTHESES) || s.equals(CLOSE_PARENTHESES) || s.equals(EQUAL))
            return false;
        else
            return true;
    }
}
