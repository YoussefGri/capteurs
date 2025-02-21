package com.example.capteurs;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Logger;

public class DirectionActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView directionTextView;
    private Logger logger = Logger.getLogger(DirectionActivity.class.getName());

    private float[] gravity = new float[3]; // Stocker la gravité pour la soustraire
    private boolean gravityInitialized = false; // Vérifier si la gravité est initialisée

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        directionTextView = findViewById(R.id.directionTextView); // Texte affichant la direction

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
            float x = event.values[0]; // Accélération sur X
            float y = event.values[1]; // Accélération sur Y
            String direction = "Stable";

            // Seuils pour éviter les petites variations inutiles
            float threshold = 1.5f; // Sensibilité pour détecter les mouvements
            float stableThreshold = 0.5f; // Tolérance pour la position stable

            // Détection de la position stable (téléphone posé à plat)
            if (Math.abs(x) < stableThreshold && Math.abs(y - 9.8) < stableThreshold) {
                direction = "Position Stable 📱";
            }
            // Détection des mouvements diagonaux
            else if (Math.abs(x) > threshold || Math.abs(y) > threshold) {
                if (x > threshold && y > threshold) {
                    direction = "Haut Gauche ⬉";
                } else if (x < -threshold && y > threshold) {
                    direction = "Haut Droite ⬈";
                } else if (x > threshold && y < -threshold) {
                    direction = "Bas Gauche ⬋";
                } else if (x < -threshold && y < -threshold) {
                    direction = "Bas Droite ⬊";
                }
                // Détection des mouvements verticaux/horizontaux purs
                else if (Math.abs(x) < threshold && y > threshold) {
                    direction = "Haut ⬆️";
                } else if (Math.abs(x) < threshold && y < -threshold) {
                    direction = "Bas ⬇️";
                } else if (x > threshold && Math.abs(y) < threshold) {
                    direction = "Gauche ⬅️";
                } else if (x < -threshold && Math.abs(y) < threshold) {
                    direction = "Droite ➡️";
                }
            }

            // Mettre à jour l'affichage uniquement si la direction change
            if (!direction.equals(directionTextView.getText().toString().replace("Direction : ", ""))) {
                directionTextView.setText("Direction : " + direction);
            }

            // Log des valeurs pour le débogage
            logger.info("x : " + x + ", y : " + y + " → Direction : " + direction);
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
