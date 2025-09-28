package com.backmo.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 添加导航到表格布局页面的按钮
        addNavigationButton();
    }

    private void addNavigationButton() {
        // 创建一个按钮来导航到表格布局页面
        Button tableLayoutButton = new Button(this);
        tableLayoutButton.setText("查看表格布局");
        tableLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableActivity.class);
                startActivity(intent);
            }
        });

        // 创建一个按钮来导航到计算器页面
        Button calculatorButton = new Button(this);
        calculatorButton.setText("简易计算器");
        calculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        });

        // 创建一个按钮来导航到约束布局2页面
        Button constraintLayout2Button = new Button(this);
        constraintLayout2Button.setText("约束布局2");
        constraintLayout2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConstraintLayout2Activity.class);
                startActivity(intent);
            }
        });

        // 将按钮添加到主布局中
        LinearLayout mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            mainLayout.addView(tableLayoutButton);
            mainLayout.addView(calculatorButton);
            mainLayout.addView(constraintLayout2Button);
        }
    }
}