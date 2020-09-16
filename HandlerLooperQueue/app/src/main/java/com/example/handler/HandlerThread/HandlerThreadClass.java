package com.example.handler.HandlerThread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

// HandlerThread is a subclass of a Java Thread class that has a looper in the message queue attached to it which means we keep it running in the background
// and can feed more and more packages of work which will be executed sequentially one after another. The best place to put the Handler is not in the
// activity due to it's lifecycle but more suitable place to put is in the service as service doesn't get destroyed when the configuration of the mobile
// changes. IntentService ( now deprecated but same concept plus some additional concept is applied in the JobIntent class ) uses the HandlerThread under the
// hood. With Service class and HandlerThread class we can create our own variation of IntentService.
public class HandlerThreadClass extends HandlerThread
{
    private static final String TAG = "HandlerThreadClass";
    private Handler handler;
    public static final int EXAMPLE_TASK = 1;

    public HandlerThreadClass(String name, int priority)
    {
        // priority is optional and it defines how much processing time a thread should get as multiple threads might be there and they all get executed
        // by the same CPU so it's more often to tell CPU that which thread gets how much time. We have removed the default super() and added our own.
        // super(name, priority);

        // Our own hard coded super(). THREAD_PRIORITY_DEFAULT value is zero. Remember that the higher the value lower the priority so higher priority
        // value is less than the default one i.e have negative values. THREAD_PRIORITY_BACKGROUND value is 10 which means of very less priority.
        super("ExampleHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
    }

    // This is the method which is used to do some stuffs before Looper loops. If you see the implementation of the HandlerThread class you will
    // notice the codes in the same order : Looper.prepare() --->  onLooperPrepared() callback ---> Looper.loop().
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared()
    {
        super.onLooperPrepared();

        // We don't need to pass the looper as we are instantiating it in the background thread.
        // handler = new Handler();

        // If we are using the Message class to send the message. Here same problem of memory leak like before when we were using anonymous Runnable
        // class. But here no memory leak will happen. Why ? As before Runnable is used inside an activity and and Runnable has a reference to the
        // activity as anonymous class is not static. If any configuration changes ( like when we rotate our mobile ) happen then activity will be
        // recreated but Runnable will remain alive as long as the message in the message queue. But here we know that HandlerThreadClass will
        // remain until all the messages in the queue will not get executed so no memory leak will happen so we can surpass this warning.
        handler = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg)
            {
                super.handleMessage(msg);

                switch(msg.what)
                {
                    case EXAMPLE_TASK:

                        Log.d(TAG, "handleMessage: arg1 " + msg.arg1);
                        Log.d(TAG, "handleMessage: obj " + msg.obj);

                        for(int i=0; i<5; i++)
                        {
                            Log.d(TAG, "HandlerThreadClass " + i);

                            SystemClock.sleep(1000);
                        }

                        break;
                }
            }
        };
    }

    public Handler getHandler()
    {
        return handler;
    }
}