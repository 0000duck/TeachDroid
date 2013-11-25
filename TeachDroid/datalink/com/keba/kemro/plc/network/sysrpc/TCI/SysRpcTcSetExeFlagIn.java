package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcSetExeFlagIn implements XDR {
	public int exeUnitHnd;
	public SysRpcTcExeFlags exeFlag;
	public boolean value;

	public SysRpcTcSetExeFlagIn () {
		exeFlag = new SysRpcTcExeFlags();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(exeUnitHnd);
		exeFlag.write(out);
		out.writeBool(value);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		exeUnitHnd = in.readInt();
		exeFlag.read(in);
		value = in.readBool();
	}
}