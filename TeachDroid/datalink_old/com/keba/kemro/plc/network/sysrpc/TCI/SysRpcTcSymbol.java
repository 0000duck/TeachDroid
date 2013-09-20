package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSymbol implements XDR {
	public int hnd;
	public SysRpcTcSymbolKind kind;
	public String text;

	public SysRpcTcSymbol () {
		kind = new SysRpcTcSymbolKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(hnd);
		kind.write(out);
		out.writeString(text);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		hnd = in.readInt();
		kind.read(in);
		text = in.readString();
	}
}