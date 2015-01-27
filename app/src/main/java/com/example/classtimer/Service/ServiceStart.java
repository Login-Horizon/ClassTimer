package com.example.classtimer.Service;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Руслан on 27.01.2015.
 */
public class ServiceStart extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, NotifService.class));
    }
}
