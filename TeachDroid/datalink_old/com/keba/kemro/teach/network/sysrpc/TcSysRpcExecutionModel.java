package com.keba.kemro.teach.network.sysrpc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.keba.jrpc.rpc.RPCException;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcAddVarsToGroupIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcAddVarsToGroupOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcContinueProgIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcContinueProgOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcCopyActValueIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcCopyActValueOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcExeFlags;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcExeUnitKind;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcExecuteMethodExtIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcExecuteMethodExtOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetActArrayValuesIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetActArrayValuesOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetActValueIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetActValueListIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetActValueListOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetActValueOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetExeUnitInfoListIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetExeUnitInfoListOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetExeUnitStatusIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetExeUnitStatusListIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetExeUnitStatusListOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetExeUnitStatusOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetFirstExeUnitChunkIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetFirstExeUnitChunkOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetMapTargetIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetMapTargetOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetMemDumpIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetMemDumpOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetMultExeUnitChunkIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetMultExeUnitChunkOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetNextExeUnitChunkIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetNextExeUnitChunkOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetTypeInfoIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetTypeInfoOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetVarInfoIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcGetVarInfoOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcInstancePathElem;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcInterruptProgIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcInterruptProgOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcLoadGlobalIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcLoadGlobalOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcLoadProjIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcLoadProjOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcMethodParam;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcNewVarGroupIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcNewVarGroupOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcReadChangedVarGroupValuesIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcReadChangedVarGroupValuesOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcReadVarGroupValuesIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcReadVarGroupValuesOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcRemoveVarGroupIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcRemoveVarGroupOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcRemoveVarsFromGroupIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcRemoveVarsFromGroupOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetActValueIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetActValueOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetDebugModeIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetDebugModeOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetExeFlagIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetExeFlagOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetInstructionPointerIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetInstructionPointerOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetMapTargetIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcSetMapTargetOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStartFlags;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStartProgExIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStartProgExOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStartProgIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStartProgOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStepIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStepListIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStepListOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStepOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStopProgIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcStopProgOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcUnloadGlobalIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcUnloadGlobalOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcUnloadProjIn;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcUnloadProjOut;
import com.keba.kemro.plc.network.sysrpc.TCI.SysRpcTcVarAccess;
import com.keba.kemro.plc.network.sysrpc.TCI.TCI;

import com.keba.kemro.teach.network.TcAccessHandle;
import com.keba.kemro.teach.network.TcExecutionModel;
import com.keba.kemro.teach.network.TcExecutionState;
import com.keba.kemro.teach.network.TcExecutionUnit;
import com.keba.kemro.teach.network.TcMapTarget;
import com.keba.kemro.teach.network.TcStructuralNode;
import com.keba.kemro.teach.network.TcStructuralRoutineNode;
import com.keba.kemro.teach.network.TcStructuralTypeNode;
import com.keba.kemro.teach.network.TcStructuralVarNode;
import com.keba.kemro.teach.network.TcValue;
import com.keba.kemro.teach.network.TcVariableGroup;
import com.keba.kemro.teach.network.sysrpc.TcSysRpcStructuralModel.AccessHandle;

public class TcSysRpcExecutionModel implements TcExecutionModel {
	/** alle Ausführungseinheiten */
	private static final int							FILTER_ALL								= SysRpcTcExeUnitKind.rpcFilterAllExeUnit;
	/** End user execution routine */
	private static final int							FILTER_USER_ROUTINE						= SysRpcTcExeUnitKind.rpcFilterUserRoutineExeUnit;
	/** End user execution routine */
	private static final int							FILTER_ALL_USER							= SysRpcTcExeUnitKind.rpcFilterAllUserExeUnit;
	/**
	 * TC Version < 3.00: Hauptablaufzeiger - Debug - Modus, in diesem Modus
	 * sind alle namenlose Routinen, erreichbar von der Ablaufwurzel, im Debug -
	 * Modus.
	 */
	private static final int							DEBUG_MODE_MAIN_FLOW					= 3;
	private static final int							EXECUTION_MODE_MAIN_FLOW_STEPPING		= SysRpcTcExeFlags.rpcExeFlagMainFlowStepping;

	private final SysRpcTcLoadGlobalIn					SysRpcTcLoadGlobalIn					= new SysRpcTcLoadGlobalIn();
	private final SysRpcTcLoadGlobalOut					SysRpcTcLoadGlobalOut					= new SysRpcTcLoadGlobalOut();
	private final SysRpcTcUnloadGlobalIn				SysRpcTcUnloadGlobalIn					= new SysRpcTcUnloadGlobalIn();
	private final SysRpcTcUnloadGlobalOut				SysRpcTcUnloadGlobalOut					= new SysRpcTcUnloadGlobalOut();
	private final SysRpcTcLoadProjIn					SysRpcTcLoadProjIn						= new SysRpcTcLoadProjIn();
	private final SysRpcTcLoadProjOut					SysRpcTcLoadProjOut						= new SysRpcTcLoadProjOut();
	private final SysRpcTcUnloadProjIn					SysRpcTcUnloadProjIn					= new SysRpcTcUnloadProjIn();
	private final SysRpcTcUnloadProjOut					SysRpcTcUnloadProjOut					= new SysRpcTcUnloadProjOut();
	private final SysRpcTcStartProgIn					SysRpcTcStartProgIn						= new SysRpcTcStartProgIn();
	private final SysRpcTcStartProgOut					SysRpcTcStartProgOut					= new SysRpcTcStartProgOut();
	private final SysRpcTcStartProgExIn					SysRpcTcStartProgExIn					= new SysRpcTcStartProgExIn();
	private final SysRpcTcStartProgExOut				SysRpcTcStartProgExOut					= new SysRpcTcStartProgExOut();
	private final SysRpcTcInterruptProgIn				SysRpcTcInterruptProgIn					= new SysRpcTcInterruptProgIn();
	private final SysRpcTcInterruptProgOut				SysRpcTcInterruptProgOut				= new SysRpcTcInterruptProgOut();
	private final SysRpcTcContinueProgIn				SysRpcTcContinueProgIn					= new SysRpcTcContinueProgIn();
	private final SysRpcTcContinueProgOut				SysRpcTcContinueProgOut					= new SysRpcTcContinueProgOut();
	private final SysRpcTcStopProgIn					SysRpcTcStopProgIn						= new SysRpcTcStopProgIn();
	private final SysRpcTcStopProgOut					SysRpcTcStopProgOut						= new SysRpcTcStopProgOut();
	private final SysRpcTcSetDebugModeIn				SysRpcTcSetDebugModeIn					= new SysRpcTcSetDebugModeIn();
	private final SysRpcTcSetDebugModeOut				SysRpcTcSetDebugModeOut					= new SysRpcTcSetDebugModeOut();
	private final SysRpcTcGetExeUnitStatusIn			SysRpcTcGetExeUnitStatusIn				= new SysRpcTcGetExeUnitStatusIn();
	private final SysRpcTcGetExeUnitStatusOut			SysRpcTcGetExeUnitStatusOut				= new SysRpcTcGetExeUnitStatusOut();
	private final SysRpcTcGetExeUnitStatusListIn		SysRpcTcGetExeUnitStatusListIn			= new SysRpcTcGetExeUnitStatusListIn();
	private final SysRpcTcGetExeUnitStatusListOut		SysRpcTcGetExeUnitStatusListOut			= new SysRpcTcGetExeUnitStatusListOut();
	private final SysRpcTcStepIn						SysRpcTcStepIn							= new SysRpcTcStepIn();
	private final SysRpcTcStepOut						SysRpcTcStepOut							= new SysRpcTcStepOut();
	private final SysRpcTcStepListIn					SysRpcTcStepListIn						= new SysRpcTcStepListIn();
	private final SysRpcTcStepListOut					SysRpcTcStepListOut						= new SysRpcTcStepListOut();
	private final SysRpcTcGetExeUnitInfoListIn			SysRpcTcGetExeUnitInfoListIn			= new SysRpcTcGetExeUnitInfoListIn();
	private final SysRpcTcGetExeUnitInfoListOut			SysRpcTcGetExeUnitInfoListOut			= new SysRpcTcGetExeUnitInfoListOut();
	private final SysRpcTcGetFirstExeUnitChunkIn		SysRpcTcGetFirstExeUnitChunkIn			= new SysRpcTcGetFirstExeUnitChunkIn();
	private final SysRpcTcGetFirstExeUnitChunkOut		SysRpcTcGetFirstExeUnitChunkOut			= new SysRpcTcGetFirstExeUnitChunkOut();
	private final SysRpcTcGetNextExeUnitChunkIn			SysRpcTcGetNextExeUnitChunkIn			= new SysRpcTcGetNextExeUnitChunkIn();
	private final SysRpcTcGetNextExeUnitChunkOut		SysRpcTcGetNextExeUnitChunkOut			= new SysRpcTcGetNextExeUnitChunkOut();
	private final SysRpcTcSetInstructionPointerIn		SysRpcTcSetInstructionPointerIn			= new SysRpcTcSetInstructionPointerIn();
	private final SysRpcTcSetInstructionPointerOut		SysRpcTcSetInstructionPointerOut		= new SysRpcTcSetInstructionPointerOut();
	private final SysRpcTcExecuteMethodExtIn			SysRpcTcExecuteMethodExtIn				= new SysRpcTcExecuteMethodExtIn();
	private final SysRpcTcExecuteMethodExtOut			SysRpcTcExecuteMethodExtOut				= new SysRpcTcExecuteMethodExtOut();
	private final SysRpcTcGetMultExeUnitChunkIn			SysRpcTcGetMultExeUnitChunkIn			= new SysRpcTcGetMultExeUnitChunkIn();
	private final SysRpcTcGetMultExeUnitChunkOut		SysRpcTcGetMultExeUnitChunkOut			= new SysRpcTcGetMultExeUnitChunkOut();
	private final SysRpcTcSetExeFlagIn					SysRpcTcSetExeFlagIn					= new SysRpcTcSetExeFlagIn();
	private final SysRpcTcSetExeFlagOut					SysRpcTcSetExeFlagOut					= new SysRpcTcSetExeFlagOut();
	private final SysRpcTcGetMapTargetIn				SysRpcTcGetMapTargetIn					= new SysRpcTcGetMapTargetIn();
	private final SysRpcTcGetMapTargetOut				SysRpcTcGetMapTargetOut					= new SysRpcTcGetMapTargetOut();
	private final SysRpcTcSetMapTargetIn				SysRpcTcSetMapTargetIn					= new SysRpcTcSetMapTargetIn();
	private final SysRpcTcSetMapTargetOut				SysRpcTcSetMapTargetOut					= new SysRpcTcSetMapTargetOut();
	private final SysRpcTcGetActValueIn					SysRpcTcGetActValueIn					= new SysRpcTcGetActValueIn();
	private final SysRpcTcGetActValueOut				SysRpcTcGetActValueOut					= new SysRpcTcGetActValueOut();
	private final SysRpcTcGetActValueListIn				SysRpcTcGetActValueListIn				= new SysRpcTcGetActValueListIn();
	private final SysRpcTcGetActValueListOut			SysRpcTcGetActValueListOut				= new SysRpcTcGetActValueListOut();
	private final SysRpcTcSetActValueIn					SysRpcTcSetActValueIn					= new SysRpcTcSetActValueIn();
	private final SysRpcTcSetActValueOut				SysRpcTcSetActValueOut					= new SysRpcTcSetActValueOut();
	private final SysRpcTcCopyActValueIn				SysRpcTcCopyActValueIn					= new SysRpcTcCopyActValueIn();
	private final SysRpcTcCopyActValueOut				SysRpcTcCopyActValueOut					= new SysRpcTcCopyActValueOut();
	private final SysRpcTcNewVarGroupIn					SysRpcTcNewVarGroupIn					= new SysRpcTcNewVarGroupIn();
	private final SysRpcTcNewVarGroupOut				SysRpcTcNewVarGroupOut					= new SysRpcTcNewVarGroupOut();
	private final SysRpcTcRemoveVarGroupIn				SysRpcTcRemoveVarGroupIn				= new SysRpcTcRemoveVarGroupIn();
	private final SysRpcTcRemoveVarGroupOut				SysRpcTcRemoveVarGroupOut				= new SysRpcTcRemoveVarGroupOut();
	private final SysRpcTcAddVarsToGroupIn				SysRpcTcAddVarsToGroupIn				= new SysRpcTcAddVarsToGroupIn();
	private final SysRpcTcAddVarsToGroupOut				SysRpcTcAddVarsToGroupOut				= new SysRpcTcAddVarsToGroupOut();
	private final SysRpcTcRemoveVarsFromGroupIn			SysRpcTcRemoveVarsFromGroupIn			= new SysRpcTcRemoveVarsFromGroupIn();
	private final SysRpcTcRemoveVarsFromGroupOut		SysRpcTcRemoveVarsFromGroupOut			= new SysRpcTcRemoveVarsFromGroupOut();
	private final SysRpcTcReadVarGroupValuesIn			SysRpcTcReadVarGroupValuesIn			= new SysRpcTcReadVarGroupValuesIn();
	private final SysRpcTcReadVarGroupValuesOut			SysRpcTcReadVarGroupValuesOut			= new SysRpcTcReadVarGroupValuesOut();
	private final SysRpcTcReadChangedVarGroupValuesIn	SysRpcTcReadChangedVarGroupValuesIn		= new SysRpcTcReadChangedVarGroupValuesIn();
	private final SysRpcTcReadChangedVarGroupValuesOut	SysRpcTcReadChangedVarGroupValuesOut	= new SysRpcTcReadChangedVarGroupValuesOut();

