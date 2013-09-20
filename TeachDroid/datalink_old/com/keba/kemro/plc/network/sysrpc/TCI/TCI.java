package com.keba.kemro.plc.network.sysrpc.TCI;

import com.keba.jrpc.rpc.*;
import java.io.*;
import java.net.*;

public class TCI extends RPCClient {
	public static final int TcSysRpcProgram = 550418960;
	public int prog;

	public TCI (String host, int prog, int version) throws RPCException, UnknownHostException, IOException {
		super(host, prog, version);
		this.prog = prog;
	}

	public TCI (String host) throws RPCException, UnknownHostException, IOException {
		super(host, TcSysRpcProgram, TcSysRpcVersion);
		this.prog = TcSysRpcProgram;
	}

	public TCI (String host, int port, int prog, int version) throws RPCException, UnknownHostException, IOException {
		super(host, port);
		this.prog = prog;
	}

	public TCI (String host, int port) throws RPCException, UnknownHostException, IOException {
		super(host, port);
		this.prog = TcSysRpcProgram;
	}

	public static final int rpcMaxCodePoints = 20;
	public static final int rpcMaxPathLen = 255;
	public static final int rpcChunkLen = 50;
	public static final int rpcMaxAttrLen = 512;
	public static final int cNrErrorParams = 8;
	public static final int rpcMaxNameLen = 32;
	public static final int cMaxErrors = 20;
	public static final int rpcMaxParams = 16;
	public static final int rpcMaxWatchVars = 48;
	public static final int rpcMaxInstancePathElems = 30;
	public static final int cMaxOffsets = 29;
	public static final int TcSysRpcVersion = 1;
	public static final XDR[] args = new XDR[128];
	public static int args_length;
	public static final XDRInt retVal_Int = new XDRInt();
	public static final XDRHyper retVal_Hyper = new XDRHyper();
	public static final XDRFloat retVal_Float = new XDRFloat();
	public static final XDRDouble retVal_Double = new XDRDouble();
	public static final XDRBool retVal_Bool = new XDRBool();
	public static final XDRString retVal_String = new XDRString();
	
