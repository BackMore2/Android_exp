package com.backmo.test1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ConstraintLayout2Activity extends Activity {

    private Button btnSpaceStations, btnFlights, btnRovers;
    private Button btnDca, btnMars, btnOneWay, btnTraveller, btnDepart;
    private ImageView ivSwap, ivRocketGalaxy;
    private View dotOneWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout2);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        // 导航按钮
        btnSpaceStations = findViewById(R.id.btn_space_stations);
        btnFlights = findViewById(R.id.btn_flights);
        btnRovers = findViewById(R.id.btn_rovers);

        // 功能按钮
        btnDca = findViewById(R.id.btn_dca);
        btnMars = findViewById(R.id.btn_mars);
        btnOneWay = findViewById(R.id.btn_one_way);
        btnTraveller = findViewById(R.id.btn_traveller);
        btnDepart = findViewById(R.id.btn_depart);

        // 图片视图
        ivSwap = findViewById(R.id.iv_swap);
        ivRocketGalaxy = findViewById(R.id.iv_rocket_galaxy);

        // 指示器
        dotOneWay = findViewById(R.id.dot_one_way);
    }

    private void setupClickListeners() {
        // 导航按钮点击事件
        btnSpaceStations.setOnClickListener(v -> onTabClick(btnSpaceStations, "Space Stations"));
        btnFlights.setOnClickListener(v -> onTabClick(btnFlights, "Flights"));
        btnRovers.setOnClickListener(v -> onTabClick(btnRovers, "Rovers"));

        // 地点按钮点击事件
        btnDca.setOnClickListener(v -> showToast("选择出发地: DCA"));
        btnMars.setOnClickListener(v -> showToast("选择目的地: MARS"));

        // 交换按钮点击事件
        ivSwap.setOnClickListener(v -> swapLocations());

        // 选项按钮点击事件
        btnOneWay.setOnClickListener(v -> showToast("选择单程"));
        btnTraveller.setOnClickListener(v -> showToast("选择旅客数量"));

        // 出发按钮点击事件
        btnDepart.setOnClickListener(v -> showToast("准备出发！"));
    }

    private void onTabClick(Button clickedButton, String tabName) {
        // 重置所有导航按钮样式
        btnSpaceStations.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btnSpaceStations.setTextColor(getResources().getColor(android.R.color.white));
        btnFlights.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btnFlights.setTextColor(getResources().getColor(android.R.color.white));
        btnRovers.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btnRovers.setTextColor(getResources().getColor(android.R.color.white));

        // 设置当前选中按钮样式
        clickedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        clickedButton.setTextColor(getResources().getColor(android.R.color.white));

        showToast("切换到: " + tabName);
    }

    private void swapLocations() {
        String dcaText = btnDca.getText().toString();
        String marsText = btnMars.getText().toString();
        
        btnDca.setText(marsText);
        btnMars.setText(dcaText);
        
        showToast("交换出发地和目的地");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
