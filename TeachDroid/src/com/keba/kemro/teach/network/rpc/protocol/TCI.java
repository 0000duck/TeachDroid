package com.keba.kemro.teach.network.rpc.protocol;

import com.keba.jrpc.rpc.*;
import java.io.*;
import java.net.*;

public class TCI extends RPCClient {
	public static final int OLIRPC_PROG = 1483;
	public int prog;

	public TCI (String host, int prog, int version) throws RPCException, UnknownHostException, IOException {
		super(host, prog, version);
		this.prog = prog;
	}

	public TCI (String host) throws RPCException, UnknownHostException, IOException {
		super(host, OLIRPC_PROG, OLIRPC_VERS);
		this.prog = OLIRPC_PROG;
	}

	public TCI (String host, int port, int prog, int version) throws RPCException, UnknownHostException, IOException {
		super(host, port);
		this.prog = prog;
	}

	public TCI (String host, int port) throws RPCException, UnknownHostException, IOException {
		super(host, port);
		this.prog = OLIRPC_PROG;
	}

	public static final int OLIRPC_VERS = 1;
	public static final XDR[] args = new XDR[128];
	public static int args_length;
	public static final XDRInt retVal_Int = new XDRInt();
	public static final XDRHyper retVal_Hyper = new XDRHyper();
	public static final XDRFloat retVal_Float = new XDRFloat();
	public static final XDRDouble retVal_Double = new XDRDouble();
	public static final XDRBool retVal_Bool = new XDRBool();
	public static final XDRString retVal_String = new XDRString();
	
