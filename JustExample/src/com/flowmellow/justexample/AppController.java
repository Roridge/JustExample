package com.flowmellow.justexample;

import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.google.android.gms.location.LocationClient;

public interface AppController {

	public void requestPostcode(CustomLocationListener customLocationListener);
	
	public void locationServiceConnection(LocationClient locationClient);
	
	public void locationServiceDisconnection();
	
}
