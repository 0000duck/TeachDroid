/**
 * 
 */
package com.keba.teachdroid;

import com.keba.teachdroid.app.NotificationThread;

import android.app.Application;
import android.content.Context;

/**
 * @author ltz
 *
 */
public class TeachdroidApplication extends Application {
	private static Context	context;
	private static Thread mNotificationThread;

	@Override
	public void onCreate() {
		super.onCreate();
		TeachdroidApplication.context = getApplicationContext();
		mNotificationThread = new NotificationThread(context);
		mNotificationThread.start();
	}

	public static Context getAppContext() {
		return TeachdroidApplication.context;
	}
}
