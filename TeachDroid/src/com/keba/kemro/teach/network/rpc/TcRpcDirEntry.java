package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcDirEntry extends TcDirEntry {
   protected static final RpcTcGetDirEntryInfoIn rpcTcGetDirEntryInfoIn = new RpcTcGetDirEntryInfoIn();
   protected static final RpcTcGetDirEntryInfoOut rpcTcGetDirEntryInfoOut = new RpcTcGetDirEntryInfoOut();

   TcRpcClient client;
   
	protected TcRpcDirEntry (String handle, TcRpcClient client) {
		super(handle);
		this.client = client;
	}
	protected String getHandle () {
		return super.getHandle();
	}
	protected static String convertHandleToDirEntryPath (String handle) {
		return TcDirEntry.convertHandleToDirEntryPath(handle);
	}
	protected static String convertDirEntryPathToHandle (String dirEntryPath) {
		return TcDirEntry.convertDirEntryPathToHandle (dirEntryPath);
	}
	protected void setDirEntryPath (String path) {
		super.setDirEntryPath(path);
	}
	protected boolean loadInfo() {
	   if (!loaded) {
         try {
            synchronized (rpcTcGetDirEntryInfoIn) {
               rpcTcGetDirEntryInfoIn.dirEntryPath = handle;
               client.client.RpcTcGetDirEntryInfo_1(rpcTcGetDirEntryInfoIn, rpcTcGetDirEntryInfoOut);
               if (rpcTcGetDirEntryInfoOut.retVal) {
                  setInfo(rpcTcGetDirEntryInfoOut.info);
                  return true;
               }
               if (handle.equals("_system.tt")) {
               	String path = getDirEntryPath();
               	String handle = path.substring(1, path.length());
                  rpcTcGetDirEntryInfoIn.dirEntryPath = handle;
                  client.client.RpcTcGetDirEntryInfo_1(rpcTcGetDirEntryInfoIn, rpcTcGetDirEntryInfoOut);
                  if (rpcTcGetDirEntryInfoOut.retVal) {
                     setInfo(rpcTcGetDirEntryInfoOut.info);
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

	protected void setInfo(RpcTcDirEntryInfo info) {
	   loaded = true;
	   name = info.name;
	   kind = info.kind.value;	
	   createdTime = (long) info.createTime * (long) 1000;
	   modifiedTime = (long) info.modifyTime * (long) 1000;
	   accessTime = (long) info.accessTime * (long) 1000;
	   size = info.size;
	   isGlobal = info.isGlobal;
	   isSystem = name.equalsIgnoreCase("_system");
	}
}
