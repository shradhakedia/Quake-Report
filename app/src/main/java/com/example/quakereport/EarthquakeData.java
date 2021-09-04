package com.example.quakereport;

public class EarthquakeData {
    private double magnitude;
    private String place;
    private String url;
    private long time;

    public EarthquakeData(double magnitude, String place, long time, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getPlace(){
        return place;
    }

    public String getUrl(){
        return url;
    }

    public long getTime(){
        return time;
    }
}
