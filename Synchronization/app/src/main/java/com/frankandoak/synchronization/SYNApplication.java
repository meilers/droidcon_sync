package com.frankandoak.synchronization;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.frankandoak.synchronization.utils.LruBitmapCache;

public class SYNApplication extends Application {

    public static final String TAG = SYNApplication.class.getSimpleName();

    private static SYNApplication sInstance;

	private static Context sContext;
	private static Handler sHandler;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();

        sInstance = this;
        sHandler = new Handler();
		sContext = getApplicationContext();
    }


    public static synchronized SYNApplication getInstance() {
        return sInstance;
    }

	public static final Context getContext() {
		return sContext;
	}

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

}
