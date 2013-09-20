package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgParamList implements XDR {
	public int mNrParams;
	public TRepSysFormatString[] mFormat;  //variable length with max length of RepSys.cRepSysNrErrorParams
	public int mFormat_count; //countains the number of elements
	public TRepSysMsgParamUnion[] mParam;  //variable length with max length of RepSys.cRepSysNrErrorParams
	public int mParam_count; //countains the number of elements
	public int[] mString;  //variable length with max length of RepSys.cRepSysErrorStringBufferSize
	public int mString_count; //countains the number of elements

	public TRepSysMsgParamList () {
		mFormat = new TRepSysFormatString[RepSys.cRepSysNrErrorParams];
		for (int for_i = 0; for_i < RepSys.cRepSysNrErrorParams; for_i++) {
			mFormat[for_i] = new TRepSysFormatString();
		}
		mParam = new TRepSysMsgParamUnion[RepSys.cRepSysNrErrorParams];
		for (int for_i = 0; for_i < RepSys.cRepSysNrErrorParams; for_i++) {
			mParam[for_i] = new TRepSysMsgParamUnion();
		}
		mString = new int[RepSys.cRepSysErrorStringBufferSize];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(mNrParams);
		out.writeInt(mFormat_count);
		for (int for_i = 0; for_i < mFormat_count; for_i++) {
			mFormat[for_i].write(out);
		}
		out.writeInt(mParam_count);
		for (int for_i = 0; for_i < mParam_count; for_i++) {
			mParam[for_i].write(out);
		}
		out.writeInt(mString_count);
		for (int for_i = 0; for_i < mString_count; for_i++) {
			out.writeInt(mString[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mNrParams = in.readInt();
		mFormat_count = in.readInt();
		for (int for_i = 0; for_i < mFormat_count; for_i++) {
			mFormat[for_i].read(in);
		}
		mParam_count = in.readInt();
		for (int for_i = 0; for_i < mParam_count; for_i++) {
			mParam[for_i].read(in);
		}
		mString_count = in.readInt();
		for (int for_i = 0; for_i < mString_count; for_i++) {
			mString[for_i] = in.readInt();
		}
	}
}