package com.example.android.notepad;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * 代码的功能
 * 笔记桌面小部件 Provider：根据保存的笔记ID更新标题与内容，并支持点击打开编辑页
 */
public class NoteWidgetProvider extends AppWidgetProvider {

    public static final String PREFS_NAME = "note_widget_prefs";
    public static final String KEY_PREFIX_NOTE_ID = "widget_note_id_";
    public static final String ACTION_REFRESH = "com.example.android.notepad.ACTION_UPDATE_NOTE_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_REFRESH.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                updateWidget(context, mgr, appWidgetId);
            } else {
                // 刷新所有
                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                ComponentName cn = new ComponentName(context, NoteWidgetProvider.class);
                int[] ids = mgr.getAppWidgetIds(cn);
                for (int id : ids) updateWidget(context, mgr, id);
            }
        }
    }

    /**
     * 代码的功能
     * 更新指定小部件实例的视图内容
     */
    public static void updateWidget(Context context, AppWidgetManager mgr, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_widget);

        long noteId = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getLong(KEY_PREFIX_NOTE_ID + appWidgetId, -1);

        String title = "选择一个笔记";
        String content = "用于显示笔记内容摘要";

        if (noteId > -1) {
            Uri noteUri = Uri.withAppendedPath(NotePad.Notes.CONTENT_URI, String.valueOf(noteId));
            Cursor c = context.getContentResolver().query(noteUri,
                    new String[]{ NotePad.Notes.COLUMN_NAME_TITLE, NotePad.Notes.COLUMN_NAME_NOTE },
                    null, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    title = c.getString(c.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE));
                    content = c.getString(c.getColumnIndex(NotePad.Notes.COLUMN_NAME_NOTE));
                }
                c.close();
            }

            // 点击打开编辑页
            Intent editIntent = new Intent(Intent.ACTION_EDIT, noteUri);
            PendingIntent pi = PendingIntent.getActivity(context, appWidgetId, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_title, pi);
            views.setOnClickPendingIntent(R.id.widget_content, pi);
        }

        views.setTextViewText(R.id.widget_title, title == null ? "" : title);
        views.setTextViewText(R.id.widget_content, content == null ? "" : content);

        mgr.updateAppWidget(appWidgetId, views);
    }
}