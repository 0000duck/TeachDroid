package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcReplaceCommentIn implements XDR {
	public int itemHnd;
	public RpcTcPos begPos;
	public RpcTcPos endPos;
	public String commentText;

	public RpcTcReplaceCommentIn () {
		begPos = new RpcTcPos();
		endPos = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(itemHnd);
		begPos.write(out);
		endPos.write(out);
		out.writeString(commentText);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		itemHnd = in.readInt();
		begPos.read(in);
		endPos.read(in);
		commentText = in.readString();
	}
}