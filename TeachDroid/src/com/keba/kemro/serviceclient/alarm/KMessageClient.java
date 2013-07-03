package com.keba.kemro.serviceclient.alarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import android.util.Log;


public abstract class KMessageClient implements Runnable {
	protected static final String MSGFILE_PREFIX = "msgtext_";
	protected static final String MSGFILE_POSTFIX = ".csv";
	protected static final int RECONNECT_DELAY = 5000;
	protected static final int PRIORITY_DEFAULT = Thread.NORM_PRIORITY - 2;
	protected static final int PRIORITY_FIRE = Thread.NORM_PRIORITY;
	protected static final int CYCLE_TIME = 500; /* time in millis */
	protected static final NumberFormat m_fmt = NumberFormat.getNumberInstance();

	protected static int m_maxNrEntries = 170;
	protected static boolean m_useLocalMessageInfo;

	protected Locale m_actLanguage;
	protected boolean m_doPoll;
	protected Thread m_thread;
	protected final Vector m_buffers = new Vector(5);
	protected int checkUOSBufferCnt = 1;
	protected final int UOS_BUFFER_CHECK_INTERVALL = 7;
	protected String m_hostName;
	protected int m_connectionTimeOut;
	protected boolean m_messageFileLoaded;
	protected Locale loadedLanguage;
	protected final Hashtable messageToText = new Hashtable();
	protected Hashtable m_messageTemplates = new Hashtable();
	protected KMessageTextSource m_messageTextSource;
	protected int m_msgChgCnt = 0;
	protected int m_logChgCnt = 0;
	protected boolean m_activeInfoLog = false;
	private char[] convtBuf = new char[1024];

	static {
		m_maxNrEntries = 500;// Config.getIntProperty("MaxReportEntries", 500);
		m_useLocalMessageInfo = false;// Config.getBooleanProperty("MsgAccessLocal",
										// false);
	}

	protected KMessageClient(String hostName, int connectionTimeOut) {
		m_hostName = hostName;
		m_connectionTimeOut = connectionTimeOut;
	}

	protected abstract boolean connect();

	/**
	 * Verbindung abbauen
	 */
	protected abstract void disconnect();

	/**
	 * Close the connection.
	 */
	protected abstract void close();

	protected abstract void createdBuffers();

	/**
	 * Checks if messages have changed.
	 */
	protected abstract boolean checkMsgChange();

	protected void startPoll() {
		if (m_thread == null) {
			m_doPoll = true;
			m_thread = new Thread(this, "AlarmServiceClient");
			m_thread.setPriority(PRIORITY_DEFAULT);
			m_thread.start();
		}
	}

	protected void stopPoll() {
		m_doPoll = false;
		if (m_thread != null) {
			m_thread.interrupt();
		}
		m_thread = null;
	}

