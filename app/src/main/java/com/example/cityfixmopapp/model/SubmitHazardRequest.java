package com.example.cityfixmopapp.model;

public class SubmitHazardRequest {
    private String Title ;
    private String Urlimage ;
    private int Category ;
    private String Description ;


    private int AddressID ;
    private int MunicipalityID ;
    private int CitizenID ;

    public SubmitHazardRequest(String title, String urlimage, int category,
                               String description, int addressID, int municipalityID,
                               int citizenID) {
        Title = title;
        Urlimage = urlimage;
        Category = category;
        Description = description;
        CitizenID = citizenID;
        MunicipalityID = municipalityID;
        AddressID = addressID;
    }
}
