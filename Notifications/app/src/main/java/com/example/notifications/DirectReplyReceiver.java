package com.example.notifications;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DirectReplyReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if(remoteInput != null)
        {
            CharSequence replyText = remoteInput.getCharSequence("key_text_reply");
            Message answer = new Message(replyText, null);

            MainActivity.messages.add(answer);

            // We have to notify the changes. Otherwise after clicking on the reply button loader will circulate for infinite time. Hence to update the
            // message we need to notify the message.
            MainActivity.sendChannelSixNotification(context);
        }
    }
}