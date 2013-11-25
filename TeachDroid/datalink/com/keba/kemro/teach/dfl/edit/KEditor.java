package com.keba.kemro.teach.dfl.edit;

import java.util.*;

import com.keba.kemro.teach.dfl.*;
import com.keba.kemro.teach.dfl.structural.*;
import com.keba.kemro.teach.dfl.structural.routine.*;
import com.keba.kemro.teach.network.*;

public class KEditor {
	private static final String EMPTY_STRING = "";
	int clients;
	private TcEditorModel model;
	TcDirEntry file;
	private final Vector editorModelListeners = new Vector(2);
	private String[] lines;
	private int lineCount;
	Date modifiedDate;
	private KTcDfl dfl;

	KEditor(TcEditorModel model, TcDirEntry file, KTcDfl dfl) {
		// ROOT
		this.dfl = dfl;
		this.model = model;
		this.file = file;
		this.modifiedDate = file.getModifiedDate();
		lineCount = model.getLineCount();
	}


	/**
	 * Fügt einen Listener hinzu der bei Änderungen benachrichtigt wird
	 *
	 * @param listener Listener
	 */
	public void addKEditModelListener(KEditModelListener listener) {
		if (editorModelListeners.contains(listener)) {
			return;
		}
		editorModelListeners.addElement(listener);
	}

	/**
	 * Entfernt den angegebenen Editor aus der Benachrichtigungsqueue
	 *
	 * @param listener Entfernt den Listener
	 */
	public void removeKEditModelListener(KEditModelListener listener) {
		editorModelListeners.removeElement(listener);
	}

	/**
	 * Ist/Wird aufgerufen wenn die Struktur des Editors verändert wurde. Bewirkt eine
	 * Benachrichtigung aller Listener
	 */
	void fireEditorModelChanged() {
		lineCount = model.getLineCount();
		lines = null;
		for (int i = editorModelListeners.size() - 1; 0 <= i; i--) {
			((KEditModelListener) editorModelListeners.elementAt(i)).changed();
		}
	}

   void reopenEditorModel () {
      TcStructuralNode n = dfl.client.structure.getNode(file);
      if (n != null) {
         file = dfl.client.structure.getDirEntry(n);
         if (file != null) {
            model.close();
            lineCount = 0;
            model = dfl.client.structure.getEditorModel(n);
            if (model != null) {
               modifiedDate = file.getModifiedDate();
               lineCount = model.getLineCount();
            }
         }
      }   
   }

	/**
	 * Liefert die Anfang und das Ende des Befehls 
	 *
	 * @param node Knoten dessen Definietionszeilen/Spalten gesucht werden
	 *
	 * @return Die Range des gewünschten Knotens
	 */
	public TcEditorTextRange getTextRange(KStructNode node) {
		return model.getTextRange(node.getTcStructuralNode());
	}

	/**
	 * Liefert die Anzahl der Zeile die der Editor umfasst.
	 *
	 * @return Die Anzahl der Zeilen
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
	 * Liefert die Zeile mit dem Index
	 * @param line Index der gewünschten Zeile
	 *
	 * @return Die gewünschte Zeile
	 */
	public String getLine(int line) {
		if ((line >=0) && (line < lineCount) && (model != null)) {
			if (lines == null) {
				lines = new String[lineCount];
			}
			if (lines[line] == null) {
				String[] chunk = model.getLines(line);
				if (chunk != null) {
					int pos = line;
					int i = 0;
					while ((pos < lines.length) && (i < chunk.length)) {
						lines[pos] = chunk[i];
						pos++;
						i++;
					}
				}

			}
			return (lines[line] != null) ? lines[line] : EMPTY_STRING;
		}
		return EMPTY_STRING;
	}

	/**
	 * Löschen des Models
	 */
	void clear() {
		model.close();
		lines = null;
		lineCount = 0;
		clients = 0;
	}

