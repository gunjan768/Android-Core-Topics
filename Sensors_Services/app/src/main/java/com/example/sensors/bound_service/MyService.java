package com.example.sensors.bound_service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

// A service is an application component that can perform long-running operations in the background with no user interface. No user interface means
// without displaying it to the user ( will be running even if the app minimizes ). Services have their own lifecycle independent of the activity
// or fragment that they were created in and due to this property services keep running even if the app is not visible.

// By default, services are unaffected by activity or fragment lifecycle events, such as onDestroy, onPause and so on. By default services do not
// run on a background thread. By default, services operate on the same Thread that they are instantiated on and in most cases that can be the main
// thread. If you have to run the services on the background thread then you have to do it by yourself.

// A bound service can also be a started service but a started service can not be a bound service. Started service is started up by calling
// "startService()" method or "startForegroundService()" method. On the other hand bound services can be start in two different ways :
// 1) When some other component binds to it so the literal act of a something binding to it will start the service.
// 2) "startService()" method or "startForegroundService()" method and then bin to it.

// What is Bound service ? Lets compare it with server. When you start a server and bind to it with some kind of client, in the same way service acts
// as the server and some other component acts a client. Client could be an activity, a fragment, a smart-watch or some other application. Mainly
// bound services is used when there is a consistent or frequent communication b/w some client and the service

public class MyService extends Service
{
    private static final String TAG = "MyService";

    private IBinder iBinder = new MyBinder();
    private Handler handler;
    private int progress, maxValue;
    private Boolean isPaused;

    @Override
    public void onCreate()
    {
        super.onCreate();

        handler = new Handler();
        progress = 0;
        isPaused = true;
        maxValue = 20000;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return iBinder;
    }

    // Purpose of the Binder class is to provide an access point for retrieving a service instance. The IBinder interface is a base interface used for
    // a remotable object. Get an instance of a service inside whatever you want to bind to it. In this case we are binding to the BoundServiceActivity
    // so BoundServiceActivity is the client and service will be the server. MyBinder class is going to help facilitates that binding or connection
    // b/w activity and service.

    public class MyBinder extends Binder
    {
        MyService getService()
        {
            return MyService.this;
        }
    }

    public void startPretendLongRunningTask()
    {
        final Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                if(progress >= maxValue || isPaused)
                {
                    handler.removeCallbacks(this);
                    pausePretendLongRunningTask();
                }
                else
                {
                    progress += 100;
                    handler.postDelayed(this, 100);
                }
            }
        };

        handler.postDelayed(runnable, 100);
    }

    public void pausePretendLongRunningTask()
    {
        isPaused = true;
    }

    public void unPausePretendLongRunningTask()
    {
        isPaused = false;

        startPretendLongRunningTask();
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public Boolean getPaused() {
        return isPaused;
    }

    public void resetTask() {
        progress = 0;
    }

    // onTaskRemoved() will be called when the application is used from the recently used application list i.e When you swipe out the application.
    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);

        stopSelf();
    }
}
