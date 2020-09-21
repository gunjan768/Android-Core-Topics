package com.example.sensors.activity_recognition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityRecognitionService extends JobIntentService
{
    private static final String TAG = "ActivityRecognitionService";
    private Bundle bundle;

    static void startTheWork(Context context, Intent intent)
    {
        // enqueueWork() method will invoke the onHandleWork() work for every intent that reaches to it.
        enqueueWork(context, ActivityRecognitionService.class, 123, intent);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent)
    {
        // Log.d(TAG, "onHandleWork: " + intent);

        if(ActivityRecognitionResult.hasResult(intent))
        {
            bundle = new Bundle();

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            handleDetectedActivity(result.getProbableActivities());
        }
    }

    private void handleDetectedActivity(List<DetectedActivity> probableActivities)
    {
        for(DetectedActivity detectedActivity: probableActivities)
        {
            switch(detectedActivity.getType())
            {
                case DetectedActivity.IN_VEHICLE:
                    Log.d(TAG, "handleDetectedActivity: In Vehicle : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "In Vehicle : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.ON_BICYCLE:
                    Log.d(TAG, "handleDetectedActivity: On Bicycle : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "On Bicycle : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.ON_FOOT:
                    Log.d(TAG, "handleDetectedActivity: On Foot : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "On Foot : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.RUNNING:
                    Log.d(TAG, "handleDetectedActivity: Running : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "Running : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.STILL:
                    Log.d(TAG, "handleDetectedActivity: Still : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "Still : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.WALKING:
                    Log.d(TAG, "handleDetectedActivity: Walking : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "Walking : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.TILTING:
                    Log.d(TAG, "handleDetectedActivity: Tilting : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "Tilting : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;

                case DetectedActivity.UNKNOWN:
                    Log.d(TAG, "handleDetectedActivity: Unknown : " + detectedActivity.getConfidence());

                    bundle.putString("user_activity_message", "Unknown : " + detectedActivity.getConfidence());
                    RecognitionActivity.myReceiver.send(RecognitionActivity.REQUEST_CODE, bundle);

                    break;
            }
        }
    }

}