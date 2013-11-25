package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetWatchPointVarsOut implements XDR {
	public int[] exeUnitHnds;  //fixed length of TCI.rpcMaxWatchVars
	public SysRpcTcInstancePath[] instancePaths;  //fixed length of TCI.rpcMaxWatchVars
	public int[] wpVarHnds;  //fixed length of TCI.rpcMaxWatchVars
	public int nrWPVars;
	public boolean retVal;

	public SysRpcTcGetWatchPointVarsOut () {
		exeUnitHnds = new int[TCI.rpcMaxWatchVars];
		instancePaths = new SysRpcTcInstancePath[TCI.rpcMaxWatchVars];
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			instancePaths[for_i] = new SysRpcTcInstancePath();
		}
		wpVarHnds = new int[TCI.rpcMaxWatchVars];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			out.writeInt(exeUnitHnds[for_i]);
		}
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			instancePaths[for_i].write(out);
		}
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			out.writeInt(wpVarHnds[for_i]);
		}
		out.writeInt(nrWPVars);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			exeUnitHnds[for_i] = in.readInt();
		}
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			instancePaths[for_i].read(in);
		}
		for (int for_i = 0; for_i < TCI.rpcMaxWatchVars; for_i++) {
			wpVarHnds[for_i] = in.readInt();
		}
		nrWPVars = in.readInt();
		retVal = in.readBool();
	}
}