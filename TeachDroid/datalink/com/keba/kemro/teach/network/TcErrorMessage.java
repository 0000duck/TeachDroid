package com.keba.kemro.teach.network;

/**
 * Fehlermeldungsobjekt
 */
public class TcErrorMessage {
   /** Info - Message */
   public static final int INFO_MSG = 1;
   /** Warning - Message */
   public static final int WARNING_MSG = 2;
   /** Error - Message */
   public static final int ERROR_MSG = 3;
   /** Error-Info - Message */
   public static final int ERROR_INFO_MSG = 4;
   
   /** Integer Message - Parameter - Typ */
   public static final int INT_PARAMETER = 0;
   /** Float Message - Parameter - Typ */
   public static final int FLOAT_PARAMETER = 1;
   /** String Message - Parameter - Typ */
   public static final int STRING_PARAMETER = 2;

   public int errorKind;
	public int errorNr;
	public int nrParams;
	public int[] paramKinds;
	public int[] paramIValues;
	public float[] paramFValues;
	public String[] paramSValues;

   protected TcErrorMessage () {
   }

   /**
    * Liefert die Art (INFO_MSG, WARING_MSG, ERROR_MSG und ERROR_INFO_MSG) der Fehlermeldung zurück.
    *
    * @return Art
    */
   public int getKind () {
      return errorKind;
   }

   /**
    * Liefert die Fehlernummer zurück.
    *
    * @return Fehlernummer
    */
   public int getErrorNr () {
      return errorNr;
   }

   /**
    * Liefert die Anzahl der Parameter zurück.
    *
    * @return Parameteranzahl
    */
   public int getParameterCount () {
      return nrParams;
   }

   /**
    * Liefert den Parametertyp (INT_PARAMETER, FLOAT_PARAMETER, STRING_PARAMETER) zurück.
    *
    * @param parameterIndex Parameterindex
    *
    * @return Parametertyp
    */
   public int getParameterKind (int parameterIndex) {
      return paramKinds[parameterIndex];
   }

   /**
    * Liefert den Integer - Parameterwert zurück.
    *
    * @param parameterIndex Parameterindex
    *
    * @return Integer - Wert
    */
   public int getParameterIntValue (int parameterIndex) {
      return paramIValues[parameterIndex];
   }

   /**
    * Liefert den Float - Parameterwert zurück.
    *
    * @param parameterIndex Parameterindex
    *
    * @return Float - Wert
    */
   public float getParameterFloatValue (int parameterIndex) {
      return paramFValues[parameterIndex];
   }

   /**
    * Liefert den String - Parameterwert zurück.
    *
    * @param parameterIndex Parameterindex
    *
    * @return String - Wert
    */
   public String getParameterStringValue (int parameterIndex) {
      return paramSValues[parameterIndex];
   }
}