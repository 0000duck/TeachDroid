package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcSaveEditorIn implements XDR {
	public int editHnd;

	public RpcTcSaveEditorIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
	}
}