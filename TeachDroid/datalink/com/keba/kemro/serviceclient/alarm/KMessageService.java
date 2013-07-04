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
package com.keba.kemro.serviceclient.alarm;

import java.util.Vector;

import android.util.Log;

import com.keba.kemro.serviceclient.alarm.sysrpc.KSysRpcMessageClient;

/**
 * Sorgt f¸r die Verbindung zu den Alarm Puffern im Laufzeitsystem.
 * 
 * @author ede
 */
public class KMessageService {
	public static String CONNECTION_TYPE_PROP = "MessageConnectionType";
	public static final String DETECT_TYPE = "detect";
	public static final String RPC_TYPE = "rpc";
	public static final String SYSRPC_TYPE = "sysrpc";
	public static final String KLINK_TYPE = "klink";

	protected static KSysRpcMessageClient	m_client;
	protected static boolean m_isConnected = false;
	protected static final Vector m_connectionListeners = new Vector(5);
	protected static KMessageTextSource m_messageTextSource;

	/**
	 * F¸gt einen Listener f¸r die Alarmverbindung hinzu
	 * 
	 * @param listener
	 *            Listener
	 */
	public static void addConnectionListener(KAlarmConnectionListener listener) {
		if (!m_connectionListeners.contains(listener)) {
			m_connectionListeners.addElement(listener);
			if (m_isConnected) {
				listener.connected();
			}
		}
	}

	/**
	 * Entfernt den Listener f¸r Alarmverbindungen
	 * 
	 * @param listener
	 *            Listener
	 */
	public static void removeConnectionListener(KAlarmConnectionListener listener) {
		m_connectionListeners.removeElement(listener);
	}

	/**
	 * Activate Infolog Buffers (default deactivated)
	 * @param activate
	 */

	public static void activateInfoLogBuffers(boolean activate) {
		if (m_client != null) {
			m_client.activateInfoLogBuffers(activate);
		}
	}
	
	
	
	/**
	 * @param bufferClassId
	 *            class id identifying message buffer
	 * @param reportsystem
	 *            true->reportsystem, false->infolog
	 * @return buffer name
	 */
	public static KMessageBuffer getMessageBuffer(int bufferClassId, boolean reportsystem) {
		KMessageClient c = m_client;
		if (c != null) {
			return c.getMessageBuffer(bufferClassId, reportsystem);
		}
		return null;
	}


	/**
	 * Liefert alle angelegten Puffer
	 * 
	 * @return Vector mit allen Puffernamen
	 */
	public static Vector getCreatedBufferNames() {
		KMessageClient c = m_client;
		if (c != null) {
			return c.getCreatedBufferNames();
		}
		return null;
	}

	/**
	 * Liefert alle angelegten Puffer
	 * 
	 * @return Vector mit allen Puffernamen
	 */
	public static Vector getCreatedBuffers() {
		KMessageClient c = m_client;
		if (c != null) {
			return c.getCreatedBuffers();
		}
		return null;
	}

	/**
	 * Muﬂ aufgerufen werden damit eine Sprachumschaltung erfolgt.
	 */
	public static void languageChanged() {
		KMessageClient c = m_client;
		if (c != null) {
			c.languageChanged();
		}
//		m_actLanguage = Locale.getDefault();
//		messageToText.clear();
//		m_messageFileLoaded = false;
//		m_messageTemplates.clear();
//		KMessageService s = m_service;
//		if ((m_messageTextSource  == null) && (s != null)){  // Write to catalog only if not handled in vis (CR_0031137)
//			s.clientLanguageChanged();
//		}
	}
	/**
	 * Verbindungsaufbau
	 * 
	 * @param hostName
	 *            Name des Hosts
	 * @param connectionTimeOut
	 *            Timeout bei Verbindungsabbruch
	 */
	public static void connect (String hostName, int connectionTimeOut) {
		languageChanged();

		String connectionType = System.getProperty(CONNECTION_TYPE_PROP);
		// if (KLINK_TYPE.equalsIgnoreCase(connectionType)) {
		// m_client = new KKlinkMessageClient(hostName, connectionTimeOut){};
		// } else if (RPC_TYPE.equalsIgnoreCase(connectionType)) {
		// m_client = new KRpcMessageClient(hostName, connectionTimeOut) {};
		// } else
		// if (SYSRPC_TYPE.equalsIgnoreCase(connectionType)) {
			m_client = new KSysRpcMessageClient(hostName, connectionTimeOut) {};
			// } else {
			// m_client = new KRpcMessageClient(hostName, connectionTimeOut) {};
			// if (m_client == null) {
			// m_client = new KSysRpcMessageClient(hostName, connectionTimeOut)
			// {};
			// }
			// if (m_client == null) {
			// // try to create a KLink - client
			// m_client = new KKlinkMessageClient(hostName, connectionTimeOut)
			// {};
			// }
		// }
		if (m_client != null) {
			if (m_client.connect()) {
				m_isConnected = true;
				m_client.setMessageTextSource(m_messageTextSource);
				m_client.createdBuffers();
				fireConnectionEvent();
				m_client.startPoll();
			} else {
				m_client = null;
			}
			
		}
	}

