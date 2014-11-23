package com.flowmellow.justexample.activities;

import java.util.ArrayList;

import com.flowmellow.justexample.RestaurantUtil;
import com.flowmellow.justexample.activities.to.RestaurantTO;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.LargeTest;

public class RestaurantActivityUnitTest extends ActivityUnitTestCase<MockRestaurantsActivity> {

	private MockRestaurantsActivity activity;
	private Context context;

	public RestaurantActivityUnitTest() {
		super(MockRestaurantsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getInstrumentation().getTargetContext();
		final Intent intent = new Intent(context, RestaurantsActivity.class);
		intent.putExtra(RestaurantsActivity.POSTCODE_INTENT, RestaurantUtil.POSTCODE);
		startActivity(intent, null, null);
		activity = getActivity();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@LargeTest
	public void test() {
		activity.onStart();
		activity.displayRestaurants(new ArrayList<RestaurantTO>());
	}

}
