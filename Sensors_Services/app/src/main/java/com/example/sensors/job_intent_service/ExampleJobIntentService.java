package com.example.sensors.job_intent_service;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

// JobIntentService uses two classes : IntentService and JobService. Since android oreo ( api 26 ) we can't keep normal services running in the background anymore
// because system will kill them after around one minute or throws an exception if we try to start the service while app itself is in the background. This happens
// to save the system resources like battery so that it can run for a long time.

// Helper for processing work that has been enqueued for a job/service. When running on Android O or later, the work will be dispatched as a job via
// JobScheduler.enqueue(). When running on older versions of the platform, it will use Context.startService. You must publish your subclass in your manifest for
// the system to interact with. This should be published as a JobService, as described for that class, since on O and later platforms it will be executed that way.
// Use enqueueWork(Context, Class, int, Intent) to enqueue new work to be dispatched to and handled by your service. It will be executed in onHandleWork(Intent).
// One of Android’s greatest strengths is its ability to use system resources in the background regardless app execution. sometimes it became the behaviour to
// use system resources excessively.

// On android O or later it uses JobScheduler to achieve the similar behaviour as an intent service. It starts the work as soon as possible and it executes the
// all the incoming intents sequentially one after another on the background thread. When it finishes it's work it stops automatically. Most of the stuffs what
// be write related to JobScheduler is handled internally in JobIntentService. There will be no notification hence it maximizes the battery life. It will
// do the work internally without any notification and stops as soon as we closes our app. Onwards android oreo the app running in background can't keep it's
// background service running anymore instead the system will stop the service to save the memory and battery. So if want to do some background work like
// networking operation then in most cases we use JobScheduler. Job scheduler is used mainly to do the work in the background. Instead of scheduling the
// background works solly based on time you can define the certain criteria like if the device if connected to wifi or if its currently charging and then
// system will decide when exactly it will run your job and it will be try to intelligent about it. Usually it will batch different jobs together.

// A wake lock is a mechanism to indicate that your application needs to have the device stay on. Any application using a WakeLock must request the
// android. permission. WAKE_LOCK permission in an <uses-permission> element of the application's manifest.

public class ExampleJobIntentService extends JobIntentService
{
    private static final String TAG = "ExampleJobIntent";

    static void startTheWork(Context context, Intent work)
    {
        // enqueueWork() method will invoke the onHandleWork() work for every intent that reaches to it.
        enqueueWork(context, ExampleJobIntentService.class, 123, work);
    }

    // onHandleWork() method is executed for every intent sequentially.
    @Override
    protected void onHandleWork(@NonNull Intent intent)
    {
        String input = intent.getStringExtra("input_extra");

        for(int i=0; i<10; i++)
        {
            Log.d(TAG, input + " - " + i);

            // If the job is stopped then onHandleWork() will not stop automatically instead we have to stop this method.
            if(isStopped())
            {
                return;
            }

            SystemClock.sleep(1000);
        }
    }

    // onStopCurrentWork() will be triggered when the current has been stopped if it's using job scheduler. Job stops due to several reasons like if system is under
    // strong memory pressure or if job is running too long ( each job has some time around like 10 minutes ). This method returns true of false. If you want to
    // continue the same work later return true else return false ( false means you are cancelling the current intent and also the following intents and they will
    // never be continued again ). Default value of 'super.onStopCurrentWork()' is true.
    @Override
    public boolean onStopCurrentWork()
    {
        Log.d(TAG, "onStopCurrentWork");

        return super.onStopCurrentWork();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
