package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcGetSymbolTextRangeOut implements XDR {
	public SysRpcTcPos beg;
	public SysRpcTcPos end;
	public boolean retVal;

	public SysRpcTcGetSymbolTextRangeOut () {
		beg = new SysRpcTcPos();
		end = new SysRpcTcPos();
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