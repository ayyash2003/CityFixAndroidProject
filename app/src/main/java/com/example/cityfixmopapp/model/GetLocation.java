package com.example.cityfixmopapp.model;

public class GetLocation {
    private String municipalityName ;
    private int municipalityId ;
    private int addressId;

    public GetLocation(String municipalityName,int municipalityId,int addressId ) {
        this.municipalityName = municipalityName;
        this.municipalityId=municipalityId ;
        this.addressId=addressId;
    }
    public String getName(){return municipalityName;}
    public int getMunicipalityId(){return municipalityId;}
    public int getAddressId(){return addressId;}




}
