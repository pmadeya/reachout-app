package ca.sfu.iat381.reachout_app.model;


import java.io.Serializable;

public class Event implements Serializable {

    //Title of the event
    private String name;

    //City that the event will take place in
    private String location;
    private double latitude;
    private double longitude;

    //Starting date of the event
    private String time;

    private String venue;


    public Event (String name, String location, String time, String venue, double longitude, double latitude) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.venue = venue;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
