package com.example.sensors.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sensors.R;


public class SensorActivity extends AppCompatActivity implements SensorEventListener
{
    // logt ---> shortcut for the log string.
    private static final String TAG = "MainActivity";
    Sensor accelerometer, gyroscope, magnetometer, light, pressure, temperature, humidity;

    private TextView acc_xValue, acc_yValue, acc_zValue;
    private TextView gyroscope_xValue, gyroscope_yValue, gyroscope_zValue;
    private TextView magnet_xValue, magnet_yValue, magnet_zValue;
    private TextView light_xValue, press_xValue, temp_xValue, humidity_xValue;

    private void initAccelerometer()
    {
        // Smart phones and other mobile technology identify their orientation through the use of an accelerator, a small device made up of axis-based motion
        // sensing. The motion sensors in accelerometers can even be used to detect earthquakes, and may by used in medical devices such as bionic limbs and
        // other artificial body parts. An accelerometer is an electronic sensor that measures the acceleration forces acting on an object, in order to
        // determine the object's position in space and monitor the object's movement.

        acc_xValue = findViewById(R.id.acc_xValue);
        acc_yValue = findViewById(R.id.acc_yValue);
        acc_zValue = findViewById(R.id.acc_zValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(accelerometer != null)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            acc_xValue.setText("Accelerometer not supported");
        }
    }

    private void initGyroscope()
    {
        // A Gyroscope can be understood as a device that is used to maintain a reference direction or provide stability in navigation, stabilizers, etc. Similarly,
        // a gyroscope or a Gyro sensor is present in your smartphone to sense angular rotational velocity and acceleration. Gyro sensors, also known as angular
        // rate sensors or angular velocity sensors, are devices that sense angular velocity. Angular velocity. In simple terms, angular velocity is the change in
        // rotational angle per unit of time. Angular velocity is generally expressed in deg/s (degrees per second).

        gyroscope_xValue = findViewById(R.id.gyro_xValue);
        gyroscope_yValue = findViewById(R.id.gyro_yValue);
        gyroscope_zValue = findViewById(R.id.gyro_zValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gyroscope != null)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            gyroscope_xValue.setText("Gyroscope not supported");
        }
    }

    private void initMagnetometer()
    {
        magnet_xValue = findViewById(R.id.magnet_xValue);
        magnet_yValue = findViewById(R.id.magnet_yValue);
        magnet_zValue = findViewById(R.id.magnet_zValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if(magnetometer != null)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            magnet_xValue.setText("Magnetic field is not supported");
        }
    }

    private void initLight()
    {
        light_xValue = findViewById(R.id.light_xValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(light != null)
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            light_xValue.setText("Light not supported");
        }
    }

    private void initPressure()
    {
        press_xValue = findViewById(R.id.pressure_xValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        if(pressure != null)
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            press_xValue.setText("Pressure not supported");
        }
    }

    private void initTemperature()
    {
        temp_xValue = findViewById(R.id.temp_xValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if(temperature != null)
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            temp_xValue.setText("Temperature not supported");
        }
    }

    private void initHumidity()
    {
        humidity_xValue = findViewById(R.id.humidity_xValue);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        if(humidity != null)
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        else
        {
            humidity_xValue.setText("Humidity not supported");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        initAccelerometer();
        initGyroscope();
        initMagnetometer();
        initLight();
        initPressure();
        initTemperature();
        initHumidity();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor sensor = sensorEvent.sensor;

        switch(sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                acc_xValue.setText("x : " + sensorEvent.values[0]);
                acc_yValue.setText("y : " + sensorEvent.values[1]);
                acc_zValue.setText("z : " + sensorEvent.values[2]);

                break;

            case Sensor.TYPE_GYROSCOPE:
                gyroscope_xValue.setText("x : " + sensorEvent.values[0]);
                gyroscope_yValue.setText("y : " + sensorEvent.values[1]);
                gyroscope_zValue.setText("z : " + sensorEvent.values[2]);

                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                magnet_xValue.setText("x : " + sensorEvent.values[0]);
                magnet_yValue.setText("y : " + sensorEvent.values[1]);
                magnet_zValue.setText("z : " + sensorEvent.values[2]);

                break;

            case Sensor.TYPE_LIGHT:
                light_xValue.setText("Light : " + sensorEvent.values[0]);

                break;

            case Sensor.TYPE_PRESSURE:
                press_xValue.setText("Pressure : " + sensorEvent.values[0]);

                break;

            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                temp_xValue.setText("Temperature : " + sensorEvent.values[0]);

                break;

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                humidity_xValue.setText("Humidity : " + sensorEvent.values[0]);

                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }
}