package com.example.android.quakereport;

public class Earthquake {
    private String magnitude;
    private String location;
    private String date;

    public Earthquake(String mag, String loc, String date){
        this.magnitude = mag;
        this.location = loc;
        this.date = date;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getDate(){
        return date;
    }
}
