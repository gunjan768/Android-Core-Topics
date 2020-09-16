package com.example.handler.Looper_Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.handler.Looper_Handler.ExampleLooperThread;
import com.example.handler.R;

import static com.example.handler.Looper_Handler.ExampleHandler.TASK_A;
import static com.example.handler.Looper_Handler.ExampleHandler.TASK_B;

public class HandlerActivity extends AppCompatActivity
{
    private static final String TAG = "HandlerActivity";
    private ExampleLooperThread exampleLooperThread ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        Button btnStart = findViewById(R.id.button_start);
        Button btnStop = findViewById(R.id.button_stop);
        Button taskOne = findViewById(R.id.button_task_one);
        Button taskTwo = findViewById(R.id.button_task_two);

        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // If exampleLooperThread is instantiated ( make an instance of ) outside the button click event then clicking the button twice will crash
                // the app. Why ? As we studied earlier that we can use an instance once only. So clicking the button second time will use the same instance
                // that was created earlier to start the thread. So to avoid the crash create an instance of Thread inside the button click so that a new
                // instance is always used to start the Thread.
                exampleLooperThread = new ExampleLooperThread();
                exampleLooperThread.start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Will quit the infinite for loop and we can get out the loop.
                exampleLooperThread.looper.quit();

                // exampleLooperThread.looper is the looper of the background thread we created. We can get the looper directly by using getLooper() method.
                // Looper looper = exampleLooperThread.handler.getLooper();
            }
        });

        taskOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                executeTaskA();
            }
        });

        taskTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Message message = Message.obtain();

                message.what = TASK_B;
                exampleLooperThread.handler.sendMessage(message);
            }
        });
    }

    private void executeTaskA()
    {
        // Though this handler is created  here ( i.e in the main thread ) but we are explicitly binding it with the looper of the background
        // thread. Remember that we can't create this handler before the start of this thread ( the thread starts when we click on start button ).

//        Handler threadHandler = new Handler(exampleLooperThread.looper);


        // 1) ............ using thread created here but bounded to the background thread. This anonymous class introduces a potential memory
        // leak. An anonymous class is basically same as non static inner class and these non static inner classes have a reference to the
        // outer class so Runnable here has a reference to our activity that's why we can access the outer class ( here activity ) variables
        // and methods inside the anonymous class ( inside Runnable ). This is called an implicit reference. And as long as we have reference
        // to this activity, this activity can't be garbage collected. Activity can be only be cleaned if there is not reference hence led
        // to the memory leak. So to fix this we need to make a static inner class.

        // Since we are putting all the works in the Runnable then why not to call it Runnable Queue instead of Message Queue ? Ans is that
        // instead of posting to the Runnable using post() method we can directly send using Message class.

        // Here we are sending something from UI thread to the message queue of our new Looper background thread. Runnable is used to as a
        // package of jobs and every job will be transferred to the background thread's message queue. Say if you pressed the button more than
        // once say twice before the completion of the first job then second will be added to the queue and will only get executed when the
        // 1st job will get finished.
//        threadHandler.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                for(int i=0; i<5; i++)
//                {
//                    Log.d(TAG, "Start: " + i);
//
//                    SystemClock.sleep(1000);
//                }
//            }
//        });




        // 2) ............ Using handler. Here we are sending something from UI thread to the message queue of our new Looper background thread.
        // We are using the handler created in the background thread only ( see ExampleLooperThread.java class ). There while we were creating
        // the handler we didn't attached the handler with the looper of the background thread as handler was created in the background thread
        // only ( see 1st way where we created handler in the UI thread and attached it to the Looper of the background thread ).
//        exampleLooperThread.handler.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                for(int i=0; i<5; i++)
//                {
//                    Log.d(TAG, "Start: " + i);
//
//                    SystemClock.sleep(1000);
//                }
//            }
//        });



        // 3) ............ Using Message class. Instead of creating an instance with new keyword, use Message.obtain() which will give you
        // the recycled instance ( i.e messages recycle under the hood ).
        Message message = Message.obtain();

        // message.arg1, message.arg2 ... etc that is we have access to the different variables. Most of them are arbitrary values i.e what
        // we put into them and how we later used them is up to us. message.what is commonly is used to indicate what type of action we want
        // to execute. Handler doesn't know what to do with these fields ( variables ). Handler know how to execute so we need to tell the
        // the handler about these variables so we create a Handler Java class.
        message.what = TASK_A;
        exampleLooperThread.handler.sendMessage(message);
    }

    // Now this static inner class has no reference to the activity. But it also means that we can't access the activity's variables and methods directly.
    // To access them, we need weak reference.
    static class MyRunnable implements Runnable
    {
        @Override
        public void run()
        {

        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        exampleLooperThread.looper.quit();
    }
}