package com.flowmellow.justexample.activities;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.flowmellow.justexample.R;
import com.flowmellow.justexample.RestaurantUtil;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class RestaurantActivityTest extends ActivityInstrumentationTestCase2<RestaurantsActivity> {

	private RestaurantsActivity activity;
	private Context context;

	public RestaurantActivityTest() {
		super(RestaurantsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	@SmallTest
	@UiThreadTest
	public void testLocationProgressBarIsGone() {
		setupWithoutPostcode();
		final ProgressBar restaurantProgressBar = (ProgressBar) activity.findViewById(R.id.restaurantProgressBar);
		final int restaurantProgressBarVisability = restaurantProgressBar.getVisibility();
		final String errorMessage = "restaurant progress bar should not be visible if no postcode was passed in";
		assertEquals(errorMessage, View.GONE, restaurantProgressBarVisability);
	}

	@SmallTest
	public void testRestaurantListIsVisible() throws Throwable {
		setupWithPostcode();
		displayMockRestaurants();
		final ListView restaurantList = (ListView) activity.findViewById(R.id.restaurantList);
		final int restaurantListVisability = restaurantList.getVisibility();
		final String errorMessage = "restaurant list view should be visible";
		assertEquals(errorMessage, View.VISIBLE, restaurantListVisability);
	}

	@LargeTest
	public void testRestaurantListDisplayItemsInRatingOrder() throws Throwable {
		setupWithPostcode();
		displayMockRestaurants();
		final ListView restaurantList = (ListView) activity.findViewById(R.id.restaurantList);
		
		final StringBuilder errorMessage = new StringBuilder();
		boolean error = false;
		
		for(int i = 0; i < 8; i++) {
			RestaurantTO restaurantTO = (RestaurantTO) restaurantList.getItemAtPosition(i);
			final String errorMessageFormat = "item was at position[%d] was expected in position[%d]";
			
			if(i != restaurantTO.getId()) {
				errorMessage.append(String.format(errorMessageFormat, restaurantTO.getId(),i ));
				error = true;
			}
		}
		
		if(error) {
			fail(errorMessage.toString());
		}
		
	}

	private void displayMockRestaurants() throws Throwable {
		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				activity.displayRestaurants(RestaurantUtil.getMockRestaurants());
			}
		});
	}

	private void setupWithPostcode() {
		context = getInstrumentation().getTargetContext();
		final Intent intent = new Intent(context, RestaurantsActivity.class);
		intent.putExtra(RestaurantsActivity.POSTCODE_INTENT, RestaurantUtil.POSTCODE);
		setActivityIntent(intent);
		activity = getActivity();

	}

	private void setupWithoutPostcode() {
		activity = getActivity();
	}

}