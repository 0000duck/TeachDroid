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

import java.util.Enumeration;
import java.util.Vector;

import android.util.Log;

/**
 * KAlarmBuffer.java: Verwaltung einer Verbindung zu einem Puffer im UOS
 * 
 * @author ede
 */
public abstract class KMessageBuffer {
	protected SortVector m_messages = new SortVector();
	protected KMessageClient m_service;
	protected int m_classId = -1;
	protected long m_lastErrorTime = 0;
	protected String m_bufferName;
	protected Vector m_listeners = new Vector(3);
	protected boolean m_activated;
	protected boolean m_isReportBuffer;
	protected int m_lastChangeCounter = -2;
	protected int m_maxNrEntries;
	protected int m_updateMode;
	protected static final int STATE_DEFAULT = 0;
	protected static final int STATE_FORCE_REFRESH = 1;
	protected static final int STATE_QUIT_ALL = 2;
	private Vector changedMessages = new Vector();
	private Vector addedMessages = new Vector();
	private Vector removedMessages = new Vector();
	private Vector temp = new Vector();

	public boolean isReportBuffer() {
		return m_isReportBuffer;
	}

	public int getClassId() {
		return m_classId;
	}

	public String getBufferName() {
		return m_bufferName;
	}

	public Vector getMessages() {
		return m_messages;
	}

	/**
	 * Legt einen neuen Puffer an
	 * 
	 * @param bufferName
	 *            Name des Puffers
	 * @param activate
	 *            Aktivieren/deaktivieren
	 */
	protected KMessageBuffer(KMessageClient service, String bufferName,
			int classId, boolean activate,
			boolean isReportBuffer, int maxNrEntries) {
		m_service = service;
		m_bufferName = bufferName;
		m_classId = classId;
		m_maxNrEntries = maxNrEntries;
		m_isReportBuffer = isReportBuffer;
	 	activate(activate && m_isReportBuffer);
	}

	/**
	 * Aktiviert / Deaktiviert Puffer
	 * 
	 * @param activate
	 */
	protected void activate(boolean activate) {
		m_activated = activate;
	}

	/**
	 * Fügt einen Changelistener hinzu
	 * 
	 * @param listener
	 *           Listener
	 */
	public void addChangeListener(KMessageChangeListener listener) {
		if (!m_listeners.contains(listener)) {
			if (m_messages.size() > 0) {
				listener.messagesChanged(m_bufferName, m_messages,
						new Vector(), new Vector());
			}
			m_listeners.addElement(listener);
		}
	}

	/**
	 * Entfernt einen Changelistener
	 * 
	 * @param listener
	 *            Listener
	 */
	public void removeChangeListener(KMessageChangeListener listener) {
		m_listeners.removeElement(listener);
	}

	/**
	 * Erzwingt ein update beim nächsten Aufruf
	 */
	protected void setUpdateMode(int updateMode) {
	    m_updateMode = updateMode;
	}
	
	protected int getUpdateMode(){
		return m_updateMode;
	}

	protected void fireAlarmChanged(Vector newMsgs, Vector remMsgs,
			Vector chMsgs) {
		int priority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(KMessageClient.PRIORITY_FIRE);
		synchronized (m_listeners) {
			int size = m_listeners.size();
			for (int i = 0; i < size; i++) {
				try {
					((KMessageChangeListener) m_listeners.elementAt(i))
							.messagesChanged(m_bufferName, newMsgs, remMsgs,
									chMsgs);
				} catch (Exception ex) {
					// KAlarmLogger.debug(getClass(), ex);
					Log.d("KMEssageBuffer", ex.getMessage());
				}
			}
		}
		Thread.currentThread().setPriority(priority);
	}

	protected void fireAllAlarmRemoved() {
		synchronized (m_listeners) {
			int size = m_listeners.size();
			for (int i = 0; i < size; i++) {
				try {
					((KMessageChangeListener) m_listeners.elementAt(i))
							.allMessagesRemoved(m_bufferName);
				} catch (Exception ex) {
					// KAlarmLogger.debug(getClass(), ex);
					Log.d("KMEssageBuffer", ex.getMessage());
				}
			}
		}
	}

	/**
	 * Löschen aller Pufferdaten
	 */
	protected void clear() {
		m_lastErrorTime = 0;
	//	m_messages.removeAllElements();
		fireAllAlarmRemoved();
	}

	public boolean quitAllMessages() {
		return m_service.quitAllMessages(this);
	}

	protected abstract KMessage getNextMessage();
	
	protected abstract int getLastMessageId();

	protected abstract void resetIteration();
	
	protected boolean checkAll(){
		return (m_updateMode > STATE_DEFAULT);
	}
	
