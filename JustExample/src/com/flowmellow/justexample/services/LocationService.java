package com.flowmellow.justexample.services;

import android.content.Context;
import android.location.Location;

import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

public interface LocationService {

	public void requestPostcodeByLocation(Context context, LocationClient locationClient, CustomLocationListener customLoctionListener,
			LocationListener locationListener);

	public String getPostCode(final Context context, final Location location);
}
