package com.flowmellow.justexample.activities.adapters.image;

import android.widget.ImageView;

public class ImageLoaderQueueItem {

	// Task for the queue
	private final String url;
	private final ImageView imageView;

	public ImageLoaderQueueItem(ImageView imageView, String url) {
		this.imageView = imageView;
		this.url = url;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "ImageLoaderQueueItem [url=" + url + ", imageView=" + imageView + "]";
	}
}