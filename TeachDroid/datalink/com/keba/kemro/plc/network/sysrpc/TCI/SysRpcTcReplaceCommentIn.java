package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReplaceCommentIn implements XDR {
	public int itemHnd;
	public SysRpcTcPos begPos;
	public SysRpcTcPos endPos;
	public String commentText;

	public SysRpcTcReplaceCommentIn () {
		begPos = new SysRpcTcPos();
		endPos = new SysRpcTcPos();
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