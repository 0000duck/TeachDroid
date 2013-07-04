package com.keba.kemro.teach.network.sysrpc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.keba.jrpc.rpc.RPCException;
import com.keba.kemro.plc.network.sysrpc.TCI.*;
import com.keba.kemro.teach.network.TcAccessHandle;
import com.keba.kemro.teach.network.TcCodePoint;
import com.keba.kemro.teach.network.TcCodePointRoutineList;
import com.keba.kemro.teach.network.TcDirEntry;
import com.keba.kemro.teach.network.TcEditorModel;
import com.keba.kemro.teach.network.TcErrorMessage;
import com.keba.kemro.teach.network.TcExecutionUnit;
import com.keba.kemro.teach.network.TcStructuralChanges;
import com.keba.kemro.teach.network.TcStructuralModel;
import com.keba.kemro.teach.network.TcStructuralNode;
import com.keba.kemro.teach.network.TcStructuralRoutineNode;
import com.keba.kemro.teach.network.TcStructuralTypeNode;
import com.keba.kemro.teach.network.TcStructuralVarNode;
import com.keba.kemro.teach.network.TcValue;
import com.keba.kemro.teach.network.TcWatchpointVarNode;
import com.keba.kemro.teach.network.base.TcStructuralAbstractNode;

public class TcSysRpcStructuralModel implements TcStructuralModel {
	/** Iteration filter for all nodes */
	// private static final int FILTER_ALL = SysRpcTcNodeKind.rpcFilterAllNode;
	/** Iteration filter for all user program nodes */
	private static final int FILTER_USER_PROGRAM = SysRpcTcNodeKind.rpcFilterProgramUserNode;
	/** Iteration filter for all user type nodes */
	private static final int FILTER_USER_TYPE = SysRpcTcNodeKind.rpcFilterTypeUserNode;
	/** Iteration filter for all user routine nodes */
	private static final int FILTER_USER_ROUTINE = SysRpcTcNodeKind.rpcFilterRoutineUserNode;
	/** Iteration filter for all user variable nodes */
	private static final int FILTER_USER_VAR = SysRpcTcNodeKind.rpcFilterVariableUserNode;
	/** Iteration filter for all user constant nodes */
	private static final int FILTER_USER_CONST = SysRpcTcNodeKind.rpcFilterConstantUserNode;
	/** Iteration filter for all user nodes */
//	 private static final int FILTER_ALL_USER =
//	 SysRpcTcNodeKind.rpcFilterAllUserNode;
	
	private static final int FILTER_ALL_VAR = SysRpcTcNodeKind.rpcVariableNode;
	
	private final SysRpcTcBuildIn SysRpcTcBuildIn = new SysRpcTcBuildIn();
	private final SysRpcTcBuildOut SysRpcTcBuildOut = new SysRpcTcBuildOut();
	private final SysRpcTcDestroyIn SysRpcTcDestroyIn = new SysRpcTcDestroyIn();
	private final SysRpcTcDestroyOut SysRpcTcDestroyOut = new SysRpcTcDestroyOut();
	private final SysRpcTcGetVarAccessIn SysRpcTcGetVarAccessIn = new SysRpcTcGetVarAccessIn();
	private final SysRpcTcGetVarAccessOut SysRpcTcGetVarAccessOut = new SysRpcTcGetVarAccessOut();
	private final SysRpcTcGetStructElemAccessIn SysRpcTcGetStructElemAccessIn = new SysRpcTcGetStructElemAccessIn();
	private final SysRpcTcGetStructElemAccessOut SysRpcTcGetStructElemAccessOut = new SysRpcTcGetStructElemAccessOut();
	private final SysRpcTcGetArrayElemAccessIn SysRpcTcGetArrayElemAccessIn = new SysRpcTcGetArrayElemAccessIn();
	private final SysRpcTcGetArrayElemAccessOut SysRpcTcGetArrayElemAccessOut = new SysRpcTcGetArrayElemAccessOut();
	private final SysRpcTcGetNodeInfoListIn SysRpcTcGetNodeInfoListIn = new SysRpcTcGetNodeInfoListIn();
	private final SysRpcTcGetNodeInfoListOut SysRpcTcGetNodeInfoListOut = new SysRpcTcGetNodeInfoListOut();
	private final SysRpcTcGetFirstNodeChunkIn SysRpcTcGetFirstNodeChunkIn = new SysRpcTcGetFirstNodeChunkIn();
	private final SysRpcTcGetFirstNodeChunkOut SysRpcTcGetFirstNodeChunkOut = new SysRpcTcGetFirstNodeChunkOut();
	private final SysRpcTcGetNextNodeChunkIn SysRpcTcGetNextNodeChunkIn = new SysRpcTcGetNextNodeChunkIn();
	private final SysRpcTcGetNextNodeChunkOut SysRpcTcGetNextNodeChunkOut = new SysRpcTcGetNextNodeChunkOut();
	private final SysRpcTcGetRoutineInfoListIn SysRpcTcGetRoutineInfoListIn = new SysRpcTcGetRoutineInfoListIn();
	private final SysRpcTcGetRoutineInfoListOut SysRpcTcGetRoutineInfoListOut = new SysRpcTcGetRoutineInfoListOut();
	private final SysRpcTcGetTypeInfoListIn SysRpcTcGetTypeInfoListIn = new SysRpcTcGetTypeInfoListIn();
	private final SysRpcTcGetTypeInfoListOut SysRpcTcGetTypeInfoListOut = new SysRpcTcGetTypeInfoListOut();
	private final SysRpcTcGetVarInfoListIn SysRpcTcGetVarInfoListIn = new SysRpcTcGetVarInfoListIn();
	private final SysRpcTcGetVarInfoListOut SysRpcTcGetVarInfoListOut = new SysRpcTcGetVarInfoListOut();
	private final SysRpcTcGetConstInfoListIn SysRpcTcGetConstInfoListIn = new SysRpcTcGetConstInfoListIn();
	private final SysRpcTcGetConstInfoListOut SysRpcTcGetConstInfoListOut = new SysRpcTcGetConstInfoListOut();
	private final SysRpcTcWriteDataIn SysRpcTcWriteDataIn = new SysRpcTcWriteDataIn();
	private final SysRpcTcWriteDataOut SysRpcTcWriteDataOut = new SysRpcTcWriteDataOut();
	private final SysRpcTcAddVarNodeIn SysRpcTcAddVarNodeIn = new SysRpcTcAddVarNodeIn();
	private final SysRpcTcAddVarNodeOut SysRpcTcAddVarNodeOut = new SysRpcTcAddVarNodeOut();
	private final SysRpcTcRemoveNodeIn SysRpcTcRemoveNodeIn = new SysRpcTcRemoveNodeIn();
	private final SysRpcTcRemoveNodeOut SysRpcTcRemoveNodeOut = new SysRpcTcRemoveNodeOut();
	private final SysRpcTcOpenNodeIn SysRpcTcOpenNodeIn = new SysRpcTcOpenNodeIn();
	private final SysRpcTcOpenNodeOut SysRpcTcOpenNodeOut = new SysRpcTcOpenNodeOut();
	private final SysRpcTcOpenVarAccessIn SysRpcTcOpenVarAccessIn = new SysRpcTcOpenVarAccessIn();
	private final SysRpcTcOpenVarAccessOut SysRpcTcOpenVarAccessOut = new SysRpcTcOpenVarAccessOut();
   private final SysRpcTcOpenVarAccessListIn SysRpcTcOpenVarAccessListIn = new SysRpcTcOpenVarAccessListIn();
   private final SysRpcTcOpenVarAccessListOut SysRpcTcOpenVarAccessListOut = new SysRpcTcOpenVarAccessListOut();  
	private final SysRpcTcAddRoutineNodeIn SysRpcTcAddRoutineNodeIn = new SysRpcTcAddRoutineNodeIn();
	private final SysRpcTcAddRoutineNodeOut SysRpcTcAddRoutineNodeOut = new SysRpcTcAddRoutineNodeOut();
	private final SysRpcTcGetErrorsIn SysRpcTcGetErrorsIn = new SysRpcTcGetErrorsIn();
	private final SysRpcTcGetErrorsOut SysRpcTcGetErrorsOut = new SysRpcTcGetErrorsOut();
	private final SysRpcTcWriteInitValueIn SysRpcTcWriteInitValueIn = new SysRpcTcWriteInitValueIn();
	private final SysRpcTcWriteInitValueOut SysRpcTcWriteInitValueOut = new SysRpcTcWriteInitValueOut();
	private final SysRpcTcAddProgramNodeIn SysRpcTcAddProgramNodeIn = new SysRpcTcAddProgramNodeIn();
	private final SysRpcTcAddProgramNodeOut SysRpcTcAddProgramNodeOut = new SysRpcTcAddProgramNodeOut();
	private final SysRpcTcGetAttributesIn SysRpcTcGetAttributesIn = new SysRpcTcGetAttributesIn();
	private final SysRpcTcGetAttributesOut SysRpcTcGetAttributesOut = new SysRpcTcGetAttributesOut();
	private final SysRpcTcSetAttributesIn SysRpcTcSetAttributesIn = new SysRpcTcSetAttributesIn();
	private final SysRpcTcSetAttributesOut SysRpcTcSetAttributesOut = new SysRpcTcSetAttributesOut();
	private final SysRpcTcGetNodeInfoIn SysRpcTcGetNodeInfoIn = new SysRpcTcGetNodeInfoIn();
	private final SysRpcTcGetNodeInfoOut SysRpcTcGetNodeInfoOut = new SysRpcTcGetNodeInfoOut();
	private final SysRpcTcBuildStartIn SysRpcTcBuildStartIn = new SysRpcTcBuildStartIn();
	private final SysRpcTcBuildStartOut SysRpcTcBuildStartOut = new SysRpcTcBuildStartOut();
	private final SysRpcTcRenameNodeIn SysRpcTcRenameNodeIn = new SysRpcTcRenameNodeIn();
	private final SysRpcTcRenameNodeOut SysRpcTcRenameNodeOut = new SysRpcTcRenameNodeOut();
	private final SysRpcTcReadNodeChangeIn SysRpcTcReadNodeChangeIn = new SysRpcTcReadNodeChangeIn();
	private final SysRpcTcReadNodeChangeOut SysRpcTcReadNodeChangeOut = new SysRpcTcReadNodeChangeOut();
	private final SysRpcTcMoveVarIn SysRpcTcMoveVarIn = new SysRpcTcMoveVarIn();
	private final SysRpcTcMoveVarOut SysRpcTcMoveVarOut = new SysRpcTcMoveVarOut();
	private final SysRpcTcSetCodePointIn SysRpcTcSetCodePointIn = new SysRpcTcSetCodePointIn();
	private final SysRpcTcSetCodePointOut SysRpcTcSetCodePointOut = new SysRpcTcSetCodePointOut();
	private final SysRpcTcActivateCodePointIn SysRpcTcActivateCodePointIn = new SysRpcTcActivateCodePointIn();
	private final SysRpcTcActivateCodePointOut SysRpcTcActivateCodePointOut = new SysRpcTcActivateCodePointOut();
	private final SysRpcTcRemoveCodePointIn SysRpcTcRemoveCodePointIn = new SysRpcTcRemoveCodePointIn();
	private final SysRpcTcRemoveCodePointOut SysRpcTcRemoveCodePointOut = new SysRpcTcRemoveCodePointOut();
	private final SysRpcTcGetWatchPointCounterIn SysRpcTcGetWatchPointCounterIn = new SysRpcTcGetWatchPointCounterIn();
	private final SysRpcTcGetWatchPointCounterOut SysRpcTcGetWatchPointCounterOut = new SysRpcTcGetWatchPointCounterOut();
	private final SysRpcTcResetWatchPointCounterIn SysRpcTcResetWatchPointCounterIn = new SysRpcTcResetWatchPointCounterIn();
	private final SysRpcTcResetWatchPointCounterOut SysRpcTcResetWatchPointCounterOut = new SysRpcTcResetWatchPointCounterOut();
	private final SysRpcTcSetWatchPointVarIn SysRpcTcSetWatchPointVarIn = new SysRpcTcSetWatchPointVarIn();
	private final SysRpcTcSetWatchPointVarOut SysRpcTcSetWatchPointVarOut = new SysRpcTcSetWatchPointVarOut();
	private final SysRpcTcRemoveWatchPointVarIn SysRpcTcRemoveWatchPointVarIn = new SysRpcTcRemoveWatchPointVarIn();
	private final SysRpcTcRemoveWatchPointVarOut SysRpcTcRemoveWatchPointVarOut = new SysRpcTcRemoveWatchPointVarOut();
	private final SysRpcTcGetWatchPointValueIn SysRpcTcGetWatchPointValueIn = new SysRpcTcGetWatchPointValueIn();
	private final SysRpcTcGetWatchPointValueOut SysRpcTcGetWatchPointValueOut = new SysRpcTcGetWatchPointValueOut();
	private final SysRpcTcGetWatchPointVarsIn SysRpcTcGetWatchPointVarsIn = new SysRpcTcGetWatchPointVarsIn();
	private final SysRpcTcGetWatchPointVarsOut SysRpcTcGetWatchPointVarsOut = new SysRpcTcGetWatchPointVarsOut();
	private final SysRpcTcGetCodePointsIn SysRpcTcGetCodePointsIn = new SysRpcTcGetCodePointsIn();
	private final SysRpcTcGetCodePointsOut SysRpcTcGetCodePointsOut = new SysRpcTcGetCodePointsOut();
	private final SysRpcTcGetCodePointRoutinesOut SysRpcTcGetCodePointRoutinesOut = new SysRpcTcGetCodePointRoutinesOut();

