package com.flowmellow.justexample.activities.connectors;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.flowmellow.justexample.activities.listeners.BasicLocationListener;
import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.flowmellow.justexample.services.BasicLocationService;
import com.flowmellow.justexample.services.BasicLocationService.LocationBinder;
import com.flowmellow.justexample.services.LocationService;
import com.google.android.gms.location.LocationClient;

public class LocationServiceConnector implements ServiceConnection {

	private Context context;
	private LocationClient locationClient;
	private LocationService locationService;
	private boolean isServiceBound = false;

	public LocationServiceConnector(Context context) {
		this.context = context;
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

	public void bindToService(LocationClient locationClient) {
		this.locationClient = locationClient;
		Intent intent = new Intent(context, BasicLocationService.class);
		context.bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	public void unbindFromService() {
		if (isServiceBound) {
			context.unbindService(this);
		} else {
			Log.w(com.flowmellow.justexample.Config.LOG_TAG, "Attempting to unbind but service was never bound");
		}
	}

	public String getPostCode(Location location) {
		String postcode = null;

		if (isServiceBound) {
			postcode = locationService.getPostCode(context, location);
		}
		return postcode;
	}

	public void requestPostcodeByLocation(CustomLocationListener customLocationListener) {
		if (isServiceBound) {
			final BasicLocationListener basicLocationListener = new BasicLocationListener(customLocationListener, locationClient, this);
			locationService.requestPostcodeByLocation(context, locationClient, customLocationListener, basicLocationListener);
		} else {
			Log.e(com.flowmellow.justexample.Config.LOG_TAG, "Service not bound");
		}
	}
}