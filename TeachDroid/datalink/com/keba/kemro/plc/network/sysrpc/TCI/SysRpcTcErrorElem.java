package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcErrorElem implements XDR {
	public SysRpcTcErrorKind errorKind;
	public int errorNr;
	public int nrParams;
	public SysRpcTcErrorParam[] errorParams;  //fixed length of TCI.cNrErrorParams

	public SysRpcTcErrorElem () {
		errorKind = new SysRpcTcErrorKind();
		errorParams = new SysRpcTcErrorParam[TCI.cNrErrorParams];
		for (int for_i = 0; for_i < TCI.cNrErrorParams; for_i++) {
			errorParams[for_i] = new SysRpcTcErrorParam();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		errorKind.write(out);
		out.writeInt(errorNr);
		out.writeInt(nrParams);
		for (int for_i = 0; for_i < TCI.cNrErrorParams; for_i++) {
			errorParams[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		errorKind.read(in);
		errorNr = in.readInt();
		nrParams = in.readInt();
		for (int for_i = 0; for_i < TCI.cNrErrorParams; for_i++) {
			errorParams[for_i].read(in);
		}
	}
}