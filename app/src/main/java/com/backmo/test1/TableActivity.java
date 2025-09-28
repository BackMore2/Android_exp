package com.backmo.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        // 为每个菜单项添加点击事件
        setupMenuClickListeners();
    }

    private void setupMenuClickListeners() {
        // 获取TableLayout中的所有TableRow
        TableLayout tableLayout = findViewById(R.id.table_layout);
        
        // 遍历TableLayout的子视图，为每个TableRow设置点击事件
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                final TableRow row = (TableRow) child;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleMenuClick(row);
                    }
                });
            }
        }
    }

    private void handleMenuClick(TableRow row) {
        // 获取第一列的文字来判断点击的是哪个菜单项
        if (row.getChildCount() > 0) {
            View firstChild = row.getChildAt(0);
            if (firstChild instanceof TextView) {
                TextView textView = (TextView) firstChild;
                String menuText = textView.getText().toString();
                
                switch (menuText) {
                    case "Open...":
                        showToast("打开文件");
                        break;
                    case "Save...":
                        showToast("保存文件");
                        break;
                    case "Save As...":
                        showToast("另存为");
                        break;
                    case "X Import...":
                        showToast("导入");
                        break;
                    case "X Export...":
                        showToast("导出");
                        break;
                    case "Quit":
                        // 返回到主页面
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        showToast("点击了: " + menuText);
                        break;
                }
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
