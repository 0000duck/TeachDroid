package com.keba.jrpc.rpc;

import java.io.*;
import java.net.*;

public abstract class RPCServer {
   protected static final int RPC_VERSION = 2;
   // Message Type
   protected static final int CALL = 0;
   protected static final int REPLY = 1;
   // Reply State
   protected static final int MSG_ACCEPTED = 0;
   protected static final int MSG_DENIED   = 1;
   // Accept State
   protected static final int SUCCESS       = 0;  /* RPC executed successfully             */
   protected static final int PROG_UNAVAIL  = 1;  /* remote hasn't exported program        */
   protected static final int PROG_MISMATCH = 2;  /* remote can't support version #        */
   protected static final int PROC_UNAVAIL  = 3;  /* program can't support procedure       */
   protected static final int GARBAGE_ARGS  = 4;  /* procedure can't decode params         */
   protected static final int SYSTEM_ERR    = 5;  /* errors like memory allocation failure */
   // Denied State
   protected static final int RPC_MISMATCH  = 0;  /* RPC version number != 2          */
   protected static final int AUTH_ERROR    = 1;  /* remote can't authenticate caller */
   // Authentication
   protected static final int AUTH_NONE     = 0;
   protected static final int AUTH_SYS      = 1;
   protected static final int AUTH_SHORT    = 2;
   // Authentication
   protected static final int AUTH_OK       = 0;      /* success                          */
   // Authentication failed at remote end
   protected static final int AUTH_BADCRED      = 1;  /* bad credential (seal broken)     */
   protected static final int AUTH_REJECTEDCRED = 2;  /* client must begin new session    */
   protected static final int AUTH_BADVERF      = 3;  /* bad verifier (seal broken)       */
   protected static final int AUTH_REJECTEDVERF = 4;  /* verifier expired or replayed     */
   protected static final int AUTH_TOOWEAK      = 5;  /* rejected for security reasons    */
   // Authentication failed locally
   protected static final int AUTH_INVALIDRESP  = 6;  /* bogus response verifier          */
   protected static final int AUTH_FAILED       = 7;  /* reason unknown                   */

   protected static final int RECORD_ERROR = 21;   // record marking standard error
   protected static final int RECEIVE_BUFFER_SIZE = 4096;
   protected static final int SEND_BUFFER_SIZE    = 4096;
   protected boolean done;
   protected int prog;
   protected int version;
   protected boolean m_serverDone;
   private SocketManager m_socketManager;

   public RPCServer (String name, int prog, int version) throws RPCException, IOException {

      this.prog = prog;
      this.version = version;
      ServerSocket serverSo = new ServerSocket(0);

      PortMapper p = new PortMapper("localhost", 111);
      mapping m = new mapping();
      m.prog = prog;
      m.vers = version;
      m.port = serverSo.getLocalPort();
      m.prot = IPPROTO_TCP.value;
      p.unset_2(m);
      if (p.set_2(m)) {
         done = false;
         m_socketManager = new SocketManager(name, serverSo);
         (new Thread(m_socketManager,name+"_SocketManager")).start();

      }
   }

   protected abstract XDR call (int version, int proc, RPCInputStream in) throws RPCException, IOException;

   protected class SocketManager implements Runnable {
      protected ServerSocket server;
      private String m_name;
      protected SocketManager (String name, ServerSocket server) {
         this.server = server;
         m_name = name;
      }
      public void run () {
         try {
            while (!done) {
               Socket socket = server.accept();
               (new Thread(new Server(socket), m_name+"_Server")).start();
            }
            server.close();
         } catch (IOException ioe) {
            System.out.println(ioe);
            ioe.printStackTrace(System.out);
         }
      }
      
