package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgEntryMultiRespEx implements XDR {
	public TRepSysMsgEntryResp[] value;  //variable length
	public int value_count; //countains the number of elements

	public TRepSysMsgEntryMultiRespEx () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(value_count);
		for (int for_i = 0; for_i < value_count; for_i++) {
			value[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {value_count = in.readInt();
		value = new TRepSysMsgEntryResp[value_count];
		for (int for_i = 0; for_i < value_count; for_i++) {
			value[for_i] = new TRepSysMsgEntryResp();
			value[for_i].read(in);
		}
	}
}