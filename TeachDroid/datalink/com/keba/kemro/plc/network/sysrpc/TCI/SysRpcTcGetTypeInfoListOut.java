package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetTypeInfoListOut implements XDR {
	public int nrOfInfos;
	public SysRpcTcTypeInfo[] infos;  //variable length with max length of TCI.rpcChunkLen
	public int infos_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetTypeInfoListOut () {
		infos = new SysRpcTcTypeInfo[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			infos[for_i] = new SysRpcTcTypeInfo();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfInfos);
		out.writeInt(infos_count);
		for (int for_i = 0; for_i < infos_count; for_i++) {
			infos[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfInfos = in.readInt();
		infos_count = in.readInt();
		for (int for_i = 0; for_i < infos_count; for_i++) {
			infos[for_i].read(in);
		}
		retVal = in.readBool();
	}
}