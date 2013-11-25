package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetValidTokensOut implements XDR {
	public byte[] kinds;  //fixed length of 100
	public int nrTokens;
	public boolean retVal;

	public SysRpcTcGetValidTokensOut () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBytes(kinds, 100);
		out.writeInt(nrTokens);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		kinds = in.readBytes(100);
		nrTokens = in.readInt();
		retVal = in.readBool();
	}
}