	public SysRpcTcOpenTeachControlOut SysRpcTcOpenTeachControl_1 (SysRpcTcOpenTeachControlIn arg_1, SysRpcTcOpenTeachControlOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25001, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcCloseTeachControlOut SysRpcTcCloseTeachControl_1 (SysRpcTcCloseTeachControlIn arg_1, SysRpcTcCloseTeachControlOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25002, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcReadErrorOut SysRpcTcReadError_1 (SysRpcTcReadErrorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25003, retVal, args_length, null);
			return retVal;
		}
	}
	public SysRpcTcReadProjectPathOut SysRpcTcReadProjectPath_1 (SysRpcTcReadProjectPathOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25004, retVal, args_length, null);
			return retVal;
		}
	}
	public SysRpcTcGetClientListOut SysRpcTcGetClientList_1 (SysRpcTcGetClientListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25005, retVal, args_length, null);
			return retVal;
		}
	}
	public SysRpcTcSetClientNameOut SysRpcTcSetClientName_1 (SysRpcTcSetClientNameIn arg_1, SysRpcTcSetClientNameOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25006, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetClientTypeOut SysRpcTcSetClientType_1 (SysRpcTcSetClientTypeIn arg_1, SysRpcTcSetClientTypeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25007, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcKeepAliveOut SysRpcTcKeepAlive_1 (SysRpcTcKeepAliveOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25008, retVal, args_length, null);
			return retVal;
		}
	}
	public SysRpcTcRequestWriteAccessOut SysRpcTcRequestWriteAccess_1 (SysRpcTcRequestWriteAccessIn arg_1, SysRpcTcRequestWriteAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25009, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcWriteAccessRequestPendingOut SysRpcTcWriteAccessRequestPending_1 (SysRpcTcWriteAccessRequestPendingOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25010, retVal, args_length, null);
			return retVal;
		}
	}
	public SysRpcTcConvertExeUnitHandleOut SysRpcTcConvertExeUnitHandle_1 (SysRpcTcConvertExeUnitHandleIn arg_1, SysRpcTcConvertExeUnitHandleOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25103, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcConvertDirEntryPathOut SysRpcTcConvertDirEntryPath_1 (SysRpcTcConvertDirEntryPathIn arg_1, SysRpcTcConvertDirEntryPathOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25104, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcScopeHdlToDirEntryPathOut SysRpcScopeHdlToDirEntryPath_1 (SysRpcScopeHdlToDirEntryPathIn arg_1, SysRpcScopeHdlToDirEntryPathOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25105, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcBuildOut SysRpcTcBuild_1 (SysRpcTcBuildIn arg_1, SysRpcTcBuildOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25201, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcDestroyOut SysRpcTcDestroy_1 (SysRpcTcDestroyIn arg_1, SysRpcTcDestroyOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25202, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetErrorsOut SysRpcTcGetErrors_1 (SysRpcTcGetErrorsIn arg_1, SysRpcTcGetErrorsOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25203, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcBuildStartOut SysRpcTcBuildStart_1 (SysRpcTcBuildStartIn arg_1, SysRpcTcBuildStartOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25204, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcReadNodeChangeOut SysRpcTcReadNodeChange_1 (SysRpcTcReadNodeChangeIn arg_1, SysRpcTcReadNodeChangeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25205, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcIsCurrentOut SysRpcTcIsCurrent_1 (SysRpcTcIsCurrentIn arg_1, SysRpcTcIsCurrentOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25206, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcOpenNodeOut SysRpcTcOpenNode_1 (SysRpcTcOpenNodeIn arg_1, SysRpcTcOpenNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25210, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstNodeOut SysRpcTcGetFirstNode_1 (SysRpcTcGetFirstNodeIn arg_1, SysRpcTcGetFirstNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25211, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextNodeOut SysRpcTcGetNextNode_1 (SysRpcTcGetNextNodeIn arg_1, SysRpcTcGetNextNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25212, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddProgramNodeOut SysRpcTcAddProgramNode_1 (SysRpcTcAddProgramNodeIn arg_1, SysRpcTcAddProgramNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25220, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddRoutineNodeOut SysRpcTcAddRoutineNode_1 (SysRpcTcAddRoutineNodeIn arg_1, SysRpcTcAddRoutineNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25221, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddVarNodeOut SysRpcTcAddVarNode_1 (SysRpcTcAddVarNodeIn arg_1, SysRpcTcAddVarNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25222, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddTypeNodeOut SysRpcTcAddTypeNode_1 (SysRpcTcAddTypeNodeIn arg_1, SysRpcTcAddTypeNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25223, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddConstNodeOut SysRpcTcAddConstNode_1 (SysRpcTcAddConstNodeIn arg_1, SysRpcTcAddConstNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25224, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRemoveNodeOut SysRpcTcRemoveNode_1 (SysRpcTcRemoveNodeIn arg_1, SysRpcTcRemoveNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25225, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRenameNodeOut SysRpcTcRenameNode_1 (SysRpcTcRenameNodeIn arg_1, SysRpcTcRenameNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25226, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcWriteDataOut SysRpcTcWriteData_1 (SysRpcTcWriteDataIn arg_1, SysRpcTcWriteDataOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25227, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcWriteDataExtOut SysRpcTcWriteDataExt_1 (SysRpcTcWriteDataExtIn arg_1, SysRpcTcWriteDataExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25228, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstNodeChunkOut SysRpcTcGetFirstNodeChunk_1 (SysRpcTcGetFirstNodeChunkIn arg_1, SysRpcTcGetFirstNodeChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25231, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextNodeChunkOut SysRpcTcGetNextNodeChunk_1 (SysRpcTcGetNextNodeChunkIn arg_1, SysRpcTcGetNextNodeChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25232, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNodeInfoListOut SysRpcTcGetNodeInfoList_1 (SysRpcTcGetNodeInfoListIn arg_1, SysRpcTcGetNodeInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25233, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetRoutineInfoListOut SysRpcTcGetRoutineInfoList_1 (SysRpcTcGetRoutineInfoListIn arg_1, SysRpcTcGetRoutineInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25234, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetVarInfoListOut SysRpcTcGetVarInfoList_1 (SysRpcTcGetVarInfoListIn arg_1, SysRpcTcGetVarInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25235, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetTypeInfoListOut SysRpcTcGetTypeInfoList_1 (SysRpcTcGetTypeInfoListIn arg_1, SysRpcTcGetTypeInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25236, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetConstInfoListOut SysRpcTcGetConstInfoList_1 (SysRpcTcGetConstInfoListIn arg_1, SysRpcTcGetConstInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25237, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetAttributesOut SysRpcTcGetAttributes_1 (SysRpcTcGetAttributesIn arg_1, SysRpcTcGetAttributesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25238, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetAttributesOut SysRpcTcSetAttributes_1 (SysRpcTcSetAttributesIn arg_1, SysRpcTcSetAttributesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25239, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcMoveVarOut SysRpcTcMoveVar_1 (SysRpcTcMoveVarIn arg_1, SysRpcTcMoveVarOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25241, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetActValueOut SysRpcTcSetActValue_1 (SysRpcTcSetActValueIn arg_1, SysRpcTcSetActValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25302, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetVarAccessOut SysRpcTcGetVarAccess_1 (SysRpcTcGetVarAccessIn arg_1, SysRpcTcGetVarAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25304, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetStructElemAccessOut SysRpcTcGetStructElemAccess_1 (SysRpcTcGetStructElemAccessIn arg_1, SysRpcTcGetStructElemAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25305, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetArrayElemAccessOut SysRpcTcGetArrayElemAccess_1 (SysRpcTcGetArrayElemAccessIn arg_1, SysRpcTcGetArrayElemAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25306, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcCreateInstanceOut SysRpcTcCreateInstance_1 (SysRpcTcCreateInstanceIn arg_1, SysRpcTcCreateInstanceOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25307, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetMapTargetOut SysRpcTcGetMapTarget_1 (SysRpcTcGetMapTargetIn arg_1, SysRpcTcGetMapTargetOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25308, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetSaveValueOut SysRpcTcSetSaveValue_1 (SysRpcTcSetSaveValueIn arg_1, SysRpcTcSetSaveValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25310, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcWriteInitValueOut SysRpcTcWriteInitValue_1 (SysRpcTcWriteInitValueIn arg_1, SysRpcTcWriteInitValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25311, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcCopySaveValueOut SysRpcTcCopySaveValue_1 (SysRpcTcCopySaveValueIn arg_1, SysRpcTcCopySaveValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25312, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcCopyActValueOut SysRpcTcCopyActValue_1 (SysRpcTcCopyActValueIn arg_1, SysRpcTcCopyActValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25313, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetMapTargetOut SysRpcTcSetMapTarget_1 (SysRpcTcSetMapTargetIn arg_1, SysRpcTcSetMapTargetOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25314, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetMemDumpOut SysRpcTcGetMemDump_1 (SysRpcTcGetMemDumpIn arg_1, SysRpcTcGetMemDumpOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25315, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetActArrayValuesOut SysRpcTcGetActArrayValues_1 (SysRpcTcGetActArrayValuesIn arg_1, SysRpcTcGetActArrayValuesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25316, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetActValueListOut SysRpcTcGetActValueList_1 (SysRpcTcGetActValueListIn arg_1, SysRpcTcGetActValueListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25321, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcOpenVarAccessListOut SysRpcTcOpenVarAccessList_1 (SysRpcTcOpenVarAccessListIn arg_1, SysRpcTcOpenVarAccessListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25322, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetSaveValueListOut SysRpcTcGetSaveValueList_1 (SysRpcTcGetSaveValueListIn arg_1, SysRpcTcGetSaveValueListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25323, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcNewVarGroupOut SysRpcTcNewVarGroup_1 (SysRpcTcNewVarGroupIn arg_1, SysRpcTcNewVarGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25330, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRemoveVarGroupOut SysRpcTcRemoveVarGroup_1 (SysRpcTcRemoveVarGroupIn arg_1, SysRpcTcRemoveVarGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25331, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddVarsToGroupOut SysRpcTcAddVarsToGroup_1 (SysRpcTcAddVarsToGroupIn arg_1, SysRpcTcAddVarsToGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25332, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRemoveVarsFromGroupOut SysRpcTcRemoveVarsFromGroup_1 (SysRpcTcRemoveVarsFromGroupIn arg_1, SysRpcTcRemoveVarsFromGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25333, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcReadVarGroupValuesOut SysRpcTcReadVarGroupValues_1 (SysRpcTcReadVarGroupValuesIn arg_1, SysRpcTcReadVarGroupValuesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25334, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcReadChangedVarGroupValuesOut SysRpcTcReadChangedVarGroupValues_1 (SysRpcTcReadChangedVarGroupValuesIn arg_1, SysRpcTcReadChangedVarGroupValuesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25335, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstDirEntryOut SysRpcTcGetFirstDirEntry_1 (SysRpcTcGetFirstDirEntryIn arg_1, SysRpcTcGetFirstDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25401, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextDirEntryOut SysRpcTcGetNextDirEntry_1 (SysRpcTcGetNextDirEntryIn arg_1, SysRpcTcGetNextDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25402, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcAddDirEntryOut SysRpcTcAddDirEntry_1 (SysRpcTcAddDirEntryIn arg_1, SysRpcTcAddDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25404, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcCopyDirEntryOut SysRpcTcCopyDirEntry_1 (SysRpcTcCopyDirEntryIn arg_1, SysRpcTcCopyDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25405, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRenameDirEntryOut SysRpcTcRenameDirEntry_1 (SysRpcTcRenameDirEntryIn arg_1, SysRpcTcRenameDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25406, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcDeleteDirEntryOut SysRpcTcDeleteDirEntry_1 (SysRpcTcDeleteDirEntryIn arg_1, SysRpcTcDeleteDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25407, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcDeleteDirEntryExtOut SysRpcTcDeleteDirEntryExt_1 (SysRpcTcDeleteDirEntryExtIn arg_1, SysRpcTcDeleteDirEntryExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25408, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstDirEntryChunkOut SysRpcTcGetFirstDirEntryChunk_1 (SysRpcTcGetFirstDirEntryChunkIn arg_1, SysRpcTcGetFirstDirEntryChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25420, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextDirEntryChunkOut SysRpcTcGetNextDirEntryChunk_1 (SysRpcTcGetNextDirEntryChunkIn arg_1, SysRpcTcGetNextDirEntryChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25422, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetDirEntryInfoListOut SysRpcTcGetDirEntryInfoList_1 (SysRpcTcGetDirEntryInfoListIn arg_1, SysRpcTcGetDirEntryInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25423, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcOpenSyntaxEditorOut SysRpcTcOpenSyntaxEditor_1 (SysRpcTcOpenSyntaxEditorIn arg_1, SysRpcTcOpenSyntaxEditorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25501, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcCloseEditorOut SysRpcTcCloseEditor_1 (SysRpcTcCloseEditorIn arg_1, SysRpcTcCloseEditorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25502, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSaveEditorOut SysRpcTcSaveEditor_1 (SysRpcTcSaveEditorIn arg_1, SysRpcTcSaveEditorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25503, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstLineOut SysRpcTcGetFirstLine_1 (SysRpcTcGetFirstLineIn arg_1, SysRpcTcGetFirstLineOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25504, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextLineOut SysRpcTcGetNextLine_1 (SysRpcTcGetNextLineIn arg_1, SysRpcTcGetNextLineOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25505, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetEditorInfoOut SysRpcTcGetEditorInfo_1 (SysRpcTcGetEditorInfoIn arg_1, SysRpcTcGetEditorInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25506, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcInsertTextOut SysRpcTcInsertText_1 (SysRpcTcInsertTextIn arg_1, SysRpcTcInsertTextOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25507, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcDeleteTextOut SysRpcTcDeleteText_1 (SysRpcTcDeleteTextIn arg_1, SysRpcTcDeleteTextOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25508, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstSymbolOut SysRpcTcGetFirstSymbol_1 (SysRpcTcGetFirstSymbolIn arg_1, SysRpcTcGetFirstSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25512, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextSymbolOut SysRpcTcGetNextSymbol_1 (SysRpcTcGetNextSymbolIn arg_1, SysRpcTcGetNextSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25513, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetSymbolTextRangeOut SysRpcTcGetSymbolTextRange_1 (SysRpcTcGetSymbolTextRangeIn arg_1, SysRpcTcGetSymbolTextRangeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25514, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNodeTextRangeOut SysRpcTcGetNodeTextRange_1 (SysRpcTcGetNodeTextRangeIn arg_1, SysRpcTcGetNodeTextRangeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25515, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcDeleteSymbolOut SysRpcTcDeleteSymbol_1 (SysRpcTcDeleteSymbolIn arg_1, SysRpcTcDeleteSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25520, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcInsertStatementOut SysRpcTcInsertStatement_1 (SysRpcTcInsertStatementIn arg_1, SysRpcTcInsertStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25521, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcDeleteStatementOut SysRpcTcDeleteStatement_1 (SysRpcTcDeleteStatementIn arg_1, SysRpcTcDeleteStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25522, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcMoveStatementOut SysRpcTcMoveStatement_1 (SysRpcTcMoveStatementIn arg_1, SysRpcTcMoveStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25523, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcReplaceStatementOut SysRpcTcReplaceStatement_1 (SysRpcTcReplaceStatementIn arg_1, SysRpcTcReplaceStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25524, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcReplaceCommentOut SysRpcTcReplaceComment_1 (SysRpcTcReplaceCommentIn arg_1, SysRpcTcReplaceCommentOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25525, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstSymbolChunkOut SysRpcTcGetFirstSymbolChunk_1 (SysRpcTcGetFirstSymbolChunkIn arg_1, SysRpcTcGetFirstSymbolChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25531, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextSymbolChunkOut SysRpcTcGetNextSymbolChunk_1 (SysRpcTcGetNextSymbolChunkIn arg_1, SysRpcTcGetNextSymbolChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25532, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstLineChunkOut SysRpcTcGetFirstLineChunk_1 (SysRpcTcGetFirstLineChunkIn arg_1, SysRpcTcGetFirstLineChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25533, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextLineChunkOut SysRpcTcGetNextLineChunk_1 (SysRpcTcGetNextLineChunkIn arg_1, SysRpcTcGetNextLineChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25534, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcOpenSyntaxEditorExtOut SysRpcTcOpenSyntaxEditorExt_1 (SysRpcTcOpenSyntaxEditorExtIn arg_1, SysRpcTcOpenSyntaxEditorExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25540, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcLoadGlobalOut SysRpcTcLoadGlobal_1 (SysRpcTcLoadGlobalIn arg_1, SysRpcTcLoadGlobalOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25601, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcUnloadGlobalOut SysRpcTcUnloadGlobal_1 (SysRpcTcUnloadGlobalIn arg_1, SysRpcTcUnloadGlobalOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25602, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcLoadProjOut SysRpcTcLoadProj_1 (SysRpcTcLoadProjIn arg_1, SysRpcTcLoadProjOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25603, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcUnloadProjOut SysRpcTcUnloadProj_1 (SysRpcTcUnloadProjIn arg_1, SysRpcTcUnloadProjOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25604, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcStartProgOut SysRpcTcStartProg_1 (SysRpcTcStartProgIn arg_1, SysRpcTcStartProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25605, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcInterruptProgOut SysRpcTcInterruptProg_1 (SysRpcTcInterruptProgIn arg_1, SysRpcTcInterruptProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25606, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcContinueProgOut SysRpcTcContinueProg_1 (SysRpcTcContinueProgIn arg_1, SysRpcTcContinueProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25607, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcStopProgOut SysRpcTcStopProg_1 (SysRpcTcStopProgIn arg_1, SysRpcTcStopProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25608, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstExeUnitOut SysRpcTcGetFirstExeUnit_1 (SysRpcTcGetFirstExeUnitIn arg_1, SysRpcTcGetFirstExeUnitOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25609, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextExeUnitOut SysRpcTcGetNextExeUnit_1 (SysRpcTcGetNextExeUnitIn arg_1, SysRpcTcGetNextExeUnitOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25610, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetExeUnitInfoOut SysRpcTcGetExeUnitInfo_1 (SysRpcTcGetExeUnitInfoIn arg_1, SysRpcTcGetExeUnitInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25611, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetExeUnitStatusOut SysRpcTcGetExeUnitStatus_1 (SysRpcTcGetExeUnitStatusIn arg_1, SysRpcTcGetExeUnitStatusOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25612, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcExecuteMethodOut SysRpcTcExecuteMethod_1 (SysRpcTcExecuteMethodIn arg_1, SysRpcTcExecuteMethodOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25613, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcStartProgExOut SysRpcTcStartProgEx_1 (SysRpcTcStartProgExIn arg_1, SysRpcTcStartProgExOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25614, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcExecuteMethodExtOut SysRpcTcExecuteMethodExt_1 (SysRpcTcExecuteMethodExtIn arg_1, SysRpcTcExecuteMethodExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25615, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetFirstExeUnitChunkOut SysRpcTcGetFirstExeUnitChunk_1 (SysRpcTcGetFirstExeUnitChunkIn arg_1, SysRpcTcGetFirstExeUnitChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25621, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetNextExeUnitChunkOut SysRpcTcGetNextExeUnitChunk_1 (SysRpcTcGetNextExeUnitChunkIn arg_1, SysRpcTcGetNextExeUnitChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25622, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetExeUnitInfoListOut SysRpcTcGetExeUnitInfoList_1 (SysRpcTcGetExeUnitInfoListIn arg_1, SysRpcTcGetExeUnitInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25623, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetExeUnitStatusListOut SysRpcTcGetExeUnitStatusList_1 (SysRpcTcGetExeUnitStatusListIn arg_1, SysRpcTcGetExeUnitStatusListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25624, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetInstructionPointerOut SysRpcTcSetInstructionPointer_1 (SysRpcTcSetInstructionPointerIn arg_1, SysRpcTcSetInstructionPointerOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25625, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetMultExeUnitChunkOut SysRpcTcGetMultExeUnitChunk_1 (SysRpcTcGetMultExeUnitChunkIn arg_1, SysRpcTcGetMultExeUnitChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25626, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetDebugModeOut SysRpcTcSetDebugMode_1 (SysRpcTcSetDebugModeIn arg_1, SysRpcTcSetDebugModeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25701, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcStepOut SysRpcTcStep_1 (SysRpcTcStepIn arg_1, SysRpcTcStepOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25702, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetCodePointOut SysRpcTcSetCodePoint_1 (SysRpcTcSetCodePointIn arg_1, SysRpcTcSetCodePointOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25704, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRemoveCodePointOut SysRpcTcRemoveCodePoint_1 (SysRpcTcRemoveCodePointIn arg_1, SysRpcTcRemoveCodePointOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25705, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetCodePointsOut SysRpcTcGetCodePoints_1 (SysRpcTcGetCodePointsIn arg_1, SysRpcTcGetCodePointsOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25706, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcActivateCodePointOut SysRpcTcActivateCodePoint_1 (SysRpcTcActivateCodePointIn arg_1, SysRpcTcActivateCodePointOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25707, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetWatchPointCounterOut SysRpcTcGetWatchPointCounter_1 (SysRpcTcGetWatchPointCounterIn arg_1, SysRpcTcGetWatchPointCounterOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25708, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcResetWatchPointCounterOut SysRpcTcResetWatchPointCounter_1 (SysRpcTcResetWatchPointCounterIn arg_1, SysRpcTcResetWatchPointCounterOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25709, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetWatchPointVarOut SysRpcTcSetWatchPointVar_1 (SysRpcTcSetWatchPointVarIn arg_1, SysRpcTcSetWatchPointVarOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25710, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcRemoveWatchPointVarOut SysRpcTcRemoveWatchPointVar_1 (SysRpcTcRemoveWatchPointVarIn arg_1, SysRpcTcRemoveWatchPointVarOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25711, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetWatchPointValueOut SysRpcTcGetWatchPointValue_1 (SysRpcTcGetWatchPointValueIn arg_1, SysRpcTcGetWatchPointValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25712, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetWatchPointVarsOut SysRpcTcGetWatchPointVars_1 (SysRpcTcGetWatchPointVarsIn arg_1, SysRpcTcGetWatchPointVarsOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25713, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetWatchPointTriggerOut SysRpcTcSetWatchPointTrigger_1 (SysRpcTcSetWatchPointTriggerIn arg_1, SysRpcTcSetWatchPointTriggerOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25714, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcStepListOut SysRpcTcStepList_1 (SysRpcTcStepListIn arg_1, SysRpcTcStepListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25730, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcSetExeFlagOut SysRpcTcSetExeFlag_1 (SysRpcTcSetExeFlagIn arg_1, SysRpcTcSetExeFlagOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25731, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetCodePointRoutinesOut SysRpcTcGetCodePointRoutines_1 (SysRpcTcGetCodePointRoutinesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25732, retVal, args_length, null);
			return retVal;
		}
	}
	public SysRpcTcGetNodeInfoOut SysRpcTcGetNodeInfo_1 (SysRpcTcGetNodeInfoIn arg_1, SysRpcTcGetNodeInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25213, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetRoutineInfoOut SysRpcTcGetRoutineInfo_1 (SysRpcTcGetRoutineInfoIn arg_1, SysRpcTcGetRoutineInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25214, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetVarInfoOut SysRpcTcGetVarInfo_1 (SysRpcTcGetVarInfoIn arg_1, SysRpcTcGetVarInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25215, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetTypeInfoOut SysRpcTcGetTypeInfo_1 (SysRpcTcGetTypeInfoIn arg_1, SysRpcTcGetTypeInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25216, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetConstInfoOut SysRpcTcGetConstInfo_1 (SysRpcTcGetConstInfoIn arg_1, SysRpcTcGetConstInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25217, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetActValueOut SysRpcTcGetActValue_1 (SysRpcTcGetActValueIn arg_1, SysRpcTcGetActValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25301, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcOpenVarAccessOut SysRpcTcOpenVarAccess_1 (SysRpcTcOpenVarAccessIn arg_1, SysRpcTcOpenVarAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25303, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetSaveValueOut SysRpcTcGetSaveValue_1 (SysRpcTcGetSaveValueIn arg_1, SysRpcTcGetSaveValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25309, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetDirEntryInfoOut SysRpcTcGetDirEntryInfo_1 (SysRpcTcGetDirEntryInfoIn arg_1, SysRpcTcGetDirEntryInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25403, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetValidTokensOut SysRpcTcGetValidTokens_1 (SysRpcTcGetValidTokensIn arg_1, SysRpcTcGetValidTokensOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25509, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcInsertTerminalSymbolOut SysRpcTcInsertTerminalSymbol_1 (SysRpcTcInsertTerminalSymbolIn arg_1, SysRpcTcInsertTerminalSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25518, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcInsertNonTerminalSymbolOut SysRpcTcInsertNonTerminalSymbol_1 (SysRpcTcInsertNonTerminalSymbolIn arg_1, SysRpcTcInsertNonTerminalSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25519, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetHandleOut SysRpcTcGetHandle_1 (SysRpcTcGetHandleIn arg_1, SysRpcTcGetHandleOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25101, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetHandleNameOut SysRpcTcGetHandleName_1 (SysRpcTcGetHandleNameIn arg_1, SysRpcTcGetHandleNameOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25102, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetSymbolTextOut SysRpcTcGetSymbolText_1 (SysRpcTcGetSymbolTextIn arg_1, SysRpcTcGetSymbolTextOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, TcSysRpcVersion, 25516, retVal, args_length, args);
			return retVal;
		}
	}
	public SysRpcTcGetExecInfoOut SysRpcTcGetExecInfo_1 (SysRpcTcGetExecInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, TcSysRpcVersion, 25715, retVal, args_length, null);
			return retVal;
		}
	}
}