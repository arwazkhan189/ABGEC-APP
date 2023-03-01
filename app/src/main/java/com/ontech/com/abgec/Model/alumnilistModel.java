package com.ontech.com.abgec.Model;

public class alumnilistModel {

    private String branch;
    private String city;
    private String country;
    private String designation;
    private String name;
    private String organisation;
    private String passoutYear;
    private String mobileNumber;
    private String state;
    private String email;
    private String pushkey;

    public alumnilistModel() {
    }

    public alumnilistModel(String branch, String city, String country, String designation, String name, String organisation, String passoutYear, String mobileNumber, String state, String email, String pushkey) {
        this.branch = branch;
        this.city = city;
        this.country = country;
        this.designation = designation;
        this.name = name;
        this.organisation = organisation;
        this.passoutYear = passoutYear;
        this.mobileNumber = mobileNumber;
        this.state = state;
        this.email = email;
        this.pushkey = pushkey;
    }

    public String getPushkey() {
        return pushkey;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }


    public String getBranch() {
        return branch;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getDesignation() {
        return designation;
    }

    public String getName() {
        return name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getPassoutYear() {
        return passoutYear;
    }

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }
}
