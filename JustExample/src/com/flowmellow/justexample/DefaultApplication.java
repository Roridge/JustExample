package com.flowmellow.justexample;

import android.app.Application;

public class DefaultApplication extends Application {

	private AppController controller;

	@Override
	public void onCreate() {
		super.onCreate();
		bootloader();
	}

	public AppController getController() {
		return controller;
	}

	private void bootloader() {
		controller = new BasicAppController(this);
	}

	void setAppController(AppController controller) {
		this.controller = controller;
	}
}
