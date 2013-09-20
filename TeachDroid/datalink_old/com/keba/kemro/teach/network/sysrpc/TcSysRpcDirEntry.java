package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcDirEntry extends TcDirEntry {
	protected static final SysRpcTcGetDirEntryInfoIn SysRpcTcGetDirEntryInfoIn = new SysRpcTcGetDirEntryInfoIn();
	protected static final SysRpcTcGetDirEntryInfoOut SysRpcTcGetDirEntryInfoOut = new SysRpcTcGetDirEntryInfoOut();

	TcSysRpcClient client;

	protected TcSysRpcDirEntry(String handle, TcSysRpcClient client) {
		super(handle);
		this.client = client;
	}

	protected String getHandle() {
		return super.getHandle();
	}

	protected static String convertHandleToDirEntryPath(String handle) {
		return TcDirEntry.convertHandleToDirEntryPath(handle);
	}

	protected static String convertDirEntryPathToHandle(String dirEntryPath) {
		return TcDirEntry.convertDirEntryPathToHandle(dirEntryPath);
	}

	protected void setDirEntryPath(String path) {
		super.setDirEntryPath(path);
	}

	protected boolean loadInfo() {
		if (!loaded) {
			try {
				synchronized (SysRpcTcGetDirEntryInfoIn) {
					SysRpcTcGetDirEntryInfoIn.dirEntryPath = handle;
					client.client.SysRpcTcGetDirEntryInfo_1(SysRpcTcGetDirEntryInfoIn, SysRpcTcGetDirEntryInfoOut);
					if (SysRpcTcGetDirEntryInfoOut.retVal) {
						setInfo(SysRpcTcGetDirEntryInfoOut.info);
						return true;
					}
					if (handle.equals("_system.tt")) {
						String path = getDirEntryPath();
						String handle = path.substring(1, path.length());
						SysRpcTcGetDirEntryInfoIn.dirEntryPath = handle;
						client.client.SysRpcTcGetDirEntryInfo_1(SysRpcTcGetDirEntryInfoIn, SysRpcTcGetDirEntryInfoOut);
						if (SysRpcTcGetDirEntryInfoOut.retVal) {
							setInfo(SysRpcTcGetDirEntryInfoOut.info);
							return true;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Disconnect in TcDirEntry - loadInfo: ");
			}
			return false;
		}
		return true;
	}

	protected void setInfo(SysRpcTcDirEntryInfo info) {
		loaded = true;
		name = info.name.toString();
		kind = info.kind.value;
		createdTime = (long) info.createTime * (long) 1000;
		modifiedTime = (long) info.modifyTime * (long) 1000;
		accessTime = (long) info.accessTime * (long) 1000;
		size = info.size;
		isGlobal = info.isGlobal;
		isSystem = name.equalsIgnoreCase("_system");
	}
}