	/**
	 * @param bufferClassId
	 *            class id identifying message buffer
	 * @param reportsystem
	 *            true->reportsystem, false->infolog
	 * @return buffer name
	 */
	public KMessageBuffer getMessageBuffer(int bufferClassId, boolean reportsystem) {
		Enumeration e = m_buffers.elements();
		while (e.hasMoreElements()) {
			KMessageBuffer elem = (KMessageBuffer) e.nextElement();
			if (elem.getClassId() == bufferClassId) {
				if ((reportsystem && elem.isReportBuffer()) || (!reportsystem && !elem.isReportBuffer())) {
					return elem;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert alle angelegten Puffer
	 * 
	 * @return Vector mit allen Puffernamen
	 */
	public Vector getCreatedBufferNames() {
		Vector v = new Vector(m_buffers.size());
		for (int i = 0; i < m_buffers.size(); i++) {
			v.addElement(((KMessageBuffer) m_buffers.elementAt(i)).m_bufferName);
		}
		return v;
	}

	/**
	 * Liefert alle angelegten Puffer
	 * 
	 * @return Vector mit allen Puffernamen
	 */
	public Vector getCreatedBuffers() {
		return m_buffers;
	}

	/**
	 * tell control to quit a message
	 * 
	 * @param msg
	 *            message object
	 * @return true on success
	 */
	protected abstract boolean quitMessage(KMessage msg);

	/**
	 * delete all messages of given buffer
	 * 
	 * @param bufferName
	 *            buffer
	 * @return true on success
	 */

	protected boolean quitAllMessages(KMessageBuffer buffer) {
		boolean result = false; // stores if at least one quit message call
		// succeeded
		if ((buffer != null) && (buffer.getUpdateMode() != KMessageBuffer.STATE_QUIT_ALL)) {
			buffer.setUpdateMode(KMessageBuffer.STATE_QUIT_ALL);
			try {
				Enumeration e = buffer.getMessages().elements();
				Vector v = new Vector(); // needs to be copied, origin
				// data is changed during clear:
				// CR_0023478
				while (e.hasMoreElements()) {
					v.addElement(e.nextElement());
				}
				e = v.elements();
				while (e.hasMoreElements()) {
					KMessage elem = (KMessage) e.nextElement();
					result = quitMessage(elem) || result;
				}
			} catch (Exception e) {
				// KvtLogger.error(KMessageService.class,
				// "Error removing all messages: " + e.getMessage());
				Log.e("KMessageService", "Error removing all messages: " + e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Muß aufgerufen werden damit eine Sprachumschaltung erfolgt.
	 */
	protected void languageChanged() {
		m_actLanguage = Locale.getDefault();
		messageToText.clear();
		m_messageFileLoaded = false;
	//	m_messageTemplates.clear();
//		loadedLanguage = null;
	}

	public void activateInfoLogBuffers(boolean activate) {
		Vector buffers = getCreatedBuffers();
		m_activeInfoLog = activate;
		if (buffers != null) {
			for (int i = 0; i < buffers.size(); i++) {
				KMessageBuffer buffer = (KMessageBuffer) buffers.elementAt(i);
				if (!buffer.isReportBuffer()) {
					buffer.activate(activate);
				}
			}
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		int sleepTime = 1;
		boolean updateAll = false;
		while (m_doPoll) {
			try {
				if (m_buffers != null) {
					checkUOSBufferCnt++;
					updateAll = checkUOSBufferCnt > UOS_BUFFER_CHECK_INTERVALL;
					if (updateAll) {
						checkUOSBufferCnt = 1;
					}
					if (checkMsgChange() || updateAll) {
						for (int i = 0; i < m_buffers.size(); i++) {
							KMessageBuffer buf = (KMessageBuffer) m_buffers.elementAt(i);
							if (buf != null) {
								if (updateAll) {
									buf.setUpdateMode(KMessageBuffer.STATE_FORCE_REFRESH);
								}
								buf.doRefresh();
							}
						}
					}
				}
				sleepTime = CYCLE_TIME;
			} catch (Exception e) {
				sleepTime = RECONNECT_DELAY;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

	/**
	 * clear texts for message
	 * 
	 * @param kmsg
	 *            message object
	 */
	public void clearMessageText(KMessage kmsg) {
		messageToText.remove(kmsg.getUniqueKey() + kmsg.getTimeUSec());
	}

	protected abstract String getMessageTextFromControl(KMessage msg);
	
	
	protected abstract String getMessageTemplateFromControl(KMessage msg);

	/**
	 * Liefert den Übersetzungstext der KMessage
	 * 
	 * @param kmsg
	 *            MEssage
	 * 
	 * @return Übersetzungstext
	 */
	protected String getMessageText(KMessage msg) {
		String key = msg.getUniqueKey() + msg.getTimeUSec();
		String msgTxt = (String) messageToText.get(key);
		if (msgTxt == null) {
			msg.hasTranslationText = false;
			if (!m_useLocalMessageInfo) { // ask control for message
				msgTxt = getMessageTextFromControl(msg);
			} else { // do message translation ourselves
				msgTxt = buildMessageText(msg);
			}
			if (msgTxt != null) {
				messageToText.put(key, msgTxt);
			}
		}
		return msgTxt;
	}

	public void parseMessageTemplates(InputStream in, Hashtable target) {
		parseMessageTemplates(new InputStreamReader(in), target);
	}

	public void parseMessageTemplates(Reader in, Hashtable target) {
		try {
			BufferedReader reader = new BufferedReader(in);
			String line =reader.readLine();
			while (line != null) {
				int index = line.indexOf(";");
				if (index != -1) {
					int index2 = line.indexOf(";", index + 1);
					if (index2 != -1) {
						String compNum = line.substring(0, index);
						String msgNum = line.substring(index + 1, index2);
						String template = line.substring(index2 + 1).trim();
						if (template.startsWith("\"")) {
							template = template.substring(1).trim();
						}
						if (template.endsWith("\"")) {
							template = template.substring(0, template.length() - 1).trim();
						}
						target.put(compNum + "_" + msgNum, template);
					}
				}
				line = reader.readLine();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException io) {
//			KvtLogger.error(KMessageService.class, "Error parsing message file", io);
			Log.e("KMessageService", "Error parsing message file " + io.getMessage());
		}
	}

	public void setMessageTextSource(KMessageTextSource source) {
		m_messageTextSource = source;
		m_messageFileLoaded = false;
		m_useLocalMessageInfo = source != null;
	}

	private void loadMessageFile() {
		m_messageFileLoaded = true;
		if (Locale.getDefault().equals(loadedLanguage)){
			return;
		}
		loadedLanguage = Locale.getDefault();
		m_messageTemplates.clear();
		String currentLocale = Locale.getDefault().getLanguage();
		if (m_messageTextSource != null) {
			Enumeration e = m_messageTextSource.getLanguageTexts();
			/*String encoding = m_messageTextSource.getMessageEncoding();
			if (encoding != null) {
				try {
					while (e.hasMoreElements()) {
						parseMessageTemplates(new InputStreamReader((InputStream) e.nextElement(), encoding), m_messageTemplates);
					}
				} catch (Exception ex) {
					// nothing to do here
				}
			} else*/ {
				
				while (e.hasMoreElements()) {
					Object reader = e.nextElement();
					if (reader != null && reader instanceof Reader)
						parseMessageTemplates((Reader) reader, m_messageTemplates);
				}
				
			}
		} else {
			InputStream stream = ClassLoader.getSystemResourceAsStream(MSGFILE_PREFIX + currentLocale + MSGFILE_POSTFIX);
			if (stream == null) {
				stream = ClassLoader.getSystemResourceAsStream(MSGFILE_PREFIX + "en" + MSGFILE_POSTFIX);
				if (stream == null) {
					// KvtLogger.error(KMessageService.class,
					// "message file not found");
					Log.e("KMessageService", "message file not found!");
					return;
				}
			}
			parseMessageTemplates(stream, m_messageTemplates);
		}
	}

	protected String getMessageTemplate(KMessage msg) {
		if (!m_messageFileLoaded) {
			loadMessageFile();
		}
		return (String) m_messageTemplates.get(msg.getShortMessageID());
	}

	private String replaceParameter(char parmNumber, String formatString, KMessage msg) {
		String result = "";
		int digit = parmNumber == 'n' ? -1 : Character.digit(parmNumber, 10) - 1;
		formatString = formatString.trim();
		int nrOfParams = msg.getParamCount();
		if (nrOfParams > digit) {
			int paramKind = digit >= 0 ? msg.getParamKind(digit) : KMessage.PARAM_INT;
			Object paramValue = digit >= 0 ? msg.getParamValue(digit) : new Integer(msg.m_InstNr);

			switch (paramKind) {
			case KMessage.PARAM_UINT64: {
				long value = (parmNumber == 'n') ? msg.m_InstNr : ((Long) paramValue).longValue();
				if (formatString.equals("x")) {
					result = "0x" + Long.toHexString(value);
				} else if (formatString.equals("b")) {
					result = "0b" + Long.toBinaryString(value);
				} else if (formatString.equals("t")) {
//					KvtLogger.error(KMessageService.class, msg.getShortMessageID() + " implementation missing for %?t");
					Log.e("KMessageService", msg.getShortMessageID() + " implementation missing for %?t");
					// TODO: implement %1t
				} else if (formatString.equals("")) {
					result = Long.toString(value);
				} else {
					result = "...";
					// KvtLogger.warn(KMessageService.class,
					// msg.getShortMessageID() + " invalid format specification"
					// + digit);
					Log.w("KMessageService", msg.getShortMessageID() + " invalid format specification" + digit);
				}
			}
				break;
			case KMessage.PARAM_UINT32:
				// no break
			case KMessage.PARAM_INT: {
				int value = (parmNumber == 'n') ? msg.m_InstNr : ((Integer) paramValue).intValue();
				if (formatString.equals("x")) {
					result = "0x" + Integer.toHexString(value);
				} else if (formatString.equals("b")) {
					result = "0b" + Integer.toBinaryString(value);
				} else if (formatString.equals("t")) {
					// KvtLogger.error(KMessageService.class,
					// msg.getShortMessageID() +
					// " implementation missing for %?t");
					Log.e("KMessageService", msg.getShortMessageID() + " implementation missing for %?t");
					// TODO: implement %1t
				} else if (formatString.equals("")) {
					result = Integer.toString(value);
				} else {
					result = "...";
					// KvtLogger.warn(KMessageService.class,
					// msg.getShortMessageID() + " invalid format specification"
					// + digit);
					Log.w("KMessageService", msg.getShortMessageID() + " invalid format specification" + digit);
				}
			}
				break;

			case KMessage.PARAM_DOUBLE:
				// no break !
			case KMessage.PARAM_REAL: {
				Number number = null;
				if (paramKind == KMessage.PARAM_REAL) {
					number = (parmNumber == 'n') ? new Float(msg.m_InstNr) : ((Float) paramValue);
				} else {
					number = (parmNumber == 'n') ? new Double(msg.m_InstNr) : (Double) paramValue;
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
					// KvtLogger.warn(KMessageService.class,
					// msg.getShortMessageID() + " invalid format specification"
					// + digit);
					Log.w("KMessageService", msg.getShortMessageID() + " invalid format specification" + digit);
				}
			}
				break;
			case KMessage.PARAM_WSTRING:
				// no break
			case KMessage.PARAM_STRING:
				result = paramValue.toString();
				break;
			case KMessage.PARAM_MEMORY:
				result = "...";
				break;
			default:
				result = "...";
				// KvtLogger.warn(KMessageService.class, msg.getShortMessageID()
				// + " has invalid param kind " + String.valueOf(paramKind));
				Log.w("KMessageService", msg.getShortMessageID() + " has invalid param kind " + String.valueOf(paramKind));
			}
		} else {
			result = "...";
		}
		// KvtLogger.warn(KMessageService.class, msg.getShortMessageID() +
		// " has no parameter with index " + digit);
		Log.w("KMessageService", msg.getShortMessageID() + " has no parameter with index " + digit);
		if (m_messageTextSource != null) {
			return m_messageTextSource.translateParameter(result.trim());
		}
		return result.trim();
	}

	private String extractFormat(String txt, int startPos) {
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

	/**
	 * gets a KMessage and gives a message text with the parameters filled in
	 * 
	 * @param msg
	 *            message for which the text is requested
	 * @return message string with correctly formatted parameters
	 */
	private String buildMessageText(KMessage msg) {
		String txt = getMessageTemplate(msg);
		if ((txt == null) && (m_messageTextSource != null)) { // CR_0031137 if
			// not defined
			// return
			// default from
			// control
			txt = getMessageTextFromControl(msg);
			if (txt != null) {
				return txt;
			}
		}
		if (txt == null) {
			txt = "... %n - %1, %2, %3, %4, %5, %6, %7, %8";
		}
		int pos = 0;
		StringBuffer result = new StringBuffer();
		while (pos < txt.length()) {
			char ch = txt.charAt(pos);
			if (ch == '%') { // first % sign --> a parameters specification
				// may follow
				char lookahead = (pos + 1 < txt.length()) ? txt.charAt(pos + 1) : ' ';
				String format = "";
				if (lookahead == '%') { // we want to output the percent sign
					// (=%%)
					pos++;
					result.append(ch);
				} else if (Character.isDigit(lookahead)) { // %1?, %2?, ...,
					// %8?
					format = extractFormat(txt, pos + 2);
					result.append(replaceParameter(lookahead, format, msg));
					pos += format.length() + 1;
				} else if (lookahead == 'n') { // %n?
					format = extractFormat(txt, pos + 2);
					result.append(replaceParameter(lookahead, format, msg));
					pos += format.length() + 1;
				} else { // error
					result.append("..."); // control uses this to specify
					// untranslateable elements
				}
			} else {
				result.append(ch);
			}
			pos++;
		}
		msg.setHasTranslationText(true);
		return loadConvert(result.toString().toCharArray());
	}

	private char tryForUnicode(char[] in, int off) {
		if (off < in.length) {
			char aChar = in[off++];
			if (aChar == 'u') {
				// Read the xxxx
				int value = 0;
				for (int i = 0; i < 4; i++) {
					if ((off) >= in.length) {
						return '\\';
					}
					aChar = in[off++];
					switch (aChar) {
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						value = (value << 4) + aChar - '0';
						break;
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
						value = (value << 4) + 10 + aChar - 'a';
						break;
					case 'A':
					case 'B':
					case 'C':
					case 'D':
					case 'E':
					case 'F':
						value = (value << 4) + 10 + aChar - 'A';
						break;
					default:
						return '\\';
					}
				}
				return (char) value;
			}
		}
		return '\\';
	}

	private String loadConvert(char[] in) {
		int len = in.length;
		int off = 0;
		if (convtBuf.length < len) {
			int newLen = len * 2;
			if (newLen < 0) {
				newLen = Integer.MAX_VALUE;
			}
			convtBuf = new char[newLen];
		}
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;

		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = tryForUnicode(in, off);
				out[outLen++] = aChar;
				if (aChar != '\\') {
					off += 5;
				}

			} else {
				out[outLen++] = aChar;
			}
		}
		return (new String(out, 0, outLen)).trim();
	}

	private String formatNumber(Number number, int postComma) {
		m_fmt.setGroupingUsed(false);
		m_fmt.setMinimumFractionDigits(postComma);
		m_fmt.setMaximumFractionDigits(postComma);
		m_fmt.setMaximumIntegerDigits(20);
		return m_fmt.format(number);
	}

}
