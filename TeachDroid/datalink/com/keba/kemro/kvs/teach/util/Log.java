/**
 * 
 */
package com.keba.kemro.kvs.teach.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Simple, platform independent logging system. In the present state of
 * development, log messages are simply forwarded to {@link System.out} or
 * {@link System.err} (in the case of error messages). If the base operating
 * system is Android, the console logging is substituted by using <a
 * href="http://developer.android.com/tools/help/logcat.html">Android's
 * LogCat</a> mechanism.
 * 
 * @author ltz
 * @version 0.1
 * @since 20.09.2013
 */
public class Log {

	private enum LogLevel {
		eVerbose, eDebug, eInfo, eWarning, eError
	};

	private static String	mTagFilter	= null;
	private static boolean	isAndroid;
	private static LogLevel	mLogLevel	= LogLevel.eDebug;
	private static Class<?>	mAndroidLogger;

	/**
	 * Gets the currently applying loglevel
	 * 
	 * @return the log level
	 */
	public static LogLevel getLogLevel() {
		return mLogLevel;
	}

	/**
	 * Sets the log level which should apply. All log messages which have a
	 * "lower" loglevel are discarded.
	 * 
	 * @param _mLogLevel
	 *            the loglevel
	 */
	public static void setLogLevel(LogLevel _mLogLevel) {
		mLogLevel = _mLogLevel;
	}

	/**
	 * Sets a white-list tag filter. Only log-messages whose log tag contains
	 * the tag filter string are logged, all others are discarded.
	 * 
	 * @param _filter
	 *            The filter string
	 */
	public static void setTagFilter(String _filter) {
		mTagFilter = _filter;
	}

	static {
		String hostSystem = System.getProperty("java.runtime.name");
		if (hostSystem.toLowerCase(Locale.getDefault()).contains("android")) {
			isAndroid = true;
			try {
				mAndroidLogger = Class.forName("android.util.Log");
				androidLog("d", "AndroidLogger", "Logger initialized!");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Logs a debug message
	 * 
	 * @param _logTag
	 *            The log tag, for example the class name from where the log
	 *            message originates.
	 * @param _message
	 *            The log message itself.
	 */
	public static void d(String _logTag, Object _message) {
		if (mLogLevel.compareTo(LogLevel.eDebug) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				// android.util.Log.d(_logTag, _message.toString());
				androidLog("d", _logTag, _message);
			} else
				System.out.println("#DEBUG# " + _logTag + "  --  " + _message);
		}
	}

	/**
	 * Sends a log message to the android log system, if the base operating
	 * system was detected to be Android.
	 * 
	 * @param _methodName
	 *            The name of the method to invoke, i.e. "i" for info messages,
	 *            "d" for debug messages, etc.<br/>
	 *            possible values are: <code>v,d,i,w,e</code>
	 * @param _logTag
	 *            The log tag, for example the class name from where the log
	 *            message originates.
	 * @param _message
	 *            The log message itself.
	 * 
	 * @see android.util.Log
	 */
	private static void androidLog(String _methodName, String _logTag, Object _message) {
		if (mAndroidLogger == null)
			throw new RuntimeException(Log.class.toString() + ": Android logging system not initialized!");

		if (_methodName == null || _methodName.equals("") || _logTag == null || _message == null) {
			System.err.println("Log not written, wrong parameters!");
			return;
		}
		try {
			Method debug = mAndroidLogger.getMethod(_methodName, String.class, String.class);
			debug.invoke(null, _logTag, _message);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Logs an info message
	 * 
	 * @param _logTag
	 *            The log tag, for example the class name from where the log
	 *            message originates.
	 * @param _msg
	 *            The log message itself.
	 */
	public static void i(String _logTag, Object _msg) {
		if (mLogLevel.compareTo(LogLevel.eInfo) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				// android.util.Log.i(_logTag, _msg.toString());
			} else
				System.out.println("#INFO# " + _logTag + "  --  " + _msg);
		}
	}

	/**
	 * Logs a warning message
	 * 
	 * @param _logTag
	 *            The log tag, for example the class name from where the log
	 *            message originates.
	 * @param _msg
	 *            The log message itself.
	 */
	public static void w(String _logTag, String _msg) {
		if (mLogLevel.compareTo(LogLevel.eWarning) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				// android.util.Log.w(_logTag, _msg.toString());
				androidLog("w", _logTag, _msg);
			} else
				System.out.println("#WARN# " + _logTag + "  --  " + _msg);
		}
	}

	/**
	 * Logs an error message
	 * 
	 * @param _logTag
	 *            The log tag, for example the class name from where the log
	 *            message originates.
	 * @param _msg
	 *            The log message itself.
	 */
	public static void e(String _logTag, String _msg) {
		if (mLogLevel.compareTo(LogLevel.eError) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				// android.util.Log.e(_logTag, _msg.toString());
				androidLog("e", _logTag, _msg);
			} else
				System.err.println("#ERROR# " + _logTag + "  --  " + _msg);
		}
	}

	/**
	 * Check if the currently selected white-list filter applies
	 * 
	 * @param _logtag
	 *            The logtag which should be examined
	 * @return true if <code>_logtag.contains(<em>tagfilter</em>)</code> returns
	 *         <code>true</code>, false otherwise
	 */
	private static boolean applyTagFilter(String _logtag) {
		return mTagFilter == null || (mTagFilter != null && _logtag.contains(mTagFilter));
	}

	/**
	 * Logs a verbose message
	 * 
	 * @param _logTag
	 *            The log tag, for example the class name from where the log
	 *            message originates.
	 * @param _msg
	 *            The log message itself.
	 */
	public static void v(String _logTag, String _msg) {
		if (mLogLevel.compareTo(LogLevel.eVerbose) <= 0 && applyTagFilter(_logTag)) {
			if (isAndroid) {
				// android.util.Log.v(_logtag, _msg);
				androidLog("v", _logTag, _msg);
			} else
				System.out.println("#VERBOSE# " + _logTag + "  --  " + _msg);
		}
	}
}
