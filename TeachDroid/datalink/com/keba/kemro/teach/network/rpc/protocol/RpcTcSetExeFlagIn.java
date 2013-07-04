package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSetExeFlagIn implements XDR {
	public int exeUnitHnd;
	public RpcTcExeFlags exeFlag;
	public boolean value;

	public RpcTcSetExeFlagIn () {
		exeFlag = new RpcTcExeFlags();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		exeFlag.write(out);
		out.writeBool(value);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		exeFlag.read(in);
		value = in.readBool();
	}
}