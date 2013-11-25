/*-------------------------------------------------------------------------
*                   (c) 1999 by KEBA Ges.m.b.H & Co
*                            Linz/AUSTRIA
*                         All rights reserved
*--------------------------------------------------------------------------
*    Projekt   : KEMRO.teachview.4
*    Auftragsnr: 5500395
*    Erstautor : ede
*    Datum     : 01.04.2003
*--------------------------------------------------------------------------
*      Revision:
*        Author:
*          Date:
*------------------------------------------------------------------------*/
package com.keba.kemro.teach.dfl.compilerError;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.util.KDflLogger;
import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

import java.io.*;
import java.util.*;


/**
 * Representation eines Compilerfehlers.
 */
public class KCompilerError {
   private static Locale m_language;
   private static ResourceBundle m_bundle;
   private final static String ERROR_PREFIX = "Compiler.msg.";
   private final static String ERROR_DEFAULT = "Compiler.msg.unknownError";
   private final static String ERROR_TEXT = "Compiler.msg.errorText";
   private final static String ERROR_TEXTIN = "Compiler.msg.errorTextIn";
   private final static String STRING_EMPTY = "";
   private static final String RESOURCE_PATH = "compilererrorTx";
   public  static String RESOURCE_PATH_LOCAL = "com.keba.kemro.teach.dfl.text.compilererrorTx";
   private static Locale m_prop_locale;
   /** Typ des KCompilerErrors ist eine Information */
   public final static int INFO = RpcTcErrorKind.rpcInfo;
   /** Typ des KCompilerErrors ist eine Warnung */
   public final static int WARNING = RpcTcErrorKind.rpcWarning;
   /** Typ des KCompilerErrors ist ein Fehler */
   public final static int ERROR = RpcTcErrorKind.rpcError;
   /** Typ des KCompilerErrors ist eine Fehler Inforrmation */
   public final static int ERROR_INFO = RpcTcErrorKind.rpcErrorInfo;
   
   public static final int PROJECT_CHANGED_ERROR = 145;

   /** Parameterliste */
   protected Object[] parameters = null;
   private String dirEntryPath;
   private int row;
   private int column;
   private TcErrorMessage m_errorElement = null;
   private String m_text;
   private Locale m_text_locale;

	public static void setDefaultTranslationFile(String path){
		RESOURCE_PATH_LOCAL = path;
	}
   
   /**
    * Konstruktor für das Element
    *
    * @param error Fehlerelement
    */
   protected KCompilerError (TcErrorMessage error) {
      m_errorElement = error;
      dirEntryPath = convertToDirEntryPath((String) (getErrorParams())[0]);
      row = ((Integer) getErrorParams()[1]).intValue();
      column = ((Integer) getErrorParams()[2]).intValue();
   }

   /**
    * Liefert den Text des Fehler in der aktuellen Sprache
    *
    * @return Fehlertext
    */
   public String getErrorMsg () {
      if ((m_text != null) && (Locale.getDefault().equals(m_text_locale))) {
         return m_text;
      }
      m_text_locale = Locale.getDefault();
      String txt;
      String file;
      String additional;
      
      String in =  getProperty(ERROR_TEXTIN, getResourceBundle());
      if (in == null){
    	  in = "in";
      }
      txt = getText(getErrorNr());
      getErrorParams();
      try {
         file = getDirEntryPath().replace('\\', '/');
//         if (!SHOW_FULL_PATH) {
//            file = file.substring(file.lastIndexOf('/') + 1);
//         }
         int pos = in.indexOf('%');
         if (pos >0){
        	 String in1 = in.substring(0, pos).trim();
        	 String in2 = in.substring(pos +1).trim();
        	 file = " "+in1+" \"" + file.substring(0, file.lastIndexOf(".")) + "\"(" + parameters[1] + "/" + parameters[2] + ") " + in2;
         }else{
            file = " "+in+" \"" + file.substring(0, file.lastIndexOf(".")) + "\"(" + parameters[1] + "/" + parameters[2] + ")";
         }
         
      } catch (Exception e) {
         file = STRING_EMPTY;
      }
      try {
         additional = (String) parameters[3];
         if (additional.length() <= 0) {
            additional = STRING_EMPTY;
         } else {
            additional = " \"" + additional + "\"";
         }
      } catch (Exception e) {
         additional = STRING_EMPTY;
      }
      m_text = txt + additional + file;
      return m_text;
   }
   
   
   /**
    * Liefert den Typ des Fehlers (Error, Info , .. )
    *
    * @return Typ des Fehlers
    */
   public int getErrorKind () {
      return m_errorElement.getKind();
   }

