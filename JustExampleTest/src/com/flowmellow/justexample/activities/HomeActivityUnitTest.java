package com.flowmellow.justexample.activities;

import com.flowmellow.justexample.activities.MockHomeActivity;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class HomeActivityUnitTest extends ActivityUnitTestCase<MockHomeActivity> {

	private MockHomeActivity activity;
	private Context context;

	public HomeActivityUnitTest() {
		super(MockHomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getInstrumentation().getTargetContext();
		startActivity(new Intent(context, MockHomeActivity.class), null, null);
		activity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SmallTest
	public void testRequestPostcodeShowsDialogWhenGPSDisabled() {
		activity.isGPSProviderDisabled(true);
		activity.requestPostcode();
		assertTrue("The warning was not displayed and the gps provider was disabled", activity.warningDialogDisplayed());
	}

	@SmallTest
	public void testRequestPostcode() {
		activity.isGPSProviderDisabled(false);
		activity.requestPostcode();
		assertFalse("The warning was displayed and the gps provider was enabled", activity.warningDialogDisplayed());
	}

}