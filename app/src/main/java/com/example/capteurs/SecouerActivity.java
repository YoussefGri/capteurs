package com.example.capteurs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecouerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean isFlashOn = false; // État du flash ou écran blanc
    private long lastShakeTime = 0; // Temps du dernier mouvement détecté
    private static final int SHAKE_THRESHOLD = 15; // Seuil de détection de secousse
    private TextView statusTextView; // Affichage de l'état
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secouer);

        statusTextView = findViewById(R.id.statusTextView);
        window = getWindow();

        // Initialisation du capteur
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
            long currentTime = System.currentTimeMillis();

            if (acceleration > SHAKE_THRESHOLD && (currentTime - lastShakeTime > 1000)) {
                lastShakeTime = currentTime;
                toggleScreenTorch();
            }
        }
    }

    private void toggleScreenTorch() {
        isFlashOn = !isFlashOn;
        if (isFlashOn) {
            window.getDecorView().setBackgroundColor(getResources().getColor(android.R.color.white));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = 1.0f; // Luminosité max
            window.setAttributes(layoutParams);
            statusTextView.setText("Écran en mode torche 🔆");
        } else {
            window.getDecorView().setBackgroundColor(getResources().getColor(android.R.color.black));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            window.setAttributes(layoutParams);
            statusTextView.setText("Écran normal 🌙");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Non utilisé
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}
