package com.example.calculatortest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Calculator {

    List<Double> numbers;
    List<String> operations;

    Calculator()
    {
        numbers = new LinkedList<Double>();
        operations = new LinkedList<String>();
    }

    public Double result()
    {
        return 0.0;
    }

    public String start()
    {
        return "";
    }

}
