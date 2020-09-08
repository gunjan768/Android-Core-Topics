package com.example.sensors.activity_recognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastReceiverJobIntentService extends BroadcastReceiver
{
    private static final String TAG = "BroadcastReceiverJobIntentService";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ActivityRecognitionService.startTheWork(context, intent);
    }
}
