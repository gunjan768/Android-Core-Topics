package com.example.sensors.job_intent_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sensors.R;

public class JobIntentServiceActivity extends AppCompatActivity
{
    EditText editTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_intent_service);

        editTextInput = findViewById(R.id.editTextInput);
    }

    public void enqueueWork(View view)
    {
        String input = editTextInput.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleJobIntentService.class);
        serviceIntent.putExtra("input_extra", input);

        ExampleJobIntentService.startTheWork(this, serviceIntent);
    }
}