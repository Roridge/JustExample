package com.flowmellow.justexample;

import android.app.Activity;
import android.content.Context;

public class TestUtil {

	public static final String LONG_STRING = "ThisIsAReallyLongPostCodeWhichWeDontWantToGetIntoTheURLWhenRequestingForRestaurantsByPostCode";
	public static final String EXTRA_LONG_STRING = LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING
			+ LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING
			+ LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING
			+ LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING;

	public static MockAppController setupMockAppController(Activity activity, Context context) {
		final MockAppController mockAppController = new MockAppController(context);
		final DefaultApplication defaultApplication = (DefaultApplication) activity.getApplication();
		defaultApplication.setAppController(mockAppController);
		return mockAppController;
	}
}
