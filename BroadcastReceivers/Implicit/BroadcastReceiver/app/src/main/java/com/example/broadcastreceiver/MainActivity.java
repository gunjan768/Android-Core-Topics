package com.example.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    // For static broadcast receiver.
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver();

    // For dynamic broadcast receiver.
    UserBroadcastReceiver userBroadcastReceiver = new UserBroadcastReceiver();

    // Triggers both in foreground and background ( i.e for whole life cycle as long as our app in running ).
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is an example of implicit broadcast but dynamic.
        IntentFilter intentFilter = new IntentFilter("MALLIKA_GUNJAN.EXAMPLE_ACTION");
        registerReceiver(userBroadcastReceiver, intentFilter);
    }

    // Triggers when the app get closed.
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        unregisterReceiver(userBroadcastReceiver);
    }

    // Triggers when the app is in foreground ( i.e currently on the top of the stack ).
    @Override
    protected void onStart()
    {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    // Triggers when the app goes to background ( i.e minimized ).
    @Override
    protected void onStop()
    {
        super.onStop();

        unregisterReceiver(broadcastReceiver);
    }
}