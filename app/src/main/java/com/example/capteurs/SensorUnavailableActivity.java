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
        featureMap.put("Accelerometer", getString(R.string.accelerometer_features));
        featureMap.put("Gyroscope", getString(R.string.gyroscope_features));
        featureMap.put("Magnetometer", getString(R.string.magnetometer_features));
        featureMap.put("Proximity Sensor", getString(R.string.proximity_features));
        featureMap.put("Light Sensor", getString(R.string.light_features));
        featureMap.put("Pressure Sensor", getString(R.string.pressure_features));
        featureMap.put("Temperature Sensor", getString(R.string.temperature_features));
        featureMap.put("Humidity Sensor", getString(R.string.humidity_features));
        featureMap.put("Gravity Sensor", getString(R.string.gravity_features));
        featureMap.put("Rotation Vector", getString(R.string.rotation_features));
        featureMap.put("Heart Rate Sensor", getString(R.string.heart_rate_features));
        featureMap.put("Step Counter", getString(R.string.step_counter_features));
        featureMap.put("Step Detector", getString(R.string.step_detector_features));
    }

    private void showUnavailableFeatureDialog(String sensorName) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_unavailable_features);
        dialog.setCancelable(true);

        TextView sensorTitle = dialog.findViewById(R.id.dialog_sensor_title);
        TextView featureList = dialog.findViewById(R.id.dialog_feature_list);
        MaterialButton btnClose = dialog.findViewById(R.id.dialog_close_button);

        sensorTitle.setText(sensorName);
        featureList.setText(featureMap.getOrDefault(sensorName, getString(R.string.no_info_available)));

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
