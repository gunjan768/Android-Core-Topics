package com.example.handler.Looper_Handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class ExampleHandler extends Handler
{
    private static final String TAG = "ExampleHandler";
    public static final int TASK_A = 1;
    public static final int TASK_B = 2;

    public ExampleHandler(@NonNull Looper looper)
    {
        super(looper);
    }

    public ExampleHandler()
    {

    }

    // handleMessage() : this is where our message will eventually arrive to be executed.
    @Override
    public void handleMessage(@NonNull Message msg)
    {
        super.handleMessage(msg);

        switch(msg.what)
        {
            case TASK_A:
                Log.d(TAG, "handleMessage: Task A is executed");

                break;

            case TASK_B:
                Log.d(TAG, "handleMessage: Task B is executed");

                break;
        }
    }
}