package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetMemDumpOut implements XDR {
	public int[] buffer;  //variable length
	public int buffer_count; //countains the number of elements
	public int nrBytes;
	public boolean bigEndian;
	public boolean retVal;

	public SysRpcTcGetMemDumpOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(buffer_count);
		for (int for_i = 0; for_i < buffer_count; for_i++) {
			out.writeInt(buffer[for_i]);
		}
		out.writeInt(nrBytes);
		out.writeBool(bigEndian);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {buffer_count = in.readInt();
		buffer = new int[buffer_count];
		for (int for_i = 0; for_i < buffer_count; for_i++) {
			buffer[for_i] = in.readInt();
		}
		nrBytes = in.readInt();
		bigEndian = in.readBool();
		retVal = in.readBool();
	}
}