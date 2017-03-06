package ca.sfu.iat381.reachout_app.model;


public class Event {

    //Title of the event
    private String name;

    //City that the event will take place in
    private String location;

    //Starting date of the event
    private String time;

    private String venue;


    public Event (String name, String location, String time, String venue) {
        this.name = name;
        this.location = location;
        this.time = time;
        this.venue = venue;
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

    @Override
    public String toString() {
        return "Name: " + name + "\n" + "Venue: " + venue + "\n";
    }
}
