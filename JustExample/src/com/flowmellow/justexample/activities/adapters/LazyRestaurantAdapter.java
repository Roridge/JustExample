package com.flowmellow.justexample.activities.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flowmellow.justexample.R;
import com.flowmellow.justexample.activities.adapters.image.ImageLoader;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class LazyRestaurantAdapter extends BaseAdapter {

	private final Context context;
	private final List<RestaurantTO> restaurants;
	private final LayoutInflater inflater;
	private final ImageLoader imageLoader;

	public LazyRestaurantAdapter(final Context context, final List<RestaurantTO> restaurants) {
		this.context = context;
		this.restaurants = restaurants; // TODO defensivly program to avoid null being passed in
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = new ImageLoader();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final RestaurantTO restaurantTO = restaurants.get(position);
		final ViewGroup noParent = null;
		final View view = (convertView != null) ? convertView : inflater.inflate(R.layout.restaurant_detail_view, noParent);

		final TextView restaurantNameTextView = (TextView) view.findViewById(R.id.restaurantNameTextView);
		final TextView numberOfRatingsTextView = (TextView) view.findViewById(R.id.numberOfRatingsTextView);
		final RatingBar restaurantRatingBar = (RatingBar) view.findViewById(R.id.restaurantRatingBar);
		final ImageView restaurantLogoImageView = (ImageView) view.findViewById(R.id.restaurantLogoImageView);

		restaurantNameTextView.setText(restaurantTO.getName());
		numberOfRatingsTextView.setText(String.format(context.getString(R.string.number_of_ratings_format),
				restaurantTO.getNumberOfRatings()));
		restaurantRatingBar.setRating(restaurantTO.getRatingStars());
		imageLoader.displayImage(restaurantLogoImageView, restaurantTO.getUrl());

		return view;
	}

	public int getCount() {
		return restaurants.size();
	}
	// TODO  document to return null and simplfy
	public RestaurantTO getItem(int position) {
		return (restaurants.size() > position) ? restaurants.get(position) : null;
	}

	public long getItemId(int position) {
		// TODO simplifed
		final RestaurantTO restaurantTO = getItem(position);
		return (restaurantTO != null) ? id = restaurantTO.getId() S -1;
	}
}
