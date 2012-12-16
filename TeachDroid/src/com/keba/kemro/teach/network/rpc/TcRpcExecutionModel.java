package com.keba.kemro.teach.network.rpc;

import java.io.*;
import java.util.*;

import com.keba.jrpc.rpc.*;
import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.TcRpcStructuralModel.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcExecutionModel implements TcExecutionModel {
   /** alle Ausführungseinheiten */
	private static final int FILTER_ALL = RpcTcExeUnitKind.rpcFilterAllExeUnit;
   /** End user execution routine */
	private static final int FILTER_USER_ROUTINE = RpcTcExeUnitKind.rpcFilterUserRoutineExeUnit;
   /** End user execution routine */
	private static final int FILTER_ALL_USER = RpcTcExeUnitKind.rpcFilterAllUserExeUnit;
   /** 
    * TC Version < 3.00:
    * Hauptablaufzeiger - Debug - Modus, in diesem Modus sind alle namenlose Routinen,  erreichbar
    * von der Ablaufwurzel, im Debug - Modus.
    */
   private static final int DEBUG_MODE_MAIN_FLOW = 3;
   private static final int EXECUTION_MODE_MAIN_FLOW_STEPPING = RpcTcExeFlags.rpcExeFlagMainFlowStepping;

   private final RpcTcLoadGlobalIn rpcTcLoadGlobalIn = new RpcTcLoadGlobalIn();
   private final RpcTcLoadGlobalOut rpcTcLoadGlobalOut = new RpcTcLoadGlobalOut();
   private final RpcTcUnloadGlobalIn rpcTcUnloadGlobalIn = new RpcTcUnloadGlobalIn();
   private final RpcTcUnloadGlobalOut rpcTcUnloadGlobalOut = new RpcTcUnloadGlobalOut();
   private final RpcTcLoadProjIn rpcTcLoadProjIn = new RpcTcLoadProjIn();
   private final RpcTcLoadProjOut rpcTcLoadProjOut = new RpcTcLoadProjOut();
   private final RpcTcUnloadProjIn rpcTcUnloadProjIn = new RpcTcUnloadProjIn();
   private final RpcTcUnloadProjOut rpcTcUnloadProjOut = new RpcTcUnloadProjOut();
   private final RpcTcStartProgIn rpcTcStartProgIn = new RpcTcStartProgIn();
   private final RpcTcStartProgOut rpcTcStartProgOut = new RpcTcStartProgOut();
   private final RpcTcStartProgExIn rpcTcStartProgExIn = new RpcTcStartProgExIn();
   private final RpcTcStartProgExOut rpcTcStartProgExOut = new RpcTcStartProgExOut();
   private final RpcTcInterruptProgIn rpcTcInterruptProgIn = new RpcTcInterruptProgIn();
   private final RpcTcInterruptProgOut rpcTcInterruptProgOut = new RpcTcInterruptProgOut();
   private final RpcTcContinueProgIn rpcTcContinueProgIn = new RpcTcContinueProgIn();
   private final RpcTcContinueProgOut rpcTcContinueProgOut = new RpcTcContinueProgOut();
   private final RpcTcStopProgIn rpcTcStopProgIn = new RpcTcStopProgIn();
   private final RpcTcStopProgOut rpcTcStopProgOut = new RpcTcStopProgOut();
   private final RpcTcSetDebugModeIn rpcTcSetDebugModeIn = new RpcTcSetDebugModeIn();
   private final RpcTcSetDebugModeOut rpcTcSetDebugModeOut = new RpcTcSetDebugModeOut();
   private final RpcTcGetExeUnitStatusIn rpcTcGetExeUnitStatusIn = new RpcTcGetExeUnitStatusIn();
   private final RpcTcGetExeUnitStatusOut rpcTcGetExeUnitStatusOut =
      new RpcTcGetExeUnitStatusOut();
   private final RpcTcGetExeUnitStatusListIn rpcTcGetExeUnitStatusListIn =
      new RpcTcGetExeUnitStatusListIn();
   private final RpcTcGetExeUnitStatusListOut rpcTcGetExeUnitStatusListOut =
      new RpcTcGetExeUnitStatusListOut();
   private final RpcTcStepIn rpcTcStepIn = new RpcTcStepIn();
   private final RpcTcStepOut rpcTcStepOut = new RpcTcStepOut();
   private final RpcTcStepListIn rpcTcStepListIn = new RpcTcStepListIn();
   private final RpcTcStepListOut rpcTcStepListOut = new RpcTcStepListOut();
   private final RpcTcGetExeUnitInfoListIn rpcTcGetExeUnitInfoListIn =
      new RpcTcGetExeUnitInfoListIn();
   private final RpcTcGetExeUnitInfoListOut rpcTcGetExeUnitInfoListOut =
      new RpcTcGetExeUnitInfoListOut();
   private final RpcTcGetFirstExeUnitChunkIn rpcTcGetFirstExeUnitChunkIn =
      new RpcTcGetFirstExeUnitChunkIn();
   private final RpcTcGetFirstExeUnitChunkOut rpcTcGetFirstExeUnitChunkOut =
      new RpcTcGetFirstExeUnitChunkOut();
   private final RpcTcGetNextExeUnitChunkIn rpcTcGetNextExeUnitChunkIn =
      new RpcTcGetNextExeUnitChunkIn();
   private final RpcTcGetNextExeUnitChunkOut rpcTcGetNextExeUnitChunkOut =
      new RpcTcGetNextExeUnitChunkOut();
   private final RpcTcSetInstructionPointerIn rpcTcSetInstructionPointerIn =
      new RpcTcSetInstructionPointerIn();
   private final RpcTcSetInstructionPointerOut rpcTcSetInstructionPointerOut =
      new RpcTcSetInstructionPointerOut();
   private final RpcTcExecuteMethodExtIn rpcTcExecuteMethodExtIn = new RpcTcExecuteMethodExtIn();
   private final RpcTcExecuteMethodExtOut rpcTcExecuteMethodExtOut = new RpcTcExecuteMethodExtOut();
   private final RpcTcGetMultExeUnitChunkIn rpcTcGetMultExeUnitChunkIn =
      new RpcTcGetMultExeUnitChunkIn();
   private final RpcTcGetMultExeUnitChunkOut rpcTcGetMultExeUnitChunkOut =
      new RpcTcGetMultExeUnitChunkOut();
   private final RpcTcSetExeFlagIn rpcTcSetExeFlagIn = new RpcTcSetExeFlagIn();
   private final RpcTcSetExeFlagOut rpcTcSetExeFlagOut = new RpcTcSetExeFlagOut();
   private final RpcTcGetMapTargetIn rpcTcGetMapTargetIn = new RpcTcGetMapTargetIn();
   private final RpcTcGetMapTargetOut rpcTcGetMapTargetOut = new RpcTcGetMapTargetOut();
   private final RpcTcSetMapTargetIn rpcTcSetMapTargetIn = new RpcTcSetMapTargetIn();
   private final RpcTcSetMapTargetOut rpcTcSetMapTargetOut = new RpcTcSetMapTargetOut();
   private final RpcTcGetActValueIn rpcTcGetActValueIn = new RpcTcGetActValueIn();
   private final RpcTcGetActValueOut rpcTcGetActValueOut = new RpcTcGetActValueOut();
   private final RpcTcGetActValueListIn rpcTcGetActValueListIn = new RpcTcGetActValueListIn();
   private final RpcTcGetActValueListOut rpcTcGetActValueListOut = new RpcTcGetActValueListOut();
   private final RpcTcSetActValueIn rpcTcSetActValueIn = new RpcTcSetActValueIn();
   private final RpcTcSetActValueOut rpcTcSetActValueOut = new RpcTcSetActValueOut();
   private final RpcTcCopyActValueIn rpcTcCopyActValueIn = new RpcTcCopyActValueIn();
   private final RpcTcCopyActValueOut rpcTcCopyActValueOut = new RpcTcCopyActValueOut();
   private final RpcTcNewVarGroupIn rpcTcNewVarGroupIn = new RpcTcNewVarGroupIn();
   private final RpcTcNewVarGroupOut rpcTcNewVarGroupOut = new RpcTcNewVarGroupOut();
   private final RpcTcRemoveVarGroupIn rpcTcRemoveVarGroupIn = new RpcTcRemoveVarGroupIn();
   private final RpcTcRemoveVarGroupOut rpcTcRemoveVarGroupOut = new RpcTcRemoveVarGroupOut();
   private final RpcTcAddVarsToGroupIn rpcTcAddVarsToGroupIn = new RpcTcAddVarsToGroupIn();
   private final RpcTcAddVarsToGroupOut rpcTcAddVarsToGroupOut = new RpcTcAddVarsToGroupOut();
   private final RpcTcRemoveVarsFromGroupIn rpcTcRemoveVarsFromGroupIn = new RpcTcRemoveVarsFromGroupIn();
   private final RpcTcRemoveVarsFromGroupOut rpcTcRemoveVarsFromGroupOut = new RpcTcRemoveVarsFromGroupOut();
   private final RpcTcReadVarGroupValuesIn rpcTcReadVarGroupValuesIn = new RpcTcReadVarGroupValuesIn();
   private final RpcTcReadVarGroupValuesOut rpcTcReadVarGroupValuesOut = new RpcTcReadVarGroupValuesOut();
   private final RpcTcReadChangedVarGroupValuesIn rpcTcReadChangedVarGroupValuesIn = new RpcTcReadChangedVarGroupValuesIn();
   private final RpcTcReadChangedVarGroupValuesOut rpcTcReadChangedVarGroupValuesOut = new RpcTcReadChangedVarGroupValuesOut();
   
   TcRpcClient client;
   
   TcRpcExecutionModel (TcRpcClient client) {
   	this.client = client;
   }

   /**
    * Liefert für den angegebene Zugriffs - Handle der Variable den Aktualwert.
    *
    * @param accessHandle Zugriffs - Handle
    * @param exeUnit Ausführungseinheit des Programms, in der die Variable deklariert wurde
    * @param value Aktualwert
    *
    * @return true für einen erfolgreichen Aufruf
    */
   public boolean getActualValue (TcAccessHandle accessHandle, TcExecutionUnit execUnit,
                                         TcValue value) {
      try {
         synchronized (rpcTcGetActValueIn) {
            rpcTcGetActValueIn.exeUnitHnd = (execUnit != null) ? ((TcRpcExecutionUnit) execUnit).getHandle(): 0;
            rpcTcGetActValueIn.varAccess = ((TcRpcStructuralModel.AccessHandle) accessHandle).access;
            client.client.RpcTcGetActValue_1(rpcTcGetActValueIn, rpcTcGetActValueOut);
            if (rpcTcGetActValueOut.retVal) {
               value.boolValue = rpcTcGetActValueOut.value.bValue;
               value.int8Value = (byte) rpcTcGetActValueOut.value.i8Value;
               value.int16Value = (short) rpcTcGetActValueOut.value.i16Value;
               value.int32Value = rpcTcGetActValueOut.value.i32Value;
               value.int64Value = rpcTcGetActValueOut.value.i64Value;
               value.floatValue = rpcTcGetActValueOut.value.fValue;
               value.stringValue = rpcTcGetActValueOut.value.sValue;
               value.isValid = rpcTcGetActValueOut.value.isValid;
               return true;
            }
            value.isValid = false;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - getActualValue: ");
      }
      return false;
   }

   /**
    * Setzt für den angegebene Zugriffs - Handle der Variable den Aktualwert.
    *
    * @param accessHandle Zugriffs - Handle
    * @param unit Ausführungseinheit des Programms, in der die Variable deklariert wurde
    * @param value Aktualwert
    *
    * @return true für einen erfolgreichen Aufruf
    */
   public boolean setActualValue (TcAccessHandle accessHandle, TcExecutionUnit execUnit,
                                         TcValue value) {
      try {
         synchronized (rpcTcSetActValueIn) {
            rpcTcSetActValueIn.exeUnitHnd = (execUnit != null) ? ((TcRpcExecutionUnit) execUnit).getHandle(): 0;
            rpcTcSetActValueIn.varAccess = ((TcRpcStructuralModel.AccessHandle) accessHandle).access;
            rpcTcSetActValueIn.value.bValue = value.boolValue;
            rpcTcSetActValueIn.value.i8Value = value.int8Value;
            rpcTcSetActValueIn.value.i16Value = value.int16Value;
            rpcTcSetActValueIn.value.i32Value = value.int32Value;
            rpcTcSetActValueIn.value.i64Value = value.int64Value;
            rpcTcSetActValueIn.value.fValue = value.floatValue;
            rpcTcSetActValueIn.value.sValue = value.stringValue;
            client.client.RpcTcSetActValue_1(rpcTcSetActValueIn, rpcTcSetActValueOut);
            return rpcTcSetActValueOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - getInitValue: ");
      }
      return false;
   }
   /**
    * Copies the actual value from srouce variable to the destination variable
    * @param srcAccessHandle access handle of the source variable
    * @param srcExeUnit executioin handle of the routine, only necessary if the source variable is a routine variable
    * @param destAccessHandle access handle of the destination variable
    * @param destExeUnit executioin handle of the routine, only necessary if the destination variable is a routine variable
    * @return true if the value was successfully copied
    */
   public boolean copyActualValue(TcAccessHandle srcAccessHandle, TcExecutionUnit srcExecUnit,
		TcAccessHandle destAccessHandle, TcExecutionUnit destExecUnit) {
		try {
			synchronized (rpcTcCopyActValueIn) {
				rpcTcCopyActValueIn.srcExeUnitHnd = (srcExecUnit != null) ? ((TcRpcExecutionUnit) srcExecUnit).getHandle() : 0;
				rpcTcCopyActValueIn.srcVarAccess = ((TcRpcStructuralModel.AccessHandle) srcAccessHandle).access;
				rpcTcCopyActValueIn.destExeUnitHnd = (destExecUnit != null) ? ((TcRpcExecutionUnit) destExecUnit).getHandle() : 0;
				rpcTcCopyActValueIn.destVarAccess = ((TcRpcStructuralModel.AccessHandle) destAccessHandle).access;
				client.client.RpcTcCopyActValue_1(rpcTcCopyActValueIn, rpcTcCopyActValueOut);
				return rpcTcCopyActValueOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - copyActualValue: ");
		}
		return false;
	}

   /**
    * Lefert für die angegebenen Zugriffs - Handles der Variablen die Aktualwerte.
    *
    * @param accessHandles enthält Zugriffs - Handles
    * @param execUnits Ausführungseinheiten der Programme, in denen die Variablen deklariert wurden
    * @param values Aktualwerte
    *
    * @return true für einen erfolgreichen Aufruf
    */
   public boolean getActualValues (Vector accessHandles, Vector execUnits, Vector values) {
      try {
         synchronized (rpcTcGetActValueListIn) {
            int index = 0;
            while (index < execUnits.size()) {
               int end = index + rpcChunkLen.value;
               if (end >= execUnits.size()) {
                  end = execUnits.size();
               }
               for (int i = index; i < end; i++) {
               	TcRpcExecutionUnit oeu = (TcRpcExecutionUnit) execUnits.elementAt(i);
                  AccessHandle ah = (AccessHandle) accessHandles.elementAt(i);
                  rpcTcGetActValueListIn.exeUnitHnd[i - index] = (oeu != null) ? oeu.getHandle(): 0;
                  rpcTcGetActValueListIn.varAccess[i - index] = ah.access;
               }
               rpcTcGetActValueListIn.nrOfActValues = end - index;
               rpcTcGetActValueListIn.exeUnitHnd_count = end - index;
               rpcTcGetActValueListIn.varAccess_count = end - index;
               client.client.RpcTcGetActValueList_1(rpcTcGetActValueListIn, rpcTcGetActValueListOut);
               if (rpcTcGetActValueListOut.retVal) {
                  for (int i = index; i < end; i++) {
                     TcValue v = (TcValue) values.elementAt(i);
                     v.boolValue = rpcTcGetActValueListOut.value[i - index].bValue;
                     v.int8Value = (byte) rpcTcGetActValueListOut.value[i - index].i8Value;
                     v.int16Value = (short) rpcTcGetActValueListOut.value[i - index].i16Value;
                     v.int32Value = rpcTcGetActValueListOut.value[i - index].i32Value;
                     v.int64Value = rpcTcGetActValueListOut.value[i - index].i64Value;
                     v.floatValue = rpcTcGetActValueListOut.value[i - index].fValue;
                     v.stringValue = rpcTcGetActValueListOut.value[i - index].sValue;
                     v.isValid = rpcTcGetActValueListOut.value[i - index].isValid;
                  }
               } else {
                  return false;
               }
               index = index + rpcChunkLen.value;
            }
            return true;
         }
      } catch (Exception e) {
         //System.out.println("Disconnect in TcStructuralModel - getActualValues: ");
      }
      return false;
   }

   /**
    * Gets the map target.
    * @param accessHandle access handle of the variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param target mag target
    * @return true if the target is successfully gotten
    */
   public boolean getMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcMapTarget target) {
   	target.setValid(false);
      try {
         synchronized (rpcTcGetMapTargetIn) {
            rpcTcGetMapTargetIn.exeUnitHnd = (execUnit != null) ? ((TcRpcExecutionUnit) execUnit).getHandle(): 0;
            rpcTcGetMapTargetIn.varAccess = ((AccessHandle) accessHandle).access;
            client.client.RpcTcGetMapTarget_1(rpcTcGetMapTargetIn, rpcTcGetMapTargetOut);
            if (
                rpcTcGetMapTargetOut.retVal &&
                (rpcTcGetMapTargetOut.target.kind.value != TcMapTarget.MAP_TARGET_VOID)) {
            	boolean isInternal =
                  rpcTcGetMapTargetOut.target.kind.value == TcMapTarget.MAP_TARGET_INTERNAL;
               Object[] path = null;
               TcStructuralRoutineNode sr = null;
               String external = null;
               if (isInternal) {
                  path = new Object[rpcTcGetMapTargetOut.target.instancePath.nrOfElems];
                  for (int j = 0; j < rpcTcGetMapTargetOut.target.instancePath.nrOfElems;
                       j++) {
                     if (
                         rpcTcGetMapTargetOut.target.instancePath.elems[j].structComponent == 0) {
                        path[j] =
                           new Integer(rpcTcGetMapTargetOut.target.instancePath.elems[j].arrayIndex);
                     } else {
                     	if (j == 0) {
                     		//first element is maybe a program
                     		int handle = rpcTcGetMapTargetOut.target.instancePath.elems[j].structComponent;
                     		TcStructuralNode tn = client.cache.getFromDirEntryCache(handle);
                     		if (tn != null) {
                     			path[j] = tn;
                     		} else {
                     			path[j] = ((TcRpcStructuralModel) client.structure).createNode(handle);
                     		}
                     	} else {
                        	path[j] =
                              new TcRpcStructuralVarNode(rpcTcGetMapTargetOut.target.instancePath.elems[j].structComponent, client);
                     	}
                     }
                  }
                  sr = (rpcTcGetMapTargetOut.target.routineHdl != 0)
                       ? new TcRpcStructuralRoutineNode(rpcTcGetMapTargetOut.target.routineHdl, client) : null;
               } else {
                  external = rpcTcGetMapTargetOut.target.external;
               }
               ((TcMapTarget) target).setValues(isInternal, path, sr, external);
               ((TcMapTarget) target).setValid(true);
               return true;
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - getMapTarget: ");
      }
      return false;
   }
   
   /**
    * Sets the map target. Target is a teachtalk variable or component.
    * @param accessHandle map to variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param path instance path of the destination
    * @return true if the target is successfully set
    */
   public boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, Object[] path) {
   	TcMapTarget target = new TcMapTarget();
   	target.setValues(true, path, null, null);
   	return setMapTarget(accessHandle, execUnit, target);
   }
   /**
    * Sets the map target. Target is program routine or a unit routine.
    * @param accessHandle map to variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param path if routine is a routine of the unit then the the instance path is the instance of the unit variable
    * @param routine new routine destination 
    * @return true if the target is successfully set
    */
   public boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, Object[] path, TcStructuralRoutineNode routine) {
   	TcMapTarget target = new TcMapTarget();
   	target.setValues(true, path, routine, null);
   	return setMapTarget(accessHandle, execUnit, target); 	
   }
   
   /**
    * Sets the map target.
    * @param accessHandle map to variable
    * @param exeUnit execution handle of the routine to which the variable belongs
    * @param external new external variable destination
    * @return true if the target is successfully set
    */
   public boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, String external) {
   	TcMapTarget target = new TcMapTarget();
   	target.setValues(false, null, null, external);
   	return setMapTarget(accessHandle, execUnit, target);   	
   }
   
   private boolean setMapTarget (TcAccessHandle accessHandle, TcExecutionUnit execUnit, TcMapTarget target) {
      try {
         synchronized (rpcTcSetMapTargetIn) {
         	rpcTcSetMapTargetIn.varAccess = ((AccessHandle) accessHandle).access;
         	rpcTcSetMapTargetIn.exeUnitHnd = (execUnit != null) ? ((TcRpcExecutionUnit) execUnit).getHandle(): 0;
         	if (target.isInternal()) {
         		rpcTcSetMapTargetIn.target.kind.value = TcMapTarget.MAP_TARGET_INTERNAL;
         		Object[] path = target.getPath();
         		if (path != null) {
            		rpcTcSetMapTargetIn.target.instancePath.nrOfElems = path.length;
            		rpcTcSetMapTargetIn.target.instancePath.elems_count = path.length;
            		for (int i = 0; i < path.length; i++) {
            			if (path[i] instanceof TcStructuralNode) {
            				rpcTcSetMapTargetIn.target.instancePath.elems[i].structComponent = ((TcRpcStructuralNode) path[i]).getHandle();
            				rpcTcSetMapTargetIn.target.instancePath.elems[i].arrayIndex = -1;
            			} else {
            				rpcTcSetMapTargetIn.target.instancePath.elems[i].structComponent = 0;
            				rpcTcSetMapTargetIn.target.instancePath.elems[i].arrayIndex = ((Integer) path[i]).intValue();
            			}
            		}
         		} else {
            		rpcTcSetMapTargetIn.target.instancePath.nrOfElems = 0;
            		rpcTcSetMapTargetIn.target.instancePath.elems_count = 0;
         		}
         		if (target.isInternalMapToRoutine()) {
            		rpcTcSetMapTargetIn.target.routineHdl = ((TcRpcExecutionUnit) target.getRoutine()).getHandle();
         		} else {
            		rpcTcSetMapTargetIn.target.routineHdl = 0;
         		}
         		rpcTcSetMapTargetIn.target.external = "";
         	} else {
         		rpcTcSetMapTargetIn.target.kind.value = TcMapTarget.MAP_TARGET_EXTERNAL;
         		rpcTcSetMapTargetIn.target.instancePath.nrOfElems = 0;
         		rpcTcSetMapTargetIn.target.instancePath.elems_count = 0;
         		rpcTcSetMapTargetIn.target.routineHdl = 0;
         		rpcTcSetMapTargetIn.target.external = target.getExternalMap();
         	}
            client.client.RpcTcSetMapTarget_1(rpcTcSetMapTargetIn, rpcTcSetMapTargetOut);
            return rpcTcSetMapTargetOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - setMapTarget: ");
      }
      return false;
   }
   
   public TcVariableGroup createVariableGroup (String groupName) {
      try {
         synchronized (rpcTcNewVarGroupIn) {
         	rpcTcNewVarGroupIn.groupName = groupName;
            client.client.RpcTcNewVarGroup_1(rpcTcNewVarGroupIn, rpcTcNewVarGroupOut);
            if (rpcTcNewVarGroupOut.retVal) {
            	return new VariableGroup(groupName, rpcTcNewVarGroupOut.varGroupHnd);
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - createVariableGroup: ");
      }
      return null;
   }

	public boolean removeVariableGroup (TcVariableGroup group) {
		try {
			synchronized (rpcTcRemoveVarGroupIn) {
				rpcTcRemoveVarGroupIn.varGroupHnd = ((VariableGroup) group).getHandle();
				client.client.RpcTcRemoveVarGroup_1(rpcTcRemoveVarGroupIn, rpcTcRemoveVarGroupOut);
				return rpcTcRemoveVarGroupOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - removeVariableGroup: ");
		}
      return false;
   }

	public int[] addVariablesToGroup (TcVariableGroup group, Vector accessHandles) {
      try {
         synchronized (rpcTcAddVarsToGroupIn) {
         	int[] variableIds = new int[accessHandles.size()];
         	rpcTcAddVarsToGroupIn.varGroupHnd = ((VariableGroup) group).getHandle();
            int index = 0;
            while (index < accessHandles.size()) {
               int end = index + rpcChunkLen.value;
               if (end >= accessHandles.size()) {
                  end = accessHandles.size();
               }
               for (int i = index; i < end; i++) {
                  AccessHandle ah = (AccessHandle) accessHandles.elementAt(i);
                  rpcTcAddVarsToGroupIn.exeUnitHnd[i - index] = 0;
                  rpcTcAddVarsToGroupIn.varAccess[i - index] = ah.access;
               }
               rpcTcAddVarsToGroupIn.exeUnitHnd_count = end - index;
               rpcTcAddVarsToGroupIn.varAccess_count = end - index;
               client.client.RpcTcAddVarsToGroup_1(rpcTcAddVarsToGroupIn, rpcTcAddVarsToGroupOut);
               if (rpcTcAddVarsToGroupOut.retVal) {
               	for (int i = index; i < end; i++) {
                     variableIds[i] = rpcTcAddVarsToGroupOut.varId[i - index];
               	}
               } else {
                  return null;
               }
               index = index + rpcChunkLen.value;
            }
            return variableIds;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - removeVariableGroup: ");
      }
      return null;
   }

	public boolean removeVariablesFromGroup (TcVariableGroup group, int[] variableIds) {
      try {
         synchronized (rpcTcRemoveVarsFromGroupIn) {
         	rpcTcRemoveVarsFromGroupIn.varGroupHnd = ((VariableGroup) group).getHandle();
            int index = 0;
            while (index < variableIds.length) {
               int end = index + rpcChunkLen.value;
               if (end >= variableIds.length) {
                  end = variableIds.length;
               }
               for (int i = index; i < end; i++) {
                  rpcTcRemoveVarsFromGroupIn.varId[i - index] = variableIds[i];
               }
               rpcTcRemoveVarsFromGroupIn.varId_count = end - index;
               client.client.RpcTcRemoveVarsFromGroup_1(rpcTcRemoveVarsFromGroupIn, rpcTcRemoveVarsFromGroupOut);
               if (!rpcTcRemoveVarsFromGroupOut.retVal) {
                  return false;
               }
               index = index + rpcChunkLen.value;
            }
            return true;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - removeVariableGroup: ");
      }
      return false;
   }

	public int[] getVariableGroupValues (TcVariableGroup group, Vector values) {
      try {
         synchronized (rpcTcReadVarGroupValuesIn) {
         	rpcTcReadVarGroupValuesIn.varGroupHnd = ((VariableGroup) group).getHandle();
         	int[] variableIDs = null;
         	int startIndex = -rpcChunkLen.value;
         	do {
         	   startIndex += rpcChunkLen.value;
         	   rpcTcReadVarGroupValuesIn.startIdx = startIndex;
         	   client.client.RpcTcReadVarGroupValues_1(rpcTcReadVarGroupValuesIn, rpcTcReadVarGroupValuesOut);
         	   if (rpcTcReadVarGroupValuesOut.retVal) {
         	      int length = (variableIDs == null) ? 0: variableIDs.length;
               	int[] help = new int[length + rpcTcReadVarGroupValuesOut.varId_count];
         	      if (0 < length) {
         	         System.arraycopy(variableIDs, 0, help, 0, length);
         	      }
         	      variableIDs = help;
                 	for (int i = 0; i < rpcTcReadVarGroupValuesOut.value_count; i++) {
                 		variableIDs[length + i] = rpcTcReadVarGroupValuesOut.varId[i];
                    	TcValue v = (TcValue) values.elementAt(startIndex + i);
                    	v.boolValue = rpcTcReadVarGroupValuesOut.value[i].bValue;
                    	v.int8Value = (byte) rpcTcReadVarGroupValuesOut.value[i].i8Value;
	                  v.int16Value = (short) rpcTcReadVarGroupValuesOut.value[i].i16Value;
                    	v.int32Value = rpcTcReadVarGroupValuesOut.value[i].i32Value;
                    	v.int64Value = rpcTcReadVarGroupValuesOut.value[i].i64Value;
                    	v.floatValue = rpcTcReadVarGroupValuesOut.value[i].fValue;
                    	v.stringValue = rpcTcReadVarGroupValuesOut.value[i].sValue;
                    	v.isValid = rpcTcReadVarGroupValuesOut.value[i].isValid;
                 }
               }
         	} while (rpcTcReadVarGroupValuesOut.retVal && (rpcTcReadVarGroupValuesOut.value_count == rpcChunkLen.value));
            return variableIDs;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcStructuralModel - getVariableGroupValues: ");
      }
      return null;
   }

	public int[] getChangedVariableGroupValues (TcVariableGroup group, Vector values) {
      try {
         synchronized (rpcTcReadChangedVarGroupValuesIn) {
         	rpcTcReadChangedVarGroupValuesIn.varGroupHnd = ((VariableGroup) group).getHandle();
         	int[] variableIDs = null;
         	int startIndex = -rpcChunkLen.value;
         	do {
         	   startIndex += rpcChunkLen.value;
         	   rpcTcReadChangedVarGroupValuesIn.startIdx = startIndex;
               client.client.RpcTcReadChangedVarGroupValues_1(rpcTcReadChangedVarGroupValuesIn, rpcTcReadChangedVarGroupValuesOut);
         	   if (rpcTcReadChangedVarGroupValuesOut.retVal) {
         	      int length = (variableIDs == null) ? 0: variableIDs.length;
               	int[] help = new int[length + rpcTcReadChangedVarGroupValuesOut.varId_count];
         	      if (0 < length) {
         	         System.arraycopy(variableIDs, 0, help, 0, length);
         	      }
         	      variableIDs = help;
                 	for (int i = 0; i < rpcTcReadChangedVarGroupValuesOut.value_count; i++) {
                 		variableIDs[length + i] = rpcTcReadChangedVarGroupValuesOut.varId[i];
                    	TcValue v = (TcValue) values.elementAt(startIndex + i);
                    	v.boolValue = rpcTcReadChangedVarGroupValuesOut.value[i].bValue;
                    	v.int8Value = (byte) rpcTcReadChangedVarGroupValuesOut.value[i].i8Value;
	                  v.int16Value = (short) rpcTcReadChangedVarGroupValuesOut.value[i].i16Value;
                    	v.int32Value = rpcTcReadChangedVarGroupValuesOut.value[i].i32Value;
                    	v.int64Value = rpcTcReadChangedVarGroupValuesOut.value[i].i64Value;
                    	v.floatValue = rpcTcReadChangedVarGroupValuesOut.value[i].fValue;
                    	v.stringValue = rpcTcReadChangedVarGroupValuesOut.value[i].sValue;
                    	v.isValid = rpcTcReadChangedVarGroupValuesOut.value[i].isValid;
                 }
               }
         	} while (rpcTcReadChangedVarGroupValuesOut.retVal && (rpcTcReadChangedVarGroupValuesOut.value_count == rpcChunkLen.value));
            return variableIDs;
         }
      } catch (Exception e) {
          System.out.println("Disconnect in TcStructuralModel - getChangedVariableGroupValues: ");
      }
      return null;
   }

   /**
    * Liefert die Enumeration der Ausführungseinheiten, die unter der Ausführungseinheit parent
    * liegen.
    *
    * @param parent
    * @param kind ist die Art der Ausführungeinheiten, welche bei der Enumeration geliefert werden
    *        sollen.
    * @param loadInfos gibt an, ob auch die Knoteninformationen der Ausführungseinheiten gelesen
    *        werden sollen.
    *
    * @return Enumeration
    */
   public Enumeration getExecutionUnits (TcExecutionUnit parent, int kind, boolean loadInfos) {
      return new ExecutionUnitChunkEnumeration((TcRpcExecutionUnit) parent, kind, loadInfos);
   }

   /**
    * Liefert die Enumeration der Ausführungseinheiten, die unter den Ausführungseinheiten im
    * Vektor parent liegen.
    *
    * @param parents Ausführungseinheiten
    * @param kind ist die Art der Ausführungeinheiten, welche bei der Enumeration geliefert werden
    *        sollen.
    * @param loadInfos gibt an, ob auch die Knoteninformationen der Ausführungseinheiten gelesen
    *        werden sollen.
    *
    * @return Enumeration
    */
   public Enumeration getExecutionUnits (Vector parents, int kind, boolean loadInfos) {
      return new MultiExecutionUnitChunkEnumeration(parents, kind, loadInfos);
   }

   /**
    * Liefert die Wurzel aller Ausführungseinheiten.
    *
    * @return Wurzel
    */
   public TcExecutionUnit getRoot () {
      return new TcRpcExecutionUnit(0, client);
   }

   /**
    * Bringt das angegebene Projekt project in Ausführung.
    *
    * @param project Projekt, welches geladen werden soll.
    *
    * @return Ausführungseinheit für das geladene Projekt
    */
   public TcExecutionUnit loadProject (TcStructuralNode project) {
      try {
         if ((project.getKind() == TcStructuralNode.GLOBAL) || (project.getKind() == TcStructuralNode.SYSTEM)) {
            synchronized (rpcTcLoadGlobalIn) {
               rpcTcLoadGlobalIn.globalScopeHnd = ((TcRpcStructuralNode) project).getHandle();
               client.client.RpcTcLoadGlobal_1(rpcTcLoadGlobalIn, rpcTcLoadGlobalOut);
               if (rpcTcLoadGlobalOut.retVal) {
                  return new TcRpcExecutionUnit(rpcTcLoadGlobalOut.exeUnitHnd, client);
               }
            }
         } else {          
            synchronized (rpcTcLoadProjIn) {
               rpcTcLoadProjIn.projScopeHnd = ((TcRpcStructuralNode) project).getHandle();
               client.client.RpcTcLoadProj_1(rpcTcLoadProjIn, rpcTcLoadProjOut);
               if (rpcTcLoadProjOut.retVal) {
                  return new TcRpcExecutionUnit(rpcTcLoadProjOut.exeUnitHnd, client);
               }
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - loadProject: ");
      }
      return null;
   }

   /**
    * Entlädt das Projekt project.
    *
    * @param project Projekt, welches abgeladen werden soll.
    *
    * @return true für das erfolgreiche Abladen
    */
   public boolean unloadProject (TcExecutionUnit project) {
      try {
         if ((project.getKind() == TcExecutionUnit.GLOBAL) || (project.getKind() == TcExecutionUnit.SYSTEM)) {
            synchronized (rpcTcUnloadGlobalIn) {
               rpcTcUnloadGlobalIn.exeUnitHnd = ((TcRpcExecutionUnit) project).getHandle();
               client.client.RpcTcUnloadGlobal_1(rpcTcUnloadGlobalIn, rpcTcUnloadGlobalOut);
               return rpcTcUnloadGlobalOut.retVal;
            }               
         }
         synchronized (rpcTcUnloadProjIn) {
            rpcTcUnloadProjIn.exeUnitHnd = ((TcRpcExecutionUnit) project).getHandle();
            client.client.RpcTcUnloadProj_1(rpcTcUnloadProjIn, rpcTcUnloadProjOut);
            return rpcTcUnloadProjOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - unloadProject: ");
      }
      return false;
   }

   /**
    * Startet die unbenannte Routine des Programms program.
    *
    * @param program Programm, das die unbenannte Routine enthält.
    *
    * @return Ausführungseinheit für die unbenannte Routine
    */
   public TcExecutionUnit startProgram (TcStructuralNode program) {
      try {
         synchronized (rpcTcStartProgIn) {
            rpcTcStartProgIn.progScopeHnd = ((TcRpcStructuralNode) program).getHandle();
            client.client.RpcTcStartProg_1(rpcTcStartProgIn, rpcTcStartProgOut);
            if (rpcTcStartProgOut.retVal) {
               return new TcRpcExecutionUnit(rpcTcStartProgOut.exeUnitHnd, client);
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - startProgram: ");
      }
      return null;
   }

   /**
    * Starts the unnamed routine of the program.
    * @param program program to start
    * @param interrupt if true the unnamed routine will be stopped at the first statement 
    * @param restart if true the unnamed routine will be automatically restarted
    * @return execution unit for the unnamed routine
    */
   public TcExecutionUnit startProgram (TcStructuralNode program, boolean interrupt, boolean restart) {
      try {
 			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			if (((major == 2) && (88 <= minor)) || (3 <= major)) {
         	synchronized (rpcTcStartProgExIn) {
               rpcTcStartProgExIn.progScopeHnd = ((TcRpcStructuralNode) program).getHandle();
               if (interrupt) {
               	rpcTcStartProgExIn.startFlags |= RpcTcStartFlags.rpcStartFlagInterrupt;
               }
               if (restart) {
               	rpcTcStartProgExIn.startFlags |= RpcTcStartFlags.rpcStartFlagRestart;
               }
               client.client.RpcTcStartProgEx_1(rpcTcStartProgExIn, rpcTcStartProgExOut);
               if (rpcTcStartProgExOut.retVal) {
                  return new TcRpcExecutionUnit(rpcTcStartProgExOut.exeUnitHnd, client);
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
         System.out.println("Disconnect in TcExecutionModel - startProgram: ");
      }
      return null;
   }

   /**
    * Unterbricht alle Routine des Ablaufes.
    *
    * @param exeUnit Wurzel eines Ablaufs
    *
    * @return true für das erfolgreiche Unterbrechen
    */
   public boolean interruptExeUnit (TcExecutionUnit execUnit) {
      try {
         synchronized (rpcTcInterruptProgIn) {
            rpcTcInterruptProgIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            client.client.RpcTcInterruptProg_1(rpcTcInterruptProgIn, rpcTcInterruptProgOut);
            return rpcTcInterruptProgOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - interruptExeUnit: ");
      }
      return false;
   }

   /**
    * Setzt den Ablauf fort.
    *
    * @param exeUnit Wurzel eines Ablaufs
    *
    * @return true für das erfolgreiche Fortsetzen
    */
   public boolean continueExeUnit (TcExecutionUnit execUnit) {
      try {
         synchronized (rpcTcContinueProgIn) {
            rpcTcContinueProgIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            client.client.RpcTcContinueProg_1(rpcTcContinueProgIn, rpcTcContinueProgOut);
            return rpcTcContinueProgOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - continueExeUnit: ");
      }
      return false;
   }

   /**
    * Stopt den Ablauf.
    *
    * @param exeUnit Wurzel eines Ablaufs
    *
    * @return true für das erfolgreiche Stoppen
    */
   public boolean stopExeUnit (TcExecutionUnit execUnit) {
      try {
         synchronized (rpcTcStopProgIn) {
            rpcTcStopProgIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            client.client.RpcTcStopProg_1(rpcTcStopProgIn, rpcTcStopProgOut);
            return rpcTcStopProgOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - stopExeUnit: ");
      }
      return false;
   }

   /**
    * Sets the execution mode.
    * @param exeUnit execution unit
    * @param mode {EXECUTION_MODE_FLOW, EXECUTION_MODE_ROUTINE, EXECUTION_MODE_OFF} 
    * @return true if the mode was successfully set
    */
   public boolean setExecutionMode (TcExecutionUnit execUnit, int mode) {
      try {
			int major = client.getMajorVersion();
			if (3 <= major) {
				synchronized (rpcTcSetExeFlagIn) {
					rpcTcSetExeFlagIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
					rpcTcSetExeFlagIn.exeFlag.value = mode;
					rpcTcSetExeFlagIn.value = true;
               client.client.RpcTcSetExeFlag_1(rpcTcSetExeFlagIn, rpcTcSetExeFlagOut);
               return rpcTcSetExeFlagOut.retVal;
				}
			}
         synchronized (rpcTcSetDebugModeIn) {
            rpcTcSetDebugModeIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            rpcTcSetDebugModeIn.exeFlags.value = mode;
            client.client.RpcTcSetDebugMode_1(rpcTcSetDebugModeIn, rpcTcSetDebugModeOut);
            return rpcTcSetDebugModeOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - setStepMode: ");
      }
      return false;
   }

   /**
    * Switchs the main flow stepping on or off
    * @param routine root of the execution flow
    * @param enable true for on and false for off
    * @return returns true if the main flow stepping is successfully switched
    */
   public boolean setMainFlowStepping (TcExecutionUnit routine, boolean enable) {
      try {
			int major = client.getMajorVersion();
			if (3 <= major) {
				synchronized (rpcTcSetExeFlagIn) {
					rpcTcSetExeFlagIn.exeUnitHnd = ((TcRpcExecutionUnit) routine).getHandle();
					rpcTcSetExeFlagIn.exeFlag.value = EXECUTION_MODE_MAIN_FLOW_STEPPING;
					rpcTcSetExeFlagIn.value = enable;
               client.client.RpcTcSetExeFlag_1(rpcTcSetExeFlagIn, rpcTcSetExeFlagOut);
               return rpcTcSetExeFlagOut.retVal;
				}
			}
         synchronized (rpcTcSetDebugModeIn) {
            rpcTcSetDebugModeIn.exeUnitHnd = ((TcRpcExecutionUnit) routine).getHandle();
            rpcTcSetDebugModeIn.exeFlags.value = enable ? DEBUG_MODE_MAIN_FLOW: TcExecutionState.EXECUTION_MODE_FLOW;
            client.client.RpcTcSetDebugMode_1(rpcTcSetDebugModeIn, rpcTcSetDebugModeOut);
            return rpcTcSetDebugModeOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - setStepMode: ");
      }
      return false;
   }

   /**
    * Liefert den Zustand der Ausführungseinheit.
    *
    * @param execUnit Ausführungseinheit
    * @param s Zustandsobjekt, welches nach dem Aufruf die Informationen enthält
    *
    * @return true für das erfolgreiche Lesen
    */
   public boolean getState (TcExecutionUnit execUnit, TcExecutionState s) {
      try {
         synchronized (rpcTcGetExeUnitStatusIn) {
            rpcTcGetExeUnitStatusIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            client.client.RpcTcGetExeUnitStatus_1(rpcTcGetExeUnitStatusIn, rpcTcGetExeUnitStatusOut);
            if (rpcTcGetExeUnitStatusOut.retVal) {
               s.changeCount = rpcTcGetExeUnitStatusOut.status.changeCnt;
               s.childCount = rpcTcGetExeUnitStatusOut.status.nrChilds;
               s.executionState = rpcTcGetExeUnitStatusOut.status.state.value;
               s.steppingState = rpcTcGetExeUnitStatusOut.status.step.value;
               s.executionMode = rpcTcGetExeUnitStatusOut.status.exeFlags.value & 0xffff;
               if (s.executionMode == DEBUG_MODE_MAIN_FLOW) {
               	s.executionMode = TcExecutionState.EXECUTION_MODE_FLOW;
               }
               s.isMainFlowStepping = (rpcTcGetExeUnitStatusOut.status.exeFlags.value == DEBUG_MODE_MAIN_FLOW)
               								|| ((rpcTcGetExeUnitStatusOut.status.exeFlags.value & 0x10000) == EXECUTION_MODE_MAIN_FLOW_STEPPING);
               
               if ((s.executionState != TcExecutionState.STATE_INVALID)
                   && ((execUnit.getKind() == TcExecutionUnit.PROJECT)
                       || (execUnit.getKind() == TcExecutionUnit.GLOBAL)
                       || (execUnit.getKind() == TcExecutionUnit.SYSTEM))) {
               	int major = client.getMajorVersion();
               	int minor = client.getMinorVersion();
               	if ((2 < major) || ((2 == major) && (84 <= minor))) {
                  int state = rpcTcGetExeUnitStatusOut.status.lineOrStatus;
                  if (state == 0) {
                     s.executionState = TcExecutionState.STATE_PROJECT_LOADING;
                  } else if (state == 1) {
                     s.executionState = TcExecutionState.STATE_PROJECT_UNLOADING;                        
                  }
               	}
                  s.line = 0;
               } else {
                  s.line = rpcTcGetExeUnitStatusOut.status.lineOrStatus;
               }
               s.mainFlowLine = rpcTcGetExeUnitStatusOut.status.mainFlowLine;
               return true;
            }
            s.executionState = TcExecutionState.STATE_INVALID;
            return true;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionUnit - getState: ");
      }
      return false;
   }

   /**
    * Führt ein oder mehrer Anweisungen mit der angegebenen Schrittart aus.
    *
    * @param execUnits Ausführungseinheiten
    * @param stepKind Schrittart
    *
    * @return true für den erfolgreichen Aufruf
    */
   public boolean step (Vector execUnits, int stepKind) {
      try {
         synchronized (rpcTcStepListIn) {
            rpcTcStepListIn.stepCmd.value = stepKind;
            int index = 0;
            while (index < execUnits.size()) {
               int end = index + rpcChunkLen.value;
               if (end >= execUnits.size()) {
                  end = execUnits.size();
               }
               for (int i = index; i < end; i++) {
                  TcRpcExecutionUnit oeu = (TcRpcExecutionUnit) execUnits.elementAt(i);
                  rpcTcStepListIn.exeUnitHnd[i - index] = oeu.getHandle();
               }
               rpcTcStepListIn.nrOfExeUnitHnd = end - index;
               rpcTcStepListIn.exeUnitHnd_count = end - index;
               client.client.RpcTcStepList_1(rpcTcStepListIn, rpcTcStepListOut);
               if (!rpcTcStepListOut.retVal) {
                  return false;
               }
               index = index + rpcChunkLen.value;
            }
            return true;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - step: ");
      }
      return false;
   }

   /**
    * Liefert die Zustände der Ausführungseinheiten.
    *
    * @param execUnits Ausführungseinheiten
    * @param states Zustandsobjekte
    *
    * @return true für den erfolgreichen Aufruf
    */
   public boolean getStates (Vector execUnits, Vector states) {
      try {
         synchronized (rpcTcGetExeUnitStatusListIn) {
            int index = 0;
            while (index < execUnits.size()) {
               int end = index + rpcChunkLen.value;
               if (end >= execUnits.size()) {
                  end = execUnits.size();
               }
               for (int i = index; i < end; i++) {
                  TcRpcExecutionUnit oeu = (TcRpcExecutionUnit) execUnits.elementAt(i);
                  rpcTcGetExeUnitStatusListIn.exeUnitHnd[i - index] = oeu.getHandle();
               }
               rpcTcGetExeUnitStatusListIn.nrOfExeUnitHnd = end - index;
               rpcTcGetExeUnitStatusListIn.exeUnitHnd_count = end - index;
               client.client.RpcTcGetExeUnitStatusList_1(
                                                  rpcTcGetExeUnitStatusListIn,
                                                  rpcTcGetExeUnitStatusListOut);
               if (rpcTcGetExeUnitStatusListOut.retVal) {
                  for (int i = index; i < end; i++) {
                  	TcExecutionState s = (TcExecutionState) states.elementAt(i);
                     TcExecutionUnit execUnit = (TcExecutionUnit) execUnits.elementAt(i);
                     s.changeCount = rpcTcGetExeUnitStatusListOut.status[i - index].changeCnt;
                     s.childCount = rpcTcGetExeUnitStatusListOut.status[i - index].nrChilds;
                     s.executionState = rpcTcGetExeUnitStatusListOut.status[i - index].state.value;
                     s.steppingState = rpcTcGetExeUnitStatusListOut.status[i - index].step.value;
                     s.executionMode = rpcTcGetExeUnitStatusListOut.status[i - index].exeFlags.value & 0xffff;
                     if (s.executionMode == DEBUG_MODE_MAIN_FLOW) {
                     	s.executionMode = TcExecutionState.EXECUTION_MODE_FLOW;
                     }
                     s.isMainFlowStepping = (rpcTcGetExeUnitStatusListOut.status[i - index].exeFlags.value == DEBUG_MODE_MAIN_FLOW)
                     								|| ((rpcTcGetExeUnitStatusListOut.status[i - index].exeFlags.value & 0x10000) == EXECUTION_MODE_MAIN_FLOW_STEPPING);
                     if ((s.executionState != TcExecutionState.STATE_INVALID)
                         && ((execUnit.getKind() == TcExecutionUnit.PROJECT)
                            || (execUnit.getKind() == TcExecutionUnit.GLOBAL)
                            || (execUnit.getKind() == TcExecutionUnit.SYSTEM))) {
                     	int major = client.getMajorVersion();
                     	int minor = client.getMinorVersion();
                     	if ((2 < major) || ((2 == major) && (84 <= minor))) {
                       int state = rpcTcGetExeUnitStatusListOut.status[i - index].lineOrStatus;
                       if (state == 0) {
                          s.executionState = TcExecutionState.STATE_PROJECT_LOADING;
                       } else if (state == 1) {
                          s.executionState = TcExecutionState.STATE_PROJECT_UNLOADING;                        
                       }
                     	}
                       s.line = 0;
                    } else {
                       s.line = rpcTcGetExeUnitStatusListOut.status[i - index].lineOrStatus;
                    }
                     s.mainFlowLine =
                        rpcTcGetExeUnitStatusListOut.status[i - index].mainFlowLine;
                  }
               } else {
                  return false;
               }
               index = index + rpcChunkLen.value;
            }
            return true;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionUnit - getStates: ");
      }
      return false;
   }

   /**
    * Setzt den Ausführungszeiger auf die angegebene Zeilenposition.
    *
    * @param execUnit Ausführungseinheit
    * @param line Zeilenposition
    *
    * @return true für das erfolgreiche Setzen
    */
   public boolean setInstructionPointer (TcExecutionUnit execUnit, int line) {
      try {
         synchronized (rpcTcSetInstructionPointerIn) {
            rpcTcSetInstructionPointerIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            rpcTcSetInstructionPointerIn.lineNr = line;
            client.client.RpcTcSetInstructionPointer_1(
                                                rpcTcSetInstructionPointerIn,
                                                rpcTcSetInstructionPointerOut);
            return rpcTcSetInstructionPointerOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - setInstructionPointer: ");
      }
      return false;
   }

   /**
    * Führt ein oder mehrer Anweisungen mit der angegebenen Schrittart aus.
    *
    * @param execUnit Ausführungseinheit
    * @param stepKind Schrittart
    *
    * @return true für den erfolgreichen Aufruf
    */
   public boolean step (TcExecutionUnit execUnit, int stepKind) {
      try {
         synchronized (rpcTcStepIn) {
            rpcTcStepIn.exeUnitHnd = ((TcRpcExecutionUnit) execUnit).getHandle();
            rpcTcStepIn.stepCmd.value = stepKind;
            client.client.RpcTcStep_1(rpcTcStepIn, rpcTcStepOut);
            return rpcTcStepOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - step: ");
      }
      return false;
   }
   /**
    * Executes the given routine. 
    *
    * @param routine routine which should be executed
    * @param instanceAccessHandle variable instance or null if the routine is program routine
    * @param parameter parameter of the routine (Access Handle, Boolean, Byte, Short, Integer, Long, Float or String)
    *
    * @return returns the routine return value if it was successfully executed otherwise null.
    * If the routine does not have a return value a Object will be retruned
    */
   public Object executeRoutine (TcStructuralRoutineNode routine,
                                         TcAccessHandle instanceAccessHandle,
                                         Object[] parameter) {
      try {
         synchronized (rpcTcExecuteMethodExtIn) {
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
         	rpcTcExecuteMethodExtIn.routineHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
         	rpcTcExecuteMethodExtIn.instanceVarAccess = (instanceAccessHandle != null) ?
         			((AccessHandle) instanceAccessHandle).access: new RpcTcVarAccess();
         	rpcTcExecuteMethodExtIn.params_count = parameter.length;
         	for (int i = 0; i < parameter.length; i++) {
         		rpcTcExecuteMethodExtIn.params[i].isValue = !(parameter[i] instanceof TcAccessHandle);
         		if (rpcTcExecuteMethodExtIn.params[i].isValue) {
         			if (parameter[i] instanceof Boolean) {
         				rpcTcExecuteMethodExtIn.params[i].value.bValue = ((Boolean) parameter[i]).booleanValue();
         			} else if (parameter[i] instanceof Byte) {
         				rpcTcExecuteMethodExtIn.params[i].value.i8Value = ((Byte) parameter[i]).byteValue();
         			} else if (parameter[i] instanceof Short) {
         				rpcTcExecuteMethodExtIn.params[i].value.i16Value = ((Short) parameter[i]).shortValue();
         			} else if (parameter[i] instanceof Integer) {
         				rpcTcExecuteMethodExtIn.params[i].value.i32Value = ((Integer) parameter[i]).intValue();
         			} else if (parameter[i] instanceof Long) {
         				rpcTcExecuteMethodExtIn.params[i].value.i64Value = ((Long) parameter[i]).longValue();
         			} else if (parameter[i] instanceof Float) {
         				rpcTcExecuteMethodExtIn.params[i].value.fValue = ((Float) parameter[i]).floatValue();
         			} else if (parameter[i] instanceof String) {
         				rpcTcExecuteMethodExtIn.params[i].value.sValue = (String) parameter[i];
         			}
         		} else {
         			rpcTcExecuteMethodExtIn.params[i].varAccess = ((AccessHandle) parameter[i]).access;
         		}
         	}
            client.client.RpcTcExecuteMethodExt_1(rpcTcExecuteMethodExtIn, rpcTcExecuteMethodExtOut);
   			if ((major < 3) && (executionMode != TcExecutionState.EXECUTION_MODE_OFF)) {
   				if (isMainFlowStepping) {
   					setMainFlowStepping(getRoot(), true);
   				} else {
   					setExecutionMode(getRoot(), executionMode);
   				}
   			}
            if (rpcTcExecuteMethodExtOut.retVal) {
            	if (rpcTcExecuteMethodExtOut.methodRetVal.isValid) {
               	TcStructuralTypeNode t = routine.getReturnType();
               	if (t != null) {
               		switch (t.getTypeKind()) {
               		case TcStructuralTypeNode.BOOL_TYPE:
               			return (rpcTcExecuteMethodExtOut.methodRetVal.bValue) ? Boolean.TRUE: Boolean.FALSE;
               		case TcStructuralTypeNode.SINT_TYPE:
               			return new Byte((byte) rpcTcExecuteMethodExtOut.methodRetVal.i8Value);
               		case TcStructuralTypeNode.INT_TYPE:
               			return new Short((short) rpcTcExecuteMethodExtOut.methodRetVal.i16Value);
               		case TcStructuralTypeNode.DINT_TYPE:
               			return new Integer(rpcTcExecuteMethodExtOut.methodRetVal.i32Value);
               		case TcStructuralTypeNode.LINT_TYPE:
               			return new Long(rpcTcExecuteMethodExtOut.methodRetVal.i64Value);
               		case TcStructuralTypeNode.REAL_TYPE:
               			return new Float(rpcTcExecuteMethodExtOut.methodRetVal.fValue);
               		case TcStructuralTypeNode.STRING_TYPE:
               			return rpcTcExecuteMethodExtOut.methodRetVal.sValue;
               		}
               	}
               } else {
               	return new Object();
               }
            }
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - executeRoutine: ");
      }
      return null;
   }

   /**
    * Liest und setzt die Ausführungseinheiteninformation (z.B. Art, Instanzpfad, 
    * Strukturbaumknoten, ...) 
    *
    * @param execUnits Ausführungseinheiten
    *
    * @return true für den erfolgreichen Aufruf
    */
   public boolean setExecUnitInfos (Vector execUnits) {
      try {
         synchronized (rpcTcGetExeUnitInfoListIn) {
            int index = 0;
            while (index < execUnits.size()) {
               int end = index + rpcChunkLen.value;
               if (end >= execUnits.size()) {
                  end = execUnits.size();
               }
               for (int i = index; i < end; i++) {
                  TcRpcExecutionUnit oeu = (TcRpcExecutionUnit) execUnits.elementAt(i);
                  rpcTcGetExeUnitInfoListIn.exeUnitHnd[i - index] = oeu.getHandle();
               }
               rpcTcGetExeUnitInfoListIn.nrOfExeUnitHnd = end - index;
               rpcTcGetExeUnitInfoListIn.exeUnitHnd_count = end - index;
               client.client.RpcTcGetExeUnitInfoList_1(
                                                rpcTcGetExeUnitInfoListIn,
                                                rpcTcGetExeUnitInfoListOut);
               if (rpcTcGetExeUnitInfoListOut.retVal) {
                  for (int i = index; i < end; i++) {
                     TcRpcExecutionUnit oeu = (TcRpcExecutionUnit) execUnits.elementAt(i);
                     oeu.setInfo(rpcTcGetExeUnitInfoListOut.info[i - index]);
                  }
               } else {
                  return false;
               }
               index = index + rpcChunkLen.value;
            }
            return true;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcExecutionModel - getInfoElems: ");
      }
      return false;
   }


   private int convertKind (int kind) {
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
   
   /* (non-Javadoc)
 * @see com.keba.kemro.teach.network.TcExecutionModel#readVarArray(com.keba.kemro.teach.network.TcExecutionUnit, com.keba.kemro.teach.network.TcStructuralVarNode, com.keba.kemro.teach.network.TcAccessHandle, com.keba.kemro.teach.network.TcStructuralTypeNode)
 */
public Object[] readVarArray(TcExecutionUnit _execUnit, TcStructuralVarNode _tcArrayVarNode, TcAccessHandle _varAccess, TcStructuralTypeNode _elemType) {
	return null;
}

/* (non-Javadoc)
 * @see com.keba.kemro.teach.network.TcExecutionModel#getTcMemdump(com.keba.kemro.teach.network.TcStructuralNode, com.keba.kemro.teach.network.TcStructuralNode, com.keba.kemro.teach.network.TcAccessHandle, com.keba.kemro.teach.network.TcExecutionUnit)
 */
public int[] getTcMemdump(TcStructuralNode _structTypeNode, TcStructuralNode _structVarNode, TcAccessHandle _accessHandle, TcExecutionUnit _execUnit) throws NullPointerException {
	return null;
}

/* (non-Javadoc)
 * @see com.keba.kemro.teach.network.TcExecutionModel#getTcMemdump(int, int, com.keba.kemro.teach.network.TcAccessHandle)
 */
public int[] getTcMemdump(int _memoffset, int _buffersize, TcAccessHandle _accessHandle) {
	return null;
}

private class ExecutionUnitChunkEnumeration implements Enumeration {
      private int kind;
      private TcRpcExecutionUnit parent;
      private boolean loadInfos;
      private int nrOfHnd;
      private int iterHandle;
      private final TcRpcExecutionUnit[] elems = new TcRpcExecutionUnit[rpcChunkLen.value];
      private int index;
      private boolean isFirst = true;
      private boolean isValid = false;

      private ExecutionUnitChunkEnumeration (TcRpcExecutionUnit parent, int kind, boolean loadInfos) {
         this.parent = parent;
         this.kind = convertKind(kind);
         this.loadInfos = loadInfos;
      }

      /**
       * @see java.util.Enumeration#hasMoreElements()
       */
      public boolean hasMoreElements () {
         if (!isValid) {
            if (index >= nrOfHnd) {
               if (isFirst) {
                  isFirst = false;
                  getFirstChunk();
               } else if (nrOfHnd == rpcChunkLen.value) {
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
      public Object nextElement () {
         if (hasMoreElements()) {
            isValid = false;
            TcExecutionUnit elem = elems[index];
            index++;
            return elem;
         }
         return null;
      }

      private boolean loadInfo (int[] execUnitHnds, int nrOfHandles)
                           throws IOException, RPCException {
         synchronized (rpcTcGetExeUnitInfoListIn) {
            rpcTcGetExeUnitInfoListIn.nrOfExeUnitHnd = nrOfHandles;
            System.arraycopy(execUnitHnds, 0, rpcTcGetExeUnitInfoListIn.exeUnitHnd, 0, nrOfHandles);
            rpcTcGetExeUnitInfoListIn.exeUnitHnd_count = nrOfHandles;
            client.client.RpcTcGetExeUnitInfoList_1(rpcTcGetExeUnitInfoListIn, rpcTcGetExeUnitInfoListOut);
            if (rpcTcGetExeUnitInfoListOut.retVal) {
               for (int i = 0; i < nrOfHandles; i++) {
                  elems[i].setInfo(rpcTcGetExeUnitInfoListOut.info[i]);
               }
               return true;
            }
            return false;
         }
      }

      private void getFirstChunk () {
         try {
            synchronized (rpcTcGetFirstExeUnitChunkIn) {
               rpcTcGetFirstExeUnitChunkIn.exeUnitHnd = parent.getHandle();
               rpcTcGetFirstExeUnitChunkIn.kind.value = kind;
               client.client.RpcTcGetFirstExeUnitChunk_1(
                                                  rpcTcGetFirstExeUnitChunkIn,
                                                  rpcTcGetFirstExeUnitChunkOut);
               if (rpcTcGetFirstExeUnitChunkOut.retVal) {
                  index = 0;
                  nrOfHnd = rpcTcGetFirstExeUnitChunkOut.nrOfExeUnitHnd;
                  iterHandle = rpcTcGetFirstExeUnitChunkOut.iterHnd;
                  if (0 < nrOfHnd) {
                     for (int i = 0; i < nrOfHnd; i++) {
                        elems[i] =
                           new TcRpcExecutionUnit(rpcTcGetFirstExeUnitChunkOut.exeUnitHnd[i], client);
                        elems[i].setParent(parent);
                     }
                     if (loadInfos) {
                        if (loadInfo(rpcTcGetFirstExeUnitChunkOut.exeUnitHnd, nrOfHnd)) {
                           return;
                        }
                     } else {
                        return;
                     }
                  }
               }
            }
         } catch (Exception e) {
            System.out.println("Disconnect in TcExecutionModel - ExecutionUnitChunkEnumeration - getFirstChunk: ");
            }
         nrOfHnd = 0;
         for (int i = 0; i < elems.length; i++) {
            elems[i] = null;
         }
      }

      private void getNextChunk () {
         try {
            synchronized (rpcTcGetNextExeUnitChunkIn) {
               rpcTcGetNextExeUnitChunkIn.iterHnd = iterHandle;
               rpcTcGetNextExeUnitChunkIn.exeUnitHnd = parent.getHandle();
               rpcTcGetNextExeUnitChunkIn.kind.value = kind;
               client.client.RpcTcGetNextExeUnitChunk_1(
                                                 rpcTcGetNextExeUnitChunkIn,
                                                 rpcTcGetNextExeUnitChunkOut);
               if (rpcTcGetNextExeUnitChunkOut.retVal) {
                  index = 0;
                  nrOfHnd = rpcTcGetNextExeUnitChunkOut.nrOfExeUnitHnd;
                  iterHandle = rpcTcGetNextExeUnitChunkOut.iterHnd;
                  if (0 < nrOfHnd) {
                     for (int i = 0; i < nrOfHnd; i++) {
                        elems[i] =
                           new TcRpcExecutionUnit(rpcTcGetNextExeUnitChunkOut.exeUnitHnd[i], client);
                        elems[i].setParent(parent);
                     }
                     if (loadInfos) {
                        if (loadInfo(rpcTcGetNextExeUnitChunkOut.exeUnitHnd, nrOfHnd)) {
                           return;
                        }
                     } else {
                        return;
                     }
                  }
               }
            }
         } catch (Exception e) {
            System.out.println("Disconnect in TcExecutionModel - ExecutionUnitChunkEnumeration - getNextChunk: ");
         }
         nrOfHnd = 0;
         for (int i = 0; i < elems.length; i++) {
            elems[i] = null;
         }
      }
   }
   
   private class MultiExecutionUnitChunkEnumeration implements Enumeration {
      private Vector parents;
      private int parentIndex;
      private int kind;
      boolean loadInfos;
      private int nrOfHnd;
      private int iterHandle;
      private final TcRpcExecutionUnit[] elems = new TcRpcExecutionUnit[rpcChunkLen.value];
      private int index;
      private boolean isValid = false;

      private MultiExecutionUnitChunkEnumeration (Vector parents, int kind, boolean loadInfos) {
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
      public boolean hasMoreElements () {
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
      public Object nextElement () {
         if (hasMoreElements()) {
            isValid = false;
            TcRpcExecutionUnit elem = elems[index];
            index++;
            return elem;
         }
         return null;
      }

      private boolean loadInfo (int[] execUnitHnds, int nrOfHandles)
                           throws RPCException, IOException {
         synchronized (rpcTcGetExeUnitInfoListIn) {
            rpcTcGetExeUnitInfoListIn.nrOfExeUnitHnd = nrOfHandles;
            System.arraycopy(execUnitHnds, 0, rpcTcGetExeUnitInfoListIn.exeUnitHnd, 0, nrOfHandles);
            rpcTcGetExeUnitInfoListIn.exeUnitHnd_count = nrOfHandles;
            client.client.RpcTcGetExeUnitInfoList_1(rpcTcGetExeUnitInfoListIn, rpcTcGetExeUnitInfoListOut);
            if (rpcTcGetExeUnitInfoListOut.retVal) {
               for (int i = 0; i < nrOfHandles; i++) {
                  elems[i].setInfo(rpcTcGetExeUnitInfoListOut.info[i]);
               }
               return true;
            }
            return false;
         }
      }

      private void getChunk () {
         try {
            synchronized (rpcTcGetMultExeUnitChunkIn) {
               int n = parents.size() - parentIndex;
               if (rpcChunkLen.value < n) {
                  n = rpcChunkLen.value;
               }
               rpcTcGetMultExeUnitChunkIn.exeUnitHnd_count = n;
               int j = 0;
               for (int i = parentIndex; i < (parentIndex + n); i++) {
                  rpcTcGetMultExeUnitChunkIn.exeUnitHnd[j] =
                     ((TcRpcExecutionUnit) parents.elementAt(i)).getHandle();
                  j++;
               }
               rpcTcGetMultExeUnitChunkIn.kind.value = kind;
               rpcTcGetMultExeUnitChunkIn.iterHnd = iterHandle;
               client.client.RpcTcGetMultExeUnitChunk_1(
                                                 rpcTcGetMultExeUnitChunkIn,
                                                 rpcTcGetMultExeUnitChunkOut);
               if (rpcTcGetMultExeUnitChunkOut.retVal) {
                  index = 0;
                  nrOfHnd = rpcTcGetMultExeUnitChunkOut.nrOfExeUnitHnd;
                  iterHandle = rpcTcGetMultExeUnitChunkOut.iterHnd;
                  if (0 < nrOfHnd) {
                     TcRpcExecutionUnit lastParent =
                        (TcRpcExecutionUnit) parents.elementAt(parentIndex);
                     int lastParentHandle = lastParent.getHandle();
                     for (int i = 0; i < nrOfHnd; i++) {
                        elems[i] =
                           new TcRpcExecutionUnit(rpcTcGetMultExeUnitChunkOut.exeUnitHnd[i], client);
                        if (lastParentHandle == rpcTcGetMultExeUnitChunkOut.upperExeUnitHnd[i]) {
                           elems[i].setParent(lastParent);
                        } else {
                           // looking for parent
                           do {
                              parentIndex++;
                              lastParent = (TcRpcExecutionUnit) parents.elementAt(parentIndex);
                              lastParentHandle = lastParent.getHandle();
                           } while (
                                    lastParentHandle != rpcTcGetMultExeUnitChunkOut.upperExeUnitHnd[i]);
                           elems[i].setParent(lastParent);
                        }
                     }
                     if (loadInfos) {
                        if (loadInfo(rpcTcGetMultExeUnitChunkOut.exeUnitHnd, nrOfHnd)) {
                           return;
                        }
                     } else {
                        return;
                     }
                  }
               }
            }
         } catch (Exception e) {
            System.out.println("Disconnect in TcExecutionModel - ExecutionUnitChunkEnumeration - getChunk: ");
         }
         nrOfHnd = 0;
         for (int i = 0; i < elems.length; i++) {
            elems[i] = null;
         }
      }
   }
   
   private static class VariableGroup extends TcVariableGroup {
   	
   	private VariableGroup (String name, int handle) {
   		super(name, handle);
      }
   	
   	private int getHandle () {
   		return handle;
   	}
   }

}
