package com.flowmellow.justexample;

import com.flowmellow.justexample.activities.connectors.LocationServiceConnector;
import com.flowmellow.justexample.activities.connectors.RestaurantServiceConnector;
import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.google.android.gms.location.LocationClient;

import android.content.Context;
import android.location.LocationManager;

public class BasicAppController implements AppController {

	private LocationServiceConnector locationServiceConnector;
	private RestaurantServiceConnector restaurantServiceConnector;
	private LocationManager locationManager;

	BasicAppController(Context context) {
		locationServiceConnector = new LocationServiceConnector(context);
		restaurantServiceConnector = new RestaurantServiceConnector(context);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void requestPostcode(CustomLocationListener customLocationListener) {
		locationServiceConnector.requestPostcodeByLocation(customLocationListener);
	}

	@Override
	public void locationServiceConnection(LocationClient locationClient) {
		locationServiceConnector.bindToService(locationClient);
	}

	@Override
	public void locationServiceDisconnection() {
		locationServiceConnector.unbindFromService();
	}

	@Override
	public void restaurantServiceConnection(final RestaurantListener restaurantListener, final String postcode) {
		restaurantServiceConnector.bindToService(restaurantListener, postcode);
	}

	@Override
	public void restaurantServiceDisconnection() {
		restaurantServiceConnector.unbindFromService();
	}

	/**
	 * Check if gps provider is Disabled
	 * 
	 * @return {@code true} if disabled, {@code false} if enabled.
	 */
	public boolean isGPSProviderDisabled() {
		return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Check if network provider is Disabled
	 * 
	 * @return {@code true} if disabled, {@code false} if enabled.
	 */
	public boolean isNetworkProviderDisabled() {
		return !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
}
