package com.example.pakagemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NewPackageBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = "NewPackageBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        // Log.d(TAG, "onReceive: " + action);

        assert action != null;
        switch(action)
        {
            case Intent.ACTION_PACKAGE_ADDED:
                Log.d(TAG, "onReceive: App installed");

                break;

            case Intent.ACTION_PACKAGE_CHANGED:
                Log.d(TAG, "onReceive: Package changed");

                break;

            case Intent.ACTION_PACKAGE_DATA_CLEARED:
                Log.d(TAG, "onReceive: Package data cleared");

                break;

            case Intent.ACTION_PACKAGE_REMOVED:
                Log.d(TAG, "onReceive: App uninstalled");

                break;

            case Intent.ACTION_PACKAGE_REPLACED:
                Log.d(TAG, "onReceive: App replaced");

                break;

            case Intent.ACTION_PACKAGE_RESTARTED:
                Log.d(TAG, "onReceive: App restarted");

                break;

            case Intent.ACTION_PACKAGE_FULLY_REMOVED:
                Log.d(TAG, "onReceive: App uninstalled successfully");

                break;

            default:
                Log.d(TAG, "onReceive: Unknown");

                break;
        }
    }
}