	public RpcTcOpenTeachControlOut RpcTcOpenTeachControl_1 (RpcTcOpenTeachControlIn arg_1, RpcTcOpenTeachControlOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 1, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcCloseTeachControlOut RpcTcCloseTeachControl_1 (RpcTcCloseTeachControlIn arg_1, RpcTcCloseTeachControlOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 2, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcReadErrorOut RpcTcReadError_1 (RpcTcReadErrorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 3, retVal, args_length, null);
			return retVal;
		}
	}
	public RpcTcReadProjectPathOut RpcTcReadProjectPath_1 (RpcTcReadProjectPathOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 4, retVal, args_length, null);
			return retVal;
		}
	}
	public RpcTcGetClientListOut RpcTcGetClientList_1 (RpcTcGetClientListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 5, retVal, args_length, null);
			return retVal;
		}
	}
	public RpcTcSetClientNameOut RpcTcSetClientName_1 (RpcTcSetClientNameIn arg_1, RpcTcSetClientNameOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 6, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetClientTypeOut RpcTcSetClientType_1 (RpcTcSetClientTypeIn arg_1, RpcTcSetClientTypeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 7, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcKeepAliveOut RpcTcKeepAlive_1 (RpcTcKeepAliveOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 8, retVal, args_length, null);
			return retVal;
		}
	}
	public RpcTcRequestWriteAccessOut RpcTcRequestWriteAccess_1 (RpcTcRequestWriteAccessIn arg_1, RpcTcRequestWriteAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 9, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcWriteAccessRequestPendingOut RpcTcWriteAccessRequestPending_1 (RpcTcWriteAccessRequestPendingOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 10, retVal, args_length, null);
			return retVal;
		}
	}
	public RpcTcGetHandleOut RpcTcGetHandle_1 (RpcTcGetHandleIn arg_1, RpcTcGetHandleOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 101, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetHandleNameOut RpcTcGetHandleName_1 (RpcTcGetHandleNameIn arg_1, RpcTcGetHandleNameOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 102, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcConvertExeUnitHandleOut RpcTcConvertExeUnitHandle_1 (RpcTcConvertExeUnitHandleIn arg_1, RpcTcConvertExeUnitHandleOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 103, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcConvertDirEntryPathOut RpcTcConvertDirEntryPath_1 (RpcTcConvertDirEntryPathIn arg_1, RpcTcConvertDirEntryPathOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 104, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcConvertScopeHandleToDirEntryPathOut RpcTcConvertScopeHandleToDirEntryPath_1 (RpcTcConvertScopeHandleToDirEntryPathIn arg_1, RpcTcConvertScopeHandleToDirEntryPathOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 105, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcBuildOut RpcTcBuild_1 (RpcTcBuildIn arg_1, RpcTcBuildOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 201, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcDestroyOut RpcTcDestroy_1 (RpcTcDestroyIn arg_1, RpcTcDestroyOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 202, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetErrorsOut RpcTcGetErrors_1 (RpcTcGetErrorsIn arg_1, RpcTcGetErrorsOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 203, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcBuildStartOut RpcTcBuildStart_1 (RpcTcBuildStartIn arg_1, RpcTcBuildStartOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 204, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcReadNodeChangeOut RpcTcReadNodeChange_1 (RpcTcReadNodeChangeIn arg_1, RpcTcReadNodeChangeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 205, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcIsCurrentOut RpcTcIsCurrent_1 (RpcTcIsCurrentIn arg_1, RpcTcIsCurrentOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 206, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcOpenNodeOut RpcTcOpenNode_1 (RpcTcOpenNodeIn arg_1, RpcTcOpenNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 210, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstNodeOut RpcTcGetFirstNode_1 (RpcTcGetFirstNodeIn arg_1, RpcTcGetFirstNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 211, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextNodeOut RpcTcGetNextNode_1 (RpcTcGetNextNodeIn arg_1, RpcTcGetNextNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 212, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNodeInfoOut RpcTcGetNodeInfo_1 (RpcTcGetNodeInfoIn arg_1, RpcTcGetNodeInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 213, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetRoutineInfoOut RpcTcGetRoutineInfo_1 (RpcTcGetRoutineInfoIn arg_1, RpcTcGetRoutineInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 214, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetVarInfoOut RpcTcGetVarInfo_1 (RpcTcGetVarInfoIn arg_1, RpcTcGetVarInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 215, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetTypeInfoOut RpcTcGetTypeInfo_1 (RpcTcGetTypeInfoIn arg_1, RpcTcGetTypeInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 216, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetConstInfoOut RpcTcGetConstInfo_1 (RpcTcGetConstInfoIn arg_1, RpcTcGetConstInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 217, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddProgramNodeOut RpcTcAddProgramNode_1 (RpcTcAddProgramNodeIn arg_1, RpcTcAddProgramNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 220, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddRoutineNodeOut RpcTcAddRoutineNode_1 (RpcTcAddRoutineNodeIn arg_1, RpcTcAddRoutineNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 221, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddVarNodeOut RpcTcAddVarNode_1 (RpcTcAddVarNodeIn arg_1, RpcTcAddVarNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 222, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddTypeNodeOut RpcTcAddTypeNode_1 (RpcTcAddTypeNodeIn arg_1, RpcTcAddTypeNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 223, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddConstNodeOut RpcTcAddConstNode_1 (RpcTcAddConstNodeIn arg_1, RpcTcAddConstNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 224, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRemoveNodeOut RpcTcRemoveNode_1 (RpcTcRemoveNodeIn arg_1, RpcTcRemoveNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 225, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRenameNodeOut RpcTcRenameNode_1 (RpcTcRenameNodeIn arg_1, RpcTcRenameNodeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 226, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcWriteDataOut RpcTcWriteData_1 (RpcTcWriteDataIn arg_1, RpcTcWriteDataOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 227, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcWriteDataExtOut RpcTcWriteDataExt_1 (RpcTcWriteDataExtIn arg_1, RpcTcWriteDataExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 228, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstNodeChunkOut RpcTcGetFirstNodeChunk_1 (RpcTcGetFirstNodeChunkIn arg_1, RpcTcGetFirstNodeChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 231, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextNodeChunkOut RpcTcGetNextNodeChunk_1 (RpcTcGetNextNodeChunkIn arg_1, RpcTcGetNextNodeChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 232, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNodeInfoListOut RpcTcGetNodeInfoList_1 (RpcTcGetNodeInfoListIn arg_1, RpcTcGetNodeInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 233, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetRoutineInfoListOut RpcTcGetRoutineInfoList_1 (RpcTcGetRoutineInfoListIn arg_1, RpcTcGetRoutineInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 234, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetVarInfoListOut RpcTcGetVarInfoList_1 (RpcTcGetVarInfoListIn arg_1, RpcTcGetVarInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 235, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetTypeInfoListOut RpcTcGetTypeInfoList_1 (RpcTcGetTypeInfoListIn arg_1, RpcTcGetTypeInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 236, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetConstInfoListOut RpcTcGetConstInfoList_1 (RpcTcGetConstInfoListIn arg_1, RpcTcGetConstInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 237, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetAttributesOut RpcTcGetAttributes_1 (RpcTcGetAttributesIn arg_1, RpcTcGetAttributesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 238, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetAttributesOut RpcTcSetAttributes_1 (RpcTcSetAttributesIn arg_1, RpcTcSetAttributesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 239, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcMoveVarOut RpcTcMoveVar_1 (RpcTcMoveVarIn arg_1, RpcTcMoveVarOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 241, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetActValueOut RpcTcGetActValue_1 (RpcTcGetActValueIn arg_1, RpcTcGetActValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 301, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetActValueOut RpcTcSetActValue_1 (RpcTcSetActValueIn arg_1, RpcTcSetActValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 302, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcOpenVarAccessOut RpcTcOpenVarAccess_1 (RpcTcOpenVarAccessIn arg_1, RpcTcOpenVarAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 303, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetVarAccessOut RpcTcGetVarAccess_1 (RpcTcGetVarAccessIn arg_1, RpcTcGetVarAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 304, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetStructElemAccessOut RpcTcGetStructElemAccess_1 (RpcTcGetStructElemAccessIn arg_1, RpcTcGetStructElemAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 305, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetArrayElemAccessOut RpcTcGetArrayElemAccess_1 (RpcTcGetArrayElemAccessIn arg_1, RpcTcGetArrayElemAccessOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 306, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcCreateInstanceOut RpcTcCreateInstance_1 (RpcTcCreateInstanceIn arg_1, RpcTcCreateInstanceOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 307, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetMapTargetOut RpcTcGetMapTarget_1 (RpcTcGetMapTargetIn arg_1, RpcTcGetMapTargetOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 308, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetSaveValueOut RpcTcGetSaveValue_1 (RpcTcGetSaveValueIn arg_1, RpcTcGetSaveValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 309, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetSaveValueOut RpcTcSetSaveValue_1 (RpcTcSetSaveValueIn arg_1, RpcTcSetSaveValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 310, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcWriteInitValueOut RpcTcWriteInitValue_1 (RpcTcWriteInitValueIn arg_1, RpcTcWriteInitValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 311, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcCopySaveValueOut RpcTcCopySaveValue_1 (RpcTcCopySaveValueIn arg_1, RpcTcCopySaveValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 312, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcCopyActValueOut RpcTcCopyActValue_1 (RpcTcCopyActValueIn arg_1, RpcTcCopyActValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 313, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetMapTargetOut RpcTcSetMapTarget_1 (RpcTcSetMapTargetIn arg_1, RpcTcSetMapTargetOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 314, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetActValueListOut RpcTcGetActValueList_1 (RpcTcGetActValueListIn arg_1, RpcTcGetActValueListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 321, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcOpenVarAccessListOut RpcTcOpenVarAccessList_1 (RpcTcOpenVarAccessListIn arg_1, RpcTcOpenVarAccessListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 322, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetSaveValueListOut RpcTcGetSaveValueList_1 (RpcTcGetSaveValueListIn arg_1, RpcTcGetSaveValueListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 323, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcNewVarGroupOut RpcTcNewVarGroup_1 (RpcTcNewVarGroupIn arg_1, RpcTcNewVarGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 330, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRemoveVarGroupOut RpcTcRemoveVarGroup_1 (RpcTcRemoveVarGroupIn arg_1, RpcTcRemoveVarGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 331, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddVarsToGroupOut RpcTcAddVarsToGroup_1 (RpcTcAddVarsToGroupIn arg_1, RpcTcAddVarsToGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 332, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRemoveVarsFromGroupOut RpcTcRemoveVarsFromGroup_1 (RpcTcRemoveVarsFromGroupIn arg_1, RpcTcRemoveVarsFromGroupOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 333, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcReadVarGroupValuesOut RpcTcReadVarGroupValues_1 (RpcTcReadVarGroupValuesIn arg_1, RpcTcReadVarGroupValuesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 334, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcReadChangedVarGroupValuesOut RpcTcReadChangedVarGroupValues_1 (RpcTcReadChangedVarGroupValuesIn arg_1, RpcTcReadChangedVarGroupValuesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 335, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstDirEntryOut RpcTcGetFirstDirEntry_1 (RpcTcGetFirstDirEntryIn arg_1, RpcTcGetFirstDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 401, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextDirEntryOut RpcTcGetNextDirEntry_1 (RpcTcGetNextDirEntryIn arg_1, RpcTcGetNextDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 402, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetDirEntryInfoOut RpcTcGetDirEntryInfo_1 (RpcTcGetDirEntryInfoIn arg_1, RpcTcGetDirEntryInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 403, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcAddDirEntryOut RpcTcAddDirEntry_1 (RpcTcAddDirEntryIn arg_1, RpcTcAddDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 404, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcCopyDirEntryOut RpcTcCopyDirEntry_1 (RpcTcCopyDirEntryIn arg_1, RpcTcCopyDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 405, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRenameDirEntryOut RpcTcRenameDirEntry_1 (RpcTcRenameDirEntryIn arg_1, RpcTcRenameDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 406, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcDeleteDirEntryOut RpcTcDeleteDirEntry_1 (RpcTcDeleteDirEntryIn arg_1, RpcTcDeleteDirEntryOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 407, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcDeleteDirEntryExtOut RpcTcDeleteDirEntryExt_1 (RpcTcDeleteDirEntryExtIn arg_1, RpcTcDeleteDirEntryExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 408, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstDirEntryChunkOut RpcTcGetFirstDirEntryChunk_1 (RpcTcGetFirstDirEntryChunkIn arg_1, RpcTcGetFirstDirEntryChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 420, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextDirEntryChunkOut RpcTcGetNextDirEntryChunk_1 (RpcTcGetNextDirEntryChunkIn arg_1, RpcTcGetNextDirEntryChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 422, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetDirEntryInfoListOut RpcTcGetDirEntryInfoList_1 (RpcTcGetDirEntryInfoListIn arg_1, RpcTcGetDirEntryInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 423, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcOpenSyntaxEditorOut RpcTcOpenSyntaxEditor_1 (RpcTcOpenSyntaxEditorIn arg_1, RpcTcOpenSyntaxEditorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 501, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcCloseEditorOut RpcTcCloseEditor_1 (RpcTcCloseEditorIn arg_1, RpcTcCloseEditorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 502, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSaveEditorOut RpcTcSaveEditor_1 (RpcTcSaveEditorIn arg_1, RpcTcSaveEditorOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 503, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstLineOut RpcTcGetFirstLine_1 (RpcTcGetFirstLineIn arg_1, RpcTcGetFirstLineOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 504, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextLineOut RpcTcGetNextLine_1 (RpcTcGetNextLineIn arg_1, RpcTcGetNextLineOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 505, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetEditorInfoOut RpcTcGetEditorInfo_1 (RpcTcGetEditorInfoIn arg_1, RpcTcGetEditorInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 506, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcInsertTextOut RpcTcInsertText_1 (RpcTcInsertTextIn arg_1, RpcTcInsertTextOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 507, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcDeleteTextOut RpcTcDeleteText_1 (RpcTcDeleteTextIn arg_1, RpcTcDeleteTextOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 508, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetValidTokensOut RpcTcGetValidTokens_1 (RpcTcGetValidTokensIn arg_1, RpcTcGetValidTokensOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 509, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstSymbolOut RpcTcGetFirstSymbol_1 (RpcTcGetFirstSymbolIn arg_1, RpcTcGetFirstSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 512, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextSymbolOut RpcTcGetNextSymbol_1 (RpcTcGetNextSymbolIn arg_1, RpcTcGetNextSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 513, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetSymbolTextRangeOut RpcTcGetSymbolTextRange_1 (RpcTcGetSymbolTextRangeIn arg_1, RpcTcGetSymbolTextRangeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 514, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNodeTextRangeOut RpcTcGetNodeTextRange_1 (RpcTcGetNodeTextRangeIn arg_1, RpcTcGetNodeTextRangeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 515, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetSymbolTextOut RpcTcGetSymbolText_1 (RpcTcGetSymbolTextIn arg_1, RpcTcGetSymbolTextOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 516, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcInsertTerminalSymbolOut RpcTcInsertTerminalSymbol_1 (RpcTcInsertTerminalSymbolIn arg_1, RpcTcInsertTerminalSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 518, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcInsertNonTerminalSymbolOut RpcTcInsertNonTerminalSymbol_1 (RpcTcInsertNonTerminalSymbolIn arg_1, RpcTcInsertNonTerminalSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 519, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcDeleteSymbolOut RpcTcDeleteSymbol_1 (RpcTcDeleteSymbolIn arg_1, RpcTcDeleteSymbolOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 520, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcInsertStatementOut RpcTcInsertStatement_1 (RpcTcInsertStatementIn arg_1, RpcTcInsertStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 521, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcDeleteStatementOut RpcTcDeleteStatement_1 (RpcTcDeleteStatementIn arg_1, RpcTcDeleteStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 522, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcMoveStatementOut RpcTcMoveStatement_1 (RpcTcMoveStatementIn arg_1, RpcTcMoveStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 523, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcReplaceStatementOut RpcTcReplaceStatement_1 (RpcTcReplaceStatementIn arg_1, RpcTcReplaceStatementOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 524, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcReplaceCommentOut RpcTcReplaceComment_1 (RpcTcReplaceCommentIn arg_1, RpcTcReplaceCommentOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 525, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstSymbolChunkOut RpcTcGetFirstSymbolChunk_1 (RpcTcGetFirstSymbolChunkIn arg_1, RpcTcGetFirstSymbolChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 531, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextSymbolChunkOut RpcTcGetNextSymbolChunk_1 (RpcTcGetNextSymbolChunkIn arg_1, RpcTcGetNextSymbolChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 532, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstLineChunkOut RpcTcGetFirstLineChunk_1 (RpcTcGetFirstLineChunkIn arg_1, RpcTcGetFirstLineChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 533, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextLineChunkOut RpcTcGetNextLineChunk_1 (RpcTcGetNextLineChunkIn arg_1, RpcTcGetNextLineChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 534, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcOpenSyntaxEditorExtOut RpcTcOpenSyntaxEditorExt_1 (RpcTcOpenSyntaxEditorExtIn arg_1, RpcTcOpenSyntaxEditorExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 540, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcLoadGlobalOut RpcTcLoadGlobal_1 (RpcTcLoadGlobalIn arg_1, RpcTcLoadGlobalOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 601, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcUnloadGlobalOut RpcTcUnloadGlobal_1 (RpcTcUnloadGlobalIn arg_1, RpcTcUnloadGlobalOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 602, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcLoadProjOut RpcTcLoadProj_1 (RpcTcLoadProjIn arg_1, RpcTcLoadProjOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 603, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcUnloadProjOut RpcTcUnloadProj_1 (RpcTcUnloadProjIn arg_1, RpcTcUnloadProjOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 604, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcStartProgOut RpcTcStartProg_1 (RpcTcStartProgIn arg_1, RpcTcStartProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 605, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcInterruptProgOut RpcTcInterruptProg_1 (RpcTcInterruptProgIn arg_1, RpcTcInterruptProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 606, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcContinueProgOut RpcTcContinueProg_1 (RpcTcContinueProgIn arg_1, RpcTcContinueProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 607, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcStopProgOut RpcTcStopProg_1 (RpcTcStopProgIn arg_1, RpcTcStopProgOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 608, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstExeUnitOut RpcTcGetFirstExeUnit_1 (RpcTcGetFirstExeUnitIn arg_1, RpcTcGetFirstExeUnitOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 609, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextExeUnitOut RpcTcGetNextExeUnit_1 (RpcTcGetNextExeUnitIn arg_1, RpcTcGetNextExeUnitOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 610, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetExeUnitInfoOut RpcTcGetExeUnitInfo_1 (RpcTcGetExeUnitInfoIn arg_1, RpcTcGetExeUnitInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 611, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetExeUnitStatusOut RpcTcGetExeUnitStatus_1 (RpcTcGetExeUnitStatusIn arg_1, RpcTcGetExeUnitStatusOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 612, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcExecuteMethodOut RpcTcExecuteMethod_1 (RpcTcExecuteMethodIn arg_1, RpcTcExecuteMethodOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 613, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcStartProgExOut RpcTcStartProgEx_1 (RpcTcStartProgExIn arg_1, RpcTcStartProgExOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 614, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcExecuteMethodExtOut RpcTcExecuteMethodExt_1 (RpcTcExecuteMethodExtIn arg_1, RpcTcExecuteMethodExtOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 615, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetFirstExeUnitChunkOut RpcTcGetFirstExeUnitChunk_1 (RpcTcGetFirstExeUnitChunkIn arg_1, RpcTcGetFirstExeUnitChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 621, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetNextExeUnitChunkOut RpcTcGetNextExeUnitChunk_1 (RpcTcGetNextExeUnitChunkIn arg_1, RpcTcGetNextExeUnitChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 622, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetExeUnitInfoListOut RpcTcGetExeUnitInfoList_1 (RpcTcGetExeUnitInfoListIn arg_1, RpcTcGetExeUnitInfoListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 623, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetExeUnitStatusListOut RpcTcGetExeUnitStatusList_1 (RpcTcGetExeUnitStatusListIn arg_1, RpcTcGetExeUnitStatusListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 624, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetInstructionPointerOut RpcTcSetInstructionPointer_1 (RpcTcSetInstructionPointerIn arg_1, RpcTcSetInstructionPointerOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 625, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetMultExeUnitChunkOut RpcTcGetMultExeUnitChunk_1 (RpcTcGetMultExeUnitChunkIn arg_1, RpcTcGetMultExeUnitChunkOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 626, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetDebugModeOut RpcTcSetDebugMode_1 (RpcTcSetDebugModeIn arg_1, RpcTcSetDebugModeOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 701, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcStepOut RpcTcStep_1 (RpcTcStepIn arg_1, RpcTcStepOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 702, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetCodePointOut RpcTcSetCodePoint_1 (RpcTcSetCodePointIn arg_1, RpcTcSetCodePointOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 704, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRemoveCodePointOut RpcTcRemoveCodePoint_1 (RpcTcRemoveCodePointIn arg_1, RpcTcRemoveCodePointOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 705, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetCodePointsOut RpcTcGetCodePoints_1 (RpcTcGetCodePointsIn arg_1, RpcTcGetCodePointsOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 706, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcActivateCodePointOut RpcTcActivateCodePoint_1 (RpcTcActivateCodePointIn arg_1, RpcTcActivateCodePointOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 707, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetWatchPointCounterOut RpcTcGetWatchPointCounter_1 (RpcTcGetWatchPointCounterIn arg_1, RpcTcGetWatchPointCounterOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 708, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcResetWatchPointCounterOut RpcTcResetWatchPointCounter_1 (RpcTcResetWatchPointCounterIn arg_1, RpcTcResetWatchPointCounterOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 709, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetWatchPointVarOut RpcTcSetWatchPointVar_1 (RpcTcSetWatchPointVarIn arg_1, RpcTcSetWatchPointVarOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 710, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcRemoveWatchPointVarOut RpcTcRemoveWatchPointVar_1 (RpcTcRemoveWatchPointVarIn arg_1, RpcTcRemoveWatchPointVarOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 711, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetWatchPointValueOut RpcTcGetWatchPointValue_1 (RpcTcGetWatchPointValueIn arg_1, RpcTcGetWatchPointValueOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 712, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetWatchPointVarsOut RpcTcGetWatchPointVars_1 (RpcTcGetWatchPointVarsIn arg_1, RpcTcGetWatchPointVarsOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 713, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetWatchPointTriggerOut RpcTcSetWatchPointTrigger_1 (RpcTcSetWatchPointTriggerIn arg_1, RpcTcSetWatchPointTriggerOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 714, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetExecInfoOut RpcTcGetExecInfo_1 (RpcTcGetExecInfoOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 715, retVal, args_length, null);
			return retVal;
		}
	}
	public RpcTcStepListOut RpcTcStepList_1 (RpcTcStepListIn arg_1, RpcTcStepListOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 730, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcSetExeFlagOut RpcTcSetExeFlag_1 (RpcTcSetExeFlagIn arg_1, RpcTcSetExeFlagOut retVal) throws RPCException, IOException {
		synchronized (args) {
			args_length = 1;
			args[0] = arg_1;
			call(prog, OLIRPC_VERS, 731, retVal, args_length, args);
			return retVal;
		}
	}
	public RpcTcGetCodePointRoutinesOut RpcTcGetCodePointRoutines_1 (RpcTcGetCodePointRoutinesOut retVal) throws RPCException, IOException {
		synchronized (args) {
			call(prog, OLIRPC_VERS, 732, retVal, args_length, null);
			return retVal;
		}
	}
}