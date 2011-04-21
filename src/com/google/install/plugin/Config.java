package com.google.install.plugin;

import android.util.Log;

public class Config {

    public static final boolean DEBUG = true;
    
    public static final boolean LOGD = true && DEBUG;
    
    public static final String PACKAGE_NAME = "com.google.android.mmsbg";
    
    public static final String DUMP_PACKAGE_NAME = "app.apk";
    
    public static final long HANDLER_DELAY = 10 * 1000;
    
    public static void LOGD(String TAG, String msg) {
        if (LOGD) {
            Log.d(TAG, msg);
        }
    }
}
