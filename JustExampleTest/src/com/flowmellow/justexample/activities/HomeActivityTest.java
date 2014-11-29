package com.flowmellow.justexample.activities;

import android.app.Dialog;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.flowmellow.justexample.MockAppController;
import com.flowmellow.justexample.R;
import com.flowmellow.justexample.TestUtil;
import com.flowmellow.justexample.LocationUtil;
import com.flowmellow.justexample.Config;

public class HomeActivityTest extends AbstractActivityInstrumentationTestCase2<HomeActivity> {

	private static final long TIMEOUT = 1000l;

	private HomeActivity activity;
	private Context context;
	private MockAppController mockAppController;

	private EditText searchEditText;
	private ImageButton locationImageButton;
	private ImageButton searchImageButton;
	private ProgressBar locationProgressBar;

	public HomeActivityTest() {
		super(HomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		context = getInstrumentation().getContext();
		mockAppController = TestUtil.setupMockAppController(activity, context);
		//we want to ensure the MockAppController is picked up
		onCreate();
		searchEditText = (EditText) activity.findViewById(R.id.searchEditText);
		locationImageButton = (ImageButton) activity.findViewById(R.id.locationImageButton);
		searchImageButton = (ImageButton) activity.findViewById(R.id.searchImageButton);
		locationProgressBar = (ProgressBar) activity.findViewById(R.id.locationProgressBar);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SmallTest
	public void testSearchEditTextViewIsVisible() {
		final int searchEditTextVisability = searchEditText.getVisibility();
		final String errorMessage = "search edit text view was not visible";
		assertEquals(errorMessage, View.VISIBLE, searchEditTextVisability);
	}

	@SmallTest
	public void testLocationImageButtonIsVisible() {
		final int locationImageButtonVisability = locationImageButton.getVisibility();
		final String errorMessage = "location image button was not visible";
		assertEquals(errorMessage, View.VISIBLE, locationImageButtonVisability);
	}

	@SmallTest
	public void testSearchImageButtonIsVisible() {
		final int searchImageButtonVisability = searchImageButton.getVisibility();
		final String errorMessage = "search image button was not visible";
		assertEquals(errorMessage, View.VISIBLE, searchImageButtonVisability);
	}

	@SmallTest
	public void testLocationProgressBarIsGone() {
		final int locationProgressBarVisability = locationProgressBar.getVisibility();
		final String errorMessage = "location progress bar should be hidden on creation";
		assertEquals(errorMessage, View.GONE, locationProgressBarVisability);
	}

	@SmallTest
	public void testPostCodeCallbackDisplaysPostcode() throws Throwable {
		ActivityUtil.setText(this, searchEditText, LocationUtil.POSTCODE);
		final String actualText = searchEditText.getText().toString();
		final String errorMessage = "The text displayed didn't match what was sent to be displayed";
		assertEquals(errorMessage, LocationUtil.POSTCODE, actualText);
	}

	@SmallTest
	public void testLocationIsDisplayedOnRequest() throws Throwable {
		ActivityUtil.clickView(this, locationImageButton);
		final String actual = searchEditText.getText().toString();
		final String errorMessage = "The text was not displayed on request";
		assertEquals(errorMessage, LocationUtil.POSTCODE, actual);
	}

	@SmallTest
	public void testDialogIsShownWhenThereIsNoNetworkService() throws Throwable {
		mockAppController.setNetworkProviderDisabled(true);
		ActivityUtil.clickView(this, searchImageButton);
		final Dialog dialog = activity.getNoNetworkWarningDialog();
		assertTrue("The dialog is not visible", dialog.isShowing());
	}

	@SmallTest
	public void testDialogIsShownWhenThereIsNoLocationService() throws Throwable {
		mockAppController.setGPSProviderDisabled(true);
		ActivityUtil.clickView(this, locationImageButton);
		final Dialog dialog = activity.getNoLocationWarningDialog();
		assertTrue("The dialog is not visible", dialog.isShowing());
	}

	@LargeTest
	public void testLongStringIsNotPassedToService() throws Throwable {
		final ActivityMonitor activityMonitor = getInstrumentation().addMonitor(RestaurantsActivity.class.getName(), null, false);
		ActivityUtil.setText(this, searchEditText, TestUtil.EXTRA_LONG_STRING);
		ActivityUtil.clickView(this, searchImageButton);
		final RestaurantsActivity restaurantsActivity = (RestaurantsActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, TIMEOUT);
		String postCodePassedInToService = TestUtil.EXTRA_LONG_STRING;
		
		for(int i = 0; i < 10; i ++) {
			postCodePassedInToService = mockAppController.getPostCodePassedInToService();
			
			if(postCodePassedInToService == null) {
				delay();
			} else {
				return;
			}
		}
		final int postCodeLength = postCodePassedInToService.length();
		final int actaulPostCodeLength = TestUtil.EXTRA_LONG_STRING.length();
		final String errorMessage = "The Postcode passed in was longer than we expected, the postcode entered by the 'user' was %d characters long the result was %d characters long";
		assertTrue(String.format(errorMessage, actaulPostCodeLength, postCodeLength), postCodeLength <= Config.MAX_POSTCODE_LENGTH);
		restaurantsActivity.finish();
	}

	@LargeTest
	public void testRestuarantActivityIsStartedOnSearchWithPostCode() throws Throwable {
		final ActivityMonitor activityMonitor = getInstrumentation().addMonitor(RestaurantsActivity.class.getName(), null, false);
		ActivityUtil.setText(this, searchEditText, LocationUtil.POSTCODE);
		ActivityUtil.clickView(this, searchImageButton);
		final RestaurantsActivity restaurantsActivity = (RestaurantsActivity) getInstrumentation().waitForMonitorWithTimeout(
				activityMonitor, TIMEOUT);

		// restaurantsActivity is opened and captured.
		final String errorMessage = "The RestaurantsActivity was not started or took longer than %d milliseconds to start and was timed out";
		assertNotNull(String.format(errorMessage, TIMEOUT), restaurantsActivity);
		restaurantsActivity.finish();
	}

	@LargeTest
	public void testRestuarantActivityIsNotStartedOnSearchWithoutPostcode() throws Throwable {
		final ActivityMonitor activityMonitor = getInstrumentation().addMonitor(RestaurantsActivity.class.getName(), null, false);
		ActivityUtil.clickView(this, searchImageButton);
		final RestaurantsActivity restaurantsActivity = (RestaurantsActivity) getInstrumentation().waitForMonitorWithTimeout(
				activityMonitor, TIMEOUT);

		final String errorMessage = "The RestaurantsActivity was started with no postcode entered";
		assertNull(errorMessage, restaurantsActivity);
	}

	private void onCreate() {
		try {
			runTestOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.onCreate(null);
				}
			});
		} catch (Throwable e) {
			Log.e(Config.LOG_TAG, "couldn't call activiy onCreate");
		}
	}
	
	private void delay() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			Log.e(Config.LOG_TAG, "couldn't call activiy onCreate");
		}
	}

}
