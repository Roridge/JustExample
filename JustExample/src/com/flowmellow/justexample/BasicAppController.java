package com.flowmellow.justexample;

import com.flowmellow.justexample.activities.connectors.LocationServiceConnector;
import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.google.android.gms.location.LocationClient;

import android.content.Context;
import android.util.Log;

public class BasicAppController implements AppController {

	private static BasicAppController controller = null;
	private static LocationServiceConnector locationServiceConnector;

			
	static void boot(Context context){
		if(controller == null) {
			controller = new BasicAppController();
			locationServiceConnector = new LocationServiceConnector(context);
		} else {
			Log.e(Config.LOG_TAG, "AppController should only be constructed once.");
		}
	}

	public static AppController getController() {
		return controller;
	}

	public void requestPostcode(CustomLocationListener customLocationListener) {
		locationServiceConnector.requestPostcodeByLocation(customLocationListener);
	}
	
	public void locationServiceConnection(LocationClient locationClient) {
		locationServiceConnector.bindToService(locationClient);
	}
	
	public void locationServiceDisconnection() {
		locationServiceConnector.unbindFromService();
	}
}
