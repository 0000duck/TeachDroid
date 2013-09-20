package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;

public class TcSysRpcStructuralInvalidNode extends TcSysRpcStructuralNode implements TcStructuralInvalidNode {
   protected TcSysRpcStructuralInvalidNode (int handle, TcSysRpcClient client) {
      super(handle, client);
   }

}
