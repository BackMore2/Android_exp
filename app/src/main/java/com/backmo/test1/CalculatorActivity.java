package com.backmo.test1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends Activity {

    private TextView display;
    private String currentInput = "0";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isOperatorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        display = findViewById(R.id.display);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // 数字按钮
        findViewById(R.id.btn_0).setOnClickListener(v -> onNumberClick("0"));
        findViewById(R.id.btn_1).setOnClickListener(v -> onNumberClick("1"));
        findViewById(R.id.btn_2).setOnClickListener(v -> onNumberClick("2"));
        findViewById(R.id.btn_3).setOnClickListener(v -> onNumberClick("3"));
        findViewById(R.id.btn_4).setOnClickListener(v -> onNumberClick("4"));
        findViewById(R.id.btn_5).setOnClickListener(v -> onNumberClick("5"));
        findViewById(R.id.btn_6).setOnClickListener(v -> onNumberClick("6"));
        findViewById(R.id.btn_7).setOnClickListener(v -> onNumberClick("7"));
        findViewById(R.id.btn_8).setOnClickListener(v -> onNumberClick("8"));
        findViewById(R.id.btn_9).setOnClickListener(v -> onNumberClick("9"));

        // 小数点按钮
        findViewById(R.id.btn_dot).setOnClickListener(v -> onDotClick());

        // 运算符按钮
        findViewById(R.id.btn_add).setOnClickListener(v -> onOperatorClick("+"));
        findViewById(R.id.btn_subtract).setOnClickListener(v -> onOperatorClick("-"));
        findViewById(R.id.btn_multiply).setOnClickListener(v -> onOperatorClick("×"));
        findViewById(R.id.btn_divide).setOnClickListener(v -> onOperatorClick("÷"));

        // 等号按钮
        findViewById(R.id.btn_equals).setOnClickListener(v -> onEqualsClick());
    }

    private void onNumberClick(String number) {
        if (isOperatorPressed) {
            currentInput = number;
            isOperatorPressed = false;
        } else {
            if (currentInput.equals("0")) {
                currentInput = number;
            } else {
                currentInput += number;
            }
        }
        updateDisplay();
    }

    private void onDotClick() {
        if (isOperatorPressed) {
            currentInput = "0.";
            isOperatorPressed = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        updateDisplay();
    }

    private void onOperatorClick(String op) {
        if (!isOperatorPressed) {
            if (!operator.isEmpty()) {
                onEqualsClick();
            } else {
                firstNumber = Double.parseDouble(currentInput);
            }
        }
        operator = op;
        isOperatorPressed = true;
    }

    private void onEqualsClick() {
        if (!operator.isEmpty() && !isOperatorPressed) {
            double secondNumber = Double.parseDouble(currentInput);
            double result = calculate(firstNumber, secondNumber, operator);
            
            // 格式化结果
            if (result == (long) result) {
                currentInput = String.valueOf((long) result);
            } else {
                currentInput = String.valueOf(result);
            }
            
            operator = "";
            firstNumber = result;
            isOperatorPressed = true;
            updateDisplay();
        }
    }

    private double calculate(double first, double second, String op) {
        switch (op) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "×":
                return first * second;
            case "÷":
                if (second != 0) {
                    return first / second;
                } else {
                    // 除零错误
                    currentInput = "Error";
                    operator = "";
                    isOperatorPressed = true;
                    return 0;
                }
            default:
                return second;
        }
    }

    private void updateDisplay() {
        display.setText(currentInput);
    }
}