	/**
	 * Fügt inkrementell den Text in die Routine ein. Das bedeutet, dass der Text in den
	 * Sourcecode eingefügt und compiliert wird. Wenn keine Fehler aufreten, wird das File gespeichert. 
	 * Der Text muss die Formatierung enthalten (Zeilenumbrüche bzw. Einrückungen). Beim Auftreten
	 * von Fehlern können die Compilerfehlermeldungen abgefragt werden. 
	 *
	 * @param routine Die Routine, in die der Text eingefügt werden soll
	 * @param line Zeilennummer an der der Text eingefügt werden soll
	 * @param text Text 
	 *
	 * @return boolean  Liefert true, wenn erfolgreich Eingefügt wurde
	 */
	public boolean insertTextInc(KStructRoutine routine, int line, String text) {
		if (dfl.client.structure.insertTextInc((TcStructuralRoutineNode) routine.getTcStructuralNode(), line, text)) {
			//statement is successfully inserted
         reopenEditorModel();
         fireEditorModelChanged();
			return true;
		}
		return false;
	}

	/**
	 * Entfernt inkrementell den Text aus der Routine. Das bedeutet, dass der Text aus
	 * dem Sourcecode entfernt und der Code compiliert wird. Wenn keine Fehler auftreten, wird das
	 * File gespeichert. Sollten Fehler auftreten, können diese über den KCompilerFehlerAdmin
	 * ausgewertet werden.
	 * 
	 *
	 * @param routine Routine aus der der Text entfernt werden soll
	 * @param line Anfangszeile des zu löschenden Texts
	 * @param count Zeilenanzahl
	 *
	 * @return boolean Liefert true, wenn erfolgreich entfernt wurde.
	 */
	public boolean removeTextInc(KStructRoutine routine, int line, int count) {
		if (dfl.client.structure.deleteTextInc((TcStructuralRoutineNode) routine.getTcStructuralNode(), line, count)) {
			//statement is successfully remvoed
         reopenEditorModel();
         fireEditorModelChanged();
			return true;
		}
		return false;
	}

	/**
	 * Ersetzt inkrementell den Text in der Routine. Das bedeutet, dass der Text im
	 * Sourcecode ersetzt ersetzt und compiliert wird.
	 * Wenn keine Fehler auftreten, wird das File gespeichert. Sollten Fehler auftreten können 
	 * diese über den KCompilerErrorAdministartor ausgewertet werden.
	 * 
	 *
	 * @param routine Routine aus der ein Befehl ersetzt werden soll
	 * @param line Anfangszeile des zu ersetzenden Befehls
	 * @param count Zeilenanzahl
	 * @param text neuer Text 
	 *
	 * @return boolean Liefert true wenn erfolgreich ersetzt wurde.
	 */
	public boolean replaceTextInc(KStructRoutine routine, int line, int count, String text) {
		if (dfl.client.structure.replaceTextInc((TcStructuralRoutineNode) routine.getTcStructuralNode(), line, count, text)) {
			//statement is successfully replaced
         reopenEditorModel();
         fireEditorModelChanged();
			return true;
		}
		return false;
	}
	
	/**
	 * Fügt den Text ein.
	 *
	 * @param line Zeilennummer an der der Text eingefügt werden soll
	 * @param text Text 
	 *
	 * @return boolean  Liefert true, wenn erfolgreich Eingefügt wurde
	 */
	public boolean insertText(int line, String text) {
		if (model.insertText(line, text) && model.save()) {
			//text is successfully inserted
         reopenEditorModel();
         fireEditorModelChanged();
			return true;
		}
		return false;
	}

	/**
	 * Entfernt den Textbereich.
	 *
	 * @param line Anfangszeile des zu löschenden Texts
	 * @param count Zeilenanzahl
	 *
	 * @return boolean Liefert true, wenn erfolgreich entfernt wurde.
	 */
	public boolean removeText(int line, int count) {
		if (model.deleteText(line, count) && model.save()) {
			//text is successfully remvoed
         reopenEditorModel();
         fireEditorModelChanged();
			return true;
		}
		return false;
	}

}