	private final SysRpcTcConvertDirEntryPathIn SysRpcTcConvertDirEntryPathIn = new SysRpcTcConvertDirEntryPathIn();
	private final SysRpcTcConvertDirEntryPathOut SysRpcTcConvertDirEntryPathOut = new SysRpcTcConvertDirEntryPathOut();
	private final SysRpcScopeHdlToDirEntryPathIn SysRpcTcConvertScopeHandleToDirEntryPathIn = new SysRpcScopeHdlToDirEntryPathIn();
	private final SysRpcScopeHdlToDirEntryPathOut SysRpcTcConvertScopeHandleToDirEntryPathOut = new SysRpcScopeHdlToDirEntryPathOut();

	private final SysRpcTcOpenSyntaxEditorIn SysRpcTcOpenSyntaxEditorIn = new SysRpcTcOpenSyntaxEditorIn();
	private final SysRpcTcOpenSyntaxEditorOut SysRpcTcOpenSyntaxEditorOut = new SysRpcTcOpenSyntaxEditorOut();
	private final SysRpcTcInsertStatementIn SysRpcTcInsertStatementIn = new SysRpcTcInsertStatementIn();
	private final SysRpcTcInsertStatementOut SysRpcTcInsertStatementOut = new SysRpcTcInsertStatementOut();
	private final SysRpcTcDeleteStatementIn SysRpcTcDeleteStatementIn = new SysRpcTcDeleteStatementIn();
	private final SysRpcTcDeleteStatementOut SysRpcTcDeleteStatementOut = new SysRpcTcDeleteStatementOut();
	private final SysRpcTcReplaceStatementIn SysRpcTcReplaceStatementIn = new SysRpcTcReplaceStatementIn();
	private final SysRpcTcReplaceStatementOut SysRpcTcReplaceStatementOut = new SysRpcTcReplaceStatementOut();
	private final SysRpcTcIsCurrentIn SysRpcTcIsCurrentIn = new SysRpcTcIsCurrentIn();
	private final SysRpcTcIsCurrentOut SysRpcTcIsCurrentOut = new SysRpcTcIsCurrentOut();
	private boolean disconnected = false;
	
	static {
		/** Breakpoint */
		TcCodePoint.BREAKPOINT = SysRpcTcCodePointKind.rpcBreakPoint;
		/** Watchpoint */
		TcCodePoint.WATCHPOINT = SysRpcTcCodePointKind.rpcWatchPoint;
		/** Main flow breakpoint */
		TcCodePoint.MAIN_FLOW_BREAKPOINT = SysRpcTcCodePointKind.rpcBreakPointMain;

	}

	TcSysRpcClient client;

	protected TcSysRpcStructuralModel(TcSysRpcClient client) {
		this.client = client;
	}
	


