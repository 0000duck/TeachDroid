package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetCodePointsOut implements XDR {
	public int[] lineNrs;  //fixed length of rpcMaxCodePoints
	public RpcTcCodePointKind[] kinds;  //fixed length of rpcMaxCodePoints
	public boolean[] active;  //fixed length of rpcMaxCodePoints
	public int nrCodePoints;
	public boolean retVal;

	public RpcTcGetCodePointsOut () {
		lineNrs = new int[rpcMaxCodePoints.value];
		kinds = new RpcTcCodePointKind[rpcMaxCodePoints.value];
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			kinds[for_i] = new RpcTcCodePointKind();
		}
		active = new boolean[rpcMaxCodePoints.value];
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			out.writeInt(lineNrs[for_i]);
		}
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			kinds[for_i].write(out);
		}
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			out.writeBool(active[for_i]);
		}
		out.writeInt(nrCodePoints);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			lineNrs[for_i] = in.readInt();
		}
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			kinds[for_i].read(in);
		}
		for (int for_i = 0; for_i < rpcMaxCodePoints.value; for_i++) {
			active[for_i] = in.readBool();
		}
		nrCodePoints = in.readInt();
		retVal = in.readBool();
	}
}