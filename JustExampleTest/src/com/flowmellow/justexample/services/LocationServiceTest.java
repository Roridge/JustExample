package com.flowmellow.justexample.services;

import com.flowmellow.justexample.LocationUtil;
import com.flowmellow.justexample.services.LocationService;
import com.flowmellow.justexample.services.LocationService.LocationBinder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class LocationServiceTest extends ServiceTestCase<LocationService> {

	private LocationService service;
	private Context context;

	public LocationServiceTest() {
		super(LocationService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getSystemContext();

	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test binding to service
	 */
	@MediumTest
	public void testLocationServiceBindable() {
		final Intent startIntent = new Intent();
		startIntent.setClass(getContext(), LocationService.class);
		final IBinder binder = bindService(startIntent);
		assertNotNull("binder not retreieved while using start intent on service", binder);
	}

	/**
	 * Test getting the service from a binder
	 */
	@MediumTest
	public void testgetLocationServiceFromBind() {
		final Intent startIntent = new Intent();
		startIntent.setClass(getContext(), LocationService.class);
		final IBinder binder = bindService(startIntent);
		final LocationBinder locationBinder = (LocationBinder) binder;
		service = locationBinder.getService();
		assertNotNull("service not retreieved from binder", service);
	}

	/**
	 * Test getting the postcode from a mockup location which is defind with
	 * constant latitude and longitude.
	 */
	@MediumTest
	public void testgetPostCodeFromLocation() {
		testgetLocationServiceFromBind();

		final Location mockLocation = LocationUtil.createLocation();
		final String postcode = service.getPostCode(context, mockLocation);
		assertEquals("postcode couldn't get retrieved from location or Borehamwood just moved", LocationUtil.POSTCODE, postcode);
	}
}
