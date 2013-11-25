package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcErrorElem implements XDR {
	public RpcTcErrorKind errorKind;
	public int errorNr;
	public int nrParams;
	public RpcTcErrorParam[] errorParams;  //fixed length of cNrErrorParams

	public RpcTcErrorElem () {
		errorKind = new RpcTcErrorKind();
		errorParams = new RpcTcErrorParam[cNrErrorParams.value];
		for (int for_i = 0; for_i < cNrErrorParams.value; for_i++) {
			errorParams[for_i] = new RpcTcErrorParam();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		errorKind.write(out);
		out.writeInt(errorNr);
		out.writeInt(nrParams);
		for (int for_i = 0; for_i < cNrErrorParams.value; for_i++) {
			errorParams[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		errorKind.read(in);
		errorNr = in.readInt();
		nrParams = in.readInt();
		for (int for_i = 0; for_i < cNrErrorParams.value; for_i++) {
			errorParams[for_i].read(in);
		}
	}
}