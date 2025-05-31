package com.example.cityfixmopapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixmopapp.apiclient.ApiClient;
import com.example.cityfixmopapp.getapi.ApiService;
import com.example.cityfixmopapp.model.GetCitizen;
import com.example.cityfixmopapp.model.UpdateCitizen;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText edtName;
    EditText edtEmail;
    EditText edtPhone;
    Button saveButton;
    ImageButton profileButton;
    ImageButton bellButton;
    ImageButton homeButton;
    ImageButton logoutButton;
    private int citizenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Find views by their IDs
         set();
         //Get User id by reference
        sharedPreferenceCitizenId();
        showCitizenInformation();


    }
    private void sharedPreferenceCitizenId(){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.CitizenPreference, MODE_PRIVATE);
        citizenId = sharedPreferences.getInt(LoginActivity.CitizenIDPreference, -1);
        Toast.makeText(this,"Citizen id: "+citizenId,Toast.LENGTH_LONG).show();
        Log.d("CitizenId","Citizen Idd: "+citizenId);
    }
    private void set(){
        edtName = findViewById(R.id.nameEditText);
        edtEmail = findViewById(R.id.emailEditText);
        edtPhone = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);
        profileButton = findViewById(R.id.profileButton);
        bellButton = findViewById(R.id.bellButton);
        homeButton = findViewById(R.id.homeButton);
        logoutButton = findViewById(R.id.logoutButton);
    }
    public void saveButtonOnClick(View view){
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();


        //   showCitizenInformation();
        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        updateCitizenInformation(name,email,phone);

        // Add logic to save the updated profile information (e.g., database or shared preferences)

    }
    public void profileButtonUpdateOnClick(View view){
        Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void bellUpdateOnClick(View view){
        Intent intent = new Intent(UpdateProfileActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }
    public void logoutButtonUpdateOnClick(View view){
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UpdateProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
    public void HomeUpdateOnClick(View view){
        // Navigate to Home Activity
        Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void showCitizenInformation(){
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<GetCitizen> call = api.getCitizenById(citizenId);
        Toast.makeText(UpdateProfileActivity.this, "Name", Toast.LENGTH_LONG).show();
        Log.d("Where the error","error "+citizenId);

        call.enqueue(new Callback<GetCitizen>() {
            @Override
            public void onResponse(Call<GetCitizen> call, Response<GetCitizen> response) {
                if (response.isSuccessful()&& response.body() != null) {
                    GetCitizen citizen = response.body();
                    String name =citizen.getName();
                    String email =citizen.getEmail();
                    String phoneNumber =citizen.getPhoneNumber();

                    edtName.setText(name);
                    edtEmail.setText(email);
                    edtPhone.setText(phoneNumber);


                    Toast.makeText(UpdateProfileActivity.this, "name is "+citizen.getEmail(), Toast.LENGTH_LONG).show();
                    Log.d("Show Name","vv"+citizen.getName());
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCitizen> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(UpdateProfileActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateCitizenInformation(String name ,String email ,String phone){
        UpdateCitizen updatedCitizen = new UpdateCitizen(name, email, phone);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = api.updateCitizen(citizenId,updatedCitizen);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Save the updated profile information

                    Toast.makeText(UpdateProfileActivity.this, "Profile updated successfully: "+response.message().toString(), Toast.LENGTH_LONG).show();
                    Log.d("Sucess","message: "+response.message().toString());

                    // Close the activity
                    finish();
                } else {
                    Log.d("Error","API Error: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(UpdateProfileActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

            }
        });
    }
}