package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDeleteTextIn implements XDR {
	public int editHnd;
	public SysRpcTcPos beg;
	public SysRpcTcPos end;

	public SysRpcTcDeleteTextIn () {
		beg = new SysRpcTcPos();
		end = new SysRpcTcPos();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(editHnd);
		beg.write(out);
		end.write(out);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		editHnd = in.readInt();
		beg.read(in);
		end.read(in);
	}
}