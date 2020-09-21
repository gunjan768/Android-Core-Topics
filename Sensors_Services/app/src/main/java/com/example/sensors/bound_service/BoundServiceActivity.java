package com.example.sensors.bound_service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sensors.R;

// A service is an application component that can perform long-running operations in the background with no user interface. No user interface means
// without displaying it to the user ( will be running even if the app minimizes ). Services have their own lifecycle independent of the activity
// or fragment that they were created in and due to this property services keep running even if the app is not visible.

// By default, services are unaffected by activity or fragment lifecycle events, such as onDestroy, onPause and so on. By default services do not
// run on a background thread. By default, services operate on the same Thread that they are instantiated on and in most cases it is the the main
// thread. If you have to run the services on the background thread then you have to do it by yourself.

// A bound service can also be a started service but a started service can not be a bound service. Started service is started up by calling
// "startService()" method or "startForegroundService()" method. On the other hand bound services can be start in two different ways :
// 1) When some other component binds to it so the literal act of a something binding to it will start the service.
// 2) "startService()" method or "startForegroundService()" method and then bin to it.

// What is Bound service ? Lets compare it with server. When you start a server and bind to it with some kind of client, in the same way service acts
// as the server and some other component acts a client. Client could be an activity, a fragment, a smart-watch or some other application. Mainly
// bound services is used when there is a consistent or frequent communication b/w some client and the service

public class BoundServiceActivity extends AppCompatActivity
{
    private static final String TAG = "BoundServiceActivity";

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private Button mButton;

    private MyService mService;
    private ActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_service);

        mProgressBar = findViewById(R.id.progress_bar);
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.toggle_updates);

        mViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        setObservers();

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggleUpdates();
            }
        });
    }

    private void toggleUpdates()
    {
        if(mService != null)
        {
            if(mService.getProgress() >= mService.getMaxValue())
            {
                mService.resetTask();
                mButton.setText("Start");
            }
            else
            {
                if(mService.getPaused())
                {
                    mService.unPausePretendLongRunningTask();

                    mViewModel.setIsProgressBarUpdating(true);
                }
                else
                {
                    mService.pausePretendLongRunningTask();

                    mViewModel.setIsProgressBarUpdating(false);
                }
            }
        }
    }

    private void setObservers()
    {
        mViewModel.getBinder().observe(this, new Observer<MyService.MyBinder>()
        {
            @Override
            public void onChanged(@Nullable MyService.MyBinder myBinder)
            {
                if(myBinder == null)
                {
                    Log.d(TAG, "onChanged: unbounded from service");
                }
                else
                {
                    Log.d(TAG, "onChanged: bound to service.");

                    mService = myBinder.getService();
                }
            }
        });

        mViewModel.getIsProgressBarUpdating().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean)
            {
                final Handler handler = new Handler();

                final Runnable runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(mViewModel.getIsProgressBarUpdating().getValue())
                        {
                            // meaning the service is bound.
                            if(mViewModel.getBinder().getValue() != null)
                            {
                                if(mService.getProgress() >= mService.getMaxValue())
                                {
                                    mViewModel.setIsProgressBarUpdating(false);
                                }

                                mProgressBar.setProgress(mService.getProgress());
                                mProgressBar.setMax(mService.getMaxValue());

                                String progress = 100 * mService.getProgress() / mService.getMaxValue() + "%";

                                mTextView.setText(progress);
                            }

                            handler.postDelayed(this, 100);
                        }
                        else
                        {
                            handler.removeCallbacks(this);
                        }
                    }
                };

                // Control what the button shows.
                if(aBoolean)
                {
                    mButton.setText("Pause");
                    handler.postDelayed(runnable, 100);
                }
                else
                {
                    if(mService.getProgress() >= mService.getMaxValue())
                    {
                        mButton.setText("Restart");
                    }
                    else
                    {
                        mButton.setText("Start");
                    }
                }
            }
        });
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        // We can start the service using startService() or startForegroundService(). This is the 1st way. 2nd way to start the service is using
        // bindService(). It means if instead of calling of startService() we can directly call bindService() and start a service. But calling
        // bindService() directly has some limitation to it like service will only stay alive as long as something ( client ) is bound to it,
        // so we use 1st way and after that we bind our activity to the service.
        startService();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        if(mViewModel.getBinder() != null)
        {
            unbindService(mViewModel.getServiceConnection());
        }
    }

    private void startService()
    {
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

        bindService();
    }

    private void bindService()
    {
        Intent serviceBindIntent =  new Intent(this, MyService.class);

        // bindService() method will call the ServiceConnection() method ( see ActivityViewModel.java class ).
        bindService(serviceBindIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }
}