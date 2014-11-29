package com.flowmellow.justexample.activities;

import android.content.Context;
import android.content.Intent;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flowmellow.justexample.LocationUtil;
import com.flowmellow.justexample.R;
import com.flowmellow.justexample.TestUtil;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class RestaurantActivityTest extends AbstractActivityInstrumentationTestCase2<RestaurantsActivity> {

	private RestaurantsActivity activity;
	private Context context;

	private ProgressBar restaurantProgressBar;
	private ListView restaurantList;
	private RelativeLayout noRestaurantsRelativeLayout;
	private TextView noRestaurantsHelperTextView;
	private Button searchAgainButton;

	public RestaurantActivityTest() {
		super(RestaurantsActivity.class);
	}
	
	private void setup(Setup withPostcode) {
		context = getInstrumentation().getContext();
		activity = getActivity();
		TestUtil.setupMockAppController(activity, context);
		
		if(withPostcode == Setup.WITH_POSTCODE) {
			Intent intent = activity.getIntent();
			intent.putExtra(RestaurantsActivity.POSTCODE_INTENT, LocationUtil.POSTCODE);
		}
		
		onCreate();
		onStart();
		
		restaurantList = (ListView) activity.findViewById(R.id.restaurantList);
		restaurantProgressBar = (ProgressBar) activity.findViewById(R.id.restaurantProgressBar);
		noRestaurantsRelativeLayout = (RelativeLayout) activity.findViewById(R.id.noRestaurantsRelativeLayout);
		noRestaurantsHelperTextView = (TextView) activity.findViewById(R.id.noRestaurantsHelperTextView);
		searchAgainButton = (Button) activity.findViewById(R.id.searchAgainButton);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SmallTest
	public void testRestaurantListIsVisible() {
		setup(Setup.WITH_POSTCODE);
		final int restaurantListVisability = restaurantList.getVisibility();
		final String errorMessage = "restaurant list view should be visible";
		assertEquals(errorMessage, View.VISIBLE, restaurantListVisability);
	}

	@SmallTest
	public void testRestaurantListIsGoneWithNoPostCode() {
		setup(Setup.WITHOUT_POSTCODE);
		final int restaurantListVisability = restaurantList.getVisibility();
		final String errorMessage = "restaurant list view should be gone if no postcode was passed in";
		assertEquals(errorMessage, View.GONE, restaurantListVisability);
	}

	@SmallTest
	public void testRestaurantProgressBarIsGone() {
		setup(Setup.WITH_POSTCODE);
		final int restaurantProgressBarVisability = restaurantProgressBar.getVisibility();
		final String errorMessage = "restaurant progress bar should not be visible";
		assertEquals(errorMessage, View.GONE, restaurantProgressBarVisability);
	}

	@SmallTest
	public void testRestaurantProgressBarIsGoneWithNoPostCode() {
		setup(Setup.WITHOUT_POSTCODE);
		final int restaurantProgressBarVisability = restaurantProgressBar.getVisibility();
		final String errorMessage = "restaurant progress bar should not be visible if no postcode was passed in";
		assertEquals(errorMessage, View.GONE, restaurantProgressBarVisability);
	}

	@SmallTest
	public void testNoRestaurantsRelativeLayoutIsGone() throws InterruptedException {
		setup(Setup.WITH_POSTCODE);
		final int noRestaurantsRelativeLayoutVisability = noRestaurantsRelativeLayout.getVisibility();
		final String errorMessage = "RelativeLayout should be gone if valid postcode was passed in";
		assertEquals(errorMessage, View.GONE, noRestaurantsRelativeLayoutVisability);
	}

	@SmallTest
	public void testNoRestaurantsRelativeLayoutIsVisibleWithNoPostcode() throws InterruptedException {
		setup(Setup.WITHOUT_POSTCODE);
		final int noRestaurantsRelativeLayoutVisability = noRestaurantsRelativeLayout.getVisibility();
		final String errorMessage = "RelativeLayout should be visible if no postcode was passed in";
		assertEquals(errorMessage, View.VISIBLE, noRestaurantsRelativeLayoutVisability);
	}

	@SmallTest
	public void testNoRestaurantsHelperTextViewIsDisplayingCorrectText() throws InterruptedException {
		setup(Setup.WITHOUT_POSTCODE);
		final String actualText = noRestaurantsHelperTextView.getText().toString();
		final String expectedText = activity.getString(R.string.helper_no_restaurants_found);
		final String errorMessage = "No Restaurants Helper TextView is displaying the wrong message to the user";
		assertEquals(errorMessage, expectedText, actualText);
	}

	@SmallTest
	public void testNoRestaurantsHelperButtonIsDisplayingCorrectText() throws InterruptedException {
		setup(Setup.WITHOUT_POSTCODE);
		final String actualText = searchAgainButton.getText().toString();
		final String expectedText = activity.getString(R.string.helper_no_restaurants_found_button);
		final String errorMessage = "No Restaurants Helper Button is displaying the wrong message to the user";
		assertEquals(errorMessage, expectedText, actualText);
	}

	@LargeTest
	public void testRestaurantListDisplayItemsInRatingOrder() {
		setup(Setup.WITH_POSTCODE);

		final ListView restaurantList = (ListView) activity.findViewById(R.id.restaurantList);

		final StringBuilder errorMessage = new StringBuilder();
		boolean error = false;

		int pos = -1;
		float lastRating = 100F;
		
		while(restaurantList.getItemAtPosition(++pos) != null) {
			final RestaurantTO restaurantTO = (RestaurantTO) restaurantList.getItemAtPosition(pos);
			final String errorMessageFormat = "item at postion [%d] rating was [%f] and the last item was [%f]";

			final float rating = restaurantTO.getRatingStars();
			
			if (rating > lastRating) {
				errorMessage.append(String.format(errorMessageFormat, pos, rating, lastRating));
				error = true;
			}
			
			lastRating = rating;
		}

		if (error) {
			fail(errorMessage.toString());
		}
	}

	@MediumTest
	public void testListItemsLogoImageViewsViewsAreVisible() {
		final int resIdForListItem = R.id.restaurantLogoImageView;
		testListItemVisible(resIdForListItem, "Logo ImageView");
	}

	@MediumTest
	public void testListItemsRestaurantNameTextViewsAreVisible() {
		final int resIdForListItem = R.id.restaurantNameTextView;
		testListItemVisible(resIdForListItem, "Restaurant Name TextView");
	}

	@MediumTest
	public void testListItemsRestaurantRatingBarsAreVisible() {
		final int resIdForListItem = R.id.restaurantRatingBar;
		testListItemVisible(resIdForListItem, "Restaurant RatingBar");
	}

	@MediumTest
	public void testListItemsNumberOfRatingTextViewsAreVisible() {
		final int resIdForListItem = R.id.numberOfRatingsTextView;
		testListItemVisible(resIdForListItem, "Number of ratings TextView");
	}
	
	private void testListItemVisible(int resIdForListItem, String itemName) {
		setup(Setup.WITH_POSTCODE);

		final ListView restaurantList = (ListView) activity.findViewById(R.id.restaurantList);
		
		final StringBuilder errorMessage = new StringBuilder();
		boolean error = false;

		int pos = -1;
		
		while(restaurantList.getChildAt(++pos) != null) {
			
			final LinearLayout view = (LinearLayout) restaurantList.getChildAt(pos);
			final View listItem = view.findViewById(resIdForListItem);

			final String errorMessageFormat = "Item postion [%d]. The %s is not visible";

			if (listItem.getVisibility() != View.VISIBLE) {
				errorMessage.append(String.format(errorMessageFormat, pos, itemName));
				error = true;
			}
		}

		if (error) {
			fail(errorMessage.toString());
		}
	}

	private void onCreate() {
		try {
			runTestOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.onCreate(null);
				}
			});
		} catch (Throwable e) {
			Log.e(com.flowmellow.justexample.Config.LOG_TAG, "couldn't call activiy onCreate");
		}
	}

	private void onStart() {
		try {
			runTestOnUiThread(new Runnable() {

				@Override
				public void run() {
					activity.onStart();
				}
			});
		} catch (Throwable e) {
			Log.e(com.flowmellow.justexample.Config.LOG_TAG, "couldn't call activiy onStart");
		}
	}
	
	enum Setup {
		WITH_POSTCODE, WITHOUT_POSTCODE;
	}

}
