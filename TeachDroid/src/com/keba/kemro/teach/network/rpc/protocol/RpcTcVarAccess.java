package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcVarAccess implements XDR {
	public int varHandle;
	public int typeHandle;
	public int index;
	public int[] offsets;  //variable length with max length of cMaxOffsets
	public int offsets_count; //countains the number of elements

	public RpcTcVarAccess () {
		offsets = new int[cMaxOffsets.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(varHandle);
		out.writeInt(typeHandle);
		out.writeInt(index);
		out.writeInt(offsets_count);
		for (int for_i = 0; for_i < offsets_count; for_i++) {
			out.writeInt(offsets[for_i]);
		}
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		varHandle = in.readInt();
		typeHandle = in.readInt();
		index = in.readInt();
		offsets_count = in.readInt();
		for (int for_i = 0; for_i < offsets_count; for_i++) {
			offsets[for_i] = in.readInt();
		}
	}
}