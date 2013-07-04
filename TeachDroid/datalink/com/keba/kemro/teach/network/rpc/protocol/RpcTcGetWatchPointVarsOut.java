package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetWatchPointVarsOut implements XDR {
	public int[] exeUnitHnds;  //fixed length of rpcMaxWatchVars
	public RpcTcInstancePath[] instancePaths;  //fixed length of rpcMaxWatchVars
	public int[] wpVarHnds;  //fixed length of rpcMaxWatchVars
	public int nrWPVars;
	public boolean retVal;

	public RpcTcGetWatchPointVarsOut () {
		exeUnitHnds = new int[rpcMaxWatchVars.value];
		instancePaths = new RpcTcInstancePath[rpcMaxWatchVars.value];
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			instancePaths[for_i] = new RpcTcInstancePath();
		}
		wpVarHnds = new int[rpcMaxWatchVars.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			out.writeInt(exeUnitHnds[for_i]);
		}
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			instancePaths[for_i].write(out);
		}
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			out.writeInt(wpVarHnds[for_i]);
		}
		out.writeInt(nrWPVars);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			exeUnitHnds[for_i] = in.readInt();
		}
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			instancePaths[for_i].read(in);
		}
		for (int for_i = 0; for_i < rpcMaxWatchVars.value; for_i++) {
			wpVarHnds[for_i] = in.readInt();
		}
		nrWPVars = in.readInt();
		retVal = in.readBool();
	}
}