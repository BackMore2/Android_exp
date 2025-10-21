package com.backmo.test2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "animal_channel";
    private static final int REQ_POST_NOTIF = 1001;

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

        // 新增：跳转到两个演示页面
        findViewById(R.id.btnXmlMenuDemo).setOnClickListener(v ->
                startActivity(new Intent(this, XmlMenuDemoActivity.class)));
        findViewById(R.id.btnActionModeDemo).setOnClickListener(v ->
                startActivity(new Intent(this, ActionModeDemoActivity.class)));

        createNotificationChannel();
        ensurePostNotificationPermission();

        showLoginDialog();

        ListView listView = findViewById(R.id.listView);

        String[] animalNames = new String[]{"Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant"};
        int[] animalIcons = new int[]{
                R.drawable.lion,
                R.drawable.tiger,
                R.drawable.monkey,
                R.drawable.dog,
                R.drawable.cat,
                R.drawable.elephant
        };

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < animalNames.length; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("title", animalNames[i]);
            row.put("icon", animalIcons[i]);
            data.add(row);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.list_item,
                new String[]{"icon", "title"},
                new int[]{R.id.icon, R.id.title}
        );
        adapter.setViewBinder((view, data1, textRepresentation) -> {
            if (view.getId() == R.id.icon && data1 instanceof Integer) {
                int resId = (Integer) data1;
                int sizePx = (int) (48 * view.getResources().getDisplayMetrics().density);
                RequestOptions opts = new RequestOptions().override(sizePx, sizePx).centerCrop();
                Glide.with(view.getContext()).load(resId).apply(opts).into((ImageView) view);
                return true;
            }
            return false;
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String name = animalNames[position];
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            sendAnimalNotification(name);
        });
    }

    private void showLoginDialog() {
        android.view.View content = getLayoutInflater().inflate(R.layout.dialog_login, null, false);
        EditText etUser = content.findViewById(R.id.etUsername);
        EditText etPass = content.findViewById(R.id.etPassword);
        etUser.setText("admin");
        etPass.setText("admin");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("ANDROID APP")
                .setView(content)
                .setCancelable(false)
                .setNegativeButton("Cancel", (d, w) -> finish())
                .setPositiveButton("Sign in", null)
                .create();

        dialog.setOnShowListener(d -> {
            android.widget.Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setOnClickListener(v -> {
                String u = etUser.getText().toString().trim();
                String p = etPass.getText().toString().trim();
                if ("admin".equals(u) && "admin".equals(p)) {
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Animal Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void ensurePostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQ_POST_NOTIF);
            }
        }
    }

    private void sendAnimalNotification(@NonNull String animalName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(animalName)
                .setContentText(animalName + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}