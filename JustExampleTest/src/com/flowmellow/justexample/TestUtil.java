package com.flowmellow.justexample;

import android.app.Activity;
import android.content.Context;

public class TestUtil {

	public static MockAppController setupMockAppController(Activity activity, Context context) {
		final MockAppController mockAppController = new MockAppController(context);
		final DefaultApplication defaultApplication = (DefaultApplication) activity.getApplication();
		defaultApplication.setAppController(mockAppController);
		return mockAppController;
	}
}
