package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcNewVarGroupIn implements XDR {
	public String groupName;

	public RpcTcNewVarGroupIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(groupName);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		groupName = in.readString();
	}
}