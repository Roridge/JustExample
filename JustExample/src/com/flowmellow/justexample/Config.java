package com.flowmellow.justexample;

import org.apache.http.client.methods.HttpUriRequest;

public class Config {

	public static final String LOG_TAG = "JustTag";
	public static final String RESTAURANTS_URI = "http://api-interview.just-eat.com/restaurants?q=";

	public static final int MAX_POSTCODE_LENGTH = 8;
	public static final int SHORTEST_POSTCODE_LENGTH = 5;

	/**
	 * This is used to define how much free memory should exist on the device
	 * until we clear our cache. Warning setting this to zero could cause out of
	 * memory exceptions since we will not be checking before caching data.
	 */
	public static final int PERCENTAGE_OF_FREE_MEMORY_TO_CLEAR_CACHE = 2;

	public static final void setHeaders(HttpUriRequest httpget) {
		httpget.setHeader("Accept-Tenant", "uk");
		httpget.setHeader("Accept-Language", "en-GB");
		httpget.setHeader("Accept-Charset", "utf-8");
		httpget.setHeader("Authorization", "Basic VGVjaFRlc3RBUEk6dXNlcjI=");
		httpget.setHeader("Host", "api-interview.just-eat.com");
	}
}
