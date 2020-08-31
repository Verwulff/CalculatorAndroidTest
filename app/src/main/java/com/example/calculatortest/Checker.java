package com.example.calculatortest;

import java.util.HashSet;
import java.util.Set;

public class Checker {

    public static final int APPEND = 0;
    public static final int REPLACE = 1;
    public static final int CLEAR_AND_APPEND = 2;
    public static final int IGNORE = 3;
    public static final int CLEAR_AND_IGNORE = 4;
    public static final int UNEXPECTED_BEHAVIOR = -1;

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
    public static final Set<String> OPERATIONS = new HashSet<String>(){{add(PLUS); add(MINUS); add(MULTIPLY);
        add(DIVISION); add(OPEN_PARENTHESES); add(CLOSE_PARENTHESES); add(EQUAL);}};

    //проверяет число или нет
    public static boolean isNumber(String s) {
        return (!OPERATIONS.contains(s));
    }

    //проверяет на выход за пределы 25 знаков числа
    public static boolean outOfDigits(String s) {
        return !(s.length() < MAX_NUMBER_LENGTH);
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
        return (isEqual(s) || isOpenPar(s));
    }

    //Проверяет на закрытыю скобку
    public static boolean isEndEquation(String s) {
        return (s.equals(CLOSE_PARENTHESES));
    }

    //Проверяет на знаки + и -
    public static boolean isPlusMinus(String s) {
        return (s.equals(PLUS) || s.equals(MINUS));
    }

    //Проверяет на знаки * и /
    public static boolean isMultDiv(String s) {
        return  (s.equals(MULTIPLY) || s.equals(DIVISION));
    }

    //Проверяет является л это арифметической операцией
    public static boolean isAriphmeticOperation(String s) {
        return (isPlusMinus(s) || isMultDiv(s));
    }

    //Проверяет на точку
    public static boolean isDote(String s) {
        return  (s.equals(DOTE));
    }

    //проверяет на =
    public static boolean isEqual(String s) {
        return (s.equals(EQUAL));
    }

    //проверка на *
    public static boolean isMult(String s) {
        return (s.equals(MULTIPLY));
    }

    //Проверка на (
    public static boolean isOpenPar(String s) {
        return (s.equals(OPEN_PARENTHESES));
    }
}
