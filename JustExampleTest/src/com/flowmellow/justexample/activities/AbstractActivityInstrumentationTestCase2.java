package com.flowmellow.justexample.activities;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public abstract class AbstractActivityInstrumentationTestCase2<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

	
	public AbstractActivityInstrumentationTestCase2(Class<T> activityClass) {
		super(activityClass);
	}

}
