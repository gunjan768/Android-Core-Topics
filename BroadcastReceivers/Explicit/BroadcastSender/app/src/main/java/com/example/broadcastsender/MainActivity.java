package com.example.broadcastsender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

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
        // ****************************************** If sender and receiver are in the same app ************************************************************


        // ........................................ 1)

        // Though we mentioned the receiver name in the AndroidManifest.xml file but it's not an implicit as we never mentioned the intent filter plus
        // we are not using the action in the Intent() constructor rather we used class name.
        // Intent intent = new Intent(this, ExplicitBroadcastReceiver.class);


        // ........................................ 2)

        // Or we can use Intent empty constructor and then use setClass() method to pass the class names.
        // Intent intent = new Intent();
        // intent.setClass(this, ExplicitBroadcastReceiver.class);


        // ........................................ 3)

        // Both 1) passing class name directly to the Intent class and 2) using empty Intent constructor and then using setClass() will create a Component name.
        // Hence we can avoid using both ways and directly use ComponentName class and create a new component.
        // Intent intent = new Intent();
        // ComponentName componentName = new ComponentName(this, ExplicitBroadcastReceiver.class);

        // intent.setComponent(componentName);
        // sendBroadcast(intent);


        // ******************************************************************************************************************************************************




        // ****************************************** If Sender and receiver are in the different app ***********************************************************



        // ........................................ 1)

        // Now interesting part is that using this ComponentName
        // class we can specify the broadcast receiver which is not in this app but in another app ( remember in implicit to target the broadcast receiver which is
        // in another app we take help of unique action name but in explicit as we are not using action hence we need to use packageName and broadcastReceiver path
        // of the receiver application ).
        // Intent intent = new Intent();
        // ComponentName componentName = new ComponentName("com.example.broadcastreceiver", "com.example.broadcastreceiver.UserBroadcastReceiver");

        // intent.setComponent(componentName);
        // sendBroadcast(intent);


        // ...................................... 2)

        // Here in the second way we will use the action name ( since using action so we also need to use intent filter ). Mainly in implicit broadcast we use action
        // but in addition we use package name of other app which will receive broadcast. Using setPackage() android will convert this type of broadcast to the
        // explicit broadcast. Now go to the broadcast receiver ( an app used to receive the broadcast ) add a same action name to it's AndroidManifest.xml file.
        // Intent intent = new Intent("MALLIKA_GUNJAN_EXAMPLE_ACTION");

        // We only need to mention the package name ( no broadcast receiver path or name is required ). Now again we have to change the AndroidManifest.xml
        // file and need to mention the intent filters which will take the action name.
        // intent.setPackage("com.example.broadcastreceiver");
        // sendBroadcast(intent);


        // ........................................ 3)

        Intent intent = new Intent("MALLIKA_GUNJAN_EXAMPLE_ACTION");

        PackageManager packageManager = getPackageManager();

        // It checks which apps of the phone have the broadcast receiver that has intent filter ( action name ) that we define above i.e which apps have the same action
        // name we used here to send and then use this information to create a single explicit intent and trigger this broadcast receiver directly but it works like
        // an implicit intent as we only used the action name and not the setPackage() method ( as we used in the above example number 2 ) to convert to explicit.
        List<ResolveInfo> infos = packageManager.queryBroadcastReceivers(intent, 0);

        for(ResolveInfo info : infos)
        {
            // activityInfo.name is the name of the broadcast receiver class ( the app which is used to catch the broadcast ) which contains the same intent filter
            // ( action name ) we used above.
            ComponentName componentName = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);

            intent.setComponent(componentName);
            sendBroadcast(intent);

            // Log.i("vallllllllllllllllllll", "sendBroadCast: "+ info.activityInfo.name + "   " + info.activityInfo.packageName);
        }

         // ******************************************************************************************************************************************************
    }
}