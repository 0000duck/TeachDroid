package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetCodePointsOut implements XDR {
	public int[] lineNrs;  //fixed length of TCI.rpcMaxCodePoints
	public SysRpcTcCodePointKind[] kinds;  //fixed length of TCI.rpcMaxCodePoints
	public boolean[] active;  //fixed length of TCI.rpcMaxCodePoints
	public int nrCodePoints;
	public boolean retVal;

	public SysRpcTcGetCodePointsOut () {
		lineNrs = new int[TCI.rpcMaxCodePoints];
		kinds = new SysRpcTcCodePointKind[TCI.rpcMaxCodePoints];
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			kinds[for_i] = new SysRpcTcCodePointKind();
		}
		active = new boolean[TCI.rpcMaxCodePoints];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			out.writeInt(lineNrs[for_i]);
		}
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			kinds[for_i].write(out);
		}
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			out.writeBool(active[for_i]);
		}
		out.writeInt(nrCodePoints);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			lineNrs[for_i] = in.readInt();
		}
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			kinds[for_i].read(in);
		}
		for (int for_i = 0; for_i < TCI.rpcMaxCodePoints; for_i++) {
			active[for_i] = in.readBool();
		}
		nrCodePoints = in.readInt();
		retVal = in.readBool();
	}
}