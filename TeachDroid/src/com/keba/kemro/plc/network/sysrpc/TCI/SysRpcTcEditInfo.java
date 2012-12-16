package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcEditInfo implements XDR {
	public int noLines;
	public int noChars;

	public SysRpcTcEditInfo () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(noLines);
		out.writeInt(noChars);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		noLines = in.readInt();
		noChars = in.readInt();
	}
}