	private final SysRpcTcGetMemDumpIn					SysRpcTcGetMemDumpIn					= new SysRpcTcGetMemDumpIn();
	private final SysRpcTcGetMemDumpOut					SysRpcTcGetMemDumpOut					= new SysRpcTcGetMemDumpOut();

	TcSysRpcClient										client;
	private boolean										disconnected;

	TcSysRpcExecutionModel(TcSysRpcClient client) {
		this.client = client;
	}

	/**
	 * Liefert für den angegebene Zugriffs - Handle der Variable den Aktualwert.
	 * 
	 * @param accessHandle
	 *            Zugriffs - Handle
	 * @param exeUnit
	 *            Ausführungseinheit des Programms, in der die Variable
	 *            deklariert wurde
	 * @param value
	 *            Aktualwert
	 * 
	 * @return true für einen erfolgreichen Aufruf
	 */
	public boolean getActualValue(TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcValue value) {
		try {
			synchronized (SysRpcTcGetActValueIn) {
				SysRpcTcGetActValueIn.exeUnitHnd = (execUnit != null) ? ((TcSysRpcExecutionUnit) execUnit).getHandle() : 0;
				SysRpcTcGetActValueIn.varAccess = ((TcSysRpcStructuralModel.AccessHandle) accessHandle).access;
				client.client.SysRpcTcGetActValue_1(SysRpcTcGetActValueIn, SysRpcTcGetActValueOut);
				if (SysRpcTcGetActValueOut.retVal) {
					value.boolValue = SysRpcTcGetActValueOut.value.bValue;
					value.int8Value = (byte) SysRpcTcGetActValueOut.value.i8Value;
					value.int16Value = (short) SysRpcTcGetActValueOut.value.i16Value;
					value.int32Value = SysRpcTcGetActValueOut.value.i32Value;
					value.int64Value = SysRpcTcGetActValueOut.value.i64Value;
					value.floatValue = SysRpcTcGetActValueOut.value.fValue;
					value.stringValue = SysRpcTcGetActValueOut.value.sValue.toString();
					value.isValid = SysRpcTcGetActValueOut.value.isValid;
					return true;
				}
				value.isValid = false;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getActualValue: ");
		}
		return false;
	}

	private void disconnected(String text) {
		if (!disconnected) {
			disconnected = true;
			disconnected(text);
		}
	}

	/**
	 * Setzt für den angegebene Zugriffs - Handle der Variable den Aktualwert.
	 * 
	 * @param accessHandle
	 *            Zugriffs - Handle
	 * @param unit
	 *            Ausführungseinheit des Programms, in der die Variable
	 *            deklariert wurde
	 * @param value
	 *            Aktualwert
	 * 
	 * @return true für einen erfolgreichen Aufruf
	 */
	public boolean setActualValue(TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcValue value) {
		try {
			synchronized (SysRpcTcSetActValueIn) {
				SysRpcTcSetActValueIn.exeUnitHnd = (execUnit != null) ? ((TcSysRpcExecutionUnit) execUnit).getHandle() : 0;
				SysRpcTcSetActValueIn.varAccess = ((TcSysRpcStructuralModel.AccessHandle) accessHandle).access;
				SysRpcTcSetActValueIn.value.bValue = value.boolValue;
				SysRpcTcSetActValueIn.value.i8Value = value.int8Value;
				SysRpcTcSetActValueIn.value.i16Value = value.int16Value;
				SysRpcTcSetActValueIn.value.i32Value = value.int32Value;
				SysRpcTcSetActValueIn.value.i64Value = value.int64Value;
				SysRpcTcSetActValueIn.value.fValue = value.floatValue;
				SysRpcTcSetActValueIn.value.sValue = value.stringValue;
				client.client.SysRpcTcSetActValue_1(SysRpcTcSetActValueIn, SysRpcTcSetActValueOut);
				return SysRpcTcSetActValueOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getInitValue: ");
		}
		return false;
	}

	/**
	 * Copies the actual value from srouce variable to the destination variable
	 * 
	 * @param srcAccessHandle
	 *            access handle of the source variable
	 * @param srcExeUnit
	 *            executioin handle of the routine, only necessary if the source
	 *            variable is a routine variable
	 * @param destAccessHandle
	 *            access handle of the destination variable
	 * @param destExeUnit
	 *            executioin handle of the routine, only necessary if the
	 *            destination variable is a routine variable
	 * @return true if the value was successfully copied
	 */
	public boolean copyActualValue(TcAccessHandle srcAccessHandle, TcExecutionUnit srcExecUnit, TcAccessHandle destAccessHandle, TcExecutionUnit destExecUnit) {
		try {
			synchronized (SysRpcTcCopyActValueIn) {
				SysRpcTcCopyActValueIn.srcExeUnitHnd = (srcExecUnit != null) ? ((TcSysRpcExecutionUnit) srcExecUnit).getHandle() : 0;
				SysRpcTcCopyActValueIn.srcVarAccess = ((TcSysRpcStructuralModel.AccessHandle) srcAccessHandle).access;
				SysRpcTcCopyActValueIn.destExeUnitHnd = (destExecUnit != null) ? ((TcSysRpcExecutionUnit) destExecUnit).getHandle() : 0;
				SysRpcTcCopyActValueIn.destVarAccess = ((TcSysRpcStructuralModel.AccessHandle) destAccessHandle).access;
				client.client.SysRpcTcCopyActValue_1(SysRpcTcCopyActValueIn, SysRpcTcCopyActValueOut);
				return SysRpcTcCopyActValueOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - copyActualValue: ");
		}
		return false;
	}

	/**
	 * Lefert für die angegebenen Zugriffs - Handles der Variablen die
	 * Aktualwerte.
	 * 
	 * @param accessHandles
	 *            enthält Zugriffs - Handles
	 * @param execUnits
	 *            Ausführungseinheiten der Programme, in denen die Variablen
	 *            deklariert wurden
	 * @param values
	 *            Aktualwerte
	 * 
	 * @return true für einen erfolgreichen Aufruf
	 */
	public boolean getActualValues(Vector accessHandles, Vector execUnits, Vector values) {
		try {
			synchronized (SysRpcTcGetActValueListIn) {
				int index = 0;
				while (index < execUnits.size()) {
					int end = index + TCI.rpcChunkLen;
					if (end >= execUnits.size()) {
						end = execUnits.size();
					}
					if (SysRpcTcGetActValueListIn.exeUnitHnd == null) {
						SysRpcTcGetActValueListIn.exeUnitHnd = new int[TCI.rpcChunkLen];
						SysRpcTcGetActValueListIn.varAccess = new SysRpcTcVarAccess[TCI.rpcChunkLen];
					}
					for (int i = index; i < end; i++) {
						TcSysRpcExecutionUnit oeu = (TcSysRpcExecutionUnit) execUnits.elementAt(i);
						AccessHandle ah = (AccessHandle) accessHandles.elementAt(i);
						SysRpcTcGetActValueListIn.exeUnitHnd[i - index] = (oeu != null) ? oeu.getHandle() : 0;
						SysRpcTcGetActValueListIn.varAccess[i - index] = ah.access;
					}
					SysRpcTcGetActValueListIn.nrOfActValues = end - index;
					SysRpcTcGetActValueListIn.exeUnitHnd_count = end - index;
					SysRpcTcGetActValueListIn.varAccess_count = end - index;
					client.client.SysRpcTcGetActValueList_1(SysRpcTcGetActValueListIn, SysRpcTcGetActValueListOut);
					if (SysRpcTcGetActValueListOut.retVal) {
						for (int i = index; i < end; i++) {
							TcValue v = (TcValue) values.elementAt(i);
							v.boolValue = SysRpcTcGetActValueListOut.value[i - index].bValue;
							v.int8Value = (byte) SysRpcTcGetActValueListOut.value[i - index].i8Value;
							v.int16Value = (short) SysRpcTcGetActValueListOut.value[i - index].i16Value;
							v.int32Value = SysRpcTcGetActValueListOut.value[i - index].i32Value;
							v.int64Value = SysRpcTcGetActValueListOut.value[i - index].i64Value;
							v.floatValue = SysRpcTcGetActValueListOut.value[i - index].fValue;
							v.stringValue = SysRpcTcGetActValueListOut.value[i - index].sValue.toString();
							v.isValid = SysRpcTcGetActValueListOut.value[i - index].isValid;
						}
					} else {
						return false;
					}
					index = index + TCI.rpcChunkLen;
				}
				return true;
			}
		} catch (Exception e) {
			// disconnected("Disconnect in TcStructuralModel -
			// getActualValues: ");
		}
		return false;
	}

	/**
	 * Gets the map target.
	 * 
	 * @param accessHandle
	 *            access handle of the variable
	 * @param exeUnit
	 *            execution handle of the routine to which the variable belongs
	 * @param target
	 *            mag target
	 * @return true if the target is successfully gotten
	 */
	public boolean getMapTarget(TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcMapTarget target) {
		target.setValid(false);
		try {
			synchronized (SysRpcTcGetMapTargetIn) {
				SysRpcTcGetMapTargetIn.exeUnitHnd = (execUnit != null) ? ((TcSysRpcExecutionUnit) execUnit).getHandle() : 0;
				SysRpcTcGetMapTargetIn.varAccess = ((AccessHandle) accessHandle).access;
				client.client.SysRpcTcGetMapTarget_1(SysRpcTcGetMapTargetIn, SysRpcTcGetMapTargetOut);
				if (SysRpcTcGetMapTargetOut.retVal && (SysRpcTcGetMapTargetOut.target.kind.value != TcMapTarget.MAP_TARGET_VOID)) {
					boolean isInternal = SysRpcTcGetMapTargetOut.target.kind.value == TcMapTarget.MAP_TARGET_INTERNAL;
					Object[] path = null;
					TcStructuralRoutineNode sr = null;
					String external = null;
					if (isInternal) {
						path = new Object[SysRpcTcGetMapTargetOut.target.instancePath.nrOfElems];
						for (int j = 0; j < SysRpcTcGetMapTargetOut.target.instancePath.nrOfElems; j++) {
							if (SysRpcTcGetMapTargetOut.target.instancePath.elems[j].structComponent == 0) {
								path[j] = new Integer(SysRpcTcGetMapTargetOut.target.instancePath.elems[j].arrayIndex);
							} else {
								if (j == 0) {
									// first element is maybe a program
									int handle = SysRpcTcGetMapTargetOut.target.instancePath.elems[j].structComponent;
									TcStructuralNode tn = client.cache.getFromDirEntryCache(handle);
									if (tn != null) {
										path[j] = tn;
									} else {
										path[j] = ((TcSysRpcStructuralModel) client.structure).createNode(handle);
									}
								} else {
									path[j] = new TcSysRpcStructuralVarNode(SysRpcTcGetMapTargetOut.target.instancePath.elems[j].structComponent, client);
								}
							}
						}
						sr = (SysRpcTcGetMapTargetOut.target.routineHdl != 0) ? new TcSysRpcStructuralRoutineNode(SysRpcTcGetMapTargetOut.target.routineHdl, client) : null;
					} else {
						external = SysRpcTcGetMapTargetOut.target.external.toString();
					}
					((TcMapTarget) target).setValues(isInternal, path, sr, external);
					((TcMapTarget) target).setValid(true);
					return true;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getMapTarget: ");
		}
		return false;
	}

	/**
	 * Sets the map target. Target is a teachtalk variable or component.
	 * 
	 * @param accessHandle
	 *            map to variable
	 * @param exeUnit
	 *            execution handle of the routine to which the variable belongs
	 * @param path
	 *            instance path of the destination
	 * @return true if the target is successfully set
	 */
	public boolean setMapTarget(TcAccessHandle accessHandle, TcExecutionUnit execUnit, Object[] path) {
		TcMapTarget target = new TcMapTarget();
		target.setValues(true, path, null, null);
		return setMapTarget(accessHandle, execUnit, target);
	}

	/**
	 * Sets the map target. Target is program routine or a unit routine.
	 * 
	 * @param accessHandle
	 *            map to variable
	 * @param exeUnit
	 *            execution handle of the routine to which the variable belongs
	 * @param path
	 *            if routine is a routine of the unit then the the instance path
	 *            is the instance of the unit variable
	 * @param routine
	 *            new routine destination
	 * @return true if the target is successfully set
	 */
	public boolean setMapTarget(TcAccessHandle accessHandle, TcExecutionUnit execUnit, Object[] path, TcStructuralRoutineNode routine) {
		TcMapTarget target = new TcMapTarget();
		target.setValues(true, path, routine, null);
		return setMapTarget(accessHandle, execUnit, target);
	}

	/**
	 * Sets the map target.
	 * 
	 * @param accessHandle
	 *            map to variable
	 * @param exeUnit
	 *            execution handle of the routine to which the variable belongs
	 * @param external
	 *            new external variable destination
	 * @return true if the target is successfully set
	 */
	public boolean setMapTarget(TcAccessHandle accessHandle, TcExecutionUnit execUnit, String external) {
		TcMapTarget target = new TcMapTarget();
		target.setValues(false, null, null, external);
		return setMapTarget(accessHandle, execUnit, target);
	}

	private boolean setMapTarget(TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcMapTarget target) {
		try {
			synchronized (SysRpcTcSetMapTargetIn) {
				SysRpcTcSetMapTargetIn.varAccess = ((AccessHandle) accessHandle).access;
				SysRpcTcSetMapTargetIn.exeUnitHnd = (execUnit != null) ? ((TcSysRpcExecutionUnit) execUnit).getHandle() : 0;
				if (target.isInternal()) {
					SysRpcTcSetMapTargetIn.target.kind.value = TcMapTarget.MAP_TARGET_INTERNAL;
					Object[] path = target.getPath();
					if (path != null) {
						SysRpcTcSetMapTargetIn.target.instancePath.nrOfElems = path.length;
						SysRpcTcSetMapTargetIn.target.instancePath.elems_count = path.length;
						if (SysRpcTcSetMapTargetIn.target.instancePath.elems == null) {
							SysRpcTcSetMapTargetIn.target.instancePath.elems = new SysRpcTcInstancePathElem[TCI.rpcMaxInstancePathElems];
						}
						for (int i = 0; i < path.length; i++) {
							if (SysRpcTcSetMapTargetIn.target.instancePath.elems[i] == null) {
								SysRpcTcSetMapTargetIn.target.instancePath.elems[i] = new SysRpcTcInstancePathElem();
							}
							if (path[i] instanceof TcStructuralNode) {
								SysRpcTcSetMapTargetIn.target.instancePath.elems[i].structComponent = ((TcSysRpcStructuralNode) path[i]).getHandle();
								SysRpcTcSetMapTargetIn.target.instancePath.elems[i].arrayIndex = -1;
							} else {
								SysRpcTcSetMapTargetIn.target.instancePath.elems[i].structComponent = 0;
								SysRpcTcSetMapTargetIn.target.instancePath.elems[i].arrayIndex = ((Integer) path[i]).intValue();
							}
						}
					} else {
						SysRpcTcSetMapTargetIn.target.instancePath.nrOfElems = 0;
						SysRpcTcSetMapTargetIn.target.instancePath.elems_count = 0;
					}
					if (target.isInternalMapToRoutine()) {
						SysRpcTcSetMapTargetIn.target.routineHdl = ((TcSysRpcExecutionUnit) target.getRoutine()).getHandle();
					} else {
						SysRpcTcSetMapTargetIn.target.routineHdl = 0;
					}
					SysRpcTcSetMapTargetIn.target.external = null;
				} else {
					SysRpcTcSetMapTargetIn.target.kind.value = TcMapTarget.MAP_TARGET_EXTERNAL;
					SysRpcTcSetMapTargetIn.target.instancePath.nrOfElems = 0;
					SysRpcTcSetMapTargetIn.target.instancePath.elems_count = 0;
					SysRpcTcSetMapTargetIn.target.routineHdl = 0;
					SysRpcTcSetMapTargetIn.target.external = target.getExternalMap();
				}
				client.client.SysRpcTcSetMapTarget_1(SysRpcTcSetMapTargetIn, SysRpcTcSetMapTargetOut);
				return SysRpcTcSetMapTargetOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - setMapTarget: ");
		}
		return false;
	}

	public TcVariableGroup createVariableGroup(String groupName) {
		try {
			synchronized (SysRpcTcNewVarGroupIn) {
				SysRpcTcNewVarGroupIn.groupName = groupName;
				client.client.SysRpcTcNewVarGroup_1(SysRpcTcNewVarGroupIn, SysRpcTcNewVarGroupOut);
				if (SysRpcTcNewVarGroupOut.retVal) {
					return new VariableGroup(groupName, SysRpcTcNewVarGroupOut.varGroupHnd);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - createVariableGroup: ");
		}
		return null;
	}

	public boolean removeVariableGroup(TcVariableGroup group) {
		try {
			synchronized (SysRpcTcRemoveVarGroupIn) {
				SysRpcTcRemoveVarGroupIn.varGroupHnd = ((VariableGroup) group).getHandle();
				client.client.SysRpcTcRemoveVarGroup_1(SysRpcTcRemoveVarGroupIn, SysRpcTcRemoveVarGroupOut);
				return SysRpcTcRemoveVarGroupOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - removeVariableGroup: ");
		}
		return false;
	}

	public int[] addVariablesToGroup(TcVariableGroup group, Vector accessHandles) {
		try {
			synchronized (SysRpcTcAddVarsToGroupIn) {
				int[] variableIds = new int[accessHandles.size()];
				SysRpcTcAddVarsToGroupIn.varGroupHnd = ((VariableGroup) group).getHandle();
				if (SysRpcTcAddVarsToGroupIn.exeUnitHnd == null) {
					SysRpcTcAddVarsToGroupIn.exeUnitHnd = new int[TCI.rpcChunkLen];
					SysRpcTcAddVarsToGroupIn.varAccess = new SysRpcTcVarAccess[TCI.rpcChunkLen];

				}
				int index = 0;
				while (index < accessHandles.size()) {
					int end = index + TCI.rpcChunkLen;
					if (end >= accessHandles.size()) {
						end = accessHandles.size();
					}
					for (int i = index; i < end; i++) {
						AccessHandle ah = (AccessHandle) accessHandles.elementAt(i);
						SysRpcTcAddVarsToGroupIn.exeUnitHnd[i - index] = 0;
						SysRpcTcAddVarsToGroupIn.varAccess[i - index] = ah.access;
					}
					SysRpcTcAddVarsToGroupIn.exeUnitHnd_count = end - index;
					SysRpcTcAddVarsToGroupIn.varAccess_count = end - index;
					client.client.SysRpcTcAddVarsToGroup_1(SysRpcTcAddVarsToGroupIn, SysRpcTcAddVarsToGroupOut);
					if (SysRpcTcAddVarsToGroupOut.retVal) {
						for (int i = index; i < end; i++) {
							variableIds[i] = SysRpcTcAddVarsToGroupOut.varId[i - index];
						}
					} else {
						return null;
					}
					index = index + TCI.rpcChunkLen;
				}
				return variableIds;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - removeVariableGroup: ");
		}
		return null;
	}

	public boolean removeVariablesFromGroup(TcVariableGroup group, int[] variableIds) {
		try {
			synchronized (SysRpcTcRemoveVarsFromGroupIn) {
				SysRpcTcRemoveVarsFromGroupIn.varGroupHnd = ((VariableGroup) group).getHandle();
				if (SysRpcTcRemoveVarsFromGroupIn.varId == null) {
					SysRpcTcRemoveVarsFromGroupIn.varId = new int[TCI.rpcChunkLen];
				}
				int index = 0;
				while (index < variableIds.length) {
					int end = index + TCI.rpcChunkLen;
					if (end >= variableIds.length) {
						end = variableIds.length;
					}
					for (int i = index; i < end; i++) {
						SysRpcTcRemoveVarsFromGroupIn.varId[i - index] = variableIds[i];
					}
					SysRpcTcRemoveVarsFromGroupIn.varId_count = end - index;
					client.client.SysRpcTcRemoveVarsFromGroup_1(SysRpcTcRemoveVarsFromGroupIn, SysRpcTcRemoveVarsFromGroupOut);
					if (!SysRpcTcRemoveVarsFromGroupOut.retVal) {
						return false;
					}
					index = index + TCI.rpcChunkLen;
				}
				return true;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - removeVariableGroup: ");
		}
		return false;
	}

	public int[] getVariableGroupValues(TcVariableGroup group, Vector values) {
		try {
			synchronized (SysRpcTcReadVarGroupValuesIn) {
				SysRpcTcReadVarGroupValuesIn.varGroupHnd = ((VariableGroup) group).getHandle();
				int[] variableIDs = null;
				int startIndex = -TCI.rpcChunkLen;
				do {
					startIndex += TCI.rpcChunkLen;
					SysRpcTcReadVarGroupValuesIn.startIdx = startIndex;
					client.client.SysRpcTcReadVarGroupValues_1(SysRpcTcReadVarGroupValuesIn, SysRpcTcReadVarGroupValuesOut);
					if (SysRpcTcReadVarGroupValuesOut.retVal) {
						int length = (variableIDs == null) ? 0 : variableIDs.length;
						int[] help = new int[length + SysRpcTcReadVarGroupValuesOut.varId_count];
						if (0 < length) {
							System.arraycopy(variableIDs, 0, help, 0, length);
						}
						variableIDs = help;
						for (int i = 0; i < SysRpcTcReadVarGroupValuesOut.value_count; i++) {
							variableIDs[length + i] = SysRpcTcReadVarGroupValuesOut.varId[i];
							TcValue v = (TcValue) values.elementAt(startIndex + i);
							v.boolValue = SysRpcTcReadVarGroupValuesOut.value[i].bValue;
							v.int8Value = (byte) SysRpcTcReadVarGroupValuesOut.value[i].i8Value;
							v.int16Value = (short) SysRpcTcReadVarGroupValuesOut.value[i].i16Value;
							v.int32Value = SysRpcTcReadVarGroupValuesOut.value[i].i32Value;
							v.int64Value = SysRpcTcReadVarGroupValuesOut.value[i].i64Value;
							v.floatValue = SysRpcTcReadVarGroupValuesOut.value[i].fValue;
							v.stringValue = SysRpcTcReadVarGroupValuesOut.value[i].sValue.toString();
							v.isValid = SysRpcTcReadVarGroupValuesOut.value[i].isValid;
						}
					}
				} while (SysRpcTcReadVarGroupValuesOut.retVal && (SysRpcTcReadVarGroupValuesOut.value_count == TCI.rpcChunkLen));
				return variableIDs;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getVariableGroupValues: ");
		}
		return null;
	}

	public int[] getChangedVariableGroupValues(TcVariableGroup group, Vector values) {
		try {
			synchronized (SysRpcTcReadChangedVarGroupValuesIn) {
				SysRpcTcReadChangedVarGroupValuesIn.varGroupHnd = ((VariableGroup) group).getHandle();
				int[] variableIDs = null;
				int startIndex = -TCI.rpcChunkLen;
				do {
					startIndex += TCI.rpcChunkLen;
					SysRpcTcReadChangedVarGroupValuesIn.startIdx = startIndex;
					client.client.SysRpcTcReadChangedVarGroupValues_1(SysRpcTcReadChangedVarGroupValuesIn, SysRpcTcReadChangedVarGroupValuesOut);
					if (SysRpcTcReadChangedVarGroupValuesOut.retVal) {
						int length = (variableIDs == null) ? 0 : variableIDs.length;
						int[] help = new int[length + SysRpcTcReadChangedVarGroupValuesOut.varId_count];
						if (0 < length) {
							System.arraycopy(variableIDs, 0, help, 0, length);
						}
						variableIDs = help;
						for (int i = 0; i < SysRpcTcReadChangedVarGroupValuesOut.value_count; i++) {
							variableIDs[length + i] = SysRpcTcReadChangedVarGroupValuesOut.varId[i];
							TcValue v = (TcValue) values.elementAt(startIndex + i);
							v.boolValue = SysRpcTcReadChangedVarGroupValuesOut.value[i].bValue;
							v.int8Value = (byte) SysRpcTcReadChangedVarGroupValuesOut.value[i].i8Value;
							v.int16Value = (short) SysRpcTcReadChangedVarGroupValuesOut.value[i].i16Value;
							v.int32Value = SysRpcTcReadChangedVarGroupValuesOut.value[i].i32Value;
							v.int64Value = SysRpcTcReadChangedVarGroupValuesOut.value[i].i64Value;
							v.floatValue = SysRpcTcReadChangedVarGroupValuesOut.value[i].fValue;
							v.stringValue = SysRpcTcReadChangedVarGroupValuesOut.value[i].sValue.toString();
							v.isValid = SysRpcTcReadChangedVarGroupValuesOut.value[i].isValid;
						}
					}
				} while (SysRpcTcReadChangedVarGroupValuesOut.retVal && (SysRpcTcReadChangedVarGroupValuesOut.value_count == TCI.rpcChunkLen));
				return variableIDs;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getChangedVariableGroupValues: ");
		}
		return null;
	}

	/**
	 * Liefert die Enumeration der Ausführungseinheiten, die unter der
	 * Ausführungseinheit parent liegen.
	 * 
	 * @param parent
	 * @param kind
	 *            ist die Art der Ausführungeinheiten, welche bei der
	 *            Enumeration geliefert werden sollen.
	 * @param loadInfos
	 *            gibt an, ob auch die Knoteninformationen der
	 *            Ausführungseinheiten gelesen werden sollen.
	 * 
	 * @return Enumeration
	 */
	public Enumeration getExecutionUnits(TcExecutionUnit parent, int kind, boolean loadInfos) {
		return new ExecutionUnitChunkEnumeration((TcSysRpcExecutionUnit) parent, kind, loadInfos);
	}

	/**
	 * Liefert die Enumeration der Ausführungseinheiten, die unter den
	 * Ausführungseinheiten im Vektor parent liegen.
	 * 
	 * @param parents
	 *            Ausführungseinheiten
	 * @param kind
	 *            ist die Art der Ausführungeinheiten, welche bei der
	 *            Enumeration geliefert werden sollen.
	 * @param loadInfos
	 *            gibt an, ob auch die Knoteninformationen der
	 *            Ausführungseinheiten gelesen werden sollen.
	 * 
	 * @return Enumeration
	 */
	public Enumeration getExecutionUnits(Vector parents, int kind, boolean loadInfos) {
		return new MultiExecutionUnitChunkEnumeration(parents, kind, loadInfos);
	}

	/**
	 * Liefert die Wurzel aller Ausführungseinheiten.
	 * 
	 * @return Wurzel
	 */
	public TcExecutionUnit getRoot() {
		return new TcSysRpcExecutionUnit(0, client);
	}

	/**
	 * Bringt das angegebene Projekt project in Ausführung.
	 * 
	 * @param project
	 *            Projekt, welches geladen werden soll.
	 * 
	 * @return Ausführungseinheit für das geladene Projekt
	 */
	public TcExecutionUnit loadProject(TcStructuralNode project) {
		try {
			if ((project.getKind() == TcStructuralNode.GLOBAL) || (project.getKind() == TcStructuralNode.SYSTEM)) {
				synchronized (SysRpcTcLoadGlobalIn) {
					SysRpcTcLoadGlobalIn.globalScopeHnd = ((TcSysRpcStructuralNode) project).getHandle();
					client.client.SysRpcTcLoadGlobal_1(SysRpcTcLoadGlobalIn, SysRpcTcLoadGlobalOut);
					if (SysRpcTcLoadGlobalOut.retVal) {
						return new TcSysRpcExecutionUnit(SysRpcTcLoadGlobalOut.exeUnitHnd, client);
					}
				}
			} else {
				synchronized (SysRpcTcLoadProjIn) {
					SysRpcTcLoadProjIn.projScopeHnd = ((TcSysRpcStructuralNode) project).getHandle();
					client.client.SysRpcTcLoadProj_1(SysRpcTcLoadProjIn, SysRpcTcLoadProjOut);
					if (SysRpcTcLoadProjOut.retVal) {
						return new TcSysRpcExecutionUnit(SysRpcTcLoadProjOut.exeUnitHnd, client);
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - loadProject: ");
		}
		return null;
	}

	/**
	 * Entlädt das Projekt project.
	 * 
	 * @param project
	 *            Projekt, welches abgeladen werden soll.
	 * 
	 * @return true für das erfolgreiche Abladen
	 */
	public boolean unloadProject(TcExecutionUnit project) {
		try {
			if ((project.getKind() == TcExecutionUnit.GLOBAL) || (project.getKind() == TcExecutionUnit.SYSTEM)) {
				synchronized (SysRpcTcUnloadGlobalIn) {
					SysRpcTcUnloadGlobalIn.exeUnitHnd = ((TcSysRpcExecutionUnit) project).getHandle();
					client.client.SysRpcTcUnloadGlobal_1(SysRpcTcUnloadGlobalIn, SysRpcTcUnloadGlobalOut);
					return SysRpcTcUnloadGlobalOut.retVal;
				}
			}
			synchronized (SysRpcTcUnloadProjIn) {
				SysRpcTcUnloadProjIn.exeUnitHnd = ((TcSysRpcExecutionUnit) project).getHandle();
				client.client.SysRpcTcUnloadProj_1(SysRpcTcUnloadProjIn, SysRpcTcUnloadProjOut);
				return SysRpcTcUnloadProjOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - unloadProject: ");
		}
		return false;
	}

	/**
	 * Startet die unbenannte Routine des Programms program.
	 * 
	 * @param program
	 *            Programm, das die unbenannte Routine enthält.
	 * 
	 * @return Ausführungseinheit für die unbenannte Routine
	 */
	public TcExecutionUnit startProgram(TcStructuralNode program) {
		try {
			synchronized (SysRpcTcStartProgIn) {
				SysRpcTcStartProgIn.progScopeHnd = ((TcSysRpcStructuralNode) program).getHandle();
				client.client.SysRpcTcStartProg_1(SysRpcTcStartProgIn, SysRpcTcStartProgOut);
				if (SysRpcTcStartProgOut.retVal) {
					return new TcSysRpcExecutionUnit(SysRpcTcStartProgOut.exeUnitHnd, client);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - startProgram: ");
		}
		return null;
	}

	/**
	 * Starts the unnamed routine of the program.
	 * 
	 * @param program
	 *            program to start
	 * @param interrupt
	 *            if true the unnamed routine will be stopped at the first
	 *            statement
	 * @param restart
	 *            if true the unnamed routine will be automatically restarted
	 * @return execution unit for the unnamed routine
	 */
	public TcExecutionUnit startProgram(TcStructuralNode program, boolean interrupt, boolean restart) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			if (((major == 2) && (88 <= minor)) || (3 <= major)) {
				synchronized (SysRpcTcStartProgExIn) {
					SysRpcTcStartProgExIn.progScopeHnd = ((TcSysRpcStructuralNode) program).getHandle();
					if (interrupt) {
						SysRpcTcStartProgExIn.startFlags |= SysRpcTcStartFlags.rpcStartFlagInterrupt;
					}
					if (restart) {
						SysRpcTcStartProgExIn.startFlags |= SysRpcTcStartFlags.rpcStartFlagRestart;
					}
					client.client.SysRpcTcStartProgEx_1(SysRpcTcStartProgExIn, SysRpcTcStartProgExOut);
					if (SysRpcTcStartProgExOut.retVal) {
						return new TcSysRpcExecutionUnit(SysRpcTcStartProgExOut.exeUnitHnd, client);
					}
				}
			} else {
				// workaround for compatibility reason
				setMainFlowStepping(getRoot(), true);
				TcExecutionUnit prog = startProgram(program);
				setMainFlowStepping(getRoot(), false);
				if (prog != null) {
					// set cont mode
					step(prog, TcExecutionState.STEP_OFF);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - startProgram: ");
		}
		return null;
	}

	/**
	 * Unterbricht alle Routine des Ablaufes.
	 * 
	 * @param exeUnit
	 *            Wurzel eines Ablaufs
	 * 
	 * @return true für das erfolgreiche Unterbrechen
	 */
	public boolean interruptExeUnit(TcExecutionUnit execUnit) {
		try {
			synchronized (SysRpcTcInterruptProgIn) {
				SysRpcTcInterruptProgIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				client.client.SysRpcTcInterruptProg_1(SysRpcTcInterruptProgIn, SysRpcTcInterruptProgOut);
				return SysRpcTcInterruptProgOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - interruptExeUnit: ");
		}
		return false;
	}

	/**
	 * Setzt den Ablauf fort.
	 * 
	 * @param exeUnit
	 *            Wurzel eines Ablaufs
	 * 
	 * @return true für das erfolgreiche Fortsetzen
	 */
	public boolean continueExeUnit(TcExecutionUnit execUnit) {
		try {
			synchronized (SysRpcTcContinueProgIn) {
				SysRpcTcContinueProgIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				client.client.SysRpcTcContinueProg_1(SysRpcTcContinueProgIn, SysRpcTcContinueProgOut);
				return SysRpcTcContinueProgOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - continueExeUnit: ");
		}
		return false;
	}

	/**
	 * Stopt den Ablauf.
	 * 
	 * @param exeUnit
	 *            Wurzel eines Ablaufs
	 * 
	 * @return true für das erfolgreiche Stoppen
	 */
	public boolean stopExeUnit(TcExecutionUnit execUnit) {
		try {
			synchronized (SysRpcTcStopProgIn) {
				SysRpcTcStopProgIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				client.client.SysRpcTcStopProg_1(SysRpcTcStopProgIn, SysRpcTcStopProgOut);
				return SysRpcTcStopProgOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - stopExeUnit: ");
		}
		return false;
	}

	/**
	 * Sets the execution mode.
	 * 
	 * @param exeUnit
	 *            execution unit
	 * @param mode
	 *            {EXECUTION_MODE_FLOW, EXECUTION_MODE_ROUTINE,
	 *            EXECUTION_MODE_OFF}
	 * @return true if the mode was successfully set
	 */
	public boolean setExecutionMode(TcExecutionUnit execUnit, int mode) {
		try {
			int major = client.getMajorVersion();
			if (3 <= major) {
				synchronized (SysRpcTcSetExeFlagIn) {
					SysRpcTcSetExeFlagIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
					SysRpcTcSetExeFlagIn.exeFlag.value = mode;
					SysRpcTcSetExeFlagIn.value = true;
					client.client.SysRpcTcSetExeFlag_1(SysRpcTcSetExeFlagIn, SysRpcTcSetExeFlagOut);
					return SysRpcTcSetExeFlagOut.retVal;
				}
			}
			synchronized (SysRpcTcSetDebugModeIn) {
				SysRpcTcSetDebugModeIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				SysRpcTcSetDebugModeIn.exeFlags.value = mode;
				client.client.SysRpcTcSetDebugMode_1(SysRpcTcSetDebugModeIn, SysRpcTcSetDebugModeOut);
				return SysRpcTcSetDebugModeOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - setStepMode: ");
		}
		return false;
	}

	/**
	 * Switchs the main flow stepping on or off
	 * 
	 * @param routine
	 *            root of the execution flow
	 * @param enable
	 *            true for on and false for off
	 * @return returns true if the main flow stepping is successfully switched
	 */
	public boolean setMainFlowStepping(TcExecutionUnit routine, boolean enable) {
		try {
			int major = client.getMajorVersion();
			if (3 <= major) {
				synchronized (SysRpcTcSetExeFlagIn) {
					SysRpcTcSetExeFlagIn.exeUnitHnd = ((TcSysRpcExecutionUnit) routine).getHandle();
					SysRpcTcSetExeFlagIn.exeFlag.value = EXECUTION_MODE_MAIN_FLOW_STEPPING;
					SysRpcTcSetExeFlagIn.value = enable;
					client.client.SysRpcTcSetExeFlag_1(SysRpcTcSetExeFlagIn, SysRpcTcSetExeFlagOut);
					return SysRpcTcSetExeFlagOut.retVal;
				}
			}
			synchronized (SysRpcTcSetDebugModeIn) {
				SysRpcTcSetDebugModeIn.exeUnitHnd = ((TcSysRpcExecutionUnit) routine).getHandle();
				SysRpcTcSetDebugModeIn.exeFlags.value = enable ? DEBUG_MODE_MAIN_FLOW : TcExecutionState.EXECUTION_MODE_FLOW;
				client.client.SysRpcTcSetDebugMode_1(SysRpcTcSetDebugModeIn, SysRpcTcSetDebugModeOut);
				return SysRpcTcSetDebugModeOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - setStepMode: ");
		}
		return false;
	}

	/**
	 * Liefert den Zustand der Ausführungseinheit.
	 * 
	 * @param execUnit
	 *            Ausführungseinheit
	 * @param s
	 *            Zustandsobjekt, welches nach dem Aufruf die Informationen
	 *            enthält
	 * 
	 * @return true für das erfolgreiche Lesen
	 */
	public boolean getState(TcExecutionUnit execUnit, TcExecutionState s) {
		try {
			synchronized (SysRpcTcGetExeUnitStatusIn) {
				SysRpcTcGetExeUnitStatusIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				client.client.SysRpcTcGetExeUnitStatus_1(SysRpcTcGetExeUnitStatusIn, SysRpcTcGetExeUnitStatusOut);
				if (SysRpcTcGetExeUnitStatusOut.retVal) {
					s.changeCount = SysRpcTcGetExeUnitStatusOut.status.changeCnt;
					s.childCount = SysRpcTcGetExeUnitStatusOut.status.nrChilds;
					s.executionState = SysRpcTcGetExeUnitStatusOut.status.state.value;
					s.steppingState = SysRpcTcGetExeUnitStatusOut.status.step.value;
					s.executionMode = SysRpcTcGetExeUnitStatusOut.status.exeFlags.value & 0xffff;
					if (s.executionMode == DEBUG_MODE_MAIN_FLOW) {
						s.executionMode = TcExecutionState.EXECUTION_MODE_FLOW;
					}
					s.isMainFlowStepping = (SysRpcTcGetExeUnitStatusOut.status.exeFlags.value == DEBUG_MODE_MAIN_FLOW) || ((SysRpcTcGetExeUnitStatusOut.status.exeFlags.value & 0x10000) == EXECUTION_MODE_MAIN_FLOW_STEPPING);

					if ((s.executionState != TcExecutionState.STATE_INVALID) && ((execUnit.getKind() == TcExecutionUnit.PROJECT) || (execUnit.getKind() == TcExecutionUnit.GLOBAL) || (execUnit.getKind() == TcExecutionUnit.SYSTEM))) {
						int state = SysRpcTcGetExeUnitStatusOut.status.lineOrStatus;
						if (state == 0) {
							s.executionState = TcExecutionState.STATE_PROJECT_LOADING;
						} else if (state == 1) {
							s.executionState = TcExecutionState.STATE_PROJECT_UNLOADING;
						}
						s.line = 0;
					} else {
						s.line = SysRpcTcGetExeUnitStatusOut.status.lineOrStatus;
					}
					s.mainFlowLine = SysRpcTcGetExeUnitStatusOut.status.mainFlowLine;
					return true;
				}
				s.executionState = TcExecutionState.STATE_INVALID;
				return true;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionUnit - getState: ");
		}
		return false;
	}

	/**
	 * Führt ein oder mehrer Anweisungen mit der angegebenen Schrittart aus.
	 * 
	 * @param execUnits
	 *            Ausführungseinheiten
	 * @param stepKind
	 *            Schrittart
	 * 
	 * @return true für den erfolgreichen Aufruf
	 */
	public boolean step(Vector execUnits, int stepKind) {
		try {
			synchronized (SysRpcTcStepListIn) {
				SysRpcTcStepListIn.stepCmd.value = stepKind;
				int index = 0;
				while (index < execUnits.size()) {
					int end = index + TCI.rpcChunkLen;
					if (end >= execUnits.size()) {
						end = execUnits.size();
					}
					for (int i = index; i < end; i++) {
						TcSysRpcExecutionUnit oeu = (TcSysRpcExecutionUnit) execUnits.elementAt(i);
						SysRpcTcStepListIn.exeUnitHnd[i - index] = oeu.getHandle();
					}
					SysRpcTcStepListIn.nrOfExeUnitHnd = end - index;
					SysRpcTcStepListIn.exeUnitHnd_count = end - index;
					client.client.SysRpcTcStepList_1(SysRpcTcStepListIn, SysRpcTcStepListOut);
					if (!SysRpcTcStepListOut.retVal) {
						return false;
					}
					index = index + TCI.rpcChunkLen;
				}
				return true;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - step: ");
		}
		return false;
	}

	/**
	 * Liefert die Zustände der Ausführungseinheiten.
	 * 
	 * @param execUnits
	 *            Ausführungseinheiten
	 * @param states
	 *            Zustandsobjekte
	 * 
	 * @return true für den erfolgreichen Aufruf
	 */
	public boolean getStates(Vector execUnits, Vector states) {
		try {
			synchronized (SysRpcTcGetExeUnitStatusListIn) {
				int index = 0;
				while (index < execUnits.size()) {
					int end = index + TCI.rpcChunkLen;
					if (end >= execUnits.size()) {
						end = execUnits.size();
					}
					if (SysRpcTcGetExeUnitStatusListIn.exeUnitHnd == null) {
						SysRpcTcGetExeUnitStatusListIn.exeUnitHnd = new int[TCI.rpcChunkLen];
					}
					for (int i = index; i < end; i++) {
						TcSysRpcExecutionUnit oeu = (TcSysRpcExecutionUnit) execUnits.elementAt(i);
						SysRpcTcGetExeUnitStatusListIn.exeUnitHnd[i - index] = oeu.getHandle();
					}
					SysRpcTcGetExeUnitStatusListIn.nrOfExeUnitHnd = end - index;
					SysRpcTcGetExeUnitStatusListIn.exeUnitHnd_count = end - index;
					client.client.SysRpcTcGetExeUnitStatusList_1(SysRpcTcGetExeUnitStatusListIn, SysRpcTcGetExeUnitStatusListOut);
					if (SysRpcTcGetExeUnitStatusListOut.retVal) {
						for (int i = index; i < end; i++) {
							TcExecutionState s = (TcExecutionState) states.elementAt(i);
							TcExecutionUnit execUnit = (TcExecutionUnit) execUnits.elementAt(i);
							s.changeCount = SysRpcTcGetExeUnitStatusListOut.status[i - index].changeCnt;
							s.childCount = SysRpcTcGetExeUnitStatusListOut.status[i - index].nrChilds;
							s.executionState = SysRpcTcGetExeUnitStatusListOut.status[i - index].state.value;
							s.steppingState = SysRpcTcGetExeUnitStatusListOut.status[i - index].step.value;
							s.executionMode = SysRpcTcGetExeUnitStatusListOut.status[i - index].exeFlags.value & 0xffff;
							if (s.executionMode == DEBUG_MODE_MAIN_FLOW) {
								s.executionMode = TcExecutionState.EXECUTION_MODE_FLOW;
							}
							s.isMainFlowStepping = (SysRpcTcGetExeUnitStatusListOut.status[i - index].exeFlags.value == DEBUG_MODE_MAIN_FLOW)
									|| ((SysRpcTcGetExeUnitStatusListOut.status[i - index].exeFlags.value & 0x10000) == EXECUTION_MODE_MAIN_FLOW_STEPPING);
							if ((s.executionState != TcExecutionState.STATE_INVALID) && ((execUnit.getKind() == TcExecutionUnit.PROJECT) || (execUnit.getKind() == TcExecutionUnit.GLOBAL) || (execUnit.getKind() == TcExecutionUnit.SYSTEM))) {
								int state = SysRpcTcGetExeUnitStatusListOut.status[i - index].lineOrStatus;
								if (state == 0) {
									s.executionState = TcExecutionState.STATE_PROJECT_LOADING;
								} else if (state == 1) {
									s.executionState = TcExecutionState.STATE_PROJECT_UNLOADING;
								}
								s.line = 0;
							} else {
								s.line = SysRpcTcGetExeUnitStatusListOut.status[i - index].lineOrStatus;
							}
							s.mainFlowLine = SysRpcTcGetExeUnitStatusListOut.status[i - index].mainFlowLine;
						}
					} else {
						return false;
					}
					index = index + TCI.rpcChunkLen;
				}
				return true;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionUnit - getStates: ");
		}
		return false;
	}

	/**
	 * Setzt den Ausführungszeiger auf die angegebene Zeilenposition.
	 * 
	 * @param execUnit
	 *            Ausführungseinheit
	 * @param line
	 *            Zeilenposition
	 * 
	 * @return true für das erfolgreiche Setzen
	 */
	public boolean setInstructionPointer(TcExecutionUnit execUnit, int line) {
		try {
			synchronized (SysRpcTcSetInstructionPointerIn) {
				SysRpcTcSetInstructionPointerIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				SysRpcTcSetInstructionPointerIn.lineNr = line;
				client.client.SysRpcTcSetInstructionPointer_1(SysRpcTcSetInstructionPointerIn, SysRpcTcSetInstructionPointerOut);
				return SysRpcTcSetInstructionPointerOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - setInstructionPointer: ");
		}
		return false;
	}

	/**
	 * Führt ein oder mehrer Anweisungen mit der angegebenen Schrittart aus.
	 * 
	 * @param execUnit
	 *            Ausführungseinheit
	 * @param stepKind
	 *            Schrittart
	 * 
	 * @return true für den erfolgreichen Aufruf
	 */
	public boolean step(TcExecutionUnit execUnit, int stepKind) {
		try {
			synchronized (SysRpcTcStepIn) {
				SysRpcTcStepIn.exeUnitHnd = ((TcSysRpcExecutionUnit) execUnit).getHandle();
				SysRpcTcStepIn.stepCmd.value = stepKind;
				client.client.SysRpcTcStep_1(SysRpcTcStepIn, SysRpcTcStepOut);
				return SysRpcTcStepOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - step: ");
		}
		return false;
	}

	/**
	 * Executes the given routine.
	 * 
	 * @param routine
	 *            routine which should be executed
	 * @param instanceAccessHandle
	 *            variable instance or null if the routine is program routine
	 * @param parameter
	 *            parameter of the routine (Access Handle, Boolean, Byte, Short,
	 *            Integer, Long, Float or String)
	 * 
	 * @return returns the routine return value if it was successfully executed
	 *         otherwise null. If the routine does not have a return value a
	 *         Object will be retruned
	 */
	public Object executeRoutine(TcStructuralRoutineNode routine, TcAccessHandle instanceAccessHandle, Object[] parameter) {
		try {
			synchronized (SysRpcTcExecuteMethodExtIn) {
				int major = client.getMajorVersion();
				int executionMode = TcExecutionState.EXECUTION_MODE_OFF;
				boolean isMainFlowStepping = false;
				if (major < 3) {
					TcExecutionUnit root = getRoot();
					TcExecutionState state = new TcExecutionState();
					getState(root, state);
					setMainFlowStepping(root, false);
					executionMode = state.executionMode;
					isMainFlowStepping = state.isMainFlowStepping;
				}
				SysRpcTcExecuteMethodExtIn.routineHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcExecuteMethodExtIn.instanceVarAccess = (instanceAccessHandle != null) ? ((AccessHandle) instanceAccessHandle).access : new SysRpcTcVarAccess();
				if (SysRpcTcExecuteMethodExtIn.routParams == null) {
					SysRpcTcExecuteMethodExtIn.routParams = new SysRpcTcMethodParam[TCI.rpcMaxParams];
				}
				SysRpcTcExecuteMethodExtIn.routParams_count = parameter.length;
				for (int i = 0; i < parameter.length; i++) {
					if (SysRpcTcExecuteMethodExtIn.routParams[i] == null) {
						SysRpcTcExecuteMethodExtIn.routParams[i] = new SysRpcTcMethodParam();
					}
					SysRpcTcExecuteMethodExtIn.routParams[i].isValue = !(parameter[i] instanceof TcAccessHandle);
					if (SysRpcTcExecuteMethodExtIn.routParams[i].isValue) {
						if (parameter[i] instanceof Boolean) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.bValue = ((Boolean) parameter[i]).booleanValue();
						} else if (parameter[i] instanceof Byte) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.i8Value = ((Byte) parameter[i]).byteValue();
						} else if (parameter[i] instanceof Short) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.i16Value = ((Short) parameter[i]).shortValue();
						} else if (parameter[i] instanceof Integer) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.i32Value = ((Integer) parameter[i]).intValue();
						} else if (parameter[i] instanceof Long) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.i64Value = ((Long) parameter[i]).longValue();
						} else if (parameter[i] instanceof Float) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.fValue = ((Float) parameter[i]).floatValue();
						} else if (parameter[i] instanceof String) {
							SysRpcTcExecuteMethodExtIn.routParams[i].value.sValue = (String) parameter[i];
						}
					} else {
						SysRpcTcExecuteMethodExtIn.routParams[i].varAccess = ((AccessHandle) parameter[i]).access;
					}
				}
				client.client.SysRpcTcExecuteMethodExt_1(SysRpcTcExecuteMethodExtIn, SysRpcTcExecuteMethodExtOut);
				if ((major < 3) && (executionMode != TcExecutionState.EXECUTION_MODE_OFF)) {
					if (isMainFlowStepping) {
						setMainFlowStepping(getRoot(), true);
					} else {
						setExecutionMode(getRoot(), executionMode);
					}
				}
				if (SysRpcTcExecuteMethodExtOut.retVal) {
					if (SysRpcTcExecuteMethodExtOut.methodRetVal.isValid) {
						TcStructuralTypeNode t = routine.getReturnType();
						if (t != null) {
							switch (t.getTypeKind()) {
								case TcStructuralTypeNode.BOOL_TYPE:
									return (SysRpcTcExecuteMethodExtOut.methodRetVal.bValue) ? Boolean.TRUE : Boolean.FALSE;
								case TcStructuralTypeNode.SINT_TYPE:
									return new Byte((byte) SysRpcTcExecuteMethodExtOut.methodRetVal.i8Value);
								case TcStructuralTypeNode.INT_TYPE:
									return new Short((short) SysRpcTcExecuteMethodExtOut.methodRetVal.i16Value);
								case TcStructuralTypeNode.DINT_TYPE:
									return new Integer(SysRpcTcExecuteMethodExtOut.methodRetVal.i32Value);
								case TcStructuralTypeNode.LINT_TYPE:
									return new Long(SysRpcTcExecuteMethodExtOut.methodRetVal.i64Value);
								case TcStructuralTypeNode.REAL_TYPE:
									return new Float(SysRpcTcExecuteMethodExtOut.methodRetVal.fValue);
								case TcStructuralTypeNode.STRING_TYPE:
									return SysRpcTcExecuteMethodExtOut.methodRetVal.sValue.toString();
							}
						}
					} else {
						return new Object();
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - executeRoutine: ");
		}
		return null;
	}

	/**
	 * Liest und setzt die Ausführungseinheiteninformation (z.B. Art,
	 * Instanzpfad, Strukturbaumknoten, ...)
	 * 
	 * @param execUnits
	 *            Ausführungseinheiten
	 * 
	 * @return true für den erfolgreichen Aufruf
	 */
	public boolean setExecUnitInfos(Vector execUnits) {
		try {
			synchronized (SysRpcTcGetExeUnitInfoListIn) {
				int index = 0;
				while (index < execUnits.size()) {
					int end = index + TCI.rpcChunkLen;
					if (end >= execUnits.size()) {
						end = execUnits.size();
					}
					if (SysRpcTcGetExeUnitInfoListIn.exeUnitHnd == null) {
						SysRpcTcGetExeUnitInfoListIn.exeUnitHnd = new int[TCI.rpcChunkLen];
					}
					for (int i = index; i < end; i++) {
						TcSysRpcExecutionUnit oeu = (TcSysRpcExecutionUnit) execUnits.elementAt(i);
						SysRpcTcGetExeUnitInfoListIn.exeUnitHnd[i - index] = oeu.getHandle();
					}
					SysRpcTcGetExeUnitInfoListIn.nrOfExeUnitHnd = end - index;
					SysRpcTcGetExeUnitInfoListIn.exeUnitHnd_count = end - index;
					client.client.SysRpcTcGetExeUnitInfoList_1(SysRpcTcGetExeUnitInfoListIn, SysRpcTcGetExeUnitInfoListOut);
					if (SysRpcTcGetExeUnitInfoListOut.retVal) {
						for (int i = index; i < end; i++) {
							TcSysRpcExecutionUnit oeu = (TcSysRpcExecutionUnit) execUnits.elementAt(i);
							oeu.setInfo(SysRpcTcGetExeUnitInfoListOut.info[i - index]);
						}
					} else {
						return false;
					}
					index = index + TCI.rpcChunkLen;
				}
				return true;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - getInfoElems: ");
		}
		return false;
	}

	private int convertKind(int kind) {
		if (client.getUserMode()) {
			if (kind == TcExecutionUnit.ROUTINE) {
				return FILTER_USER_ROUTINE;
			}
			if (kind == FILTER_ALL) {
				return FILTER_ALL_USER;
			}
			return kind;
		}
		return kind;
	}

	private class ExecutionUnitChunkEnumeration implements Enumeration {
		private int								kind;
		private TcSysRpcExecutionUnit			parent;
		private boolean							loadInfos;
		private int								nrOfHnd;
		private int								iterHandle;
		private final TcSysRpcExecutionUnit[]	elems	= new TcSysRpcExecutionUnit[TCI.rpcChunkLen];
		private int								index;
		private boolean							isFirst	= true;
		private boolean							isValid	= false;

		private ExecutionUnitChunkEnumeration(TcSysRpcExecutionUnit parent, int kind, boolean loadInfos) {
			this.parent = parent;
			this.kind = convertKind(kind);
			this.loadInfos = loadInfos;
		}

		/**
		 * @see java.util.Enumeration#hasMoreElements()
		 */
		public boolean hasMoreElements() {
			if (!isValid) {
				if (index >= nrOfHnd) {
					if (isFirst) {
						isFirst = false;
						getFirstChunk();
					} else if (nrOfHnd == TCI.rpcChunkLen) {
						getNextChunk();
					}
				}
				isValid = index < nrOfHnd;
			}
			return isValid;
		}

		/**
		 * @see java.util.Enumeration#nextElement()
		 */
		public Object nextElement() {
			if (hasMoreElements()) {
				isValid = false;
				TcExecutionUnit elem = elems[index];
				index++;
				return elem;
			}
			return null;
		}

		private boolean loadInfo(int[] execUnitHnds, int nrOfHandles) throws IOException, RPCException {
			synchronized (SysRpcTcGetExeUnitInfoListIn) {
				SysRpcTcGetExeUnitInfoListIn.nrOfExeUnitHnd = nrOfHandles;
				if (SysRpcTcGetExeUnitInfoListIn.exeUnitHnd == null) {
					SysRpcTcGetExeUnitInfoListIn.exeUnitHnd = new int[TCI.rpcChunkLen];
				}
				System.arraycopy(execUnitHnds, 0, SysRpcTcGetExeUnitInfoListIn.exeUnitHnd, 0, nrOfHandles);
				SysRpcTcGetExeUnitInfoListIn.exeUnitHnd_count = nrOfHandles;
				client.client.SysRpcTcGetExeUnitInfoList_1(SysRpcTcGetExeUnitInfoListIn, SysRpcTcGetExeUnitInfoListOut);
				if (SysRpcTcGetExeUnitInfoListOut.retVal) {
					for (int i = 0; i < nrOfHandles; i++) {
						elems[i].setInfo(SysRpcTcGetExeUnitInfoListOut.info[i]);
					}
					return true;
				}
				return false;
			}
		}

		private void getFirstChunk() {
			try {
				synchronized (SysRpcTcGetFirstExeUnitChunkIn) {
					SysRpcTcGetFirstExeUnitChunkIn.exeUnitHnd = parent.getHandle();
					SysRpcTcGetFirstExeUnitChunkIn.kind.value = kind;
					client.client.SysRpcTcGetFirstExeUnitChunk_1(SysRpcTcGetFirstExeUnitChunkIn, SysRpcTcGetFirstExeUnitChunkOut);
					if (SysRpcTcGetFirstExeUnitChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetFirstExeUnitChunkOut.nrOfExeUnitHnd;
						iterHandle = SysRpcTcGetFirstExeUnitChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcSysRpcExecutionUnit(SysRpcTcGetFirstExeUnitChunkOut.exeUnitHnd[i], client);
								elems[i].setParent(parent);
							}
							if (loadInfos) {
								if (loadInfo(SysRpcTcGetFirstExeUnitChunkOut.exeUnitHnd, nrOfHnd)) {
									return;
								}
							} else {
								return;
							}
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcExecutionModel - ExecutionUnitChunkEnumeration - getFirstChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}

		private void getNextChunk() {
			try {
				synchronized (SysRpcTcGetNextExeUnitChunkIn) {
					SysRpcTcGetNextExeUnitChunkIn.iterHnd = iterHandle;
					SysRpcTcGetNextExeUnitChunkIn.exeUnitHnd = parent.getHandle();
					SysRpcTcGetNextExeUnitChunkIn.kind.value = kind;
					client.client.SysRpcTcGetNextExeUnitChunk_1(SysRpcTcGetNextExeUnitChunkIn, SysRpcTcGetNextExeUnitChunkOut);
					if (SysRpcTcGetNextExeUnitChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetNextExeUnitChunkOut.nrOfExeUnitHnd;
						iterHandle = SysRpcTcGetNextExeUnitChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcSysRpcExecutionUnit(SysRpcTcGetNextExeUnitChunkOut.exeUnitHnd[i], client);
								elems[i].setParent(parent);
							}
							if (loadInfos) {
								if (loadInfo(SysRpcTcGetNextExeUnitChunkOut.exeUnitHnd, nrOfHnd)) {
									return;
								}
							} else {
								return;
							}
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcExecutionModel - ExecutionUnitChunkEnumeration - getNextChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}
	}

	private class MultiExecutionUnitChunkEnumeration implements Enumeration {
		private Vector							parents;
		private int								parentIndex;
		private int								kind;
		boolean									loadInfos;
		private int								nrOfHnd;
		private int								iterHandle;
		private final TcSysRpcExecutionUnit[]	elems	= new TcSysRpcExecutionUnit[TCI.rpcChunkLen];
		private int								index;
		private boolean							isValid	= false;

		private MultiExecutionUnitChunkEnumeration(Vector parents, int kind, boolean loadInfos) {
			this.parents = parents;
			this.kind = convertKind(kind);
			this.loadInfos = loadInfos;
			parentIndex = -1;
			iterHandle = 0;
			nrOfHnd = 0;
		}

		/**
		 * @see java.util.Enumeration#hasMoreElements()
		 */
		public boolean hasMoreElements() {
			if (!isValid) {
				if ((nrOfHnd <= index) && ((iterHandle != 0) || (parentIndex < (parents.size() - 1)))) {
					if (iterHandle == 0) {
						parentIndex++;
					}
					getChunk();
				}
				isValid = index < nrOfHnd;
			}
			return isValid;
		}

		/**
		 * @see java.util.Enumeration#nextElement()
		 */
		public Object nextElement() {
			if (hasMoreElements()) {
				isValid = false;
				TcSysRpcExecutionUnit elem = elems[index];
				index++;
				return elem;
			}
			return null;
		}

		private boolean loadInfo(int[] execUnitHnds, int nrOfHandles) throws RPCException, IOException {
			synchronized (SysRpcTcGetExeUnitInfoListIn) {
				SysRpcTcGetExeUnitInfoListIn.nrOfExeUnitHnd = nrOfHandles;
				if (SysRpcTcGetExeUnitInfoListIn.exeUnitHnd == null) {
					SysRpcTcGetExeUnitInfoListIn.exeUnitHnd = new int[TCI.rpcChunkLen];
				}
				System.arraycopy(execUnitHnds, 0, SysRpcTcGetExeUnitInfoListIn.exeUnitHnd, 0, nrOfHandles);
				SysRpcTcGetExeUnitInfoListIn.exeUnitHnd_count = nrOfHandles;
				client.client.SysRpcTcGetExeUnitInfoList_1(SysRpcTcGetExeUnitInfoListIn, SysRpcTcGetExeUnitInfoListOut);
				if (SysRpcTcGetExeUnitInfoListOut.retVal) {
					for (int i = 0; i < nrOfHandles; i++) {
						elems[i].setInfo(SysRpcTcGetExeUnitInfoListOut.info[i]);
					}
					return true;
				}
				return false;
			}
		}

		private void getChunk() {
			try {
				synchronized (SysRpcTcGetMultExeUnitChunkIn) {
					int n = parents.size() - parentIndex;
					if (TCI.rpcChunkLen < n) {
						n = TCI.rpcChunkLen;
					}
					SysRpcTcGetMultExeUnitChunkIn.exeUnitHnd_count = n;
					int j = 0;
					if (SysRpcTcGetMultExeUnitChunkIn.exeUnitHnd == null) {
						SysRpcTcGetMultExeUnitChunkIn.exeUnitHnd = new int[TCI.rpcChunkLen];
					}
					for (int i = parentIndex; i < (parentIndex + n); i++) {
						SysRpcTcGetMultExeUnitChunkIn.exeUnitHnd[j] = ((TcSysRpcExecutionUnit) parents.elementAt(i)).getHandle();
						j++;
					}
					SysRpcTcGetMultExeUnitChunkIn.kind.value = kind;
					SysRpcTcGetMultExeUnitChunkIn.iterHnd = iterHandle;
					client.client.SysRpcTcGetMultExeUnitChunk_1(SysRpcTcGetMultExeUnitChunkIn, SysRpcTcGetMultExeUnitChunkOut);
					if (SysRpcTcGetMultExeUnitChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetMultExeUnitChunkOut.nrOfExeUnitHnd;
						iterHandle = SysRpcTcGetMultExeUnitChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							TcSysRpcExecutionUnit lastParent = (TcSysRpcExecutionUnit) parents.elementAt(parentIndex);
							int lastParentHandle = lastParent.getHandle();
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcSysRpcExecutionUnit(SysRpcTcGetMultExeUnitChunkOut.exeUnitHnd[i], client);
								if (lastParentHandle == SysRpcTcGetMultExeUnitChunkOut.upperExeUnitHnd[i]) {
									elems[i].setParent(lastParent);
								} else {
									// looking for parent
									do {
										parentIndex++;
										lastParent = (TcSysRpcExecutionUnit) parents.elementAt(parentIndex);
										lastParentHandle = lastParent.getHandle();
									} while (lastParentHandle != SysRpcTcGetMultExeUnitChunkOut.upperExeUnitHnd[i]);
									elems[i].setParent(lastParent);
								}
							}
							if (loadInfos) {
								if (loadInfo(SysRpcTcGetMultExeUnitChunkOut.exeUnitHnd, nrOfHnd)) {
									return;
								}
							} else {
								return;
							}
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcExecutionModel - ExecutionUnitChunkEnumeration - getChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}
	}

	private static class VariableGroup extends TcVariableGroup {

		private VariableGroup(String name, int handle) {
			super(name, handle);
		}

		private int getHandle() {
			return handle;
		}
	}

	/**
	 * Requests a memory dump (allocated raw memory) from TeachControl.
	 * 
	 * @param _memoffset
	 *            The offset within the dump. This can be used if a certain
	 *            member of an object should be dumped rather than the whole
	 *            object. The offset of the respective member, however, must be
	 *            known.
	 * @param _buffersize
	 *            Specifies the amount of bytes that should be dumped.
	 * @param _accessHandle
	 *            Identifies the object within TeachControl
	 * @return A raw memory block containing the requestet information
	 */
	public int[] getTcMemdump(int _memoffset, int _buffersize, TcAccessHandle _accessHandle) {
		synchronized (SysRpcTcGetMemDumpIn) {
			try {

				SysRpcTcGetMemDumpIn.exeUnitHnd = 0;
				SysRpcTcGetMemDumpIn.varAccess = ((TcSysRpcStructuralModel.AccessHandle) _accessHandle).access;
				SysRpcTcGetMemDumpIn.bufferSize = _buffersize;
				SysRpcTcGetMemDumpIn.offset = _memoffset;

				client.client.SysRpcTcGetMemDump_1(SysRpcTcGetMemDumpIn, SysRpcTcGetMemDumpOut);
				if (SysRpcTcGetMemDumpOut.retVal) {
					return SysRpcTcGetMemDumpOut.buffer;
				}
			} catch (RPCException e) {
				e.printStackTrace();
			} catch (IOException ioex) {
				ioex.printStackTrace();
			} catch(NullPointerException npe) {
				//can occur upon system shutdown
			}

		}
		return null;
	}

	/**
	 * Creates a memory dump of a complex TeachControl Object. This means, the
	 * memory occupied by an object (e.g. an Area) is serialized on TC side and
	 * must be deserialized on TV side.
	 * 
	 * @param _structTypeNode
	 *            Structural Node that identifies the type of the object which
	 *            is to be dumped (e.g. TYPE AREA)
	 * @param _structVarNode
	 *            Variable Node that identifies the run-time instance of a
	 *            variable (e.g. anArea : AREA)
	 * @param _accessHandle
	 *            The run-time variable's access handle
	 * @param _execUnit
	 *            If the variable is routine-local, the execution handle
	 *            identifies the instance
	 * @return an array of integers that contain the memdump
	 * @throws NullPointerException
	 */
	public int[] getTcMemdump(TcStructuralNode _structTypeNode, TcStructuralNode _structVarNode, TcAccessHandle _accessHandle, TcExecutionUnit _execUnit) throws NullPointerException {
		synchronized (SysRpcTcGetMemDumpIn) {
			try {
				SysRpcTcGetVarInfoIn vin = new SysRpcTcGetVarInfoIn();
				SysRpcTcGetVarInfoOut vout = new SysRpcTcGetVarInfoOut();
				SysRpcTcGetTypeInfoIn tin = new SysRpcTcGetTypeInfoIn();
				SysRpcTcGetTypeInfoOut tout = new SysRpcTcGetTypeInfoOut();

				if (_structTypeNode != null) {
					int typeHnd = ((TcSysRpcStructuralTypeNode) _structTypeNode).getHandle();
					tin.typeScopeHnd = typeHnd;
				}
				if (_structVarNode != null) {
					int varHnd = ((TcSysRpcStructuralVarNode) _structVarNode).getHandle();
					vin.varScopeHnd = varHnd;
				}

				// get static type info
				client.client.SysRpcTcGetVarInfo_1(vin, vout);

				// get runtime variable info
				client.client.SysRpcTcGetTypeInfo_1(tin, tout);

				SysRpcTcGetMemDumpIn.exeUnitHnd = (_execUnit != null) ? ((TcSysRpcExecutionUnit) _execUnit).getHandle() : 0;
				SysRpcTcGetMemDumpIn.varAccess = ((TcSysRpcStructuralModel.AccessHandle) _accessHandle).access;
				SysRpcTcGetMemDumpIn.bufferSize = tout.info.typeSize;
				if (vout.retVal) {
					SysRpcTcGetMemDumpIn.offset = ((-1 * vout.info.incCnt) - 1);
				} else {
					SysRpcTcGetMemDumpIn.offset = 0;
				}

				client.client.SysRpcTcGetMemDump_1(SysRpcTcGetMemDumpIn, SysRpcTcGetMemDumpOut);
				if (SysRpcTcGetMemDumpOut.retVal) {
					return SysRpcTcGetMemDumpOut.buffer;
				}

			} catch (RPCException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Utility method to read an entire array variable from TeachControl instead of reading each element per se.
	 * Note that this only works for arrays of basic datatypes. Note also, that obtaining a memdump will always be faster!
	 * @see #getTcMemdump(int, int, TcAccessHandle)
	 * @see #getTcMemdump(TcStructuralNode, TcStructuralNode, TcAccessHandle, TcExecutionUnit)
	 * @param _execUnit Needed for routine-local variables, can be null in most cases
	 * @param _tcArrayVarNode A {@link TcStructuralVarNode} containing runtime-information of an instantiated object (myArr : ARRAY [0..2] OF DINT) such as object size. 
	 * @param _varAccess A variable access handle
	 * @param _elemType Static type information of the variable to correctly interpret the list of {@link TcValue} that TeachControl sends back.
	 * @return An array, whose element base type depends on the Tec
	 * 
	 */
	public Object[] readVarArray(TcExecutionUnit _execUnit, TcStructuralVarNode _tcArrayVarNode, TcAccessHandle _varAccess, TcStructuralTypeNode _elemType) {

		SysRpcTcGetActArrayValuesIn in = new SysRpcTcGetActArrayValuesIn();
		SysRpcTcGetActArrayValuesOut out = new SysRpcTcGetActArrayValuesOut();

		in.exeUnitHnd = _execUnit != null ? _execUnit.getHandle() : 0;
		in.varAccess = ((TcSysRpcStructuralModel.AccessHandle) _varAccess).access;
		in.offset = 0;
		int size = 0;
		TcSysRpcStructuralVarNode varNode = null;
		if (_tcArrayVarNode instanceof TcSysRpcStructuralVarNode) {
			size = ((TcSysRpcStructuralVarNode) _tcArrayVarNode).getTCMemSize();
			varNode = (TcSysRpcStructuralVarNode) _tcArrayVarNode;
		}
		in.bufferSize = size;
		try {
			client.client.SysRpcTcGetActArrayValues_1(in, out);

			if (out.retVal && varNode != null) {
				// parse return value (TcValue) into array

				int typeKind = _elemType.getTypeKind();
				Object[] result = new Object[out.nrElems];
				for (int i = 0; i < out.nrElems; i++) {

					TcValue v = new TcValue();
					v.boolValue = out.buffer[i].bValue;
					v.int8Value = (byte) out.buffer[i].i8Value;
					v.int16Value = (short) out.buffer[i].i16Value;
					v.int32Value = out.buffer[i].i32Value;
					v.int64Value = out.buffer[i].i64Value;
					v.floatValue = out.buffer[i].fValue;
					v.stringValue = out.buffer[i].sValue.toString();
					v.isValid = out.buffer[i].isValid;
					result[i] = v.getValue(typeKind);
				}
				return result;

			}
		} catch (RPCException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
