package com.example.handler.HandlerThread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.handler.R;

import static com.example.handler.HandlerThread.HandlerThreadClass.EXAMPLE_TASK;

// HandlerThread is a subclass of a Java Thread class that has a looper in the message queue attached to it which means we keep it running in the background
// and can feed more and more packages of work which will be executed sequentially one after another. The best place to put the Handler is not in the
// activity due to it's lifecycle but more suitable place to put is in the service as service doesn't get destroyed when the configuration of the mobile
// changes. IntentService ( now deprecated but same concept plus some additional concept is applied in the JobIntent class ) uses the HandlerThread under the
// hood. With Service class and HandlerThread class we can create our own variation of IntentService.
public class HandlerThreadActivity extends AppCompatActivity
{
    private static final String TAG = "HandlerThreadActivity";

    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private Handler threadHandler;

    private HandlerThreadClass handlerThreadClass = new HandlerThreadClass(
            "ExampleHandlerThread", Process.THREAD_PRIORITY_DEFAULT
    );

    private ExampleRunnable1 exampleRunnable1 = new ExampleRunnable1();
    private Object token = new Object();

    private void initThread()
    {
        handlerThread.start();

        // Same thing is done as before. We created the Handler in the UI thread but we are attaching it with the HandlerThread class instance's looper.
        threadHandler = new Handler(handlerThread.getLooper());
    }

    private void initHandlerThread()
    {
        handlerThreadClass.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        Button add = findViewById(R.id.button_add);
        final Button remove = findViewById(R.id.button_remove);

        // initThread();
        initHandlerThread();

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                executeTask();
            }
        });

        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                removeMessageFromQueue();
            }
        });
    }

    private void executeTask()
    {
        // **************************************************** Using Handler class starts ********************************************************************


        // Runnable2 will be get executed first as Runnable1 is delayed by 2 sec. Though Runnable1 is delayed by 2 sec but it will start after Runnable2
        // is completed so it might have to wait for more than 2 sec.
        // threadHandler.postDelayed(new ExampleRunnable1(), 2000);
        // threadHandler.post(new ExampleRunnable2());


        // It might happen that Runnable1 will execute first before Runnable2 though Runnable2 has been put in the front the queue because putting
        // in the message queue takes some time and before that Runnable2 has been there put Runnable1 will start first.
        // threadHandler.post(new ExampleRunnable1());
        // threadHandler.postAtFrontOfQueue(new ExampleRunnable2());


        // **************************************************** Using Handler class finishes *************************************************************



        // ********************************************** Using HandlerThreadClass class starts ********************************************************


        // It might happen that Runnable1 will execute first before Runnable2 though Runnable2 has been put in the front the queue because putting
        // in the message queue takes some time and before that Runnable2 has been there put Runnable1 will start first.
        // handlerThreadClass.getHandler().post(new ExampleRunnable1());
        // handlerThreadClass.getHandler().postAtFrontOfQueue(new ExampleRunnable2());


        // ************************************************** Using HandlerThreadClass class finishes ***************************************************



        // ****************************************************** Using Message class Starts ************************************************************


        // ..................................... 1)
        // Using sendEmptyMessage() which do the same work under he hood. But sendEmptyMessage() has not all the properties like arg1,arg2 props etc.
        // handlerThreadClass.getHandler().sendEmptyMessage(1);


        // You can avoid writing below three lines and directly use sendEmptyMessage() method. Again same problem arises, Handler doesn't know how to
        // interpret these fields ( arg1, what etc ) so we need to write our own logic as we did before by creating a separate class which extends
        // Handler class. Here we will follow the same steps but we will use anonymous Handler class ( see HandlerThreadCLass.java ).

        // ..................................... 2)
        // Message message = Message.obtain();
        // message.what = EXAMPLE_TASK;
        // message.arg1 = 23;
        // message.obj = "Love is always false";

        // handlerThreadClass.getHandler().sendMessage(message);


        // ..................................... 3) Another way of passing the message ( similar to what we did in 2 ). Here we will directly pass the
        // instance of HandlerThread ( handlerThreadClass ) as an argument to the Message.obtain() method and then directly using sentToTarget() method.
        // Message message = Message.obtain(handlerThreadClass.getHandler());
        // message.what = EXAMPLE_TASK;
        // message.arg1 = 23;
        // message.obj = "Love is always false";

        // message.sendToTarget();


        // ............................. 4) We can avoid passing instance of HandlerThread ( handlerThreadClass ) as an argument to the Message.obtain()
        // and pass it as an argument to the setTarget() method ( not confuse it with sendToTarget() method ) and then use sendToTarget() method.
        // Message message = Message.obtain();
        // message.what = EXAMPLE_TASK;
        // message.arg1 = 23;
        // message.obj = "Love is always false";

        // message.setTarget(handlerThreadClass.getHandler());
        // message.sendToTarget();


        // ............................. 5) By mixing the Message class and HandlerThreadClass
        Message message = Message.obtain();
        message.what = EXAMPLE_TASK;
        message.arg1 = 23;
        message.obj = "Love is always false";

        message.setTarget(handlerThreadClass.getHandler());
        message.sendToTarget();

        // handlerThreadClass.getHandler().post(new ExampleRunnable1());

        // Token is just used as an identification ( in case when we want to remove any one message posted by exampleRunnable1 instance but not all the
        // message posted by it ).
        handlerThreadClass.getHandler().postAtTime(exampleRunnable1, token, SystemClock.uptimeMillis());

        handlerThreadClass.getHandler().post(exampleRunnable1);

        // ****************************************************** Using Message class finishes ***********************************************************
    }

    private void removeMessageFromQueue()
    {
        // We can use that instance only which was used to put the messages in the message queue. We can't use some other instance to remove the  messages
        // from the message queue which were put by some other instance.

        // Passing a null to the argument will remove all messages.
        // handlerThreadClass.getHandler().removeCallbacksAndMessages(null);

        // Used to remove message by using same Runnable instance which was used to post the message.
        // handlerThreadClass.getHandler().removeCallbacks(exampleRunnable1);

        // Removing the specific message posted by 'exampleRunnable1' Runnable instance.
        handlerThreadClass.getHandler().removeCallbacks(exampleRunnable1, token);
    }

    static class ExampleRunnable1 implements Runnable
    {

        @Override
        public void run() 
        {
            for(int i=0; i<5; i++)
            {
                Log.d(TAG, "run: Runnable 1 " + i);

                SystemClock.sleep(1000);
            }
        }
    }

    static class ExampleRunnable2 implements Runnable
    {

        @Override
        public void run()
        {
            for(int i=0; i<5; i++)
            {
                Log.d(TAG, "run: Runnable 2 " + i);
                SystemClock.sleep(1000);
            }
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // handlerThread.quit();

        handlerThreadClass.quitSafely();
    }
}