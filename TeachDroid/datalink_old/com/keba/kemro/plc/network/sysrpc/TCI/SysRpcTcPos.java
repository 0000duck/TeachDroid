package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcPos implements XDR {
	public int line;
	public int col;

	public SysRpcTcPos () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(line);
		out.writeInt(col);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		line = in.readInt();
		col = in.readInt();
	}
}