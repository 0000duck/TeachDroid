/**
 * 
 */
package com.keba.teachdroid;

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
//		mNotificationThread = new AlarmUpdaterThread(context);
//		mNotificationThread.start();
	}

	public static Context getAppContext() {
		return context;
	}
}
