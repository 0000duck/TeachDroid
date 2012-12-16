package com.keba.kemro.teach.network.rpc;

import com.keba.kemro.teach.network.*;
import com.keba.kemro.teach.network.rpc.protocol.*;

public class TcRpcEditorModel implements TcEditorModel {
   private static final RpcTcCloseEditorIn rpcTcCloseEditorIn = new RpcTcCloseEditorIn();
   private static final RpcTcCloseEditorOut rpcTcCloseEditorOut = new RpcTcCloseEditorOut();
   private static final RpcTcGetFirstLineChunkIn rpcTcGetFirstLineChunkIn =
      new RpcTcGetFirstLineChunkIn();
   private static final RpcTcGetFirstLineChunkOut rpcTcGetFirstLineChunkOut =
      new RpcTcGetFirstLineChunkOut();
   private static final RpcTcGetNodeTextRangeIn rpcTcGetNodeTextRangeIn = new RpcTcGetNodeTextRangeIn();
   private static final RpcTcGetNodeTextRangeOut rpcTcGetNodeTextRangeOut =
      new RpcTcGetNodeTextRangeOut();
   private static final RpcTcGetSymbolTextRangeIn rpcTcGetSymbolTextRangeIn =
      new RpcTcGetSymbolTextRangeIn();
   private static final RpcTcGetSymbolTextRangeOut rpcTcGetSymbolTextRangeOut =
      new RpcTcGetSymbolTextRangeOut();
   private static final RpcTcDeleteTextIn rpcTcDeleteTextIn = new RpcTcDeleteTextIn();
   private static final RpcTcDeleteTextOut rpcTcDeleteTextOut = new RpcTcDeleteTextOut();
   private static final RpcTcInsertTextIn rpcTcInsertTextIn = new RpcTcInsertTextIn();
   private static final RpcTcInsertTextOut rpcTcInsertTextOut = new RpcTcInsertTextOut();
   private static final RpcTcSaveEditorIn rpcTcSaveEditorIn = new RpcTcSaveEditorIn();
   private static final RpcTcSaveEditorOut rpcTcSaveEditorOut = new RpcTcSaveEditorOut();

   TcRpcClient client;
   private int handle;
   private boolean valid = false;
   
   TcRpcEditorModel (int handle, TcRpcClient client) {
      this.handle = handle;
		this.client = client;
      valid = true;
   }
   /**
    * Liefert den Handle zurück.
    *
    * @return Handle
    */
   protected int getHandle () {
      return handle;
   }

   /**
    * @see java.lang.Object#finalize()
    */
   protected void finalize () throws Throwable {
      close();
      super.finalize();
   }

