package com.example.capteurs;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.logging.Logger;

public class DirectionActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private CardView cardStable, cardHaut, cardBas, cardGauche, cardDroite; // Cartes pour chaque direction
    private Logger logger = Logger.getLogger(DirectionActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        // Récupérer les cartes
        cardStable = findViewById(R.id.cardStable);
        cardHaut = findViewById(R.id.cardHaut);
        cardBas = findViewById(R.id.cardBas); // Ajouter dans le layout
        cardGauche = findViewById(R.id.cardGauche); // Ajouter dans le layout
        cardDroite = findViewById(R.id.cardDroite); // Ajouter dans le layout

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

            // Seuils pour éviter les petites variations inutiles
            float threshold = 1.5f; // Sensibilité pour détecter les mouvements
            float stableThreshold = 0.5f; // Tolérance pour la position stable

            // Masquer toutes les cartes par défaut
            cardStable.setVisibility(View.GONE);
            cardHaut.setVisibility(View.GONE);
            cardBas.setVisibility(View.GONE);
            cardGauche.setVisibility(View.GONE);
            cardDroite.setVisibility(View.GONE);

            // Détection de la position stable (téléphone posé à plat)
            if (Math.abs(x) < stableThreshold && Math.abs(y - 9.8) < stableThreshold) {
                cardStable.setVisibility(View.VISIBLE);
            }
            // Détection des mouvements diagonaux
            else if (Math.abs(x) > threshold || Math.abs(y) > threshold) {
                if (x > threshold && y > threshold) {
                    cardHaut.setVisibility(View.VISIBLE); // Exemple : Haut Gauche
                } else if (x < -threshold && y > threshold) {
                    cardHaut.setVisibility(View.VISIBLE); // Exemple : Haut Droite
                } else if (x > threshold && y < -threshold) {
                    cardBas.setVisibility(View.VISIBLE); // Exemple : Bas Gauche
                } else if (x < -threshold && y < -threshold) {
                    cardBas.setVisibility(View.VISIBLE); // Exemple : Bas Droite
                }
                // Détection des mouvements verticaux/horizontaux purs
                else if (Math.abs(x) < threshold && y > threshold) {
                    cardHaut.setVisibility(View.VISIBLE);
                } else if (Math.abs(x) < threshold && y < -threshold) {
                    cardBas.setVisibility(View.VISIBLE);
                } else if (x > threshold && Math.abs(y) < threshold) {
                    cardGauche.setVisibility(View.VISIBLE);
                } else if (x < -threshold && Math.abs(y) < threshold) {
                    cardDroite.setVisibility(View.VISIBLE);
                }
            }

            // Log des valeurs pour le débogage
            logger.info("x : " + x + ", y : " + y);
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