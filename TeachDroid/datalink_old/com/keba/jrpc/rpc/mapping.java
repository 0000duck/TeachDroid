package com.keba.jrpc.rpc;

import java.io.*;

public class mapping implements XDR {
	public int prog;
	public int vers;
	public int prot;
	public int port;

	public mapping () {
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(prog);
		out.writeInt(vers);
		out.writeInt(prot);
		out.writeInt(port);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		prog = in.readInt();
		vers = in.readInt();
		prot = in.readInt();
		port = in.readInt();
	}
}