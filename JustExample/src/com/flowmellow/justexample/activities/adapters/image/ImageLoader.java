package com.flowmellow.justexample.activities.adapters.image;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flowmellow.justexample.Config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	private static final int MAX_THREADS = 6;
	private final Handler handler = new Handler();
	private final ImageCache imageCache = new ImageCache();
	private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
	private final Map<ImageView, String> imageViewUrlMap = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

	/**
	 * Get image from cache and render it, otherwise go load it, cache it then
	 * display it.
	 */
	public void displayImage(ImageView imageView, String url) {
		imageViewUrlMap.put(imageView, url);
		final Bitmap image = imageCache.getImage(url);

		if (image == null) {
			queueForLoad(imageView, url);
		} else {
			imageView.setImageBitmap(image);
		}
	}

	/**
	 * Prepare to get a bitmap image from a url.
	 */
	private void queueForLoad(ImageView imageView, String url) {
		final ImageLoaderQueueItem queueItem = new ImageLoaderQueueItem(imageView, url);
		final ImageRetrieverTask queueTask = new ImageRetrieverTask(queueItem);
		executorService.execute(queueTask);
	}

	/**
	 * Cache the image
	 */
	private void cacheImage(final ImageLoaderQueueItem queueItem, final Bitmap bitmap) {
		imageCache.putImage(queueItem.getUrl(), bitmap);
	}

	/**
	 * Post the ImageDisplayRunnable in a handler to be displayed on the UI
	 * thread.
	 */
	private void display(final ImageLoaderQueueItem queueItem, final Bitmap bitmap) {
		final ImageDisplayTask displayTask = new ImageDisplayTask(queueItem, bitmap);
		handler.post(displayTask);
	}

	/**
	 * Retrieve a bitmap from a url using the web.
	 */
	private Bitmap getBitmap(String url) {
		Bitmap bitmap = null;

		try {
			final InputStream inputStream = new URL(url).openStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (Throwable ex) {
			Log.w(Config.LOG_TAG, "Error downloading or decoding a logo for url: " + url);
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError) {
				clearCache();
			}
		}
		return bitmap;
	}

	/**
	 * purge all images from the cache.
	 */
	private void clearCache() {
		imageCache.clear();
	}

	/**
	 * Checks if the image view has been reused since first coming into the
	 * ImageLoader
	 * 
	 * @return {@code true} ImageView has been reused otherwise {@code false}.
	 */
	boolean hasImageViewBeenReused(final ImageLoaderQueueItem queueItem) {
		final ImageView key = queueItem.getImageView();
		final String url = queueItem.getUrl();
		final String urlValue = imageViewUrlMap.get(key);

		if (urlValue != null && urlValue.equals(url)) {
			// still the same as original
			return false;
		}
		return true;
	}

	/**
	 * Retrieve image, cache it, display it.
	 */
	class ImageRetrieverTask implements Runnable {

		private final ImageLoaderQueueItem queueItem;

		protected ImageRetrieverTask(final ImageLoaderQueueItem queueItem) {
			this.queueItem = queueItem;
		}

		@Override
		public void run() {

			//Don't get image if the view has been reused.
			if(hasImageViewBeenReused(queueItem)) {
				return;
			}

			final String url = queueItem.getUrl();
			final Bitmap image = getBitmap(url);

			if (image != null) {
				cacheImage(queueItem, image);
				display(queueItem, image);
			}
		}
	}

	/**
	 * Display the image on the UI Thread
	 */
	class ImageDisplayTask implements Runnable {

		private final ImageLoaderQueueItem queueItem;
		private final Bitmap bitmap;

		protected ImageDisplayTask(final ImageLoaderQueueItem queueItem, final Bitmap bitmap) {
			this.queueItem = queueItem;
			this.bitmap = bitmap;
		}

		@Override
		public void run() {
			
			//Don't set image if the view has been reused.
			if(hasImageViewBeenReused(queueItem)) {
				return;
			}
			
			queueItem.getImageView().setImageBitmap(bitmap);
		}

	}
}
