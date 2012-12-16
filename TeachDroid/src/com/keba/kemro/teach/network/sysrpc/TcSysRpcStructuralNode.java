package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.base.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcStructuralNode extends TcStructuralAbstractNode {
   private static final SysRpcTcGetNodeInfoIn SysRpcTcGetNodeInfoIn = new SysRpcTcGetNodeInfoIn();
   private static final SysRpcTcGetNodeInfoOut SysRpcTcGetNodeInfoOut = new SysRpcTcGetNodeInfoOut();

   TcSysRpcClient client;
   
	TcSysRpcStructuralNode (int handle, TcSysRpcClient client) {
		super(handle);
		this.client = client;
   }

   protected int getHandle () {
      return super.getHandle();
   }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object o) {
     if (this == o) {
    	 return true;
     }
     if (!(o instanceof TcSysRpcStructuralNode)) {
    	 return false;
     }
  	 return getHandle() == ((TcSysRpcStructuralNode) o).getHandle();
   }
   
   protected void setHasAttributes (boolean b) {
   	super.setHasAttributes(b);
   }
   
	protected boolean loadInfo () {
      try {
         synchronized (SysRpcTcGetNodeInfoIn) {
         	SysRpcTcGetNodeInfoIn.scopeHnd = getHandle();
            client.client.SysRpcTcGetNodeInfo_1(SysRpcTcGetNodeInfoIn, SysRpcTcGetNodeInfoOut);
            if (SysRpcTcGetNodeInfoOut.retVal) {
               setInfo(SysRpcTcGetNodeInfoOut.info, null);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralNode - loadInfo: ");
      }
      return false;
   }

   void setInfo (SysRpcTcNodeInfo info, TcStructuralAbstractNode parent) {
      setLoaded(true);
      name = info.elemName.toString();
      nodeKind = (byte) info.kind.value;
      setUserNode((info.attr & SysRpcTcNodeAttr.rpcUserNodeAttr) == SysRpcTcNodeAttr.rpcUserNodeAttr);
      setAbstract((info.attr & SysRpcTcNodeAttr.rpcIsAbstractAttr) == SysRpcTcNodeAttr.rpcIsAbstractAttr);
      setExportVariable((info.attr & SysRpcTcNodeAttr.rpcIsExportVarAttr) == SysRpcTcNodeAttr.rpcIsExportVarAttr);
      setHasAttributes((info.attr & SysRpcTcNodeAttr.rpcHasAttributesAttr) == SysRpcTcNodeAttr.rpcHasAttributesAttr);
      this.parent = parent;
      int declHnd = info.declHnd;
      if (this.parent == null) {
         this.parent =  client.cache.getFromDirEntryCache(info.upperHnd);
         if (this.parent == null) {
            this.parent = client.cache.getFromCache(info.upperHnd);
            if (this.parent == null) {
               this.parent = ((TcSysRpcStructuralModel) client.structure).createNode(info.upperHnd);
               if (
                   (this.parent != null) &&
                   ((this.parent.getKind() == ROOT) || (this.parent.getKind() == GLOBAL) ||
                   (this.parent.getKind() == PROJECT) || (this.parent.getKind() == PROGRAM))) {
               	client.cache.putToDirEntryCache(this.parent);
               } else if ((this.parent != null) && (this.parent instanceof TcSysRpcStructuralTypeNode)) {
               	client.cache.putToCache(this.parent);
                  if (((TcSysRpcStructuralTypeNode) this.parent).getBaseType() == null) {
                  	client.cache.putToDirEntryCache(this.parent);
                  }
               }
            }
         }
      }
      if (declHnd == handle) {
         dirEntryNode = this;
      } else if (declHnd != 0) {
         dirEntryNode = client.cache.getFromDirEntryCache(declHnd);
         if (dirEntryNode == null) {
            dirEntryNode = client.cache.getFromCache(declHnd);
            if (dirEntryNode == null) {
               dirEntryNode = ((TcSysRpcStructuralModel) client.structure).createNode(declHnd);
               if (dirEntryNode != null) {
               	client.cache.putToDirEntryCache(dirEntryNode);
                  if (dirEntryNode.getKind() == TYPE) {
                  	client.cache.putToCache(dirEntryNode);
                  }
               }
            } else {
            	client.cache.putToDirEntryCache(dirEntryNode);
            }
         }
      }
   }

}
