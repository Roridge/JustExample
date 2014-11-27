package com.flowmellow.justexample;

import android.app.Application;

public class DefaultApplication extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		bootloader();
	}
	
	private void bootloader() {
		BasicAppController.boot(this);
	}

}
