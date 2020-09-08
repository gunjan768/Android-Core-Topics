package com.example.broadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // You may write this way also 'intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)' i.e calling equals() method of string on intent.getAction() which
        // might be null but string string constant Intent.ACTION_BOOT_COMPLETED can never be null.
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Toast.makeText(context, "Boot completed", Toast.LENGTH_SHORT).show();
        }

        // Not work for android version >= N.
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
        {
            boolean noConnectivity  = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            
            if(noConnectivity)
            {
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

// NOTE : Implicit means specified in action in AndroidManifest.xml ( inside <receiver></receiver> tag ) and we specify the action to trigger the broadcast.
// Static means we registered our broadcast receiver in the manifest file and in this way we can receive our broadcast even if our app is not running. So if
// system sense these broadcasts then our app get started in purpose to react to them. If lots of apps are registered are in the static way then it may leads
// to the memory leak. Hence android version >= N has some restrictions on these static broadcasts and most of them doesn't work ( i.e deprecated ) like
// ConnectivityManager.CONNECTIVITY_ACTION.

// However these restrictions are not applied for the dynamic broadcast receivers. Dynamic means we don't put them on AndroidManifest file instead we register
// them on the context of our app or activity ( as long as our app or activity is running ). For dynamic we need our app to be running.