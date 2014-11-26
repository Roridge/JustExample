package com.flowmellow.justexample.activities;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.flowmellow.justexample.R;
import com.flowmellow.justexample.activities.adapters.LazyRestaurantAdapter;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.flowmellow.justexample.activities.to.RestaurantTO;
import com.flowmellow.justexample.services.RestaurantService;
import com.flowmellow.justexample.services.RestaurantService.RestaurantBinder;

public class RestaurantsActivity extends Activity implements RestaurantListener, ServiceConnection {

	public static final String POSTCODE_INTENT = "POSTCODE_INTENT";

	private ListView restaurantList;
	private ProgressBar restaurantProgressBar;
	private LazyRestaurantAdapter adapter;

	private RestaurantService restaurantService;

	private boolean isRestaurantServiceBound = false;
	private String postcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurants_list);

		restaurantList = (ListView) findViewById(R.id.restaurantList);
		restaurantProgressBar = (ProgressBar) findViewById(R.id.restaurantProgressBar);

		final Intent intent = getIntent();

		if (intent.hasExtra(POSTCODE_INTENT)) {
			postcode = intent.getStringExtra(POSTCODE_INTENT);
		}

		if (postcode == null) {
			noRestaurantsFound();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		// Bind to RestaurantService
		final Intent bindIntent = new Intent(this, RestaurantService.class);
		bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Unbind from RestaurantService
		if (isRestaurantServiceBound) {
			unbindService(this);
			isRestaurantServiceBound = false;
		}
	}

	protected void noRestaurantsFound() {
		restaurantProgressBar.setVisibility(View.GONE);

	}

	@Override
	public void displayRestaurants(List<RestaurantTO> restaurants) {
		Collections.sort(restaurants);
		restaurantProgressBar.setVisibility(View.GONE);
		adapter = new LazyRestaurantAdapter(this, restaurants);
		restaurantList.setAdapter(adapter);
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		RestaurantBinder binder = (RestaurantBinder) service;
		restaurantService = binder.getService();
		isRestaurantServiceBound = true;
		restaurantService.requestRestaurant(postcode, this);
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		isRestaurantServiceBound = false;
	}
}