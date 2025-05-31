package com.example.cityfixmopapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton imgBtProfile ;
    ImageButton imgBtBell ;
    ImageButton imgBtHome ;
    ImageButton imgBtLogout ;
    Button btUpdateProfile;
    Button btReportHazard;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        set();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.CitizenPreference, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(LoginActivity.CitizenIDPreference, -1);
        Toast.makeText(this,"Citizen id: "+userId,Toast.LENGTH_LONG).show();
        // Set click listeners for each button
//        updateProfileButton.setOnClickListener(v -> {
//
//            Toast.makeText(MainActivity.this, "Update Profile Button Clicked", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
//            startActivity(intent);
//        });




    }
    public void UpdateProfileOnClick(View view){
        // Navigate to UpdateProfileActivity
        Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }
    public void ReportHazardOnClick(View view){
        // Navigate to UpdateProfileActivity
        Intent intent = new Intent(MainActivity.this, ReportHazardActivity.class);
        startActivity(intent);
    }

    public void ProfileOnClick(View view){
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void BellOnClick(View view){
        Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }
    public void HomeOnClick(View view){
        // Handle home button click
        Toast.makeText(this, "Already on home page", Toast.LENGTH_SHORT).show();
    }
    public void LogoutClick(View view){
    // Handle logout action
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
    private void set(){
        imgBtProfile = findViewById(R.id.profileButton);
        imgBtBell = findViewById(R.id.bellButton);
        imgBtHome = findViewById(R.id.homeButton);
        imgBtLogout = findViewById(R.id.logoutButton);
        btUpdateProfile = findViewById(R.id.updateProfileButton);
        btReportHazard = findViewById(R.id.reportHazardButton);
    }

}