package com.police.demonstration.manage_demonstration.notification.rest_api;

public class NotificationRequest {
    private String caltime;
    private String time;
    private String location;
    private String distance;
    private String region;
    private String wind;
    private String stdnoise;
    private String noise;
    private String name;
    private String equinoise;
    private String maxnoise;

    public NotificationRequest(String name, String equinoise, String maxnoise) {
        this.name = name;
        this.equinoise = equinoise;
        this.maxnoise = maxnoise;
    }

    public NotificationRequest(String caltime, String time, String location,
                        String distance, String region, String wind,
                        String stdnoise, String noise, String name) {
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
        this.name = name;
    }

    public NotificationRequest(String caltime, String time, String location,
                        String distance, String region, String wind,
                        String stdnoise, String noise) {
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
    }
}