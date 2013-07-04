package com.keba.jrpc.rpc;

import java.io.*;
import java.net.*;

public class PortMapper extends RPCClient {
	public static final int PMAP_PROG = 100000;
	public int prog;

	public PortMapper (String host, int prog, int version) throws RPCException, UnknownHostException, IOException {
		super(host, prog, version);
		this.prog = prog;
	}

	public PortMapper (String host) throws RPCException, UnknownHostException, IOException {
		super(host, PMAP_PROG, PMAP_VERSION);
		this.prog = PMAP_PROG;
	}

	public PortMapper (String host, int port, int prog, int version) throws RPCException, UnknownHostException, IOException {
		super(host, port);
		this.prog = prog;
	}

	public PortMapper (String host, int port) throws RPCException, UnknownHostException, IOException {
		super(host, port);
		this.prog = PMAP_PROG;
	}

	public static final int PMAP_VERSION = 2;
	public static final XDR[] args = new XDR[128];
	public static int args_length;
	public static final XDRInt retVal_Int = new XDRInt();
	public static final XDRHyper retVal_Hyper = new XDRHyper();
	public static final XDRFloat retVal_Float = new XDRFloat();
	public static final XDRDouble retVal_Double = new XDRDouble();
	public static final XDRBool retVal_Bool = new XDRBool();
	public static final XDRString retVal_String = new XDRString();
	
	public boolean set_2 (mapping arg_1) throws RPCException, IOException {
		synchronized (args) {;
			args_length = 1;
			args[0] = arg_1;
			call(prog, PMAP_VERSION, 1, retVal_Bool, args_length, args);
			return retVal_Bool.value;
		}
	}
	public boolean unset_2 (mapping arg_1) throws RPCException, IOException {
		synchronized (args) {;
			args_length = 1;
			args[0] = arg_1;
			call(prog, PMAP_VERSION, 2, retVal_Bool, args_length, args);
			return retVal_Bool.value;
		}
	}
	public int getPort_2 (mapping arg_1) throws RPCException, IOException {
		synchronized (args) {;
			args_length = 1;
			args[0] = arg_1;
			call(prog, PMAP_VERSION, 3, retVal_Int, args_length, args);
			return retVal_Int.value;
		}
	}
}