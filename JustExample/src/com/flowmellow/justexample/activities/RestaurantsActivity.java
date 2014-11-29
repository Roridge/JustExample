package com.flowmellow.justexample.activities;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.flowmellow.justexample.AppController;
import com.flowmellow.justexample.DefaultApplication;
import com.flowmellow.justexample.R;
import com.flowmellow.justexample.activities.adapters.LazyRestaurantAdapter;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class RestaurantsActivity extends Activity implements RestaurantListener {

	public static final String POSTCODE_INTENT = "POSTCODE_INTENT";

	private ListView restaurantList;
	private ProgressBar restaurantProgressBar;
	private LazyRestaurantAdapter adapter;
	private String postcode;
	private AppController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurants_list);
		controller = ((DefaultApplication) this.getApplication()).getController();

		restaurantList = (ListView) findViewById(R.id.restaurantList);
		restaurantProgressBar = (ProgressBar) findViewById(R.id.restaurantProgressBar);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Bind to RestaurantService
		controller.restaurantServiceConnection(this);

		startRequest();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Unbind from RestaurantService
		controller.restaurantServiceDisconnection();
	}

	private void startRequest() {

		final Intent intent = getIntent();

		if (intent.hasExtra(POSTCODE_INTENT)) {
			postcode = intent.getStringExtra(POSTCODE_INTENT);
		}

		if (postcode != null) {
			controller.requestRestaurant(postcode);
		} else {
			noRestaurantsFound();
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
}