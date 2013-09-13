/**
 * 
 */
package com.keba.kemro.kvs.teach.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import com.keba.kemro.kvs.teach.util.Log;

/**
 * @author ltz
 * 
 */
public class KvtTraceUpdater implements Runnable {

	private static KvtTraceUpdater					mUpdater;
	private int										mTcpPort		= 4001;
	private static InetAddress						mHost;
	private Socket									mClientSocket;
	private static Thread							mListenerThread;
	protected static boolean						mKillThread		= false;
	public static final int							MODE_HISTORY	= 0;
	public static final int							MODE_FROMNOW	= 1;

	private static int								mLoadMode		= MODE_FROMNOW;

	private static Vector<KvtTraceUpdateListener>	mListeners		= new Vector<KvtTraceUpdateListener>();

	/**
	 * Interface that allows to receive PLC trace messages directly via a raw
	 * TCP channel
	 * 
	 * @author ltz
	 * @since 08.08.2013
	 * 
	 */
	public static interface KvtTraceUpdateListener {
		/**
		 * Called whenever one line is received from the trace server
		 * 
		 * @param _line
		 *            The trace message. Can be ""
		 */
		public void lineReceived(String _line);
	}

	private KvtTraceUpdater() {
		createConnection();
	}

	/**
	 * creates a connection to the host and starts a thread which periodically
	 * reads trace messages from the server
	 */
	private void createConnection() {
		if (mHost == null)
			throw new RuntimeException("KvtTraceUpdater: no hostname configured, call connec(<hostname>) first!");

		try {
			if (mHost.isReachable(5000)) {
				// mClientSocket = new Socket(mHost, mTcpPort);
				mClientSocket = new Socket();
				mClientSocket.connect(new InetSocketAddress(mHost, mTcpPort), 2000);

				mListenerThread = new Thread(this, getClass().getSimpleName());
				mListenerThread.start();
			}
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
	}

	/**
	 * The Load mode defines, which messages are read from the trace server:
	 * <ul>
	 * <li><code>MODE_HISTORY</code>: loads all messages since the last PLC
	 * restart (COSTLY!!)</li>
	 * <li><code>MODE_FROMNOW</code>: loads all messages which are traced from
	 * now on. This is naturally more efficient, but past messages are ignored.</li>
	 * </ul>
	 * 
	 * @param _loadMode
	 *            {@link KvtTraceUpdater#MODE_HISTORY} for complete trace,
	 *            {@link KvtTraceUpdater#MODE_FROMNOW} for reduced trace.
	 */
	public static void setLoadMode(int _loadMode) {
		if (_loadMode > MODE_FROMNOW || _loadMode < MODE_HISTORY) {
			mLoadMode = MODE_HISTORY;
			Log.w("KvtTraceUpdater", "load mode must be MODE_HISTORY (" + MODE_HISTORY + ") or MODE_FROMNOW (" + MODE_FROMNOW + ") but was "
					+ _loadMode);
		} else
			mLoadMode = _loadMode;
	}

	/**
	 * Set the textual hostname or the IP address string ("XXX.XXX.XXX.XXX") of
	 * the PLC whos trace should be obtained.
	 * 
	 * @param _hostname
	 *            A textual (DNS) hostname or an IP address.
	 */
	public static void setHostname(String _hostname) {
		try {
			mHost = InetAddress.getByName(_hostname);
		} catch (UnknownHostException e) {
			Log.e("KvtTraceUpdater", e.getLocalizedMessage());
		}
	}

	/**
	 * Adds the specified listener to the list. Lazy initialization takes place,
	 * which means the internal update thread is initialized upon first
	 * invocation of this method.
	 * 
	 * @param _listener
	 *            The listener to add to the list.s
	 * @return <code>true</code> if the listener was successfully added,
	 *         <code>false</code> otherwise
	 * @throws RuntimeException
	 *             if {@link KvtTraceUpdater#setHostname(String)} has not yet
	 *             been invoked. Call {@link KvtTraceUpdater#isInitialized()} to
	 *             check this.
	 */
	public static boolean addListener(KvtTraceUpdateListener _listener) throws RuntimeException {

		if (mUpdater == null) {
			mUpdater = new KvtTraceUpdater();
		}
		return mListeners.add(_listener);
	}

	public static boolean isInitialized() {
		return mHost == null;
	}

	/**
	 * Removes the specified listener from the list. If no more listeners are
	 * present, a graceful shutdown is performed, i.e. networking resources are
	 * freed and the update thread is stopped.
	 * 
	 * @param _listener
	 *            The listener which should be removed from the list
	 * @return <code>true</code> if the listener was removed successfully,
	 *         <code>false</code> if the listener was not added before or if the
	 *         listener list is <code>null</code>
	 */
	public static boolean removeListener(KvtTraceUpdateListener _listener) {
		if (mListeners != null && mListeners.contains(_listener)) {
			boolean success = mListeners.remove(_listener);

			if (mListeners.isEmpty()) {

				stopThread();
				mUpdater = null;
			}

			return success;
		}
		return false;
	}

	/**
	 * method that kills the tcp read thread gracefully by setting the
	 * run-method's kill flag
	 */
	private synchronized static void stopThread() {
		mKillThread = true;
		try {
			mListenerThread.join(1000);

		} catch (InterruptedException e) {
			mListenerThread.stop();
			e.printStackTrace();
		} finally {
			mListenerThread = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */

	@Override
	public synchronized void run() {

		InputStream netStream = null;
		boolean initialMessage = true;

		if (!mClientSocket.isConnected()) {
			for (KvtTraceUpdateListener l : mListeners) {
				l.lineReceived("No connection to the trace server established! Maybe another Client is already connected?");
			}
			return;
		}

		try {
			netStream = mClientSocket.getInputStream();
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), e.getLocalizedMessage());
		}
		if (netStream != null) {
			String line = null;
			BufferedReader bis = new BufferedReader(new InputStreamReader(netStream));

			try {
				while ((line = bis.readLine()) != null && !mKillThread) {

					if (initialMessage) {
						byte mode = 'f';
						if (mLoadMode == MODE_FROMNOW)
							mode = 'c';
						mClientSocket.getOutputStream().write(new byte[] { mode });
						initialMessage = false;
					} else {
						for (KvtTraceUpdateListener l : mListeners) {
							l.lineReceived(line);
						}
					}
					Thread.sleep(50);
				}
				// thread is killed, perform cleanup
				mClientSocket.close();
				mClientSocket = null;

			} catch (IOException ex) {
				Log.w(getClass().getSimpleName(), ex.getLocalizedMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * @param _host
	 */
	public static void connect(String _host) {
		setHostname(_host);
		if (mUpdater == null)
			mUpdater = new KvtTraceUpdater();
		mUpdater.createConnection();

	}

}
