package com.example.capteurs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ListeCapteursActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private ListView sensorListView;
    private List<Sensor> sensors;

    Logger logger = Logger.getLogger(ListeCapteursActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_capteurs);

        sensorListView = findViewById(R.id.sensorListView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        listSensors();
    }

    private void listSensors() {
        List<String> sensorNames = new ArrayList<>();

        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNames);
        sensorListView.setAdapter(adapter);

        // Gérer le clic sur un capteur pour afficher le popup personnalisé
        sensorListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Sensor selectedSensor = sensors.get(position);
            showSensorDetailsDialog(selectedSensor);
        });
    }

    @SuppressLint("SetTextI18n")
    private void showSensorDetailsDialog(Sensor sensor) {
        Dialog dialog = new Dialog(this);

        // Rendre le fond transparent pour éviter les bords carrés
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.dialogue_sensor_detail);
        dialog.setCancelable(true);

        // Récupérer les vues du popup
        TextView sensorName = dialog.findViewById(R.id.sensorName);
        TextView sensorDetails = dialog.findViewById(R.id.sensorDetails);
        Button closeButton = dialog.findViewById(R.id.closeButton);

        // Remplir les détails
        sensorName.setText(sensor.getName());
        sensorDetails.setText(
                getString(R.string.type) + " : " + sensor.getType() + "\n\n" +
                        getString(R.string.version) + " : " + sensor.getVersion() + "\n\n" +
                        getString(R.string.resolution) + " : " + sensor.getResolution() + "\n\n" +
                        getString(R.string.power) + " : " + sensor.getPower() + " mA\n\n" +
                        getString(R.string.vendor) + " : " + sensor.getVendor() + "\n\n" +
                        getString(R.string.maximum_range) + " : " + sensor.getMaximumRange() + "\n\n" +
                        getString(R.string.minimum_delay) + " : " + sensor.getMinDelay()
        );


        // Bouton pour fermer la boîte de dialogue
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Afficher le popup
        dialog.show();
    }

}
