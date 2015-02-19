package com.frankandoak.synchronization.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class UiUtil {



	
	public static int convertDpToPixels(int dp, Context context){

        if( context != null ) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            int px = (int) (dp * (metrics.densityDpi / 160f));
            return px;
        }

        return 0;
	}

    public static int convertDpToPixels(double dp, Context context){
        if( context != null ) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            int px = (int)(dp* (metrics.densityDpi / 160f));
            return px;
        }

        return 0;
    }

    public static DisplayMetrics getScreenMetrics(Context c)
    {
        if( c != null ) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getMetrics(metrics);

            return metrics;
        }

        return new DisplayMetrics();
    }
}
