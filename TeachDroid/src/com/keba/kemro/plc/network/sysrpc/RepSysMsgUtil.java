
package com.keba.kemro.plc.network.sysrpc;

import java.text.NumberFormat;

import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgParamKind;
import com.keba.kemro.plc.network.sysrpc.RepSys.TRepSysMsgParamUnion;
//import com.keba.kemro.plc.RepSysTxtBundle;

public class RepSysMsgUtil {
   protected static final NumberFormat m_fmt = NumberFormat.getNumberInstance();

	// public static String buildMessageText(int classId, int compId, int
	// instId,
	// int msgId, Object[] msgParam, String messageTxt,
	// RepSysTxtBundle language) {
	// String txt = null;
	// try {
	// txt = language.getString(String.valueOf(compId) + "_"
	// + String.valueOf(msgId));
	// } catch (MissingResourceException ex) {
	// // ignore
	// }
	//
	// if ((txt == null)) { // CR_0031137 if not defined return default from
	// // control
	// txt = messageTxt;
	// if (txt != null) {
	// return txt;
	// }
	// }
	// if (txt == null) {
	// txt = "... %n - %1, %2, %3, %4, %5, %6, %7, %8";
	// }
	// int pos = 0;
	// StringBuffer result = new StringBuffer();
	// while (pos < txt.length()) {
	// char ch = txt.charAt(pos);
	// if (ch == '%') { // first % sign --> a parameters specification
	// // may follow
	// char lookahead = (pos + 1 < txt.length()) ? txt.charAt(pos + 1)
	// : ' ';
	// String format = "";
	// if (lookahead == '%') { // we want to output the percent sign
	// // (=%%)
	// pos++;
	// result.append(ch);
	// } else if (Character.isDigit(lookahead)) { // %1?, %2?, ...,
	// // %8?
	// format = extractFormat(txt, pos + 2);
	// result.append(replaceParameter(lookahead, format, classId,
	// compId, instId, msgId, msgParam));
	// pos += format.length() + 1;
	// } else if (lookahead == 'n') { // %n?
	// format = extractFormat(txt, pos + 2);
	// result.append(replaceParameter(lookahead, format, classId,
	// compId, instId, msgId, msgParam));
	// pos += format.length() + 1;
	// } else { // error
	// result.append("..."); // control uses this to specify
	// // untranslateable elements
	// }
	// } else {
	// result.append(ch);
	// }
	// pos++;
	// }
	// return result.toString().trim();
	// }

   private static String extractFormat(String txt, int startPos) {
      char ch1 = txt.length() > startPos ? txt.charAt(startPos) : ' ';
      char ch2 = txt.length() > startPos + 1 ? txt.charAt(startPos + 1) : ' ';
      String result = "";

      switch (ch1) {
      case 't':
         // no break
      case 'b':
         // no break
      case 'x':
         result += ch1;
         break;
      case 'f':
         result += ch1;
         if (Character.isDigit(ch2)) {
            result += ch2;
         }
         break;
      }
      return result;
   }

