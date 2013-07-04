package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcInstancePathElem implements XDR {
	public int structComponent;
	public int arrayIndex;

	public SysRpcTcInstancePathElem () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(structComponent);
		out.writeInt(arrayIndex);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		structComponent = in.readInt();
		arrayIndex = in.readInt();
	}
}