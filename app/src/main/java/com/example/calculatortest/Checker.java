package com.example.calculatortest;

public class Checker {

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String MULTIPLY_BUTTON = "×";
    public static final String DIVISION = "/";
    public static final String DIVISION_BUTTON = "÷";
    public static final String OPEN_PARENTHESES = "(";
    public static final String CLOSE_PARENTHESES = ")";
    public static final String EQUAL = "=";
    public static final String DOTE = ".";
    //максимальная разрядность числа 25 (по условию)
    public static final int MAX_NUMBER_LENGTH = 25;

    //проверяет число или нет
    public static boolean isNumber(String s) {
        if (s.equals(PLUS) || s.equals(MINUS) || s.equals(MULTIPLY) || s.equals(DIVISION)
                || s.equals(OPEN_PARENTHESES) || s.equals(CLOSE_PARENTHESES) || s.equals(EQUAL))
            return false;
        else
            return true;
    }

    //проверяет на выход за пределы 25 знаков числа
    public static boolean outOfDigits(String s) {
        if (s.length() < MAX_NUMBER_LENGTH)
            return false;
        else
            return true;
    }

    //Присваивает валидные входные наименования кнопок.
    public static String validButtonText(String s) {
        if (s.equals(MULTIPLY_BUTTON))
            s = MULTIPLY;
        if (s.equals(DIVISION_BUTTON))
            s = DIVISION;
        return s;
    }

    //Проверяет на начало выражения. Начало, если = или (
    public static boolean isStartEquation(String s) {
        if (s.equals(EQUAL) || s.equals(OPEN_PARENTHESES))
            return true;
        else return false;
    }

    //Проверяет на закрытыю скобку
    public static boolean isEndEquation(String s) {
        if (s.equals(CLOSE_PARENTHESES))
            return true;
        else return false;
    }

    //Проверяет на знаки + и -
    public static boolean isPlusMinus(String s) {
        if (s.equals(PLUS) || s.equals(MINUS))
            return true;
        else return false;
    }

    //Проверяет на знаки * и /
    public static boolean isMultDiv(String s) {
        if (s.equals(MULTIPLY) || s.equals(DIVISION))
            return true;
        else return false;
    }

    //Проверяет является л это арифметической операцией
    public static boolean isAriphmeticOperation(String s) {
        if (isPlusMinus(s) || isMultDiv(s))
            return true;
        else return false;
    }

    //Проверяет на точку
    public static boolean isDote(String s) {
        if (s.equals(DOTE))
            return true;
        else return false;
    }
}
