package com.google.install.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service_intent = new Intent(context, InstallPluginService.class);
        context.startService(service_intent);
    }

}
