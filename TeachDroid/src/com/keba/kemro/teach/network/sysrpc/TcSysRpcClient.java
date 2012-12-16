package com.keba.kemro.teach.network.sysrpc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.XDR;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcClientInfo;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcClientType;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcCloseTeachControlIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcCloseTeachControlOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetClientListOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcKeepAliveOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcOpenTeachControlIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcOpenTeachControlOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcReadProjectPathOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetClientNameIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetClientNameOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetClientTypeIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetClientTypeOut;
import com.keba.kemro.plc.network.sysrpc.TCI.TCI;
import com.keba.kemro.teach.network.TcClient;
import com.keba.kemro.teach.network.base.TcStructuralCache;
import com.keba.kemro.teach.network.rpc.TcRpcStructuralModel;
import com.keba.kemro.teach.network.rpc.protocol.RpcTcClientInfo;
import com.keba.kemro.teach.network.rpc.protocol.RpcTcClientType;

public class TcSysRpcClient extends TcClient {
	private static final int KEEP_ALIVE_SLEEP_TIME = 5000;
	// private final SysRpcTcKeepAliveIn SysRpcTcKeepAliveIn = new
	// SysRpcTcKeepAliveIn();
	private final SysRpcTcKeepAliveOut SysRpcTcKeepAliveOut = new SysRpcTcKeepAliveOut();
	private final SysRpcTcOpenTeachControlIn SysRpcTcOpenTeachControlIn = new SysRpcTcOpenTeachControlIn();
	private final SysRpcTcOpenTeachControlOut SysRpcTcOpenTeachControlOut = new SysRpcTcOpenTeachControlOut();
	private final SysRpcTcCloseTeachControlIn SysRpcTcCloseTeachControlIn = new SysRpcTcCloseTeachControlIn();
	private final SysRpcTcCloseTeachControlOut SysRpcTcCloseTeachControlOut = new SysRpcTcCloseTeachControlOut();
	private final SysRpcTcSetClientTypeIn SysRpcTcSetClientTypeIn = new SysRpcTcSetClientTypeIn();
	private final SysRpcTcSetClientTypeOut SysRpcTcSetClientTypeOut = new SysRpcTcSetClientTypeOut();
	private final SysRpcTcSetClientNameIn SysRpcTcSetClientNameIn = new SysRpcTcSetClientNameIn();
	private final SysRpcTcSetClientNameOut SysRpcTcSetClientNameOut = new SysRpcTcSetClientNameOut();
	private final SysRpcTcReadProjectPathOut SysRpcTcReadProjectPathOut = new SysRpcTcReadProjectPathOut();

	final TcStructuralCache cache = new TcRcpStructuralCache();

	TCI client;
//	private int m_clientID;

	protected static TcSysRpcClient createClient(String application, String server) {
		try {
			TcSysRpcClient rpcClient = new TcSysRpcClient();
			if (rpcClient.openConnection(application, server)) {
				rpcClient.directory = new TcSysRpcDirectoryModel(rpcClient);
				rpcClient.structure = new TcSysRpcStructuralModel(rpcClient);
				rpcClient.execution = new TcSysRpcExecutionModel(rpcClient);
				return rpcClient;
			}
		} catch (Exception e) {
		}
		return null;
	}

	protected TcSysRpcClient() {
	}

	public int getHandle() {
		return super.getHandle();
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
	 * @throws IOException
	 */
	public boolean openConnection(String application, String server) {
		try {
			client = new TCClient(server);
			client.setSoTimeout(TIMEOUT);
			synchronized (SysRpcTcOpenTeachControlIn) {
				SysRpcTcOpenTeachControlIn.clType.value = SysRpcTcClientType.rpcObserver;
				client.SysRpcTcOpenTeachControl_1(SysRpcTcOpenTeachControlIn, SysRpcTcOpenTeachControlOut);
				if (SysRpcTcOpenTeachControlOut.retVal) {
					m_clientID = SysRpcTcOpenTeachControlOut.clientHnd;
					System.out.println("Opened TcSysRPC Connection, my ID is " + SysRpcTcOpenTeachControlOut.clientHnd);
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
					SysRpcTcSetClientNameIn.name = clientID;
					client.SysRpcTcSetClientName_1(SysRpcTcSetClientNameIn, SysRpcTcSetClientNameOut);
					if (SysRpcTcSetClientNameOut.retVal) {
						client.SysRpcTcReadProjectPath_1(SysRpcTcReadProjectPathOut);
						if (SysRpcTcReadProjectPathOut.retVal) {
							setClientData(SysRpcTcOpenTeachControlOut.clientHnd, clientID, SysRpcTcOpenTeachControlOut.tcVersion.toString(), SysRpcTcReadProjectPathOut.pathLocal.toString());
							isConnected = true;
							new KeepAliveThread();
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
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
				synchronized (SysRpcTcCloseTeachControlIn) {
					SysRpcTcCloseTeachControlIn.closeClientHnd = getHandle();
					client.SysRpcTcCloseTeachControl_1(SysRpcTcCloseTeachControlIn, SysRpcTcCloseTeachControlOut);
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
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		if (client != null) {
			try {
				synchronized (SysRpcTcSetClientTypeIn) {

					SysRpcTcSetClientTypeIn.type.value = writeAccessAllowed ? SysRpcTcClientType.rpcController : SysRpcTcClientType.rpcObserver;
					SysRpcTcSetClientTypeIn.forceController = true;
					client.SysRpcTcSetClientType_1(SysRpcTcSetClientTypeIn, SysRpcTcSetClientTypeOut);
					if (SysRpcTcSetClientTypeOut.retVal) {
						hasWriteAccess = writeAccessAllowed;
					}

//					System.out.println("Setting write access to [" + writeAccessAllowed + "] returned " + SysRpcTcSetClientTypeOut.retVal);
//
//					if (isTcController()) {
//						System.out.println("...yep, I really am the controller.");
//					}

					return SysRpcTcSetClientTypeOut.retVal;
				}
			} catch (Exception e) {
				// TODO: do the exception handling in TCClient
			}
		}
		return false;
	}

	public boolean isTcController() {
		SysRpcTcClientInfo[] info = getClientInfo();
		if (m_clientID <= 0)
			return false;

		for (int i = 0; i < info.length; i++) {
			if (info[i].hnd == m_clientID && info[i].type.value == RpcTcClientType.rpcController)
				return true;
		}

		return false;
	}

	public SysRpcTcClientInfo[] getClientInfo() {
		SysRpcTcGetClientListOut out = new SysRpcTcGetClientListOut();

		try {
			client.SysRpcTcGetClientList_1(out);
			if (out.retVal) {
				return out.clients;
			}
		} catch (RPCException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected synchronized void sendDisconnectEvent() {
		if (isConnected) {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.sendDisconnectEvent();
		}
	}

	private void keepAlive() {
		try {
			synchronized (SysRpcTcKeepAliveOut) {
				client.SysRpcTcKeepAlive_1(SysRpcTcKeepAliveOut);
				if (!SysRpcTcKeepAliveOut.retVal) {
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

		public synchronized void call(int prog, int version, int proc, XDR argValue, XDR[] retValue) throws RPCException, IOException {
			try {
				super.call(prog, version, proc, argValue, retValue);
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
