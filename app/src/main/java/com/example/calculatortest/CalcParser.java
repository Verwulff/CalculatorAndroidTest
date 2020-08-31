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
        int result = -1;
        if (Checker.isPlusMinus(newDigit)) {
            result = plusMinusParse(newDigit);
        } else if (Checker.isMultDiv(newDigit)) {
            result = multDivParse(newDigit);
        } else if (newDigit.equals(Checker.OPEN_PARENTHESES)) {
            result = openParenthesesParse(newDigit);
        } else if (Checker.isEndEquation(newDigit)) {
            result = closeParenthesesParse(newDigit);
        } else if (Checker.isNumber(newDigit)) {
            result = numberParse(newDigit);
        }
        return result;
    }

    //удаляет элемент. Если удалено число, возвращает тру, если операция - возвращает фолс
    public boolean delNumber() {
        if (Checker.isNumber(lastDigit) && currentNumber.length() > 0) {
            currentNumber.setLength(currentNumber.length() - 1);
            if (currentNumber.length() == 0)
                lastDigit = lastOperation;
            else lastDigit = Character.toString(currentNumber.charAt(currentNumber.length() - 1));
            return true;
        } else {
            return false;
        }
    }

    //обработчик нажатия кнопки равно. Если выражение получившееся валидное, возвращает тру. Если выражение некорректно, возвращает фалс
    public boolean equalParser() {
        if (parenthesesOpen != parenthesesClose)
            return false;
        if (lastDigit.equals(Checker.EQUAL))
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
        int result = -1;
        if (Checker.isStartEquation(lastDigit)) {
            result = 2;
            currentNumber.setLength(0);
            currentNumber.append(newDigit);
            setLastDigit(newDigit);
        } else if (Checker.isNumber(lastDigit)) {
            result = 0;
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            setLastOperation(newDigit);
        } else if (Checker.isAriphmeticOperation(lastDigit)) {
            if (Checker.isPlusMinus(currentNumber.toString())) {
                if (!lastDigit.equals(newDigit)) {
                    currentNumber.setLength(0);
                    currentNumber.append(newDigit);
                    setLastDigit(newDigit);
                    return 1;
                } else return 3;
            }
            result = 1;
            setLastOperation(newDigit);
        } else if (Checker.isEndEquation(lastDigit)) {
            result = 0;
            calc.get(depths).addNewNumber(currentNumber.toString(), digitStack.peek());
            setLastOperation(newDigit);
        }
        return result;
    }

    //обрабатывает кнопки * и /
    private int multDivParse(String newDigit) {
        int result = -1;
        if (Checker.isStartEquation(lastDigit)) {
            result = 4;
        } else if (Checker.isNumber(lastDigit)) {
            result = 0;
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            setLastOperation(newDigit);
        } else if (Checker.isPlusMinus(lastDigit)) {
            if (Checker.isStartEquation(lastOperation)) {
                result = 3;
            } else {
                result = 1;
                setLastOperation(newDigit);
            }
        } else if (Checker.isMultDiv(lastDigit)) {
            result = 1;
            setLastOperation(newDigit);
        } else if (Checker.isEndEquation(lastDigit)) {
            result = 0;
            calc.get(depths).addNewNumber(currentNumber.toString(), digitStack.peek());
            setLastOperation(newDigit);
        }
        return result;
    }

    //Обрабатывает кнопку (
    private int openParenthesesParse(String newDigit) {
        int result = -1;
        if (Checker.isNumber(lastDigit) || Checker.isEndEquation(lastDigit)) {
            result = 3;
        } else {
            if (Checker.isStartEquation(lastDigit)) {
                if (lastDigit.equals(Checker.EQUAL))
                    result = 2;
                else result = 0;
                calc.get(depths).addNewNumber("0.0", lastOperation);
                lastOperation = Checker.PLUS;
            } else {
                //calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
                result = 0;
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
        int result = -1;
        if (lastDigit.equals(Checker.EQUAL))
            result = 4;
        else if (parenthesesOpen == parenthesesClose) {
            result = 3;
        } else if (!Checker.isNumber(lastDigit)) {
            result = 3;
        } else {
            parenthesesClose++;
            calc.get(depths).addNewNumber(currentNumber.toString(), lastOperation);
            currentNumber.setLength(0);
            currentNumber.append(calc.get(depths).result().toString());
            calc.remove(depths);
            depths--;
            lastOperation = digitStack.pop();
            setLastDigit(newDigit);
            result = 0;
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
            result = 3;
            return result;
        }
        if (Checker.isDote(newDigit)) {
            if (!Checker.isNumber(lastDigit)) {
                result = 3;
                return result;
            }
            if (currentNumber.toString().contains(Checker.DOTE)) {
                result = 3;
                return result;
            }
        }
        if (lastDigit.equals(Checker.EQUAL))
            result = 2;
        else result = 0;
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
