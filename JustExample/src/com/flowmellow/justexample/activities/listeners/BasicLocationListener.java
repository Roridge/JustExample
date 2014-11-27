package com.flowmellow.justexample.activities.listeners;

import android.location.Location;

import com.flowmellow.justexample.activities.connectors.LocationServiceConnector;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

public class BasicLocationListener implements LocationListener {

	private CustomLocationListener customLocationListener;
	private LocationClient locationClient;
	private LocationServiceConnector locationServiceConnector;

	public BasicLocationListener(CustomLocationListener customLocationListener, LocationClient locationClient,
			LocationServiceConnector locationServiceConnector) {
		this.customLocationListener = customLocationListener;
		this.locationClient = locationClient;
		this.locationServiceConnector = locationServiceConnector;
	}

	/**
	 * The location must call through to {@code displayPostcode} even if the
	 * postcode is not found so that the {@code locationProgressBar} is
	 * handled correctly
	 */
	@Override
	public void onLocationChanged(Location location) {

		String postcode = null;

		if (location != null) {
			postcode = locationServiceConnector.getPostCode(location);
		}

		customLocationListener.displayPostcode(postcode);
		locationClient.removeLocationUpdates(this);
	}
}