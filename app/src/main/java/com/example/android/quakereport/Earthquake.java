package com.example.android.quakereport;

public class Earthquake {
    private double magnitude;
    private String location;
    private long time;
    private String url;

    public Earthquake(double mag, String loc, long time, String url){
        this.magnitude = mag;
        this.location = loc;
        this.time = time;
        this.url = url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getTime(){ return time; }

    public String getUrl(){ return url; }
}
