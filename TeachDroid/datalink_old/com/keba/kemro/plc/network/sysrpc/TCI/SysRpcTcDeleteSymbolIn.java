package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDeleteSymbolIn implements XDR {
	public int editHnd;
	public int symHnd;

	public SysRpcTcDeleteSymbolIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		out.writeInt(symHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		symHnd = in.readInt();
	}
}