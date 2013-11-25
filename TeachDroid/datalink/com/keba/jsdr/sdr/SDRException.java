
package com.keba.jsdr.sdr;

public class SDRException extends Exception {

   public static final int INVALID_RESULT_LEN = 1;
   public static final int INVALID_ARGUMENT   = 2;
   public static final int INVALID_HANDLE     = 3;
   public static final int INVALID_STATION    = 4;
   public static final int INVALID_SERVICE    = 5;
   public static final int TIMEOUT            = 6;
   public static final int OVERFLOW           = 7;
   public static final int SYSTEM             = 8;
   public static final int UNKNOWN_MSG        = 9;
   public static final int NOT_CONNECTED = 20;
   public static final int CONNECTION_BROKEN = 21;
   
   public static final int PROTOCOL_ERROR = 50;
   public static final int PROT_ERROR_NO_REQEST_HDL = 50;
   public static final int DATA_ERROR = 51;
   public static final int DATA_ERROR_MORE_EXPECTED = 52;
   
   private int             m_reason;

   public SDRException(int reason) {
      m_reason = reason;
   }

   public String getMessage() {
      String msg = null;
      switch (m_reason) {
      case INVALID_RESULT_LEN:
         msg = "invalid result length";
         break;
      case INVALID_ARGUMENT:
         msg = "invalid arguments";
         break;
      case INVALID_HANDLE:
         msg = "invalid handle";
         break;
      case INVALID_STATION:
         msg = "invalid station";
         break;
      case INVALID_SERVICE:
         msg = "invalid service";
         break;
      case TIMEOUT:
         msg = "timout";
         break;
      case OVERFLOW:
         msg = "overflow";
         break;
      case SYSTEM:
         msg = "system";
         break;
      case UNKNOWN_MSG:
         msg = "unknown message id";
         break;
      case PROT_ERROR_NO_REQEST_HDL:
         msg = "missing request hdl";
         break;
      case DATA_ERROR:
         msg = "error in data stream";
         break;
      case DATA_ERROR_MORE_EXPECTED:
         msg = "error in data stream: more data expected";
         break;
      case NOT_CONNECTED:
         msg = "client not connected";
         break;

      default:
         msg = "unknown reason: "+m_reason;
      }
      return msg;
   }

   public int getReason() {
      return m_reason;
   }

}
