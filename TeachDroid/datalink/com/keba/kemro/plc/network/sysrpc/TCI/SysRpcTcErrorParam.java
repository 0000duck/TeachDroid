package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcErrorParam implements XDR {
	public SysRpcTcErrorParamType errorType;
	public int iValue;
	public float fValue;
	public String sValue;

	public SysRpcTcErrorParam () {
		errorType = new SysRpcTcErrorParamType();
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