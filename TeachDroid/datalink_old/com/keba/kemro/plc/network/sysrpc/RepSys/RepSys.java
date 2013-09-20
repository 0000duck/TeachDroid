package com.keba.kemro.plc.network.sysrpc.RepSys;

import java.io.IOException;
import java.net.UnknownHostException;

import com.keba.jrpc.rpc.RPCClient;
import com.keba.jrpc.rpc.RPCException;
import com.keba.jrpc.rpc.XDR;
import com.keba.jrpc.rpc.XDRBool;
import com.keba.jrpc.rpc.XDRDouble;
import com.keba.jrpc.rpc.XDRFloat;
import com.keba.jrpc.rpc.XDRHyper;
import com.keba.jrpc.rpc.XDRInt;
import com.keba.jrpc.rpc.XDRString;

public class RepSys extends RPCClient {
   public static final int STISRpcProgram = 550418951;
   public int prog;

   public RepSys (String host, int prog, int version) throws RPCException, UnknownHostException, IOException {
       super(host, prog, version);
       this.prog = prog;
   }

   public RepSys (String host) throws RPCException, UnknownHostException, IOException {
       super(host, STISRpcProgram, STISRpcVersion);
       this.prog = STISRpcProgram;
   }

   public RepSys (String host, int port, int prog, int version) throws RPCException, UnknownHostException, IOException {
       super(host, port);
       this.prog = prog;
   }

   public RepSys (String host, int port) throws RPCException, UnknownHostException, IOException {
       super(host, port);
       this.prog = STISRpcProgram;
   }

   public static final int cRepSysMaxNameSize = 30;
   public static final int cRepSys_SetMsg_ParamType_Int = 100;
   public static final int cRepSys_SetMsg_RespCode_InvalidMsgData = 2;
   public static final int cRepSys_SetMsg_RespCode_InvalidParamType = 3;
   public static final int cRepSys_SetMsg_RespCode_OutOfMemory = 5;
   public static final int cRepSys_SetMsg_RespCode_ParamDataError = 4;
   public static final int cRepSysNrErrorParams = 8;
   public static final int cRepSys_SetMsg_ParamType_Float = 101;
   public static final int cRepSys_SetMsg_RespCode_Failed = 1;
   public static final int cRepSys_SetMsg_ParamType_Double = 106;
   public static final int cRepSys_SetMsg_ParamType_String = 102;
   public static final int cRepSysMaxMsgCnt = 3;
   public static final int cRepSysMaxClasses = 32;
   public static final int cRepSys_SetMsg_ParamType_LongLong = 107;
   public static final int cRepSys_SetMsg_RespCode_ParamWarning = 10;
   public static final int cRepSys_SetMsg_ParamType_WString = 104;
   public static final int cRepSysMaxFormatStringLength = 8;
   public static final int cRepSysLanguageCode = 3;
   public static final int cRepSysMaxMsgTextSize = 250;
   public static final int cRepSysErrorStringBufferSize = 76;
   public static final int cRepSys_SetMsg_RespCode_Ok = 0;
   public static final int cRepSys_SetMsg_ParamType_Memory = 103;
   public static final int cRepSys_SetMsg_ParamType_Unused = 0;
   public static final int STISRpcVersion = 1;
   public static final XDR[] args = new XDR[128];
   public static int args_length;
   public static final XDRInt retVal_Int = new XDRInt();
   public static final XDRHyper retVal_Hyper = new XDRHyper();
   public static final XDRFloat retVal_Float = new XDRFloat();
   public static final XDRDouble retVal_Double = new XDRDouble();
   public static final XDRBool retVal_Bool = new XDRBool();
   public static final XDRString retVal_String = new XDRString();
   
   public TRepSysClassIdMultiResp GetAllClassIds_1 (TRepSysClassIdMultiResp retVal) throws RPCException, IOException {
       synchronized (args) {
           call(prog, STISRpcVersion, 3000, retVal, args_length, null);
           return retVal;
       }
   }
   public TRepSysEntryTypeResp GetClassName_1 (TRepSysClassMaskRequ arg_1, TRepSysEntryTypeResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3001, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysRespErr QuitMsg_1 (TRepSysMsgNrStructRequ arg_1, TRepSysRespErr retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3005, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetLastMsgOfBuf_1 (TRepSysClassIdAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3006, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryMultiResp GetAllMsgOfBufStartWithN_1 (TRepSysClassMaskAndIdxRequ arg_1, TRepSysMsgEntryMultiResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3008, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryMultiRespEx GetAllMsgOfBufStartWithN_Ex_1 (TRepSysClassMaskAndIdxRequEx arg_1, TRepSysMsgEntryMultiRespEx retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3027, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysRespErr ResetMsg_1 (TRepSysMsgNrStructRequ arg_1, TRepSysRespErr retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3011, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetFirstMsgOfBuf_1 (TRepSysClassIdAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3013, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetNextMsgOfBuf_1 (TRepSysClassIdAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3014, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetPrevMsgOfBuf_1 (TRepSysClassIdAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3019, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetLastMsgOfProt_1 (TRepSysClassMaskAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3015, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryMultiResp GetAllMsgOfProtStartWithN_1 (TRepSysClassMaskAndIdxRequ arg_1, TRepSysMsgEntryMultiResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3017, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryMultiRespEx GetAllMsgOfProtStartWithN_Ex_1 (TRepSysClassMaskAndIdxRequEx arg_1, TRepSysMsgEntryMultiRespEx retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3028, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetFirstMsgOfProt_1 (TRepSysClassMaskAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3020, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetNextMsgOfProt_1 (TRepSysClassMaskAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3021, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgEntryResp GetPrevMsgOfProt_1 (TRepSysClassMaskAndIdxRequ arg_1, TRepSysMsgEntryResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3022, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgTextResp GetMsgTextTempl_1 (TRepSysMsgTextTemplRequ arg_1, TRepSysMsgTextResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3023, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysMsgTextResp GetMsgTextPlain_1 (TRepSysMsgTextRequ arg_1, TRepSysMsgTextResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3024, retVal, args_length, args);
           return retVal;
       }
   }
   public TRepSysLanguageResp GetLanguage_1 (TRepSysLanguageResp retVal) throws RPCException, IOException {
       synchronized (args) {
           call(prog, STISRpcVersion, 3025, retVal, args_length, null);
           return retVal;
       }
   }
   public int GetNrOfNewBufEntries_1 (int arg_0) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = new XDRInt(arg_0);
           call(prog, STISRpcVersion, 3026, retVal_Int, args_length, args);
           return retVal_Int.value;
       }
   }
   public TRepSysSetMsgResp RepSys_SetMsg_1 (TRepSysSetMsgRequ arg_1, TRepSysSetMsgResp retVal) throws RPCException, IOException {
       synchronized (args) {
           args_length = 1;
           args[0] = arg_1;
           call(prog, STISRpcVersion, 3030, retVal, args_length, args);
           return retVal;
       }
   }
}