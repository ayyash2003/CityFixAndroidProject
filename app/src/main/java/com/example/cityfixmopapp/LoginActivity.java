package com.example.cityfixmopapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixmopapp.apiclient.ApiClient;
import com.example.cityfixmopapp.getapi.ApiService;
import com.example.cityfixmopapp.model.LoginRequest;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    SharedPreferences sharedPreferences ;
    public static final String CitizenPreference ="CitezenKey";
    public static final String CitizenIDPreference="CitezenID";
    private EditText edtEmail;
    private Button btLogin;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        set();
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<String> call = api.getData();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    edtEmail.setText(result);
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                edtEmail.setText("failed: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void LoginOnClick(View view) {
        String email = edtEmail.getText().toString();

        if (isValidEmail(email)) {
            // Navigate to MainActivity on successful login
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            LoginRequest loginRequest = new LoginRequest(email);
            Call<ResponseBody> call = apiService.loginUser(loginRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String status = null;
                            status =response.message().toString();
                        String i = null;
                        try {
                            i = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        int id =Integer.parseInt(i);
                        Toast.makeText(LoginActivity.this, "Hello ", Toast.LENGTH_LONG).show();
                        if ("OK".equalsIgnoreCase(status)) {
                            // ✅ Login successful
                            Toast.makeText(LoginActivity.this, "Hi ", Toast.LENGTH_LONG).show();
                            sharedPreferences = getSharedPreferences(CitizenPreference, MODE_PRIVATE);

// get editor to modify the preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putInt(CitizenIDPreference, id);
// apply changes
                            Toast.makeText(LoginActivity.this, "ID :  "+id, Toast.LENGTH_LONG).show();
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "is an invalid email " + status, Toast.LENGTH_LONG).show();
                        }
                    } else if (response.message().equals("Unauthorized")) {
                        edtEmail.setText("is an invalid email: " + response.message());
                        Toast.makeText(LoginActivity.this, "is an invalid email " + response.message(), Toast.LENGTH_LONG).show();
                    } else {
                        edtEmail.setText("Error: " + response.message());
                        Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    edtEmail.setText("error: " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } else {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
        }
    }
    public void SignupOnClick(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    public void set(){
        edtEmail  = findViewById(R.id.emailEditText);
        btLogin = findViewById(R.id.loginButton);
        tvSignUp = findViewById(R.id.signUpTextView);
    }
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}