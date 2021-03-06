package com.flowmellow.justexample;

import java.util.ArrayList;
import java.util.List;

import com.flowmellow.justexample.activities.to.RestaurantTO;

public class RestaurantUtil {

	public static final String ONLINE_URL = "http://cdn.sstatic.net/stackexchange/img/logos/so/so-icon.png";

	public static List<RestaurantTO> getMockRestaurants() {
		/*
		 * When sorted in order of star rating the order will be 0,1,2,3,4,5,6,7
		 * where zero is highest so should appear at the top of the screen.
		 */
		List<RestaurantTO> restaurants = new ArrayList<RestaurantTO>();
		restaurants.add(new RestaurantTO(7, "Restaurant7", 0.5F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(1, "Restaurant1", 4.5F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(0, "Restaurant0", 5.5F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(4, "Restaurant4", 2.5F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(2, "Restaurant2", 4.0F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(6, "Restaurant6", 1.0F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(5, "Restaurant5", 2.0F, 111, ONLINE_URL));
		restaurants.add(new RestaurantTO(3, "Restaurant3", 3.0F, 111, ONLINE_URL));
		return restaurants;
	}
}
