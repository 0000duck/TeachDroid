package com.keba.kemro.plc.network.sysrpc.RepSys;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class TRepSys_SetMsg_Param implements XDR {
	public int mType;
	// case RepSys.cRepSys_SetMsg_ParamType_Unused
	public int mUnusedParam;
	// case RepSys.cRepSys_SetMsg_ParamType_Int
	public int mInt32Param;
	// case RepSys.cRepSys_SetMsg_ParamType_LongLong
	public long mLongLongParam;
	// case RepSys.cRepSys_SetMsg_ParamType_Float
	public float mFloatParam;
	// case RepSys.cRepSys_SetMsg_ParamType_Double
	public double mDoubleParam;
	// case RepSys.cRepSys_SetMsg_ParamType_String
	public String mStringParam;
	// case RepSys.cRepSys_SetMsg_ParamType_WString
	public int[] mWStringParam;  //variable length with max length of 0
	public int mWStringParam_count; //countains the number of elements
	// case RepSys.cRepSys_SetMsg_ParamType_Memory
	public byte[] mMemParam;  //variable length with max length of 0
	public int mMemParam_count; //countains the number of elements

	public TRepSys_SetMsg_Param () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(mType);
	switch (mType) {
		case RepSys.cRepSys_SetMsg_ParamType_Unused: 
			out.writeInt(mUnusedParam);
			break;
		case RepSys.cRepSys_SetMsg_ParamType_Int: 
			out.writeInt(mInt32Param);
			break;
		case RepSys.cRepSys_SetMsg_ParamType_LongLong: 
			out.writeHyper(mLongLongParam);
			break;
		case RepSys.cRepSys_SetMsg_ParamType_Float: 
			out.writeFloat(mFloatParam);
			break;
		case RepSys.cRepSys_SetMsg_ParamType_Double: 
			out.writeDouble(mDoubleParam);
			break;
		case RepSys.cRepSys_SetMsg_ParamType_String: 
			out.writeString(mStringParam);
			break;
		case RepSys.cRepSys_SetMsg_ParamType_WString: 
			out.writeInt(mWStringParam_count);
			for (int for_i = 0; for_i < mWStringParam_count; for_i++) {
				out.writeInt(mWStringParam[for_i]);
			}
			break;
		case RepSys.cRepSys_SetMsg_ParamType_Memory: 
			out.writeBytes(mMemParam);
			break;
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		mType = in.readInt();
		switch (mType) {
		case RepSys.cRepSys_SetMsg_ParamType_Unused: 
		mUnusedParam = in.readInt();
	break;
		case RepSys.cRepSys_SetMsg_ParamType_Int: 
		mInt32Param = in.readInt();
	break;
		case RepSys.cRepSys_SetMsg_ParamType_LongLong: 
		mLongLongParam = in.readHyper();
	break;
		case RepSys.cRepSys_SetMsg_ParamType_Float: 
		mFloatParam = in.readFloat();
	break;
		case RepSys.cRepSys_SetMsg_ParamType_Double: 
		mDoubleParam = in.readDouble();
	break;
		case RepSys.cRepSys_SetMsg_ParamType_String: 
		mStringParam = in.readString();
	break;
		case RepSys.cRepSys_SetMsg_ParamType_WString: 
			mWStringParam_count = in.readInt();
			mWStringParam = new int[mWStringParam_count];
			for (int for_i = 0; for_i < mWStringParam_count; for_i++) {
				mWStringParam[for_i] = in.readInt();
			}
	break;
		case RepSys.cRepSys_SetMsg_ParamType_Memory: 
			mMemParam = in.readBytes();
			break;
		}
	}
}