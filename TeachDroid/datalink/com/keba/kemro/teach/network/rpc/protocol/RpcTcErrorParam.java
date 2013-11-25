package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcErrorParam implements XDR {
	public RpcTcErrorParamType errorType;
	public int iValue;
	public float fValue;
	public String sValue;

	public RpcTcErrorParam () {
		errorType = new RpcTcErrorParamType();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		errorType.write(out);
		out.writeInt(iValue);
		out.writeFloat(fValue);
		out.writeString(sValue);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		errorType.read(in);
		iValue = in.readInt();
		fValue = in.readFloat();
		sValue = in.readString();
	}
}