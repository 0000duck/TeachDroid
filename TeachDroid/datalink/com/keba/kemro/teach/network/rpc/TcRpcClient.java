package com.keba.kemro.teach.network.rpc;

import java.io.*;
import java.net.*;

import com.keba.jrpc.rpc.*;
//import com.keba.kemro.kvs.teach.framework.util.KvtLogger;
import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.base.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcClient extends TcClient {
	private static final int KEEP_ALIVE_SLEEP_TIME = 5000;
	private final RpcTcKeepAliveOut rpcTcKeepAliveOut = new RpcTcKeepAliveOut();
	private final RpcTcOpenTeachControlIn rpcTcOpenTeachControlIn = new RpcTcOpenTeachControlIn();
	private final RpcTcOpenTeachControlOut rpcTcOpenTeachControlOut = new RpcTcOpenTeachControlOut();
	private final RpcTcCloseTeachControlIn rpcTcCloseTeachControlIn = new RpcTcCloseTeachControlIn();
	private final RpcTcCloseTeachControlOut rpcTcCloseTeachControlOut = new RpcTcCloseTeachControlOut();
	private final RpcTcSetClientTypeIn rpcTcSetClientTypeIn = new RpcTcSetClientTypeIn();
	private final RpcTcSetClientTypeOut rpcTcSetClientTypeOut = new RpcTcSetClientTypeOut();
	// private final RpcTcRequestWriteAccessIn rpcTcRequestWriteAccessIn = new
	// RpcTcRequestWriteAccessIn();
	// private final RpcTcRequestWriteAccessOut rpcTcRequestWriteAccessOut = new
	// RpcTcRequestWriteAccessOut();
	// private final RpcTcWriteAccessRequestPendingOut
	// rpcTcWriteAccessRequestPendingOut = new
	// RpcTcWriteAccessRequestPendingOut();
	private final RpcTcSetClientNameIn rpcTcSetClientNameIn = new RpcTcSetClientNameIn();
	private final RpcTcSetClientNameOut rpcTcSetClientNameOut = new RpcTcSetClientNameOut();
	private final RpcTcReadProjectPathOut rpcTcReadProjectPathOut = new RpcTcReadProjectPathOut();

	final TcStructuralCache cache = new TcRcpStructuralCache();

	TCI client;
//	private int m_clientID;

	protected static TcRpcClient createClient(String application, String server) {
		try {
			TcRpcClient rpcClient = new TcRpcClient();
			if (rpcClient.openConnection(application, server)) {
				rpcClient.directory = new TcRpcDirectoryModel(rpcClient);
				rpcClient.structure = new TcRpcStructuralModel(rpcClient);
				rpcClient.execution = new TcRpcExecutionModel(rpcClient);
				return rpcClient;
			}
		} catch (Exception e) {
		}
		return null;
	}

	protected TcRpcClient() {
	}

	public RpcTcClientInfo[] getClientInfo() {
		RpcTcGetClientListOut lst = new RpcTcGetClientListOut();
		try {
			client.RpcTcGetClientList_1(lst);
			return lst.clients;
		} catch (RPCException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isTcController() {
		RpcTcClientInfo[] info = getClientInfo();
		if(m_clientID <= 0)return false;
		
		for(int i=0; i<info.length; i++) {
			if(info[i].hnd == m_clientID && info[i].type.value == RpcTcClientType.rpcController)
				return true;
		}
		
		return false;
	}

	/**
	 * Initiate a connection to TeachControl
	 * 
	 * @param server
	 *            name of the server on which TeachControl is running on.
	 * @param asController
	 *            true if the client has write access otherwise false
	 * 
	 * @return true if the client is successfully connected
	 */
	public boolean openConnection(String application, String server) {
		try {
//			KvtLogger.error(this, "Creating RPC client for server " + server);
			client = new TCClient(server);
			client.setSoTimeout(TIMEOUT);
			synchronized (rpcTcOpenTeachControlIn) {
				rpcTcOpenTeachControlIn.clType.value = RpcTcClientType.rpcObserver;
				client.RpcTcOpenTeachControl_1(rpcTcOpenTeachControlIn, rpcTcOpenTeachControlOut);
//				KvtLogger.error(this, "TcOpenTeachControl returned [" + rpcTcOpenTeachControlOut.retVal + "]");
				if (rpcTcOpenTeachControlOut.retVal) {
					m_clientID = rpcTcOpenTeachControlOut.clientHnd;
					// set client name
					InetAddress localhost = InetAddress.getLocalHost();
					String ip = localhost.getHostAddress();
					String clientID = null;
					if (application != null) {
						clientID = application + " (" + ip + ")";
						if (32 < clientID.length()) {
							clientID = clientID.substring(0, 32);
						}
					} else {
						clientID = ip;
					}
					rpcTcSetClientNameIn.name = clientID;
					client.RpcTcSetClientName_1(rpcTcSetClientNameIn, rpcTcSetClientNameOut);
					if (rpcTcSetClientNameOut.retVal) {
						client.RpcTcReadProjectPath_1(rpcTcReadProjectPathOut);
						if (rpcTcReadProjectPathOut.retVal) {
							setClientData(rpcTcOpenTeachControlOut.clientHnd, clientID, rpcTcOpenTeachControlOut.tcVersion, rpcTcReadProjectPathOut.pathLocal);
							isConnected = true;
							new KeepAliveThread();
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			// System.out.println("Connect failed in TcConnectionManager - connect: ");
		}
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException exc) {
			// ignore
		}
		return false;
	}

	/**
	 * Disconnects the client.
	 */
	public void disconnect() {
		if (isConnected) {
			try {
				synchronized (rpcTcCloseTeachControlIn) {
					rpcTcCloseTeachControlIn.clientHnd = getHandle();
					client.RpcTcCloseTeachControl_1(rpcTcCloseTeachControlIn, rpcTcCloseTeachControlOut);
				}
			} catch (Exception e) {
				// TODO: do the exception handling in TCClient
			}
		}
		sendDisconnectEvent();
	}

	public void close() {
		sendDisconnectEvent();
	}

	/**
	 * Sets the connection timeout.
	 * 
	 * @param ms
	 *            timeout
	 */
	public void setTimeout(int ms) {
		super.setTimeout(ms);
		if (client != null) {
			try {
				client.setSoTimeout(TIMEOUT);
			} catch (SocketException se) {
			}
		}
	}

	/**
	 * Sets the write access.
	 * 
	 * @param writeAccessAllowed
	 *            true for write access
	 * @return true if write access was granted
	 */
	public boolean setWriteAccess(boolean writeAccessAllowed) {
//		KvtLogger.error(this, "Trying to set write access: " + writeAccessAllowed);
		if (client != null) {
			try {
				synchronized (rpcTcSetClientTypeIn) {

					rpcTcSetClientTypeIn.type.value = writeAccessAllowed ? RpcTcClientType.rpcController : RpcTcClientType.rpcObserver;
					rpcTcSetClientTypeIn.forceController = true;
					client.RpcTcSetClientType_1(rpcTcSetClientTypeIn, rpcTcSetClientTypeOut);
					if (rpcTcSetClientTypeOut.retVal) {
						hasWriteAccess = writeAccessAllowed;
					}
					System.out.println("Setting write access to [" + writeAccessAllowed + "] returned " + rpcTcSetClientTypeOut.retVal);

					if(isTcController())
						System.out.println("...yep, I really am the controller.");

//					KvtLogger.error(this, "Setting write access to [" + writeAccessAllowed + "] returned " + rpcTcSetClientTypeOut.retVal);
					return rpcTcSetClientTypeOut.retVal;
				}
			} catch (Exception e) {
				// TODO: do the exception handling in TCClient
			}
		}
		return false;
	}

	protected synchronized void sendDisconnectEvent() {
		if (isConnected) {
			try {
				client.close();
			} catch (IOException exc) {
				// ignore
			}
			super.sendDisconnectEvent();
		}
	}

	private void keepAlive() {
		try {
			synchronized (rpcTcKeepAliveOut) {
				client.RpcTcKeepAlive_1(rpcTcKeepAliveOut);
				if (!rpcTcKeepAliveOut.retVal) {
					sendDisconnectEvent();
				}
			}
		} catch (Exception e) {
			// TODO: do the exception handling in TCClient
		}
	}

	private static class TcRcpStructuralCache extends TcStructuralCache {
	}

	private class KeepAliveThread extends Thread {
		private KeepAliveThread() {
			super("TcConnectionManager-KeepAlive");
			setPriority(Thread.MIN_PRIORITY);
			start();
		}

		public void run() {
			while (isConnected) {
				try {
					Thread.sleep(KEEP_ALIVE_SLEEP_TIME);
				} catch (InterruptedException e) {
					// ignore
				}
				if (isConnected) {
					keepAlive();
				}
			}
		}
	}

	private class TCClient extends TCI {
		private TCClient(String host) throws RPCException, UnknownHostException, IOException {
			super(host);
		}

		public synchronized void call(int prog, int version, int proc, XDR retVal, int args_length, XDR[] args) throws IOException, RPCException {
			try {
				super.call(prog, version, proc, retVal, args_length, args);
			} catch (IOException ioe) {
				sendDisconnectEvent();
				throw ioe;
			} catch (RPCException ex) {
				sendDisconnectEvent();
				throw ex;
			} catch (Exception e) {
				sendDisconnectEvent();
				System.out.println("Error in TcConnectionManager-TCClient.call");
				e.printStackTrace();
				throw new IOException(e.getMessage());
			}
		}
	}

}
