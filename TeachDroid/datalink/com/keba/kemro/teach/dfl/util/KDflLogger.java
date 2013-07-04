/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Project : KEMRO.teachview.4
 *    Account : 5500566
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.util;

import java.util.Hashtable;

import android.util.Log;

/**
 * Manages the logging at KVS-Teach
 */
public class KDflLogger {
	private static final Hashtable classCategories = new Hashtable(113);
	
	/**
	 * Log a message with the info priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		string to append
	 */
	public static void info(Class c, String message) {
		Log.i(c.toString(), message);
	}
	
	/**
	 * Log a message with the info priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		string to append
	 */
	public static void info(Object instance, String message) {
		info(instance.getClass(), message);
	}
	
	/**
	 * Log a message with the info priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void info(Class c, Object message) {
		Log.i(c.toString(), message.toString());
	}
	
	/**
	 * Log a message with the info priority.
	 * 
	 * @param 	instance 	classobject, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void info(Object instance, Object message) {
		info(instance.getClass(), message);
	}
	
	/**
	 * Log a message with the info priority.
	 * 
	 * @param	c		 		class, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace
	 */
	public static void info(Class c, String message, Throwable t) {
		Log.i(c.toString(), message);
	}
	
	/**
	 * Log a message with the info priority.
	 * 
	 * @param	instance		classobject, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace
	 */
	public static void info(Object instance, String message, Throwable t) {
		info(instance.getClass(), message, t);
	}
	
	/**
	 * Log a message with the warn priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		string to append
	 */
	public static void warn(Class c, String message) {
		Log.w(c.toString(), message);
	}
	
	/**
	 * Log a message with the warn priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		string to append
	 */
	public static void warn(Object instance, String message) {
		warn(instance.getClass(), message);
	}
	
	/**
	 * Log a message with the warn priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void warn(Class c, Object message) {
		Log.w(c.toString(), message.toString());
	}
	
	/**
	 * Log a message with the warn priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void warn(Object instance, Object message) {
		warn(instance, message);
	}
	
	/**
	 * Log a message with the warn priority.
	 * 
	 * @param	c		 		class, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace
	 */
	public static void warn(Class c, String message, Throwable t) {
		Log.w(c.toString(), message);
	}
	
	/**
	 * Log a message with the warn priority.
	 * 
	 * @param	instance		classobject, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace
	 */
	public static void warn(Object instance, String message, Throwable t) {
		warn(instance.getClass(), message, t);
	}
	
	/**
	 * Log a message with the error priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		string to append
	 */
	public static void error(Class c, String message) {
		Log.e(c.toString(), message);
	}
	
	/**
	 * Log a message with the error priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		string to append
	 */
	public static void error(Object instance, String message) {
		error(instance.getClass(), message);
	}
	
	/**
	 * Log a message with the error priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void error(Class c, Object message) {
		Log.e(c.toString(), message.toString());
	}
	
	/**
	 * Log a message with the error priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void error(Object instance, Object message) {
		error(instance.getClass(), message);
	}
	
	/**
	 * Log a message with the error priority.
	 * 
	 * @param	c		 		class, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace
	 */
	public static void error(Class c, String message, Throwable t) {
		Log.e(c.toString(), message);
	}
	
	/**
	 * Log a message with the error priority.
	 * 
	 * @param	instance		classobject, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace
	 */
	public static void error(Object instance, String message, Throwable t) {
		error(instance.getClass(), message, t);
	}
		
	/**
	 * Log a message with the debug priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		string to append
	 */
	public static void debug(Class c, String message) {
		Log.d(c.toString(), message);
	}
	
	/**
	 * Log a message with the debug priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		string to append
	 */
	public static void debug(Object instance, String message) {
		debug(instance.getClass(), message);
	}
		
	/**
	 * Log a message with the debug priority.
	 * 
	 * @param 	c				class, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void debug(Class c, Object message) {
		Log.d(c.toString(), message.toString());
	}
	
	/**
	 * Log a message with the debug priority.
	 * 
	 * @param 	instance		classobject, where log method is called
	 * @param 	message		object to convert to String before writing into the log file
	 */
	public static void debug(Object instance, Object message) {
		debug(instance.getClass(), message);
	}
	 
   /** 
	 *	Log a message with the debug priority.
    *
    * @param	c		 		class, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace  
    */  
	public static void debug(Class c, String message, Throwable t) {
		Log.d(c.toString(), message);
	}
	
	/** 
	 *	Log a message with the debug priority.
    *
    * @param	instance		classobject, where log method is called
    * @param	message  	string to append
    * @param	t 				the exception to log, including a stack trace  
    */  
	public static void debug(Object instance, String message, Throwable t) {
		debug(instance.getClass(), message, t);
	}
	
	
}
