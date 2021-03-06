package com.flowmellow.justexample.activities.to;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.flowmellow.justexample.activities.to.RestaurantTO;

import junit.framework.TestCase;

public class RestaurantTOUnitTest extends TestCase {

	public void testRestaurantTOCompare() {

		final List<RestaurantTO> restaurants = new ArrayList<RestaurantTO>();
		restaurants.add(new RestaurantTO(1, "name1", 3.5F, 1, ""));
		restaurants.add(new RestaurantTO(2, "name2", 1.5F, 2, ""));
		restaurants.add(new RestaurantTO(3, "name3", 2.5F, 3, ""));
		restaurants.add(new RestaurantTO(4, "name4", 4.5F, 4, ""));
		
		// sort order by rating
		Collections.sort(restaurants);

		final Iterator<RestaurantTO> restaurantsSortedInterator = restaurants.iterator();
		test(1, restaurantsSortedInterator, 4);
		test(2, restaurantsSortedInterator, 1);
		test(3, restaurantsSortedInterator, 3);
		test(4, restaurantsSortedInterator, 2);
	}

	private void test(int position, Iterator<RestaurantTO> restaurantsInterator, long expectedId) {
		final String errorMessage = String.format(Locale.ENGLISH,"RestaurantTO at postion %d missing from list, test setup has been altered", position);
		assertTrue(errorMessage, restaurantsInterator.hasNext());

		final RestaurantTO restaurant = restaurantsInterator.next();
		final long actualRestaurantId = restaurant.getId();

		final String errorMessage2 = String.format(Locale.ENGLISH, "%s at postion %d is in the wrong order", restaurant.toString(), position);
		assertEquals(errorMessage2, expectedId, actualRestaurantId);
	}
}