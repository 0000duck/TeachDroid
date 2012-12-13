package com.keba.teachdroid.rc;

import com.keba.kemro.teach.dfl.KTcDfl;
import com.keba.kemro.teach.network.TcClient;
import com.keba.kemro.teach.network.TcConnectionManager;

public class RobotControlProxy {

	private static String clientID;

	public static String[] getRobotNames() {
		return new String[] { "ArtarmTX60L", "ArtarmTX40", "Scara RS80" };
	}

	public static KTcDfl connect(String _hostName, int timeout,
			String globalFilter) {

		System.setProperty(TcConnectionManager.CONNECTION_TYPE_PROP, "sysrpc");

		TcClient client = TcConnectionManager.getTcClient("Teachview",
				_hostName);

		for (int i = 0; client == null && i < 120; client = TcConnectionManager
				.getTcClient("Teachview", _hostName)) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException ie) {
			}
			i++;
		}

		KTcDfl dfl = null;
		if (client != null) {
			client.setTimeout(timeout);
			client.setUserMode(false);
			client.setWriteAccess(true);
			clientID = client.getClientID();
			dfl = new KTcDfl(client, globalFilter);
		}
		return dfl;
	}
}
