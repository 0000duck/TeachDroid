package com.keba.kemro.plc.network.sysrpc.RepSys;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class TRepSysSetMsgResp implements XDR {
	public int mMasterResult;
	public int[] mParamsResult;  //variable length
	public int mParamsResult_count; //countains the number of elements

	public TRepSysSetMsgResp () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(mMasterResult);
		out.writeInt(mParamsResult_count);
		for (int for_i = 0; for_i < mParamsResult_count; for_i++) {
			out.writeInt(mParamsResult[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mMasterResult = in.readInt();mParamsResult_count = in.readInt();
		mParamsResult = new int[mParamsResult_count];
		for (int for_i = 0; for_i < mParamsResult_count; for_i++) {
			mParamsResult[for_i] = in.readInt();
		}
	}
}