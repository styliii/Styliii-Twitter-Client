package com.codepath.apps.StyliiisSimpleTweets.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by liouyang on 3/10/15.
 */
public class NetworkUtility {
    private static int REQUEST_CODE = 20;

    public static int getREQUEST_CODE() {
        return REQUEST_CODE;
    }

    // static is for class methods
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
