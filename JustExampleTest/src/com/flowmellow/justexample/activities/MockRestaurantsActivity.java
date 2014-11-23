package com.flowmellow.justexample.activities;

import com.flowmellow.justexample.RestaurantUtil;

import android.content.ComponentName;
import android.os.IBinder;

public class MockRestaurantsActivity extends RestaurantsActivity {

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		displayRestaurants(RestaurantUtil.getMockRestaurants());
	}
}
