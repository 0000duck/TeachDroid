package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysClassIdMultiResp implements XDR {
	public int[] classIdArr;  //variable length with max length of RepSys.cRepSysMaxClasses
	public int classIdArr_count; //countains the number of elements
	public int classCnt;

	public TRepSysClassIdMultiResp () {
		classIdArr = new int[RepSys.cRepSysMaxClasses];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(classIdArr_count);
		for (int for_i = 0; for_i < classIdArr_count; for_i++) {
			out.writeInt(classIdArr[for_i]);
		}
		out.writeInt(classCnt);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		classIdArr_count = in.readInt();
		for (int for_i = 0; for_i < classIdArr_count; for_i++) {
			classIdArr[for_i] = in.readInt();
		}
		classCnt = in.readInt();
	}
}