package com.flowmellow.justexample;

import android.content.Context;

public class MockApplication extends DefaultApplication {

	private MockAppController controller;

	MockApplication(Context context) {
		controller = new MockAppController(context);
	}
	
	@Override
	public AppController getController() {
		return controller;
	}
	
}
