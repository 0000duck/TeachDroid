package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReadNodeChangeIn implements XDR {
	public int startChangeCnt;

	public SysRpcTcReadNodeChangeIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(startChangeCnt);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		startChangeCnt = in.readInt();
	}
}