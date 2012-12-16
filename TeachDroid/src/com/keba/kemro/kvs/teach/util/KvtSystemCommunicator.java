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
package com.keba.kemro.kvs.teach.util;

import java.util.*;

import android.os.AsyncTask;
import android.util.Log;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.network.*;

/**
 * Class KSystemCommunicator
 */
public class KvtSystemCommunicator {
	/** Field AS_CONTROLLER */
	private static boolean WRITE_ACCESS_ALLOWED = false;

	/** Field m_connectionListeners */
	private static final Vector m_connectionListeners = new Vector(10);
	/** Field m_hostName */
	private static String m_hostName;
	private static String clientID;

	private static KTcDfl dfl;

	public static KTcDfl getTcDfl() {
		return dfl;
	}

	/**
	 * Verbindungsaufbau zum Laufzeitsystem
	 * 
	 * @param hostName
	 *            Name des Hosts
	 * @param timeout
	 *            Timeout int ms
	 * @param _progressPublisher 
	 * @param _progressPublisher 
	 * @param automaticReconnect
	 *            Automatisches Reconnect bei Verbindungsabbruch
	 */
	public static void connect(String hostName, int timeout, String globalFilter) {
		m_hostName = hostName;

		TcClient client = null; //TcConnectionManager.getTcClient("Teachview",m_hostName);

		int i = 0;
		while ((client == null) && (i < 100)) { //20 seconds connect timeout
			try {
				Thread.sleep(200);
			} catch (InterruptedException ie) {
			}
			i++;
			client = TcConnectionManager.getTcClient("TeachDroid", m_hostName);
			
		}
		if (client != null) {
			client.setTimeout(timeout);
			client.setUserMode(false);
			client.setWriteAccess(WRITE_ACCESS_ALLOWED);
			clientID = client.getClientID();
			dfl = new KTcDfl(client, globalFilter);
			client.addConnectionListener(new KTcConnectionListener());

			fireConnected();

		}
	}
	
	public static boolean connectOnce(String _host, int _to, String _globalFilter) {
		m_hostName= _host;
		TcClient client= null;
		
		client= TcConnectionManager.getTcClient("TeachDroid", m_hostName);
		if(client != null) {
			client.setTimeout(_to);
			client.setUserMode(false);
			client.setWriteAccess(true);
			clientID= client.getClientID();
			dfl= new KTcDfl(client, _globalFilter);
			client.addConnectionListener(new KTcConnectionListener());
			fireConnected();
			return true;
		}
		
		return false;
	}

	public static void shutdown() {
		KTcDfl tcDfl = dfl;
		if (tcDfl != null) {
			tcDfl.disconnect();
			dfl = null;
		}
	}

	/**
	 * Verbindung wird abbgebaut
	 */
	public static void disconnect() {
		KTcDfl tcDfl = dfl;
		if (tcDfl != null) {
			tcDfl.client.disconnect();
			dfl = null;
			fireDisconnected();
		}
	}

	public static void close() {
		KTcDfl tcDfl = dfl;
		if (tcDfl != null) {
			tcDfl.client.close();
			fireDisconnected();
			dfl = null;
		}
	}

	/**
	 * Liefert true wenn eine Verbindung besteht
	 * 
	 * @return true wenn eine Verbindung besteht
	 */
	public static boolean isConnected() {
		return dfl != null;
	}

	/**
	 * sets o removes write access
	 * 
	 * @param writeAccessAllowed
	 *            true if write access is granted
	 */
	public static void setAccessMode(boolean writeAccessAllowed) {
		WRITE_ACCESS_ALLOWED = writeAccessAllowed;
		KTcDfl tcDfl = dfl;
		if (tcDfl != null) {
			tcDfl.client.setWriteAccess(writeAccessAllowed);
		}
	}

	/**
	 * Fügt einen Verbindungslistener hinzu.
	 * 
	 * @param listener
	 *            Verbindungslistener
	 */
	public static void addConnectionListener(
			KvtTeachviewConnectionListener listener) {
		if (!m_connectionListeners.contains(listener)) {
			m_connectionListeners.addElement(listener);
		}
	}

	/**
	 * Entfernt einen Verbindungslistener
	 * 
	 * @param listener
	 *            Verbindungslistener
	 */
	public static void removeConnectionListener(
			KvtTeachviewConnectionListener listener) {
		m_connectionListeners.removeElement(listener);
	}

	private static void fireConnected() {
		for (int i = 0; i < m_connectionListeners.size(); i++) {
			try {

				((KvtTeachviewConnectionListener) (m_connectionListeners
						.elementAt(i))).teachviewConnected();

			} catch (Exception ex) {
				Log.e(KvtSystemCommunicator.class.toString(),
						"Error in Call of KTeachviewConnectionListener.connected "
								+ m_connectionListeners.elementAt(i), ex);
			}
		}
	}

	private static void fireDisconnected() {
		for (int i = 0; i < m_connectionListeners.size(); i++) {
			try {
				((KvtTeachviewConnectionListener) (m_connectionListeners
						.elementAt(i))).teachviewDisconnected();
			} catch (Exception ex) {
				Log.e(KvtSystemCommunicator.class.toString(),
						"Error in Call of KTeachviewConnectionListener.disconnected "
								+ m_connectionListeners.elementAt(i), ex);
			}
		}
	}

	private static class KTcConnectionListener implements TcConnectionListener {
		/**
		 * @see com.keba.kemro.teach.network.TcConnectionListener#connectionStateChanged(boolean)
		 */
		public void connectionStateChanged(boolean isConnected) {
			if (isConnected) {
			} else {
				if (dfl != null) {
					Log.i(getClass().toString(),
							"Disconnected to TeachControl - Client ID: "
									+ clientID);
					dfl = null;
					fireDisconnected();
				}
			}
		}
	}
}
