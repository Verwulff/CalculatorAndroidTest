package com.example.calculatortest;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView resultField;
    EditText calcField;
    String lastDigit = "="; // последняя операция
    List<Calculator> calc;
    StringBuilder currentNumber;
    int depths = 0;
    int parenthesesOpen=0;
    int parenthesesClose=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField =(TextView) findViewById(R.id.resultField);
        calcField =(EditText) findViewById(R.id.calcField);
        calc = new LinkedList<Calculator>();
        calc.add(new Calculator());
        currentNumber = new StringBuilder();
    }

    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        String digit = button.getText().toString();
        if ((digit == ".") && (!Checker.isNumber(lastDigit)))
            return;
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
                calcField.append(s);
                break;
            case "-":
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                calcField.append(s);
                break;
            case "×":
                s = "*";
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                calcField.append(s);
                break;
            case "÷":
                s="/";
                calc.get(depths).numbers.add(Double.parseDouble(currentNumber.toString()));
                calc.get(depths).operations.add(s);
                calcField.append(s);
                break;
            default:
                break;

        }
    }


}