   /**
    * Schließt das Editormodell und gibt die Resourcen im TeachControl frei.
    */
   public void close () {
      if (valid) {
         try {
            synchronized (rpcTcCloseEditorIn) {
               rpcTcCloseEditorIn.editHnd = getHandle();
               client.client.RpcTcCloseEditor_1(rpcTcCloseEditorIn, rpcTcCloseEditorOut);
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
    * @param pos gibt die Einfügeposition an.
    * @param text enthält den Text der eingefügt werden soll.
    *
    * @return true für das erfolgreiche Einfügen
    */
   public boolean insertText (int line, String text) {
      try {
         synchronized (rpcTcInsertTextIn) {
            rpcTcInsertTextIn.editHnd = getHandle();
            rpcTcInsertTextIn.len = text.length();
            rpcTcInsertTextIn.pos.line = line;
            rpcTcInsertTextIn.pos.col = 0;
            rpcTcInsertTextIn.text = text;
            client.client.RpcTcInsertText_1(rpcTcInsertTextIn, rpcTcInsertTextOut);
            return rpcTcInsertTextOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcEditorModel - insertText: ");
      }
      return false;
   }
   
   /**
    * Löscht den angegeben Textbereich range.
    * @param range Textbereich
    *
    * @return true für das erfolgreiche Löschen
    */
   public boolean deleteText (int line, int count) {
      try {
         synchronized (rpcTcDeleteTextIn) {
            rpcTcDeleteTextIn.editHnd = getHandle();
            rpcTcDeleteTextIn.beg.line = line;
            rpcTcDeleteTextIn.beg.col = 0;
            rpcTcDeleteTextIn.end.line = line + count;
            rpcTcDeleteTextIn.end.col = 0;
            client.client.RpcTcDeleteText_1(rpcTcDeleteTextIn, rpcTcDeleteTextOut);
            return rpcTcDeleteTextOut.retVal;
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
   public boolean save () {
      try {
         synchronized (rpcTcSaveEditorIn) {
            rpcTcSaveEditorIn.editHnd = getHandle();
            client.client.RpcTcSaveEditor_1(rpcTcSaveEditorIn, rpcTcSaveEditorOut);
            return rpcTcSaveEditorOut.retVal;
         }
      } catch (Exception e) {
         System.out.println("Disconnect in TcEditorModel - save(): " + e);
      }
      return false;
   }

   /**
    * Liefert den Textbereich zurück, das dem angegebenen Strukturbaumknoten entspricht.
    *
    * @return Textbereich
    * @see Range
    */
   public TcEditorTextRange getTextRange (TcStructuralNode node) {
      try {
         synchronized (rpcTcGetNodeTextRangeIn) {
            rpcTcGetNodeTextRangeIn.editHnd = getHandle();
            rpcTcGetNodeTextRangeIn.nodeHnd = ((TcRpcStructuralNode) node).getHandle();
            client.client.RpcTcGetNodeTextRange_1(rpcTcGetNodeTextRangeIn, rpcTcGetNodeTextRangeOut);
            if (rpcTcGetNodeTextRangeOut.retVal) {
            	TcEditorTextRange range = new TcEditorTextRange();
               range.line = rpcTcGetNodeTextRangeOut.beg.line;
               range.count = rpcTcGetNodeTextRangeOut.end.line - range.line;
               if (0 <rpcTcGetNodeTextRangeOut.end.col) {
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
    * @return number of lines
    */
   public int getLineCount () {
      try {
         synchronized (rpcTcGetSymbolTextRangeIn) {
            rpcTcGetSymbolTextRangeIn.editHnd = getHandle();
            rpcTcGetSymbolTextRangeIn.symHnd = 0;
            client.client.RpcTcGetSymbolTextRange_1(
                                             rpcTcGetSymbolTextRangeIn,
                                             rpcTcGetSymbolTextRangeOut);
            if (rpcTcGetSymbolTextRangeOut.retVal) {
               int count = rpcTcGetSymbolTextRangeOut.end.line - rpcTcGetSymbolTextRangeOut.beg.line;
               if (0 < rpcTcGetSymbolTextRangeOut.end.col) {
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
    * @param startLine start position
    * @return lines
    */
   public String[] getLines (int startLine) {
      try {            
         synchronized (rpcTcGetFirstLineChunkIn) {
            rpcTcGetFirstLineChunkIn.editHnd = getHandle();
            rpcTcGetFirstLineChunkIn.lineNr = startLine;
            client.client.RpcTcGetFirstLineChunk_1(
                                            rpcTcGetFirstLineChunkIn,
                                            rpcTcGetFirstLineChunkOut);
            if (rpcTcGetFirstLineChunkOut.retVal) {
               int nrOfLines = rpcTcGetFirstLineChunkOut.lines_count;
               String[] lines = new String[nrOfLines];
               System.arraycopy(rpcTcGetFirstLineChunkOut.lines, 0, lines, 0, nrOfLines);
               return lines;
            }
         }
         
      } catch (Exception e) {
         System.out.println("Disconnect in TcEditorModel - getLineChunk: ");
      }
      return null;
   }

}
