package com.flowmellow.justexample.activities.adapters.image;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.flowmellow.justexample.Config;

import android.graphics.Bitmap;

public class ImageCache {

	private static final Map<String, Bitmap> orderedCache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

	public Bitmap getImage(String url) {
		Bitmap image = null;

		if (orderedCache.containsKey(url)) {
			image = orderedCache.get(url);
		}
		return image;
	}

	public void putImage(String url, Bitmap bitmap) {
		checkCacheSize();
		orderedCache.put(url, bitmap);
	}

	public void clear() {
		orderedCache.clear();
	}

	/**
	 * TODO nice to have, this could be implemented better to remove the oldest
	 * bitmaps (the map is ordered) if the memory is running low rather than
	 * clearing the whole thing.
	 */
	private void checkCacheSize() {

		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long customPercentage = (totalMemory / 100) * Config.PERCENTAGE_OF_FREE_MEMORY_TO_CLEAR_CACHE;
		
		//if less than n percent of the total memory is free, clear the cache
		if (freeMemory < customPercentage) {
			clear();
		}
	}
}