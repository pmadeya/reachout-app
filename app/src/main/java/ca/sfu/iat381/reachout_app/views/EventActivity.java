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
    //private Vibrator myVibrator;

    private EditText inputCity;
    private Toast mToast;

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

        //Instantiate a vibrator object
        //myVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        //Show the intro activity (tutorial) only once
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(EventActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


        eventResults = new ArrayList<Event>();
        mAdapter = new EventAdapter(getBaseContext(), eventResults);

        inputCity = (EditText) findViewById(R.id.cityName_editText);
        searchEventsBtn = (Button) findViewById(R.id.searchButton);
        eventListings = (TextView) findViewById(R.id.eventListings_textView);

        eventRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setAdapter(mAdapter);


        //Check connection of phone to internet
        checkConnection();

        //Fetch the Events Data
        searchEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FetchEventsAsyncTask task = new FetchEventsAsyncTask();
                task.execute("http://api.eventful.com/json/events/search?...&location=" + inputCity.getEditableText().toString() + "&category=music&app_key=LGZXJ2LkPvTZQghJ&sort_order=date&date=2017031800-2017032000&sort_direction=descending");
            }
        });

        //eventRecyclerView.setAdapter(mAdapter);

    }// End onCreate

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

            //After finding the events, the phone vibrates
//            if (myVibrator.hasVibrator()) {
//                myVibrator.vibrate(1000);
//            }

        }
    }
}
