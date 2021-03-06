package com.flowmellow.justexample.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.flowmellow.justexample.Config;
import com.flowmellow.justexample.activities.listeners.RestaurantListener;
import com.flowmellow.justexample.activities.to.RestaurantTO;

public class BasicRestaurantService extends Service implements RestaurantService {
	
	private static final int MAX_THREADS = 4;
	private final Handler handler = new Handler();
	private final IBinder binder = new RestaurantBinder();
	private final RestaurantConverter converter = new RestaurantConverter();
	private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

	public class RestaurantBinder extends Binder {

		public BasicRestaurantService getService() {
			return BasicRestaurantService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void requestRestaurant(final String postcode, final RestaurantListener restaurantListener) {
		final String url = Config.RESTAURANTS_URI + postcode;
		executorService.execute(new RestaurantRetrieverTask(url, restaurantListener));
	}

	/**
	 * Post the ImageDisplayRunnable in a handler to be displayed on the UI
	 * thread.
	 */
	private void callback(final  List<RestaurantTO> Restaurants, final RestaurantListener restaurantListener) {
		final RestaurantCallBackTask restaurantCallBackTask = new RestaurantCallBackTask(Restaurants, restaurantListener);
		handler.post(restaurantCallBackTask);
	}

	private class RestaurantRetrieverTask implements Runnable {

		private String url;
		private RestaurantListener restaurantListener;
		
		private RestaurantRetrieverTask(String url, RestaurantListener restaurantListener) {
			this.url = url;
			this.restaurantListener = restaurantListener;
		}
		
		@Override
		public void run() {
			
			final List<RestaurantTO> restaurants = new ArrayList<RestaurantTO>();
			String response = null;

			if ((url != null)) {
				
				final HttpClient client = new DefaultHttpClient();
				final HttpUriRequest httpget = new HttpGet(url);
				Config.setHeaders(httpget);

				try {
					// Execute HTTP Get Request
					final HttpResponse httpResponse = client.execute(httpget);
					final BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
					response = reader.readLine();

				} catch (Exception e) {
					Log.e(Config.LOG_TAG, "Error Executing HTTP get Request. " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(response != null) {
				restaurants.addAll(converter.convertRestaurantResponse(response));
			}

			callback(restaurants, restaurantListener);
		}
	}

	/**
	 * Return the restaurants on the UI Thread
	 */
	class RestaurantCallBackTask implements Runnable {

		private final List<RestaurantTO> Restaurants;
		private final RestaurantListener restaurantListener;

		protected RestaurantCallBackTask(final List<RestaurantTO> Restaurants, final RestaurantListener restaurantListener) {
			this.Restaurants = Restaurants;
			this.restaurantListener = restaurantListener;
		}

		@Override
		public void run() {
			restaurantListener.displayRestaurants(Restaurants);
		}
	}
}