      public void close() {
         try {
            server.close();
         } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
         }
      }

   }

   protected class Server implements Runnable {
      protected Socket socket;
      protected RPCInputStream in;
      protected RPCOutputStream out;
      protected int transactionID;
      protected int msg_Type;
      protected int proc;

      protected Server (Socket socket) {
         this.socket = socket;
      }
      public void run () {
         try {
            in = new RPCInputStream(socket.getInputStream(), RECEIVE_BUFFER_SIZE);
            out = new RPCOutputStream(socket.getOutputStream(), SEND_BUFFER_SIZE);
            while (!m_serverDone) {
               transactionID = in.readInt();
               msg_Type = in.readInt();
               if (CALL == msg_Type) {
                  int rpcVersion = in.readInt();
                  if (rpcVersion == RPC_VERSION) {
                     if (prog == in.readInt()) {
                        if (version == in.readInt()) {
                           proc = in.readInt();
                           if (AUTH_NONE == in.readInt()) {
                              in.readInt();
                              if (AUTH_NONE == in.readInt()) {
                                 in.readInt();
                                 // parameter
                                 XDR retVal = null;
                                 try {
                                    retVal = call(version, proc, in);

                                 } catch (IOException ioe) {
                                    out.writeInt(transactionID);
                                    out.writeInt(REPLY);
                                    out.writeInt(MSG_ACCEPTED);
                                    out.writeInt(AUTH_NONE);
                                    out.writeInt(0);
                                    out.writeInt(GARBAGE_ARGS);
                                    out.flush();
                                    return;
                                 }
                                 // procedure call was successful
                                 if (retVal != null) {
                                    in.skip();
                                    out.writeInt(transactionID);
                                    out.writeInt(REPLY);
                                    out.writeInt(MSG_ACCEPTED);
                                    out.writeInt(AUTH_NONE);
                                    out.writeInt(0);
                                    out.writeInt(SUCCESS);
                                    retVal.write(out);
                                    out.flush();
                                 } else {
                                    in.skip();
                                    // send error message: proc not supported
                                    out.writeInt(transactionID);
                                    out.writeInt(REPLY);
                                    out.writeInt(MSG_ACCEPTED);
                                    out.writeInt(AUTH_NONE);
                                    out.writeInt(0);
                                    out.writeInt(PROC_UNAVAIL);
                                    out.flush();
                                 }
                              } else {
                                 in.skip();
                                 out.writeInt(transactionID);
                                 out.writeInt(REPLY);
                                 out.writeInt(MSG_DENIED);
                                 out.writeInt(AUTH_ERROR);
                                 out.writeInt(AUTH_FAILED);
                                 out.flush();
                              }
                           } else {
                              in.skip();
                              out.writeInt(transactionID);
                              out.writeInt(REPLY);
                              out.writeInt(MSG_DENIED);
                              out.writeInt(AUTH_ERROR);
                              out.writeInt(AUTH_FAILED);
                              out.flush();
                           }
                        } else {
                           in.skip();
                           out.writeInt(transactionID);
                           out.writeInt(REPLY);
                           out.writeInt(MSG_ACCEPTED);
                           out.writeInt(AUTH_NONE);
                           out.writeInt(0);
                           out.writeInt(PROG_MISMATCH);
                           out.writeInt(version);
                           out.writeInt(version);
                           out.flush();
                        }
                     } else {
                        in.skip();
                        out.writeInt(transactionID);
                        out.writeInt(REPLY);
                        out.writeInt(MSG_ACCEPTED);
                        out.writeInt(AUTH_NONE);
                        out.writeInt(0);
                        out.writeInt(PROG_UNAVAIL);
                        out.flush();
                     }
                  } else {
                     in.skip();
                     // RPC_MISMATCH  RPC version number != 2 => send reply
                     out.writeInt(transactionID);
                     out.writeInt(REPLY);
                     out.writeInt(MSG_DENIED);
                     out.writeInt(RPC_MISMATCH);
                     out.writeInt(RPC_VERSION);
                     out.writeInt(RPC_VERSION);
                     out.flush();
                  }
               } else {
                  System.out.println("RPCServer - Server - error: no call message: " + msg_Type);
               }
            }
//          socket.close();
         } catch (IOException ioe) {
//            System.out.println(m_name);
            ioe.printStackTrace(System.out);
         } catch (RPCException rpce) {
            rpce.printStackTrace(System.out);
         } finally {
            try {
               socket.close();
            } catch (IOException ex) {
               // TODO Auto-generated catch block
               ex.printStackTrace();
            }
         }
      }
   }

   public void close () {
      done = true;
      m_serverDone = true;
      m_socketManager.close();
   }
   public void removeServer() {
      m_serverDone = true;
   }


}