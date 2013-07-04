package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetClientListOut implements XDR {
	public SysRpcTcClientInfo[] clients;  //variable length with max length of 10
	public int clients_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetClientListOut () {
		clients = new SysRpcTcClientInfo[10];
		for (int for_i = 0; for_i < 10; for_i++) {
			clients[for_i] = new SysRpcTcClientInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(clients_count);
		for (int for_i = 0; for_i < clients_count; for_i++) {
			clients[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		clients_count = in.readInt();
		for (int for_i = 0; for_i < clients_count; for_i++) {
			clients[for_i].read(in);
		}
		retVal = in.readBool();
	}
}