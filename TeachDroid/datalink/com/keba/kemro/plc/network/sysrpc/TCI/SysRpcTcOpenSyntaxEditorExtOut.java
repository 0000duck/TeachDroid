package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenSyntaxEditorExtOut implements XDR {
	public int editHnd;
	public boolean retVal;

	public SysRpcTcOpenSyntaxEditorExtOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		retVal = in.readBool();
	}
}