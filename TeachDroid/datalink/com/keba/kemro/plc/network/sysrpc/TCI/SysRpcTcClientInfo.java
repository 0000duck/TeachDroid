package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcClientInfo implements XDR {
	public int hnd;
	public SysRpcTcClientType type;
	public String name;

	public SysRpcTcClientInfo () {
		type = new SysRpcTcClientType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
		type.write(out);
		out.writeString(name);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
		type.read(in);
		name = in.readString();
	}
}