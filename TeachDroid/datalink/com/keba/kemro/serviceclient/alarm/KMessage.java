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


/**
 */
public abstract class KMessage {
	public static final int STATE_UNDEF = 0;
	public static final int STATE_QUIT = 1;
	public static final int STATE_RESET = 2;
	public static final int STATE_QUIT_AND_RESET = 4;
	public static final int STATE_QUIT_OR_RESET = 8;
	public static final int STATE_TO_QUIT = 16;
	public static final int STATE_TO_RESET = 32;
	public static final int STATE_TO_QUIT_AND_RESET = 64;
	public static final int STATE_IS_QUIT = 128;

	public static final int PARAM_INT = 0;
	public static final int PARAM_REAL = 1;
	public static final int PARAM_STRING = 2;
	public static final int PARAM_MEMORY = 3;
	public static final int PARAM_WSTRING = 4;
	public static final int PARAM_UINT32 = 5;
	public static final int PARAM_UINT64 = 7;
	public static final int PARAM_DOUBLE = 6;

	/**
	 * Enthält das Message element dass alle Informationen über den Alarm
	 * enthält.
	 */
	public final int m_ClassId;
	public final int m_CompNr;
	public final int m_InstNr;
	public final int m_Nr;
	private final int m_hashCode;

	protected KMessageBuffer m_buffer;
	protected boolean hasTranslationText = false;

	public KMessageBuffer getBuffer() {
		return m_buffer;
	}

	public String getCSVEntry() {
		try {
			if (hasTranslationText()){
				return m_CompNr + ";" + m_Nr + ";\"" + m_buffer.m_service.getMessageTemplate(this) + "\"";
			} else {
				return m_CompNr + ";" + m_Nr + ";\"" + m_buffer.m_service.getMessageTemplateFromControl(this) + "\"";
			} 
		} catch (Exception e) {
			// nothing to do
		}
		return m_CompNr + ";" + m_Nr + ";\"\"";
	}

	public boolean hasTranslationText() {
		getMessageText();
		return hasTranslationText;
	}

	public void setHasTranslationText(boolean hasText) {
		hasTranslationText = hasText;
	}

	/**
	 * Construktor des KAlarm Objectes.
	 * 
	 * @param alarmMessage
	 *            Message
	 * @param buffer
	 *            Puffer
	 */
	protected KMessage(KMessageBuffer buffer, int classId, int compNr, int instNr, int nr) {
		m_buffer = buffer;
		m_ClassId = classId;
		m_CompNr = compNr;
		m_InstNr = instNr;
		m_Nr = nr;
		m_hashCode = ((m_InstNr & 0x3f) << 26) | ((m_CompNr & 0x3fff) << 12) | ((m_Nr & 0xfff));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof KMessage)) {
			return false;
		}
		return (m_CompNr == ((KMessage) obj).m_CompNr) && (m_ClassId == ((KMessage) obj).m_ClassId) && (m_InstNr == ((KMessage) obj).m_InstNr) && (m_Nr == ((KMessage) obj).m_Nr);
	}

	public abstract int getMsgIndex();

	// public MsgId getMsgID() {
	// return null;
	// }

	@Override
	public int hashCode() {
		return m_hashCode;
	}

	/**
	 * Liefert die Anzahl der Parameter zurück.
	 * 
	 * @return Anzahl der Parameter
	 */
	public abstract int getParamCount();

	/**
	 * Method getAlarmText liefert den Ubersetzungstext in der aktuellen
	 * Sprache. Sprachumschaltung wird berücksichtigt.
	 * 
	 * @return String
	 */
	public String getMessageText() {
		return m_buffer.m_service.getMessageText(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getMessageText();
	}

	/**
	 * Liefert die Message Id des Alarms
	 * 
	 * @return MessageID
	 */
	public String getShortMessageID() {
		return getPropertyKey();
	}

	public String getPropertyKey() {
		return String.valueOf(m_CompNr) + "_" + String.valueOf(m_Nr);
	}

	public String getMessageID() {
		return String.valueOf(m_CompNr) + "_" + String.valueOf(m_Nr) + "_" + String.valueOf((m_InstNr) & 0xFFFFFFFFL);
	}

	public String getUniqueKey() {
		StringBuffer buf = new StringBuffer();
		buf.append(String.valueOf(m_ClassId));
		buf.append("_");
		buf.append(String.valueOf(m_CompNr));
		buf.append("_");
		buf.append(String.valueOf(m_InstNr & 0xFFFFFFFFL));
		buf.append("_");
		buf.append(String.valueOf(m_Nr));
		return buf.toString();
	}

	protected boolean isVisibleAlarm() {
		return isQuittableAlarm() || (getState() & STATE_RESET) != 0;
	}

	protected boolean isQuittableAlarm() {
		return (getState() & (STATE_QUIT | STATE_QUIT_AND_RESET | STATE_QUIT_OR_RESET)) != 0;
	}

	/**
	 * Wenn true dann ist ein Confirm der Message erlaubt. Liefert immer True
	 * 
	 * @return true dann ist ein Confirm der Message erlaubt
	 */

	public boolean confirmAllowed() {
		return isQuittableAlarm();
	}

	public long getSortInfo() {
		return getTimeUSec();
	}

	public boolean quitMessage() {
		if (m_buffer.m_service.quitMessage(this)) {
			m_buffer.setUpdateMode(KMessageBuffer.STATE_QUIT_ALL);
			return true;
		}
		return false;
	}

	public abstract int getParamKind(int idx);

	public abstract Object getParamValue(int idx);

	public abstract int getState();

	public abstract long getTimeMSec();

	protected abstract long getTimeUSec();

	protected abstract boolean update(KMessage msg);

}
