package com.example.cityfixmopapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityfixmopapp.apiclient.ApiClient;
import com.example.cityfixmopapp.getapi.ApiService;
import com.example.cityfixmopapp.model.GetEmail;
import com.example.cityfixmopapp.model.GetLocation;
import com.example.cityfixmopapp.model.Location;
import com.example.cityfixmopapp.model.SignupRequest;
import com.example.cityfixmopapp.model.SubmitHazardRequest;
import com.example.cityfixmopapp.model.UploadResponse;
import com.example.cityfixmopapp.model.UploadResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportHazardActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private TextView locationTextView;
    private ImageButton captureImageButton ;
    private ImageButton profileButton ;
    private ImageButton homeButton ;
    private ImageButton bellButton ;
    private Spinner categorySpinner ;
    private EditText hazardDescriptionEditText ;
    private Button submitHazardButton ;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static UploadResponse URLIMAGE ;
    private int citizenId ;
    SharedPreferences sharedPreferences;
    Location location ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hazard);

        InitializeView();

        sharedPreferences = getSharedPreferences(LoginActivity.CitizenPreference, MODE_PRIVATE);
        citizenId = sharedPreferences.getInt(LoginActivity.CitizenIDPreference, -1);

        location=new Location(0.0,0.0);
        getLatlong();
        // Set up the category dropdown
        String[] categories = {"Road Damage", "Water Leakage", "Electric Hazard", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
  //      Toast.makeText(ReportHazardActivity.this, location.getLatitude()+" isssss my location", Toast.LENGTH_LONG).show();

        // Detect location (mocked for now)
     //   detectLocation();

    }
    private void getLatlong(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }
    }
    private void getLocation() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // 1 second
        locationRequest.setFastestInterval(500);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(ReportHazardActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                fusedLocationClient.removeLocationUpdates(this); // Stop after first result
                double lat =locationResult.getLastLocation().getLatitude();
                double lon =locationResult.getLastLocation().getLongitude();
                location.setLatitude(lat);
                location.setLongitude(lon);

                detectLocation( lat , lon);
                Toast.makeText(ReportHazardActivity.this, "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude(), Toast.LENGTH_LONG).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera(); // 👈 This is where you call your camera logic
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void InitializeView(){
// Initialize views
        locationTextView = findViewById(R.id.locationTextView);
        captureImageButton = findViewById(R.id.cameraButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        hazardDescriptionEditText = findViewById(R.id.hazardDescriptionEditText);
        submitHazardButton = findViewById(R.id.submitHazardButton);

        // Initialize bottom navigation buttons
        profileButton = findViewById(R.id.profileButton);
        homeButton = findViewById(R.id.homeButton);
        bellButton = findViewById(R.id.bellButton);


    }
    public void captureImageButtonOnClick(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera(); // Permission is already granted
        }
    }
    // Set click listeners for bottom navigation buttons
    public void profileButtonOnClick(View view){
        // Navigate to Profile Activity
        Intent intent = new Intent(ReportHazardActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void homeButtonOnClick(View view){
        // Navigate to Home Activity
        Intent intent = new Intent(ReportHazardActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void bellButtonOnClick(View view){
        Intent intent = new Intent(ReportHazardActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }
    // Handle hazard submission
    public void submitHazardButtonOnClick(View view){
        String description = hazardDescriptionEditText.getText().toString().trim();
        int category = categorySpinner.getSelectedItemPosition();
        Log.d("check address id", "addressId: "+location.getAddressId());
        if(URLIMAGE!= null){
            Toast.makeText(this, "URL IMage : "+URLIMAGE.getUrlImage(), Toast.LENGTH_LONG).show();
            Log.d("URL Image","URL Image"+URLIMAGE.getUrlImage());
        }


        if (description.isEmpty()) {
            Toast.makeText(this, "Please provide a description", Toast.LENGTH_SHORT).show();
        } else {
            // Logic to handle hazard submission (e.g., send to server or save locally)
            if(location.getValid()==false){
                Log.d("Check Location","This location doesn't follow any registered municipality ");
                Toast.makeText(this, "This location doesn't follow any registered municipality", Toast.LENGTH_SHORT).show();
                return ;
            }
            if(URLIMAGE==null){
                Log.d("Check URL Image if exist","Please Capture image for the problem");
                Toast.makeText(this, "Please Capture image for the problem", Toast.LENGTH_SHORT).show();
                return ;
            }

//            if(URLIMAGE.isSuccess()==false){
//                Log.d("Check URL Image if fake or not","Please Capture a valid image for the problem ");
//                Toast.makeText(this, "Please Capture a valid image for the problem", Toast.LENGTH_SHORT).show();
//                return ;
//            }
            String title ="test";
            String urlImage =URLIMAGE.getUrlImage();
            int addressId =location.getAddressId();
            int municipaltyId =location.getMunicapilityId();

            Toast.makeText(this, "Submit baby", Toast.LENGTH_LONG).show();
            Log.d("Submit","submit");
            submitHazard(title ,urlImage,category,description,addressId,municipaltyId,citizenId);

            Toast.makeText(this, "Hazard reported successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    private void submitHazard(String title ,String urlImage ,int category ,String description ,
                              int addressId ,int municipaltyId,int citizenId){



        //Submit hazard \this code will send data to the api
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        SubmitHazardRequest submitHazardRequest = new SubmitHazardRequest(title, urlImage, category
        ,description,addressId,municipaltyId,citizenId);
        Call<ResponseBody> call = apiService.submitHazard(submitHazardRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                      Toast.makeText(ReportHazardActivity.this, "Success", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(SubmitHazardRequest.this, LoginActivity.class);
//                    startActivity(intent);
                    removeItems();

                }else {
                   // emailEditText.setText("Error: " + response.message());
                     Toast.makeText(ReportHazardActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReportHazardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    //this method used to remove items and return them to default after the submit hazard sucess
    private void removeItems(){
        categorySpinner.setSelection(0);
        hazardDescriptionEditText.setText("");

    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void detectLocation(double lit ,double lon) {
        // Mock location detection
        String mockLocation = "Ramallah";
        locationTextView.setText("Detected Location: " + mockLocation);
        Toast.makeText(ReportHazardActivity.this, location.getLatitude()+" is my location", Toast.LENGTH_LONG).show();

        ApiService api = ApiClient.getClient().create(ApiService.class);
        //34.7822 ,32.0850
        Call<GetLocation> call = api.getLocation(34.7822,32.0850);
        call.enqueue(new Callback<GetLocation>() {
            @Override
            public void onResponse(Call<GetLocation> call, Response<GetLocation> response) {
                if (response.isSuccessful()) {
                    GetLocation getlocation = response.body();
                    location =new Location(lit,lon);
                    location.setValid(true);
                    location.setAddressId(getlocation.getAddressId());
                    location.setMunicapilityId(getlocation.getMunicipalityId());

                    String mockLocation=getlocation.getName().toString();
                    locationTextView.setText("Detected Location :" +mockLocation);
                    Toast.makeText(ReportHazardActivity.this, response.message().toString()+"", Toast.LENGTH_LONG).show();
                } else {
                    locationTextView.setText("Detected Location : this location doesn't support " );

                    Toast.makeText(ReportHazardActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetLocation> call, Throwable t) {
                locationTextView.setText("failed: " + t.getMessage());
                Toast.makeText(ReportHazardActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ReportHazardActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image captured successfully!", Toast.LENGTH_SHORT).show();
            Log.d("at an activity Result","Image captured successfully!");

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File imageFile = bitmapToFile(photo, "captured.jpg");

            if (imageFile != null) {
                uploadImageToServer(imageFile);
            }
            else{
                Log.d("image file ","Image file is null!// it exist at method onActivityResult");

            }
        }
    }
    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File filesDir = getApplicationContext().getCacheDir();
        File imageFile = new File(filesDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void uploadImageToServer(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<UploadResponse> call = api.uploadImage(body);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = response.body().getUrlImage();

                    Log.d("at an onResponse // success","Uploaded  "+imageUrl);
                    URLIMAGE= response.body();
                    Toast.makeText(getApplicationContext(), "Uploaded: " + imageUrl, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("at an onResponse// failed","Upload failed:  "+response.code() + " Message "+ response.message());

                    Toast.makeText(getApplicationContext(), "Upload failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.d("at an onFailure","Upload failed:  "+t.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}