package com.example.pakagemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity implements RecyclerViewAdapter.UninstallApplicationListener
{
    public static final int REQUEST_CODE = 100;

    @Override
    public void uninstallApp(ApplicationInfo data, int position)
    {

        // ********************************************************** OLD WAY STARTS *********************************************************************


          Intent intent = new Intent(Intent.ACTION_DELETE);

          intent.setData(Uri.parse("package:" + data.packageName));
          intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
          intent.putExtra("position", position);

          startActivityForResult(intent, REQUEST_CODE);


        // ********************************************************** OLD WAY ENDS *****************************************************************



        // The above intent ACTION_UNINSTALL_PACKAGE is only available after API 14 (Android 4.0), if your app targets Android P and above, you have to add
        // the permission REQUEST_DELETE_PACKAGE to your AndroidManifest. Also, this intent is deprecated since Android Q, in which situation you should use
        // PackageInstaller.uninstall() like this:



        // ************************************************************* NEW WAY STARTS *******************************************************************

        // Not working : Reason still known but will find soon.

//        String packageName = "package:" + data.packageName;
//
//        Intent intent = new Intent();
//        intent.putExtra("position", position);
//
//        PendingIntent sender = PendingIntent.getActivity(AppListActivity.this, 0, intent, 0);
//
//        PackageInstaller packageInstaller = packageManager.getPackageInstaller();
//
//        // getIntentSender() retrieve a IntentSender object that wraps the existing sender of the PendingIntent. Instances of IntentSender class can not be made
//        // directly, but rather must be created from an existing PendingIntent with PendingIntent.getIntentSender(). See at the last, an interface has been
//        // implemented using nested ( or inner ) class.
//        packageInstaller.uninstall(packageName, sender.getIntentSender());


        // ************************************************************** NEW WAY ENDS **************************************************************
    }

    private static final String TAG = "AppListActivity";
    
    private PackageManager packageManager;
    RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<ApplicationInfo> applicationInfoList;
    private Context context;
    NewPackageBroadcastReceiver newPackageBroadcastReceiver;

    private void setRecyclerView()
    {
        recyclerView = findViewById(R.id.app_list_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        applicationInfoList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(context, applicationInfoList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setIntentFiltersForPackage()
    {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);

        intentFilter.addDataScheme("package");

        newPackageBroadcastReceiver = new NewPackageBroadcastReceiver();
        registerReceiver(newPackageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(newPackageBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        packageManager = getPackageManager();
        context = AppListActivity.this;

        setRecyclerView();
        setIntentFiltersForPackage();
        getAllInstalledApps();
    }

    private void getAllInstalledApps()
    {
        packageManager = getPackageManager();

        List<PackageInfo> packageLists = getPackageManager().getInstalledPackages(0);

        for(PackageInfo packageInfo : packageLists)
        {
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                //String appDescription = packageInfo.applicationInfo.loadDescription(getPackageManager()).toString();

                // Log.e("App Name : " , appName + " " + appDescription);
            }
        }

        checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
    }

    private void checkForLaunchIntent(List<ApplicationInfo> list)
    {
        for(ApplicationInfo info: list)
        {
            try
            {
                if(packageManager.getLaunchIntentForPackage(info.packageName) != null)
                {
                    applicationInfoList.add(info);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.exit:

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);

                Button button = new Button(context);
                button.setText("This is the exit button");
                button.setHeight(40);
                button.setWidth(20);
                button.setBackgroundColor(Color.RED);

                builder.setView(button);

                // If we don't set title, icon is not displayed.
                builder.setTitle("Exit Anyway...");
                builder.setIcon(R.drawable.ic_exit);

                // Use setTitle instead of setMessage which sets message body and overrides the items list.
                // builder.setMessage("Do you want to exit ?");

                // setCancelable() accepts Boolean argument and decides whether dialog is cancelable or not. Default is true.
                builder.setCancelable(false);

                final String[] fileNames = {"Google", "Facebook", "Tutorials Point"};

                final String[] WEBSITES = {"https://www.google.com", "https://www.facebook.com",
                                            "https://www.tutorialspoint.com/android/android_webview_layout.html"
                };

                // If you have set the message then message will override it so use one of them as only one will be visible if both are used
                // ( only message will be seen ).
                builder.setItems(fileNames, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position)
                    {
                        Intent webViewIntent = new Intent(context, WebViewActivity.class);
                        webViewIntent.putExtra("URL", WEBSITES[position]);
                        startActivity(webViewIntent);
                    }
                });

                builder.setNeutralButton("Do Nothing", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        finish();

                        // System.exit(0);
                    }
                });

                builder.setPositiveButton("NO", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });

                // Finally to show alert dialog, we call AlertDialog.show() as follows.
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;

            default: return false;
        }
    }

    // ***************************************************** If using old way of uninstalling an application ************************************************

    // In old version we used a REQUEST_CODE to invoke this onActivityResult() method. Remember that onActivityResult() is not deprecated but the way of
    // uninstalling an app got deprecated. Rather it's an useful concept of communication between the activities ( may be between the same activity ).
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE )
        {
            if(resultCode == RESULT_OK)
            {
                Log.d("TAG", "onActivityResult: user accepted the (un)install");

                // Update the recycler view for the data set changed ( uninstall apps needed to be removed from the UI ).
                // recyclerViewAdapter.notifyDataSetChanged();

                // Or second way of updating the UI is to recreate the activity by calling recreate() method.
                recreate();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Log.d("TAG", "onActivityResult: user canceled the (un)install");
            }
            else if(resultCode == RESULT_FIRST_USER)
            {
                Log.d("TAG", "onActivityResult: failed to (un)install");
            }
        }
    }


    // *****************************************************************************************************************************************************



    // *********************************************** If using new way of uninstalling an application ****************************************************


    // IntentSender.OnFinished : Callback interface for discovering when a send operation has completed. The object to call back on when the send has completed,
    // or null for no callback.

    // IntentSender.SendIntentException : Exception thrown when trying to send through a PendingIntent that has been canceled or is otherwise no longer able to
    // execute the request.

    public class MyIntentSender extends IntentSender.SendIntentException implements IntentSender.OnFinished
    {
        public MyIntentSender() {
            super();
        }

        public MyIntentSender(String name) {
            super(name);
        }

        public MyIntentSender(Exception cause) {
            super(cause);
        }

        // onSendFinished() method is a Callback for discovering when a send operation has completed.
        @Override
        public void onSendFinished(IntentSender intentSender, Intent intent, int i, String s, Bundle bundle)
        {
            Log.d(TAG, "onSendFinished: We finished uninstalling the application successfully");
            Log.d(TAG, "onSendFinished: " + intent.getIntExtra("position", 0));
            Log.d(TAG, "onSendFinished: " + i);
            Log.d(TAG, "onSendFinished: " + intentSender.getCreatorPackage());
            Log.d(TAG, "onSendFinished: " + intentSender.getCreatorUid());

            // Update the recycler view for the data set changed ( uninstall apps needed to be removed from the UI ).
            // recyclerViewAdapter.notifyDataSetChanged();

            // Or second way of updating the UI is to recreate the activity by calling recreate() method.
            recreate();
        }
    }

    // *****************************************************************************************************************************************************
}