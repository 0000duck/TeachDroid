package com.keba.kemro.teach.network.sysrpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.plc.network.sysrpc.TCI.*;

public class TcSysRpcEditorModel implements TcEditorModel {
	private static final SysRpcTcCloseEditorIn SysRpcTcCloseEditorIn = new SysRpcTcCloseEditorIn();
	private static final SysRpcTcCloseEditorOut SysRpcTcCloseEditorOut = new SysRpcTcCloseEditorOut();
	private static final SysRpcTcGetFirstLineChunkIn SysRpcTcGetFirstLineChunkIn = new SysRpcTcGetFirstLineChunkIn();
	private static final SysRpcTcGetFirstLineChunkOut SysRpcTcGetFirstLineChunkOut = new SysRpcTcGetFirstLineChunkOut();
	private static final SysRpcTcGetNodeTextRangeIn SysRpcTcGetNodeTextRangeIn = new SysRpcTcGetNodeTextRangeIn();
	private static final SysRpcTcGetNodeTextRangeOut SysRpcTcGetNodeTextRangeOut = new SysRpcTcGetNodeTextRangeOut();
	private static final SysRpcTcGetSymbolTextRangeIn SysRpcTcGetSymbolTextRangeIn = new SysRpcTcGetSymbolTextRangeIn();
	private static final SysRpcTcGetSymbolTextRangeOut SysRpcTcGetSymbolTextRangeOut = new SysRpcTcGetSymbolTextRangeOut();
	private static final SysRpcTcDeleteTextIn SysRpcTcDeleteTextIn = new SysRpcTcDeleteTextIn();
	private static final SysRpcTcDeleteTextOut SysRpcTcDeleteTextOut = new SysRpcTcDeleteTextOut();
	private static final SysRpcTcInsertTextIn SysRpcTcInsertTextIn = new SysRpcTcInsertTextIn();
	private static final SysRpcTcInsertTextOut SysRpcTcInsertTextOut = new SysRpcTcInsertTextOut();
	private static final SysRpcTcSaveEditorIn SysRpcTcSaveEditorIn = new SysRpcTcSaveEditorIn();
	private static final SysRpcTcSaveEditorOut SysRpcTcSaveEditorOut = new SysRpcTcSaveEditorOut();

	TcSysRpcClient client;
	private int handle;
	private boolean valid = false;

	TcSysRpcEditorModel(int handle, TcSysRpcClient client) {
		this.handle = handle;
		this.client = client;
		valid = true;
	}

	/**
	 * Liefert den Handle zurück.
	 * 
	 * @return Handle
	 */
	protected int getHandle() {
		return handle;
	}

	/**
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	/**
	 * Schließt das Editormodell und gibt die Resourcen im TeachControl frei.
	 */
	public void close() {
		if (valid) {
			try {
				synchronized (SysRpcTcCloseEditorIn) {
					SysRpcTcCloseEditorIn.editHnd = getHandle();
					client.client.SysRpcTcCloseEditor_1(SysRpcTcCloseEditorIn, SysRpcTcCloseEditorOut);
					valid = false;
				}
			} catch (Exception e) {
				System.out.println("Disconnect in TcEditorModel - close(): " + e);
			}
		}
	}

