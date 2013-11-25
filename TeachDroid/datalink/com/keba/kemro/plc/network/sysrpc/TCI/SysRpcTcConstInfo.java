package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcConstInfo implements XDR {
	public int typeHnd;
	public SysRpcTcValue value;

	public SysRpcTcConstInfo () {
		value = new SysRpcTcValue();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeHnd);
		value.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeHnd = in.readInt();
		value.read(in);
	}
}