package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetExecInfoOut implements XDR {
	public boolean retVal;
	public int nrOfInfos;
	public SysRpcTcExecInfoElem[] execInfos;  //variable length with max length of 10
	public int execInfos_count; //countains the number of elements

	public SysRpcTcGetExecInfoOut () {
		execInfos = new SysRpcTcExecInfoElem[10];
		for (int for_i = 0; for_i < 10; for_i++) {
			execInfos[for_i] = new SysRpcTcExecInfoElem();
		}
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
		nrOfInfos = in.readInt();
		execInfos_count = in.readInt();
		for (int for_i = 0; for_i < execInfos_count; for_i++) {
			execInfos[for_i].read(in);
		}
	}
}