/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.util.Locale;

/**
 * @author ltz
 * 
 */
public class Log {
	private enum LogLevel {
		eVerbose, eDebug, eInfo, eWarning, eError
	};

	private static String	mTagFilter	= null;

	private static boolean	isAndroid;
	private static LogLevel	mLogLevel	= LogLevel.eDebug;

	public static LogLevel getLogLevel() {
		return mLogLevel;
	}

	public static void setLogLevel(LogLevel _mLogLevel) {
		mLogLevel = _mLogLevel;
	}

	public static void setTagFilter(String _filter) {
		mTagFilter = _filter;
	}

	static {
		String hostSystem = System.getProperty("java.runtime.name");
		if (hostSystem.toLowerCase(Locale.getDefault()).contains("android")) {
			isAndroid = true;
		}
	}

	public static void d(String _logTag, Object _message) {
		if (mLogLevel.compareTo(LogLevel.eDebug) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				android.util.Log.d(_logTag, _message.toString());
			} else
				System.out.println("#DEBUG# " + _logTag + "  --  " + _message);
		}
	}

	/**
	 * @param _string
	 * @param _string2
	 */
	public static void i(String _logTag, Object _msg) {
		if (mLogLevel.compareTo(LogLevel.eInfo) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				android.util.Log.i(_logTag, _msg.toString());
			} else
				System.out.println("#INFO# " + _logTag + "  --  " + _msg);
		}
	}

	/**
	 * @param _string
	 * @param _message
	 */
	public static void w(String _logTag, String _msg) {
		if (mLogLevel.compareTo(LogLevel.eWarning) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				android.util.Log.w(_logTag, _msg.toString());
			} else
				System.out.println("#WARN# " + _logTag + "  --  " + _msg);
		}
	}

	/**
	 * @param _string
	 * @param _message
	 */
	public static void e(String _logTag, String _msg) {
		if (mLogLevel.compareTo(LogLevel.eError) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				android.util.Log.e(_logTag, _msg.toString());
			} else
				System.err.println("#ERROR# " + _logTag + "  --  " + _msg);
		}
	}


	private static boolean applyTagFilter(String _logtag) {
		return mTagFilter == null || (mTagFilter != null && _logtag.contains(mTagFilter));
	}

	/**
	 * @param _string
	 * @param _string2
	 */
	public static void v(String _logtag, String _msg) {
		if (mLogLevel.compareTo(LogLevel.eVerbose) <= 0 && applyTagFilter(_logtag)) {
			if (isAndroid) {
				android.util.Log.v(_logtag, _msg);
			} else
				System.out.println("#VERBOSE# " + _logtag + "  --  " + _msg);
		}
	}
}
