package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetExeUnitStatusListOut implements XDR {
	public int nrOfExeUnitHnd;
	public SysRpcTcExeUnitStatus[] status;  //variable length with max length of TCI.rpcChunkLen
	public int status_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetExeUnitStatusListOut () {
		status = new SysRpcTcExeUnitStatus[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			status[for_i] = new SysRpcTcExeUnitStatus();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfExeUnitHnd);
		out.writeInt(status_count);
		for (int for_i = 0; for_i < status_count; for_i++) {
			status[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfExeUnitHnd = in.readInt();
		status_count = in.readInt();
		for (int for_i = 0; for_i < status_count; for_i++) {
			status[for_i].read(in);
		}
		retVal = in.readBool();
	}
}