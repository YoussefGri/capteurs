package com.example.capteurs;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccelerometreActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private BouleView bouleView;
    private TextView accelerationText;
    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometre);

        bouleView = findViewById(R.id.bouleView); // Récupérer la vue personnalisée
        accelerationText = findViewById(R.id.accelerationText); // Récupérer le TextView pour l'accélération
        messageText = findViewById(R.id.messageText); // Récupérer le TextView pour le message

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

            // Mettre à jour le texte de l'accélération
            accelerationText.setText(String.format("Accélération : %.2f", acceleration));

            // Mettre à jour le message en fonction de l'intensité du mouvement
            if (acceleration < 10) {
                messageText.setText("Tout est calme...");
                messageText.setTextColor(Color.GREEN);
            } else if (acceleration >= 10 && acceleration < 12) {
                messageText.setText("On bouge un peu !");
                messageText.setTextColor(Color.BLACK);
            } else {
                messageText.setText("Ça secoue !");
                messageText.setTextColor(Color.RED);
            }

            // Calculer le déplacement progressif de la boule
            float deltaX = event.values[0] * 5; // Ajuster la sensibilité du mouvement
            float deltaY = -event.values[1] * 5; // Inverser Y pour correspondre à l'écran

            bouleView.updatePosition(deltaX, deltaY);

            // Changer la couleur de la boule selon les catégories
            if (acceleration < 10) {
                bouleView.setColor(Color.GREEN); // Faible mouvement
            } else if (acceleration >= 10 && acceleration < 20) {
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