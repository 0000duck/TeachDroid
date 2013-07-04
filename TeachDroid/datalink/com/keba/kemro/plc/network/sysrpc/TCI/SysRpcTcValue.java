package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcValue implements XDR {
	public boolean isValid;
	public boolean bValue;
	public int i8Value;
	public int i16Value;
	public int i32Value;
	public long i64Value;
	public float fValue;
	public String sValue;

	public SysRpcTcValue () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeBool(isValid);
		out.writeBool(bValue);
		out.writeInt(i8Value);
		out.writeInt(i16Value);
		out.writeInt(i32Value);
		out.writeHyper(i64Value);
		out.writeFloat(fValue);
		out.writeString(sValue);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		isValid = in.readBool();
		bValue = in.readBool();
		i8Value = in.readInt();
		i16Value = in.readInt();
		i32Value = in.readInt();
		i64Value = in.readHyper();
		fValue = in.readFloat();
		sValue = in.readString();
	}
}