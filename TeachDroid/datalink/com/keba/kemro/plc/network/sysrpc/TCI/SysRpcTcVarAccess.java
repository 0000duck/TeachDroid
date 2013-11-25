package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcVarAccess implements XDR {
	public int varHandle;
	public int typeHandle;
	public int index;
	public int[] offsets;  //variable length with max length of TCI.cMaxOffsets
	public int offsets_count; //countains the number of elements

	public SysRpcTcVarAccess () {
		offsets = new int[TCI.cMaxOffsets];
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