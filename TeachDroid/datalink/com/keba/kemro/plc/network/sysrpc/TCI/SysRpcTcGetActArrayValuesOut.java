package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetActArrayValuesOut implements XDR {
	public SysRpcTcValue[] buffer;  //variable length
	public int buffer_count; //countains the number of elements
	public int nrElems;
	public boolean retVal;

	public SysRpcTcGetActArrayValuesOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(buffer_count);
		for (int for_i = 0; for_i < buffer_count; for_i++) {
			buffer[for_i].write(out);
		}
		out.writeInt(nrElems);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {buffer_count = in.readInt();
		buffer = new SysRpcTcValue[buffer_count];
		for (int for_i = 0; for_i < buffer_count; for_i++) {
			buffer[for_i] = new SysRpcTcValue();
			buffer[for_i].read(in);
		}
		nrElems = in.readInt();
		retVal = in.readBool();
	}
}