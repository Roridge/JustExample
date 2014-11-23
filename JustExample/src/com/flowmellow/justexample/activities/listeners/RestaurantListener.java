package com.flowmellow.justexample.activities.listeners;

import java.util.List;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public interface RestaurantListener {

	public void displayRestaurants(List<RestaurantTO> results);

}