	public static void disconnect() {
		if (m_client != null) {
			m_client.stopPoll();
			m_client.disconnect();
			m_client = null;
			fireDisconnectionEvent();
		}
	}

	/**
	 * Close the connection.
	 */
	public static void close() {
		if (m_client != null) {
			m_client.stopPoll();
			m_client.close();
			m_client = null;
			fireDisconnectionEvent();
		}
	}
	

	/**
	 * Liefert true wenn Verbindung besteht
	 * 
	 * @return true wenn Verbindung besteht
	 */
	public static boolean isConnected() {
		return m_isConnected;
	}


	protected static void fireConnectionEvent() {
		synchronized (m_connectionListeners) {
			int size = m_connectionListeners.size();
			for (int i = 0; i < size; i++) {
				try {
					((KAlarmConnectionListener) m_connectionListeners.elementAt(i)).connected();
				} catch (Exception ex) {
					// KAlarmLogger.debug(KMessageService.class, ex);
					Log.d("KMEssageService", ex.getMessage());
				}
			}
		}
	}

	protected static void fireDisconnectionEvent() {
		synchronized (m_connectionListeners) {
			int size = m_connectionListeners.size();
			for (int i = 0; i < size; i++) {
				try {
					((KAlarmConnectionListener) m_connectionListeners.elementAt(i)).disconnected();
				} catch (Exception ex) {
					// KAlarmLogger.debug(KMessageService.class, ex);
					Log.d("KMEssageService", ex.getMessage());
				}
			}
		}
	}

	/**
	 * clear texts for message
	 * 
	 * @param kmsg
	 *            message object
	 */
	public static void clearMessageText(KMessage kmsg) {
		KMessageClient c = m_client;
		if (c != null) {
			c.clearMessageText(kmsg);
		}
	}
//	
//	protected abstract String getMessageTextFromControl(KMessage kmsg);
//
//	/**
//	 * Liefert den ‹bersetzungstext der KMessage
//	 * 
//	 * @param kmsg
//	 *            MEssage
//	 * 
//	 * @return ‹bersetzungstext
//	 */
//	protected static String getMessageText(KMessage kmsg) {
//		String msgTxt = null;
//		KMessageService s = m_service;
//		if (s != null) {
//			String key = kmsg.getUniqueKey() + kmsg.getTimeMs();
//			msgTxt = (String) messageToText.get(key);
//			if (msgTxt == null) {
//				if (!m_useLocalMessageInfo) { // ask control for message
//					msgTxt = s.getMessageTextFromControl(kmsg);
//				} else { // do message translation ourselves
//					msgTxt = buildMessageText(kmsg);
//				}
//				if (msgTxt != null) {
//					messageToText.put(kmsg.getUniqueKey() + kmsg.getTimeMs(), msgTxt);
//				}
//			}
//		}
//		return msgTxt;
//	}
//
//	private static void parseMessageTemplates(InputStreamReader in, Hashtable target) {
//		try {
//			BufferedReader reader = new BufferedReader(in);
//			String line = reader.readLine();
//			while (line != null) {
//				int index = line.indexOf(";");
//				if (index != -1) {
//					int index2 = line.indexOf(";", index + 1);
//					if (index2 != -1) {
//						String compNum = line.substring(0, index);
//						String msgNum = line.substring(index + 1, index2);
//						String template = line.substring(index2 + 1).trim();
//
//						target.put(compNum + "_" + msgNum, template);
//					}
//				}
//				line = reader.readLine();
//			}
//			if (reader != null){
//				reader.close();
//			}
//		} catch (IOException io) {
//			KvtLogger.error(KMessageService.class, "Error parsing message file", io);
//		}
//	}

