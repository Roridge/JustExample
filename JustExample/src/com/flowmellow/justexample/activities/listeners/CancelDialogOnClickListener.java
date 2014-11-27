package com.flowmellow.justexample.activities.listeners;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CancelDialogOnClickListener implements OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
	}
}
