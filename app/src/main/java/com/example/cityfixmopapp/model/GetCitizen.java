package com.example.cityfixmopapp.model;

public class GetCitizen {
private String name ;
private String email ;
private String phoneNumber ;

    public GetCitizen(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }
}
