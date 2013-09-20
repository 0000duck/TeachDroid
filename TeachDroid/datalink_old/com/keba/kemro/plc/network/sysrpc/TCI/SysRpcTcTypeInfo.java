package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;

public class SysRpcTcTypeInfo implements XDR {
	public int typeSize;
	public int baseTypeHnd;
	public SysRpcTcTypeKind kind;
	public int lowerBound;
	public int upperBound;
	public int lowerBoundConstHnd;
	public int upperBoundConstHnd;
	public int arraySizeConstHnd;
	public int baseUnitHnd;
	public int variantVarHnd;
	public int returnTypeHnd;
	public int arrayElemHnd;

	public SysRpcTcTypeInfo () {
		kind = new SysRpcTcTypeKind();
	}

	public void write (RPCOutputStream out) throws RPCException, IOException {
		out.writeInt(typeSize);
		out.writeInt(baseTypeHnd);
		kind.write(out);
		out.writeInt(lowerBound);
		out.writeInt(upperBound);
		out.writeInt(lowerBoundConstHnd);
		out.writeInt(upperBoundConstHnd);
		out.writeInt(arraySizeConstHnd);
		out.writeInt(baseUnitHnd);
		out.writeInt(variantVarHnd);
		out.writeInt(returnTypeHnd);
		out.writeInt(arrayElemHnd);
	}

	public void read (RPCInputStream in) throws RPCException, IOException {
		typeSize = in.readInt();
		baseTypeHnd = in.readInt();
		kind.read(in);
		lowerBound = in.readInt();
		upperBound = in.readInt();
		lowerBoundConstHnd = in.readInt();
		upperBoundConstHnd = in.readInt();
		arraySizeConstHnd = in.readInt();
		baseUnitHnd = in.readInt();
		variantVarHnd = in.readInt();
		returnTypeHnd = in.readInt();
		arrayElemHnd = in.readInt();
	}
}