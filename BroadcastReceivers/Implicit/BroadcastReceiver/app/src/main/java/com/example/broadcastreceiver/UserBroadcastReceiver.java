package com.example.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UserBroadcastReceiver extends BroadcastReceiver
{
    // Open the SenderBroadcast project to send the broadcast and received the message in this project.
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Here we are going to create our own broadcast receiver. We can define our own action ( as built in actions are just string constant, example :
        // Intent.ACTION_BOOT_COMPLETED ). For unique name we choose our name ( lovers :) ).
        if("MALLIKA_GUNJAN.EXAMPLE_ACTION".equals(intent.getAction()))
        {
            String receivedText = intent.getStringExtra("GET_OUR_LOVE");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }
    }
}