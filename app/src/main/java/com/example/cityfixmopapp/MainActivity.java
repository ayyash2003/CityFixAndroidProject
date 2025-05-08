package com.example.cityfixmopapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons by their IDs
        ImageButton profileButton = findViewById(R.id.profileButton);
        ImageButton bellButton = findViewById(R.id.bellButton);
        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton logoutButton = findViewById(R.id.logoutButton);
        Button updateProfileButton = findViewById(R.id.updateProfileButton);
        Button reportHazardButton = findViewById(R.id.reportHazardButton);


        updateProfileButton.setOnClickListener(v -> {
            // Navigate to UpdateProfileActivity
            Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        });
        reportHazardButton.setOnClickListener(v -> {
            // Navigate to ReportHazardActivity
            Intent intent = new Intent(MainActivity.this, ReportHazardActivity.class);
            startActivity(intent);
        });



        // Set click listeners for each button
        updateProfileButton.setOnClickListener(v -> {

            Toast.makeText(MainActivity.this, "Update Profile Button Clicked", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        });
        profileButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        });

        bellButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });
        homeButton.setOnClickListener(v -> {
            // Handle home button click
            Toast.makeText(this, "Already on home page", Toast.LENGTH_SHORT).show();
        });
        logoutButton.setOnClickListener(v -> {
            // Handle logout action
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}