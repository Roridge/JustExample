package com.flowmellow.justexample.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.flowmellow.justexample.Config;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class RestaurantConverter {

	public List<RestaurantTO> convertRestaurantResponse(final String json) {
		final List<RestaurantTO> resturants = new ArrayList<RestaurantTO>();

		try {

			final JSONObject reader = new JSONObject(json);
			final JSONArray resturantsArray = reader.getJSONArray("Restaurants");

			for (int i = 0; i < resturantsArray.length(); i++) {
				final JSONObject restaurant = (JSONObject) resturantsArray.get(i);
				resturants.add(extractRestaurant(restaurant));
			}

		} catch (Exception e) {
			Log.e(Config.LOG_TAG, "Error Executing HTTP get Request. " + e.getMessage());
			e.printStackTrace();
		}
		return resturants;
	}

	private RestaurantTO extractRestaurant(final JSONObject restaurant) throws JSONException {
		final long id =  restaurant.getLong("Id");
		final String name = restaurant.getString("Name");
		final float ratingStars = (float) restaurant.getDouble("RatingStars");
		final int numberOfRatings = restaurant.getInt("NumberOfRatings");
		final JSONArray logoArray = restaurant.getJSONArray("Logo");
		final String imageUrl = extractLogoUrl(logoArray);
		final RestaurantTO restaurantTO = new RestaurantTO(id, name, ratingStars, numberOfRatings, imageUrl);
		return restaurantTO;
	}

	private String extractLogoUrl(JSONArray logoArray) throws JSONException {
		String imageUrl = null;

		for (int j = 0; j < logoArray.length(); j++) {
			final JSONObject logos = (JSONObject) logoArray.get(j);
			imageUrl = logos.getString("StandardResolutionURL");

			if (imageUrl != null) {
				break;
			}
		}
		return imageUrl;
	}
}