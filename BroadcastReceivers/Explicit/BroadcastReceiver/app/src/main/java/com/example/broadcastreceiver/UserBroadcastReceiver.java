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
        Toast.makeText(context, "Received broadcast from the different app and toasted here", Toast.LENGTH_SHORT).show();
    }
}