package com.keba.jrpc.rpc;

import java.io.*;
import java.net.*;
import java.util.*;

import com.keba.jrpc.rpc.RPCClient.KeepAlive;

public class RPCClient {
   protected static final int RPC_VERSION = 2;
   // Message Type
   private static final int CALL = 0;
   private static final int REPLY = 1;
   // Reply State
   private static final int MSG_ACCEPTED = 0;
   private static final int MSG_DENIED   = 1;
   // Accept State
   private static final int SUCCESS       = 0;  /* RPC executed successfully             */
   private static final int PROG_UNAVAIL  = 1;  /* remote hasn't exported program        */
   private static final int PROG_MISMATCH = 2;  /* remote can't support version #        */
   private static final int PROC_UNAVAIL  = 3;  /* program can't support procedure       */
   private static final int GARBAGE_ARGS  = 4;  /* procedure can't decode params         */
   private static final int SYSTEM_ERR    = 5;  /* errors like memory allocation failure */
   // Denied State
   private static final int RPC_MISMATCH  = 0;  /* RPC version number != 2          */
   private static final int AUTH_ERROR    = 1;  /* remote can't authenticate caller */
   // Authentication
   private static final int AUTH_NONE     = 0;
   private static final int AUTH_SYS      = 1;
   private static final int AUTH_SHORT    = 2;
   // Authentication
   private static final int AUTH_OK       = 0;      /* success                          */
   // Authentication failed at remote end
   private static final int AUTH_BADCRED      = 1;  /* bad credential (seal broken)     */
   private static final int AUTH_REJECTEDCRED = 2;  /* client must begin new session    */
   private static final int AUTH_BADVERF      = 3;  /* bad verifier (seal broken)       */
   private static final int AUTH_REJECTEDVERF = 4;  /* verifier expired or replayed     */
   private static final int AUTH_TOOWEAK      = 5;  /* rejected for security reasons    */
   // Authentication failed locally
   private static final int AUTH_INVALIDRESP  = 6;  /* bogus response verifier          */
   private static final int AUTH_FAILED       = 7;   /* reason unknown                   */

   private static final int RECORD_ERROR = 21;   // record marking standard error

   private static final int RECEIVE_BUFFER_SIZE = 4096;
   private static final int SEND_BUFFER_SIZE    = 4096;
   
   private static final int TIMEOUT = 30000;
   private static KeepAlive keepAlive;

   private int transactionID = 0;
   private RPCInputStream in;
   private RPCOutputStream out;
   private CountingInputStream countIn;
   private CountingOutputStream countOut;
   private Socket socket;
   int programNumber;
   int portNumber;


   private Hashtable debugTable = new Hashtable();
   private boolean debugCalls = false;
   private String m_host;
   private int m_port;
   
   
   
   static class KeepAlive extends Thread {
      Hashtable table = new Hashtable();
      KeepAlive() {
         super("KeepAlive");
         setPriority(Thread.MIN_PRIORITY);
         setDaemon(true);
      }

      public  void run() {
         while(true) {
            checkTimeout();
            try {
               synchronized(this) {
                  wait(10000);
               }
            } catch (InterruptedException e) {
            }
         }
      }

      private void checkTimeout() {
         
         Vector timedOut = new Vector();
         synchronized(table) {
            long time = System.currentTimeMillis();
            Enumeration enumeration = table.keys();
            while(enumeration.hasMoreElements()) {
               RPCClient client = (RPCClient) enumeration.nextElement();
               Long lasttime = (Long) table.get(client);
               long diff = time-lasttime.longValue();
               if(diff > 10000 || diff <= 0) {
                  timedOut.addElement(client);
               }
            }
         }
         for(int i=0;i <timedOut.size();i++) {
            try{
               ((RPCClient)timedOut.elementAt(i)).keepAlive();
            }catch(Throwable th){}
         }
         
      }
      
      public  void postCall(RPCClient client) {
//         System.out.println("postcall "+client);
         synchronized(table) {
            table.put(client, new Long(System.currentTimeMillis()));
         }
      }

      public  void remove(RPCClient rpcClient) {
         synchronized(table) {
            table.remove(rpcClient);
         }
         
      }
      
   }
   
   static {
      keepAlive = new KeepAlive();
      keepAlive.start();
      
   }

   /**
    * Enables or disables the debug mode
    * @param debug true to enable
    */
   public void setDebug(boolean debug){
      debugCalls = debug;
   }