	/**
	 * Fügt den Text text an der Position pos ein.
	 * 
	 * @param pos
	 *            gibt die Einfügeposition an.
	 * @param text
	 *            enthält den Text der eingefügt werden soll.
	 * 
	 * @return true für das erfolgreiche Einfügen
	 */
	public boolean insertText(int line, String text) {
		try {
			synchronized (SysRpcTcInsertTextIn) {
				SysRpcTcInsertTextIn.editHnd = getHandle();
				SysRpcTcInsertTextIn.len = text.length();
				SysRpcTcInsertTextIn.pos.line = line;
				SysRpcTcInsertTextIn.pos.col = 0;
				SysRpcTcInsertTextIn.text = text;
				client.client.SysRpcTcInsertText_1(SysRpcTcInsertTextIn, SysRpcTcInsertTextOut);
				return SysRpcTcInsertTextOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - insertText: ");
		}
		return false;
	}

	/**
	 * Löscht den angegeben Textbereich range.
	 * 
	 * @param range
	 *            Textbereich
	 * 
	 * @return true für das erfolgreiche Löschen
	 */
	public boolean deleteText(int line, int count) {
		try {
			synchronized (SysRpcTcDeleteTextIn) {
				SysRpcTcDeleteTextIn.editHnd = getHandle();
				SysRpcTcDeleteTextIn.beg.line = line;
				SysRpcTcDeleteTextIn.beg.col = 0;
				SysRpcTcDeleteTextIn.end.line = line + count;
				SysRpcTcDeleteTextIn.end.col = 0;
				client.client.SysRpcTcDeleteText_1(SysRpcTcDeleteTextIn, SysRpcTcDeleteTextOut);
				return SysRpcTcDeleteTextOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - deleteText: ");
		}
		return false;
	}

	/**
	 * Speichert den Editormodellinhalt.
	 * 
	 * @return true, wenn das Speichern erfolgreich durchgeführt wurde
	 */
	public boolean save() {
		try {
			synchronized (SysRpcTcSaveEditorIn) {
				SysRpcTcSaveEditorIn.editHnd = getHandle();
				client.client.SysRpcTcSaveEditor_1(SysRpcTcSaveEditorIn, SysRpcTcSaveEditorOut);
				return SysRpcTcSaveEditorOut.retVal;
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - save(): " + e);
		}
		return false;
	}

	/**
	 * Liefert den Textbereich zurück, das dem angegebenen Strukturbaumknoten
	 * entspricht.
	 * 
	 * @return Textbereich
	 * @see Range
	 */
	public TcEditorTextRange getTextRange(TcStructuralNode node) {
		try {
			synchronized (SysRpcTcGetNodeTextRangeIn) {
				SysRpcTcGetNodeTextRangeIn.editHnd = getHandle();
				SysRpcTcGetNodeTextRangeIn.nodeHnd = ((TcSysRpcStructuralNode) node).getHandle();
				client.client.SysRpcTcGetNodeTextRange_1(SysRpcTcGetNodeTextRangeIn, SysRpcTcGetNodeTextRangeOut);
				if (SysRpcTcGetNodeTextRangeOut.retVal) {
					TcEditorTextRange range = new TcEditorTextRange();
					range.line = SysRpcTcGetNodeTextRangeOut.beg.line;
					range.count = SysRpcTcGetNodeTextRangeOut.end.line - range.line;
					if (0 < SysRpcTcGetNodeTextRangeOut.end.col) {
						range.count++;
					}
					return range;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - getTextRange(): " + e);
		}
		return null;
	}

	/**
	 * Returns the number of lines.
	 * 
	 * @return number of lines
	 */
	public int getLineCount() {
		try {
			synchronized (SysRpcTcGetSymbolTextRangeIn) {
				SysRpcTcGetSymbolTextRangeIn.editHnd = getHandle();
				SysRpcTcGetSymbolTextRangeIn.symHnd = 0;
				client.client.SysRpcTcGetSymbolTextRange_1(SysRpcTcGetSymbolTextRangeIn, SysRpcTcGetSymbolTextRangeOut);
				if (SysRpcTcGetSymbolTextRangeOut.retVal) {
					int count = SysRpcTcGetSymbolTextRangeOut.end.line - SysRpcTcGetSymbolTextRangeOut.beg.line;
					if (0 < SysRpcTcGetSymbolTextRangeOut.end.col) {
						count++;
					}
					return count;
				}
			}
		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorSymbol - getTextRange(): " + e);
		}
		return 0;
	}

	/**
	 * Gets up to 50 lines.
	 * 
	 * @param startLine
	 *            start position
	 * @return lines
	 */
	public String[] getLines(int startLine) {
		try {
			synchronized (SysRpcTcGetFirstLineChunkIn) {
				SysRpcTcGetFirstLineChunkIn.editHnd = getHandle();
				SysRpcTcGetFirstLineChunkIn.lineNr = startLine;
				client.client.SysRpcTcGetFirstLineChunk_1(SysRpcTcGetFirstLineChunkIn, SysRpcTcGetFirstLineChunkOut);
				if (SysRpcTcGetFirstLineChunkOut.retVal) {
					int nrOfLines = SysRpcTcGetFirstLineChunkOut.lines_count;
					String[] lines = new String[nrOfLines];
					for (int i = 0; i < nrOfLines; i++) {
						lines[i] = SysRpcTcGetFirstLineChunkOut.lines[i].toString();
					}
					return lines;
				}
			}

		} catch (Exception e) {
			System.out.println("Disconnect in TcEditorModel - getLineChunk: ");
		}
		return null;
	}

}
