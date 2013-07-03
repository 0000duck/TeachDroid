package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysClassDesc implements XDR {
	public String mEntryName;
	public int mClassId;

	public TRepSysClassDesc () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(mEntryName);
		out.writeInt(mClassId);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mEntryName = in.readString();
		mClassId = in.readInt();
	}
}