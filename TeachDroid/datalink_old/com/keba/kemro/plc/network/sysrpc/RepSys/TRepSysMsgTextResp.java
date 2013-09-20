package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;

import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.RPCInputStream;
import com.keba.jrpc.rpc.RPCOutputStream;
import com.keba.jrpc.rpc.XDR;

public class TRepSysMsgTextResp implements XDR {
	public TRepSysKLinkTextFormat textFormat;
	public int[] text;  //variable length with max length of RepSys.cRepSysMaxMsgTextSize
	public int text_count; //countains the number of elements

	public TRepSysMsgTextResp () {
		textFormat = new TRepSysKLinkTextFormat();
		text = new int[RepSys.cRepSysMaxMsgTextSize];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		textFormat.write(out);
		out.writeInt(text_count);
		for (int for_i = 0; for_i < text_count; for_i++) {
			out.writeInt(text[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		textFormat.read(in);
		text_count = in.readInt();
		for (int for_i = 0; for_i < text_count; for_i++) {
			text[for_i] = in.readInt();
		}
	}
}