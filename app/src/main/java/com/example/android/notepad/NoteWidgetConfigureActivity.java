package com.example.android.notepad;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * 代码的功能
 * 小部件配置页：选择一个笔记进行固定
 */
public class NoteWidgetConfigureActivity extends Activity {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);

        // 获取AppWidgetId
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        // 加载所有笔记标题
        Cursor c = getContentResolver().query(NotePad.Notes.CONTENT_URI,
                new String[]{ NotePad.Notes._ID, NotePad.Notes.COLUMN_NAME_TITLE },
                null, null, NotePad.Notes.DEFAULT_SORT_ORDER);

        final ArrayList<String> titles = new ArrayList<>();
        final ArrayList<Long> ids = new ArrayList<>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                titles.add(c.getString(c.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE)));
                ids.add(c.getLong(c.getColumnIndex(NotePad.Notes._ID)));
            }
            c.close();
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long noteId = ids.get(position);
                SharedPreferences prefs = getSharedPreferences(NoteWidgetProvider.PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putLong(NoteWidgetProvider.KEY_PREFIX_NOTE_ID + mAppWidgetId, noteId).apply();

                // 更新小部件
                AppWidgetManager mgr = AppWidgetManager.getInstance(NoteWidgetConfigureActivity.this);
                NoteWidgetProvider.updateWidget(NoteWidgetConfigureActivity.this, mgr, mAppWidgetId);

                // 返回OK
                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}