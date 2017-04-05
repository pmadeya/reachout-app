package ca.sfu.iat381.reachout_app.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.List;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;

public class MapView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    List<Event> returnedEvents;

    Double currentLocationLatitude;
    Double currentLocationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        //Get current lat and long
        Intent location = getIntent();
        currentLocationLatitude = location.getDoubleExtra("current_location_latitude", 0);
        currentLocationLongitude = location.getDoubleExtra("current_location_longitude", 0);


        //Get instance of all events in the area
        returnedEvents = (List<Event>) getIntent().getSerializableExtra("event_map_markers");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng currentArea = new LatLng(currentLocationLatitude, currentLocationLongitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentArea, 10);
        map.animateCamera(update);

        for (final Event event : returnedEvents) {
            System.out.println(event.getLatitude() + event.getLongitude());

            //Latitude - longitude of each event
            LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());

            final Marker eventMarker = map.addMarker(new MarkerOptions().position(eventLocation).title(event.getName()));
            eventMarker.setTag(event);
            System.out.println(eventMarker.toString());

            //Click on info window to get to the details of the event
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent i = new Intent(MapView.this, EventDetails.class);
                    i.putExtra("Class", "MapView");
                    i.putExtra("single_mapped_event", (Serializable) marker.getTag());

                    startActivity(i);

                    //Toast.makeText(getActivity(), "Infowindow clicked", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}
