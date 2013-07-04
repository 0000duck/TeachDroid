package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcInstancePath implements XDR {
	public int nrOfElems;
	public RpcTcInstancePathElem[] elems;  //variable length with max length of rpcMaxInstancePathElems
	public int elems_count; //countains the number of elements

	public RpcTcInstancePath () {
		elems = new RpcTcInstancePathElem[rpcMaxInstancePathElems.value];
		for (int for_i = 0; for_i < rpcMaxInstancePathElems.value; for_i++) {
			elems[for_i] = new RpcTcInstancePathElem();
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