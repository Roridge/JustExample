package com.flowmellow.justexample.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.flowmellow.justexample.Config;
import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BasicLocationService extends Service implements LocationService {

	// Binder given to clients
	private final IBinder binder = new LocationBinder();

	public class LocationBinder extends Binder {

		public BasicLocationService getService() {
			return BasicLocationService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/**
	 * 
	 * @param locationClient
	 *            used to get users location
	 * @param activity
	 *            context used to run on the UI thread
	 * @param customLoctionListener
	 *            callback invoked to return postcode to activity
	 * @param locationListener
	 */
	@Override
	public void requestPostcodeByLocation(final Context context, final LocationClient locationClient,
			final CustomLocationListener customLoctionListener, LocationListener locationListener) {

		// short circuit if locationClient is not connected
		if (!locationClient.isConnected()) {
			Log.e(Config.LOG_TAG, "LocationClient is disconnected");
			return;
		}

		String postcode = null;
		final Location location = locationClient.getLastLocation();

		if (location != null) {
			postcode = getPostCode(context, location);

			if (postcode != null) {
				customLoctionListener.displayPostcode(postcode);
			}
		}

		if (location == null || postcode == null) {
			// request location if unavailable
			final LocationRequest locationrequest = LocationRequest.create();
			locationrequest.setInterval(2000L);
			locationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationClient.requestLocationUpdates(locationrequest, locationListener);
		}
	}

	/**
	 * Get post code from location
	 * 
	 * @param context
	 * @param location
	 * @return
	 */
	public String getPostCode(final Context context, final Location location) {
		List<Address> addresses = null;

		try {
			final Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
		} catch (IOException e) {
			Log.e(Config.LOG_TAG, "IO Exception in getFromLocation(): " + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			String errorString = "Illegal arguments " + Double.toString(location.getLatitude()) + " , "
					+ Double.toString(location.getLongitude()) + " passed to address service : " + e.getMessage();
			Log.e(Config.LOG_TAG, errorString);
			e.printStackTrace();
		}

		// if we found some address, search them for the best postcode
		final String postcode = (addresses != null) ? findPostcode(addresses) : null;
		return postcode;
	}

	/**
	 * Search the address for the best postcode.
	 * 
	 * @return postcode
	 */
	private String findPostcode(final List<Address> addresses) {
		String postcode = null;
		String partialPostCode = null;

		for (Address address : addresses) {
			postcode = address.getPostalCode();

			if (postcode != null) {
				// postcode may not
				if (partialPostCode == null && postcode.length() < Config.SHORTEST_POSTCODE_LENGTH) {
					partialPostCode = postcode;
				} else if (postcode.length() >= Config.SHORTEST_POSTCODE_LENGTH) {
					// found full postcode
					break;
				}
			}
		}

		// TODO check here that the partialPostCode matches the start of the
		// post code.
		// If it doesn't then the partialPostCode is more accurate since it was
		// found first and this should be returned.

		if (postcode == null) {
			postcode = partialPostCode;
		}
		return postcode;
	}
}