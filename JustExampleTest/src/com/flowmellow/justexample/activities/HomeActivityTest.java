package com.flowmellow.justexample.activities;

import com.flowmellow.justexample.R;
import com.flowmellow.justexample.activities.HomeActivity;

import android.app.Application;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class HomeActivityTest extends
		ActivityInstrumentationTestCase2<HomeActivity> {

	private HomeActivity activity;

	public HomeActivityTest() {
		super(HomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SmallTest
	public void testSearchEditTextViewIsVisible() {
		final EditText searchEditText = (EditText) activity
				.findViewById(R.id.searchEditText);
		final int searchEditTextVisability = searchEditText.getVisibility();
		final String errorMessage = "search edit text view was not visible";
		assertEquals(errorMessage, View.VISIBLE, searchEditTextVisability);
	}

	@SmallTest
	public void testLocationImageButtonIsVisible() {
		final ImageButton locationImageButton = (ImageButton) activity
				.findViewById(R.id.locationImageButton);
		final int locationImageButtonVisability = locationImageButton
				.getVisibility();
		final String errorMessage = "location image button was not visible";
		assertEquals(errorMessage, View.VISIBLE, locationImageButtonVisability);
	}

	@SmallTest
	public void testSearchImageButtonIsVisible() {
		final ImageButton searchImageButton = (ImageButton) activity
				.findViewById(R.id.searchImageButton);
		final int searchImageButtonVisability = searchImageButton
				.getVisibility();
		final String errorMessage = "search image button was not visible";
		assertEquals(errorMessage, View.VISIBLE, searchImageButtonVisability);
	}

	@SmallTest
	public void testLocationProgressBarIsGone() {
		final ProgressBar locationProgressBar = (ProgressBar) activity
				.findViewById(R.id.locationProgressBar);
		final int locationProgressBarVisability = locationProgressBar
				.getVisibility();
		final String errorMessage = "location progress bar should be hidden on creation";
		assertEquals(errorMessage, View.GONE, locationProgressBarVisability);
	}

	@SmallTest
	@UiThreadTest
	public void testPostCodeCallbackDisplaysPostcode() {
		final EditText searchEditText = (EditText) activity
				.findViewById(R.id.searchEditText);
		final String postcode = "WD6 1JN";
		activity.displayPostcode(postcode);
		final String actualText = searchEditText.getText().toString();
		final String errorMessage = "The text displayed didn't match what was sent to be displayed";
		assertEquals(errorMessage, postcode, actualText);
	}

	@LargeTest
	public void test() throws Throwable {
		
		Application i = (Application) getInstrumentation().getTargetContext().getApplicationContext();
		
		final ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				RestaurantsActivity.class.getName(), null, false);

		final String postcode = "WD6 1JN";
		final EditText searchEditText = (EditText) activity
				.findViewById(R.id.searchEditText);
		final ImageButton searchImageButton = (ImageButton) activity
				.findViewById(R.id.searchImageButton);

		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				searchEditText.setText(postcode);
				searchImageButton.performClick();

			}
		});

		RestaurantsActivity nextActivity = (RestaurantsActivity) getInstrumentation()
				.waitForMonitorWithTimeout(activityMonitor, 5);
		// next activity is opened and captured.
		assertNotNull(nextActivity);
		nextActivity.finish();

	}
}
