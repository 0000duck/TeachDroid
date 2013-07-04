package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcInsertTextIn implements XDR {
	public int editHnd;
	public RpcTcPos pos;
	public int len;
	public String text;

	public RpcTcInsertTextIn () {
		pos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		pos.write(out);
		out.writeInt(len);
		out.writeString(text);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		pos.read(in);
		len = in.readInt();
		text = in.readString();
	}
}