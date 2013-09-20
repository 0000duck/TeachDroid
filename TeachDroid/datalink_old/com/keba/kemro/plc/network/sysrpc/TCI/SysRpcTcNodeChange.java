package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcNodeChange implements XDR {
	public int changeCnt;
	public int hdl;

	public SysRpcTcNodeChange () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(changeCnt);
		out.writeInt(hdl);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		changeCnt = in.readInt();
		hdl = in.readInt();
	}
}