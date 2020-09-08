package com.example.sensors.job_scheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService
{
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    // returning false means our job is short and will get over when this method gets over. But in most cases our background job is long like connecting to server
    // and by default onStartJob() runs on UI thread so we are responsible for starting our own background thread and doing our work there. For longer background
    // work we return true which tells the system that it should keep the device awake so that our service can finish executing its work. Hence it means that we
    // are responsible to tell the system when should our work finish so that it can release WAKE_CLOCK ( if it doesn't release then the user can see in his
    // device setting that our app is consuming the battery ).
    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        Log.d(TAG, "onStartJob:");

        doBackgroundWork(jobParameters);

        return true;
    }

    // Returning true means we want to reschedule our job later job. If returned false then it means that our job is dropped.
    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        Log.d(TAG, "Job cancelled before completion");

        // If job is cancelled then we are responsible for stopping our background work our self. If don't do this then our app will misbehave. For this purpose
        // we created a boolean variable 'jobCancelled'.
        jobCancelled = true;

        return true;
    }

    private void doBackgroundWork(final JobParameters jobParameters)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for(int i=0; i<10; i++)
                {
                    Log.d(TAG, "run "+ i);

                    if(jobCancelled)
                    {
                        return;
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

                Log.d(TAG, "Job Finished");

                jobFinished(jobParameters, false);
            }
        }).start();
    }
}
