package com.example.cityfixmopapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UploadResponse {
        private int Id;
    @SerializedName("urlImage")
    private String UrlImage;
    private boolean Success;

        // Getters
        public boolean isSuccess() {
            return Success;
        }

        public String getUrlImage() {
            return UrlImage;
        }

    public int getId() {
        return Id;
    }

    // Setters (optional if using Gson)
        public void setSuccess(boolean Success) {
            this.Success = Success;
        }

        public void setUrlImage(String UrlImage) {
            this.UrlImage = UrlImage;
        }

    public void setId(int id) {
        Id = id;
    }
}
