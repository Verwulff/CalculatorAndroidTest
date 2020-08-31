package com.example.calculatortest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView resultField;
    TextView calcField;
    Button equalButton;

    CalcParser calcParser;

    //переменные для обработки всплывающего меню
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private static final long CORNER_TAP_TIME = 5000;
    private static final long EQUAL_PRESS_TIME = 4000;
    private static final float X_COORD_MAX = 70.0f;
    private static final float Y_COORD_MAX = 70.0f;
    long equalPressStartTime;
    long equalPressEndTime;
    long onScreenTouchTimeFirst;
    long onScreenTouchTimeSecond;
    boolean isItFirstClick = true;

    //Проверяет удалена ли с помощью клавиши операция. Если только цифра, то установлена на false
    boolean isDelOperation = false;
    //Переменная для запрета кнопки дел после нажатия равно до нажатия кнопки
    boolean isDelEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = (TextView) findViewById(R.id.resultField);
        calcField = (TextView) findViewById(R.id.calcField);
        equalButton = (Button) findViewById(R.id.equalButton);

        calcParser = new CalcParser();


        //Обработчик кликов по углу экрана
        calcField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        hiddenMenuOp(x, y);
                        break;
                }
                return false;
            }
        });

        //Обработчик нажайти клавиши =
        equalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        equalPressStartTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        equalPressEndTime = System.currentTimeMillis();
                        if (calcParser.equalParser()) {
                            resultField.append(calcParser.getResult());
                        } else showMessage("Invalid function");
                        calcParser = new CalcParser();
                        isDelEnable = false;
                        break;
                }
                return false;
            }
        });
    }

    //Обработчик нажатий на основные клавиши калькулятора
    public void onCalcClick(View view) {
        Button button = (Button) view;
        String digit = button.getText().toString();
        isDelEnable = true;
        if (isDelOperation) {
            calcParser = new CalcParser();
            calcParser.parser(calcField.getText().toString());
            isDelOperation = false;
        }
        digit = Checker.validButtonText(digit);
        int inputCheck = calcParser.inputParse(digit);
        switch (inputCheck) {
            //если все хорошо
            case 0:
                calcField.append(digit);
                break;
            //если происходит замена последней операции на новую
            case 1:
                calcField.setText(calcField.getText().toString().substring(0, calcField.getText().length() - 1));
                calcField.append(digit);
                break;
            //если надо обнулить поля с началом нового выражения
            case 2:
                calcField.setText("");
                calcField.append(digit);
                resultField.setText("");
                break;
            //Если действие не является математически верным
            case 3:
                showMessage("Invalid operation");
                break;
            //если действией не является математически верным, но при этом это начало нового вычисления
            case 4:
                showMessage("Invalid operation");
                resultField.setText("");
                break;
            //непонятные непредвиденные обстоятельства
            default:
                showMessage("Что-то пошло не так");
                break;
        }
    }

    //Обработчик нажатия кнопки C(Clear)
    public void onClearClick(View view) {
        calcField.setText("");
        resultField.setText("");
        calcParser = new CalcParser();
        isDelEnable = true;
    }

    //Обработчик нажайтий кнопки Del.(Backspace)
    public void onDelClick(View view) {
        if (isDelEnable) {
            boolean check = calcParser.delNumber();
            if (check) {
                calcField.setText(calcField.getText().toString().substring(0, calcField.getText().length() - 1));
            } else {
                if (calcField.getText().toString().length() > 0)
                    calcField.setText(calcField.getText().toString().substring(0, calcField.getText().length() - 1));
                isDelOperation = true;
            }
        }
    }

    //Функция, отвечающая за открытие секретного меню при выполненных условиях
    private void hiddenMenuOp(float x, float y) {
        if (x <= X_COORD_MAX && y <= Y_COORD_MAX) {
            if (isItFirstClick) {
                onScreenTouchTimeFirst = System.currentTimeMillis();
                isItFirstClick = false;
            } else {
                onScreenTouchTimeSecond = System.currentTimeMillis();
                Long doubleClickCheck = onScreenTouchTimeSecond - onScreenTouchTimeFirst;
                if (doubleClickCheck <= DOUBLE_CLICK_TIME_DELTA) {
                    Long tapTimeCheck = onScreenTouchTimeFirst - equalPressEndTime;
                    if (tapTimeCheck <= CORNER_TAP_TIME) {
                        Long equalTimeCheck = equalPressEndTime - equalPressStartTime;
                        if (equalTimeCheck >= EQUAL_PRESS_TIME) {
                            Intent intent = new Intent(MainActivity.this, HiddenMenu.class);
                            startActivity(intent);
                        }
                    }
                }
                isItFirstClick = true;
            }
        }
    }

    //выводит высплывающее сообщение
    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
