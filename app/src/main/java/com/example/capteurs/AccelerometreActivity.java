package com.example.capteurs;


import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AccelerometreActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometre);

        rootView = findViewById(R.id.rootLayout); // Récupérer la vue principale

        // Initialisation du gestionnaire de capteurs
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float acceleration = Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2]);

            // Définir les couleurs selon les catégories
            if (acceleration < 10) {
                rootView.setBackgroundColor(Color.GREEN); // Faible mouvement
            } else if (acceleration >= 10 && acceleration < 20) {
                rootView.setBackgroundColor(Color.BLACK); // Mouvement moyen
            } else {
                rootView.setBackgroundColor(Color.RED); // Mouvement intense
            }
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
