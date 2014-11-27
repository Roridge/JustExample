package com.flowmellow.justexample.activities.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

public class StartActivityOnClickListener implements OnClickListener {

	private Context context;
	private String intentAction;

	public StartActivityOnClickListener(final Context context, final String intentAction) {
		this.context = context;
		this.intentAction = intentAction;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		context.startActivity(new Intent(intentAction));
	}

}
