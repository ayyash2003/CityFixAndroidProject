package com.example.cityfixmopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityfixmopapp.apiclient.ApiClient;
import com.example.cityfixmopapp.getapi.ApiService;
import com.example.cityfixmopapp.model.SignupRequest;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText nameEditText ;
    EditText emailEditText ;
    EditText phoneEditText;
    Button signUpButton ;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        set();
    }
    private void set(){
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        signUpButton = findViewById(R.id.signUpButton);
    }
    public void SignUpRegisterOnClick(View view){
        String email = emailEditText.getText().toString();
        String name =nameEditText.getText().toString();
        String phoneNumber =phoneEditText.getText().toString();

        if (isValidEmail(email)) {

            if(name.isBlank() || phoneNumber.isBlank()){
                Toast.makeText(this, "There a empty filed", Toast.LENGTH_LONG).show();
                return;
            }
            if(phoneNumber.length()>20){
                Toast.makeText(this, "Phone number should less than 20 digits", Toast.LENGTH_LONG).show();
                return ;
            }
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            SignupRequest signupRequest = new SignupRequest(name, email, phoneNumber);
            Call<ResponseBody> call = apiService.signupUser(signupRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }else {
                        emailEditText.setText("Error: " + response.message());
                        Toast.makeText(SignUpActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    emailEditText.setText("error: " + t.getMessage());
                    Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}