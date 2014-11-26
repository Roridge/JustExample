package com.flowmellow.justexample.services;

import java.util.List;

import com.flowmellow.justexample.LocationUtil;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.flowmellow.justexample.activities.to.RestaurantTO;
import com.flowmellow.justexample.services.BasicRestaurantService;
import com.flowmellow.justexample.services.BasicRestaurantService.RestaurantBinder;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

public class RestaurantServiceTest extends ServiceTestCase<BasicRestaurantService> {

	private BasicRestaurantService service;

	public RestaurantServiceTest() {
		super(BasicRestaurantService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test binding to service
	 */
	@MediumTest
	public void testRestaurantServiceBindable() {
		final Intent startIntent = new Intent();
		startIntent.setClass(getContext(), BasicRestaurantService.class);
		final IBinder binder = bindService(startIntent);
		assertNotNull("binder not retreieved while using start intent on service", binder);
	}

	/**
	 * Test getting the service from a binder
	 */
	@MediumTest
	public void testgetRestaurantServiceFromBind() {
		final Intent startIntent = new Intent();
		startIntent.setClass(getContext(), BasicRestaurantService.class);
		final IBinder binder = bindService(startIntent);
		final RestaurantBinder restaurantBinder = (RestaurantBinder) binder;
		service = restaurantBinder.getService();
		assertNotNull("service not retreieved from binder", service);
	}
	
	@LargeTest
	public void testRequestRestaurant() {
		
		RestaurantListener restaurantListener = new RestaurantListener() {
			
			@Override
			public void displayRestaurants(List<RestaurantTO> results) {
				String i = new String();
				Log.d("", i);
			}
		};
		
		testgetRestaurantServiceFromBind();
		service.requestRestaurant(LocationUtil.POSTCODE, restaurantListener);
	}
}
