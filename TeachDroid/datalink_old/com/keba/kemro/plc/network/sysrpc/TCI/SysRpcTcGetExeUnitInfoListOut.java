package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetExeUnitInfoListOut implements XDR {
	public int nrOfExeUnitHnd;
	public SysRpcTcExeUnitInfo[] info;  //variable length with max length of TCI.rpcChunkLen
	public int info_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetExeUnitInfoListOut () {
		info = new SysRpcTcExeUnitInfo[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			info[for_i] = new SysRpcTcExeUnitInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfExeUnitHnd);
		out.writeInt(info_count);
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfExeUnitHnd = in.readInt();
		info_count = in.readInt();
		for (int for_i = 0; for_i < info_count; for_i++) {
			info[for_i].read(in);
		}
		retVal = in.readBool();
	}
}