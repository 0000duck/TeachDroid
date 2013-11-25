package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;

public class TcRpcStructuralInvalidNode extends TcRpcStructuralNode implements TcStructuralInvalidNode {
   protected TcRpcStructuralInvalidNode (int handle, TcRpcClient client) {
      super(handle, client);
   }

}
