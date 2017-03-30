package ca.sfu.iat381.reachout_app.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import ca.sfu.iat381.reachout_app.model.Event;
import ca.sfu.iat381.reachout_app.model.EventData;

public class EventActivity extends AppCompatActivity {

    public RecyclerView eventRecyclerView;

    private EditText inputCity;

    private Button searchEventsBtn;

    private TextView eventListings;

    private EventAdapter mAdapter;

    //Events that are happening in the Future in Vancouver in all categories - unsorted
    //TODO: Change query to show events that we want to see
    //private static final String EVENTS_QUERY = "http://api.eventful.com/json/events/search?...&date=ThisWeek&location=Vancouver&category=music&app_key=LGZXJ2LkPvTZQghJ&sort_order=date";

    List<Event> eventResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventResults = new ArrayList<Event>();
        mAdapter = new EventAdapter(getBaseContext(), eventResults);

        eventListings = (TextView) findViewById(R.id.eventListings_textView);

        eventRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(layoutManager);


        eventResults = (List<Event>) getIntent().getSerializableExtra("event_list");

        for (Event event : eventResults) {
            System.out.println(event.getName());
        }

        //Return a recylerview of the objects and display in a list
        mAdapter = new EventAdapter(getBaseContext(), eventResults);
        eventRecyclerView.setAdapter(mAdapter);


        //Check connection of phone to internet
        //checkConnection();

        //Fetch the Events Data
//        searchEventsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FetchEventsAsyncTask task = new FetchEventsAsyncTask();
//                task.execute("http://api.eventful.com/json/events/search?...&location=" + inputCity.getEditableText().toString() + "&category=music&app_key=LGZXJ2LkPvTZQghJ&sort_order=popularity&sort_direction=descending");
//            }
//        });

        //eventRecyclerView.setAdapter(mAdapter);

    }// End onCreate




}
