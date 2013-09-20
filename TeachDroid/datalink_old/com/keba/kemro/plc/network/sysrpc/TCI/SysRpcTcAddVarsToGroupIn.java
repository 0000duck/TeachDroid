package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcAddVarsToGroupIn implements XDR {
	public int varGroupHnd;
	public int[] exeUnitHnd;  //variable length with max length of TCI.rpcChunkLen
	public int exeUnitHnd_count; //countains the number of elements
	public SysRpcTcVarAccess[] varAccess;  //variable length with max length of TCI.rpcChunkLen
	public int varAccess_count; //countains the number of elements

	public SysRpcTcAddVarsToGroupIn () {
		exeUnitHnd = new int[TCI.rpcChunkLen];
		varAccess = new SysRpcTcVarAccess[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			varAccess[for_i] = new SysRpcTcVarAccess();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varGroupHnd);
		out.writeInt(exeUnitHnd_count);
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			out.writeInt(exeUnitHnd[for_i]);
		}
		out.writeInt(varAccess_count);
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varGroupHnd = in.readInt();
		exeUnitHnd_count = in.readInt();
		for (int for_i = 0; for_i < exeUnitHnd_count; for_i++) {
			exeUnitHnd[for_i] = in.readInt();
		}
		varAccess_count = in.readInt();
		for (int for_i = 0; for_i < varAccess_count; for_i++) {
			varAccess[for_i].read(in);
		}
	}
}