package com.example.sensors.bound_service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityViewModel extends ViewModel
{
    private static final String TAG = "ActivityViewModel";

    private MutableLiveData<Boolean> mIsProgressBarUpdating = new MutableLiveData<>();

    // Keeping the reference of the MyBinder inside the ViewModel because it could be easy to monitor the service when it binds to the BoundServiceActivity.
    private MutableLiveData<MyService.MyBinder> mBinder = new MutableLiveData<>();

    // Keeping this in here because it doesn't require a context. ServiceConnection is responsible for facilitating the connection of the service to
    // the client.
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        // ComponentName represents to what service a client binds to. IBinder is the link b/w the service and client
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder)
        {
            Log.d(TAG, "ServiceConnection: connected to service.");

            // We've bound to MyService, cast the IBinder and get MyBinder instance.
            MyService.MyBinder binder = (MyService.MyBinder) iBinder;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            Log.d(TAG, "ServiceConnection: disconnected from service.");
            mBinder.postValue(null);
        }
    };


    public ServiceConnection getServiceConnection()
    {
        return serviceConnection;
    }

    public LiveData<MyService.MyBinder> getBinder()
    {
        return mBinder;
    }

    public LiveData<Boolean> getIsProgressBarUpdating()
    {
        return mIsProgressBarUpdating;
    }

    public void setIsProgressBarUpdating(boolean isUpdating)
    {
        mIsProgressBarUpdating.postValue(isUpdating);
    }
}