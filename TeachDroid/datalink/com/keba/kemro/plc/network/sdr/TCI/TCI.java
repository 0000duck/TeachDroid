package com.keba.kemro.plc.network.sdr.TCI;

import com.keba.jsdr.sdr.*;
import java.io.*;

public class TCI extends SDRClient {

   public TCI(SdrConnection connection) {
      super(connection);
   }

   public static final int FALSE = 0;
   public static final int TRUE = 1;
   public static final int rpcMaxPathLen = 255;
   public static final int rpcMaxNameLen = 32;
   public static final int rpcChunkLen = 50;
   public static final int cNrErrorParams = 8;
   public static final int rpcMaxInstancePathElems = 30;
   public static final int cMaxOffsets = 29;
   public static final int cMaxErrors = 20;
   public static final int rpcMaxAttrLen = 512;
   public static final int rpcMaxParams = 16;
   public static final int rpcMaxCodePoints = 20;
   public static final int rpcMaxWatchVars = 48;
   //------------------------------------------------------------------------------
   //  RpcTcOpenTeachControl
   //
   public RpcTcOpenTeachControlOut RpcTcOpenTeachControl(RpcTcOpenTeachControlIn arg, RpcTcOpenTeachControlOut retVal) throws SDRException, IOException {

      call(25001, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcCloseTeachControl
   //
   public RpcTcCloseTeachControlOut RpcTcCloseTeachControl(RpcTcCloseTeachControlIn arg, RpcTcCloseTeachControlOut retVal) throws SDRException, IOException {

      call(25002, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReadError
   //
   public RpcTcReadErrorOut RpcTcReadError(RpcTcReadErrorIn arg, RpcTcReadErrorOut retVal) throws SDRException, IOException {

      call(25003, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReadProjectPath
   //
   public RpcTcReadProjectPathOut RpcTcReadProjectPath(RpcTcReadProjectPathIn arg, RpcTcReadProjectPathOut retVal) throws SDRException, IOException {

      call(25004, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetClientList
   //
   public RpcTcGetClientListOut RpcTcGetClientList(RpcTcGetClientListIn arg, RpcTcGetClientListOut retVal) throws SDRException, IOException {

      call(25005, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetClientName
   //
   public RpcTcSetClientNameOut RpcTcSetClientName(RpcTcSetClientNameIn arg, RpcTcSetClientNameOut retVal) throws SDRException, IOException {

      call(25006, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetClientType
   //
   public RpcTcSetClientTypeOut RpcTcSetClientType(RpcTcSetClientTypeIn arg, RpcTcSetClientTypeOut retVal) throws SDRException, IOException {

      call(25007, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcKeepAlive
   //
   public RpcTcKeepAliveOut RpcTcKeepAlive(RpcTcKeepAliveIn arg, RpcTcKeepAliveOut retVal) throws SDRException, IOException {

      call(25008, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRequestWriteAccess
   //
   public RpcTcRequestWriteAccessOut RpcTcRequestWriteAccess(RpcTcRequestWriteAccessIn arg, RpcTcRequestWriteAccessOut retVal) throws SDRException, IOException {

      call(25009, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcWriteAccessRequestPending
   //
   public RpcTcWriteAccessRequestPendingOut RpcTcWriteAccessRequestPending(RpcTcWriteAccessRequestPendingIn arg, RpcTcWriteAccessRequestPendingOut retVal) throws SDRException, IOException {

      call(25010, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcConvertExeUnitHandle
   //
   public RpcTcConvertExeUnitHandleOut RpcTcConvertExeUnitHandle(RpcTcConvertExeUnitHandleIn arg, RpcTcConvertExeUnitHandleOut retVal) throws SDRException, IOException {

      call(25103, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcConvertDirEntryPath
   //
   public RpcTcConvertDirEntryPathOut RpcTcConvertDirEntryPath(RpcTcConvertDirEntryPathIn arg, RpcTcConvertDirEntryPathOut retVal) throws SDRException, IOException {

      call(25104, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcScopeHdlToDirEntryPath
   //
   public RpcScopeHdlToDirEntryPathOut RpcScopeHdlToDirEntryPath(RpcScopeHdlToDirEntryPathIn arg, RpcScopeHdlToDirEntryPathOut retVal) throws SDRException, IOException {

      call(25105, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcBuild
   //
   public RpcTcBuildOut RpcTcBuild(RpcTcBuildIn arg, RpcTcBuildOut retVal) throws SDRException, IOException {

      call(25201, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcDestroy
   //
   public RpcTcDestroyOut RpcTcDestroy(RpcTcDestroyIn arg, RpcTcDestroyOut retVal) throws SDRException, IOException {

      call(25202, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetErrors
   //
   public RpcTcGetErrorsOut RpcTcGetErrors(RpcTcGetErrorsIn arg, RpcTcGetErrorsOut retVal) throws SDRException, IOException {

      call(25203, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcBuildStart
   //
   public RpcTcBuildStartOut RpcTcBuildStart(RpcTcBuildStartIn arg, RpcTcBuildStartOut retVal) throws SDRException, IOException {

      call(25204, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReadNodeChange
   //
   public RpcTcReadNodeChangeOut RpcTcReadNodeChange(RpcTcReadNodeChangeIn arg, RpcTcReadNodeChangeOut retVal) throws SDRException, IOException {

      call(25205, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcIsCurrent
   //
   public RpcTcIsCurrentOut RpcTcIsCurrent(RpcTcIsCurrentIn arg, RpcTcIsCurrentOut retVal) throws SDRException, IOException {

      call(25206, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcOpenNode
   //
   public RpcTcOpenNodeOut RpcTcOpenNode(RpcTcOpenNodeIn arg, RpcTcOpenNodeOut retVal) throws SDRException, IOException {

      call(25210, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstNode
   //
   public RpcTcGetFirstNodeOut RpcTcGetFirstNode(RpcTcGetFirstNodeIn arg, RpcTcGetFirstNodeOut retVal) throws SDRException, IOException {

      call(25211, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextNode
   //
   public RpcTcGetNextNodeOut RpcTcGetNextNode(RpcTcGetNextNodeIn arg, RpcTcGetNextNodeOut retVal) throws SDRException, IOException {

      call(25212, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddProgramNode
   //
   public RpcTcAddProgramNodeOut RpcTcAddProgramNode(RpcTcAddProgramNodeIn arg, RpcTcAddProgramNodeOut retVal) throws SDRException, IOException {

      call(25220, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddRoutineNode
   //
   public RpcTcAddRoutineNodeOut RpcTcAddRoutineNode(RpcTcAddRoutineNodeIn arg, RpcTcAddRoutineNodeOut retVal) throws SDRException, IOException {

      call(25221, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddVarNode
   //
   public RpcTcAddVarNodeOut RpcTcAddVarNode(RpcTcAddVarNodeIn arg, RpcTcAddVarNodeOut retVal) throws SDRException, IOException {

      call(25222, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddTypeNode
   //
   public RpcTcAddTypeNodeOut RpcTcAddTypeNode(RpcTcAddTypeNodeIn arg, RpcTcAddTypeNodeOut retVal) throws SDRException, IOException {

      call(25223, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddConstNode
   //
   public RpcTcAddConstNodeOut RpcTcAddConstNode(RpcTcAddConstNodeIn arg, RpcTcAddConstNodeOut retVal) throws SDRException, IOException {

      call(25224, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRemoveNode
   //
   public RpcTcRemoveNodeOut RpcTcRemoveNode(RpcTcRemoveNodeIn arg, RpcTcRemoveNodeOut retVal) throws SDRException, IOException {

      call(25225, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRenameNode
   //
   public RpcTcRenameNodeOut RpcTcRenameNode(RpcTcRenameNodeIn arg, RpcTcRenameNodeOut retVal) throws SDRException, IOException {

      call(25226, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcWriteData
   //
   public RpcTcWriteDataOut RpcTcWriteData(RpcTcWriteDataIn arg, RpcTcWriteDataOut retVal) throws SDRException, IOException {

      call(25227, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcWriteDataExt
   //
   public RpcTcWriteDataExtOut RpcTcWriteDataExt(RpcTcWriteDataExtIn arg, RpcTcWriteDataExtOut retVal) throws SDRException, IOException {

      call(25228, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstNodeChunk
   //
   public RpcTcGetFirstNodeChunkOut RpcTcGetFirstNodeChunk(RpcTcGetFirstNodeChunkIn arg, RpcTcGetFirstNodeChunkOut retVal) throws SDRException, IOException {

      call(25231, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextNodeChunk
   //
   public RpcTcGetNextNodeChunkOut RpcTcGetNextNodeChunk(RpcTcGetNextNodeChunkIn arg, RpcTcGetNextNodeChunkOut retVal) throws SDRException, IOException {

      call(25232, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNodeInfoList
   //
   public RpcTcGetNodeInfoListOut RpcTcGetNodeInfoList(RpcTcGetNodeInfoListIn arg, RpcTcGetNodeInfoListOut retVal) throws SDRException, IOException {

      call(25233, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetRoutineInfoList
   //
   public RpcTcGetRoutineInfoListOut RpcTcGetRoutineInfoList(RpcTcGetRoutineInfoListIn arg, RpcTcGetRoutineInfoListOut retVal) throws SDRException, IOException {

      call(25234, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetVarInfoList
   //
   public RpcTcGetVarInfoListOut RpcTcGetVarInfoList(RpcTcGetVarInfoListIn arg, RpcTcGetVarInfoListOut retVal) throws SDRException, IOException {

      call(25235, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetTypeInfoList
   //
   public RpcTcGetTypeInfoListOut RpcTcGetTypeInfoList(RpcTcGetTypeInfoListIn arg, RpcTcGetTypeInfoListOut retVal) throws SDRException, IOException {

      call(25236, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetConstInfoList
   //
   public RpcTcGetConstInfoListOut RpcTcGetConstInfoList(RpcTcGetConstInfoListIn arg, RpcTcGetConstInfoListOut retVal) throws SDRException, IOException {

      call(25237, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetAttributes
   //
   public RpcTcGetAttributesOut RpcTcGetAttributes(RpcTcGetAttributesIn arg, RpcTcGetAttributesOut retVal) throws SDRException, IOException {

      call(25238, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetAttributes
   //
   public RpcTcSetAttributesOut RpcTcSetAttributes(RpcTcSetAttributesIn arg, RpcTcSetAttributesOut retVal) throws SDRException, IOException {

      call(25239, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcMoveVar
   //
   public RpcTcMoveVarOut RpcTcMoveVar(RpcTcMoveVarIn arg, RpcTcMoveVarOut retVal) throws SDRException, IOException {

      call(25241, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetActValue
   //
   public RpcTcSetActValueOut RpcTcSetActValue(RpcTcSetActValueIn arg, RpcTcSetActValueOut retVal) throws SDRException, IOException {

      call(25302, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetVarAccess
   //
   public RpcTcGetVarAccessOut RpcTcGetVarAccess(RpcTcGetVarAccessIn arg, RpcTcGetVarAccessOut retVal) throws SDRException, IOException {

      call(25304, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetStructElemAccess
   //
   public RpcTcGetStructElemAccessOut RpcTcGetStructElemAccess(RpcTcGetStructElemAccessIn arg, RpcTcGetStructElemAccessOut retVal) throws SDRException, IOException {

      call(25305, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetArrayElemAccess
   //
   public RpcTcGetArrayElemAccessOut RpcTcGetArrayElemAccess(RpcTcGetArrayElemAccessIn arg, RpcTcGetArrayElemAccessOut retVal) throws SDRException, IOException {

      call(25306, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcCreateInstance
   //
   public RpcTcCreateInstanceOut RpcTcCreateInstance(RpcTcCreateInstanceIn arg, RpcTcCreateInstanceOut retVal) throws SDRException, IOException {

      call(25307, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetMapTarget
   //
   public RpcTcGetMapTargetOut RpcTcGetMapTarget(RpcTcGetMapTargetIn arg, RpcTcGetMapTargetOut retVal) throws SDRException, IOException {

      call(25308, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetSaveValue
   //
   public RpcTcSetSaveValueOut RpcTcSetSaveValue(RpcTcSetSaveValueIn arg, RpcTcSetSaveValueOut retVal) throws SDRException, IOException {

      call(25310, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcWriteInitValue
   //
   public RpcTcWriteInitValueOut RpcTcWriteInitValue(RpcTcWriteInitValueIn arg, RpcTcWriteInitValueOut retVal) throws SDRException, IOException {

      call(25311, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcCopySaveValue
   //
   public RpcTcCopySaveValueOut RpcTcCopySaveValue(RpcTcCopySaveValueIn arg, RpcTcCopySaveValueOut retVal) throws SDRException, IOException {

      call(25312, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcCopyActValue
   //
   public RpcTcCopyActValueOut RpcTcCopyActValue(RpcTcCopyActValueIn arg, RpcTcCopyActValueOut retVal) throws SDRException, IOException {

      call(25313, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetMapTarget
   //
   public RpcTcSetMapTargetOut RpcTcSetMapTarget(RpcTcSetMapTargetIn arg, RpcTcSetMapTargetOut retVal) throws SDRException, IOException {

      call(25314, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetActValueList
   //
   public RpcTcGetActValueListOut RpcTcGetActValueList(RpcTcGetActValueListIn arg, RpcTcGetActValueListOut retVal) throws SDRException, IOException {

      call(25321, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcOpenVarAccessList
   //
   public RpcTcOpenVarAccessListOut RpcTcOpenVarAccessList(RpcTcOpenVarAccessListIn arg, RpcTcOpenVarAccessListOut retVal) throws SDRException, IOException {

      call(25322, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetSaveValueList
   //
   public RpcTcGetSaveValueListOut RpcTcGetSaveValueList(RpcTcGetSaveValueListIn arg, RpcTcGetSaveValueListOut retVal) throws SDRException, IOException {

      call(25323, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcNewVarGroup
   //
   public RpcTcNewVarGroupOut RpcTcNewVarGroup(RpcTcNewVarGroupIn arg, RpcTcNewVarGroupOut retVal) throws SDRException, IOException {

      call(25330, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRemoveVarGroup
   //
   public RpcTcRemoveVarGroupOut RpcTcRemoveVarGroup(RpcTcRemoveVarGroupIn arg, RpcTcRemoveVarGroupOut retVal) throws SDRException, IOException {

      call(25331, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddVarsToGroup
   //
   public RpcTcAddVarsToGroupOut RpcTcAddVarsToGroup(RpcTcAddVarsToGroupIn arg, RpcTcAddVarsToGroupOut retVal) throws SDRException, IOException {

      call(25332, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRemoveVarsFromGroup
   //
   public RpcTcRemoveVarsFromGroupOut RpcTcRemoveVarsFromGroup(RpcTcRemoveVarsFromGroupIn arg, RpcTcRemoveVarsFromGroupOut retVal) throws SDRException, IOException {

      call(25333, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReadVarGroupValues
   //
   public RpcTcReadVarGroupValuesOut RpcTcReadVarGroupValues(RpcTcReadVarGroupValuesIn arg, RpcTcReadVarGroupValuesOut retVal) throws SDRException, IOException {

      call(25334, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReadChangedVarGroupValues
   //
   public RpcTcReadChangedVarGroupValuesOut RpcTcReadChangedVarGroupValues(RpcTcReadChangedVarGroupValuesIn arg, RpcTcReadChangedVarGroupValuesOut retVal) throws SDRException, IOException {

      call(25335, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstDirEntry
   //
   public RpcTcGetFirstDirEntryOut RpcTcGetFirstDirEntry(RpcTcGetFirstDirEntryIn arg, RpcTcGetFirstDirEntryOut retVal) throws SDRException, IOException {

      call(25401, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextDirEntry
   //
   public RpcTcGetNextDirEntryOut RpcTcGetNextDirEntry(RpcTcGetNextDirEntryIn arg, RpcTcGetNextDirEntryOut retVal) throws SDRException, IOException {

      call(25402, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcAddDirEntry
   //
   public RpcTcAddDirEntryOut RpcTcAddDirEntry(RpcTcAddDirEntryIn arg, RpcTcAddDirEntryOut retVal) throws SDRException, IOException {

      call(25404, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcCopyDirEntry
   //
   public RpcTcCopyDirEntryOut RpcTcCopyDirEntry(RpcTcCopyDirEntryIn arg, RpcTcCopyDirEntryOut retVal) throws SDRException, IOException {

      call(25405, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRenameDirEntry
   //
   public RpcTcRenameDirEntryOut RpcTcRenameDirEntry(RpcTcRenameDirEntryIn arg, RpcTcRenameDirEntryOut retVal) throws SDRException, IOException {

      call(25406, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcDeleteDirEntry
   //
   public RpcTcDeleteDirEntryOut RpcTcDeleteDirEntry(RpcTcDeleteDirEntryIn arg, RpcTcDeleteDirEntryOut retVal) throws SDRException, IOException {

      call(25407, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcDeleteDirEntryExt
   //
   public RpcTcDeleteDirEntryExtOut RpcTcDeleteDirEntryExt(RpcTcDeleteDirEntryExtIn arg, RpcTcDeleteDirEntryExtOut retVal) throws SDRException, IOException {

      call(25408, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstDirEntryChunk
   //
   public RpcTcGetFirstDirEntryChunkOut RpcTcGetFirstDirEntryChunk(RpcTcGetFirstDirEntryChunkIn arg, RpcTcGetFirstDirEntryChunkOut retVal) throws SDRException, IOException {

      call(25420, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextDirEntryChunk
   //
   public RpcTcGetNextDirEntryChunkOut RpcTcGetNextDirEntryChunk(RpcTcGetNextDirEntryChunkIn arg, RpcTcGetNextDirEntryChunkOut retVal) throws SDRException, IOException {

      call(25422, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetDirEntryInfoList
   //
   public RpcTcGetDirEntryInfoListOut RpcTcGetDirEntryInfoList(RpcTcGetDirEntryInfoListIn arg, RpcTcGetDirEntryInfoListOut retVal) throws SDRException, IOException {

      call(25423, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcOpenSyntaxEditor
   //
   public RpcTcOpenSyntaxEditorOut RpcTcOpenSyntaxEditor(RpcTcOpenSyntaxEditorIn arg, RpcTcOpenSyntaxEditorOut retVal) throws SDRException, IOException {

      call(25501, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcCloseEditor
   //
   public RpcTcCloseEditorOut RpcTcCloseEditor(RpcTcCloseEditorIn arg, RpcTcCloseEditorOut retVal) throws SDRException, IOException {

      call(25502, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSaveEditor
   //
   public RpcTcSaveEditorOut RpcTcSaveEditor(RpcTcSaveEditorIn arg, RpcTcSaveEditorOut retVal) throws SDRException, IOException {

      call(25503, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstLine
   //
   public RpcTcGetFirstLineOut RpcTcGetFirstLine(RpcTcGetFirstLineIn arg, RpcTcGetFirstLineOut retVal) throws SDRException, IOException {

      call(25504, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextLine
   //
   public RpcTcGetNextLineOut RpcTcGetNextLine(RpcTcGetNextLineIn arg, RpcTcGetNextLineOut retVal) throws SDRException, IOException {

      call(25505, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetEditorInfo
   //
   public RpcTcGetEditorInfoOut RpcTcGetEditorInfo(RpcTcGetEditorInfoIn arg, RpcTcGetEditorInfoOut retVal) throws SDRException, IOException {

      call(25506, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcInsertText
   //
   public RpcTcInsertTextOut RpcTcInsertText(RpcTcInsertTextIn arg, RpcTcInsertTextOut retVal) throws SDRException, IOException {

      call(25507, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcDeleteText
   //
   public RpcTcDeleteTextOut RpcTcDeleteText(RpcTcDeleteTextIn arg, RpcTcDeleteTextOut retVal) throws SDRException, IOException {

      call(25508, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstSymbol
   //
   public RpcTcGetFirstSymbolOut RpcTcGetFirstSymbol(RpcTcGetFirstSymbolIn arg, RpcTcGetFirstSymbolOut retVal) throws SDRException, IOException {

      call(25512, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextSymbol
   //
   public RpcTcGetNextSymbolOut RpcTcGetNextSymbol(RpcTcGetNextSymbolIn arg, RpcTcGetNextSymbolOut retVal) throws SDRException, IOException {

      call(25513, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetSymbolTextRange
   //
   public RpcTcGetSymbolTextRangeOut RpcTcGetSymbolTextRange(RpcTcGetSymbolTextRangeIn arg, RpcTcGetSymbolTextRangeOut retVal) throws SDRException, IOException {

      call(25514, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNodeTextRange
   //
   public RpcTcGetNodeTextRangeOut RpcTcGetNodeTextRange(RpcTcGetNodeTextRangeIn arg, RpcTcGetNodeTextRangeOut retVal) throws SDRException, IOException {

      call(25515, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcDeleteSymbol
   //
   public RpcTcDeleteSymbolOut RpcTcDeleteSymbol(RpcTcDeleteSymbolIn arg, RpcTcDeleteSymbolOut retVal) throws SDRException, IOException {

      call(25520, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcInsertStatement
   //
   public RpcTcInsertStatementOut RpcTcInsertStatement(RpcTcInsertStatementIn arg, RpcTcInsertStatementOut retVal) throws SDRException, IOException {

      call(25521, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcDeleteStatement
   //
   public RpcTcDeleteStatementOut RpcTcDeleteStatement(RpcTcDeleteStatementIn arg, RpcTcDeleteStatementOut retVal) throws SDRException, IOException {

      call(25522, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcMoveStatement
   //
   public RpcTcMoveStatementOut RpcTcMoveStatement(RpcTcMoveStatementIn arg, RpcTcMoveStatementOut retVal) throws SDRException, IOException {

      call(25523, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReplaceStatement
   //
   public RpcTcReplaceStatementOut RpcTcReplaceStatement(RpcTcReplaceStatementIn arg, RpcTcReplaceStatementOut retVal) throws SDRException, IOException {

      call(25524, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcReplaceComment
   //
   public RpcTcReplaceCommentOut RpcTcReplaceComment(RpcTcReplaceCommentIn arg, RpcTcReplaceCommentOut retVal) throws SDRException, IOException {

      call(25525, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstSymbolChunk
   //
   public RpcTcGetFirstSymbolChunkOut RpcTcGetFirstSymbolChunk(RpcTcGetFirstSymbolChunkIn arg, RpcTcGetFirstSymbolChunkOut retVal) throws SDRException, IOException {

      call(25531, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextSymbolChunk
   //
   public RpcTcGetNextSymbolChunkOut RpcTcGetNextSymbolChunk(RpcTcGetNextSymbolChunkIn arg, RpcTcGetNextSymbolChunkOut retVal) throws SDRException, IOException {

      call(25532, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstLineChunk
   //
   public RpcTcGetFirstLineChunkOut RpcTcGetFirstLineChunk(RpcTcGetFirstLineChunkIn arg, RpcTcGetFirstLineChunkOut retVal) throws SDRException, IOException {

      call(25533, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextLineChunk
   //
   public RpcTcGetNextLineChunkOut RpcTcGetNextLineChunk(RpcTcGetNextLineChunkIn arg, RpcTcGetNextLineChunkOut retVal) throws SDRException, IOException {

      call(25534, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcOpenSyntaxEditorExt
   //
   public RpcTcOpenSyntaxEditorExtOut RpcTcOpenSyntaxEditorExt(RpcTcOpenSyntaxEditorExtIn arg, RpcTcOpenSyntaxEditorExtOut retVal) throws SDRException, IOException {

      call(25540, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcLoadGlobal
   //
   public RpcTcLoadGlobalOut RpcTcLoadGlobal(RpcTcLoadGlobalIn arg, RpcTcLoadGlobalOut retVal) throws SDRException, IOException {

      call(25601, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcUnloadGlobal
   //
   public RpcTcUnloadGlobalOut RpcTcUnloadGlobal(RpcTcUnloadGlobalIn arg, RpcTcUnloadGlobalOut retVal) throws SDRException, IOException {

      call(25602, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcLoadProj
   //
   public RpcTcLoadProjOut RpcTcLoadProj(RpcTcLoadProjIn arg, RpcTcLoadProjOut retVal) throws SDRException, IOException {

      call(25603, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcUnloadProj
   //
   public RpcTcUnloadProjOut RpcTcUnloadProj(RpcTcUnloadProjIn arg, RpcTcUnloadProjOut retVal) throws SDRException, IOException {

      call(25604, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcStartProg
   //
   public RpcTcStartProgOut RpcTcStartProg(RpcTcStartProgIn arg, RpcTcStartProgOut retVal) throws SDRException, IOException {

      call(25605, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcInterruptProg
   //
   public RpcTcInterruptProgOut RpcTcInterruptProg(RpcTcInterruptProgIn arg, RpcTcInterruptProgOut retVal) throws SDRException, IOException {

      call(25606, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcContinueProg
   //
   public RpcTcContinueProgOut RpcTcContinueProg(RpcTcContinueProgIn arg, RpcTcContinueProgOut retVal) throws SDRException, IOException {

      call(25607, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcStopProg
   //
   public RpcTcStopProgOut RpcTcStopProg(RpcTcStopProgIn arg, RpcTcStopProgOut retVal) throws SDRException, IOException {

      call(25608, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstExeUnit
   //
   public RpcTcGetFirstExeUnitOut RpcTcGetFirstExeUnit(RpcTcGetFirstExeUnitIn arg, RpcTcGetFirstExeUnitOut retVal) throws SDRException, IOException {

      call(25609, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextExeUnit
   //
   public RpcTcGetNextExeUnitOut RpcTcGetNextExeUnit(RpcTcGetNextExeUnitIn arg, RpcTcGetNextExeUnitOut retVal) throws SDRException, IOException {

      call(25610, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetExeUnitInfo
   //
   public RpcTcGetExeUnitInfoOut RpcTcGetExeUnitInfo(RpcTcGetExeUnitInfoIn arg, RpcTcGetExeUnitInfoOut retVal) throws SDRException, IOException {

      call(25611, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetExeUnitStatus
   //
   public RpcTcGetExeUnitStatusOut RpcTcGetExeUnitStatus(RpcTcGetExeUnitStatusIn arg, RpcTcGetExeUnitStatusOut retVal) throws SDRException, IOException {

      call(25612, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcExecuteMethod
   //
   public RpcTcExecuteMethodOut RpcTcExecuteMethod(RpcTcExecuteMethodIn arg, RpcTcExecuteMethodOut retVal) throws SDRException, IOException {

      call(25613, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcStartProgEx
   //
   public RpcTcStartProgExOut RpcTcStartProgEx(RpcTcStartProgExIn arg, RpcTcStartProgExOut retVal) throws SDRException, IOException {

      call(25614, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcExecuteMethodExt
   //
   public RpcTcExecuteMethodExtOut RpcTcExecuteMethodExt(RpcTcExecuteMethodExtIn arg, RpcTcExecuteMethodExtOut retVal) throws SDRException, IOException {

      call(25615, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetFirstExeUnitChunk
   //
   public RpcTcGetFirstExeUnitChunkOut RpcTcGetFirstExeUnitChunk(RpcTcGetFirstExeUnitChunkIn arg, RpcTcGetFirstExeUnitChunkOut retVal) throws SDRException, IOException {

      call(25621, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNextExeUnitChunk
   //
   public RpcTcGetNextExeUnitChunkOut RpcTcGetNextExeUnitChunk(RpcTcGetNextExeUnitChunkIn arg, RpcTcGetNextExeUnitChunkOut retVal) throws SDRException, IOException {

      call(25622, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetExeUnitInfoList
   //
   public RpcTcGetExeUnitInfoListOut RpcTcGetExeUnitInfoList(RpcTcGetExeUnitInfoListIn arg, RpcTcGetExeUnitInfoListOut retVal) throws SDRException, IOException {

      call(25623, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetExeUnitStatusList
   //
   public RpcTcGetExeUnitStatusListOut RpcTcGetExeUnitStatusList(RpcTcGetExeUnitStatusListIn arg, RpcTcGetExeUnitStatusListOut retVal) throws SDRException, IOException {

      call(25624, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetInstructionPointer
   //
   public RpcTcSetInstructionPointerOut RpcTcSetInstructionPointer(RpcTcSetInstructionPointerIn arg, RpcTcSetInstructionPointerOut retVal) throws SDRException, IOException {

      call(25625, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetMultExeUnitChunk
   //
   public RpcTcGetMultExeUnitChunkOut RpcTcGetMultExeUnitChunk(RpcTcGetMultExeUnitChunkIn arg, RpcTcGetMultExeUnitChunkOut retVal) throws SDRException, IOException {

      call(25626, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetDebugMode
   //
   public RpcTcSetDebugModeOut RpcTcSetDebugMode(RpcTcSetDebugModeIn arg, RpcTcSetDebugModeOut retVal) throws SDRException, IOException {

      call(25701, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcStep
   //
   public RpcTcStepOut RpcTcStep(RpcTcStepIn arg, RpcTcStepOut retVal) throws SDRException, IOException {

      call(25702, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetCodePoint
   //
   public RpcTcSetCodePointOut RpcTcSetCodePoint(RpcTcSetCodePointIn arg, RpcTcSetCodePointOut retVal) throws SDRException, IOException {

      call(25704, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRemoveCodePoint
   //
   public RpcTcRemoveCodePointOut RpcTcRemoveCodePoint(RpcTcRemoveCodePointIn arg, RpcTcRemoveCodePointOut retVal) throws SDRException, IOException {

      call(25705, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetCodePoints
   //
   public RpcTcGetCodePointsOut RpcTcGetCodePoints(RpcTcGetCodePointsIn arg, RpcTcGetCodePointsOut retVal) throws SDRException, IOException {

      call(25706, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcActivateCodePoint
   //
   public RpcTcActivateCodePointOut RpcTcActivateCodePoint(RpcTcActivateCodePointIn arg, RpcTcActivateCodePointOut retVal) throws SDRException, IOException {

      call(25707, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetWatchPointCounter
   //
   public RpcTcGetWatchPointCounterOut RpcTcGetWatchPointCounter(RpcTcGetWatchPointCounterIn arg, RpcTcGetWatchPointCounterOut retVal) throws SDRException, IOException {

      call(25708, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcResetWatchPointCounter
   //
   public RpcTcResetWatchPointCounterOut RpcTcResetWatchPointCounter(RpcTcResetWatchPointCounterIn arg, RpcTcResetWatchPointCounterOut retVal) throws SDRException, IOException {

      call(25709, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetWatchPointVar
   //
   public RpcTcSetWatchPointVarOut RpcTcSetWatchPointVar(RpcTcSetWatchPointVarIn arg, RpcTcSetWatchPointVarOut retVal) throws SDRException, IOException {

      call(25710, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcRemoveWatchPointVar
   //
   public RpcTcRemoveWatchPointVarOut RpcTcRemoveWatchPointVar(RpcTcRemoveWatchPointVarIn arg, RpcTcRemoveWatchPointVarOut retVal) throws SDRException, IOException {

      call(25711, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetWatchPointValue
   //
   public RpcTcGetWatchPointValueOut RpcTcGetWatchPointValue(RpcTcGetWatchPointValueIn arg, RpcTcGetWatchPointValueOut retVal) throws SDRException, IOException {

      call(25712, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetWatchPointVars
   //
   public RpcTcGetWatchPointVarsOut RpcTcGetWatchPointVars(RpcTcGetWatchPointVarsIn arg, RpcTcGetWatchPointVarsOut retVal) throws SDRException, IOException {

      call(25713, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetWatchPointTrigger
   //
   public RpcTcSetWatchPointTriggerOut RpcTcSetWatchPointTrigger(RpcTcSetWatchPointTriggerIn arg, RpcTcSetWatchPointTriggerOut retVal) throws SDRException, IOException {

      call(25714, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcStepList
   //
   public RpcTcStepListOut RpcTcStepList(RpcTcStepListIn arg, RpcTcStepListOut retVal) throws SDRException, IOException {

      call(25730, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcSetExeFlag
   //
   public RpcTcSetExeFlagOut RpcTcSetExeFlag(RpcTcSetExeFlagIn arg, RpcTcSetExeFlagOut retVal) throws SDRException, IOException {

      call(25731, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetCodePointRoutines
   //
   public RpcTcGetCodePointRoutinesOut RpcTcGetCodePointRoutines(RpcTcGetCodePointRoutinesIn arg, RpcTcGetCodePointRoutinesOut retVal) throws SDRException, IOException {

      call(25732, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetNodeInfo
   //
   public RpcTcGetNodeInfoOut RpcTcGetNodeInfo(RpcTcGetNodeInfoIn arg, RpcTcGetNodeInfoOut retVal) throws SDRException, IOException {

      call(25213, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetRoutineInfo
   //
   public RpcTcGetRoutineInfoOut RpcTcGetRoutineInfo(RpcTcGetRoutineInfoIn arg, RpcTcGetRoutineInfoOut retVal) throws SDRException, IOException {

      call(25214, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetVarInfo
   //
   public RpcTcGetVarInfoOut RpcTcGetVarInfo(RpcTcGetVarInfoIn arg, RpcTcGetVarInfoOut retVal) throws SDRException, IOException {

      call(25215, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetTypeInfo
   //
   public RpcTcGetTypeInfoOut RpcTcGetTypeInfo(RpcTcGetTypeInfoIn arg, RpcTcGetTypeInfoOut retVal) throws SDRException, IOException {

      call(25216, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetConstInfo
   //
   public RpcTcGetConstInfoOut RpcTcGetConstInfo(RpcTcGetConstInfoIn arg, RpcTcGetConstInfoOut retVal) throws SDRException, IOException {

      call(25217, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetActValue
   //
   public RpcTcGetActValueOut RpcTcGetActValue(RpcTcGetActValueIn arg, RpcTcGetActValueOut retVal) throws SDRException, IOException {

      call(25301, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcOpenVarAccess
   //
   public RpcTcOpenVarAccessOut RpcTcOpenVarAccess(RpcTcOpenVarAccessIn arg, RpcTcOpenVarAccessOut retVal) throws SDRException, IOException {

      call(25303, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetSaveValue
   //
   public RpcTcGetSaveValueOut RpcTcGetSaveValue(RpcTcGetSaveValueIn arg, RpcTcGetSaveValueOut retVal) throws SDRException, IOException {

      call(25309, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetDirEntryInfo
   //
   public RpcTcGetDirEntryInfoOut RpcTcGetDirEntryInfo(RpcTcGetDirEntryInfoIn arg, RpcTcGetDirEntryInfoOut retVal) throws SDRException, IOException {

      call(25403, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetValidTokens
   //
   public RpcTcGetValidTokensOut RpcTcGetValidTokens(RpcTcGetValidTokensIn arg, RpcTcGetValidTokensOut retVal) throws SDRException, IOException {

      call(25509, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcInsertTerminalSymbol
   //
   public RpcTcInsertTerminalSymbolOut RpcTcInsertTerminalSymbol(RpcTcInsertTerminalSymbolIn arg, RpcTcInsertTerminalSymbolOut retVal) throws SDRException, IOException {

      call(25518, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcInsertNonTerminalSymbol
   //
   public RpcTcInsertNonTerminalSymbolOut RpcTcInsertNonTerminalSymbol(RpcTcInsertNonTerminalSymbolIn arg, RpcTcInsertNonTerminalSymbolOut retVal) throws SDRException, IOException {

      call(25519, false, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetHandle
   //
   public RpcTcGetHandleOut RpcTcGetHandle(RpcTcGetHandleIn arg, RpcTcGetHandleOut retVal) throws SDRException, IOException {

      call(25101, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetHandleName
   //
   public RpcTcGetHandleNameOut RpcTcGetHandleName(RpcTcGetHandleNameIn arg, RpcTcGetHandleNameOut retVal) throws SDRException, IOException {

      call(25102, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetSymbolText
   //
   public RpcTcGetSymbolTextOut RpcTcGetSymbolText(RpcTcGetSymbolTextIn arg, RpcTcGetSymbolTextOut retVal) throws SDRException, IOException {

      call(25516, true, arg, retVal);
      return retVal;
   }

   //------------------------------------------------------------------------------
   //  RpcTcGetExecInfo
   //
   public RpcTcGetExecInfoOut RpcTcGetExecInfo(RpcTcGetExecInfoIn arg, RpcTcGetExecInfoOut retVal) throws SDRException, IOException {

      call(25715, false, arg, retVal);
      return retVal;
   }

}