   /**
    * Liefert die Nummer des Fehlers
    * 
    * @return Liefert die Nummer des Fehlers
    */
   public int getErrorNr () {
      return m_errorElement.getErrorNr();
   }

   /**
    * Liefert die Zeileninformation des Fehlers
    *
    * @return Zeileninformation des Fehlers
    */
   public int getRow () {
      return row;
   }

   /**
    * Liefert die Spalteninformation des Fehlers
    *
    * @return Die Spalteninformation des Fehlers
    */
   public int getColumn () {
      return column;
   }

   /**
    * Liefert den Pfad der Datei in dem der Fehler aufgetreten ist
    *
    * @return Der Pfad der Fehlerhaften Datei
    */
   public String getDirEntryPath () {
      return dirEntryPath;
   }

   /**
    * Liefert das Array mit den Fehlerparametern
    *
    * @return Liefert ein Array mit Fehlerparametern
    */
   public Object[] getErrorParams () {
      if (parameters == null) {
         int length;
         length = m_errorElement.getParameterCount();
         parameters = new Object[length];
         for (int i = 0; i < length; i++) {
            //            param = m_errorElement.errorParams[i];
            switch (m_errorElement.getParameterKind(i)) {
            //            switch (param.errorType.value) {
            case RpcTcErrorParamType.rpcFloatParam:
               parameters[i] = new Float(m_errorElement.getParameterFloatValue(i));
               break;
            case RpcTcErrorParamType.rpcIntParam:
               parameters[i] = new Integer(m_errorElement.getParameterIntValue(i));
               break;
            case RpcTcErrorParamType.rpcStringParam:
               parameters[i] = m_errorElement.getParameterStringValue(i);
               break;
            default:
               KDflLogger.error(this, "KCompilerError: Unknown Compiler Error in KCompilerError.getErrorParams()");
            }
         }
      }
      return parameters;
   }

   /**
    * Liefert den Fehlertext in der aktuellen Sprache
    *
    * @return Fehlertext in der aktuellen Sprache
    */
   public String toString () {
      return getErrorMsg();
   }

   private static ResourceBundle getResourceBundle () {
      if (Locale.getDefault().equals(m_language) && (m_bundle != null)) {
         return m_bundle;
      }
      m_language = Locale.getDefault();
      try {
         m_bundle = ResourceBundle.getBundle(RESOURCE_PATH, m_language);
      } catch (MissingResourceException e) {
         try {
            m_bundle = ResourceBundle.getBundle(RESOURCE_PATH_LOCAL, m_language);
         } catch (MissingResourceException ex) {
            KDflLogger.error(KCompilerError.class, ex);
         }
      }
      return m_bundle;
   }

   private static void checkFormatLanguage () {
      if (!Locale.getDefault().equals(m_prop_locale)) {
         m_prop_locale = Locale.getDefault();
      }
   }

   private static String getText (int errorNr) {
      String errTxt;
      String errMsg;
      checkFormatLanguage();
      errMsg = getProperty(ERROR_PREFIX + errorNr, getResourceBundle());
      if (errMsg == null) {
         errMsg = getProperty(ERROR_DEFAULT, getResourceBundle());
         if (errMsg == null) {
            errMsg = "Unknown / Undocumented Error ";
         }
      }
      errTxt = getProperty(ERROR_TEXT, getResourceBundle());
      if (errTxt == null) {
         errTxt = "Error";
      }
      return errTxt + " (" + errorNr + "): " + errMsg;
   }
   
 
   private static String getProperty (String key, ResourceBundle bundle) {
      if ((key != null) && (bundle != null)) {
	   	try {
	         return bundle.getString(key);
	      } catch (MissingResourceException e) {
	      	// no value for this key
	      }
      }
      return null;
   }

   private static String convertToDirEntryPath (String str) {
      str.replace('\\', '/');
      int index = str.lastIndexOf(KTcDfl.PROJECT_DIR_EXTENSION);
      if (0 < index) {
         index = str.lastIndexOf('/', index - 1);
      }
      if (index < 0) {
         index = 0;
      }
      String dirEntryPath = str.substring(index).replace('/', File.separatorChar).toUpperCase();
      return (dirEntryPath.charAt(0) == File.separatorChar) ? dirEntryPath: File.separator + dirEntryPath;
   }
}
