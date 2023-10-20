package com.mark.pocketmanager.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mark.pocketmanager.R;


/**
 * Implementation of App Widget functionality.
 */
public class tabletool extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tabletool);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    private static final String stri = "income";
    private static final String stro = "outcome";
    private static final String strg = "graph";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context,
                tabletool.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.tabletool);

            remoteViews.setOnClickPendingIntent(R.id.appwidget_btn,
                    getPendingSelfIntent(context, stri));
            remoteViews.setOnClickPendingIntent(R.id.appwidget_btn2,
                    getPendingSelfIntent(context, stro));
            remoteViews.setOnClickPendingIntent(R.id.appwidget_btn3,
                    getPendingSelfIntent(context, strg));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (stri.equals(intent.getAction())) {
            Toast.makeText(context, "Income!", Toast.LENGTH_SHORT).show();
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.tabletool);
            views.setTextViewText(R.id.appwidget_text_value,"本日");
            views.setTextViewText(R.id.appwidget_text,"本日");
            views.setTextColor(R.id.appwidget_text, Color.parseColor("#0072E3"));
            views.setTextViewText(R.id.appwidget_text2,"本月");
            views.setTextColor(R.id.appwidget_text2,Color.parseColor("#0072E3"));
            AppWidgetManager.getInstance(context).updateAppWidget(
                    new ComponentName(context, tabletool.class),views);

        } else if (stro.equals(intent.getAction())) {
            Toast.makeText(context, "Outcome!", Toast.LENGTH_SHORT).show();
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.tabletool);
            views.setTextViewText(R.id.appwidget_text,"本日");
            views.setTextColor(R.id.appwidget_text, Color.parseColor("#FF0000"));
            views.setTextViewText(R.id.appwidget_text2,"本月");
            views.setTextColor(R.id.appwidget_text2, Color.parseColor("#FF0000"));
            AppWidgetManager.getInstance(context).updateAppWidget(
                    new ComponentName(context, tabletool.class),views);

        } else if (strg.equals(intent.getAction())) {
            Toast.makeText(context, "Total!", Toast.LENGTH_SHORT).show();
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.tabletool);
            views.setTextViewText(R.id.appwidget_text,"這日");
            views.setTextColor(R.id.appwidget_text, Color.parseColor("#FF000000"));
            views.setTextViewText(R.id.appwidget_text2,"上月");
            views.setTextColor(R.id.appwidget_text2, Color.parseColor("#FF000000"));
            AppWidgetManager.getInstance(context).updateAppWidget(
                    new ComponentName(context, tabletool.class),views);
        };
    }
}