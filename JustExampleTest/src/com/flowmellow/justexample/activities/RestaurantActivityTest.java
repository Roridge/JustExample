package com.flowmellow.justexample.activities;

import android.content.Context;
import android.content.Intent;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.flowmellow.justexample.LocationUtil;
import com.flowmellow.justexample.MockAppController;
import com.flowmellow.justexample.R;
import com.flowmellow.justexample.TestUtil;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class RestaurantActivityTest extends AbstractActivityInstrumentationTestCase2<RestaurantsActivity> {

	private RestaurantsActivity activity;
	private Context context;
	
	private ProgressBar restaurantProgressBar;
	private ListView restaurantList;
	private MockAppController mockAppController;

	public RestaurantActivityTest() {
		super(RestaurantsActivity.class);
	}

	private void setupWithPostcode() {
		context = getInstrumentation().getTargetContext();
		final Intent intent = new Intent(context, RestaurantsActivity.class);
		intent.putExtra(RestaurantsActivity.POSTCODE_INTENT, LocationUtil.POSTCODE);
		setActivityIntent(intent);
		setup();
	}

	private void setup() {
		activity = getActivity();
		context = getInstrumentation().getContext();
		mockAppController = TestUtil.setupMockAppController(activity, context);
		//we want to ensure the MockAppController is picked up
		onCreate();
		//requests the restaurant data
		onStart();
		restaurantList = (ListView) activity.findViewById(R.id.restaurantList);
		restaurantProgressBar = (ProgressBar) activity.findViewById(R.id.restaurantProgressBar);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SmallTest
	public void testRestaurantListIsVisible() throws Throwable {
		setupWithPostcode();
		final int restaurantListVisability = restaurantList.getVisibility();
		final String errorMessage = "restaurant list view should be visible";
		assertEquals(errorMessage, View.VISIBLE, restaurantListVisability);
	}

	@SmallTest
	public void testLocationProgressBarIsGone() {
		setup();
		final int restaurantProgressBarVisability = restaurantProgressBar.getVisibility();
		final String errorMessage = "restaurant progress bar should not be visible if no postcode was passed in";
		assertEquals(errorMessage, View.GONE, restaurantProgressBarVisability);
	}

	@LargeTest
	public void testRestaurantListDisplayItemsInRatingOrder() throws Throwable {
		setupWithPostcode();
		
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
	

	private void onCreate() {
		try {
			runTestOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.onCreate(null);
				}
			});
		} catch (Throwable e) {
			Log.e(com.flowmellow.justexample.Config.LOG_TAG, "couldn't call activiy onCreate");
		}
	}
	

	private void onStart() {
		try {
			runTestOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.onStart();
				}
			});
		} catch (Throwable e) {
			Log.e(com.flowmellow.justexample.Config.LOG_TAG, "couldn't call activiy onStart");
		}
	}

}
