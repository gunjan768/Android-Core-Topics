package com.example.sensors.job_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sensors.R;

public class JobSchedulerActivity extends AppCompatActivity
{
    private static final String TAG = "JobSchedulerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_scheduler);
    }

    public void scheduleJob(View view)
    {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);

        // NETWORK_TYPE_UNMETERED is the constant which means if we have WIFI connection.
        // setPersisted() if set to true means system will keep our job alive even if we boot our device.
        // setPeriodic() tells how often you want to execute this job. Minimum time passed should be greater than equal to 15 minutes or else internally it will be
        // converted to 15 minutes if passed value was less than that. However this period is not exact but system will try to do it within these periods.
        JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        int resultCode = jobScheduler.schedule(jobInfo);

        if(resultCode == JobScheduler.RESULT_SUCCESS)
        {
            Log.d(TAG, "Job scheduled successfully ");
        }
        else
        {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View view)
    {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(123);

        Log.d(TAG, "Job cancelled");
    }
}