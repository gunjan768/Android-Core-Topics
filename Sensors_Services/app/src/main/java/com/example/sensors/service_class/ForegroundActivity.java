package com.example.sensors.service_class;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sensors.R;
import com.example.sensors.activity_recognition.RecognitionActivity;
import com.example.sensors.bound_service.BoundServiceActivity;
import com.example.sensors.job_intent_service.JobIntentServiceActivity;
import com.example.sensors.job_scheduler.JobSchedulerActivity;
import com.example.sensors.sensors.SensorActivity;

public class ForegroundActivity extends AppCompatActivity
{
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground);

        editText = findViewById(R.id.editText);

        Button sensorBtn = findViewById(R.id.sensor_activity);
        Button jobIntentBtn = findViewById(R.id.job_intent);
        Button jobServiceBtn = findViewById(R.id.job_service);
        Button activityRecognitionBtn = findViewById(R.id.activity_recognition);
        Button boundServiceButton = findViewById(R.id.bound_service_button);


        sensorBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ForegroundActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        });

        jobServiceBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ForegroundActivity.this, JobSchedulerActivity.class);
                startActivity(intent);
            }
        });

        jobIntentBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ForegroundActivity.this, JobIntentServiceActivity.class);
                startActivity(intent);
            }
        });

        activityRecognitionBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ForegroundActivity.this, RecognitionActivity.class);
                startActivity(intent);
            }
        });

        boundServiceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ForegroundActivity.this, BoundServiceActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startService(View view)
    {
        String input = editText.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("input_extra", input);

        // startService() is a inbuilt method and don't confuse it with the our defined startService() method. Used to start a service. It is similar to
        // startActivity(). There we used to navigate to the other activity and here to other service ( ExampleService.jav extends Service ).
        startService(serviceIntent);
    }

    public void stopService(View view)
    {
        Intent serviceIntent = new Intent(this, ExampleService.class);

        // stopService() is a inbuilt method and don't confuse it with the our defined stopService() method. Used to stop a service.
        stopService(serviceIntent);
    }

    @Override
    protected void onDestroy()
    {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);

        super.onDestroy();
    }

    // When app will go the background ( when you minimizes the app ).
    @Override
    protected void onPause()
    {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);

        super.onPause();
    }
}