package com.flowmellow.justexample;

import android.location.Location;

public class LocationUtil {
	
	public static final String POSTCODE = "WD6 1JN";
	public static final String PROVIDER = "flp";
	public static final double LAT = 51.65814;
	public static final double LNG = -0.267029;
	public static final float ACCURACY = 3.0f;
	public static final long TIME = 1000l;
	public static final long ERTN = 1000l;
	
	public static Location createLocation() {
		return createLocation(LAT, LNG, ACCURACY, TIME, ERTN);
	}

	public static Location createLocation(double lat, double lng, float accuracy, long time, long ertn) {
		// Create a new Location
		Location newLocation = new Location(PROVIDER);
		newLocation.setLatitude(lat);
		newLocation.setLongitude(lng);
		newLocation.setAccuracy(accuracy);
		newLocation.setTime(time);
		newLocation.setElapsedRealtimeNanos(ertn);
		return newLocation;
	}
	
}
