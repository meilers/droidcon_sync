package com.frankandoak.synchronization;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class SYNApplication extends Application {

    public static final String TAG = SYNApplication.class.getSimpleName();

    private static SYNApplication sInstance;

	private static Context sContext;
	private static Handler sHandler;

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


}