	protected void doRefresh() {
		try {
			if (isReportBuffer() && checkAll()) {
				resetIteration();
			}
			if (m_activated) {
				changedMessages.removeAllElements();
				addedMessages.removeAllElements();
				removedMessages.removeAllElements();
				temp.removeAllElements();
				int lastMessage = getLastMessageId();
				KMessage alarmMessage = getNextMessage();
				while (alarmMessage != null){
   					if (!isReportBuffer() || alarmMessage.isVisibleAlarm()) {
						temp.addElement(alarmMessage);
					}
   					if ((lastMessage <= 0) || (alarmMessage.getMsgIndex() != lastMessage)){
					   alarmMessage = getNextMessage();
   					} else {
   						alarmMessage = null;
   					}
				}
				if (isReportBuffer()) {
					// check for new messages and update existing ones
					Enumeration currentMessages = temp.elements();
					while (currentMessages.hasMoreElements()) {
						KMessage currentMessage = (KMessage) currentMessages
								.nextElement();
						boolean found = false;

						Enumeration storedMessages = m_messages.elements();
						while (storedMessages.hasMoreElements()) {
							KMessage storedMessage = (KMessage) storedMessages
									.nextElement();
							if (currentMessage.equals(storedMessage)) {
								found = true;
								m_service.clearMessageText(storedMessage);
								if (storedMessage.update(currentMessage)) {
									changedMessages.addElement(storedMessage);
								}
							}
						}
						if (!found) {
							addedMessages.addElement(currentMessage);
						}
					}
					Enumeration ae = addedMessages.elements();
					while (ae.hasMoreElements()) {
						KMessage elem = (KMessage) ae.nextElement();
						m_messages.addElement(elem);
					}
					if (checkAll()) {
						// check for removed messages
						Enumeration storedMessages = m_messages.elements();
						while (storedMessages.hasMoreElements()) {
							KMessage storedMessage = (KMessage) storedMessages
									.nextElement();
							boolean found = false;

							currentMessages = temp.elements();
							while (currentMessages.hasMoreElements()) {
								KMessage currentMessage = (KMessage) currentMessages
										.nextElement();
								if (currentMessage.equals(storedMessage)) {
									found = true;
								}
							}
							if (!found) {
								removedMessages.addElement(storedMessage);
							}
						}
						Enumeration re = removedMessages.elements();
						while (re.hasMoreElements()) {
							KMessage elem = (KMessage) re.nextElement();
							KMessageService.clearMessageText(elem);
							m_messages.removeElement(elem);
						}
					}
				} else {
					addedMessages = temp;
					Enumeration e = addedMessages.elements();

					while (e.hasMoreElements()) {
						Object elem = e.nextElement();
						m_messages.addElement(elem);
					}
				}

				if (m_maxNrEntries > 0) {
					m_messages.sort(); // sort only when m_maxNrEntries
					// reached
					if (isReportBuffer()) {
						while (m_messages.size() > m_maxNrEntries) {
							KMessage msg = (KMessage) m_messages.lastElement();
							if (addedMessages.contains(msg)) {
								addedMessages.removeElement(msg);
								KMessageService.clearMessageText(msg);
								m_messages.removeElement(msg);
							} else if (changedMessages.contains(msg)) {
								changedMessages.removeElement(msg);
								KMessageService.clearMessageText(msg);
								m_messages.removeElement(msg);
							} else if (removedMessages.contains(msg)) {
								KMessageService.clearMessageText(msg);
								m_messages.removeElement(msg);
							} else {
								removedMessages.addElement(msg);
								KMessageService.clearMessageText(msg);
								m_messages.removeElement(msg);
							}
						}
					} else {
						// Info Log: only m_maxNrEntries message are kept
						// a message can occur several times in a vector
						// with a different time stamp
						while (m_messages.size() > m_maxNrEntries) {
							KMessage msg = (KMessage) m_messages.lastElement();
							int i = addedMessages.size() - 1;
							while (0 <= i) {
								KMessage addMsg = (KMessage) addedMessages
										.elementAt(i);
								if (addMsg.equals(msg)
										&& addMsg.getTimeMSec() == msg
												.getTimeMSec()) {
									// no more entry for the new message,
									// remove it
									addedMessages.removeElementAt(i);
									break;
								}
								i--;
							}
							if (-1 == i) {
								removedMessages.addElement(msg);
							}
							m_messages.removeElementAt(m_messages.size() - 1);
							KMessageService.clearMessageText(msg);
						}
					}
				}

				if (removedMessages.size() > 0 || addedMessages.size() > 0
						|| changedMessages.size() > 0) {
					fireAlarmChanged(addedMessages, removedMessages,
							changedMessages);
				}
				m_updateMode = STATE_DEFAULT;
			}
		} catch (Exception e) {
		}
	}

	protected static class SortVector extends Vector {
		public void sort() {
			int r;
			int gap;
			int counter;
			int pointer;
			boolean less;
			Object temp;
			r = elementCount;
			gap = r / 2;
			while (gap > 0) {
				for (counter = gap; counter < r; counter++) {
					pointer = counter - gap;
					while (pointer >= 0) {
						less = ((KMessage) elementData[pointer]).getSortInfo() < ((KMessage) elementData[gap
								+ pointer]).getSortInfo();
						if (less) {
							temp = elementData[pointer];
							elementData[pointer] = elementData[gap + pointer];
							elementData[gap + pointer] = temp;
							pointer = pointer - gap;
						} else {
							pointer = -1;
						}
					}
				}
				gap = gap / 2;
			}
		}
	}

}
