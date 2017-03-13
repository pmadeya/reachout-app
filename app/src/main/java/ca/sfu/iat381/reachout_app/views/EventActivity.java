package ca.sfu.iat381.reachout_app.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
    private static final String EVENTS_QUERY = "http://api.eventful.com/json/events/search?...&location=Barcelona&category=sports&date=Future&app_key=LGZXJ2LkPvTZQghJ";

    List<Event> eventResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventResults = new ArrayList<Event>();
        mAdapter = new EventAdapter(getBaseContext(), eventResults);

        inputCity = (EditText) findViewById(R.id.cityName_editText);
        searchEventsBtn = (Button) findViewById(R.id.searchButton);
        eventListings = (TextView) findViewById(R.id.eventListings_textView);



        eventRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setAdapter(mAdapter);


//
//        eventResults.add(new Event("Bruno Mars", "Vancouver", "7 pm", "Rogers Arena"));
//        eventResults.add(new Event("Pink Floyd", "Boston", "5 pm", "Pepsi Centre"));
//        eventResults.add(new Event("Ed Sheeran", "Raleigh", "8 pm", "Safeco Field"));
//        eventResults.add(new Event("John Lennon", "Seattle", "7 pm", "Radio City Music Hall"));








        //Check connection of phone to internet
        checkConnection();

        //Fetch the Events Data
        searchEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FetchEventsAsyncTask task = new FetchEventsAsyncTask();
                task.execute(EVENTS_QUERY);
            }
        });

        //eventRecyclerView.setAdapter(mAdapter);



    }

    public void checkConnection(){
        ConnectivityManager connectMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //fetch data

            String networkType = networkInfo.getTypeName().toString();
            Toast.makeText(this, "connected to " + networkType, Toast.LENGTH_LONG).show();
        }
        else {
            //display error
            Toast.makeText(this, "no network connection", Toast.LENGTH_LONG).show();
        }
    }

    private class FetchEventsAsyncTask extends AsyncTask<String, Void, List<Event>> {
        @Override
        protected List<Event> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            eventResults = EventData.getEventInfo(urls[0]);
            return eventResults;
        }

        @Override
        protected void onPostExecute(List<Event> events) {

            //Return a recylerview of the objects and display in a list
            mAdapter = new EventAdapter(getBaseContext(), eventResults);
            eventRecyclerView.setAdapter(mAdapter);

        }
    }
}
