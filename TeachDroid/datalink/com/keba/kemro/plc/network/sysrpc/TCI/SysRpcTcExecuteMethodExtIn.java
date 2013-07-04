package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcExecuteMethodExtIn implements XDR {
	public int routineHnd;
	public SysRpcTcVarAccess instanceVarAccess;
	public SysRpcTcMethodParam[] routParams;  //variable length with max length of TCI.rpcMaxParams
	public int routParams_count; //countains the number of elements

	public SysRpcTcExecuteMethodExtIn () {
		instanceVarAccess = new SysRpcTcVarAccess();
		routParams = new SysRpcTcMethodParam[TCI.rpcMaxParams];
		for (int for_i = 0; for_i < TCI.rpcMaxParams; for_i++) {
			routParams[for_i] = new SysRpcTcMethodParam();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(routineHnd);
		instanceVarAccess.write(out);
		out.writeInt(routParams_count);
		for (int for_i = 0; for_i < routParams_count; for_i++) {
			routParams[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		routineHnd = in.readInt();
		instanceVarAccess.read(in);
		routParams_count = in.readInt();
		for (int for_i = 0; for_i < routParams_count; for_i++) {
			routParams[for_i].read(in);
		}
	}
}