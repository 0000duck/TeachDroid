package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcNewVarGroupIn implements XDR {
	public String groupName;

	public SysRpcTcNewVarGroupIn () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(groupName);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		groupName = in.readString();
	}
}