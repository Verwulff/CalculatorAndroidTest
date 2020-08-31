package com.example.calculatortest;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CalcParser {

    //Хранит последнюю операцию (+-*/=())
    private String lastOperation;
    //хранит последний знак. Может равняться как операции, так и числу
    private String lastDigit;
    //стэк операций для хранения предыдущих операций при заходе в скобки
    private Stack<String> digitStack;
    //список калькуляторов
    private List<Calculator> calc;
    //текущее число
    private StringBuilder currentNumber;
    //глубина выражения (количество открытых и при этом не закрытых) скобок
    private int depths;
    //общее количество открытых скобок
    private int parenthesesOpen;
    //общее количество незакрытых скобок
    private int parenthesesClose;

    //конструктор по-умолчанию. Иницализирует переменные
    public CalcParser() {
        calc = new LinkedList<Calculator>();
        calc.add(new Calculator());
        currentNumber = new StringBuilder();
        lastOperation = Checker.EQUAL;
        lastDigit = Checker.EQUAL;
        digitStack = new Stack<String>();
        depths = 0;
        parenthesesOpen = 0;
        parenthesesClose = 0;
    }

    //Парсит выражение из строки
    public void parser(String s) {
        for (int i = 0; i < s.length(); i++)
            inputParse(Character.toString(s.charAt(i)));
    }

    //Определяет какая кнопка нажата
    public int inputParse(String newDigit) {
        if (Checker.isPlusMinus(newDigit))
            return plusMinusParse(newDigit);
        if (Checker.isMultDiv(newDigit))
            return multDivParse(newDigit);
        if (Checker.isOpenPar(newDigit))
            return openParenthesesParse(newDigit);
        if (Checker.isEndEquation(newDigit))
            return closeParenthesesParse(newDigit);
        if (Checker.isNumber(newDigit))
            return numberParse(newDigit);
        return Checker.UNEXPECTED_BEHAVIOR;
    }

    //удаляет элемент. Если удалено число, возвращает тру, если операция - возвращает фолс
    public boolean delNumber() {
        if (Checker.isNumber(lastDigit) && currentNumber.length() > 0) {
            currentNumber.setLength(currentNumber.length() - 1);
            if (currentNumber.length() == 0)
                lastDigit = lastOperation;
            else lastDigit = Character.toString(currentNumber.charAt(currentNumber.length() - 1));
            return true;
        }
        return false;
    }

    //обработчик нажатия кнопки равно. Если выражение получившееся валидное, возвращает тру. Если выражение некорректно, возвращает фалс
    public boolean equalParser() {
        if ((parenthesesOpen != parenthesesClose) || Checker.isEqual(lastDigit))
            return false;
        if (Checker.isNumber(lastDigit) || Checker.isEndEquation(lastDigit)) {
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            return true;
        }
        return true;
    }

    //Возвращает в виде строки результат вычисления выражения
    public String getResult() {
        return calc.get(depths).result().toString();
    }

    //обрабатывает кнопки - и +
    private int plusMinusParse(String newDigit) {
        if (Checker.isStartEquation(lastDigit)) {
            currentNumber.setLength(0);
            currentNumber.append(newDigit);
            setLastDigit(newDigit);
            if (Checker.isEqual(lastDigit))
                return Checker.CLEAR_AND_APPEND;
            return Checker.APPEND;
        } else if (Checker.isNumber(lastDigit)) {
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            setLastOperation(newDigit);
            return Checker.APPEND;
        } else if (Checker.isAriphmeticOperation(lastDigit)) {
            if (Checker.isPlusMinus(currentNumber.toString())) {
                if (!lastDigit.equals(newDigit)) {
                    currentNumber.setLength(0);
                    currentNumber.append(newDigit);
                    setLastDigit(newDigit);
                    return Checker.REPLACE;
                }
                return Checker.IGNORE;
            }
            setLastOperation(newDigit);
            return Checker.REPLACE;
        } else if (Checker.isEndEquation(lastDigit)) {
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            setLastOperation(newDigit);
            return Checker.APPEND;
        }
        return Checker.UNEXPECTED_BEHAVIOR;
    }

    //обрабатывает кнопки * и /
    private int multDivParse(String newDigit) {
        if (Checker.isStartEquation(lastDigit))
            return Checker.CLEAR_AND_IGNORE;
        if (Checker.isNumber(lastDigit)) {
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            setLastOperation(newDigit);
            return Checker.APPEND;
        }
        if (Checker.isPlusMinus(lastDigit)) {
            if (Checker.isStartEquation(lastOperation)) {
                return Checker.IGNORE;
            } else {
                setLastOperation(newDigit);
                return Checker.REPLACE;
            }
        }
        if (Checker.isMultDiv(lastDigit)) {
            setLastOperation(newDigit);
            return Checker.REPLACE;
        }
        if (Checker.isEndEquation(lastDigit)) {
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            setLastOperation(newDigit);
            return Checker.APPEND;
        }
        return Checker.UNEXPECTED_BEHAVIOR;
    }

    //Обрабатывает кнопку (
    private int openParenthesesParse(String newDigit) {
        int result = Checker.UNEXPECTED_BEHAVIOR;
        if (Checker.isNumber(lastDigit) || Checker.isEndEquation(lastDigit)) {
            result = Checker.IGNORE;
        } else {
            if (Checker.isStartEquation(lastDigit)) {
                if (Checker.isEqual(lastDigit))
                    result = Checker.CLEAR_AND_APPEND;
                else result = Checker.APPEND;
                calc.get(depths).addNewNumber("0.0", lastOperation);
                lastOperation = Checker.PLUS;
            } else {
                //calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
                result = Checker.APPEND;
            }
            calc.add(new Calculator());
            depths++;
            parenthesesOpen++;
            digitStack.push(lastOperation);
            setLastOperation(newDigit);
        }
        return result;
    }

    //Обрабатывает кнпоку )
    private int closeParenthesesParse(String newDigit) {
        int result = Checker.UNEXPECTED_BEHAVIOR;
        if (Checker.isEqual(lastDigit))
            result = Checker.CLEAR_AND_IGNORE;
        else if (parenthesesOpen == parenthesesClose) {
            result = Checker.IGNORE;
        } else if (!Checker.isNumber(lastDigit) && !Checker.isEndEquation(lastDigit)) {
            result = Checker.IGNORE;
        } else {
            parenthesesClose++;
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            currentNumber.setLength(0);
            currentNumber.append(calc.get(depths).result().toString());
            calc.remove(depths);
            depths--;
            lastOperation = digitStack.pop();
            setLastDigit(newDigit);
            result = Checker.APPEND;
        }
        return result;
    }

    //обрабатывает нажатие цифровых кнопок и кнопки .
    private int numberParse(String newDigit) {
        int result;
        if (!Checker.isNumber(lastDigit)) {
            currentNumber.setLength(0);
        }
        if (Checker.outOfDigits(currentNumber.toString())) {
            return Checker.IGNORE;
        }
        if (Checker.isDote(newDigit)) {
            if (!Checker.isNumber(lastDigit)) {
                return Checker.IGNORE;
            }
            if (currentNumber.toString().contains(Checker.DOTE)) {
                return Checker.IGNORE;
            }
        }
        if (Checker.isEqual(lastDigit))
            result = Checker.CLEAR_AND_APPEND;
        else result = Checker.APPEND;
        currentNumber.append(newDigit);
        setLastDigit(newDigit);

        return result;
    }

    //присваевает последнему оператору и последнему знаку текущую кнопку
    private void setLastOperation(String newDigit) {
        lastDigit = newDigit;
        lastOperation = newDigit;
    }

    //присваивает последнему знаку текущую кнпоку
    private void setLastDigit(String newDigit) {
        lastDigit = newDigit;
    }
}
