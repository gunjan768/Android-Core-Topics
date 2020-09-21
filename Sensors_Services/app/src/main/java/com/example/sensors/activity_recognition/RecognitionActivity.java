package com.example.sensors.activity_recognition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sensors.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class RecognitionActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "RecognitionActivity";

    public GoogleApiClient googleApiClient;
    private Context context = RecognitionActivity.this;
    private EditText personActivity;
    public static final int REQUEST_CODE = 2;
    public static MyReceiver myReceiver;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        personActivity = findViewById(R.id.person_activity_edit_text);

        googleApiClient = new GoogleApiClient.Builder(RecognitionActivity.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(RecognitionActivity.this)
                .addOnConnectionFailedListener(RecognitionActivity.this)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Intent intent = new Intent(context, BroadcastReceiverJobIntentService.class);
        myReceiver = new MyReceiver(handler);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(this);
        Task<Void> task =  activityRecognitionClient.requestActivityUpdates(3000, pendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                Toast.makeText(getApplicationContext(), "Successfully requested activity updates", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getApplicationContext(), "Requesting activity updates failed to start", Toast.LENGTH_SHORT).show();
            }
        });

        // Either use the above commented one or below one.
        // ActivityRecognitionClient activityRecognitionClient = new ActivityRecognitionClient(context);
        // activityRecognitionClient.requestActivityUpdates(3000, pendingIntent);

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    // Generic interface for receiving a callback result from someone. Use this by creating a subclass and extends onReceiveResult(int, Bundle)
    public class MyReceiver extends ResultReceiver
    {
        // Create a new ResultReceive to receive results. Your onReceiveResult(int, Bundle) method will be called from the thread running handler
        // if given, or from an arbitrary thread if null.
        public MyReceiver(Handler handler)
        {
            super(handler);
        }

        // Deliver a result to this receiver. Will call onReceiveResult(int, Bundle), always asynchronously if the receiver has supplied a Handler
        // in which to dispatch the result.
        @Override
        public void send(int resultCode, Bundle resultData)
        {
            super.send(resultCode, resultData);
        }

        @Override
        public int describeContents()
        {
            return super.describeContents();
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
        }

        // Override to receive results delivered to this object.
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData)
        {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode == REQUEST_CODE && resultData != null)
            {
                final String message = resultData.getString("user_activity_message");

                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        personActivity.setText(message);

                        assert message != null;
                        personActivity.setSelection(message.length());
                    }
                });
            }
        }
    }
}