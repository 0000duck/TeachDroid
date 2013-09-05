package com.keba.teachdroid.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater;
import com.keba.kemro.kvs.teach.controller.KvtAlarmUpdater.KvtAlarmUpdaterListener;
import com.keba.kemro.serviceclient.alarm.KMessage;

public class NotificationThread extends Thread implements KvtAlarmUpdaterListener {
	private Context context;
	private int notificationCount = 0;

	public NotificationThread(Context _context) {
		super("Notifications");
		context = _context;
		KvtAlarmUpdater.addListener(this);
	}

	public void messageUpdated(int lastMessageType, Object lastMessage) {
		// TODO Auto-generated method stub

	}

	public void messageAdded(String _bufferName, KMessage _msg) {
		Message msg = new Message(_msg);
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(msg.getImageID()).setContentTitle(msg.getDate().toString()).setContentText(_msg.getMessageText()).setSound(soundUri);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, InfoActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(InfoActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.ledARGB=0xffffffff; //color, in this case, white
		notification.ledOnMS=1000; //light on in milliseconds
		notification.ledOffMS=4000; //light off in milliseconds
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.number = msg.getImageID();

		mNotificationManager.notify(msg.getID(), notification);
		notificationCount++;
	}

	public void messageRemoved(String _bufferName, KMessage _msg) {
		// TODO Auto-generated method stub

	}

	public void messageChanged(String _bufferName, KMessage _msg) {
		// TODO Auto-generated method stub

	}

}
