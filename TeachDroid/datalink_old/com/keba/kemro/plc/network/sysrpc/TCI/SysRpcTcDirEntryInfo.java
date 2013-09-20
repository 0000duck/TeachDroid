package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcDirEntryInfo implements XDR {
	public String name;
	public SysRpcTcDirEntryKind kind;
	public int size;
	public int attr;
	public int createTime;
	public int modifyTime;
	public int accessTime;
	public boolean isGlobal;

	public SysRpcTcDirEntryInfo () {
		kind = new SysRpcTcDirEntryKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeString(name);
		kind.write(out);
		out.writeInt(size);
		out.writeInt(attr);
		out.writeInt(createTime);
		out.writeInt(modifyTime);
		out.writeInt(accessTime);
		out.writeBool(isGlobal);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		name = in.readString();
		kind.read(in);
		size = in.readInt();
		attr = in.readInt();
		createTime = in.readInt();
		modifyTime = in.readInt();
		accessTime = in.readInt();
		isGlobal = in.readBool();
	}
}