package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcDeleteTextIn implements XDR {
	public int editHnd;
	public RpcTcPos beg;
	public RpcTcPos end;

	public RpcTcDeleteTextIn () {
		beg = new RpcTcPos();
		end = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		beg.write(out);
		end.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		beg.read(in);
		end.read(in);
	}
}