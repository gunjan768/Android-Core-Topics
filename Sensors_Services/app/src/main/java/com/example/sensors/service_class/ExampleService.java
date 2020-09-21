package com.example.sensors.service_class;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.sensors.utils.App;
import com.example.sensors.R;

// The foreground contains the applications the user is working on, and the background contains the applications that are behind the scenes. Foreground
// refers to the active apps which consume data and are currently running on the mobile. Background refers to the data used when the app is doing some
// activity in the background, which is not active right now.

public class ExampleService extends Service
{
    // Bond services are the services where other components can communicate back and forth by binding to it. And for bond services we need onBind()
    // method. Our is a starter service hence we don't need the onBind() method so we kept it empty.
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    // onStartCommand() will be called every time when we call startService() method defined in ForegroundActivity.java. Remember that it doesn't start
    // the background thread instead it runs in main UI thread. However there is sub class ( IntentService ) of Service which automatically creates a
    // local thread but it is deprecated now. There is similar class called JobIntentService.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String input = intent.getStringExtra("input_extra");

        Intent notificationIntent = new Intent(this, ForegroundActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        // Sends the notification which will run in foreground. You can't kill the notification and will run as long as you don't explicitly stop the
        // service.
        startForeground(1, notification);

        // stopSelf() will stop the service.
        // stopSelf();

        // These constants define what happens when the system kills our service.
        // START_NOT_STICKY means our service will just begun and not start again.
        // START_STICKY means the system will restart our service as soon as possible but intent passed will be null.
        // START_REDELIVER_INTENT means start again and pass the last intent.
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
