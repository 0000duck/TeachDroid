package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class RpcTcGetSymbolTextRangeOut implements XDR {
	public RpcTcPos beg;
	public RpcTcPos end;
	public boolean retVal;

	public RpcTcGetSymbolTextRangeOut () {
		beg = new RpcTcPos();
		end = new RpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		beg.write(out);
		end.write(out);
		out.writeBool(retVal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		beg.read(in);
		end.read(in);
		retVal = in.readBool();
	}
}