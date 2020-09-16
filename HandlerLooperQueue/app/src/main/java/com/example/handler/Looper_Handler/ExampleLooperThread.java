package com.example.handler.Looper_Handler;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.handler.Looper_Handler.ExampleHandler;

public class ExampleLooperThread extends Thread
{
    private static final String TAG = "ExampleLooperThread";
    public Handler handler;
    public Looper looper;

    @Override
    public void run()
    {
        super.run();

        // This will add looper to the background thread and it automatically creates the message queue. It should be added else your app will
        // get crashed. It is must.
        Looper.prepare();

        // myLooper() : returns the looper of the current thread. looper variable is used to quit the infinite loop when we click the stop
        // button and it is used in the 1st way of executing of the taskA where we created a handler in the main thread but attached that
        // handler with the this looper that is looper of the background thread. So every task inside the Runnable will be transferred
        // from the UI thread to here i.e to the background thread.
        looper = Looper.myLooper();

        // As we have instantiated the handler in the run() method i.e in background thread hence this handler is associated with the background
        // thread. It is used in the 2nd way of executing the taskA.
        // handler = new Handler();

        // When we will use Message class ( 3rd way of executing the taskA ).
         handler = new ExampleHandler();

        // loop() method will starts the infinite for loop. So whatever written after this will not executed unless we quit the infinite for loop.
        Looper.loop();

        Log.d(TAG, "run: End of the run");
    }
}