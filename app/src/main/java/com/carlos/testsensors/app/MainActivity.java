package com.carlos.testsensors.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ProgressBar progressBar;
    private Button lightButton;
    private TextView maxLightText;
    private TextView currentLightText;

    private int currentLightLevel;
    private int maxLightLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lightButton = (Button) findViewById(R.id.button);
        maxLightText = (TextView) findViewById(R.id.textMaxLight);
        currentLightText = (TextView) findViewById(R.id.textCurrentLight);

        lightSensor();
        lightButton();
        RedFlashLight();
    }

    private void lightSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors =  sensorManager.getSensorList(Sensor.TYPE_ALL);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        maxLightLevel = Math.round(sensor.getMaximumRange() / 300);

        SensorEventListener sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                currentLightLevel = Math.round(sensorEvent.values[0]);

                progressBar.setMax(maxLightLevel);

                progressBar.setProgress(currentLightLevel);
                currentLightText.setText(Integer.toString(currentLightLevel));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void lightButton () {
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxLightLevel = currentLightLevel;
                maxLightText.setText(Integer.toString(maxLightLevel));
            }
        });
    }

    private void RedFlashLight() {
        NotificationManager notificationManager = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        Notification.Builder notification = new Notification.Builder(this);
        notification.setLights(Color.RED, 50, 50);
        notification.setDefaults(0);
        notificationManager.notify("lights", 1, notification.build());
    }
}