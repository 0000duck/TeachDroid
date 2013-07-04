package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysFormatString implements XDR {
	public String mFormatString;

	public TRepSysFormatString () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(mFormatString);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mFormatString = in.readString();
	}
}