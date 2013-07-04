/**
 * 
 */
package com.keba.kemro.serviceclient.alarm;

import java.util.*;

public interface KMessageTextSource {


	/**
	 * Returns a enumeration of input streams which contains the cvs message text
	 * @return enumeration of readers
	 */
	public Enumeration getLanguageTexts();


	public String translateParameter(String parameter);
	

}