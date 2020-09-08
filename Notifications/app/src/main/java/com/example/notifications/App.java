package com.example.notifications;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application
{
    public static final String GROUP_1_ID = "group1";
    public static final String GROUP_2_ID = "group2";

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static final String CHANNEL_4_ID = "channel4";
    public static final String CHANNEL_5_ID = "channel5";

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
            NotificationChannelGroup group1 = new NotificationChannelGroup(GROUP_1_ID, "Group 1");
            NotificationChannelGroup group2 = new NotificationChannelGroup(GROUP_2_ID, "Group 2");

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel_1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("An eternal love story of my Lord Radha and Lord Krishn");
            channel1.setGroup(GROUP_1_ID);

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel_2", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("An eternal love story of my Lord Sita and Lord Ram");
            channel2.setGroup(GROUP_1_ID);

            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID, "Channel_3", NotificationManager.IMPORTANCE_HIGH);
            channel3.setDescription("An eternal love story of my Lord Radha and Lord Krishn");
            channel3.setGroup(GROUP_2_ID);

            // We leave the channel 4 alone i.e not in any group. Hence when you long press on the notification then you will se an option "All categories". When
            // you go to the option, there you will see that which channels are in which group. You will observe that channel4 will be placed automatically to
            // the group name "other".
            NotificationChannel channel4 = new NotificationChannel(CHANNEL_4_ID, "Channel_4", NotificationManager.IMPORTANCE_LOW);
            channel4.setDescription("An eternal love story of my Lord Sita and Lord Ram");

            // This channel is used for custom notification.
            NotificationChannel channel5 = new NotificationChannel(CHANNEL_5_ID, "Channel_5", NotificationManager.IMPORTANCE_HIGH);
            channel5.setDescription("This is the custom notification");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannelGroup(group1);
            notificationManager.createNotificationChannelGroup(group2);

            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
            notificationManager.createNotificationChannel(channel3);
            notificationManager.createNotificationChannel(channel4);
            notificationManager.createNotificationChannel(channel5);
        }
    }
}