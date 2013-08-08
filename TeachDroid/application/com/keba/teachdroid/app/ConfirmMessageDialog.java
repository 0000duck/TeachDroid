package com.keba.teachdroid.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class ConfirmMessageDialog extends DialogFragment {
	private int position;
	private MessageAdapter<Message> adp;
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		adp = (MessageAdapter<Message>)getArguments().getSerializable("msgadp");
		position = getArguments().getInt("position");
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_confirm_message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				adp.remove(adp.getItem(position));
				Toast.makeText(getActivity(), "Message confirmed!", Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}