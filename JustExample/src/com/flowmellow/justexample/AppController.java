package com.flowmellow.justexample;

import com.flowmellow.justexample.activities.listeners.CustomLocationListener;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.google.android.gms.location.LocationClient;
// TODO I would expect to see JavaDoc here and all the methods.
public interface AppController {

	public void requestPostcode(CustomLocationListener customLocationListener);

	public void locationServiceConnection(LocationClient locationClient);
	
	public void locationServiceDisconnection();
	
	public void restaurantServiceConnection(final RestaurantListener restaurantListener, final String postcode);
	
	public void restaurantServiceDisconnection();
	
	/**
	 * Check if gps provider is Disabled
	 * 
	 * @return {@code true} if disabled, {@code false} if enabled.
	 */
	public boolean isGPSProviderDisabled();

	/**
	 * Check if network provider is Disabled
	 * 
	 * @return {@code true} if disabled, {@code false} if enabled.
	 */
	public boolean isNetworkProviderDisabled();
	
}
