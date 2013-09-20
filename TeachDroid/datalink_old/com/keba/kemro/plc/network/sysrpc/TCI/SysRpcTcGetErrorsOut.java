package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetErrorsOut implements XDR {
	public SysRpcTcErrorElem[] errors;  //variable length with max length of TCI.cMaxErrors
	public int errors_count; //countains the number of elements
	public boolean retVal;

	public SysRpcTcGetErrorsOut () {
		errors = new SysRpcTcErrorElem[TCI.cMaxErrors];
		for (int for_i = 0; for_i < TCI.cMaxErrors; for_i++) {
			errors[for_i] = new SysRpcTcErrorElem();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(errors_count);
		for (int for_i = 0; for_i < errors_count; for_i++) {
			errors[for_i].write(out);
		}
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		errors_count = in.readInt();
		for (int for_i = 0; for_i < errors_count; for_i++) {
			errors[for_i].read(in);
		}
		retVal = in.readBool();
	}
}