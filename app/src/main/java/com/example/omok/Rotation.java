package com.example.omok;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class Rotation {
    private static final float ROTATION_THRESHOLD = 1.0f;   // rotation된 값의 임계점

    private Context context; // context 추가, for Toast msg.

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener; // sensor 관련

    private boolean isEventNeed = false;    // true면 회전 작동 실행 가능

    private int rotationDirection = 0; // default : 0

    public int getRotationDirection() {
        int n = rotationDirection;
        getRotationDirectionRenewal();
        return n;
    }   // to get rotation direction in GameActivity.java

    private void getRotationDirectionRenewal() {
        rotationDirection = 0;
    }

    public Rotation(Context context) {
        this.context = context; // 생성자에서 context 초기화
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyroscopeEventListener = new RotatingSensorEventListener();
    }

    public void registerGyroscope() {    // gyroscope 센서 사용하기 위한 선언
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(context, "Gyroscope sensor is not supported", Toast.LENGTH_SHORT).show();
            Log.d("Rotation", "Gyroscope sensor not supported!!");
        }
    }
    
    public void unregisterGyroscope() {     // gyroscope 센서 종료 선언 
        sensorManager.unregisterListener(gyroscopeEventListener);
    }

    public void setEventNeed(boolean eventNeed) {       // sensor 값을 받아올 것인가에 대한 설정
        isEventNeed = eventNeed;    
    } 

    private class RotatingSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {        // 실제 센서의 값을 return(float형식)
            if (!isEventNeed) {
                return;
            }

            float x = event.values[2];

            if (Math.abs(x) > ROTATION_THRESHOLD) {
                if (x > 0) {
                    rotationDirection = 1;
                    Log.d("Rotation", "Gyroscope : right, " + x);
                } else {
                    rotationDirection = -1;
                    Log.d("Rotation", "Gyroscope : left, " + x);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle sensor accuracy changes if needed
        }
    }
}