package com.example.sensors.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application
{
    public static final String CHANNEL_ID = "channel";
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate()
    {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels()
    {
        // Checking if the android version above or equal to the Oreo ie version 8.0.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is the foreground notification");

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is the intent service notification");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel2", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("This is the job intent notification");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }
}