   public static void setServerName(int serverId, String name){
//      synchronized(debugTable){
//         Integer sid = new Integer(serverId);
//         if(!debugTable.containsKey(sid))
//            debugTable.put(sid, new DebugCallInfo(serverId));
//         ((DebugCallInfo)debugTable.get(sid)).setServerName(name);
//      }
   }
//
//   public static Hashtable getDebugInfo(){
//      Hashtable result = new Hashtable();
//      Enumeration keys = debugTable.keys();
//      while(keys.hasMoreElements()){
//         Object key = keys.nextElement();
//         result.put(key, ((DebugCallInfo)debugTable.get(key)).clone());
//      }
//      return result;
//   }


   public RPCClient(String host, int prog, int version) throws RPCException, UnknownHostException, IOException {
      PortMapper p = new PortMapper(host, 111);
      mapping m = new mapping();
      m.prog = prog;
      m.vers = version;
      m.prot = IPPROTO_TCP.value;
      programNumber = prog;
      try {
         portNumber = p.getPort_2(m);
         if (portNumber <= 0) {
            System.out.println(this.getClass().getName() + "  portmapper.getPort_2 ->  port number: " + 
                  portNumber + "  ( program number: " + programNumber + 
                  " ; version: " + version + " )");
         }
      } catch (RPCException e) {
         p.close();
         throw e;
      } catch (IOException e) {
         p.close();
         throw e;
      } finally {
         p.close();  // laut sinn
      }
      
      connect(host, portNumber);
      keepAlive.postCall(this);
   }

   public int getProgramNumber(){
      return programNumber;
   }

   public int getPortNumber(){
      return portNumber;
   }

   public int getSentByteCount(){
      if (countOut != null){
         return countOut.getByteCount();
      }
      return -1;
   }

   public int getReceivedByteCount(){
      if (countIn != null){
         return countIn.getByteCount();
      }
      return -1;
   }

   public RPCClient(String host, int port) throws RPCException, UnknownHostException, IOException {
      portNumber = port;
      connect(host, port);
   }
   public void close () throws IOException {
      keepAlive.remove(this);
      NetworkMonitor monitor = NetworkMonitor.getInstance();
      if (monitor != null) {
         monitor.unregisterClient(this);
      }
      if (socket != null) {
         socket.close();
      }
   }

   public int getSoTimeout() throws SocketException {
      if (socket != null) {
         return socket.getSoTimeout();
      }
      return 0;
   }
   public void setSoTimeout(int timeout) throws SocketException {
      if (socket != null) {
         socket.setSoTimeout(timeout);
      }
   }

   private void connect (String host, int port) throws UnknownHostException, IOException {
      m_host = host;
      m_port = port;
      socket = new Socket(InetAddress.getByName(host), port);
      socket.setSoTimeout(TIMEOUT);
      socket.setTcpNoDelay(true);
      NetworkMonitor.getInstance().registerClient(this);
      if (NetworkMonitor.isLogNetworkTraffic()){
         countIn = new CountingInputStream(socket.getInputStream());
         countOut = new CountingOutputStream(socket.getOutputStream());
         in = new RPCInputStream(countIn, RECEIVE_BUFFER_SIZE);
         out = new RPCOutputStream(countOut, SEND_BUFFER_SIZE);
      } else {
         in = new RPCInputStream(socket.getInputStream(), RECEIVE_BUFFER_SIZE);
         out = new RPCOutputStream(socket.getOutputStream(), SEND_BUFFER_SIZE);
      }
   }


   protected synchronized void call (int prog, int ver, int proc, XDR retVal, XDR[] args) throws RPCException, IOException {
      call(prog, ver, proc, retVal, (args != null) ? args.length : 0, args);
   }
   
   
   private  void keepAlive() {
      try {
//         System.out.println("keep alive "+toString());
         call(programNumber,1,0,new XDRVoid(),null);
      } catch (RPCException e) {
         
      } catch (IOException e) {
        
      }
   }
   
