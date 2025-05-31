package com.example.cityfixmopapp.model;

public class Location {
    private double latitude ;
    private double longitude  ;
    private Boolean valid;
    private int locationId ;
    private int addressId ;
    private int municapilityId ;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        valid =false ;
        locationId =-1 ;
        addressId=-1;
        municapilityId=-1 ;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getMunicapilityId() {
        return municapilityId;
    }

    public void setMunicapilityId(int municapilityId) {
        this.municapilityId = municapilityId;
    }
}