	public TcStructuralNode build(TcDirEntry dirEntry) {
		try {
			TcSysRpcStructuralNode oldProject = (TcSysRpcStructuralNode) getNode(dirEntry);
			TcSysRpcStructuralNode newProject = null;
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			int branchMajor = client.getBranchMajorVersion();
			int branchMinor = client.getBranchMinorVersion();
			if ((3 <= major) || ((major == 2) && (92 < minor)) || ((major == 2) && (92 == minor) && (2 < branchMajor))
					|| ((major == 2) && (92 == minor) && (1 == branchMajor) && (3 <= branchMinor))) {
				synchronized (SysRpcTcBuildStartIn) {
					SysRpcTcBuildStartIn.dirEntryPath=((TcSysRpcDirEntry) dirEntry).getHandle();
//					SysRpcTcClientInfo [] info= client.getClientInfo();
					client.client.SysRpcTcBuildStart_1(SysRpcTcBuildStartIn, SysRpcTcBuildStartOut);
					if (SysRpcTcBuildStartOut.retVal) {
						int count = 0;
						do {
							count++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
							newProject = (TcSysRpcStructuralNode) getNode(dirEntry);
						} while ((count < 600) && (newProject == null));
					}
				}
			} else {
				synchronized (SysRpcTcBuildIn) {
					SysRpcTcBuildIn.dirEntryPath=((TcSysRpcDirEntry) dirEntry).getHandle();
					client.client.SysRpcTcBuild_1(SysRpcTcBuildIn, SysRpcTcBuildOut);
					if (SysRpcTcBuildOut.retVal) {
						newProject = (TcSysRpcStructuralNode) getNode(dirEntry);
					}
				}
			}
			if (newProject != null) {
				if (oldProject != null) {
					client.cache.removeProject(oldProject);
				}
				client.cache.putToDirEntryCache(newProject);
				return newProject;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - build: ");
		}
		return null;
	}
	
	private void disconnected(String text){
		if (!disconnected){
			disconnected = true;
			System.out.println(text);
		}
	}
	

	/**
	 * Entfernt das angegeben Projekt aus dem Strukturbaum.
	 * 
	 * @param project
	 *            Projekt
	 * 
	 * @return true für das erfolgreich Entfernen
	 */
	public boolean destroy(TcStructuralNode project) {
		boolean done = false;
		try {
			synchronized (SysRpcTcDestroyIn) {
				SysRpcTcDestroyIn.scopeHnd = ((TcSysRpcStructuralNode) project).getHandle();
				client.client.SysRpcTcDestroy_1(SysRpcTcDestroyIn, SysRpcTcDestroyOut);
				done = SysRpcTcDestroyOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - unloadProject: ");
		}
		if (done) {
			client.cache.removeProject((TcSysRpcStructuralNode) project);
		}
		return done;
	}

	/**
	 * Liefert den Verzeichniseintrag für den Strukturbaumknoten (Projekt,
	 * Programm oder Baustein) oder für die Deklarationsdatei, in der der
	 * Strukturbaumknoten deklariert wurde, zurück.
	 * 
	 * @return Verzeichniseintrag
	 */
	public TcDirEntry getDirEntry(TcStructuralNode node) {
		try {
			synchronized (SysRpcTcConvertScopeHandleToDirEntryPathIn) {
				SysRpcTcConvertScopeHandleToDirEntryPathIn.scopeHnd = ((TcSysRpcStructuralNode) node).getHandle();
				client.client.SysRpcScopeHdlToDirEntryPath_1(SysRpcTcConvertScopeHandleToDirEntryPathIn, SysRpcTcConvertScopeHandleToDirEntryPathOut);
				if (SysRpcTcConvertScopeHandleToDirEntryPathOut.retVal) {
					return new TcSysRpcDirEntry(SysRpcTcConvertScopeHandleToDirEntryPathOut.dirEntryPath.toString(), client);
				} else {
					if ((node.getKind() == TcStructuralNode.PROJECT) || (node.getKind() == TcStructuralNode.GLOBAL)
							|| (node.getKind() == TcStructuralNode.SYSTEM)) {
						client.cache.removeProject((TcSysRpcStructuralNode) node);
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcDirEntry - getConvertTo: ");
		}
		return null;
	}

	public TcStructuralNode getNode(TcDirEntry entry) {
		try {
			synchronized (SysRpcTcConvertDirEntryPathIn) {
				SysRpcTcConvertDirEntryPathIn.dirEntryPath=((TcSysRpcDirEntry) entry).getHandle();
				client.client.SysRpcTcConvertDirEntryPath_1(SysRpcTcConvertDirEntryPathIn, SysRpcTcConvertDirEntryPathOut);

				if (!SysRpcTcConvertDirEntryPathOut.retVal && ((TcSysRpcDirEntry) entry).getHandle().equals("_system.tt")) {
					String path = entry.getDirEntryPath();
					String handle = path.substring(1, path.length());
					SysRpcTcConvertDirEntryPathIn.dirEntryPath=handle;
					client.client.SysRpcTcConvertDirEntryPath_1(SysRpcTcConvertDirEntryPathIn, SysRpcTcConvertDirEntryPathOut);
				}
				if (SysRpcTcConvertDirEntryPathOut.retVal) {
					TcStructuralAbstractNode osn = client.cache.getFromCache(SysRpcTcConvertDirEntryPathOut.scopeHnd);
					if (entry.getKind() == TcDirEntry.UNIT) {
						if ((osn == null) || !(osn instanceof TcSysRpcStructuralTypeNode)) {
							osn = new TcSysRpcStructuralTypeNode(SysRpcTcConvertDirEntryPathOut.scopeHnd, client);
							client.cache.putToCache(osn);
							client.cache.putToDirEntryCache(osn);
						}
						return osn;
					}
					if ((osn == null) || (osn instanceof TcSysRpcStructuralTypeNode) || (osn instanceof TcSysRpcStructuralConstNode)) {
						osn = new TcSysRpcStructuralNode(SysRpcTcConvertDirEntryPathOut.scopeHnd, client);
						client.cache.putToDirEntryCache(osn);
					}
					return osn;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcDirEntry - getConvertTo: ");
		}
		return null;
	}

	public Enumeration getNodes(TcStructuralNode scope, int kind) {
		return new StructuralNodeChunkEnumeration(scope, kind);
	}

	/**
	 * Liefert den Wurzelknoten des Strukturbaums.
	 * 
	 * @return Wurzelknoten
	 */
	public TcStructuralNode getRoot() {
		try {
			TcSysRpcStructuralNode parent = null;
			synchronized (SysRpcTcOpenNodeIn) {
				SysRpcTcOpenNodeIn.nodePath="_root";
				client.client.SysRpcTcOpenNode_1(SysRpcTcOpenNodeIn, SysRpcTcOpenNodeOut);
				if (SysRpcTcOpenNodeOut.retVal) {
					parent = new TcSysRpcStructuralNode(SysRpcTcOpenNodeOut.nodeHnd, client);
				}
			}
			while ((parent != null) && (parent.getKind() != TcStructuralNode.ROOT)) {
				parent = (TcSysRpcStructuralNode) parent.getParent();
			}
			if (parent != null) {
				client.cache.putToDirEntryCache(parent);
			}
			return parent;
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - addRoutine: ");
		}
		return null;
	}

	/**
	 * Returns the attributes for the given node.
	 * 
	 * @param node
	 *            node
	 * @return attributes
	 */
	public String getAttributes(TcStructuralNode node) {
		try {
			synchronized (SysRpcTcGetAttributesIn) {
				if (SysRpcTcGetAttributesIn.scopeHnd == null) {
					SysRpcTcGetAttributesIn.scopeHnd = new int[TCI.rpcChunkLen];
				}
				SysRpcTcGetAttributesIn.scopeHnd[0] = ((TcSysRpcStructuralNode) node).getHandle();
				SysRpcTcGetAttributesIn.scopeHnd_count = 1;
				client.client.SysRpcTcGetAttributes_1(SysRpcTcGetAttributesIn, SysRpcTcGetAttributesOut);
				if (SysRpcTcGetAttributesOut.retVal) {
					return SysRpcTcGetAttributesOut.attributes[0].toString();
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getAttributes: ");
		}
		return null;
	}

	/**
	 * Returns the attributes for the given nodes.
	 * 
	 * @param nodes
	 *            nodes
	 * @return attributes
	 */
	public String[] getAttributes(Vector nodes) {
		try {
			synchronized (SysRpcTcGetAttributesIn) {
				String[] attributes = new String[nodes.size()];
				int index = 0;
				while (index < nodes.size()) {
					int end = index + TCI.rpcChunkLen;
					if (end >= nodes.size()) {
						end = nodes.size();
					}
					if (SysRpcTcGetAttributesIn.scopeHnd == null) {
						SysRpcTcGetAttributesIn.scopeHnd = new int[TCI.rpcChunkLen];
					}
					for (int i = index; i < end; i++) {
						TcSysRpcStructuralNode sn = (TcSysRpcStructuralNode) nodes.elementAt(i);
						SysRpcTcGetAttributesIn.scopeHnd[i - index] = sn.getHandle();
					}
					SysRpcTcGetAttributesIn.scopeHnd_count = end - index;
					client.client.SysRpcTcGetAttributes_1(SysRpcTcGetAttributesIn, SysRpcTcGetAttributesOut);
					if (SysRpcTcGetAttributesOut.retVal) {
						for (int i = 0; i < end - index; i++) {
							attributes[index + 1] = SysRpcTcGetAttributesOut.attributes[i].toString();
						}
					} else {
						return null;
					}
					index = index + TCI.rpcChunkLen;
				}
				return attributes;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getAttributes: ");
		}
		return null;
	}

	/**
	 * Sets the attributes for the given node.
	 * 
	 * @param node
	 *            node
	 * @param attributes
	 *            attributes
	 * @return true if the attributes has successfully set
	 */
	public boolean setAttirbutes(TcStructuralNode node, String attributes) {
		try {
			synchronized (SysRpcTcSetAttributesIn) {
				((TcSysRpcStructuralNode) node).setHasAttributes(0 < attributes.length());
				SysRpcTcSetAttributesIn.scopeHnd = ((TcSysRpcStructuralNode) node).getHandle();
				SysRpcTcSetAttributesIn.attributes=attributes;
				client.client.SysRpcTcSetAttributes_1(SysRpcTcSetAttributesIn, SysRpcTcSetAttributesOut);
				return SysRpcTcSetAttributesOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - setAttirbutes: ");
		}
		return false;
	}

	/**
	 * @return true if teach control supports the change list interface
	 */
	public boolean supportsChangeList() {
		int major = client.getMajorVersion();
		int minor = client.getMinorVersion();
		return ((3 < major) || ((3 == major) && (2 <= minor)));
	}

	/**
	 * Returns changes that happened after the startCounter.
	 * 
	 * @param startCounter
	 *            counter from which the changes should be read
	 * @return changes that happened after startCounter otherwise null if there
	 *         wasn't any changes
	 */
	public TcStructuralChanges getChanges(int startCounter) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			if ((3 < major) || ((3 == major) && (2 <= minor))) {
				synchronized (SysRpcTcReadNodeChangeIn) {
					SysRpcTcReadNodeChangeIn.startChangeCnt = startCounter;
					client.client.SysRpcTcReadNodeChange_1(SysRpcTcReadNodeChangeIn, SysRpcTcReadNodeChangeOut);
					if (SysRpcTcReadNodeChangeOut.retVal) {
						if (0 < SysRpcTcReadNodeChangeOut.nrChanges) {
							synchronized (SysRpcTcGetNodeInfoIn) {
								int nodeHnds = SysRpcTcReadNodeChangeOut.nrChanges;
								TcStructuralChanges changes = new TcStructuralChanges();
								changes.nodes = new TcSysRpcStructuralNode[nodeHnds];
								changes.nrOfNodes = 0;
								changes.nextChangeCounter = SysRpcTcReadNodeChangeOut.changes[nodeHnds - 1].changeCnt + 1;
								changes.hasMoreChanges = nodeHnds == TCI.rpcChunkLen;

								for (int i = 0; i < nodeHnds; i++) {
									changes.sameChangesLost = changes.sameChangesLost || (startCounter != SysRpcTcReadNodeChangeOut.changes[i].changeCnt);
									startCounter = SysRpcTcReadNodeChangeOut.changes[i].changeCnt + 1;
									SysRpcTcGetNodeInfoIn.scopeHnd = SysRpcTcReadNodeChangeOut.changes[i].hdl;
									client.client.SysRpcTcGetNodeInfo_1(SysRpcTcGetNodeInfoIn, SysRpcTcGetNodeInfoOut);
									if (SysRpcTcGetNodeInfoOut.retVal) {
										// node added to the structural tree
										SysRpcTcNodeInfo info = SysRpcTcGetNodeInfoOut.info;
										boolean add = !client.getUserMode() || (((info.attr & SysRpcTcNodeAttr.rpcUserNodeAttr) == SysRpcTcNodeAttr.rpcUserNodeAttr));
										if (add) {
											int kind = info.kind.value;
											switch (kind) {
											case TcStructuralNode.ROUTINE:
												changes.nodes[changes.nrOfNodes] = new TcSysRpcStructuralRoutineNode(SysRpcTcReadNodeChangeOut.changes[i].hdl,
														client);
												break;
											case TcStructuralNode.TYPE:
												changes.nodes[changes.nrOfNodes] = new TcSysRpcStructuralTypeNode(SysRpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											case TcStructuralNode.VAR:
												changes.nodes[changes.nrOfNodes] = new TcSysRpcStructuralVarNode(SysRpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											case TcStructuralNode.CONST:
												changes.nodes[changes.nrOfNodes] = new TcSysRpcStructuralConstNode(SysRpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											default:
												changes.nodes[changes.nrOfNodes] = new TcSysRpcStructuralNode(SysRpcTcReadNodeChangeOut.changes[i].hdl, client);
											}
											((TcSysRpcStructuralNode) changes.nodes[changes.nrOfNodes]).setInfo(info, null);
											changes.nrOfNodes++;
										}
									} else {
										// node removed from the structural tree
										changes.nodes[changes.nrOfNodes] = new TcSysRpcStructuralInvalidNode(SysRpcTcReadNodeChangeOut.changes[i].hdl, client);
										changes.nrOfNodes++;
										TcSysRpcStructuralNode n = (TcSysRpcStructuralNode) client.cache
												.getFromDirEntryCache(SysRpcTcReadNodeChangeOut.changes[i].hdl);
										if (n != null) {
											client.cache.removeProject(n);
										}
									}
								}
								return changes;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getChanges: ");
		}
		return null;
	}

	public boolean projectSourcesChanged(TcStructuralNode project) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			int branchMajor = client.getBranchMajorVersion();
			int branchMinor = client.getBranchMinorVersion();			
			if (	(3 < major) 
				|| ((3 == major) && (16 <= minor))
				|| ((3 == major) && (12 == minor) && (1 == branchMajor) && (1 <= branchMinor))) {
				synchronized (SysRpcTcIsCurrentIn) {
					SysRpcTcIsCurrentIn.scopeHnd = ((TcSysRpcStructuralNode) project).getHandle();
					client.client.SysRpcTcIsCurrent_1(SysRpcTcIsCurrentIn, SysRpcTcIsCurrentOut);
					return !SysRpcTcIsCurrentOut.retVal;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - projectSourcesChanged: ");
		}
		return true;
	}

	/**
	 * Creates a program.
	 * 
	 * @param parent
	 *            global or project node
	 * @param name
	 *            name of the program
	 * @return program node
	 */
	public TcStructuralNode addProgram(TcStructuralNode parent, String name) {
		try {
			synchronized (SysRpcTcAddProgramNodeIn) {
				SysRpcTcAddProgramNodeIn.nodeInfo.kind.value = SysRpcTcNodeKind.rpcProgramNode;
				SysRpcTcAddProgramNodeIn.nodeInfo.attr = client.getUserMode() ? SysRpcTcNodeAttr.rpcUserNodeAttr : 0;
				SysRpcTcAddProgramNodeIn.nodeInfo.declHnd = ((TcSysRpcStructuralNode) parent).getHandle();
				SysRpcTcAddProgramNodeIn.nodeInfo.upperHnd = ((TcSysRpcStructuralNode) parent).getHandle();
				SysRpcTcAddProgramNodeIn.nodeInfo.elemName=name;
				client.client.SysRpcTcAddProgramNode_1(SysRpcTcAddProgramNodeIn, SysRpcTcAddProgramNodeOut);
				if (SysRpcTcAddProgramNodeOut.retVal) {
					/**
					 * @todo workaround: routine handle should be returned
					 */
					String path = name;
					while (parent.getKind() != TcStructuralNode.ROOT) {
						path = parent.getName() + "." + path;
						parent = parent.getParent();
					}
					synchronized (SysRpcTcOpenNodeIn) {
						SysRpcTcOpenNodeIn.nodePath=path;
						client.client.SysRpcTcOpenNode_1(SysRpcTcOpenNodeIn, SysRpcTcOpenNodeOut);
						if (SysRpcTcOpenNodeOut.retVal) {
							TcStructuralNode r = new TcSysRpcStructuralNode(SysRpcTcOpenNodeOut.nodeHnd, client);
							return r;
						}
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - addProgram: ");
		}
		return null;
	}

	/**
	 * Erzeugt einen Routinenknoten im Strukturbaum.
	 * 
	 * @param dirEntryNode
	 *            Programm oder Baustein, in der die Routine deklariert werden
	 *            soll
	 * @param parent
	 *            bestimmt die Sichtbarkeit, für eine globale Routine ist parent
	 *            der Wurzelknoten oder ein Projekt, für eine public oder
	 *            private Routine ist parent ein Programm oder Bautstein
	 * @param name
	 *            Routinenname
	 * @param routineKind
	 *            UNNAMED_ROUTINE, NAMED_ROUTINE oder AT_ROUTINE, sind
	 *            deklariert in TcStructuralRoutineNode
	 * @param returnType
	 *            Rückgabetyp der Routine
	 * @param eventVariable
	 *            Ereignisvariable
	 * @param isPrivate
	 *            true für eine private Routine
	 * 
	 * @return TcStructuralRoutineNode neue Routine
	 * 
	 * @see TcStructuralRoutineNode
	 */
	public TcStructuralRoutineNode addRoutine(TcStructuralNode declarationNode, TcStructuralNode parent, String name, byte routineKind,
			TcStructuralTypeNode returnType, TcStructuralVarNode eventVariable, boolean isPrivate) {
		try {
			synchronized (SysRpcTcAddRoutineNodeIn) {
				SysRpcTcAddRoutineNodeIn.nodeInfo.kind.value = SysRpcTcNodeKind.rpcRoutineNode;
				SysRpcTcAddRoutineNodeIn.nodeInfo.attr = client.getUserMode() ? SysRpcTcNodeAttr.rpcUserNodeAttr : 0;
				SysRpcTcAddRoutineNodeIn.nodeInfo.declHnd = ((TcSysRpcStructuralNode) declarationNode).getHandle();
				SysRpcTcAddRoutineNodeIn.nodeInfo.upperHnd = ((TcSysRpcStructuralNode) parent).getHandle();
				SysRpcTcAddRoutineNodeIn.nodeInfo.elemName=name;
				SysRpcTcAddRoutineNodeIn.routInfo.kind.value = routineKind;
				SysRpcTcAddRoutineNodeIn.routInfo.retTypeHnd = (returnType != null) ? ((TcSysRpcStructuralNode) returnType).getHandle() : 0;
				SysRpcTcAddRoutineNodeIn.routInfo.eventVarHnd = (eventVariable != null) ? ((TcSysRpcStructuralNode) eventVariable).getHandle() : 0;
				SysRpcTcAddRoutineNodeIn.routInfo.isPrivate = isPrivate;
				client.client.SysRpcTcAddRoutineNode_1(SysRpcTcAddRoutineNodeIn, SysRpcTcAddRoutineNodeOut);
				if (SysRpcTcAddRoutineNodeOut.retVal) {
					/**
					 * @todo workaround: routine handle should be returned
					 */
					String path = name;
					while (parent.getKind() != TcStructuralNode.ROOT) {
						path = parent.getName() + "." + path;
						parent = parent.getParent();
					}
					synchronized (SysRpcTcOpenNodeIn) {
						SysRpcTcOpenNodeIn.nodePath=path;
						client.client.SysRpcTcOpenNode_1(SysRpcTcOpenNodeIn, SysRpcTcOpenNodeOut);
						if (SysRpcTcOpenNodeOut.retVal) {
							TcSysRpcStructuralRoutineNode r = new TcSysRpcStructuralRoutineNode(SysRpcTcOpenNodeOut.nodeHnd, client);
							return r;
						}
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - addRoutine: ");
		}
		return null;
	}

	/**
	 * Erzeugt eine Variablenknoten im Strukturbaum.
	 * 
	 * @param dirEntryNode
	 *            Programm oder Baustein, in der die Variable deklariert werden
	 *            soll
	 * @param parent
	 *            bestimmt die Sichtbarkeit, für eine globale Variable ist
	 *            parent der Wurzelknoten oder ein Projekt, für eine public oder
	 *            private Variable ist parent ein Programm oder Bautstein und
	 *            für einen Parameter oder Routinenvariable ist parent eine
	 *            Routine
	 * @param name
	 *            Variablenname
	 * @param varKind
	 *            VAR, PARAM, CONST_PARAM oder CONST_VAR deklariert in
	 *            TcStructuralVarNode
	 * @param type
	 *            Variablentyp
	 * @param isProjectSave
	 *            true für eine Project-Save-Variable
	 * @param isSave
	 *            true für eine Save-Variable
	 * @param isPrivate
	 *            true für eine private Variable
	 * @param isDynamic 
	 *            true für Variable ohne Deklaration in Sourcefile
	 * 
	 * @return TcStructuralVarNode neue Variable
	 * 
	 * @see TcStructuralVarNode
	 */
	public TcStructuralVarNode addVariable(TcStructuralNode declarationNode, TcStructuralNode parent, String name, byte varKind, TcStructuralTypeNode type,
			boolean isProjectSave, boolean isSave, boolean isPrivate, boolean isDynamic) {
		try {
			synchronized (SysRpcTcAddVarNodeIn) {
				SysRpcTcAddVarNodeIn.nodeInfo.kind.value = SysRpcTcNodeKind.rpcVariableNode;
				SysRpcTcAddVarNodeIn.nodeInfo.attr = ((declarationNode == null) || declarationNode.isUserNode()) ? SysRpcTcNodeAttr.rpcUserNodeAttr : 0;	            
				SysRpcTcAddVarNodeIn.nodeInfo.declHnd = (declarationNode != null) ? ((TcSysRpcStructuralNode) declarationNode).getHandle() : 0;
				SysRpcTcAddVarNodeIn.nodeInfo.upperHnd = ((TcSysRpcStructuralNode) parent).getHandle();
				SysRpcTcAddVarNodeIn.nodeInfo.elemName=name;
				SysRpcTcAddVarNodeIn.varInfo.attr = 0;
				SysRpcTcAddVarNodeIn.varInfo.kind.value = varKind;
				SysRpcTcAddVarNodeIn.varInfo.typeHnd = ((TcSysRpcStructuralNode) type).getHandle();
				SysRpcTcAddVarNodeIn.varInfo.isProjectSave = isProjectSave;
				SysRpcTcAddVarNodeIn.varInfo.isSave = isSave;
				SysRpcTcAddVarNodeIn.varInfo.isPrivate = isPrivate;
	            if (isDynamic) {
	            	SysRpcTcAddVarNodeIn.varInfo.attr |= SysRpcTcVarAttr.rpcVarAttrIsDynamic;
	            }					
				client.client.SysRpcTcAddVarNode_1(SysRpcTcAddVarNodeIn, SysRpcTcAddVarNodeOut);
				if (SysRpcTcAddVarNodeOut.retVal) {
					// TcStructuralVarNode v = new
					// TcStructuralVarNode(SysRpcTcAddVarNodeOut.varHnd);
					// return v;
					/**
					 * @todo workaround: variable handle should be returned
					 */
					String path = name;
					while (parent.getKind() != TcStructuralNode.ROOT) {
						path = parent.getName() + "." + path;
						parent = parent.getParent();
					}
					synchronized (SysRpcTcOpenNodeIn) {
							SysRpcTcOpenNodeIn.nodePath=path;
						client.client.SysRpcTcOpenNode_1(SysRpcTcOpenNodeIn, SysRpcTcOpenNodeOut);
						if (SysRpcTcOpenNodeOut.retVal) {
							TcSysRpcStructuralVarNode v = new TcSysRpcStructuralVarNode(SysRpcTcOpenNodeOut.nodeHnd, client);
							return v;
						}
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - addVariable: ");
		}
		return null;
	}

	/**
	 * Entfernt eine Strukturbaumknoten.
	 * 
	 * @param node
	 *            Strukturbaumknoten
	 * 
	 * @return boolean true für das erfolgreiche Entfernen
	 */
	public boolean removeNode(TcStructuralNode node) {
		try {
			synchronized (SysRpcTcRemoveNodeIn) {
				SysRpcTcRemoveNodeIn.scopeHnd = ((TcSysRpcStructuralNode) node).getHandle();
				client.client.SysRpcTcRemoveNode_1(SysRpcTcRemoveNodeIn, SysRpcTcRemoveNodeOut);
				return SysRpcTcRemoveNodeOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - removeNode: ");
		}
		return false;
	}

	/**
	 * Renames the variable.
	 * 
	 * @param var
	 *            variable to rename
	 * @param name
	 *            new name
	 * @return new variable node if the renaming of the variable was
	 *         successfully
	 */
	public TcStructuralVarNode renameVariable(TcStructuralVarNode var, String name) {
		try {
			synchronized (SysRpcTcRenameNodeIn) {
				SysRpcTcRenameNodeIn.scopeHnd = ((TcSysRpcStructuralNode) var).getHandle();
				SysRpcTcRenameNodeIn.newName=name;
				client.client.SysRpcTcRenameNode_1(SysRpcTcRenameNodeIn, SysRpcTcRenameNodeOut);
				if (SysRpcTcRenameNodeOut.retVal) {
					return new TcSysRpcStructuralVarNode(((TcSysRpcStructuralNode) var).getHandle(), client);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - renameVariable: ");
		}
		return null;
	}

	/**
	 * Moves a variable to the destination scope.
	 * 
	 * @param var
	 *            variable to move
	 * @param dest
	 *            destination scope
	 * @return the new variable was successfully moved
	 */
	public TcStructuralVarNode moveVariable(TcStructuralVarNode var, TcStructuralNode dest) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			if ((3 < major) || ((3 == major) && (6 <= minor))) {
				synchronized (SysRpcTcMoveVarIn) {
					SysRpcTcMoveVarIn.varHnd = ((TcSysRpcStructuralNode) var).getHandle();
					SysRpcTcMoveVarIn.newScopeHnd = ((TcSysRpcStructuralNode) dest).getHandle();
					client.client.SysRpcTcMoveVar_1(SysRpcTcMoveVarIn, SysRpcTcMoveVarOut);
					if (SysRpcTcMoveVarOut.retVal) {
						return new TcSysRpcStructuralVarNode(SysRpcTcMoveVarOut.newVarHnd, client);
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - SysRpcTcMoveVarIn: ");
		}
		return null;
	}

	/**
	 * Fügt das angegeben Text text an der Postion pos der Routine routine ein
	 * und speichert den Editorinhalt. Das Einfügen erfolgt inkrementell und
	 * kann zur Ausführungszeit durchgeführt werden. Die Prüfung erfolgt im
	 * TeachControl, bei einem Misserfolg können die Kompilerfehlermeldung mit
	 * der Methode TcStructuralModel.getErrorMessage ausgelesen werden.
	 * 
	 * @param routine
	 *            Routine, in die das Statement eingefügt werden soll
	 * @param pos
	 *            Einfügeposition
	 * @param text
	 *            Statement
	 * 
	 * @return true für das erfolgreiche Einfügen
	 */
	public boolean insertTextInc(TcStructuralRoutineNode routine, int line, String text) {
		try {
			synchronized (SysRpcTcInsertStatementIn) {
				SysRpcTcInsertStatementIn.routineHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcInsertStatementIn.pos.line = line;
				SysRpcTcInsertStatementIn.pos.col = 0;
				SysRpcTcInsertStatementIn.stmtText=text;
				client.client.SysRpcTcInsertStatement_1(SysRpcTcInsertStatementIn, SysRpcTcInsertStatementOut);
				return SysRpcTcInsertStatementOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcEditorModel - insertStatementText: ");
		}
		return false;
	}

	/**
	 * Löscht den angegeben Textbereich. Das Löschen erfolgt inkrementell und
	 * kann zur Ausführungszeit durchgeführt werden. Die Prüfung erfolgt im
	 * TeachControl, bei einem Misserfolg können die Kompilerfehlermeldung mit
	 * der Methode TcStructuralModel.getErrorMessage ausgelesen werden.
	 * 
	 * @param routine
	 *            Routine, in die das Statement enthält
	 * @param range
	 *            Textbereich
	 * 
	 * @return true für das erfolgreiche Löschen
	 */
	public boolean deleteTextInc(TcStructuralRoutineNode routine, int line, int count) {
		try {
			synchronized (SysRpcTcDeleteStatementIn) {
				SysRpcTcDeleteStatementIn.routineHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcDeleteStatementIn.begPos.line = line;
				SysRpcTcDeleteStatementIn.begPos.col = 0;
				SysRpcTcDeleteStatementIn.endPos.line = line + count - 1;
				SysRpcTcDeleteStatementIn.endPos.col = 0;
				client.client.SysRpcTcDeleteStatement_1(SysRpcTcDeleteStatementIn, SysRpcTcDeleteStatementOut);
				return SysRpcTcDeleteStatementOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcEditorModel - deleteStatementText: ");
		}
		return false;
	}

	/**
	 * Ersetzt den angegeben Textbereich range durch den Text text. Das Löschen
	 * erfolgt inkrementell und kann zur Ausführungszeit durchgeführt werden.
	 * Die Prüfung erfolgt im TeachControl, bei einem Misserfolg können die
	 * Kompilerfehlermeldung mit der Methode TcStructuralModel.getErrorMessage
	 * ausgelesen werden.
	 * 
	 * @param routine
	 *            Routine, in die das Statement enthält
	 * @param range
	 *            der zu ersetzende Textbereich
	 * @param text
	 *            neuer Text
	 * @return true für das erfolgreiche Ersetzen
	 */
	public boolean replaceTextInc(TcStructuralRoutineNode routine, int line, int count, String text) {
		try {
			synchronized (SysRpcTcReplaceStatementIn) {
				SysRpcTcReplaceStatementIn.routineHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcReplaceStatementIn.begPos.line = line;
				SysRpcTcReplaceStatementIn.begPos.col = 0;
				SysRpcTcReplaceStatementIn.endPos.line = line + count - 1;
				SysRpcTcReplaceStatementIn.endPos.col = 0;
				SysRpcTcReplaceStatementIn.stmtText=text;
				client.client.SysRpcTcReplaceStatement_1(SysRpcTcReplaceStatementIn, SysRpcTcReplaceStatementOut);
				return SysRpcTcReplaceStatementOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcEditorModel - replaceStatementText: ");
		}
		return false;
	}

	public TcEditorModel getEditorModel(TcStructuralNode node) {
		try {
			synchronized (SysRpcTcOpenSyntaxEditorIn) {
				SysRpcTcOpenSyntaxEditorIn.scopeHnd = ((TcSysRpcStructuralNode) node).getHandle();
				SysRpcTcOpenSyntaxEditorIn.part.value = SysRpcTcPart.rpcAllPart;
				client.client.SysRpcTcOpenSyntaxEditor_1(SysRpcTcOpenSyntaxEditorIn, SysRpcTcOpenSyntaxEditorOut);
				if (SysRpcTcOpenSyntaxEditorOut.retVal) {
					return new TcSysRpcEditorModel(SysRpcTcOpenSyntaxEditorOut.editHnd, client);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcEditorModel - getEditorModel(): " + e);
		}
		return null;
	}

	public TcAccessHandle getVarAccessHandle(String fullPath) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			if ((major <= 3) && (minor < 18)) {
				synchronized (SysRpcTcOpenVarAccessIn) {
					SysRpcTcOpenVarAccessIn.varAccessPath=fullPath;
					client.client.SysRpcTcOpenVarAccess_1(SysRpcTcOpenVarAccessIn, SysRpcTcOpenVarAccessOut);
					if (SysRpcTcOpenVarAccessOut.retVal) {
						AccessHandle accessHandle = new AccessHandle(SysRpcTcOpenVarAccessOut.type.value);
						accessHandle.access = new SysRpcTcVarAccess();
						accessHandle.access.index = SysRpcTcOpenVarAccessOut.varAccess.index;
						accessHandle.access.offsets = new int[SysRpcTcOpenVarAccessOut.varAccess.offsets_count];
						System.arraycopy(SysRpcTcOpenVarAccessOut.varAccess.offsets, 0, accessHandle.access.offsets, 0, SysRpcTcOpenVarAccessOut.varAccess.offsets_count);
						accessHandle.access.offsets_count = SysRpcTcOpenVarAccessOut.varAccess.offsets_count;
						accessHandle.access.typeHandle = SysRpcTcOpenVarAccessOut.varAccess.typeHandle;
						accessHandle.access.varHandle = SysRpcTcOpenVarAccessOut.varAccess.varHandle;
						return accessHandle;
					}
				}
			} else {
	         synchronized (SysRpcTcOpenVarAccessListIn) {
	         	if (SysRpcTcOpenVarAccessListIn.varAccessPaths == null) {
	         		SysRpcTcOpenVarAccessListIn.varAccessPaths = new String[TCI.rpcChunkLen];
	         	}
	         	SysRpcTcOpenVarAccessListIn.varAccessPaths[0]=fullPath;
	         	SysRpcTcOpenVarAccessListIn.varAccessPaths_count = 1;
	            client.client.SysRpcTcOpenVarAccessList_1(SysRpcTcOpenVarAccessListIn, SysRpcTcOpenVarAccessListOut);
	            if (SysRpcTcOpenVarAccessListOut.retVal && (SysRpcTcOpenVarAccessListOut.varAccess_count == 1)) {
	               AccessHandle accessHandle = new AccessHandle(SysRpcTcOpenVarAccessListOut.info[0].typeKind.value);
	               accessHandle.setAttributesValid();
	               accessHandle.setReadOnly((SysRpcTcOpenVarAccessListOut.info[0].attr & SysRpcTcVarAccessAttr.rpcVarAccIsReadonly) != 0);
	               accessHandle.setConstant((SysRpcTcOpenVarAccessListOut.info[0].attr & SysRpcTcVarAccessAttr.rpcVarAccIsConst) != 0);
	               accessHandle.setUser((SysRpcTcOpenVarAccessListOut.info[0].attr & SysRpcTcVarAccessAttr.rpcVarAccIsUser) != 0);
	               accessHandle.setPrivate((SysRpcTcOpenVarAccessListOut.info[0].attr & SysRpcTcVarAccessAttr.rpcVarAccIsPrivate) != 0);
	         	   accessHandle.access = new SysRpcTcVarAccess();
	               accessHandle.access.index = SysRpcTcOpenVarAccessListOut.varAccess[0].index;
	               accessHandle.access.offsets = new int[SysRpcTcOpenVarAccessListOut.varAccess[0].offsets_count];
	               System.arraycopy(SysRpcTcOpenVarAccessListOut.varAccess[0].offsets, 0,
	                                accessHandle.access.offsets, 0,
	                                SysRpcTcOpenVarAccessListOut.varAccess[0].offsets_count);
	               accessHandle.access.offsets_count = SysRpcTcOpenVarAccessListOut.varAccess[0].offsets_count;
	               accessHandle.access.typeHandle = SysRpcTcOpenVarAccessListOut.varAccess[0].typeHandle;
	               accessHandle.access.varHandle = SysRpcTcOpenVarAccessListOut.varAccess[0].varHandle;
	               return accessHandle;
	            }
	         }
			}			
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getVarAccessHandle (String): ");
		}
		return null;
	}

	public TcAccessHandle getVarAccessHandle(Object[] instancePath) {
		try {
			if ((0 < instancePath.length) && (instancePath[0] instanceof TcSysRpcStructuralVarNode)
					&& (((TcSysRpcStructuralVarNode) instancePath[0]).getType() != null)) {
				synchronized (SysRpcTcGetVarAccessIn) {
					SysRpcTcGetVarAccessIn.varHnd = ((TcSysRpcStructuralVarNode) instancePath[0]).getHandle();
					client.client.SysRpcTcGetVarAccess_1(SysRpcTcGetVarAccessIn, SysRpcTcGetVarAccessOut);
					if (SysRpcTcGetVarAccessOut.retVal) {
						AccessHandle accessHandle = new AccessHandle(((TcSysRpcStructuralVarNode) instancePath[0]).getType().getTypeKind());
						accessHandle.access = new SysRpcTcVarAccess();
						accessHandle.access.index = SysRpcTcGetVarAccessOut.varAccess.index;
						accessHandle.access.offsets = new int[SysRpcTcGetVarAccessOut.varAccess.offsets_count];
						System.arraycopy(SysRpcTcGetVarAccessOut.varAccess.offsets, 0, accessHandle.access.offsets, 0,
								SysRpcTcGetVarAccessOut.varAccess.offsets_count);
						accessHandle.access.offsets_count = SysRpcTcGetVarAccessOut.varAccess.offsets_count;
						accessHandle.access.typeHandle = SysRpcTcGetVarAccessOut.varAccess.typeHandle;
						accessHandle.access.varHandle = SysRpcTcGetVarAccessOut.varAccess.varHandle;
						for (int i = 1; i < instancePath.length; i++) {
							if (instancePath[i] instanceof TcSysRpcStructuralVarNode) {
								synchronized (SysRpcTcGetStructElemAccessIn) {
										SysRpcTcGetStructElemAccessIn.varHnd = ((TcSysRpcStructuralVarNode) instancePath[i]).getHandle();
									SysRpcTcGetStructElemAccessIn.varAccess = accessHandle.access;
									client.client.SysRpcTcGetStructElemAccess_1(SysRpcTcGetStructElemAccessIn, SysRpcTcGetStructElemAccessOut);
									if (SysRpcTcGetStructElemAccessOut.retVal) {
										accessHandle
												.setTypeKind((((TcSysRpcStructuralVarNode) instancePath[i]).getType() != null) ? ((TcSysRpcStructuralVarNode) instancePath[i])
														.getType().getTypeKind()
														: -1);
										accessHandle.access.index = SysRpcTcGetStructElemAccessOut.elemAccess.index;
										accessHandle.access.offsets = new int[SysRpcTcGetStructElemAccessOut.elemAccess.offsets_count];
										System.arraycopy(SysRpcTcGetStructElemAccessOut.elemAccess.offsets, 0, accessHandle.access.offsets, 0,
												SysRpcTcGetStructElemAccessOut.elemAccess.offsets_count);
										accessHandle.access.offsets_count = SysRpcTcGetStructElemAccessOut.elemAccess.offsets_count;
										accessHandle.access.typeHandle = SysRpcTcGetStructElemAccessOut.elemAccess.typeHandle;
										accessHandle.access.varHandle = SysRpcTcGetStructElemAccessOut.elemAccess.varHandle;
									}
								}
							} else if (instancePath[i] instanceof Integer) {
								synchronized (SysRpcTcGetArrayElemAccessIn) {
									SysRpcTcGetArrayElemAccessIn.varAccess = accessHandle.access;
									SysRpcTcGetArrayElemAccessIn.index = ((Integer) instancePath[i]).intValue();
									client.client.SysRpcTcGetArrayElemAccess_1(SysRpcTcGetArrayElemAccessIn, SysRpcTcGetArrayElemAccessOut);
									if (SysRpcTcGetArrayElemAccessOut.retVal) {
	                        	int arrayFieldType = accessHandle.getTypeKind();
	                        	if (instancePath[i-1] instanceof TcSysRpcStructuralVarNode) {
	                        		TcStructuralTypeNode t = ((TcSysRpcStructuralVarNode) instancePath[i - 1]).getType();
	                        		while ((t != null) && (t.getTypeKind() == TcStructuralTypeNode.MAPTO_TYPE)) {
	                        			t = t.getBaseType();
	                        		}
	                        		arrayFieldType = ((t != null) && (t.getArrayElementType() != null)) ? t.getArrayElementType().getTypeKind(): TcStructuralTypeNode.UNKOWN; 
	                        	}
										accessHandle.setTypeKind(arrayFieldType);
										accessHandle.access.index = SysRpcTcGetArrayElemAccessOut.elemAccess.index;
										accessHandle.access.offsets = new int[SysRpcTcGetArrayElemAccessOut.elemAccess.offsets_count];
										System.arraycopy(SysRpcTcGetArrayElemAccessOut.elemAccess.offsets, 0, accessHandle.access.offsets, 0,
												SysRpcTcGetArrayElemAccessOut.elemAccess.offsets_count);
										accessHandle.access.offsets_count = SysRpcTcGetArrayElemAccessOut.elemAccess.offsets_count;
										accessHandle.access.typeHandle = SysRpcTcGetArrayElemAccessOut.elemAccess.typeHandle;
										accessHandle.access.varHandle = SysRpcTcGetArrayElemAccessOut.elemAccess.varHandle;
									}
								}

							} else {
								return null;
							}
						}
						return accessHandle;
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - getVarAccessHandle: ");
		}
		return null;
	}

	/**
	 * Speichert alle Save-Werte im angegebenen Sichtbarkeitsbereich.
	 * 
	 * @param scope
	 *            Projekt oder Programm
	 * 
	 * @return true für einen erfolgreichen Aufruf
	 */
	public boolean writeBackSaveValues(TcStructuralNode scope) {
		try {
			synchronized (SysRpcTcWriteDataIn) {
				SysRpcTcWriteDataIn.scopeHnd = ((TcSysRpcStructuralNode) scope).getHandle();
				client.client.SysRpcTcWriteData_1(SysRpcTcWriteDataIn, SysRpcTcWriteDataOut);
				return SysRpcTcWriteDataOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - writeBackSaveValues: ");
		}
		return false;
	}

	/**
	 * Writes the new variable declaration with the new init values. For the new
	 * init values are taken the actual values.
	 * 
	 * @param variable
	 *            variable which decaration should be written
	 * @return true if the declaration is written successfully
	 */
	public boolean writeBackInitValues(TcStructuralVarNode variable) {
		try {
			synchronized (SysRpcTcWriteInitValueIn) {
				SysRpcTcWriteInitValueIn.varHnd = ((TcSysRpcStructuralNode) variable).getHandle();
				client.client.SysRpcTcWriteInitValue_1(SysRpcTcWriteInitValueIn, SysRpcTcWriteInitValueOut);
				return SysRpcTcWriteInitValueOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcStructuralModel - writeBackInitValues: ");
		}
		return false;
	}

	/**
	 * Liefert die Fehlermeldungen des letzten build - Aufrufes bzw. der
	 * inkrementellen Editieroperation zurück.
	 * 
	 * @return Fehlermeldung
	 */
	
	
	Object SysRpcTcReadErrorIn = new Object();
	public TcErrorMessage getErrorMessage() {
		try {
			synchronized (SysRpcTcReadErrorIn) {
				SysRpcTcReadErrorOut SysRpcTcReadErrorOut = new SysRpcTcReadErrorOut();
				client.client.SysRpcTcReadError_1(SysRpcTcReadErrorOut);
				if (SysRpcTcReadErrorOut.retVal) {
					return new ErrorMessage(SysRpcTcReadErrorOut.error);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcTeachControl - getErrorMessage: ");
		}
		return null;
	}

	/**
	 * Returns the compiler error messages of the last build form the project.
	 * 
	 * @param project
	 *            project to check
	 * @return true if the target is successfully gotten
	 */
	public TcErrorMessage[] getErrorMessages(TcStructuralNode project) {
		try {
			synchronized (SysRpcTcGetErrorsIn) {
				SysRpcTcGetErrorsIn.prjHandle = ((TcSysRpcStructuralNode) project).getHandle();
				client.client.SysRpcTcGetErrors_1(SysRpcTcGetErrorsIn, SysRpcTcGetErrorsOut);
				if (SysRpcTcGetErrorsOut.retVal && (0 < SysRpcTcGetErrorsOut.errors_count)) {
					ErrorMessage[] errors = new ErrorMessage[SysRpcTcGetErrorsOut.errors_count];
					for (int i = 0; i < SysRpcTcGetErrorsOut.errors_count; i++) {
						errors[i] = new ErrorMessage(SysRpcTcGetErrorsOut.errors[i]);
					}
					return errors;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcTeachControl - getErrorMessages: ");
		}
		return null;
	}

	/**
	 * Setzt einen Breakpoint oder einen Watchpoint. Für execUnit und varPath
	 * null angeben, außer es ist ein Breakpoint bzw. Watchpoint bezogen auf
	 * eine Bausteininstanz notwendig.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * @param execUnit
	 *            Programmausführungseinheit, in der die Bausteininstanz
	 *            deklariert wurde.
	 * @param varPath
	 *            Bausteininstanzpfad
	 * @param kind
	 *            BREAKPOINT, WATCHPOINT or MAIN_FLOW_BREAKPOINT
	 * 
	 * @return true für das erfolgreiche Stoppen
	 */
	public boolean setCodePoint(TcStructuralRoutineNode routine, int lineNr, TcExecutionUnit execUnit, Object[] varPath, int kind) {
		try {
			synchronized (SysRpcTcSetCodePointIn) {
				SysRpcTcSetCodePointIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcSetCodePointIn.lineNr = lineNr;
				SysRpcTcSetCodePointIn.exeUnitHnd = (execUnit != null) ? ((TcSysRpcExecutionUnit) execUnit).getHandle() : 0;
				SysRpcTcSetCodePointIn.kind.value = kind;
				if (SysRpcTcSetCodePointIn.instancePath.elems == null) {
					SysRpcTcSetCodePointIn.instancePath.elems = new SysRpcTcInstancePathElem[TCI.rpcMaxInstancePathElems];
				}
				if (varPath == null) {
					SysRpcTcSetCodePointIn.instancePath.nrOfElems = 0;
				} else {
					SysRpcTcSetCodePointIn.instancePath.nrOfElems = (varPath.length <= TCI.rpcMaxInstancePathElems) ? varPath.length : TCI.rpcMaxInstancePathElems;
					for (int i = 0; i < SysRpcTcSetCodePointIn.instancePath.nrOfElems; i++) {
						if (SysRpcTcSetCodePointIn.instancePath.elems[i] == null) {
							SysRpcTcSetCodePointIn.instancePath.elems[i] = new SysRpcTcInstancePathElem();
						}
						if (varPath[i] instanceof TcStructuralVarNode) {
							SysRpcTcSetCodePointIn.instancePath.elems[i].structComponent = ((TcSysRpcStructuralVarNode) varPath[i]).getHandle();
						} else {
							SysRpcTcSetCodePointIn.instancePath.elems[i].arrayIndex = ((Integer) varPath[i]).intValue();
						}
					}
				}
				SysRpcTcSetCodePointIn.instancePath.elems_count = SysRpcTcSetCodePointIn.instancePath.nrOfElems;
				client.client.SysRpcTcSetCodePoint_1(SysRpcTcSetCodePointIn, SysRpcTcSetCodePointOut);
				return SysRpcTcSetCodePointOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - setCodePoint: ");
		}
		return false;
	}

	/**
	 * Aktivieren eines Breakpoints oder Watchpoints.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * @param enable
	 *            true für einen freigeschalteten Breakpoint bzw. Watchpoint
	 * 
	 * @return true für den erfolgreichen Aufruf
	 */
	public boolean enableCodePoint(TcStructuralRoutineNode routine, int lineNr, boolean enable) {
		try {
			synchronized (SysRpcTcActivateCodePointIn) {
				SysRpcTcActivateCodePointIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcActivateCodePointIn.lineNr = lineNr;
				SysRpcTcActivateCodePointIn.active = enable;
				client.client.SysRpcTcActivateCodePoint_1(SysRpcTcActivateCodePointIn, SysRpcTcActivateCodePointOut);
				return SysRpcTcActivateCodePointOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - enableCodePoint: ");
		}
		return false;
	}

	/**
	 * Entfernt einen Breakpoint oder Watchpoint.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * 
	 * @return true für das erfolgreiche Entfernen
	 */
	public boolean removeCodePoint(TcStructuralRoutineNode routine, int lineNr) {
		try {
			synchronized (SysRpcTcRemoveCodePointIn) {
				SysRpcTcRemoveCodePointIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcRemoveCodePointIn.lineNr = lineNr;
				client.client.SysRpcTcRemoveCodePoint_1(SysRpcTcRemoveCodePointIn, SysRpcTcRemoveCodePointOut);
				return SysRpcTcRemoveCodePointOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - removeCodePoint: ");
		}
		return false;
	}

	/**
	 * Entfernt alle Breakpoints und Watchpoints im angegeben TeachTalk -
	 * Project, - Programm und - Bausteine.
	 * 
	 * @param structNode
	 *            Strukturbaumknoten
	 * 
	 * @return true für das erfolgreiche Stoppen
	 */
	public boolean removeAllCodePoints(TcStructuralNode node) {
		try {
			synchronized (SysRpcTcRemoveCodePointIn) {
				SysRpcTcRemoveCodePointIn.routineScopeHnd = ((TcSysRpcStructuralNode) node).getHandle();
				SysRpcTcRemoveCodePointIn.lineNr = -1;
				client.client.SysRpcTcRemoveCodePoint_1(SysRpcTcRemoveCodePointIn, SysRpcTcRemoveCodePointOut);
				return SysRpcTcRemoveCodePointOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - removeCodePoint: ");
		}
		return false;
	}

	/**
	 * Liefert den Wert des Watchpoints - Zählers
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * 
	 * @return true für das erfolgreiche Lesen
	 */
	public int getWatchpointCounter(TcStructuralRoutineNode routine, int lineNr) {
		try {
			synchronized (SysRpcTcGetWatchPointCounterIn) {
				SysRpcTcGetWatchPointCounterIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcGetWatchPointCounterIn.lineNr = lineNr;
				client.client.SysRpcTcGetWatchPointCounter_1(SysRpcTcGetWatchPointCounterIn, SysRpcTcGetWatchPointCounterOut);
				if (SysRpcTcGetWatchPointCounterOut.retVal) {
					return SysRpcTcGetWatchPointCounterOut.counter;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - getWatchpointCounter: ");
		}
		return -1;
	}

	/**
	 * Setzt den Watchpoints - Zähler auf 0.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * 
	 * @return true für das erfolgreiche zurücksetzen
	 */
	public boolean resetWatchpointCounter(TcStructuralRoutineNode routine, int lineNr) {
		try {
			synchronized (SysRpcTcResetWatchPointCounterIn) {
				SysRpcTcResetWatchPointCounterIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcResetWatchPointCounterIn.lineNr = lineNr;
				client.client.SysRpcTcResetWatchPointCounter_1(SysRpcTcResetWatchPointCounterIn, SysRpcTcResetWatchPointCounterOut);
				return SysRpcTcResetWatchPointCounterOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - resetWatchpointCounter: ");
		}
		return false;
	}

	/**
	 * Setzt eine Watchpoint - Variable.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * @param execUnit
	 *            Ausführungseinheit des Programms, in der die Variable
	 *            deklariert wurde
	 * @param path
	 *            Variableninstanzpfad, enthält TcStructuralVarNode oder Integer
	 *            Object
	 * 
	 * @return true für das erfolgreiche Setzten
	 */
	public TcWatchpointVarNode setWatchpointVariable(TcStructuralRoutineNode routine, int lineNr, TcExecutionUnit execUnit, Object[] path) {
		try {
			synchronized (SysRpcTcSetWatchPointVarIn) {
				SysRpcTcSetWatchPointVarIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcSetWatchPointVarIn.lineNr = lineNr;
				SysRpcTcSetWatchPointVarIn.exeUnitHnd = (execUnit != null) ? ((TcSysRpcExecutionUnit) execUnit).getHandle() : 0;
				SysRpcTcSetWatchPointVarIn.instPath.nrOfElems = (path.length <= TCI.rpcMaxInstancePathElems) ? path.length : TCI.rpcMaxInstancePathElems;
				SysRpcTcSetWatchPointVarIn.instPath.elems_count = SysRpcTcSetWatchPointVarIn.instPath.nrOfElems;
				for (int i = 0; i < SysRpcTcSetWatchPointVarIn.instPath.nrOfElems; i++) {
					if (path[i] instanceof TcSysRpcStructuralVarNode) {
						SysRpcTcSetWatchPointVarIn.instPath.elems[i].structComponent = ((TcSysRpcStructuralVarNode) path[i]).getHandle();
					} else {
						SysRpcTcSetWatchPointVarIn.instPath.elems[i].arrayIndex = ((Integer) path[i]).intValue();
					}
				}
				client.client.SysRpcTcSetWatchPointVar_1(SysRpcTcSetWatchPointVarIn, SysRpcTcSetWatchPointVarOut);
				if (SysRpcTcSetWatchPointVarOut.retVal) {
					return new WatchpointVarNode(SysRpcTcSetWatchPointVarOut.wpVarHnd, path, execUnit);
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - setWatchpointVariable: ");
		}
		return null;
	}

	/**
	 * Löscht die angegebene Watchpoint - Variable.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * @param variable
	 *            Watchpoint - Variable
	 * 
	 * @return true für das erfolgreiche Löschen
	 */
	public boolean removeWatchpointVariable(TcStructuralRoutineNode routine, int lineNr, TcWatchpointVarNode variable) {
		try {
			synchronized (SysRpcTcRemoveWatchPointVarIn) {
				SysRpcTcRemoveWatchPointVarIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcRemoveWatchPointVarIn.lineNr = lineNr;
				SysRpcTcRemoveWatchPointVarIn.wpVarHnd = ((WatchpointVarNode) variable).getHandle();
				client.client.SysRpcTcRemoveWatchPointVar_1(SysRpcTcRemoveWatchPointVarIn, SysRpcTcRemoveWatchPointVarOut);
				return SysRpcTcRemoveWatchPointVarOut.retVal;
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - removeWatchpointVariable: ");
		}
		return false;
	}

	/**
	 * Löscht alle Watchpoint - Variable des Watchpoints.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * 
	 * @return true für das erfolgreiche Löschen
	 */
	public boolean removeAllWatchpointVariable(TcStructuralRoutineNode routine, int lineNr) {
		try {
			synchronized (SysRpcTcRemoveWatchPointVarIn) {
				SysRpcTcRemoveWatchPointVarIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcRemoveWatchPointVarIn.lineNr = lineNr;
				SysRpcTcRemoveWatchPointVarIn.wpVarHnd = 0;
				synchronized (SysRpcTcRemoveWatchPointVarOut) {
					client.client.SysRpcTcRemoveWatchPointVar_1(SysRpcTcRemoveWatchPointVarIn, SysRpcTcRemoveWatchPointVarOut);
					return SysRpcTcRemoveWatchPointVarOut.retVal;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - removeAllWatchpointVariable: ");
		}
		return false;
	}

	/**
	 * Liest den Wert der Watchpoint - Variable.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * @param variable
	 *            Watchpoint - Variable
	 * @param value
	 *            Wertobjekt
	 * 
	 * @return true für das erfolgreiche Lesen
	 */
	public boolean getWatchpointVariableValue(TcStructuralRoutineNode routine, int lineNr, TcWatchpointVarNode variable, TcValue value) {
		try {
			synchronized (SysRpcTcGetWatchPointValueIn) {
				SysRpcTcGetWatchPointValueIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcGetWatchPointValueIn.lineNr = lineNr;
				SysRpcTcGetWatchPointValueIn.wpVarHnd = ((WatchpointVarNode) variable).getHandle();
				client.client.SysRpcTcGetWatchPointValue_1(SysRpcTcGetWatchPointValueIn, SysRpcTcGetWatchPointValueOut);
				if (SysRpcTcGetWatchPointValueOut.retVal) {
					value.boolValue = SysRpcTcGetWatchPointValueOut.value.bValue;
					value.int8Value = (byte) SysRpcTcGetWatchPointValueOut.value.i8Value;
					value.int16Value = (short) SysRpcTcGetWatchPointValueOut.value.i16Value;
					value.int32Value = SysRpcTcGetWatchPointValueOut.value.i32Value;
					value.int64Value = SysRpcTcGetWatchPointValueOut.value.i64Value;
					value.floatValue = SysRpcTcGetWatchPointValueOut.value.fValue;
					value.stringValue = SysRpcTcGetWatchPointValueOut.value.sValue.toString();
					return true;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - getWatchpointVariableValue: ");
		}
		return false;
	}

	/**
	 * Liefert die Watchpoint - Variablen des angegebenen Watchpoints.
	 * 
	 * @param routine
	 *            Routine
	 * @param lineNr
	 *            Zeilennummer
	 * 
	 * @return true für den erfolgreiche Aufruf
	 */
	public TcWatchpointVarNode[] getWatchpointVariables(TcStructuralRoutineNode routine, int lineNr) {
		try {
			synchronized (SysRpcTcGetWatchPointVarsIn) {
				SysRpcTcGetWatchPointVarsIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				SysRpcTcGetWatchPointVarsIn.lineNr = lineNr;
				client.client.SysRpcTcGetWatchPointVars_1(SysRpcTcGetWatchPointVarsIn, SysRpcTcGetWatchPointVarsOut);
				if (SysRpcTcGetWatchPointVarsOut.retVal) {
					TcWatchpointVarNode[] vs = new TcWatchpointVarNode[SysRpcTcGetWatchPointVarsOut.nrWPVars];
					for (int i = 0; i < SysRpcTcGetWatchPointVarsOut.nrWPVars; i++) {
						Object[] path = new Object[SysRpcTcGetWatchPointVarsOut.instancePaths[i].nrOfElems];
						for (int j = 0; j < SysRpcTcGetWatchPointVarsOut.instancePaths[i].nrOfElems; j++) {
							if (SysRpcTcGetWatchPointVarsOut.instancePaths[i].elems[j].structComponent == 0) {
								path[j] = new Integer(SysRpcTcGetWatchPointVarsOut.instancePaths[i].elems[j].arrayIndex);
							} else {
								path[j] = new TcSysRpcStructuralVarNode(SysRpcTcGetWatchPointVarsOut.instancePaths[i].elems[j].structComponent, client);
							}
						}
						vs[i] = new WatchpointVarNode(SysRpcTcGetWatchPointVarsOut.wpVarHnds[i], path, new TcSysRpcExecutionUnit(
								SysRpcTcGetWatchPointVarsOut.exeUnitHnds[i], client));
					}
					return vs;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - getWatchpointVariables: ");
		}
		return null;
	}

	/**
	 * Returns a list of routines which have a codepoint set.
	 * 
	 * @param force
	 *            ture to get a list anyway
	 * @return list of routines
	 */
	Object SysRpcTcGetCodePointRoutinesIn = new Object();
	public TcCodePointRoutineList getCodePointRoutines(int lastChangeCount) {
		try {
			synchronized (SysRpcTcGetCodePointRoutinesIn) {
				client.client.SysRpcTcGetCodePointRoutines_1(SysRpcTcGetCodePointRoutinesOut);
				if (SysRpcTcGetCodePointRoutinesOut.retVal) {
					if (lastChangeCount != SysRpcTcGetCodePointRoutinesOut.chgCnt) {
						TcCodePointRoutineList list = new TcCodePointRoutineList();
						list.changeCount = SysRpcTcGetCodePointRoutinesOut.chgCnt;
						list.routines = new TcStructuralRoutineNode[SysRpcTcGetCodePointRoutinesOut.routineScopeHnd_count];
						for (int i = 0; i < SysRpcTcGetCodePointRoutinesOut.routineScopeHnd_count; i++) {
							TcSysRpcStructuralNode n = (TcSysRpcStructuralNode) client.cache.getFromCache(SysRpcTcGetCodePointRoutinesOut.routineScopeHnd[i]);
							if (n instanceof TcSysRpcStructuralRoutineNode) {
								list.routines[i] = (TcSysRpcStructuralRoutineNode) n;
							} else {
								list.routines[i] = new TcSysRpcStructuralRoutineNode(SysRpcTcGetCodePointRoutinesOut.routineScopeHnd[i], client);
								client.cache.putToCache((TcSysRpcStructuralRoutineNode) list.routines[i]);
							}
						}
						return list;
					}
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - getCodePointRoutines: ");
		}
		return null;

	}

	/**
	 * Liefert die CodePoints für die angegebene Routine.
	 * 
	 * @param routine
	 *            Routine
	 * 
	 * @return true für den erfolgreichen Aufruf
	 */
	public TcCodePoint[] getCodePoints(TcStructuralRoutineNode routine) {
		try {
			synchronized (SysRpcTcGetCodePointsIn) {
				SysRpcTcGetCodePointsIn.routineScopeHnd = ((TcSysRpcStructuralRoutineNode) routine).getHandle();
				client.client.SysRpcTcGetCodePoints_1(SysRpcTcGetCodePointsIn, SysRpcTcGetCodePointsOut);
				if (SysRpcTcGetCodePointsOut.retVal) {
					TcCodePoint[] codePoints = new TcCodePoint[SysRpcTcGetCodePointsOut.nrCodePoints];
					for (int i = 0; i < SysRpcTcGetCodePointsOut.nrCodePoints; i++) {
						codePoints[i] = new TcCodePoint();
						codePoints[i].lineNr = SysRpcTcGetCodePointsOut.lineNrs[i];
						codePoints[i].kind = SysRpcTcGetCodePointsOut.kinds[i].value;
						codePoints[i].isEnabled = SysRpcTcGetCodePointsOut.active[i];
					}
					return codePoints;
				}
			}
		} catch (Exception e) {
			disconnected("Disconnect in TcExecutionModel - step: ");
		}
		return null;
	}

	/**
	 * Creates a porject, program, variable, routine, type or constant node.
	 * 
	 * @param handle
	 *            handle of the node
	 * @return new created node
	 */
	TcStructuralAbstractNode createNode(int handle) {
		if (handle != 0) {
			try {
				synchronized (SysRpcTcGetNodeInfoIn) {
					SysRpcTcGetNodeInfoIn.scopeHnd = handle;
					client.client.SysRpcTcGetNodeInfo_1(SysRpcTcGetNodeInfoIn, SysRpcTcGetNodeInfoOut);
					if (SysRpcTcGetNodeInfoOut.retVal) {
						TcSysRpcStructuralNode node;
						switch (SysRpcTcGetNodeInfoOut.info.kind.value) {
						case TcStructuralNode.ROUTINE:
							node = new TcSysRpcStructuralRoutineNode(handle, client);
							break;
						case TcStructuralNode.TYPE:
							node = new TcSysRpcStructuralTypeNode(handle, client);
							break;
						case TcStructuralNode.VAR:
							node = new TcSysRpcStructuralVarNode(handle, client);
							break;
						case TcStructuralNode.CONST:
							node = new TcSysRpcStructuralConstNode(handle, client);
							break;
						default:
							node = new TcSysRpcStructuralNode(handle, client);
						}
						node.setInfo(SysRpcTcGetNodeInfoOut.info, null);
						return node;
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcStructuralNode - createNode: ");
			}
		}
		return null;
	}

	/**
	 * Variablen - Zugriffs - Handle
	 */
	public static class AccessHandle extends TcAccessHandle {
		protected SysRpcTcVarAccess access;

		protected AccessHandle(int typeKind) {
			this.typeKind = typeKind;
		}
		
      protected void setAttributesValid () {
      	hasValidAttributes = true;
      }

		protected void setTypeKind(int typeKind) {
			this.typeKind = typeKind;
		}
		protected void setReadOnly (boolean isReadOnly) {
   		this.isReadOnly = isReadOnly;
   	}
		protected void setConstant (boolean isConstant) {
   		this.isConstant = isConstant;
   	}
		protected void setUser (boolean isUser) {
   		this.isUser = isUser;
   	}
		protected void setPrivate (boolean isPrivate) {
   		this.isPrivate = isPrivate;
   	}
	}

	/**
	 * Fehlermeldungsobjekt
	 */
	public static class ErrorMessage extends TcErrorMessage {

		private ErrorMessage(SysRpcTcErrorElem e) {
			errorKind = e.errorKind.value;
			errorNr = e.errorNr;
			nrParams = e.nrParams;
			paramKinds = new int[nrParams];
			paramIValues = new int[nrParams];
			paramFValues = new float[nrParams];
			paramSValues = new String[nrParams];
			for (int i = 0; i < nrParams; i++) {
				paramKinds[i] = e.errorParams[i].errorType.value;
				paramIValues[i] = e.errorParams[i].iValue;
				paramFValues[i] = e.errorParams[i].fValue;
				paramSValues[i] = e.errorParams[i].sValue.toString();
			}
		}
	}

	private class StructuralNodeChunkEnumeration implements Enumeration {
		private int kind;
		private TcSysRpcStructuralNode scope;
		private int nrOfHnd;
		private final TcSysRpcStructuralNode[] elems = new TcSysRpcStructuralNode[TCI.rpcChunkLen];
		private int iterHandle;
		private int index;
		private boolean isFirst = true;
		private boolean isValid = false;

		private StructuralNodeChunkEnumeration(TcStructuralNode scope, int kind) {
			this.scope = (TcSysRpcStructuralNode) scope;
			// if ((kind == TcStructuralNode.PROGRAM)
			// || (scope.getKind() == TcStructuralNode.ROUTINE)
			// || (scope.getHandle() == 0)) {
			// // 1. program can contain end user objects => alle programs
			// required
			// // 2. routine have no end user objects
			// // 3. root contains simple types
			// this.kind = kind;
			// } else {
			if (client.getUserMode()) {
				switch (kind) {
				case TcStructuralNode.PROJECT:
					switch (scope.getKind()) {
					case TcStructuralNode.ROOT:
						this.kind = TcStructuralNode.SYSTEM;
						break;
					case TcStructuralNode.SYSTEM:
						this.kind = TcStructuralNode.GLOBAL;
						break;
					default:
						this.kind = kind;
					}
					break;
				case TcStructuralNode.PROGRAM:
					this.kind = FILTER_USER_PROGRAM;
					break;
				case TcStructuralNode.ROUTINE:
					this.kind = FILTER_USER_ROUTINE;
					break;
				case TcStructuralNode.TYPE:
					this.kind = FILTER_USER_TYPE;
					break;
				case TcStructuralNode.VAR:
					this.kind = FILTER_USER_VAR;
//					this.kind = FILTER_ALL_VAR;
					break;
				case TcStructuralNode.CONST:
					this.kind = FILTER_USER_CONST;
					break;
				default:
					this.kind = kind;
				}
			} else {
				if (kind == TcStructuralNode.PROJECT) {
					switch (scope.getKind()) {
					case TcStructuralNode.ROOT:
						this.kind = TcStructuralNode.SYSTEM;
						break;
					case TcStructuralNode.SYSTEM:
						this.kind = TcStructuralNode.GLOBAL;
						break;
					default:
						this.kind = kind;
					}
				} else {
					this.kind = kind;
				}
			}
			// }

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
				index++;
				return elems[index - 1];
			}
			return null;
		}

		private void loadInfo(int[] nodeHnds) throws IOException, RPCException {
			synchronized (SysRpcTcGetNodeInfoListIn) {
				SysRpcTcGetNodeInfoListIn.nrOfScopHnd = nrOfHnd;
				if (SysRpcTcGetNodeInfoListIn.scopeHnd == null) {
					SysRpcTcGetNodeInfoListIn.scopeHnd = new int[TCI.rpcChunkLen];
				}
				System.arraycopy(nodeHnds, 0, SysRpcTcGetNodeInfoListIn.scopeHnd, 0, nrOfHnd);
				SysRpcTcGetNodeInfoListIn.scopeHnd_count = nrOfHnd;
				client.client.SysRpcTcGetNodeInfoList_1(SysRpcTcGetNodeInfoListIn, SysRpcTcGetNodeInfoListOut);
				if (SysRpcTcGetNodeInfoListOut.retVal) {
					switch (kind) {
					case TcStructuralNode.ROUTINE:
					case FILTER_USER_ROUTINE: {
						SysRpcTcGetRoutineInfoListIn.nrOfRoutineScopeHnd = nrOfHnd;
						if (SysRpcTcGetRoutineInfoListIn.routineScopeHnd == null) {
							SysRpcTcGetRoutineInfoListIn.routineScopeHnd = new int[TCI.rpcChunkLen];
						}
						System.arraycopy(nodeHnds, 0, SysRpcTcGetRoutineInfoListIn.routineScopeHnd, 0, nrOfHnd);
						SysRpcTcGetRoutineInfoListIn.routineScopeHnd_count = nrOfHnd;
						client.client.SysRpcTcGetRoutineInfoList_1(SysRpcTcGetRoutineInfoListIn, SysRpcTcGetRoutineInfoListOut);
						if (SysRpcTcGetRoutineInfoListOut.retVal) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcSysRpcStructuralRoutineNode(nodeHnds[i], client);
								elems[i].setInfo(SysRpcTcGetNodeInfoListOut.infos[i], scope);
								((TcSysRpcStructuralRoutineNode) elems[i]).setRoutineInfo(SysRpcTcGetRoutineInfoListOut.infos[i]);
								client.cache.putToCache(elems[i]);
							}
							return;
						}
					}
						break;
					case TcStructuralNode.TYPE:
					case FILTER_USER_TYPE: {
						SysRpcTcGetTypeInfoListIn.nrOfTypeScopeHnd = nrOfHnd;
						if (SysRpcTcGetTypeInfoListIn.typeScopeHnd == null) {
							SysRpcTcGetTypeInfoListIn.typeScopeHnd = new int[TCI.rpcChunkLen];
						}
						System.arraycopy(nodeHnds, 0, SysRpcTcGetTypeInfoListIn.typeScopeHnd, 0, nrOfHnd);
						SysRpcTcGetTypeInfoListIn.typeScopeHnd_count = nrOfHnd;
						client.client.SysRpcTcGetTypeInfoList_1(SysRpcTcGetTypeInfoListIn, SysRpcTcGetTypeInfoListOut);
						if (SysRpcTcGetTypeInfoListOut.retVal) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = (TcSysRpcStructuralNode) client.cache.getFromCache(nodeHnds[i]);
								if ((elems[i] == null) || !(elems[i] instanceof TcSysRpcStructuralTypeNode)) {
									elems[i] = new TcSysRpcStructuralTypeNode(nodeHnds[i], client);
									client.cache.putToCache(elems[i]);
								}
								if ((SysRpcTcGetTypeInfoListOut.infos[i].kind.value == TcStructuralTypeNode.UNIT_TYPE)
										&& (SysRpcTcGetTypeInfoListOut.infos[i].baseTypeHnd == 0)) {
									client.cache.putToDirEntryCache(elems[i]);
								}
								elems[i].setInfo(SysRpcTcGetNodeInfoListOut.infos[i], scope);
								((TcSysRpcStructuralTypeNode) elems[i]).setTypeInfo(SysRpcTcGetTypeInfoListOut.infos[i]);
							}

							// load base type infos
							int counter = 0;
							for (int i = 0; i < nrOfHnd; i++) {
								int typeKind = ((TcSysRpcStructuralTypeNode) elems[i]).typeKind;
								TcSysRpcStructuralTypeNode baseType = (TcSysRpcStructuralTypeNode) ((TcSysRpcStructuralTypeNode) elems[i]).getBaseType();
								if (((typeKind == TcStructuralTypeNode.ENUM_TYPE) || (typeKind == TcStructuralTypeNode.STRUCT_TYPE) || (typeKind == TcSysRpcStructuralTypeNode.VARIANT_TYPE))
										&& (baseType != null) && (baseType.getName() == null)) {
									nodeHnds[counter] = i;
									SysRpcTcGetNodeInfoListIn.scopeHnd[counter] = baseType.getHandle();
									counter++;
								}
							}
							if (0 < counter) {
								SysRpcTcGetNodeInfoListIn.nrOfScopHnd = counter;
								SysRpcTcGetNodeInfoListIn.scopeHnd_count = counter;
								client.client.SysRpcTcGetNodeInfoList_1(SysRpcTcGetNodeInfoListIn, SysRpcTcGetNodeInfoListOut);
								if (SysRpcTcGetNodeInfoListOut.retVal) {
									for (int i = 0; i < counter; i++) {
										TcSysRpcStructuralTypeNode baseType = (TcSysRpcStructuralTypeNode) ((TcSysRpcStructuralTypeNode) elems[nodeHnds[i]])
												.getBaseType();
										baseType.setInfo(SysRpcTcGetNodeInfoListOut.infos[i], null);
									}
									return;
								}
							} else {
								return;
							}
						}
					}
						break;
					case TcStructuralNode.VAR:
					case FILTER_USER_VAR: {
						SysRpcTcGetVarInfoListIn.nrOfVarScopeHnd = nrOfHnd;
						if (SysRpcTcGetVarInfoListIn.varScopeHnd == null) {
							SysRpcTcGetVarInfoListIn.varScopeHnd = new int[TCI.rpcChunkLen];
						}
						System.arraycopy(nodeHnds, 0, SysRpcTcGetVarInfoListIn.varScopeHnd, 0, nrOfHnd);
						SysRpcTcGetVarInfoListIn.varScopeHnd_count = nrOfHnd;
						client.client.SysRpcTcGetVarInfoList_1(SysRpcTcGetVarInfoListIn, SysRpcTcGetVarInfoListOut);
						if (SysRpcTcGetVarInfoListOut.retVal) {
							// var infos loaded
							int nrOfVars = nrOfHnd;
							for (int i = 0; i < nrOfVars; i++) {
								elems[i] = new TcSysRpcStructuralVarNode(nodeHnds[i], client);
								elems[i].setInfo(SysRpcTcGetNodeInfoListOut.infos[i], scope);
							}

							// ********
							// load all type infos for all not already loaded
							// types
							int counter = 0;
							for (int i = 0; i < nrOfVars; i++) {
								int typeHnd = SysRpcTcGetVarInfoListOut.infos[i].typeHnd;
								if (client.cache.getFromCache(typeHnd) == null) {
									SysRpcTcGetNodeInfoListIn.scopeHnd[counter] = typeHnd;
									counter++;
								}
							}
							int bc = 0;
							if (0 < counter) {
								// load node info
								SysRpcTcGetNodeInfoListIn.nrOfScopHnd = counter;
								SysRpcTcGetNodeInfoListIn.scopeHnd_count = counter;
								client.client.SysRpcTcGetNodeInfoList_1(SysRpcTcGetNodeInfoListIn, SysRpcTcGetNodeInfoListOut);
								if (SysRpcTcGetNodeInfoListOut.retVal) {
									// load type info
									SysRpcTcGetTypeInfoListIn.nrOfTypeScopeHnd = counter;
									SysRpcTcGetTypeInfoListIn.typeScopeHnd_count = counter;
									if (SysRpcTcGetTypeInfoListIn.typeScopeHnd == null) {
										SysRpcTcGetTypeInfoListIn.typeScopeHnd = new int[TCI.rpcChunkLen];
									}
									System.arraycopy(SysRpcTcGetNodeInfoListIn.scopeHnd, 0, SysRpcTcGetTypeInfoListIn.typeScopeHnd, 0, counter);
									client.client.SysRpcTcGetTypeInfoList_1(SysRpcTcGetTypeInfoListIn, SysRpcTcGetTypeInfoListOut);
									if (SysRpcTcGetTypeInfoListOut.retVal) {
										for (int i = 0; i < counter; i++) {
											TcSysRpcStructuralTypeNode tn = new TcSysRpcStructuralTypeNode(SysRpcTcGetNodeInfoListIn.scopeHnd[i], client);
											tn.setInfo(SysRpcTcGetNodeInfoListOut.infos[i], null);
											if ((SysRpcTcGetTypeInfoListOut.infos[i].kind.value == TcStructuralTypeNode.MAPTO_TYPE)
													&& (SysRpcTcGetTypeInfoListOut.infos[i].baseTypeHnd != 0)
													&& (client.cache.getFromCache(SysRpcTcGetTypeInfoListOut.infos[i].baseTypeHnd) == null)) {
												SysRpcTcGetNodeInfoListIn.scopeHnd[bc] = SysRpcTcGetTypeInfoListOut.infos[i].baseTypeHnd;
												bc++;
											}
											tn.setTypeInfo(SysRpcTcGetTypeInfoListOut.infos[i]);
											client.cache.putToCache(tn);
										}
									}
								}
							}

							// ********
							// *********
							// load all base types for mapto types
							// bc = 0;
							if (0 < bc) {
								SysRpcTcGetNodeInfoListIn.nrOfScopHnd = bc;
								SysRpcTcGetNodeInfoListIn.scopeHnd_count = bc;
								client.client.SysRpcTcGetNodeInfoList_1(SysRpcTcGetNodeInfoListIn, SysRpcTcGetNodeInfoListOut);
								if (SysRpcTcGetNodeInfoListOut.retVal) {
									// load type info
									SysRpcTcGetTypeInfoListIn.nrOfTypeScopeHnd = bc;
									SysRpcTcGetTypeInfoListIn.typeScopeHnd_count = bc;
									if (SysRpcTcGetTypeInfoListIn.typeScopeHnd == null) {
										SysRpcTcGetTypeInfoListIn.typeScopeHnd = new int[TCI.rpcChunkLen];
									}
									System.arraycopy(SysRpcTcGetNodeInfoListIn.scopeHnd, 0, SysRpcTcGetTypeInfoListIn.typeScopeHnd, 0, bc);
									client.client.SysRpcTcGetTypeInfoList_1(SysRpcTcGetTypeInfoListIn, SysRpcTcGetTypeInfoListOut);
									if (SysRpcTcGetTypeInfoListOut.retVal) {
										for (int i = 0; i < bc; i++) {
											TcSysRpcStructuralNode tn = (TcSysRpcStructuralNode) client.cache.getFromCache(SysRpcTcGetNodeInfoListIn.scopeHnd[i]);
											if ((tn != null) && (tn instanceof TcSysRpcStructuralTypeNode)) {
												tn.setInfo(SysRpcTcGetNodeInfoListOut.infos[i], null);
												((TcSysRpcStructuralTypeNode) tn).setTypeInfo(SysRpcTcGetTypeInfoListOut.infos[i]);
											}
										}
									}
								}
							}

							// **********
							for (int i = 0; i < nrOfVars; i++) {
								((TcSysRpcStructuralVarNode) elems[i]).setVarInfo(SysRpcTcGetVarInfoListOut.infos[i]);
							}
							return;
						}
						break;
					}
					case TcStructuralNode.CONST:
					case FILTER_USER_CONST: {
						SysRpcTcGetConstInfoListIn.nrOfConstScopeHnd = nrOfHnd;
						if (SysRpcTcGetConstInfoListIn.constScopeHnd == null) {
							SysRpcTcGetConstInfoListIn.constScopeHnd = new int[TCI.rpcChunkLen];
						}
						System.arraycopy(nodeHnds, 0, SysRpcTcGetConstInfoListIn.constScopeHnd, 0, nrOfHnd);
						SysRpcTcGetConstInfoListIn.constScopeHnd_count = nrOfHnd;
						client.client.SysRpcTcGetConstInfoList_1(SysRpcTcGetConstInfoListIn, SysRpcTcGetConstInfoListOut);
						if (SysRpcTcGetConstInfoListOut.retVal) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = (TcSysRpcStructuralNode) client.cache.getFromCache(nodeHnds[i]);
								if ((elems[i] == null) || !(elems[i] instanceof TcSysRpcStructuralConstNode)) {
									elems[i] = new TcSysRpcStructuralConstNode(nodeHnds[i], client);
									client.cache.putToCache(elems[i]);
								}
								elems[i].setInfo(SysRpcTcGetNodeInfoListOut.infos[i], scope);
								((TcSysRpcStructuralConstNode) elems[i]).setConstInfo(SysRpcTcGetConstInfoListOut.infos[i]);
							}
							return;
						}
						break;
					}
					default:
						for (int i = 0; i < nrOfHnd; i++) {
							elems[i] = (TcSysRpcStructuralNode) client.cache.getFromDirEntryCache(nodeHnds[i]);
							if ((elems[i] == null) || (elems[i] instanceof TcSysRpcStructuralTypeNode) || (elems[i] instanceof TcSysRpcStructuralConstNode)) {
								elems[i] = new TcSysRpcStructuralNode(nodeHnds[i], client);
								client.cache.putToDirEntryCache(elems[i]);
								elems[i].setInfo(SysRpcTcGetNodeInfoListOut.infos[i], scope);
							} else {
								elems[i].setInfo(SysRpcTcGetNodeInfoListOut.infos[i], scope);
							}
						}
						return;
					}
				}
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}

		private void getFirstChunk() {
			try {
				synchronized (SysRpcTcGetFirstNodeChunkIn) {
					SysRpcTcGetFirstNodeChunkIn.scopeHnd = scope.getHandle();
					SysRpcTcGetFirstNodeChunkIn.kind.value = kind;
					client.client.SysRpcTcGetFirstNodeChunk_1(SysRpcTcGetFirstNodeChunkIn, SysRpcTcGetFirstNodeChunkOut);
					if (SysRpcTcGetFirstNodeChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetFirstNodeChunkOut.nrOfHnd;
						iterHandle = SysRpcTcGetFirstNodeChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							loadInfo(SysRpcTcGetFirstNodeChunkOut.nodeHnd);
							return;
						} else if (kind == TcStructuralNode.SYSTEM) {
							// try global project
							SysRpcTcGetFirstNodeChunkIn.kind.value = TcStructuralNode.GLOBAL;
							client.client.SysRpcTcGetFirstNodeChunk_1(SysRpcTcGetFirstNodeChunkIn, SysRpcTcGetFirstNodeChunkOut);
							if (SysRpcTcGetFirstNodeChunkOut.retVal) {
								kind = TcStructuralNode.GLOBAL;
								index = 0;
								nrOfHnd = SysRpcTcGetFirstNodeChunkOut.nrOfHnd;
								iterHandle = SysRpcTcGetFirstNodeChunkOut.iterHnd;
								if (0 < nrOfHnd) {
									loadInfo(SysRpcTcGetFirstNodeChunkOut.nodeHnd);
									return;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcStructuralModel - StructuralNodeChunkEnumeration - getFirstChunk: ");
				e.printStackTrace();
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}

		private void getNextChunk() {
			try {
				synchronized (SysRpcTcGetNextNodeChunkIn) {
					SysRpcTcGetNextNodeChunkIn.iterHnd = iterHandle;
					SysRpcTcGetNextNodeChunkIn.scopeHnd = scope.getHandle();
					SysRpcTcGetNextNodeChunkIn.kind.value = kind;
					client.client.SysRpcTcGetNextNodeChunk_1(SysRpcTcGetNextNodeChunkIn, SysRpcTcGetNextNodeChunkOut);
					if (SysRpcTcGetNextNodeChunkOut.retVal) {
						index = 0;
						nrOfHnd = SysRpcTcGetNextNodeChunkOut.nrOfHnd;
						iterHandle = SysRpcTcGetNextNodeChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							loadInfo(SysRpcTcGetNextNodeChunkOut.nodeHnd);
							return;
						}
					}
				}
			} catch (Exception e) {
				disconnected("Disconnect in TcStructuralModel - StructuralNodeChunkEnumeration - getNextChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}
	}

	static class WatchpointVarNode extends TcWatchpointVarNode {
		protected WatchpointVarNode(int handle, Object[] path, TcExecutionUnit execUnit) {
			super(handle, path, execUnit);
		}

		protected int getHandle() {
			return handle;
		}
	}

	public TcAccessHandle getVoidAccessHandle() {
		AccessHandle accessHandle = new AccessHandle(0);
		accessHandle.access = new SysRpcTcVarAccess();
		accessHandle.access.index = 0;
		accessHandle.access.typeHandle = 0;
		accessHandle.access.varHandle = 0;
		accessHandle.access.offsets_count = 1;
		return accessHandle;
	}

}
