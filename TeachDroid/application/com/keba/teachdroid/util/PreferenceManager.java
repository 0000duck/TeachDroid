/**
 * 
 */
package com.keba.teachdroid.util;

import android.content.SharedPreferences;

import com.keba.teachdroid.TeachdroidApplication;

/**
 * @author ltz
 * 
 */
public class PreferenceManager {

	private static PreferenceManager mInstance;
	private SharedPreferences mPrefs;
	private String mHost = "127.0.0.1";

	public static PreferenceManager getInstance() {
		if (mInstance == null)
			mInstance = new PreferenceManager();
		return mInstance;
	}

	protected PreferenceManager() {
		mPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(TeachdroidApplication.getAppContext());
	}

	public String getHostname() {
		return mPrefs.getString("hostname_ip", mHost);
	}

	public void setHostname(String _host) {
		mHost = _host;
	}

}
