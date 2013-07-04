package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetValidTokensIn implements XDR {
	public int editHnd;
	public RpcTcPos pos;

	public RpcTcGetValidTokensIn () {
		pos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		pos.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		pos.read(in);
	}
}