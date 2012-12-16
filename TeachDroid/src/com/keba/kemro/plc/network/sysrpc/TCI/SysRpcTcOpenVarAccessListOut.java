package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcOpenVarAccessListOut implements XDR {
	public SysRpcTcVarAccess[] varAccess;  //variable length with max length of TCI.rpcChunkLen
	public int varAccess_count; //countains the number of elements
	public SysRpcTcVarAccessInfo[] info;  //variable length with max length of TCI.rpcChunkLen
	public int info_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcOpenVarAccessListOut () {
		varAccess = new SysRpcTcVarAccess[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			varAccess[for_i] = new SysRpcTcVarAccess();
		}
		info = new SysRpcTcVarAccessInfo[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			info[for_i] = new SysRpcTcVarAccessInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varAccess_count);
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].write(out);
		}
		out.writeInt(info_count);
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varAccess_count = in.readInt();
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].read(in);
		}
		info_count = in.readInt();
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].read(in);
		}
		retVal = in.readBool();
	}
}