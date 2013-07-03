package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgEntry implements XDR {
	public int mIndex;
	public TRepSysFlag mFlag;
	public TRepSysTime mTime;
	public int mCpuId;
	public TRepSysNr mNr;
	public TRepSysMsgParamList mParam;

	public TRepSysMsgEntry () {
		mFlag = new TRepSysFlag();
		mTime = new TRepSysTime();
		mNr = new TRepSysNr();
		mParam = new TRepSysMsgParamList();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(mIndex);
		mFlag.write(out);
		mTime.write(out);
		out.writeInt(mCpuId);
		mNr.write(out);
		mParam.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mIndex = in.readInt();
		mFlag.read(in);
		mTime.read(in);
		mCpuId = in.readInt();
		mNr.read(in);
		mParam.read(in);
	}
}