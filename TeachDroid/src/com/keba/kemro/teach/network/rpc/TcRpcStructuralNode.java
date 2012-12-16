package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.base.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcStructuralNode extends TcStructuralAbstractNode {
   private static final RpcTcGetNodeInfoIn rpcTcGetNodeInfoIn = new RpcTcGetNodeInfoIn();
   private static final RpcTcGetNodeInfoOut rpcTcGetNodeInfoOut = new RpcTcGetNodeInfoOut();

   TcRpcClient client;
   
	TcRpcStructuralNode (int handle, TcRpcClient client) {
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
     if (!(o instanceof TcRpcStructuralNode)) {
    	 return false;
     }
  	 return getHandle() == ((TcRpcStructuralNode) o).getHandle();
   }
   
   protected void setHasAttributes (boolean b) {
   	super.setHasAttributes(b);
   }
   
	protected boolean loadInfo () {
      try {
         synchronized (rpcTcGetNodeInfoIn) {
            rpcTcGetNodeInfoIn.scopeHnd = getHandle();
            client.client.RpcTcGetNodeInfo_1(rpcTcGetNodeInfoIn, rpcTcGetNodeInfoOut);
            if (rpcTcGetNodeInfoOut.retVal) {
               setInfo(rpcTcGetNodeInfoOut.info, null);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralNode - loadInfo: ");
      }
      return false;
   }

   void setInfo (RpcTcNodeInfo info, TcStructuralAbstractNode parent) {
      setLoaded(true);
      name = info.elemName;
      nodeKind = (byte) info.kind.value;
      setUserNode((info.attr & RpcTcNodeAttr.rpcUserNodeAttr) == RpcTcNodeAttr.rpcUserNodeAttr);
      setAbstract((info.attr & RpcTcNodeAttr.rpcIsAbstractAttr) == RpcTcNodeAttr.rpcIsAbstractAttr);
      setExportVariable((info.attr & RpcTcNodeAttr.rpcIsExportVarAttr) == RpcTcNodeAttr.rpcIsExportVarAttr);
      setHasAttributes((info.attr & RpcTcNodeAttr.rpcHasAttributesAttr) == RpcTcNodeAttr.rpcHasAttributesAttr);
      this.parent = parent;
      int declHnd = info.declHnd;
      if (this.parent == null) {
         this.parent =  client.cache.getFromDirEntryCache(info.upperHnd);
         if (this.parent == null) {
            this.parent = client.cache.getFromCache(info.upperHnd);
            if (this.parent == null) {
               this.parent = ((TcRpcStructuralModel) client.structure).createNode(info.upperHnd);
               if (
                   (this.parent != null) &&
                   ((this.parent.getKind() == ROOT) || (this.parent.getKind() == GLOBAL) ||
                   (this.parent.getKind() == PROJECT) || (this.parent.getKind() == PROGRAM))) {
               	client.cache.putToDirEntryCache(this.parent);
               } else if ((this.parent != null) && (this.parent instanceof TcRpcStructuralTypeNode)) {
               	client.cache.putToCache(this.parent);
                  if (((TcRpcStructuralTypeNode) this.parent).getBaseType() == null) {
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
               dirEntryNode = ((TcRpcStructuralModel) client.structure).createNode(declHnd);
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
