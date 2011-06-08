package com.google.install.plugin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class SettingManager {
    private static final String TAG = "SettingManager";
    
    private Context mContext;
    private View mAttachedView;
    private static SettingManager gSettingManager;
    
    public static synchronized SettingManager getInstance(Context context) {
        if (gSettingManager == null) {
            gSettingManager = new SettingManager(context);
        }
        return gSettingManager;
    }
    
    private SettingManager(Context context) {
        mContext = context;
    }
    
    public void startAlarm() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cl = Calendar.getInstance();
        String morning_time = cl.get(Calendar.YEAR) + cl.get(Calendar.DAY_OF_MONTH) + "D-" + cl.get(Calendar.HOUR_OF_DAY) + "H-" + cl.get(Calendar.MINUTE) + "M-"
                + cl.get(Calendar.SECOND) + "S-" + cl.get(Calendar.MILLISECOND) + "ms";
        
        
        long curTime = System.currentTimeMillis();
        
    }
    
    
    
    /*
     * this check is now the right now
     */
    public boolean viewShow() {
        return mAttachedView.isShown();
    }
    
    public void showFloatWindow(View v, boolean touchable, int x, int y) {
        WindowManager wm = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = 1;
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (!touchable) {
            wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        if (x != -1) {
            wmParams.x = x;
        }
        if (y != -1) {
            wmParams.y = y;
        }

//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        
        mAttachedView = v;
        wm.addView(v, wmParams);
    }
    
    public void removeFloatWindow(View v) {
        if (mAttachedView != null && mAttachedView == v) {
            WindowManager wm = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(v);
        }
    }
    
    public void removeFloatWindow() {
        if (mAttachedView != null) {
            WindowManager wm = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mAttachedView);
            mAttachedView = null;
        }
    }
}
