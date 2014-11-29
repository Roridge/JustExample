package com.flowmellow.justexample.activities;

import android.test.InstrumentationTestCase;
import android.view.View;
import android.widget.EditText;

public class ActivityUtil {

	public static void clickView(final InstrumentationTestCase context, final View v) throws Throwable {
		context.runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				v.performClick();

			}
		});
	}

	public static void setText(final InstrumentationTestCase context, final EditText et, final String text) throws Throwable {
		context.runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				et.setText(text);
			}
		});
	}
}
