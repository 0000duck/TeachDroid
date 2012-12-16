package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetExecInfoOut implements XDR {
	public boolean retVal;
	public int nrOfInfos;
	public RpcTcExecInfoElem[] execInfos;  //variable length
	public int execInfos_count; //countains the number of elements

	public RpcTcGetExecInfoOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(retVal);
		out.writeInt(nrOfInfos);
		out.writeInt(execInfos_count);
		for (int for_i = 0; for_i < execInfos_count; for_i++) {
			execInfos[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		retVal = in.readBool();
		nrOfInfos = in.readInt();execInfos_count = in.readInt();
		execInfos = new RpcTcExecInfoElem[execInfos_count];
		for (int for_i = 0; for_i < execInfos_count; for_i++) {
			execInfos[for_i] = new RpcTcExecInfoElem();
			execInfos[for_i].read(in);
		}
	}
}