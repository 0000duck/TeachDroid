/*-------------------------------------------------------------------------
 *                   (c) 1999 by KEBA Ges.m.b.H & Co
 *                            Linz/AUSTRIA
 *                         All rights reserved
 *--------------------------------------------------------------------------
 *    Projekt   : KEMRO.teachview.4
 *    Auftragsnr: 5500395
 *    Erstautor : sinn
 *    Datum     : 01.04.2003
 *--------------------------------------------------------------------------
 *      Revision:
 *        Author:  sinn
 *          Date:
 *------------------------------------------------------------------------*/
package com.keba.kemro.teach.network;

import android.util.Log;

import com.keba.kemro.teach.network.rpc.TcRpcClient;
import com.keba.kemro.teach.network.rpc.TcRpcDirectoryModel;
import com.keba.kemro.teach.network.sysrpc.TcSysRpcClient;

/**
 * TcConnectionManager manages the connection to TeachControl.
 * 
 * @see TcConnectionListener
 * @see TcRpcDirectoryModel
 * @see TcStructuralModel
 * @see TcExecutionModel
 */
public class TcConnectionManager {

	public static String CONNECTION_TYPE_PROP = "TCConnectionType";
	public static final String DETECT_TYPE = "detect";
	public static final String RPC_TYPE = "rpc";
	public static final String SYSRPC_TYPE = "sysrpc";
	public static final String KLINK_TYPE = "klink";

	public static TcClient getTcClient(String application, String server) {
		String connectionType = System.getProperty(CONNECTION_TYPE_PROP);
		TcClient client = null;

		 if (RPC_TYPE.equalsIgnoreCase(connectionType)) {
			client = RpcClient.createRpcClient(application, server);
		} else if (SYSRPC_TYPE.equalsIgnoreCase(connectionType)) {
			client = SysRpcClient.createSysRpcClient(application, server);
		} else {
			Log.i("TC connection","initializing SysRPC Client");
			client = SysRpcClient.createSysRpcClient(application, server);


		}
		return client;
	}

	private static class RpcClient extends TcRpcClient {
		private static TcRpcClient createRpcClient(String application, String server) {
			return TcRpcClient.createClient(application, server);
		}
	}



	private static class SysRpcClient extends TcSysRpcClient {
		protected static TcSysRpcClient createSysRpcClient(String application, String server) {
			return TcSysRpcClient.createClient(application, server);
		}
	}
}
