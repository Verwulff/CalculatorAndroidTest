package com.example.calculatortest;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView resultField;
    TextView calcField;
    Button equalButton;
    String lastDigit = "="; // последняя операция
    List<Calculator> calc;
    StringBuilder currentNumber;
    int depths = 0;
    int parenthesesOpen=0;
    int parenthesesClose=0;
    //переменные для обработки всплывающего меню
    long equalPressStartTime;
    long equalPressEndTime;
    long onScreenTouchTimeFirst;
    long onScreenTouchTimeSecond;
    boolean menuCorrectTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField =(TextView) findViewById(R.id.resultField);
        calcField =(TextView) findViewById(R.id.calcField);
        equalButton = (Button) findViewById(R.id.equalButton);
        calc = new LinkedList<Calculator>();
        calc.add(new Calculator());
        currentNumber = new StringBuilder();

        calcField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        onScreenTouchTimeFirst = System.currentTimeMillis();
                        Long check = (onScreenTouchTimeFirst - equalPressEndTime)/1000;
                        if(check<5)
                        {
                            showMessage("Успел");
                        }
                        resultField.append(check.toString());
                        break;
                }
                return false;
            }
        });

        equalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        equalPressStartTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        equalPressEndTime = System.currentTimeMillis();
                        Long check = (equalPressEndTime-equalPressStartTime)/1000;
                        if (check>=4)
                        {
                            menuCorrectTime = true;
                            showMessage("Прикинь, работает");
                        }
                        resultField.append(check.toString());
                        calc.get(depths).result();
                        break;
                }
                return false;
            }
        });
    }

    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        String digit = button.getText().toString();
        if (!Checker.outOfDigits(currentNumber.toString())) {
            showMessage("Число не может быть больше 25 знаков");
            return;
        }
        if (digit.equals(".")){
            if (!Checker.isNumber(lastDigit)){
                showMessage("Сперва введите число");
                return;
            }
            if (currentNumber.toString().contains(".")){
                showMessage("Не больше одного разделителя в числе");
                return;
            }
        }
        lastDigit = digit;
        currentNumber.append(digit);
        calcField.append(digit);
    }

    // обработка нажатия на кнопку операции
    public void onOperationClick(View view){

        Button button = (Button)view;
        String s;
        s = button.getText().toString();
        switch(s) {
            case "(":
                depths++;
                parenthesesOpen++;
                calc.add(new Calculator());
                calcField.append(s);
                break;
            case ")":
                depths--;
                parenthesesClose++;
                calc.get(depths).numbers.add(calc.get(depths+1).result());
                calc.remove(depths+1);
                calcField.append(s);
                break;
            case "+":
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                currentNumber.setLength(0);
                calcField.append(s);
                break;
            case "-":
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                currentNumber.setLength(0);
                calcField.append(s);
                break;
            case "×":
                s = "*";
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                currentNumber.setLength(0);
                calcField.append(s);
                break;
            case "÷":
                s="/";
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                currentNumber.setLength(0);
                calcField.append(s);
                break;
            default:
                break;

        }
    }

    private void showMessage(String message)
    {
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_LONG);
        toast.show();
    }

}
