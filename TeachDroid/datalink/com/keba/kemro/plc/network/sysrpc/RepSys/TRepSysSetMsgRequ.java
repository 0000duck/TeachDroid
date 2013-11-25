package com.keba.kemro.plc.network.sysrpc.RepSys;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class TRepSysSetMsgRequ implements XDR {
	public TRepSysNr mMsgData;
	public TRepSys_SetMsg_Param[] mParams;  //variable length
	public int mParams_count; //countains the number of elements

	public TRepSysSetMsgRequ () {
		mMsgData = new TRepSysNr();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		mMsgData.write(out);
		out.writeInt(mParams_count);
		for (int for_i = 0; for_i < mParams_count; for_i++) {
			mParams[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mMsgData.read(in);mParams_count = in.readInt();
		mParams = new TRepSys_SetMsg_Param[mParams_count];
		for (int for_i = 0; for_i < mParams_count; for_i++) {
			mParams[for_i] = new TRepSys_SetMsg_Param();
			mParams[for_i].read(in);
		}
	}
}