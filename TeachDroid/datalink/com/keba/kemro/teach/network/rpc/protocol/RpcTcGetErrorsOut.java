package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetErrorsOut implements XDR {
	public RpcTcErrorElem[] errors;  //variable length with max length of cMaxErrors
	public int errors_count; //countains the number of elements
	public boolean retVal;

	public RpcTcGetErrorsOut () {
		errors = new RpcTcErrorElem[cMaxErrors.value];
		for (int for_i = 0; for_i < cMaxErrors.value; for_i++) {
			errors[for_i] = new RpcTcErrorElem();
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