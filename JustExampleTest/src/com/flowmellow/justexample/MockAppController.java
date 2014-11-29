package com.flowmellow.justexample;

import android.content.Context;
import android.util.Log;

import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.google.android.gms.location.LocationClient;

public class MockAppController extends BasicAppController {

	private boolean isGPSProviderDisabled = false;
	private boolean isNetworkProviderDisabled = false;
	private String postCodePassedInToService;

	public MockAppController(Context context) {
		super(context);
	}

	@Override
	public void requestPostcode(CustomLocationListener customLocationListener) {
		Log.i(Config.LOG_TAG, "requestPostcode");
		// return postcode mocking a successful response
		customLocationListener.displayPostcode(LocationUtil.POSTCODE);
	}

	@Override
	public void locationServiceConnection(LocationClient locationClient) {
		Log.i(Config.LOG_TAG, "locationServiceConnection");
	}

	@Override
	public void locationServiceDisconnection() {
		Log.i(Config.LOG_TAG, "locationServiceDisconnection");
	}

	@Override
	public void restaurantServiceConnection(RestaurantListener restaurantListener, String postcode) {
		Log.i(Config.LOG_TAG, "restaurantServiceConnection");

		postCodePassedInToService = postcode;
		
		if (postcode.equals(LocationUtil.POSTCODE)) {
			restaurantListener.displayRestaurants(RestaurantUtil.getMockRestaurants());
		}
	}

	@Override
	public void restaurantServiceDisconnection() {
		Log.i(Config.LOG_TAG, "restaurantServiceDisconnection");
	}

	@Override
	public boolean isGPSProviderDisabled() {
		return isGPSProviderDisabled;
	}

	@Override
	public boolean isNetworkProviderDisabled() {
		return isNetworkProviderDisabled;
	}

	public void setGPSProviderDisabled(boolean isGPSProviderDisabled) {
		this.isGPSProviderDisabled = isGPSProviderDisabled;
	}

	public void setNetworkProviderDisabled(boolean isNetworkProviderDisabled) {
		this.isNetworkProviderDisabled = isNetworkProviderDisabled;
	}

	public String getPostCodePassedInToService() {
		return postCodePassedInToService;
	}

}
