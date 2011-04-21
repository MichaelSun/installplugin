package com.google.install.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.LOGD(TAG, "[[onCreate]]");
        
        this.setContentView(R.layout.test);
        
        View install = findViewById(R.id.install);
        if (install != null) {
            install.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TestActivity.this, InstallPluginService.class);
                    intent.setAction(InstallPluginService.ACTION_INSTALL_NOW);
                    Config.LOGD(TAG, "start service .....");
                    startService(intent);
                }
            });
        }
        
        View install1 = findViewById(R.id.install1);
        if (install1 != null) {
            install1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TestActivity.this, InstallPluginService.class);
                    Config.LOGD(TAG, "start service for register ......");
                    startService(intent);
                }
            });
        }
    }
}
