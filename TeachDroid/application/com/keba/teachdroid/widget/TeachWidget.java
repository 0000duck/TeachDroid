package com.keba.teachdroid.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.keba.teachdroid.app.R;

/**
 * Implementation of App Widget functionality.
 */
public class TeachWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
		}
		Log.d("WIDGET", "Widget onUpdate()");
	}

	@Override
	public void onDeleted(Context _context, int[] _appWidgetIds) {
		Log.d("WIDGET", "Widget onDeleted()");
		super.onDeleted(_context, _appWidgetIds);

	}

	@Override
	public void onEnabled(Context context) {
		Log.d("WIDGET", "Widget onEnabled()");
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
		Log.d("WIDGET", "Widget onDisabled()");
	}

	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

		CharSequence widgetText = context.getString(R.string.appwidget_text);
		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.teach_widget);
		views.setTextViewText(R.id.appwidget_text, widgetText);

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
