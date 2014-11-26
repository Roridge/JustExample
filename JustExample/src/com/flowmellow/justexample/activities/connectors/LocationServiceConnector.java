package com.flowmellow.justexample.activities.connectors;

import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.flowmellow.justexample.services.BasicLocationService;
import com.flowmellow.justexample.services.BasicLocationService.LocationBinder;
import com.flowmellow.justexample.services.LocationService;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;

public class LocationServiceConnector implements ServiceConnection {

	private Context context;
	private LocationService locationService;
	private boolean isServiceBound;
	private LocationListener locationListener;
	private CustomLocationListener customLocationListener;

	public LocationServiceConnector(Context context, LocationListener locationListener, CustomLocationListener customLocationListener) {
		this.context = context;
		this.locationListener = locationListener;
		this.customLocationListener = customLocationListener;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		LocationBinder binder = (LocationBinder) service;
		locationService = binder.getService();
		isServiceBound = true;
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		isServiceBound = false;
	}

	public void bindToService() {
		Intent intent = new Intent(context, BasicLocationService.class);
		context.bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	public void unbindFromService() {
		if (isServiceBound) {
			context.unbindService(this);
		}
	}

	public String getPostCode(Location location) {
		String postcode = null;

		if (isServiceBound) {
			postcode = locationService.getPostCode(context, location);
		}
		return postcode;
	}

	public void requestPostcodeByLocation(LocationClient locationClient) {
		if (isServiceBound) {
			locationService.requestPostcodeByLocation(context, locationListener, customLocationListener, locationClient);
		}
	}
}