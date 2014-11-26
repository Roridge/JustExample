package com.flowmellow.justexample.activities;

import com.flowmellow.justexample.Config;
import com.flowmellow.justexample.R;
import com.flowmellow.justexample.activities.connectors.LocationServiceConnector;
import com.flowmellow.justexample.activities.listeners.CustomLocationListener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

public class HomeActivity extends Activity implements OnClickListener, LocationListener, CustomLocationListener,
		GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private ImageButton locationImageButton;
	private ImageButton searchImageButton;
	private EditText searchEditText;
	private ProgressBar locationProgressBar;

	private LocationClient locationClient;
	private LocationServiceConnector locationServiceConnector;
	private LocationManager locationManager;
	private boolean isLocationClientConnected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		locationImageButton = (ImageButton) findViewById(R.id.locationImageButton);
		searchImageButton = (ImageButton) findViewById(R.id.searchImageButton);
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		locationProgressBar = (ProgressBar) findViewById(R.id.locationProgressBar);

		locationImageButton.setOnClickListener(this);
		searchImageButton.setOnClickListener(this);

		locationClient = getLocationClient();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationServiceConnector = new LocationServiceConnector(this, this, this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// connect to google play services location client
		locationClient.connect();

		// Bind to LocationService
		locationServiceConnector.bindToService();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (locationClient.isConnected()) {

			// remove any location updates
			locationClient.removeLocationUpdates(this);

			// disconnect to google play services location client
			locationClient.disconnect();
		}

		// Unbind from LocationService
		locationServiceConnector.unbindFromService();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.locationImageButton:
			requestPostcode();
			break;

		case R.id.searchImageButton:
			requestRestaurantsByLocation();
			break;

		default:
			// do nothing
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:

			switch (resultCode) {

			/* If the result code is Activity.RESULT_OK, try to connect again */
			case Activity.RESULT_OK:

				if (!isLocationClientConnected) {
					locationClient.connect();
				}
				break;
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		isLocationClientConnected = false;

		if (connectionResult.hasResolution()) {

			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				Log.w(Config.LOG_TAG, "Google Play services has cancelled the orignal PendingIntent " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		isLocationClientConnected = true;
	}

	@Override
	public void onDisconnected() {
		isLocationClientConnected = false;
	}

	/**
	 * The location must call through to {@code displayPostcode} even if the
	 * postcode is not found so that the {@code locationProgressBar} is handled
	 * correctly
	 */
	@Override
	public void onLocationChanged(Location location) {

		String postcode = null;

		if (location != null) {
			postcode = locationServiceConnector.getPostCode(location);
		}

		displayPostcode(postcode);
		locationClient.removeLocationUpdates(this);
	}

	/**
	 * Display postcode requested in {@code searchEditText}, hide spinner.
	 */
	@Override
	public void displayPostcode(String postcode) {

		locationProgressBar.setVisibility(View.GONE);

		if (postcode != null) {
			searchEditText.setText(postcode);
		} else {
			Toast.makeText(this, getString(R.string.error_postcode_not_found), Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Request postcode, show spinner.
	 */
	public void requestPostcode() {

		// short circuit if locationManager is not connected
		if (isGPSProviderDisabled()) {
			showNoLocationWarningDialog();
			return;
		}

		locationProgressBar.setVisibility(View.VISIBLE);
		locationServiceConnector.requestPostcodeByLocation(locationClient);
	}

	/**
	 * Start the {@link RestaurantsActivity} with postcode in
	 * {@code searchEditText}
	 */
	private void requestRestaurantsByLocation() {

		// short circuit if locationManager is not connected
		if (isNetworkProviderDisabled()) {
			showNoLocationWarningDialog();
			return;
		}
		
		String postcode = searchEditText.getText().toString();

		if (postcode != null && !postcode.isEmpty()) {
			// remove any spaces
			postcode = postcode.replaceAll("\\s+", "");

			// restrict strings larger than max postcode
			if (postcode.length() > Config.MAX_POSTCODE_LENGTH) {
				postcode = postcode.substring(0, Config.MAX_POSTCODE_LENGTH);
			}

			// Explicit intent with postcode to display restaurants
			final Intent intent = new Intent(this, RestaurantsActivity.class);
			intent.putExtra(RestaurantsActivity.POSTCODE_INTENT, postcode);
			startActivity(intent);

		} else {
			Toast.makeText(this, getString(R.string.helper_missing_postcode), Toast.LENGTH_LONG).show();
		}
	}

	protected void showNoLocationWarningDialog() {

		final DialogInterface.OnClickListener loadSettings = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		};

		final DialogInterface.OnClickListener cancelDialog = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.cancel();
			}
		};

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false).setTitle(getString(R.string.helper_location_disabled_title))
				.setMessage(getString(R.string.helper_location_disabled_message))
				.setPositiveButton(getString(R.string.helper_location_disabled_settings), loadSettings)
				.setNegativeButton(getString(R.string.helper_location_disabled_cancel), cancelDialog);

		final AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Get the location client, could be exposed to package private for testing.
	 * 
	 * @return {@code LocationClient}
	 */
	private LocationClient getLocationClient() {
		return new LocationClient(this, this, this);
	}

	/**
	 * Check if gps provider is Disabled
	 * 
	 * @return {@code true} if disabled, {@code false} if enabled.
	 */
	protected boolean isGPSProviderDisabled() {
		return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Check if network provider is Disabled
	 * 
	 * @return {@code true} if disabled, {@code false} if enabled.
	 */
	protected boolean isNetworkProviderDisabled() {
		return !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
}