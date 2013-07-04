package com.keba.kemro.teach.network.rpc;

import java.io.*;
import java.util.*;

import com.keba.jrpc.rpc.*;
//import com.keba.kemro.kvs.teach.framework.util.KvtLogger;
import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.base.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcStructuralModel implements TcStructuralModel {
	/** Iteration filter for all nodes */
	// private static final int FILTER_ALL = RpcTcNodeKind.rpcFilterAllNode;
	/** Iteration filter for all user program nodes */
	private static final int FILTER_USER_PROGRAM = RpcTcNodeKind.rpcFilterProgramUserNode;
	/** Iteration filter for all user type nodes */
	private static final int FILTER_USER_TYPE = RpcTcNodeKind.rpcFilterTypeUserNode;
	/** Iteration filter for all user routine nodes */
	private static final int FILTER_USER_ROUTINE = RpcTcNodeKind.rpcFilterRoutineUserNode;
	/** Iteration filter for all user variable nodes */
	private static final int FILTER_USER_VAR = RpcTcNodeKind.rpcFilterVariableUserNode;
	/** Iteration filter for all user constant nodes */
	private static final int FILTER_USER_CONST = RpcTcNodeKind.rpcFilterConstantUserNode;
	/** Iteration filter for all user nodes */
	// private static final int FILTER_ALL_USER =
	// RpcTcNodeKind.rpcFilterAllUserNode;

	private final RpcTcBuildIn rpcTcBuildIn = new RpcTcBuildIn();
	private final RpcTcBuildOut rpcTcBuildOut = new RpcTcBuildOut();
	private final RpcTcDestroyIn rpcTcDestroyIn = new RpcTcDestroyIn();
	private final RpcTcDestroyOut rpcTcDestroyOut = new RpcTcDestroyOut();
	private final RpcTcGetVarAccessIn rpcTcGetVarAccessIn = new RpcTcGetVarAccessIn();
	private final RpcTcGetVarAccessOut rpcTcGetVarAccessOut = new RpcTcGetVarAccessOut();
	private final RpcTcGetStructElemAccessIn rpcTcGetStructElemAccessIn = new RpcTcGetStructElemAccessIn();
	private final RpcTcGetStructElemAccessOut rpcTcGetStructElemAccessOut = new RpcTcGetStructElemAccessOut();
	private final RpcTcGetArrayElemAccessIn rpcTcGetArrayElemAccessIn = new RpcTcGetArrayElemAccessIn();
	private final RpcTcGetArrayElemAccessOut rpcTcGetArrayElemAccessOut = new RpcTcGetArrayElemAccessOut();
	private final RpcTcGetNodeInfoListIn rpcTcGetNodeInfoListIn = new RpcTcGetNodeInfoListIn();
	private final RpcTcGetNodeInfoListOut rpcTcGetNodeInfoListOut = new RpcTcGetNodeInfoListOut();
	private final RpcTcGetFirstNodeChunkIn rpcTcGetFirstNodeChunkIn = new RpcTcGetFirstNodeChunkIn();
	private final RpcTcGetFirstNodeChunkOut rpcTcGetFirstNodeChunkOut = new RpcTcGetFirstNodeChunkOut();
	private final RpcTcGetNextNodeChunkIn rpcTcGetNextNodeChunkIn = new RpcTcGetNextNodeChunkIn();
	private final RpcTcGetNextNodeChunkOut rpcTcGetNextNodeChunkOut = new RpcTcGetNextNodeChunkOut();
	private final RpcTcGetRoutineInfoListIn rpcTcGetRoutineInfoListIn = new RpcTcGetRoutineInfoListIn();
	private final RpcTcGetRoutineInfoListOut rpcTcGetRoutineInfoListOut = new RpcTcGetRoutineInfoListOut();
	private final RpcTcGetTypeInfoListIn rpcTcGetTypeInfoListIn = new RpcTcGetTypeInfoListIn();
	private final RpcTcGetTypeInfoListOut rpcTcGetTypeInfoListOut = new RpcTcGetTypeInfoListOut();
	private final RpcTcGetVarInfoListIn rpcTcGetVarInfoListIn = new RpcTcGetVarInfoListIn();
	private final RpcTcGetVarInfoListOut rpcTcGetVarInfoListOut = new RpcTcGetVarInfoListOut();
	private final RpcTcGetConstInfoListIn rpcTcGetConstInfoListIn = new RpcTcGetConstInfoListIn();
	private final RpcTcGetConstInfoListOut rpcTcGetConstInfoListOut = new RpcTcGetConstInfoListOut();
	private final RpcTcWriteDataIn rpcTcWriteDataIn = new RpcTcWriteDataIn();
	private final RpcTcWriteDataOut rpcTcWriteDataOut = new RpcTcWriteDataOut();
	private final RpcTcAddVarNodeIn rpcTcAddVarNodeIn = new RpcTcAddVarNodeIn();
	private final RpcTcAddVarNodeOut rpcTcAddVarNodeOut = new RpcTcAddVarNodeOut();
	private final RpcTcRemoveNodeIn rpcTcRemoveNodeIn = new RpcTcRemoveNodeIn();
	private final RpcTcRemoveNodeOut rpcTcRemoveNodeOut = new RpcTcRemoveNodeOut();
	private final RpcTcOpenNodeIn rpcTcOpenNodeIn = new RpcTcOpenNodeIn();
	private final RpcTcOpenNodeOut rpcTcOpenNodeOut = new RpcTcOpenNodeOut();
	private final RpcTcOpenVarAccessIn rpcTcOpenVarAccessIn = new RpcTcOpenVarAccessIn();
	private final RpcTcOpenVarAccessOut rpcTcOpenVarAccessOut = new RpcTcOpenVarAccessOut();
	private final RpcTcOpenVarAccessListIn rpcTcOpenVarAccessListIn = new RpcTcOpenVarAccessListIn();
	private final RpcTcOpenVarAccessListOut rpcTcOpenVarAccessListOut = new RpcTcOpenVarAccessListOut();
	private final RpcTcAddRoutineNodeIn rpcTcAddRoutineNodeIn = new RpcTcAddRoutineNodeIn();
	private final RpcTcAddRoutineNodeOut rpcTcAddRoutineNodeOut = new RpcTcAddRoutineNodeOut();
	private final RpcTcGetErrorsIn rpcTcGetErrorsIn = new RpcTcGetErrorsIn();
	private final RpcTcGetErrorsOut rpcTcGetErrorsOut = new RpcTcGetErrorsOut();
	private final RpcTcWriteInitValueIn rpcTcWriteInitValueIn = new RpcTcWriteInitValueIn();
	private final RpcTcWriteInitValueOut rpcTcWriteInitValueOut = new RpcTcWriteInitValueOut();
	private final RpcTcAddProgramNodeIn rpcTcAddProgramNodeIn = new RpcTcAddProgramNodeIn();
	private final RpcTcAddProgramNodeOut rpcTcAddProgramNodeOut = new RpcTcAddProgramNodeOut();
	private final RpcTcGetAttributesIn rpcTcGetAttributesIn = new RpcTcGetAttributesIn();
	private final RpcTcGetAttributesOut rpcTcGetAttributesOut = new RpcTcGetAttributesOut();
	private final RpcTcSetAttributesIn rpcTcSetAttributesIn = new RpcTcSetAttributesIn();
	private final RpcTcSetAttributesOut rpcTcSetAttributesOut = new RpcTcSetAttributesOut();
	private final RpcTcGetNodeInfoIn rpcTcGetNodeInfoIn = new RpcTcGetNodeInfoIn();
	private final RpcTcGetNodeInfoOut rpcTcGetNodeInfoOut = new RpcTcGetNodeInfoOut();
	private final RpcTcBuildStartIn rpcTcBuildStartIn = new RpcTcBuildStartIn();
	private final RpcTcBuildStartOut rpcTcBuildStartOut = new RpcTcBuildStartOut();
	private final RpcTcRenameNodeIn rpcTcRenameNodeIn = new RpcTcRenameNodeIn();
	private final RpcTcRenameNodeOut rpcTcRenameNodeOut = new RpcTcRenameNodeOut();
	private final RpcTcReadNodeChangeIn rpcTcReadNodeChangeIn = new RpcTcReadNodeChangeIn();
	private final RpcTcReadNodeChangeOut rpcTcReadNodeChangeOut = new RpcTcReadNodeChangeOut();
	private final RpcTcMoveVarIn rpcTcMoveVarIn = new RpcTcMoveVarIn();
	private final RpcTcMoveVarOut rpcTcMoveVarOut = new RpcTcMoveVarOut();
	private final RpcTcSetCodePointIn rpcTcSetCodePointIn = new RpcTcSetCodePointIn();
	private final RpcTcSetCodePointOut rpcTcSetCodePointOut = new RpcTcSetCodePointOut();
	private final RpcTcActivateCodePointIn rpcTcActivateCodePointIn = new RpcTcActivateCodePointIn();
	private final RpcTcActivateCodePointOut rpcTcActivateCodePointOut = new RpcTcActivateCodePointOut();
	private final RpcTcRemoveCodePointIn rpcTcRemoveCodePointIn = new RpcTcRemoveCodePointIn();
	private final RpcTcRemoveCodePointOut rpcTcRemoveCodePointOut = new RpcTcRemoveCodePointOut();
	private final RpcTcGetWatchPointCounterIn rpcTcGetWatchPointCounterIn = new RpcTcGetWatchPointCounterIn();
	private final RpcTcGetWatchPointCounterOut rpcTcGetWatchPointCounterOut = new RpcTcGetWatchPointCounterOut();
	private final RpcTcResetWatchPointCounterIn rpcTcResetWatchPointCounterIn = new RpcTcResetWatchPointCounterIn();
	private final RpcTcResetWatchPointCounterOut rpcTcResetWatchPointCounterOut = new RpcTcResetWatchPointCounterOut();
	private final RpcTcSetWatchPointVarIn rpcTcSetWatchPointVarIn = new RpcTcSetWatchPointVarIn();
	private final RpcTcSetWatchPointVarOut rpcTcSetWatchPointVarOut = new RpcTcSetWatchPointVarOut();
	private final RpcTcRemoveWatchPointVarIn rpcTcRemoveWatchPointVarIn = new RpcTcRemoveWatchPointVarIn();
	private final RpcTcRemoveWatchPointVarOut rpcTcRemoveWatchPointVarOut = new RpcTcRemoveWatchPointVarOut();
	private final RpcTcGetWatchPointValueIn rpcTcGetWatchPointValueIn = new RpcTcGetWatchPointValueIn();
	private final RpcTcGetWatchPointValueOut rpcTcGetWatchPointValueOut = new RpcTcGetWatchPointValueOut();
	private final RpcTcGetWatchPointVarsIn rpcTcGetWatchPointVarsIn = new RpcTcGetWatchPointVarsIn();
	private final RpcTcGetWatchPointVarsOut rpcTcGetWatchPointVarsOut = new RpcTcGetWatchPointVarsOut();
	private final RpcTcGetCodePointsIn rpcTcGetCodePointsIn = new RpcTcGetCodePointsIn();
	private final RpcTcGetCodePointsOut rpcTcGetCodePointsOut = new RpcTcGetCodePointsOut();
	private final RpcTcGetCodePointRoutinesOut rpcTcGetCodePointRoutinesOut = new RpcTcGetCodePointRoutinesOut();

	private final RpcTcConvertDirEntryPathIn rpcTcConvertDirEntryPathIn = new RpcTcConvertDirEntryPathIn();
	private final RpcTcConvertDirEntryPathOut rpcTcConvertDirEntryPathOut = new RpcTcConvertDirEntryPathOut();
	private final RpcTcConvertScopeHandleToDirEntryPathIn rpcTcConvertScopeHandleToDirEntryPathIn = new RpcTcConvertScopeHandleToDirEntryPathIn();
	private final RpcTcConvertScopeHandleToDirEntryPathOut rpcTcConvertScopeHandleToDirEntryPathOut = new RpcTcConvertScopeHandleToDirEntryPathOut();

	private final RpcTcOpenSyntaxEditorIn rpcTcOpenSyntaxEditorIn = new RpcTcOpenSyntaxEditorIn();
	private final RpcTcOpenSyntaxEditorOut rpcTcOpenSyntaxEditorOut = new RpcTcOpenSyntaxEditorOut();
	private final RpcTcInsertStatementIn rpcTcInsertStatementIn = new RpcTcInsertStatementIn();
	private final RpcTcInsertStatementOut rpcTcInsertStatementOut = new RpcTcInsertStatementOut();
	private final RpcTcDeleteStatementIn rpcTcDeleteStatementIn = new RpcTcDeleteStatementIn();
	private final RpcTcDeleteStatementOut rpcTcDeleteStatementOut = new RpcTcDeleteStatementOut();
	private final RpcTcReplaceStatementIn rpcTcReplaceStatementIn = new RpcTcReplaceStatementIn();
	private final RpcTcReplaceStatementOut rpcTcReplaceStatementOut = new RpcTcReplaceStatementOut();
	private final RpcTcIsCurrentIn rpcTcIsCurrentIn = new RpcTcIsCurrentIn();
	private final RpcTcIsCurrentOut rpcTcIsCurrentOut = new RpcTcIsCurrentOut();

	static {
		/** Breakpoint */
		TcCodePoint.BREAKPOINT = RpcTcCodePointKind.rpcBreakPoint;
		/** Watchpoint */
		TcCodePoint.WATCHPOINT = RpcTcCodePointKind.rpcWatchPoint;
		/** Main flow breakpoint */
		TcCodePoint.MAIN_FLOW_BREAKPOINT = RpcTcCodePointKind.rpcBreakPointMain;

	}

	TcRpcClient client;

	protected TcRpcStructuralModel(TcRpcClient client) {
		this.client = client;
	}
	


	public TcStructuralNode build(TcDirEntry dirEntry) {
		try {
			TcRpcStructuralNode oldProject = (TcRpcStructuralNode) getNode(dirEntry);
			TcRpcStructuralNode newProject = null;
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			int branchMajor = client.getBranchMajorVersion();
			int branchMinor = client.getBranchMinorVersion();
			if ((3 <= major) || ((major == 2) && (92 < minor)) || ((major == 2) && (92 == minor) && (2 < branchMajor)) || ((major == 2) && (92 == minor) && (1 == branchMajor) && (3 <= branchMinor))) {
				synchronized (rpcTcBuildStartIn) {
					rpcTcBuildStartIn.dirEntryPath = ((TcRpcDirEntry) dirEntry).getHandle();

					//@ltz Test DÜRR Teachview sometimes looses Tc Controller status --> re-fetch if necessary
					if(!client.isTcController())
						if(!client.setWriteAccess(true)) {
							
							return null;
						}
					
					client.client.RpcTcBuildStart_1(rpcTcBuildStartIn, rpcTcBuildStartOut);
					
//					KvtLogger.error(this, "TcBuildStart returned [" + rpcTcBuildStartOut.retVal + "]");
					
					if (rpcTcBuildStartOut.retVal) {
						int count = 0;
						do {
							count++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
							newProject = (TcRpcStructuralNode) getNode(dirEntry);
//							if(newProject != null)
//								KvtLogger.error(this, "Pass "+count+" - new project is [" + newProject.getName()+ "]");
//							else
//								KvtLogger.error(this, "Pass "+count+" - new project is NULL!");
						} while ((count < 600) && (newProject == null));
					}
				}
			} else {
				synchronized (rpcTcBuildIn) {
					rpcTcBuildIn.dirEntryPath = ((TcRpcDirEntry) dirEntry).getHandle();
					client.client.RpcTcBuild_1(rpcTcBuildIn, rpcTcBuildOut);
					if (rpcTcBuildOut.retVal) {
						newProject = (TcRpcStructuralNode) getNode(dirEntry);
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
			System.out.println("Disconnect in TcStructuralModel - build: ");
//			KvtLogger.error(this,"Disconnect in TcStructuralModel - build: ");
		}
		return null;
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
			synchronized (rpcTcDestroyIn) {
				rpcTcDestroyIn.scopeHnd = ((TcRpcStructuralNode) project).getHandle();
				client.client.RpcTcDestroy_1(rpcTcDestroyIn, rpcTcDestroyOut);
				done = rpcTcDestroyOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - unloadProject: ");
		}
		if (done) {
			client.cache.removeProject((TcRpcStructuralNode) project);
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
			synchronized (rpcTcConvertScopeHandleToDirEntryPathIn) {
				rpcTcConvertScopeHandleToDirEntryPathIn.scopeHnd = ((TcRpcStructuralNode) node).getHandle();
				client.client.RpcTcConvertScopeHandleToDirEntryPath_1(rpcTcConvertScopeHandleToDirEntryPathIn, rpcTcConvertScopeHandleToDirEntryPathOut);
				if (rpcTcConvertScopeHandleToDirEntryPathOut.retVal) {
					if ((node.getKind() == TcStructuralNode.GLOBAL) && (rpcTcConvertScopeHandleToDirEntryPathOut.dirEntryPath.length() == 0)) {
						// older TC: workaround
						return new TcRpcDirEntry(node.getName() + ".tt", client);
					}
					return new TcRpcDirEntry(rpcTcConvertScopeHandleToDirEntryPathOut.dirEntryPath, client);
				} else {
					if ((node.getKind() == TcStructuralNode.PROJECT) || (node.getKind() == TcStructuralNode.GLOBAL) || (node.getKind() == TcStructuralNode.SYSTEM)) {
						client.cache.removeProject((TcRpcStructuralNode) node);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcDirEntry - getConvertTo: ");
		}
		return null;
	}

	public TcStructuralNode getNode(TcDirEntry entry) {
		try {
			synchronized (rpcTcConvertDirEntryPathIn) {
				rpcTcConvertDirEntryPathIn.dirEntryPath = ((TcRpcDirEntry) entry).getHandle();
				client.client.RpcTcConvertDirEntryPath_1(rpcTcConvertDirEntryPathIn, rpcTcConvertDirEntryPathOut);
				if (!rpcTcConvertDirEntryPathOut.retVal && ((TcRpcDirEntry) entry).getHandle().equals("_system.tt")) {
					String path = entry.getDirEntryPath();
					String handle = path.substring(1, path.length());
					rpcTcConvertDirEntryPathIn.dirEntryPath = handle;
					client.client.RpcTcConvertDirEntryPath_1(rpcTcConvertDirEntryPathIn, rpcTcConvertDirEntryPathOut);
				}
				if (rpcTcConvertDirEntryPathOut.retVal) {
					TcStructuralAbstractNode osn = client.cache.getFromCache(rpcTcConvertDirEntryPathOut.scopeHnd);
					if (entry.getKind() == TcDirEntry.UNIT) {
						if ((osn == null) || !(osn instanceof TcRpcStructuralTypeNode)) {
							osn = new TcRpcStructuralTypeNode(rpcTcConvertDirEntryPathOut.scopeHnd, client);
							client.cache.putToCache(osn);
							client.cache.putToDirEntryCache(osn);
						}
						return osn;
					}
					if ((osn == null) || (osn instanceof TcRpcStructuralTypeNode) || (osn instanceof TcRpcStructuralConstNode)) {
						osn = new TcRpcStructuralNode(rpcTcConvertDirEntryPathOut.scopeHnd, client);
						client.cache.putToDirEntryCache(osn);
					}
					return osn;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcDirEntry - getConvertTo: ");
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
			TcRpcStructuralNode parent = null;
			synchronized (rpcTcOpenNodeIn) {
				rpcTcOpenNodeIn.nodePath = "_root";
				client.client.RpcTcOpenNode_1(rpcTcOpenNodeIn, rpcTcOpenNodeOut);
				if (rpcTcOpenNodeOut.retVal) {
					parent = new TcRpcStructuralNode(rpcTcOpenNodeOut.nodeHnd, client);
				} else {
					// may be a older TC version
					parent = (TcRpcStructuralNode) new TcRpcStructuralNode(0, client).getParent();
				}
			}
			while ((parent != null) && (parent.getKind() != TcStructuralNode.ROOT)) {
				parent = (TcRpcStructuralNode) parent.getParent();
			}
			if (parent != null) {
				client.cache.putToDirEntryCache(parent);
			}
			return parent;
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - addRoutine: ");
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
			synchronized (rpcTcGetAttributesIn) {
				rpcTcGetAttributesIn.scopeHnd[0] = ((TcRpcStructuralNode) node).getHandle();
				rpcTcGetAttributesIn.scopeHnd_count = 1;
				client.client.RpcTcGetAttributes_1(rpcTcGetAttributesIn, rpcTcGetAttributesOut);
				if (rpcTcGetAttributesOut.retVal) {
					return rpcTcGetAttributesOut.attributes[0];
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - getAttributes: ");
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
			synchronized (rpcTcGetAttributesIn) {
				String[] attributes = new String[nodes.size()];
				int index = 0;
				while (index < nodes.size()) {
					int end = index + rpcChunkLen.value;
					if (end >= nodes.size()) {
						end = nodes.size();
					}
					for (int i = index; i < end; i++) {
						TcRpcStructuralNode sn = (TcRpcStructuralNode) nodes.elementAt(i);
						rpcTcGetAttributesIn.scopeHnd[i - index] = sn.getHandle();
					}
					rpcTcGetAttributesIn.scopeHnd_count = end - index;
					client.client.RpcTcGetAttributes_1(rpcTcGetAttributesIn, rpcTcGetAttributesOut);
					if (rpcTcGetAttributesOut.retVal) {
						System.arraycopy(rpcTcGetAttributesOut.attributes, 0, attributes, index, end - index);
					} else {
						return null;
					}
					index = index + rpcChunkLen.value;
				}
				return attributes;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - getAttributes: ");
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
			synchronized (rpcTcSetAttributesIn) {
				((TcRpcStructuralNode) node).setHasAttributes(0 < attributes.length());
				rpcTcSetAttributesIn.scopeHnd = ((TcRpcStructuralNode) node).getHandle();
				rpcTcSetAttributesIn.attributes = attributes;
				client.client.RpcTcSetAttributes_1(rpcTcSetAttributesIn, rpcTcSetAttributesOut);
				return rpcTcSetAttributesOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - setAttirbutes: ");
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
				synchronized (rpcTcReadNodeChangeIn) {
					rpcTcReadNodeChangeIn.startChangeCnt = startCounter;
					client.client.RpcTcReadNodeChange_1(rpcTcReadNodeChangeIn, rpcTcReadNodeChangeOut);
					if (rpcTcReadNodeChangeOut.retVal) {
						if (0 < rpcTcReadNodeChangeOut.nrChanges) {
							synchronized (rpcTcGetNodeInfoIn) {
								int nodeHnds = rpcTcReadNodeChangeOut.nrChanges;
								TcStructuralChanges changes = new TcStructuralChanges();
								changes.nodes = new TcRpcStructuralNode[nodeHnds];
								changes.nrOfNodes = 0;
								changes.nextChangeCounter = rpcTcReadNodeChangeOut.changes[nodeHnds - 1].changeCnt + 1;
								changes.hasMoreChanges = nodeHnds == rpcChunkLen.value;

								for (int i = 0; i < nodeHnds; i++) {
									changes.sameChangesLost = changes.sameChangesLost || (startCounter != rpcTcReadNodeChangeOut.changes[i].changeCnt);
									startCounter = rpcTcReadNodeChangeOut.changes[i].changeCnt + 1;
									rpcTcGetNodeInfoIn.scopeHnd = rpcTcReadNodeChangeOut.changes[i].hdl;
									client.client.RpcTcGetNodeInfo_1(rpcTcGetNodeInfoIn, rpcTcGetNodeInfoOut);
									if (rpcTcGetNodeInfoOut.retVal) {
										// node added to the structural tree
										RpcTcNodeInfo info = rpcTcGetNodeInfoOut.info;
										boolean add = !client.getUserMode() || (((info.attr & RpcTcNodeAttr.rpcUserNodeAttr) == RpcTcNodeAttr.rpcUserNodeAttr));
										if (add) {
											int kind = info.kind.value;
											switch (kind) {
											case TcStructuralNode.ROUTINE:
												changes.nodes[changes.nrOfNodes] = new TcRpcStructuralRoutineNode(rpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											case TcStructuralNode.TYPE:
												changes.nodes[changes.nrOfNodes] = new TcRpcStructuralTypeNode(rpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											case TcStructuralNode.VAR:
												changes.nodes[changes.nrOfNodes] = new TcRpcStructuralVarNode(rpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											case TcStructuralNode.CONST:
												changes.nodes[changes.nrOfNodes] = new TcRpcStructuralConstNode(rpcTcReadNodeChangeOut.changes[i].hdl, client);
												break;
											default:
												changes.nodes[changes.nrOfNodes] = new TcRpcStructuralNode(rpcTcReadNodeChangeOut.changes[i].hdl, client);
											}
											((TcRpcStructuralNode) changes.nodes[changes.nrOfNodes]).setInfo(info, null);
											changes.nrOfNodes++;
										}
									} else {
										// node removed from the structural tree
										changes.nodes[changes.nrOfNodes] = new TcRpcStructuralInvalidNode(rpcTcReadNodeChangeOut.changes[i].hdl, client);
										changes.nrOfNodes++;
										TcRpcStructuralNode n = (TcRpcStructuralNode) client.cache.getFromDirEntryCache(rpcTcReadNodeChangeOut.changes[i].hdl);
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
			System.out.println("Disconnect in TcStructuralModel - getChanges: ");
		}
		return null;
	}

	public boolean projectSourcesChanged(TcStructuralNode project) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			int branchMajor = client.getBranchMajorVersion();
			int branchMinor = client.getBranchMinorVersion();
			if ((3 < major) || ((3 == major) && (16 <= minor)) || ((3 == major) && (12 == minor) && (1 == branchMajor) && (1 <= branchMinor))) {
				synchronized (rpcTcIsCurrentIn) {
					rpcTcIsCurrentIn.scopeHnd = ((TcRpcStructuralNode) project).getHandle();
					client.client.RpcTcIsCurrent_1(rpcTcIsCurrentIn, rpcTcIsCurrentOut);
					return !rpcTcIsCurrentOut.retVal;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - projectSourcesChanged: ");
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
			synchronized (rpcTcAddProgramNodeIn) {
				rpcTcAddProgramNodeIn.nodeInfo.kind.value = RpcTcNodeKind.rpcProgramNode;
				rpcTcAddProgramNodeIn.nodeInfo.attr = client.getUserMode() ? RpcTcNodeAttr.rpcUserNodeAttr : 0;
				rpcTcAddProgramNodeIn.nodeInfo.declHnd = ((TcRpcStructuralNode) parent).getHandle();
				rpcTcAddProgramNodeIn.nodeInfo.upperHnd = ((TcRpcStructuralNode) parent).getHandle();
				rpcTcAddProgramNodeIn.nodeInfo.elemName = name;
				client.client.RpcTcAddProgramNode_1(rpcTcAddProgramNodeIn, rpcTcAddProgramNodeOut);
				if (rpcTcAddProgramNodeOut.retVal) {
					/**
					 * @todo workaround: routine handle should be returned
					 */
					String path = name;
					while (parent.getKind() != TcStructuralNode.ROOT) {
						path = parent.getName() + "." + path;
						parent = parent.getParent();
					}
					synchronized (rpcTcOpenNodeIn) {
						rpcTcOpenNodeIn.nodePath = path;
						client.client.RpcTcOpenNode_1(rpcTcOpenNodeIn, rpcTcOpenNodeOut);
						if (rpcTcOpenNodeOut.retVal) {
							TcStructuralNode r = new TcRpcStructuralNode(rpcTcOpenNodeOut.nodeHnd, client);
							return r;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - addProgram: ");
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
	public TcStructuralRoutineNode addRoutine(TcStructuralNode declarationNode, TcStructuralNode parent, String name, byte routineKind, TcStructuralTypeNode returnType, TcStructuralVarNode eventVariable, boolean isPrivate) {
		try {
			synchronized (rpcTcAddRoutineNodeIn) {
				rpcTcAddRoutineNodeIn.nodeInfo.kind.value = RpcTcNodeKind.rpcRoutineNode;
				rpcTcAddRoutineNodeIn.nodeInfo.attr = client.getUserMode() ? RpcTcNodeAttr.rpcUserNodeAttr : 0;
				rpcTcAddRoutineNodeIn.nodeInfo.declHnd = ((TcRpcStructuralNode) declarationNode).getHandle();
				rpcTcAddRoutineNodeIn.nodeInfo.upperHnd = ((TcRpcStructuralNode) parent).getHandle();
				rpcTcAddRoutineNodeIn.nodeInfo.elemName = name;
				rpcTcAddRoutineNodeIn.routInfo.kind.value = routineKind;
				rpcTcAddRoutineNodeIn.routInfo.retTypeHnd = (returnType != null) ? ((TcRpcStructuralNode) returnType).getHandle() : 0;
				rpcTcAddRoutineNodeIn.routInfo.eventVarHnd = (eventVariable != null) ? ((TcRpcStructuralNode) eventVariable).getHandle() : 0;
				rpcTcAddRoutineNodeIn.routInfo.isPrivate = isPrivate;
				client.client.RpcTcAddRoutineNode_1(rpcTcAddRoutineNodeIn, rpcTcAddRoutineNodeOut);
				if (rpcTcAddRoutineNodeOut.retVal) {
					/**
					 * @todo workaround: routine handle should be returned
					 */
					String path = name;
					while (parent.getKind() != TcStructuralNode.ROOT) {
						path = parent.getName() + "." + path;
						parent = parent.getParent();
					}
					synchronized (rpcTcOpenNodeIn) {
						rpcTcOpenNodeIn.nodePath = path;
						client.client.RpcTcOpenNode_1(rpcTcOpenNodeIn, rpcTcOpenNodeOut);
						if (rpcTcOpenNodeOut.retVal) {
							TcRpcStructuralRoutineNode r = new TcRpcStructuralRoutineNode(rpcTcOpenNodeOut.nodeHnd, client);
							return r;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - addRoutine: ");
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
	public TcStructuralVarNode addVariable(TcStructuralNode declarationNode, TcStructuralNode parent, String name, byte varKind, TcStructuralTypeNode type, boolean isProjectSave, boolean isSave, boolean isPrivate, boolean isDynamic) {
		try {
			synchronized (rpcTcAddVarNodeIn) {
				rpcTcAddVarNodeIn.nodeInfo.kind.value = RpcTcNodeKind.rpcVariableNode;
				rpcTcAddVarNodeIn.nodeInfo.attr = ((declarationNode == null) || declarationNode.isUserNode()) ? RpcTcNodeAttr.rpcUserNodeAttr : 0;

				rpcTcAddVarNodeIn.nodeInfo.declHnd = (declarationNode != null) ? ((TcRpcStructuralNode) declarationNode).getHandle() : 0;
				rpcTcAddVarNodeIn.nodeInfo.upperHnd = ((TcRpcStructuralNode) parent).getHandle();
				rpcTcAddVarNodeIn.nodeInfo.elemName = name;
				rpcTcAddVarNodeIn.varInfo.attr = 0;
				rpcTcAddVarNodeIn.varInfo.kind.value = varKind;
				rpcTcAddVarNodeIn.varInfo.typeHnd = ((TcRpcStructuralNode) type).getHandle();
				rpcTcAddVarNodeIn.varInfo.isProjectSave = isProjectSave;
				rpcTcAddVarNodeIn.varInfo.isSave = isSave;
				rpcTcAddVarNodeIn.varInfo.isPrivate = isPrivate;
				if (isDynamic) {
					rpcTcAddVarNodeIn.varInfo.attr |= RpcTcVarAttr.rpcVarAttrIsDynamic;
				}
				client.client.RpcTcAddVarNode_1(rpcTcAddVarNodeIn, rpcTcAddVarNodeOut);
				if (rpcTcAddVarNodeOut.retVal) {
					// TcStructuralVarNode v = new
					// TcStructuralVarNode(rpcTcAddVarNodeOut.varHnd);
					// return v;
					/**
					 * @todo workaround: variable handle should be returned
					 */
					String path = name;
					while (parent.getKind() != TcStructuralNode.ROOT) {
						path = parent.getName() + "." + path;
						parent = parent.getParent();
					}
					synchronized (rpcTcOpenNodeIn) {
						rpcTcOpenNodeIn.nodePath = path;
						client.client.RpcTcOpenNode_1(rpcTcOpenNodeIn, rpcTcOpenNodeOut);
						if (rpcTcOpenNodeOut.retVal) {
							TcRpcStructuralVarNode v = new TcRpcStructuralVarNode(rpcTcOpenNodeOut.nodeHnd, client);
							return v;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - addVariable: ");
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
			synchronized (rpcTcRemoveNodeIn) {
				rpcTcRemoveNodeIn.scopeHnd = ((TcRpcStructuralNode) node).getHandle();
				client.client.RpcTcRemoveNode_1(rpcTcRemoveNodeIn, rpcTcRemoveNodeOut);
				return rpcTcRemoveNodeOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - removeNode: ");
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
			synchronized (rpcTcRenameNodeIn) {
				rpcTcRenameNodeIn.scopeHnd = ((TcRpcStructuralNode) var).getHandle();
				rpcTcRenameNodeIn.newName = name;
				client.client.RpcTcRenameNode_1(rpcTcRenameNodeIn, rpcTcRenameNodeOut);
				if (rpcTcRenameNodeOut.retVal) {
					return new TcRpcStructuralVarNode(((TcRpcStructuralNode) var).getHandle(), client);
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - renameVariable: ");
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
				synchronized (rpcTcMoveVarIn) {
					rpcTcMoveVarIn.varHnd = ((TcRpcStructuralNode) var).getHandle();
					rpcTcMoveVarIn.newScopeHnd = ((TcRpcStructuralNode) dest).getHandle();
					client.client.RpcTcMoveVar_1(rpcTcMoveVarIn, rpcTcMoveVarOut);
					if (rpcTcMoveVarOut.retVal) {
						return new TcRpcStructuralVarNode(rpcTcMoveVarOut.newVarHnd, client);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - rpcTcMoveVarIn: ");
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
			synchronized (rpcTcInsertStatementIn) {
				rpcTcInsertStatementIn.routineHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcInsertStatementIn.pos.line = line;
				rpcTcInsertStatementIn.pos.col = 0;
				rpcTcInsertStatementIn.stmtText = text;
				client.client.RpcTcInsertStatement_1(rpcTcInsertStatementIn, rpcTcInsertStatementOut);
				return rpcTcInsertStatementOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - insertStatementText: ");
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
			synchronized (rpcTcDeleteStatementIn) {
				rpcTcDeleteStatementIn.routineHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcDeleteStatementIn.begPos.line = line;
				rpcTcDeleteStatementIn.begPos.col = 0;
				rpcTcDeleteStatementIn.endPos.line = line + count - 1;
				rpcTcDeleteStatementIn.endPos.col = 0;
				client.client.RpcTcDeleteStatement_1(rpcTcDeleteStatementIn, rpcTcDeleteStatementOut);
				return rpcTcDeleteStatementOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - deleteStatementText: ");
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
			synchronized (rpcTcReplaceStatementIn) {
				rpcTcReplaceStatementIn.routineHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcReplaceStatementIn.begPos.line = line;
				rpcTcReplaceStatementIn.begPos.col = 0;
				rpcTcReplaceStatementIn.endPos.line = line + count - 1;
				rpcTcReplaceStatementIn.endPos.col = 0;
				rpcTcReplaceStatementIn.stmtText = text;
				client.client.RpcTcReplaceStatement_1(rpcTcReplaceStatementIn, rpcTcReplaceStatementOut);
				return rpcTcReplaceStatementOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - replaceStatementText: ");
		}
		return false;
	}

	public TcEditorModel getEditorModel(TcStructuralNode node) {
		try {
			synchronized (rpcTcOpenSyntaxEditorIn) {
				rpcTcOpenSyntaxEditorIn.scopeHnd = ((TcRpcStructuralNode) node).getHandle();
				rpcTcOpenSyntaxEditorIn.part.value = RpcTcPart.rpcAllPart;
				client.client.RpcTcOpenSyntaxEditor_1(rpcTcOpenSyntaxEditorIn, rpcTcOpenSyntaxEditorOut);
				if (rpcTcOpenSyntaxEditorOut.retVal) {
					return new TcRpcEditorModel(rpcTcOpenSyntaxEditorOut.editHnd, client);
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - getEditorModel(): " + e);
		}
		return null;
	}

	public TcAccessHandle getVarAccessHandle(String fullPath) {
		try {
			int major = client.getMajorVersion();
			int minor = client.getMinorVersion();
			if ((major <= 3) && (minor < 18) || (major < 3)) {
				synchronized (rpcTcOpenVarAccessIn) {
					rpcTcOpenVarAccessIn.varAccessPath = fullPath;
					client.client.RpcTcOpenVarAccess_1(rpcTcOpenVarAccessIn, rpcTcOpenVarAccessOut);
					if (rpcTcOpenVarAccessOut.retVal) {
						AccessHandle accessHandle = new AccessHandle(rpcTcOpenVarAccessOut.type.value);
						accessHandle.access = new RpcTcVarAccess();
						accessHandle.access.index = rpcTcOpenVarAccessOut.varAccess.index;
						System.arraycopy(rpcTcOpenVarAccessOut.varAccess.offsets, 0, accessHandle.access.offsets, 0, rpcTcOpenVarAccessOut.varAccess.offsets_count);
						accessHandle.access.offsets_count = rpcTcOpenVarAccessOut.varAccess.offsets_count;
						accessHandle.access.typeHandle = rpcTcOpenVarAccessOut.varAccess.typeHandle;
						accessHandle.access.varHandle = rpcTcOpenVarAccessOut.varAccess.varHandle;
						return accessHandle;
					}
				}
			} else {
				synchronized (rpcTcOpenVarAccessListIn) {
					rpcTcOpenVarAccessListIn.varAccessPaths[0] = fullPath;
					rpcTcOpenVarAccessListIn.varAccessPaths_count = 1;
					client.client.RpcTcOpenVarAccessList_1(rpcTcOpenVarAccessListIn, rpcTcOpenVarAccessListOut);
					if (rpcTcOpenVarAccessListOut.retVal && (rpcTcOpenVarAccessListOut.varAccess_count == 1)) {
						AccessHandle accessHandle = new AccessHandle(rpcTcOpenVarAccessListOut.info[0].typeKind.value);
						accessHandle.setAttributesValid();
						accessHandle.setReadOnly((rpcTcOpenVarAccessListOut.info[0].attr & RpcTcVarAccessAttr.rpcVarAccIsReadonly) != 0);
						accessHandle.setConstant((rpcTcOpenVarAccessListOut.info[0].attr & RpcTcVarAccessAttr.rpcVarAccIsConst) != 0);
						accessHandle.setUser((rpcTcOpenVarAccessListOut.info[0].attr & RpcTcVarAccessAttr.rpcVarAccIsUser) != 0);
						accessHandle.setPrivate((rpcTcOpenVarAccessListOut.info[0].attr & RpcTcVarAccessAttr.rpcVarAccIsPrivate) != 0);
						accessHandle.access = new RpcTcVarAccess();
						accessHandle.access.index = rpcTcOpenVarAccessListOut.varAccess[0].index;
						System.arraycopy(rpcTcOpenVarAccessListOut.varAccess[0].offsets, 0, accessHandle.access.offsets, 0, rpcTcOpenVarAccessListOut.varAccess[0].offsets_count);
						accessHandle.access.offsets_count = rpcTcOpenVarAccessListOut.varAccess[0].offsets_count;
						accessHandle.access.typeHandle = rpcTcOpenVarAccessListOut.varAccess[0].typeHandle;
						accessHandle.access.varHandle = rpcTcOpenVarAccessListOut.varAccess[0].varHandle;
						return accessHandle;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - getVarAccessHandle (String): ");
		}
		return null;
	}

	public TcAccessHandle getVarAccessHandle(Object[] instancePath) {
		try {
			if ((0 < instancePath.length) && (instancePath[0] instanceof TcRpcStructuralVarNode) && (((TcRpcStructuralVarNode) instancePath[0]).getType() != null)) {
				synchronized (rpcTcGetVarAccessIn) {
					rpcTcGetVarAccessIn.varHnd = ((TcRpcStructuralVarNode) instancePath[0]).getHandle();
					client.client.RpcTcGetVarAccess_1(rpcTcGetVarAccessIn, rpcTcGetVarAccessOut);
					if (rpcTcGetVarAccessOut.retVal) {
						AccessHandle accessHandle = new AccessHandle(((TcRpcStructuralVarNode) instancePath[0]).getType().getTypeKind());
						accessHandle.access = new RpcTcVarAccess();
						accessHandle.access.index = rpcTcGetVarAccessOut.varAccess.index;
						System.arraycopy(rpcTcGetVarAccessOut.varAccess.offsets, 0, accessHandle.access.offsets, 0, rpcTcGetVarAccessOut.varAccess.offsets_count);
						accessHandle.access.offsets_count = rpcTcGetVarAccessOut.varAccess.offsets_count;
						accessHandle.access.typeHandle = rpcTcGetVarAccessOut.varAccess.typeHandle;
						accessHandle.access.varHandle = rpcTcGetVarAccessOut.varAccess.varHandle;
						for (int i = 1; i < instancePath.length; i++) {
							if (instancePath[i] instanceof TcRpcStructuralVarNode) {
								synchronized (rpcTcGetStructElemAccessIn) {
									rpcTcGetStructElemAccessIn.varHnd = ((TcRpcStructuralVarNode) instancePath[i]).getHandle();
									rpcTcGetStructElemAccessIn.varAccess = accessHandle.access;
									client.client.RpcTcGetStructElemAccess_1(rpcTcGetStructElemAccessIn, rpcTcGetStructElemAccessOut);
									if (rpcTcGetStructElemAccessOut.retVal) {
										accessHandle.setTypeKind((((TcRpcStructuralVarNode) instancePath[i]).getType() != null) ? ((TcRpcStructuralVarNode) instancePath[i]).getType().getTypeKind() : -1);
										accessHandle.access.index = rpcTcGetStructElemAccessOut.elemAccess.index;
										System.arraycopy(rpcTcGetStructElemAccessOut.elemAccess.offsets, 0, accessHandle.access.offsets, 0, rpcTcGetStructElemAccessOut.elemAccess.offsets_count);
										accessHandle.access.offsets_count = rpcTcGetStructElemAccessOut.elemAccess.offsets_count;
										accessHandle.access.typeHandle = rpcTcGetStructElemAccessOut.elemAccess.typeHandle;
										accessHandle.access.varHandle = rpcTcGetStructElemAccessOut.elemAccess.varHandle;
									}
								}
							} else if (instancePath[i] instanceof Integer) {
								synchronized (rpcTcGetArrayElemAccessIn) {
									rpcTcGetArrayElemAccessIn.varAccess = accessHandle.access;
									rpcTcGetArrayElemAccessIn.index = ((Integer) instancePath[i]).intValue();
									client.client.RpcTcGetArrayElemAccess_1(rpcTcGetArrayElemAccessIn, rpcTcGetArrayElemAccessOut);
									if (rpcTcGetArrayElemAccessOut.retVal) {
										int arrayFieldType = accessHandle.getTypeKind();
										if (instancePath[i - 1] instanceof TcRpcStructuralVarNode) {
											TcStructuralTypeNode t = ((TcRpcStructuralVarNode) instancePath[i - 1]).getType();
											while ((t != null) && (t.getTypeKind() == TcStructuralTypeNode.MAPTO_TYPE)) {
												t = t.getBaseType();
											}
											arrayFieldType = ((t != null) && (t.getArrayElementType() != null)) ? t.getArrayElementType().getTypeKind() : TcStructuralTypeNode.UNKOWN;
										}
										accessHandle.setTypeKind(arrayFieldType);
										accessHandle.access.index = rpcTcGetArrayElemAccessOut.elemAccess.index;
										System.arraycopy(rpcTcGetArrayElemAccessOut.elemAccess.offsets, 0, accessHandle.access.offsets, 0, rpcTcGetArrayElemAccessOut.elemAccess.offsets_count);
										accessHandle.access.offsets_count = rpcTcGetArrayElemAccessOut.elemAccess.offsets_count;
										accessHandle.access.typeHandle = rpcTcGetArrayElemAccessOut.elemAccess.typeHandle;
										accessHandle.access.varHandle = rpcTcGetArrayElemAccessOut.elemAccess.varHandle;
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
			System.out.println("Disconnect in TcStructuralModel - getVarAccessHandle: ");
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
			synchronized (rpcTcWriteDataIn) {
				rpcTcWriteDataIn.scopeHnd = ((TcRpcStructuralNode) scope).getHandle();
				client.client.RpcTcWriteData_1(rpcTcWriteDataIn, rpcTcWriteDataOut);
				return rpcTcWriteDataOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - writeBackSaveValues: ");
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
			synchronized (rpcTcWriteInitValueIn) {
				rpcTcWriteInitValueIn.varHnd = ((TcRpcStructuralNode) variable).getHandle();
				client.client.RpcTcWriteInitValue_1(rpcTcWriteInitValueIn, rpcTcWriteInitValueOut);
				return rpcTcWriteInitValueOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcStructuralModel - writeBackInitValues: ");
		}
		return false;
	}

	/**
	 * Liefert die Fehlermeldungen des letzten build - Aufrufes bzw. der
	 * inkrementellen Editieroperation zurück.
	 * 
	 * @return Fehlermeldung
	 */
	public TcErrorMessage getErrorMessage() {
		try {
			RpcTcReadErrorOut rpcTcReadErrorOut = new RpcTcReadErrorOut();
			client.client.RpcTcReadError_1(rpcTcReadErrorOut);
			if (rpcTcReadErrorOut.retVal) {
				return new ErrorMessage(rpcTcReadErrorOut.error);
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcTeachControl - getErrorMessage: ");
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
			synchronized (rpcTcGetErrorsIn) {
				rpcTcGetErrorsIn.prjHandle = ((TcRpcStructuralNode) project).getHandle();
				client.client.RpcTcGetErrors_1(rpcTcGetErrorsIn, rpcTcGetErrorsOut);
				if (rpcTcGetErrorsOut.retVal && (0 < rpcTcGetErrorsOut.errors_count)) {
					ErrorMessage[] errors = new ErrorMessage[rpcTcGetErrorsOut.errors_count];
					for (int i = 0; i < rpcTcGetErrorsOut.errors_count; i++) {
						errors[i] = new ErrorMessage(rpcTcGetErrorsOut.errors[i]);
					}
					return errors;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcTeachControl - getErrorMessages: ");
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
			synchronized (rpcTcSetCodePointIn) {
				rpcTcSetCodePointIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcSetCodePointIn.lineNr = lineNr;
				rpcTcSetCodePointIn.exeUnitHnd = (execUnit != null) ? ((TcRpcExecutionUnit) execUnit).getHandle() : 0;
				rpcTcSetCodePointIn.kind.value = kind;
				if (varPath == null) {
					rpcTcSetCodePointIn.instancePath.nrOfElems = 0;
				} else {
					rpcTcSetCodePointIn.instancePath.nrOfElems = (varPath.length <= rpcMaxInstancePathElems.value) ? varPath.length : rpcMaxInstancePathElems.value;
					for (int i = 0; i < rpcTcSetCodePointIn.instancePath.nrOfElems; i++) {
						if (varPath[i] instanceof TcStructuralVarNode) {
							rpcTcSetCodePointIn.instancePath.elems[i].structComponent = ((TcRpcStructuralVarNode) varPath[i]).getHandle();
						} else {
							rpcTcSetCodePointIn.instancePath.elems[i].arrayIndex = ((Integer) varPath[i]).intValue();
						}
					}
				}
				rpcTcSetCodePointIn.instancePath.elems_count = rpcTcSetCodePointIn.instancePath.nrOfElems;
				client.client.RpcTcSetCodePoint_1(rpcTcSetCodePointIn, rpcTcSetCodePointOut);
				return rpcTcSetCodePointOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - setCodePoint: ");
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
			synchronized (rpcTcActivateCodePointIn) {
				rpcTcActivateCodePointIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcActivateCodePointIn.lineNr = lineNr;
				rpcTcActivateCodePointIn.active = enable;
				client.client.RpcTcActivateCodePoint_1(rpcTcActivateCodePointIn, rpcTcActivateCodePointOut);
				return rpcTcActivateCodePointOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - enableCodePoint: ");
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
			synchronized (rpcTcRemoveCodePointIn) {
				rpcTcRemoveCodePointIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcRemoveCodePointIn.lineNr = lineNr;
				client.client.RpcTcRemoveCodePoint_1(rpcTcRemoveCodePointIn, rpcTcRemoveCodePointOut);
				return rpcTcRemoveCodePointOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - removeCodePoint: ");
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
			synchronized (rpcTcRemoveCodePointIn) {
				rpcTcRemoveCodePointIn.routineScopeHnd = ((TcRpcStructuralNode) node).getHandle();
				rpcTcRemoveCodePointIn.lineNr = -1;
				client.client.RpcTcRemoveCodePoint_1(rpcTcRemoveCodePointIn, rpcTcRemoveCodePointOut);
				return rpcTcRemoveCodePointOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - removeCodePoint: ");
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
			synchronized (rpcTcGetWatchPointCounterIn) {
				rpcTcGetWatchPointCounterIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcGetWatchPointCounterIn.lineNr = lineNr;
				client.client.RpcTcGetWatchPointCounter_1(rpcTcGetWatchPointCounterIn, rpcTcGetWatchPointCounterOut);
				if (rpcTcGetWatchPointCounterOut.retVal) {
					return rpcTcGetWatchPointCounterOut.counter;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - getWatchpointCounter: ");
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
			synchronized (rpcTcResetWatchPointCounterIn) {
				rpcTcResetWatchPointCounterIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcResetWatchPointCounterIn.lineNr = lineNr;
				client.client.RpcTcResetWatchPointCounter_1(rpcTcResetWatchPointCounterIn, rpcTcResetWatchPointCounterOut);
				return rpcTcResetWatchPointCounterOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - resetWatchpointCounter: ");
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
			synchronized (rpcTcSetWatchPointVarIn) {
				rpcTcSetWatchPointVarIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcSetWatchPointVarIn.lineNr = lineNr;
				rpcTcSetWatchPointVarIn.exeUnitHnd = (execUnit != null) ? ((TcRpcExecutionUnit) execUnit).getHandle() : 0;
				rpcTcSetWatchPointVarIn.instPath.nrOfElems = (path.length <= rpcMaxInstancePathElems.value) ? path.length : rpcMaxInstancePathElems.value;
				rpcTcSetWatchPointVarIn.instPath.elems_count = rpcTcSetWatchPointVarIn.instPath.nrOfElems;
				for (int i = 0; i < rpcTcSetWatchPointVarIn.instPath.nrOfElems; i++) {
					if (path[i] instanceof TcRpcStructuralVarNode) {
						rpcTcSetWatchPointVarIn.instPath.elems[i].structComponent = ((TcRpcStructuralVarNode) path[i]).getHandle();
					} else {
						rpcTcSetWatchPointVarIn.instPath.elems[i].arrayIndex = ((Integer) path[i]).intValue();
					}
				}
				client.client.RpcTcSetWatchPointVar_1(rpcTcSetWatchPointVarIn, rpcTcSetWatchPointVarOut);
				if (rpcTcSetWatchPointVarOut.retVal) {
					return new WatchpointVarNode(rpcTcSetWatchPointVarOut.wpVarHnd, path, execUnit);
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - setWatchpointVariable: ");
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
			synchronized (rpcTcRemoveWatchPointVarIn) {
				rpcTcRemoveWatchPointVarIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcRemoveWatchPointVarIn.lineNr = lineNr;
				rpcTcRemoveWatchPointVarIn.wpVarHnd = ((WatchpointVarNode) variable).getHandle();
				client.client.RpcTcRemoveWatchPointVar_1(rpcTcRemoveWatchPointVarIn, rpcTcRemoveWatchPointVarOut);
				return rpcTcRemoveWatchPointVarOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - removeWatchpointVariable: ");
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
			synchronized (rpcTcRemoveWatchPointVarIn) {
				rpcTcRemoveWatchPointVarIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcRemoveWatchPointVarIn.lineNr = lineNr;
				rpcTcRemoveWatchPointVarIn.wpVarHnd = 0;
				synchronized (rpcTcRemoveWatchPointVarOut) {
					client.client.RpcTcRemoveWatchPointVar_1(rpcTcRemoveWatchPointVarIn, rpcTcRemoveWatchPointVarOut);
					return rpcTcRemoveWatchPointVarOut.retVal;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - removeAllWatchpointVariable: ");
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
			synchronized (rpcTcGetWatchPointValueIn) {
				rpcTcGetWatchPointValueIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcGetWatchPointValueIn.lineNr = lineNr;
				rpcTcGetWatchPointValueIn.wpVarHnd = ((WatchpointVarNode) variable).getHandle();
				client.client.RpcTcGetWatchPointValue_1(rpcTcGetWatchPointValueIn, rpcTcGetWatchPointValueOut);
				if (rpcTcGetWatchPointValueOut.retVal) {
					value.boolValue = rpcTcGetWatchPointValueOut.value.bValue;
					value.int8Value = (byte) rpcTcGetWatchPointValueOut.value.i8Value;
					value.int16Value = (short) rpcTcGetWatchPointValueOut.value.i16Value;
					value.int32Value = rpcTcGetWatchPointValueOut.value.i32Value;
					value.int64Value = rpcTcGetWatchPointValueOut.value.i64Value;
					value.floatValue = rpcTcGetWatchPointValueOut.value.fValue;
					value.stringValue = rpcTcGetWatchPointValueOut.value.sValue;
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - getWatchpointVariableValue: ");
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
			synchronized (rpcTcGetWatchPointVarsIn) {
				rpcTcGetWatchPointVarsIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				rpcTcGetWatchPointVarsIn.lineNr = lineNr;
				client.client.RpcTcGetWatchPointVars_1(rpcTcGetWatchPointVarsIn, rpcTcGetWatchPointVarsOut);
				if (rpcTcGetWatchPointVarsOut.retVal) {
					TcWatchpointVarNode[] vs = new TcWatchpointVarNode[rpcTcGetWatchPointVarsOut.nrWPVars];
					for (int i = 0; i < rpcTcGetWatchPointVarsOut.nrWPVars; i++) {
						Object[] path = new Object[rpcTcGetWatchPointVarsOut.instancePaths[i].nrOfElems];
						for (int j = 0; j < rpcTcGetWatchPointVarsOut.instancePaths[i].nrOfElems; j++) {
							if (rpcTcGetWatchPointVarsOut.instancePaths[i].elems[j].structComponent == 0) {
								path[j] = new Integer(rpcTcGetWatchPointVarsOut.instancePaths[i].elems[j].arrayIndex);
							} else {
								path[j] = new TcRpcStructuralVarNode(rpcTcGetWatchPointVarsOut.instancePaths[i].elems[j].structComponent, client);
							}
						}
						vs[i] = new WatchpointVarNode(rpcTcGetWatchPointVarsOut.wpVarHnds[i], path, new TcRpcExecutionUnit(rpcTcGetWatchPointVarsOut.exeUnitHnds[i], client));
					}
					return vs;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - getWatchpointVariables: ");
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
	public TcCodePointRoutineList getCodePointRoutines(int lastChangeCount) {
		try {
			synchronized (rpcTcGetCodePointRoutinesOut) {
				client.client.RpcTcGetCodePointRoutines_1(rpcTcGetCodePointRoutinesOut);
				if (rpcTcGetCodePointRoutinesOut.retVal) {
					if (lastChangeCount != rpcTcGetCodePointRoutinesOut.chgCnt) {
						TcCodePointRoutineList list = new TcCodePointRoutineList();
						list.changeCount = rpcTcGetCodePointRoutinesOut.chgCnt;
						list.routines = new TcStructuralRoutineNode[rpcTcGetCodePointRoutinesOut.routineScopeHnd_count];
						for (int i = 0; i < rpcTcGetCodePointRoutinesOut.routineScopeHnd_count; i++) {
							TcRpcStructuralNode n = (TcRpcStructuralNode) client.cache.getFromCache(rpcTcGetCodePointRoutinesOut.routineScopeHnd[i]);
							if (n instanceof TcRpcStructuralRoutineNode) {
								list.routines[i] = (TcRpcStructuralRoutineNode) n;
							} else {
								list.routines[i] = new TcRpcStructuralRoutineNode(rpcTcGetCodePointRoutinesOut.routineScopeHnd[i], client);
								client.cache.putToCache((TcRpcStructuralRoutineNode) list.routines[i]);
							}
						}
						return list;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - getCodePointRoutines: ");
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
			synchronized (rpcTcGetCodePointsIn) {
				rpcTcGetCodePointsIn.routineScopeHnd = ((TcRpcStructuralRoutineNode) routine).getHandle();
				client.client.RpcTcGetCodePoints_1(rpcTcGetCodePointsIn, rpcTcGetCodePointsOut);
				if (rpcTcGetCodePointsOut.retVal) {
					TcCodePoint[] codePoints = new TcCodePoint[rpcTcGetCodePointsOut.nrCodePoints];
					for (int i = 0; i < rpcTcGetCodePointsOut.nrCodePoints; i++) {
						codePoints[i] = new TcCodePoint();
						codePoints[i].lineNr = rpcTcGetCodePointsOut.lineNrs[i];
						codePoints[i].kind = rpcTcGetCodePointsOut.kinds[i].value;
						codePoints[i].isEnabled = rpcTcGetCodePointsOut.active[i];
					}
					return codePoints;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcExecutionModel - step: ");
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
				synchronized (rpcTcGetNodeInfoIn) {
					rpcTcGetNodeInfoIn.scopeHnd = handle;
					client.client.RpcTcGetNodeInfo_1(rpcTcGetNodeInfoIn, rpcTcGetNodeInfoOut);
					if (rpcTcGetNodeInfoOut.retVal) {
						TcRpcStructuralNode node;
						switch (rpcTcGetNodeInfoOut.info.kind.value) {
						case TcStructuralNode.ROUTINE:
							node = new TcRpcStructuralRoutineNode(handle, client);
							break;
						case TcStructuralNode.TYPE:
							node = new TcRpcStructuralTypeNode(handle, client);
							break;
						case TcStructuralNode.VAR:
							node = new TcRpcStructuralVarNode(handle, client);
							break;
						case TcStructuralNode.CONST:
							node = new TcRpcStructuralConstNode(handle, client);
							break;
						default:
							node = new TcRpcStructuralNode(handle, client);
						}
						node.setInfo(rpcTcGetNodeInfoOut.info, null);
						return node;
					}
				}
			} catch (Exception e) {
				System.out.println("Disconnect in TcStructuralNode - createNode: ");
			}
		}
		return null;
	}

	/**
	 * Variablen - Zugriffs - Handle
	 */
	public class AccessHandle extends TcAccessHandle {
		protected RpcTcVarAccess access;

		protected AccessHandle(int typeKind) {
			this.typeKind = typeKind;
		}

		protected void setAttributesValid() {
			hasValidAttributes = true;
		}

		protected void setTypeKind(int typeKind) {
			this.typeKind = typeKind;
		}

		protected void setReadOnly(boolean isReadOnly) {
			this.isReadOnly = isReadOnly;
		}

		protected void setConstant(boolean isConstant) {
			this.isConstant = isConstant;
		}

		protected void setUser(boolean isUser) {
			this.isUser = isUser;
		}

		protected void setPrivate(boolean isPrivate) {
			this.isPrivate = isPrivate;
		}
	}

	/**
	 * Fehlermeldungsobjekt
	 */
	public static class ErrorMessage extends TcErrorMessage {

		private ErrorMessage(RpcTcErrorElem e) {
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
				paramSValues[i] = e.errorParams[i].sValue;
			}
		}
	}

	private class StructuralNodeChunkEnumeration implements Enumeration {
		private int kind;
		private TcRpcStructuralNode scope;
		private int nrOfHnd;
		private final TcRpcStructuralNode[] elems = new TcRpcStructuralNode[rpcChunkLen.value];
		private int iterHandle;
		private int index;
		private boolean isFirst = true;
		private boolean isValid = false;

		private StructuralNodeChunkEnumeration(TcStructuralNode scope, int kind) {
			this.scope = (TcRpcStructuralNode) scope;
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
		public Object nextElement() {
			if (hasMoreElements()) {
				isValid = false;
				index++;
				return elems[index - 1];
			}
			return null;
		}

		private void loadInfo(int[] nodeHnds) throws IOException, RPCException {
			synchronized (rpcTcGetNodeInfoListIn) {
				rpcTcGetNodeInfoListIn.nrOfScopHnd = nrOfHnd;
				System.arraycopy(nodeHnds, 0, rpcTcGetNodeInfoListIn.scopeHnd, 0, nrOfHnd);
				rpcTcGetNodeInfoListIn.scopeHnd_count = nrOfHnd;
				client.client.RpcTcGetNodeInfoList_1(rpcTcGetNodeInfoListIn, rpcTcGetNodeInfoListOut);
				if (rpcTcGetNodeInfoListOut.retVal) {
					switch (kind) {
					case TcStructuralNode.ROUTINE:
					case FILTER_USER_ROUTINE: {
						rpcTcGetRoutineInfoListIn.nrOfRoutineScopeHnd = nrOfHnd;
						System.arraycopy(nodeHnds, 0, rpcTcGetRoutineInfoListIn.routineScopeHnd, 0, nrOfHnd);
						rpcTcGetRoutineInfoListIn.routineScopeHnd_count = nrOfHnd;
						client.client.RpcTcGetRoutineInfoList_1(rpcTcGetRoutineInfoListIn, rpcTcGetRoutineInfoListOut);
						if (rpcTcGetRoutineInfoListOut.retVal) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = new TcRpcStructuralRoutineNode(nodeHnds[i], client);
								elems[i].setInfo(rpcTcGetNodeInfoListOut.infos[i], scope);
								((TcRpcStructuralRoutineNode) elems[i]).setRoutineInfo(rpcTcGetRoutineInfoListOut.infos[i]);
								client.cache.putToCache(elems[i]);
							}
							return;
						}
					}
						break;
					case TcStructuralNode.TYPE:
					case FILTER_USER_TYPE: {
						rpcTcGetTypeInfoListIn.nrOfTypeScopeHnd = nrOfHnd;
						System.arraycopy(nodeHnds, 0, rpcTcGetTypeInfoListIn.typeScopeHnd, 0, nrOfHnd);
						rpcTcGetTypeInfoListIn.typeScopeHnd_count = nrOfHnd;
						client.client.RpcTcGetTypeInfoList_1(rpcTcGetTypeInfoListIn, rpcTcGetTypeInfoListOut);
						if (rpcTcGetTypeInfoListOut.retVal) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = (TcRpcStructuralNode) client.cache.getFromCache(nodeHnds[i]);
								if ((elems[i] == null) || !(elems[i] instanceof TcRpcStructuralTypeNode)) {
									elems[i] = new TcRpcStructuralTypeNode(nodeHnds[i], client);
									client.cache.putToCache(elems[i]);
								}
								if ((rpcTcGetTypeInfoListOut.infos[i].kind.value == TcStructuralTypeNode.UNIT_TYPE) && (rpcTcGetTypeInfoListOut.infos[i].baseTypeHnd == 0)) {
									client.cache.putToDirEntryCache(elems[i]);
								}
								elems[i].setInfo(rpcTcGetNodeInfoListOut.infos[i], scope);
								((TcRpcStructuralTypeNode) elems[i]).setTypeInfo(rpcTcGetTypeInfoListOut.infos[i]);
							}

							// load base type infos
							int counter = 0;
							for (int i = 0; i < nrOfHnd; i++) {
								int typeKind = ((TcRpcStructuralTypeNode) elems[i]).typeKind;
								TcRpcStructuralTypeNode baseType = (TcRpcStructuralTypeNode) ((TcRpcStructuralTypeNode) elems[i]).getBaseType();
								if (((typeKind == TcStructuralTypeNode.ENUM_TYPE) || (typeKind == TcStructuralTypeNode.STRUCT_TYPE) || (typeKind == TcRpcStructuralTypeNode.VARIANT_TYPE)) && (baseType != null) && (baseType.getName() == null)) {
									nodeHnds[counter] = i;
									rpcTcGetNodeInfoListIn.scopeHnd[counter] = baseType.getHandle();
									counter++;
								}
							}
							if (0 < counter) {
								rpcTcGetNodeInfoListIn.nrOfScopHnd = counter;
								rpcTcGetNodeInfoListIn.scopeHnd_count = counter;
								client.client.RpcTcGetNodeInfoList_1(rpcTcGetNodeInfoListIn, rpcTcGetNodeInfoListOut);
								if (rpcTcGetNodeInfoListOut.retVal) {
									for (int i = 0; i < counter; i++) {
										TcRpcStructuralTypeNode baseType = (TcRpcStructuralTypeNode) ((TcRpcStructuralTypeNode) elems[nodeHnds[i]]).getBaseType();
										baseType.setInfo(rpcTcGetNodeInfoListOut.infos[i], null);
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
						rpcTcGetVarInfoListIn.nrOfVarScopeHnd = nrOfHnd;
						System.arraycopy(nodeHnds, 0, rpcTcGetVarInfoListIn.varScopeHnd, 0, nrOfHnd);
						rpcTcGetVarInfoListIn.varScopeHnd_count = nrOfHnd;
						client.client.RpcTcGetVarInfoList_1(rpcTcGetVarInfoListIn, rpcTcGetVarInfoListOut);
						if (rpcTcGetVarInfoListOut.retVal) {
							// var infos loaded
							int nrOfVars = nrOfHnd;
							for (int i = 0; i < nrOfVars; i++) {
								elems[i] = new TcRpcStructuralVarNode(nodeHnds[i], client);
								elems[i].setInfo(rpcTcGetNodeInfoListOut.infos[i], scope);
							}

							// ********
							// load all type infos for all not already loaded
							// types
							int counter = 0;
							for (int i = 0; i < nrOfVars; i++) {
								int typeHnd = rpcTcGetVarInfoListOut.infos[i].typeHnd;
								if (client.cache.getFromCache(typeHnd) == null) {
									rpcTcGetNodeInfoListIn.scopeHnd[counter] = typeHnd;
									counter++;
								}
							}
							int bc = 0;
							if (0 < counter) {
								// load node info
								rpcTcGetNodeInfoListIn.nrOfScopHnd = counter;
								rpcTcGetNodeInfoListIn.scopeHnd_count = counter;
								client.client.RpcTcGetNodeInfoList_1(rpcTcGetNodeInfoListIn, rpcTcGetNodeInfoListOut);
								if (rpcTcGetNodeInfoListOut.retVal) {
									// load type info
									rpcTcGetTypeInfoListIn.nrOfTypeScopeHnd = counter;
									rpcTcGetTypeInfoListIn.typeScopeHnd_count = counter;
									System.arraycopy(rpcTcGetNodeInfoListIn.scopeHnd, 0, rpcTcGetTypeInfoListIn.typeScopeHnd, 0, counter);
									client.client.RpcTcGetTypeInfoList_1(rpcTcGetTypeInfoListIn, rpcTcGetTypeInfoListOut);
									if (rpcTcGetTypeInfoListOut.retVal) {
										for (int i = 0; i < counter; i++) {
											TcRpcStructuralTypeNode tn = new TcRpcStructuralTypeNode(rpcTcGetNodeInfoListIn.scopeHnd[i], client);
											tn.setInfo(rpcTcGetNodeInfoListOut.infos[i], null);
											if ((rpcTcGetTypeInfoListOut.infos[i].kind.value == TcStructuralTypeNode.MAPTO_TYPE) && (rpcTcGetTypeInfoListOut.infos[i].baseTypeHnd != 0)
													&& (client.cache.getFromCache(rpcTcGetTypeInfoListOut.infos[i].baseTypeHnd) == null)) {
												rpcTcGetNodeInfoListIn.scopeHnd[bc] = rpcTcGetTypeInfoListOut.infos[i].baseTypeHnd;
												bc++;
											}
											tn.setTypeInfo(rpcTcGetTypeInfoListOut.infos[i]);
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
								rpcTcGetNodeInfoListIn.nrOfScopHnd = bc;
								rpcTcGetNodeInfoListIn.scopeHnd_count = bc;
								client.client.RpcTcGetNodeInfoList_1(rpcTcGetNodeInfoListIn, rpcTcGetNodeInfoListOut);
								if (rpcTcGetNodeInfoListOut.retVal) {
									// load type info
									rpcTcGetTypeInfoListIn.nrOfTypeScopeHnd = bc;
									rpcTcGetTypeInfoListIn.typeScopeHnd_count = bc;
									System.arraycopy(rpcTcGetNodeInfoListIn.scopeHnd, 0, rpcTcGetTypeInfoListIn.typeScopeHnd, 0, bc);
									client.client.RpcTcGetTypeInfoList_1(rpcTcGetTypeInfoListIn, rpcTcGetTypeInfoListOut);
									if (rpcTcGetTypeInfoListOut.retVal) {
										for (int i = 0; i < bc; i++) {
											TcRpcStructuralNode tn = (TcRpcStructuralNode) client.cache.getFromCache(rpcTcGetNodeInfoListIn.scopeHnd[i]);
											if ((tn != null) && (tn instanceof TcRpcStructuralTypeNode)) {
												tn.setInfo(rpcTcGetNodeInfoListOut.infos[i], null);
												((TcRpcStructuralTypeNode) tn).setTypeInfo(rpcTcGetTypeInfoListOut.infos[i]);
											}
										}
									}
								}
							}

							// **********
							for (int i = 0; i < nrOfVars; i++) {
								((TcRpcStructuralVarNode) elems[i]).setVarInfo(rpcTcGetVarInfoListOut.infos[i]);
							}
							return;
						}
						break;
					}
					case TcStructuralNode.CONST:
					case FILTER_USER_CONST: {
						rpcTcGetConstInfoListIn.nrOfConstScopeHnd = nrOfHnd;
						System.arraycopy(nodeHnds, 0, rpcTcGetConstInfoListIn.constScopeHnd, 0, nrOfHnd);
						rpcTcGetConstInfoListIn.constScopeHnd_count = nrOfHnd;
						client.client.RpcTcGetConstInfoList_1(rpcTcGetConstInfoListIn, rpcTcGetConstInfoListOut);
						if (rpcTcGetConstInfoListOut.retVal) {
							for (int i = 0; i < nrOfHnd; i++) {
								elems[i] = (TcRpcStructuralNode) client.cache.getFromCache(nodeHnds[i]);
								if ((elems[i] == null) || !(elems[i] instanceof TcRpcStructuralConstNode)) {
									elems[i] = new TcRpcStructuralConstNode(nodeHnds[i], client);
									client.cache.putToCache(elems[i]);
								}
								elems[i].setInfo(rpcTcGetNodeInfoListOut.infos[i], scope);
								((TcRpcStructuralConstNode) elems[i]).setConstInfo(rpcTcGetConstInfoListOut.infos[i]);
							}
							return;
						}
						break;
					}
					default:
						for (int i = 0; i < nrOfHnd; i++) {
							elems[i] = (TcRpcStructuralNode) client.cache.getFromDirEntryCache(nodeHnds[i]);
							if ((elems[i] == null) || (elems[i] instanceof TcRpcStructuralTypeNode) || (elems[i] instanceof TcRpcStructuralConstNode)) {
								elems[i] = new TcRpcStructuralNode(nodeHnds[i], client);
								client.cache.putToDirEntryCache(elems[i]);
								elems[i].setInfo(rpcTcGetNodeInfoListOut.infos[i], scope);
							} else {
								elems[i].setInfo(rpcTcGetNodeInfoListOut.infos[i], scope);
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
				synchronized (rpcTcGetFirstNodeChunkIn) {
					rpcTcGetFirstNodeChunkIn.scopeHnd = scope.getHandle();
					rpcTcGetFirstNodeChunkIn.kind.value = kind;
					client.client.RpcTcGetFirstNodeChunk_1(rpcTcGetFirstNodeChunkIn, rpcTcGetFirstNodeChunkOut);
					if (rpcTcGetFirstNodeChunkOut.retVal) {
						index = 0;
						nrOfHnd = rpcTcGetFirstNodeChunkOut.nrOfHnd;
						iterHandle = rpcTcGetFirstNodeChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							loadInfo(rpcTcGetFirstNodeChunkOut.nodeHnd);
							return;
						} else if (kind == TcStructuralNode.SYSTEM) {
							// try global project
							rpcTcGetFirstNodeChunkIn.kind.value = TcStructuralNode.GLOBAL;
							client.client.RpcTcGetFirstNodeChunk_1(rpcTcGetFirstNodeChunkIn, rpcTcGetFirstNodeChunkOut);
							if (rpcTcGetFirstNodeChunkOut.retVal) {
								kind = TcStructuralNode.GLOBAL;
								index = 0;
								nrOfHnd = rpcTcGetFirstNodeChunkOut.nrOfHnd;
								iterHandle = rpcTcGetFirstNodeChunkOut.iterHnd;
								if (0 < nrOfHnd) {
									loadInfo(rpcTcGetFirstNodeChunkOut.nodeHnd);
									return;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Disconnect in TcStructuralModel - StructuralNodeChunkEnumeration - getFirstChunk: ");
			}
			nrOfHnd = 0;
			for (int i = 0; i < elems.length; i++) {
				elems[i] = null;
			}
		}

		private void getNextChunk() {
			try {
				synchronized (rpcTcGetNextNodeChunkIn) {
					rpcTcGetNextNodeChunkIn.iterHnd = iterHandle;
					rpcTcGetNextNodeChunkIn.scopeHnd = scope.getHandle();
					rpcTcGetNextNodeChunkIn.kind.value = kind;
					client.client.RpcTcGetNextNodeChunk_1(rpcTcGetNextNodeChunkIn, rpcTcGetNextNodeChunkOut);
					if (rpcTcGetNextNodeChunkOut.retVal) {
						index = 0;
						nrOfHnd = rpcTcGetNextNodeChunkOut.nrOfHnd;
						iterHandle = rpcTcGetNextNodeChunkOut.iterHnd;
						if (0 < nrOfHnd) {
							loadInfo(rpcTcGetNextNodeChunkOut.nodeHnd);
							return;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Disconnect in TcStructuralModel - StructuralNodeChunkEnumeration - getNextChunk: ");
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
		accessHandle.access = new RpcTcVarAccess();
		accessHandle.access.index = 0;
		accessHandle.access.typeHandle = 0;
		accessHandle.access.varHandle = 0;
		accessHandle.access.offsets_count = 1;
		return accessHandle;
	}
}
