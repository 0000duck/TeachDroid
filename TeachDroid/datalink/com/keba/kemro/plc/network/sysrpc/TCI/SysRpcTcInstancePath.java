package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcInstancePath implements XDR {
	public int nrOfElems;
	public SysRpcTcInstancePathElem[] elems;  //variable length with max length of TCI.rpcMaxInstancePathElems
	public int elems_count; //countains the number of elements

	public SysRpcTcInstancePath () {
		elems = new SysRpcTcInstancePathElem[TCI.rpcMaxInstancePathElems];
		for (int for_i = 0; for_i < TCI.rpcMaxInstancePathElems; for_i++) {
			elems[for_i] = new SysRpcTcInstancePathElem();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(nrOfElems);
		out.writeInt(elems_count);
		for (int for_i = 0; for_i < elems_count; for_i++) {
			elems[for_i].write(out);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		nrOfElems = in.readInt();
		elems_count = in.readInt();
		for (int for_i = 0; for_i < elems_count; for_i++) {
			elems[for_i].read(in);
		}
	}
}