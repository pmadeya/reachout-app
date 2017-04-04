package ca.sfu.iat381.reachout_app.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.DBConstants;
import ca.sfu.iat381.reachout_app.model.Event;
import ca.sfu.iat381.reachout_app.model.EventData;
import ca.sfu.iat381.reachout_app.model.EventDbHelper;
import ca.sfu.iat381.reachout_app.model.MyEventDatabase;

public class EventActivity extends AppCompatActivity {

    public RecyclerView eventRecyclerView;
    private EventAdapter mAdapter;


    private Event event;
    MyEventDatabase db;
    EventDbHelper helper;

    //Events that are happening in the Future in Vancouver in all categories - unsorted
    //TODO: Change query to show events that we want to see
    //private static final String EVENTS_QUERY = "http://api.eventful.com/json/events/search?...&date=ThisWeek&location=Vancouver&category=music&app_key=LGZXJ2LkPvTZQghJ&sort_order=date";

    List<Event> eventResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        db = new MyEventDatabase(this);
        helper = new EventDbHelper(this);




        eventResults = new ArrayList<Event>();

        eventRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(layoutManager);

        //Check where the intent came from (Show all events or favorite events)
        String activity = getIntent().getStringExtra("Class");

        if (activity.equals("MainActivityFavorites")) {
            //Get the resultant data
            Cursor cursor = db.getData();

            //Retrieve the information from the database
            int index1 = cursor.getColumnIndex(DBConstants.NAME);
            int index2 = cursor.getColumnIndex(DBConstants.LOCATION);
            int index3 = cursor.getColumnIndex(DBConstants.TIME);
            int index4 = cursor.getColumnIndex(DBConstants.VENUE);
            int index5 = cursor.getColumnIndex(DBConstants.LONGITUDE);
            int index6 = cursor.getColumnIndex(DBConstants.LATITUDE);
            int index7 = cursor.getColumnIndex(DBConstants.DESCRIPTION);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String eventName = cursor.getString(index1);
                String eventLocation = cursor.getString(index2);
                String eventTime = cursor.getString(index3);
                String eventVenue = cursor.getString(index4);
                String eventLongitude = cursor.getString(index5);
                String eventLatitude = cursor.getString(index6);
                String eventDescription = cursor.getString(index7);

                System.out.println(eventName);
                System.out.println(eventLocation);
                System.out.println(eventTime);
                System.out.println(eventVenue);
                System.out.println(eventLongitude);
                System.out.println(eventLatitude);
                System.out.println(eventDescription);

                event = new Event(eventName, eventLocation, eventTime, eventVenue, Double.parseDouble(eventLongitude),
                Double.parseDouble(eventLatitude), eventDescription);


                eventResults.add(event);
                cursor.moveToNext();
            }

            mAdapter = new EventAdapter(getBaseContext(), eventResults);
            eventRecyclerView.setAdapter(mAdapter);
        }
        else if (activity.equals("CategoriesActivity")) {

            //Retrieve all the events from the query
            eventResults = (List<Event>) getIntent().getSerializableExtra("event_list");

            //Return a recylerview of the objects and display in a list
            mAdapter = new EventAdapter(getBaseContext(), eventResults);
            eventRecyclerView.setAdapter(mAdapter);
        }




//        for (Event event : eventResults) {
//            System.out.println(event.getName());
//        }




    }




}
