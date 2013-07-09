package com.keba.teachdroid.app;

import java.text.MessageFormat;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.keba.teachdroid.data.InitializationTask;
import com.keba.teachdroid.data.InitializationTask.InitializationListener;
import com.keba.teachdroid.data.RobotControlProxy;

public class MainActivity extends Activity implements InitializationListener {

	private String				m_host					= "10.0.0.5";
	final String				m_connectFormatString	= "Connecting...  {0} % complete";
	protected ProgressDialog	m_dlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (!RobotControlProxy.isConnected()) {
			final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (!wifi.isWifiEnabled()) {

				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

				dlgAlert.setMessage("Click OK to enable WiFi, Cancel to quit app!");

				dlgAlert.setTitle("Info");
				dlgAlert.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface _dialog, int _which) {
						wifi.setWifiEnabled(true);
						connectToPlc();
					}
				});
				dlgAlert.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface _dialog, int _which) {
						finish();
					}
				});
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();

			} else
				connectToPlc();
		}

		Button b = (Button) findViewById(R.id.button1);
		if (b != null)
			b.setOnClickListener(new OnClickListener() {

				public void onClick(View _v) {

					RobotControlProxy.toggleDrivesPower();

				}
			});

	}

	/**
	 * 
	 */
	private void connectToPlc() {
		InitializationTask itask = new InitializationTask(this);
		itask.execute(m_host);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem _item) {

		if (_item.getTitle().equals(getString(R.string.menu_settings))) {
			Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
			startActivity(settingsActivity);

		}
		return super.onOptionsItemSelected(_item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationBegin()
	 */
	public void initializationBegin() {

		runOnUiThread(new Runnable() {

			public void run() {

				// m_dlg = ProgressDialog.show(MainActivity.this,
				// "Connecting...", "Connecting to " + m_host, true, true);
				m_dlg = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
				m_dlg.setTitle("Connecting...");
				m_dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				m_dlg.setCancelable(true);
				m_dlg.setCanceledOnTouchOutside(false);
				m_dlg.setMessage("Connecting to " + m_host);

				m_dlg.setIndeterminate(false);
				m_dlg.show();

				// m_dlg.setMessage("Begin initialization...");

			}

		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #initializationComplete()
	 */
	public void initializationComplete(final boolean _success) {

		runOnUiThread(new Runnable() {
			public void run() {
				// m_dlg.dismiss();

				// String msg = "Connection " + (_success ? "established" :
				// "failed!!");

				// m_dlg.setMessage(msg);
				//
				// try {
				// Thread.sleep(2000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				m_dlg.dismiss();

			}
		});
		List<String> l = RobotControlProxy.getMessageBacklog();
		if (l != null) {
			EditText v = (EditText) findViewById(R.id.alarmLogTextview);
			for (String s : l) {
				v.setText(s + "\n" + v.getText());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.keba.teachdroid.app.data.InitializationTask.InitializationListener
	 * #setInitializationProgress(int)
	 */
	public void setInitializationProgress(final int _progress) {
		runOnUiThread(new Runnable() {
			public void run() {
				m_dlg.setTitle(MessageFormat.format(m_connectFormatString, _progress));
				m_dlg.setProgress(_progress);
			}
		});
	}

}
