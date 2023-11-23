package com.example.omok;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class Rotation {
    private static final float ROTATION_THRESHOLD = 1.0f;

    private Context context; // context 추가

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;

    private boolean isEventNeed = false;    // true면 회전 작동 실행 가능

    private int rotationDirection = 0; // default : 0

    public int getRotationDirection() {
        return rotationDirection;
    }

    public Rotation(Context context) {
        this.context = context; // 생성자에서 context 초기화
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeEventListener = new RotatingSensorEventListener();
    }

    public void onResume() {
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(context, "Gyroscope sensor is not supported", Toast.LENGTH_SHORT).show();
        }
    }
    ;
    public void onPause() {
        sensorManager.unregisterListener(gyroscopeEventListener);
    }

    public void setEventNeed(boolean eventNeed) {
        isEventNeed = eventNeed;
    } 

    private class RotatingSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!isEventNeed) {
                return;
            }

            float x = event.values[0];

            if (Math.abs(x) > ROTATION_THRESHOLD) {
                if (x > 0) {
                    Toast.makeText(context, "Rotated to the right", Toast.LENGTH_SHORT).show();
                    rotationDirection = 1;
                    System.out.println("Right");
                } else {
                    Toast.makeText(context, "Rotated to the left", Toast.LENGTH_SHORT).show();
                    rotationDirection = -1;
                    System.out.println("Left");
                }
            } else {
                rotationDirection = 0;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle sensor accuracy changes if needed
        }
    }
}