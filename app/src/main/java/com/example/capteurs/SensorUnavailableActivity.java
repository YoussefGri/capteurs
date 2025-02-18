package com.example.capteurs;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SensorUnavailableActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_unavailable);

        ListView listView = findViewById(R.id.list_unavailable_sensors);

        // Liste des capteurs possibles sur Android
        String[] allSensors = {
                "Accelerometer", "Gyroscope", "Magnetometer", "Proximity Sensor",
                "Light Sensor", "Pressure Sensor", "Temperature Sensor",
                "Humidity Sensor", "Gravity Sensor", "Rotation Vector",
                "Heart Rate Sensor", "Step Counter", "Step Detector"
        };

        // Accès au service des capteurs
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Stockage des capteurs disponibles
        List<String> availableSensorNames = new ArrayList<>();
        for (Sensor sensor : availableSensors) {
            availableSensorNames.add(sensor.getName());
        }

        // Déterminer les capteurs absents
        List<String> unavailableSensors = new ArrayList<>();
        for (String sensorName : allSensors) {
            boolean found = false;
            for (String availableName : availableSensorNames) {
                if (availableName.toLowerCase().contains(sensorName.toLowerCase())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                unavailableSensors.add(sensorName);
            }
        }

        // Affichage des capteurs non disponibles dans la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unavailableSensors);
        listView.setAdapter(adapter);
    }
}
