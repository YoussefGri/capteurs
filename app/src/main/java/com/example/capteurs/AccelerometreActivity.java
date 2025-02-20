package com.example.capteurs;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Logger;

public class AccelerometreActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private BouleView bouleView;

    private Logger logger = Logger.getLogger(AccelerometreActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometre);

        bouleView = findViewById(R.id.bouleView); // Récupérer la vue personnalisée

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

            // Calculer le déplacement progressif de la boule
            float deltaX = event.values[0] * 5; // Ajuster la sensibilité du mouvement
            float deltaY = -event.values[1] * 5; // Inverser Y pour correspondre à l'écran

            bouleView.updatePosition(deltaX, deltaY);

            logger.info("Acceleration: " + acceleration);

            // Changer la couleur de la boule selon les catégories
            if (acceleration < 10) {
                bouleView.setColor(Color.GREEN); // Faible mouvement
            } else if (acceleration >= 10 && acceleration < 12) {
                bouleView.setColor(Color.BLACK); // Mouvement moyen
            } else {
                bouleView.setColor(Color.RED); // Mouvement intense
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