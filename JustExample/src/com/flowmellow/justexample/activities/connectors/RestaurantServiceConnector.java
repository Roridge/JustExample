package com.flowmellow.justexample.activities.connectors;

import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.flowmellow.justexample.services.BasicRestaurantService;
import com.flowmellow.justexample.services.RestaurantService;
import com.flowmellow.justexample.services.BasicRestaurantService.RestaurantBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class RestaurantServiceConnector implements ServiceConnection {
	private Context context;
	private RestaurantService restaurantService;
	private boolean isServiceBound;
	private RestaurantListener restaurantListener;
	private String postCode;

	public RestaurantServiceConnector(Context context) {
		this.context = context;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		RestaurantBinder binder = (RestaurantBinder) service;
		restaurantService = binder.getService();
		isServiceBound = true;
		requestRestaurant(postCode);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		isServiceBound = false;
	}

	public void bindToService(final RestaurantListener restaurantListener, final String postCode) {
		this.restaurantListener = restaurantListener;
		this.postCode = postCode;
		Intent intent = new Intent(context, BasicRestaurantService.class);
		context.bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	public void unbindFromService() {
		if (isServiceBound) {
			context.unbindService(this);
		}
	}

	public void requestRestaurant(String postcode) {
		if (isServiceBound) {
			restaurantService.requestRestaurant(postcode, restaurantListener);
		}
	}
}
