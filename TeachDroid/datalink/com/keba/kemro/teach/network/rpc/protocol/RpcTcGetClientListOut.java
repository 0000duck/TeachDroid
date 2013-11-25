package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetClientListOut implements XDR {
	public RpcTcClientInfo[] clients;  //variable length
	public int clients_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetClientListOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(clients_count);
		for (int for_i = 0; for_i < clients_count; for_i++) {
			clients[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {clients_count = in.readInt();
		clients = new RpcTcClientInfo[clients_count];
		for (int for_i = 0; for_i < clients_count; for_i++) {
			clients[for_i] = new RpcTcClientInfo();
			clients[for_i].read(in);
		}
		retVal = in.readBool();
	}
}