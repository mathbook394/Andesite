package com.java.andesite.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.java.andesite.MainActivity;
import com.java.andesite.R;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static final String EXTRA_ITEM_POSITION = "com.java.andesite.widget.EXTRA_ITEM_POSITION";
    public static final String CLICK_ACTION = "com.java.andesite.widget.CLICK_ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // RemoteViews 객체 생성
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // 서비스 인텐트 설정
            Intent serviceIntent = new Intent(context, TodoRemoteViewsService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            // ListView에 어댑터 설정
            views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

            // 항목 클릭시 실행될 인텐트 설정
            Intent clickIntent = new Intent(context, MainActivity.class);
            clickIntent.setAction(CLICK_ACTION);
            PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0,
                    clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntent);

            // 전체 위젯 클릭시 메인 액티비티로 이동
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0,
                    mainIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widget_title, mainPendingIntent);

            // 위젯 업데이트
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction() != null) {
            if (intent.getAction().equals("com.java.andesite.widget.UPDATE_WIDGET")) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, AppWidgetProvider.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

                // 데이터 갱신
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            }
        }
    }
}