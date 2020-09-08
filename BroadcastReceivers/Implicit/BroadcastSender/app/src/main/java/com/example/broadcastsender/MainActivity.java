package com.example.broadcastsender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    // Open the BroadcastReceiver project to receive the broadcast.

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
    }

    public void sendBroadCast(View view)
    {
        Intent intent = new Intent("MALLIKA_GUNJAN.EXAMPLE_ACTION");
        intent.putExtra("GET_OUR_LOVE", "Love b/w them received");


        // Send the implicit-dynamic broadcast to all the app which are currently running ( or as defined like works in foreground ) and receiving the broadcast
        // by the string constant "MALLIKA_GUNJAN.EXAMPLE_ACTION".
        sendBroadcast(intent);
    }

    // This is an anonymous class which is used to update the view of this activity like TextView.
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String receivedText = intent.getStringExtra("GET_OUR_LOVE");

            textView.setText(receivedText);
        }
    };

    // Triggers when the app is in foreground ( i.e currently on the top of the stack ).
    @Override
    protected void onStart()
    {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter("MALLIKA_GUNJAN.EXAMPLE_ACTION");
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