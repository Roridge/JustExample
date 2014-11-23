package com.flowmellow.justexample.activities.to;

public class RestaurantTO implements Comparable<RestaurantTO> {

	private final long id;
	private final String name;
	private final float ratingStars;
	private final int numberOfRatings;
	private final String url;

	public RestaurantTO(long id, String name, float ratingStars, int numberOfRatings, String url) {
		this.id = id;
		this.name = name;
		this.ratingStars = ratingStars;
		this.numberOfRatings = numberOfRatings;
		this.url = url;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public float getRatingStars() {
		return this.ratingStars;
	}

	public int getNumberOfRatings() {
		return numberOfRatings;
	}

	public String getUrl() {
		return this.url;
	}

	/**
	 * Order by rating
	 */
	@Override
	public int compareTo(RestaurantTO restaurantTO) {
		// Compare on ratingStars
		final float comparable = restaurantTO.getRatingStars();
		final int result;

		if (this.ratingStars == comparable) {
			// Equal
			result = 0;
		} else if (this.ratingStars > comparable) {
			// Before
			result = -1;
		} else {
			// After
			result = 1;
		}

		return result;
	}

	@Override
	public String toString() {
		return "RestaurantTO [id=" + id + ", name=" + name + ", ratingStars=" + ratingStars + ", numberOfRatings=" + numberOfRatings
				+ ", url=" + url + "]";
	}
}
