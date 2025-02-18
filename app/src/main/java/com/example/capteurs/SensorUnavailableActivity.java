package com.example.capteurs;

import android.app.Activity;
import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorUnavailableActivity extends Activity {

    private ListView listView;
    private SensorManager sensorManager;
    private Map<String, String> featureMap; // Stocke les fonctionnalités affectées

    private List<String> allSensors = List.of(
            "Accelerometer", "Gyroscope", "Magnetometer", "Proximity Sensor",
            "Light Sensor", "Pressure Sensor", "Temperature Sensor",
            "Humidity Sensor", "Gravity Sensor", "Rotation Vector",
            "Heart Rate Sensor", "Step Counter", "Step Detector"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_unavailable);

        listView = findViewById(R.id.list_unavailable_sensors);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Initialisation des fonctionnalités affectées par l'absence d'un capteur
        initFeatureMap();

        // Charger la liste initialement
        loadUnavailableSensors();


        // Gestion du clic sur un élément de la liste
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String sensorName = (String) parent.getItemAtPosition(position);
            showUnavailableFeatureDialog(sensorName);
        });
    }

    private void loadUnavailableSensors() {
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        List<String> availableSensorNames = new ArrayList<>();
        for (Sensor sensor : availableSensors) {
            availableSensorNames.add(sensor.getName());
        }

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unavailableSensors);
        listView.setAdapter(adapter);
    }

    private void initFeatureMap() {
        featureMap = new HashMap<>();
        featureMap.put("Accelerometer", "Jeux, détection de mouvement, podomètre.");
        featureMap.put("Gyroscope", "VR, stabilisation de l'appareil photo, jeux en 3D.");
        featureMap.put("Magnetometer", "Boussole, navigation GPS améliorée.");
        featureMap.put("Proximity Sensor", "Extinction automatique de l'écran lors des appels.");
        featureMap.put("Light Sensor", "Ajustement automatique de la luminosité.");
        featureMap.put("Pressure Sensor", "Prévisions météorologiques basées sur la pression.");
        featureMap.put("Temperature Sensor", "Détection de la température ambiante.");
        featureMap.put("Humidity Sensor", "Détection de l'humidité ambiante.");
        featureMap.put("Gravity Sensor", "Optimisation du gyroscope et de l’accéléromètre.");
        featureMap.put("Rotation Vector", "Jeux VR, orientation précise de l'écran.");
        featureMap.put("Heart Rate Sensor", "Applications de suivi de la santé.");
        featureMap.put("Step Counter", "Suivi des pas et activités sportives.");
        featureMap.put("Step Detector", "Reconnaissance des mouvements de marche.");
    }

    private void showUnavailableFeatureDialog(String sensorName) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_unavailable_features);
        dialog.setCancelable(true);

        TextView sensorTitle = dialog.findViewById(R.id.dialog_sensor_title);
        TextView featureList = dialog.findViewById(R.id.dialog_feature_list);
        MaterialButton btnClose = dialog.findViewById(R.id.dialog_close_button);

        sensorTitle.setText(sensorName);
        featureList.setText(featureMap.getOrDefault(sensorName, "Aucune information disponible."));

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