   private static String replaceParameter(char parmNumber, String formatString,
         int classId, int compId, int instId, int msgId, Object[] msgParam) {
      String result = "";
      int digit = parmNumber == 'n' ? -1 : Character.digit(parmNumber, 10) - 1;
      formatString = formatString.trim();
      int nrOfParams = msgParam.length;
      if (nrOfParams > digit) {
         // int paramKind = digit >= 0 ? msgParam[digit].getKind()
         // : RepSysParam.INT;
         Object paramValue = digit >= 0 ? msgParam[digit] : new Integer(instId);

         // switch (paramKind) {
         // case RepSysParam.LONG: {
         if (paramValue instanceof Long) {
            long value = (parmNumber == 'n') ? instId : ((Long) paramValue)
                  .longValue();
            if (formatString.equals("x")) {
               result = "0x" + Long.toHexString(value);
            } else if (formatString.equals("b")) {
               result = "0b" + Long.toBinaryString(value);
            } else if (formatString.equals("t")) {
               // KvtLogger.error(KMessageService.class, msg.getShortMessageID()
               // + " implementation missing for %?t");
               // TODO: implement %1t
            } else if (formatString.equals("")) {
               result = Long.toString(value);
            } else {
               result = "...";
               // KvtLogger.warn(KMessageService.class, msg.getShortMessageID()
               // + " invalid format specification" + digit);
            }
         } else
         // break;

         if (paramValue instanceof Integer) {
            int value = (parmNumber == 'n') ? instId : ((Integer) paramValue)
                  .intValue();
            if (formatString.equals("x")) {
               result = "0x" + Integer.toHexString(value);
            } else if (formatString.equals("b")) {
               result = "0b" + Integer.toBinaryString(value);
            } else if (formatString.equals("t")) {
               // KvtLogger.error(KMessageService.class, getShortMessageID()
               // + " implementation missing for %?t");
               // TODO: implement %1t
            } else if (formatString.equals("")) {
               result = Integer.toString(value);
            } else {
               result = "...";
               // KvtLogger.warn(KMessageService.class, getShortMessageID()
               // + " invalid format specification" + digit);
            }
         } else

         if (paramValue instanceof Double || paramValue instanceof Float) {
            Number number = null;
            if (paramValue instanceof Float) {
               number = (parmNumber == 'n') ? new Float(instId)
                     : ((Float) paramValue);
            } else {
               number = (parmNumber == 'n') ? new Double(instId)
                     : (Double) paramValue;
            }

            if (formatString.startsWith("f")) {
               if (formatString.length() == 1) {
                  result = formatNumber(number, 2);
               } else {
                  int digits = Character.digit(formatString.charAt(2), 10);
                  result = formatNumber(number, digits);
               }
            } else if (formatString.equals("")) {
               result = formatNumber(number, 2);
            } else {
               result = "...";
               // KvtLogger.warn(KMessageService.class, getShortMessageID()
               // + " invalid format specification" + digit);
            }
         } else if (paramValue instanceof String) {
            result = paramValue.toString();
         } else {
            result = "...";
         }
         // case RepSysParam.MEMORY:
         // result = "...";
         // break;
         // default:
         // result = "...";
         // KvtLogger.warn(KMessageService.class, getShortMessageID() + "
         // has invalid param kind " + String.valueOf(paramKind));
         // }
      } else {
         result = "...";
      }
      // KvtLogger.warn(KMessageService.class, getShortMessageID() + " has
      // no parameter with index " + digit);
      return result;
   }

   private static String formatNumber(Number number, int postComma) {
      m_fmt.setGroupingUsed(false);
      m_fmt.setMinimumFractionDigits(postComma);
      m_fmt.setMaximumFractionDigits(postComma);
      m_fmt.setMaximumIntegerDigits(20);
      return m_fmt.format(number);
   }

   public static Object[] createParams(int cnt, TRepSysMsgParamUnion[] param,
         int[] bs) {
      Object[] result = new Object[cnt];
      Object value;
      TRepSysMsgParamUnion help;
      for (int i = 0; i < cnt; i++) {
         help = param[i];
         switch (help.mKind.value) {
         case TRepSysMsgParamKind.eRepSysFloatParam:
            value = new Float(help.mFltVal);
            break;
         case TRepSysMsgParamKind.eRepSysIntParam:
            value = new Integer(help.mIntVal);
            break;
         case TRepSysMsgParamKind.eRepSysMemoryParam:
            value = new Object();
            break;

         case TRepSysMsgParamKind.eRepSysStringParam:
            int offset = help.mIntVal;

            byte[] tmp = new byte[bs.length];
            for(int j = 0; j < bs.length; j++){
               tmp[j] = (byte)(bs[j] & 0xFF);
         }
            value = new String(tmp);
            int idx = ((String) value).indexOf((char) 0, offset);
            if (idx != -1) {
               value = ((String) value).substring(offset, idx);
            }
            break;
         case TRepSysMsgParamKind.eRepSysWStringParam:
            value = null;
            break;
         case TRepSysMsgParamKind.eRepSysUINT32Param:
            value = new Integer(help.mIntVal);
            value = null;
            break;
         case TRepSysMsgParamKind.eRepSysUINT64Param:
            value = new Long(help.mIntVal);
            break;
         case TRepSysMsgParamKind.eRepSysRealParam:
            value = new Double(help.mFltVal);
            break;
         default:
            value = null;
         }

         result[i] = value;
      }
      return result;
   }

}
