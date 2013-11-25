package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReadProjectPathOut implements XDR {
	public String pathLocal;
	public String pathUNC;
	public boolean retVal;

	public SysRpcTcReadProjectPathOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(pathLocal);
		out.writeString(pathUNC);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		pathLocal = in.readString();
		pathUNC = in.readString();
		retVal = in.readBool();
	}
}