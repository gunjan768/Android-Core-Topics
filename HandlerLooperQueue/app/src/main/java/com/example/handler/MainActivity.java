package com.example.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.handler.HandlerThread.HandlerThreadActivity;
import com.example.handler.Looper_Handler.HandlerActivity;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    private Button btnStart, btnStop, btnChange, button_handlerThread;

    // Handler is not a java class, it is an android class where as Thread and Runnable are java classes. By default thread starts, executes some work and
    // then finishes. What would happen if this would be case of main thread i.e our app starts, shows the first screen and then finishes. So thread has
    // a so called Message Queue and Looper which loops through the message queue and keeps the thread this way. This message queue contains the package
    // of works to be done so everything we do like switching the switch, clicking the button .. etc gets put into this message queue and then executed
    // sequentially.

    // One of the responsibility of the Handler is to put the work in the message queue and by default it is associated with the thread we instantiated
    // ( made an instance ) it. Like when we instantiate the Handler below we are doing in the main thread so this handler will only work with the message
    // queue of the main thread i.e we can use this handler to get our work to the main thread.

    // By default thread starts, executes some work and when work is done thread terminates. We can't use the same thread again instead we have to create
    // a new instance. But for our main UI thread , it doesn't even if it doesn't have any work to do. Instead it seems to wait for new input ( like
    // button click etc ). Mechanism that keeps this thread alive is the message queue and Looper which loops through this message queue. Looper loops
    // through an infinite for loop written in a normal java code. Unless we quited on purpose we don't leave this loop. Handler is used to get the
    // packages of work into the message queue and in background, handler is used to get the work from background thread to our UI thread. Handler can
    // put the message anywhere ( position ) or at any time that is can change the execution order of the works in the message queue. Looper will search
    // the work according the time and then dispatches the work to the handler so handler has an another responsibility is to execute the work and looper
    // goes on..on.

    // If there is no message in the queue which has to executed right now, then this thread just blocks and then it waits until the message hits the
    // time barrier.
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.button_start_thread);
        btnStop = findViewById(R.id.button_stop_thread);
        btnChange = findViewById(R.id.button_change_activity);
        button_handlerThread = findViewById(R.id.button_handlerThread);

        btnChange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, HandlerActivity.class));
            }
        });

        button_handlerThread.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, HandlerThreadActivity.class));
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // 1) ................. Using Thread Class
                // ExampleThread exampleThread = new ExampleThread(10);
                // exampleThread.start();

                // 2) .................. Using Runnable Interface.
                // ExampleRunnable exampleRunnable = new ExampleRunnable(10);
                // Thread thread = new Thread(exampleRunnable);
                // thread.start();

                // Runnable can be executed in the main thread. exampleRunnable.run() will execute the codes in the thread exampleRunnable instance is
                // currently in which is main thread.
                // exampleRunnable.run();

                // 3) ................... Using anonymous class.
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                btnStart.setText("Using anonymous class");
                            }
                        });
                    }
                }).start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
    }

    static class ExampleThread extends Thread
    {
        int sec;

        ExampleThread(int sec)
        {
            this.sec = sec;
        }

        @Override
        public void run()
        {
            super.run();

            for(int i=0; i<sec; i++)
            {
                Log.d(TAG, "Start: " + i);

                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    class ExampleRunnable implements Runnable
    {
        int sec;

        ExampleRunnable(int sec)
        {
            this.sec = sec;
        }

        @Override
        public void run()
        {
            for(int i=0; i<sec; i++)
            {
                Log.d(TAG, "Start: " + i);

                if(i == 5)
                {
                    // .............. 1) By creating the Handler in the main thread.
                    // Runnable doesn't start a new thread, it only contains the work to be done. Making changes in the UI must be done in the main thread so
                    // we used mainHandler instance. mainHandler post the work in the UI thread.
                    mainHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnStart.setText("Main Thread");
                        }
                    });


                    // ............. 2) By creating the Handler here and then passing the Looper of the main thread. Handler needs a looper in the message
                    // queue to be work with. So we are acquiring the looper of the main thread by passing it as an argument to the Handler constructor.
                    // Now this handler will be associated with the looper of the UI thread. As handler is instantiated here in the background thread
                    // so there will be no looper. Attaching this handler with the main thread Looper means any work passed here will get transferred
                    // to the message queue of main thread and this is what we want as changes to the UI can only be done in the main thread only.
                    Handler threadHandler = new Handler(Looper.getMainLooper());

                    threadHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnStart.setText("Another Thread");
                        }
                    });


                    // .............. 3) By using the post() method of the View class and Button is a view.
                    btnStart.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnStart.setText("View Class post method");
                        }
                    });


                    // ............. 4) Using runOnUiThread() thread.
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnStart.setText("runOnUiThread");
                        }
                    });
                }

                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}