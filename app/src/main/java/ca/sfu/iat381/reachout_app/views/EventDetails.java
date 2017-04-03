package ca.sfu.iat381.reachout_app.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;

/*
Show Event details with a main map fragment that shows the exact location of the event
 */
public class EventDetails extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap eventMap;
    Event event;
    Event map_view_event;

    private TextView eventName;
    private TextView eventDescription;
    private TextView eventMonth;
    private TextView eventDay;

    private Calendar calendar;
    private Date event_date;

    WebView eventPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //Instantiate textviews
        eventName = (TextView) findViewById(R.id.eventName);
        eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventMonth = (TextView) findViewById(R.id.eventMonth);
        eventDay = (TextView) findViewById(R.id.eventDateNumber);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventMap);
        mapFragment.getMapAsync(this);


        //Get the instance of the event that was clicked in the map view!
        String activity = getIntent().getStringExtra("Class");


        if (activity.equals("MapView")) {
            event = (Event) getIntent().getSerializableExtra("single_mapped_event");
        }
        else if (activity.equals("EventAdapter")) {
            //Get the instance of the event that was clicked (from category view)
            event = (Event) getIntent().getSerializableExtra("Event_object");

        }


        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());

        //Create a date object for the start time
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            event_date = parser.parse(event.getTime());
            calendar = Calendar.getInstance();
            calendar.setTime(event_date);
            System.out.println(calendar.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        eventMonth.setText(new SimpleDateFormat("MMM").format(calendar.getTime()));
        eventDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));


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
