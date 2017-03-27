package ca.sfu.iat381.reachout_app.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;

/*
Show Event details with a main map fragment that shows the exact location of the event
 */
public class EventDetails extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap eventMap;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventMap);
        mapFragment.getMapAsync(this);


        //Get the instance of the event that was clicked
        event = (Event) getIntent().getSerializableExtra("Event_object");

        System.out.println(event.getLatitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Lat and Long of event
        double eventLatitude = event.getLatitude();
        double eventLongitude = event.getLongitude();

        eventMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng eventMarker = new LatLng(eventLatitude, eventLongitude);
        eventMap.addMarker(new MarkerOptions().position(eventMarker).title(event.getVenue()));
        eventMap.moveCamera(CameraUpdateFactory.newLatLng(eventMarker));

    }
}
