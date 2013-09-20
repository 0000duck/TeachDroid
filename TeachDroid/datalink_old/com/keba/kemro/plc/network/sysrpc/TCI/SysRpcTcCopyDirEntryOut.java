package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcCopyDirEntryOut implements XDR {
	public String dirEntryPath;
	public boolean retVal;

	public SysRpcTcCopyDirEntryOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(dirEntryPath);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		dirEntryPath = in.readString();
		retVal = in.readBool();
	}
}