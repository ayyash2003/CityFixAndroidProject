package com.example.cityfixmopapp;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReportHazardActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hazard);

        // Initialize views
        locationTextView = findViewById(R.id.locationTextView);
        ImageButton captureImageButton = findViewById(R.id.cameraButton);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        EditText hazardDescriptionEditText = findViewById(R.id.hazardDescriptionEditText);
        Button submitHazardButton = findViewById(R.id.submitHazardButton);

        // Initialize bottom navigation buttons
        ImageButton profileButton = findViewById(R.id.profileButton);
        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton bellButton = findViewById(R.id.bellButton);

        // Set up the category dropdown
        String[] categories = {"Road Damage", "Water Leakage", "Electric Hazard", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Detect location (mocked for now)
        detectLocation();

        // Handle image capture
        captureImageButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        });

        // Handle hazard submission
        submitHazardButton.setOnClickListener(v -> {
            String description = hazardDescriptionEditText.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please provide a description", Toast.LENGTH_SHORT).show();
            } else {
                // Logic to handle hazard submission (e.g., send to server or save locally)
                Toast.makeText(this, "Hazard reported successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listeners for bottom navigation buttons
        profileButton.setOnClickListener(v -> {
            // Navigate to Profile Activity
            Intent intent = new Intent(ReportHazardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            // Navigate to Home Activity
            Intent intent = new Intent(ReportHazardActivity.this, MainActivity.class);
            startActivity(intent);
        });

        bellButton.setOnClickListener(v -> {
            // Handle bell button click
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void detectLocation() {
        // Mock location detection
        String mockLocation = "Ramallah";
        locationTextView.setText("Detected Location: " + mockLocation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image captured successfully!", Toast.LENGTH_SHORT).show();
            // Handle the captured image (e.g., display or save it)
        }
    }
}