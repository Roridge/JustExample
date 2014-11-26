package com.flowmellow.justexample.services;

import com.flowmellow.justexample.activities.listeners.RestaurantListener;

public interface RestaurantService {
	
	public void requestRestaurant(final String postcode, final RestaurantListener restaurantListener);
	
}
