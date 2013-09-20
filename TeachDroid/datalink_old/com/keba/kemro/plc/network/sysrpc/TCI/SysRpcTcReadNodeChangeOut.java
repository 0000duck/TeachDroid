package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcReadNodeChangeOut implements XDR {
	public SysRpcTcNodeChange[] changes;  //variable length with max length of TCI.rpcChunkLen
	public int changes_count; //countains the number of elements
	public int nrChanges;
	public boolean retVal;

	public SysRpcTcReadNodeChangeOut () {
		changes = new SysRpcTcNodeChange[TCI.rpcChunkLen];
		for (int for_i = 0; for_i < TCI.rpcChunkLen; for_i++) {
			changes[for_i] = new SysRpcTcNodeChange();
		}
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(changes_count);
		for (int for_i = 0; for_i < changes_count; for_i++) {
			changes[for_i].write(out);
		}
		out.writeInt(nrChanges);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		changes_count = in.readInt();
		for (int for_i = 0; for_i < changes_count; for_i++) {
			changes[for_i].read(in);
		}
		nrChanges = in.readInt();
		retVal = in.readBool();
	}
}