package com.example.calculatortest;

import java.util.LinkedList;
import java.util.List;

public class Calculator {

    //Хранит числа
    List<Double> numbers;
    //Хранит знаки операций (+ и -)
    List<String> operations;

    //конструктор, который инициирует списки
    Calculator() {
        numbers = new LinkedList<Double>();
        operations = new LinkedList<String>();
    }

    //возвращает результат вычисления выражения
    public Double result() {
        double sum = numbers.get(0);
        int i = 1;
        for (String op : operations) {
            if (op.equals(Checker.MINUS))
                sum = sum - numbers.get(i);
            else sum = sum + numbers.get(i);
            i++;
        }
        return sum;
    }

    //Добавляет новые элементы в калькулятор
    public void addNewNumber(String currentNumber, String lastOperation) {
        //Если только начало выражения, то сохраняет только число
        if (Checker.isStartEquation(lastOperation)) {
            numbers.add(Double.parseDouble(currentNumber));
        }
        //если последняя операция + или -, то добавляет операцию и число в списки
        else if (Checker.isPlusMinus(lastOperation)) {
            numbers.add(Double.parseDouble(currentNumber));
            operations.add(lastOperation);
        }
        //если операция умножить или поделить, то записывает вместо последнего числа результат
        //произведения/деления сохраненного ранее числа на новое входное число
        else if (Checker.isMultDiv(lastOperation)) {
            double sum = numbers.get(numbers.size() - 1);
            if (lastOperation.equals(Checker.MULTIPLY))
                sum = sum * Double.parseDouble(currentNumber);
            else sum = sum / Double.parseDouble(currentNumber);
            numbers.set(numbers.size() - 1, sum);
        }
    }

}