	public static void setMessageTextSource(KMessageTextSource source) {
		m_messageTextSource = source;
		KMessageClient c = m_client;
		if (c != null) {
			c.setMessageTextSource(m_messageTextSource);
		}
	}

//	private static void loadMessageFile() {
//		m_messageFileLoaded = true;
//		m_messageTemplates.clear();
//		String currentLocale = Locale.getDefault().getLanguage();
//		if (m_messageTextSource != null) {
//			m_messageTextSource.getLanguageText(m_messageTemplates);
//		} else {
//			InputStream stream = ClassLoader.getSystemResourceAsStream(MSGFILE_PREFIX + currentLocale + MSGFILE_POSTFIX);
//			if (stream == null) {
//				stream = ClassLoader.getSystemResourceAsStream(MSGFILE_PREFIX + "en" + MSGFILE_POSTFIX);
//				if (stream == null) {
//					KvtLogger.error(KMessageService.class, "message file not found");
//					return;
//				}
//			}
//			parseMessageTemplates(stream, m_messageTemplates);
//		}
//	}
//
//	private static String getMessageTemplate(KMessage msg) {
//		if (!m_messageFileLoaded) {
//			loadMessageFile();
//		}
//		return (String) m_messageTemplates.get(msg.getShortMessageID());
//	}
//
//	private static String replaceParameter(char parmNumber, String formatString, KMessage msg) {
//		String result = "";
//		int digit = parmNumber == 'n' ? -1 : Character.digit(parmNumber, 10) - 1;
//		formatString = formatString.trim();
//		int nrOfParams = msg.getParamCount();
//		if (nrOfParams > digit) {
//			int paramKind = digit >= 0 ? msg.getParamKind(digit) : KMessage.PARAM_INT;
//			Object paramValue = digit >= 0 ? msg.getParamValue(digit) : new Integer(msg.m_InstNr);
//
//			switch (paramKind) {
//			case KMessage.PARAM_UINT64: {
//				long value = (parmNumber == 'n') ? msg.m_InstNr : ((Long) paramValue).longValue();
//				if (formatString.equals("x")) {
//					result = "0x" + Long.toHexString(value);
//				} else if (formatString.equals("b")) {
//					result = "0b" + Long.toBinaryString(value);
//				} else if (formatString.equals("t")) {
//					KvtLogger.error(KMessageService.class, msg.getShortMessageID() + " implementation missing for %?t");
//					// TODO: implement %1t
//				} else if (formatString.equals("")) {
//					result = Long.toString(value);
//				} else {
//					result = "...";
//					KvtLogger.warn(KMessageService.class, msg.getShortMessageID() + " invalid format specification" + digit);
//				}
//			}
//				break;
//			case KMessage.PARAM_UINT32:
//				// no break
//			case KMessage.PARAM_INT: {
//				int value = (parmNumber == 'n') ? msg.m_InstNr : ((Integer) paramValue).intValue();
//				if (formatString.equals("x")) {
//					result = "0x" + Integer.toHexString(value);
//				} else if (formatString.equals("b")) {
//					result = "0b" + Integer.toBinaryString(value);
//				} else if (formatString.equals("t")) {
//					KvtLogger.error(KMessageService.class, msg.getShortMessageID() + " implementation missing for %?t");
//					// TODO: implement %1t
//				} else if (formatString.equals("")) {
//					result = Integer.toString(value);
//				} else {
//					result = "...";
//					KvtLogger.warn(KMessageService.class, msg.getShortMessageID() + " invalid format specification" + digit);
//				}
//			}
//				break;
//
//			case KMessage.PARAM_DOUBLE:
//				// no break !
//			case KMessage.PARAM_REAL: {
//				Number number = null;
//				if (paramKind == KMessage.PARAM_REAL) {
//					number = (parmNumber == 'n') ? new Float(msg.m_InstNr) : ((Float) paramValue);
//				} else {
//					number = (parmNumber == 'n') ? new Double(msg.m_InstNr) : (Double) paramValue;
//				}
//
//				if (formatString.startsWith("f")) {
//					if (formatString.length() == 1) {
//						result = formatNumber(number, 2);
//					} else {
//						int digits = Character.digit(formatString.charAt(2), 10);
//						result = formatNumber(number, digits);
//					}
//				} else if (formatString.equals("")) {
//					result = formatNumber(number, 2);
//				} else {
//					result = "...";
//					KvtLogger.warn(KMessageService.class, msg.getShortMessageID() + " invalid format specification" + digit);
//				}
//			}
//				break;
//			case KMessage.PARAM_WSTRING:
//				// no break
//			case KMessage.PARAM_STRING:
//				result = paramValue.toString();
//				break;
//			case KMessage.PARAM_MEMORY:
//				result = "...";
//				break;
//			default:
//				result = "...";
//				KvtLogger.warn(KMessageService.class, msg.getShortMessageID() + " has invalid param kind " + String.valueOf(paramKind));
//			}
//		} else {
//			result = "...";
//		}
//		KvtLogger.warn(KMessageService.class, msg.getShortMessageID() + " has no parameter with index " + digit);
//		return result;
//	}
//
//	private static String extractFormat(String txt, int startPos) {
//		char ch1 = txt.length() > startPos ? txt.charAt(startPos) : ' ';
//		char ch2 = txt.length() > startPos + 1 ? txt.charAt(startPos + 1) : ' ';
//		String result = "";
//
//		switch (ch1) {
//		case 't':
//			// no break
//		case 'b':
//			// no break
//		case 'x':
//			result += ch1;
//			break;
//		case 'f':
//			result += ch1;
//			if (Character.isDigit(ch2)) {
//				result += ch2;
//			}
//			break;
//		}
//		return result;
//	}
//
//	/**
//	 * gets a KMessage and gives a message text with the parameters filled in
//	 * 
//	 * @param msg
//	 *            message for which the text is requested
//	 * @return message string with correctly formatted parameters
//	 */
//	private static String buildMessageText(KMessage msg) {
//		KMessageService s = m_service;
//		if (s != null) {
//		String txt = getMessageTemplate(msg);
//		if ((txt == null) &&  (m_messageTextSource  != null)){  // CR_0031137 if not defined return default from control
//			 txt = s.getMessageTextFromControl(msg);
//			 if (txt != null){
//				 return txt;
//			 }
//		}
//		if (txt == null) {
//		   txt = "... %n - %1, %2, %3, %4, %5, %6, %7, %8";
//		}
//		int pos = 0;
//		StringBuffer result = new StringBuffer();
//		while (pos < txt.length()) {
//			char ch = txt.charAt(pos);
//			if (ch == '%') { // first % sign --> a parameters specification
//								// may follow
//				char lookahead = (pos + 1 < txt.length()) ? txt.charAt(pos + 1) : ' ';
//				String format = "";
//				if (lookahead == '%') { // we want to output the percent sign
//										// (=%%)
//					pos++;
//					result.append(ch);
//				} else if (Character.isDigit(lookahead)) { // %1?, %2?, ...,
//															// %8?
//					format = extractFormat(txt, pos + 2);
//					result.append(replaceParameter(lookahead, format, msg));
//					pos += format.length() + 1;
//				} else if (lookahead == 'n') { // %n?
//					format = extractFormat(txt, pos + 2);
//					result.append(replaceParameter(lookahead, format, msg));
//					pos += format.length() + 1;
//				} else { // error
//					result.append("..."); // control uses this to specify
//											// untranslateable elements
//				}
//			} else {
//				result.append(ch);
//			}
//			pos++;
//		}
//		return result.toString().trim();
//		}
//		return null;
//	}
//
//	private static String formatNumber(Number number, int postComma) {
//		m_fmt.setGroupingUsed(false);
//		m_fmt.setMinimumFractionDigits(postComma);
//		m_fmt.setMaximumFractionDigits(postComma);
//		m_fmt.setMaximumIntegerDigits(20);
//		return m_fmt.format(number);
//	}

}
