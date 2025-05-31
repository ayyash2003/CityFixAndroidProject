package com.example.cityfixmopapp.getapi;

import com.example.cityfixmopapp.model.GetCitizen;
import com.example.cityfixmopapp.model.GetLocation;
import com.example.cityfixmopapp.model.LoginRequest;
import com.example.cityfixmopapp.model.SignupRequest;
import com.example.cityfixmopapp.model.SubmitHazardRequest;
import com.example.cityfixmopapp.model.UpdateCitizen;
import com.example.cityfixmopapp.model.UploadResponse;
import com.example.cityfixmopapp.model.UploadResponse2;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/Citizen/GetFirst")// Relative path of your ASP.NET Core endpoint
    Call<String> getData();

    @POST("api/Citizen/LoginEmail")
    Call<ResponseBody> loginUser(@Body LoginRequest request);

    @POST("api/Citizen/AddCitizen")
    Call<ResponseBody> signupUser(@Body SignupRequest request);

    @GET("api/Citizen/MapLocation")// Relative path of your ASP.NET Core endpoint
    Call<GetLocation> getLocation(
            @Query("longitude") double longitude,
            @Query("latitude") double latitude
    );

    @Multipart
    @POST("api/Citizen/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @POST("api/Citizen/SubmitHazard")
    Call<ResponseBody> submitHazard(@Body SubmitHazardRequest request);
    @GET("api/Citizen/ViewCitizen/{id}")
    Call<GetCitizen> getCitizenById(@Path("id") int id);

    @PATCH("api/Citizen/UpdateCitizen/{id}")
    Call<ResponseBody> updateCitizen(@Path("id") int id, @Body UpdateCitizen citizen);

}