   protected synchronized void call (int prog, int ver, int proc, XDR retVal, int args_length, XDR[] args) throws RPCException, IOException {
      keepAlive.postCall(this);
      long time = System.currentTimeMillis();
      transactionID++;
      // skip all bytes which are in the RPCInputStream
      in.skip();
      // call
//      try{
         out.writeInt(transactionID);
         out.writeInt(CALL);
         out.writeInt(RPC_VERSION);
         out.writeInt(prog);
         out.writeInt(ver);
         out.writeInt(proc);
         out.writeInt(AUTH_NONE);
         out.writeInt(0);
         out.writeInt(AUTH_NONE);
         out.writeInt(0);
         if (args != null) {
            for (int i = 0; i < args_length; i++) {
               args[i].write(out);
            }
         }
         out.flush();
/*
      } catch(Exception ex){
         System.out.println("-------------------------------------------");
         System.out.println("RPC_Version "+RPC_VERSION);
         System.out.println("PROG-NR: "+prog);
         System.out.println("Vers-Nr: "+ver);
         System.out.println("Procedure: "+proc);
         System.out.println("-------------------------------------------");
         ex.printStackTrace();
      }
*/
      // reply
      int id = in.readInt();
      if (id == transactionID) {
         int msg_Type = in.readInt();
         if (msg_Type == REPLY) {
            switch (in.readInt()) {
            case MSG_ACCEPTED:
               int auth = in.readInt();
               byte[] b = in.readBytes();
               switch (in.readInt()) {
               case SUCCESS:
                  // procedure-specific results start here
                  if (retVal != null) {
                     retVal.read(in);
                     if (!in.isEmpty()) {
                        throw new RPCException(RPCClient.RECORD_ERROR, "retVal read - to many bytes in RPCInputStream");
                     }
                  }
                  break;
               case PROG_UNAVAIL:
                  throw new RPCException(RPCException.PROG_UNAVAIL, "wrong program");   // remote hasn't exported program
               case PROG_MISMATCH:
                  throw new RPCException(RPCException.PROG_MISMATCH, "wrong version: " + in.readInt() + " " + in.readInt());  // remote can't support version
               case PROC_UNAVAIL:
                  throw new RPCException(RPCException.PROC_UNAVAIL, "wrong procedure"); // program can't support procedure
               case GARBAGE_ARGS:
                  throw new RPCException(RPCException.GARBAGE_ARGS, "wrong parameter"); // procedure can't decode params
               case SYSTEM_ERR:
                  throw new RPCException(RPCException.SYSTEM_ERR, "server error");      // errors like memory allocation failure
               }
               break;
            case MSG_DENIED:
               switch (in.readInt()) {
               case RPC_MISMATCH:
                  throw new RPCException(RPCException.RPC_MISMATCH, "wrong version: " + in.readInt() + " " + in.readInt());  // RPC version number != 2
               case AUTH_ERROR:
                  int a = in.readInt();
                  if (a != AUTH_OK) {
                     throw new RPCException(RPCException.AUTH_ERROR, "authentication failed at remote end");
                  }
               }
               break;
            }
         } else {
            throw new RPCException(RPCException.NO_REPLY, "no reply message");
         }
      } else {
         throw new RPCException(RPCException.TRANSACTION_ID, "transaction id false");
      }
      if(debugCalls){
         Integer procId = new Integer(proc);
         synchronized(debugTable){
            DebugCallInfo callInfo = new DebugCallInfo();
            if(!debugTable.containsKey(procId)) {
               debugTable.put(procId, callInfo);
            } else {
               callInfo = (DebugCallInfo) debugTable.get(procId);
            }
            callInfo.incMethodCall(System.currentTimeMillis() - time);
         }
      }
   }

   
   
   /**
    * Returns the debuginfo
    * @return info
    */
   public Hashtable getDebugCallInfo(){
      return (Hashtable) debugTable.clone();
   }
   
   /**
    * Resets the debuginfo table
    */
   public void resetCallInfo(){
      debugTable = new Hashtable();
   }

   private class CountingInputStream extends InputStream {
      private InputStream in;
      private int counter;


      public CountingInputStream(InputStream in){
         this.in = in;
         counter = 0;
      }

      public int read() throws IOException {
         int data = in.read();
         counter++;
         return data;
      }

      public int getByteCount(){
         return counter;
      }
   }

   private class CountingOutputStream extends OutputStream {
      private OutputStream out;
      private int counter;

      public CountingOutputStream(OutputStream out){
         this.out = out;
         counter = 0;
      }

      public void flush() throws IOException {
         out.flush();
      }

      public void write(int data) throws IOException {
         out.write(data);
         counter++;
      }

      public void write(byte[] data) throws IOException {
         out.write(data);
         counter += data.length;
      }

      public void write(byte[] data, int off, int len) throws IOException {
         out.write(data, off, len);
         counter += len;
      }

      public int getByteCount() {
         return counter;
      }

      public void close() throws IOException {
         out.close();
      }
   }
 
   /**
    * Returns the host where this client is connected to
    * @return host
    */
   public String getHost() {
      return m_host;
   }
   
   /**
    * Returns the socket where this client is connected to
    * @return host
    */
   public Socket getSocket() {
      return socket;
   }
}
