package ca.sfu.iat381.reachout_app.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;
import ca.sfu.iat381.reachout_app.model.EventData;
import ca.sfu.iat381.reachout_app.model.MyEventDatabase;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    MyEventDatabase db;

    private final String LOG_TAG = "Reachout-app";
    private List<Event> eventResults;
    private Location currentCoordinates;

    private ImageButton categoryViewBtn;
    private ImageButton mapViewBtn;


    private Button searchKeywordEvents;
    private Button favoriteEventsBtn;

    private TextView currentCitytxtView;

    private EditText eventKeywordSearch;

    //Google APIs
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
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

        //Instantiate a Database object
        db = new MyEventDatabase(this);

        //EditText
        eventKeywordSearch = (EditText) findViewById(R.id.eventKeyword_editText);

        //TextViews
        currentCitytxtView = (TextView) findViewById(R.id.currentCityTextView);

        //Buttons
        categoryViewBtn = (ImageButton) findViewById(R.id.categoryView_btn);
        mapViewBtn = (ImageButton) findViewById(R.id.mapView_btn);
        favoriteEventsBtn = (Button) findViewById(R.id.favoriteEventsBtn);
        searchKeywordEvents = (Button) findViewById(R.id.searchKeywordEvents);

        //Listeners
        categoryViewBtn.setOnClickListener(this);
        mapViewBtn.setOnClickListener(this);
        favoriteEventsBtn.setOnClickListener(this);
        searchKeywordEvents.setOnClickListener(this);

        checkConnection();

        //Build API client for location
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Display last location
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (lastLocation == null) {
//
//        }
//        else {
//            Toast.
//        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.categoryView_btn:

                //Navigate into activity_categories
                Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(i);

                break;

            case R.id.mapView_btn:

                //Fetch the event data from the background thread
                new FetchEventsAsyncTask().execute("http://api.eventful.com/json/events/search?...&where="+ currentCoordinates.getLatitude() + "," + currentCoordinates.getLongitude() +
                "&within=10&units=km&app_key=LGZXJ2LkPvTZQghJ&date=Future", "mapView");

                //Go to onPostExecute to start the intent
                break;

            case R.id.favoriteEventsBtn:
                //View favorite Events stored in Database
                Intent intent = new Intent(this, EventActivity.class);

                //put extra to differentiate from favorite events to all events
                intent.putExtra("Class", "MainActivityFavorites");
                startActivity(intent);

                break;

            case R.id.searchKeywordEvents:
                //Start searching the event Data for the keywords in the EditText in a background thread
                //FetchEventsAsyncTask fetchEvents = new FetchEventsAsyncTask();
                Log.e("LOCATION", "Latitude" + currentCoordinates.getLatitude());
                Log.e("LOCATION", "Longitude" + currentCoordinates.getLongitude());

                if (eventKeywordSearch.getText().toString().contains(" ")) {
                    String keyword = eventKeywordSearch.getText().toString().replaceAll("\\s+","%20");
                    System.out.println(eventKeywordSearch.getText().toString());

                    new FetchEventsAsyncTask().execute("http://api.eventful.com/json/events/search?...&keywords=" + keyword + "&where="+ currentCoordinates.getLatitude() + "," + currentCoordinates.getLongitude() +
                            "&within=100&units=km&app_key=LGZXJ2LkPvTZQghJ&date=2017040700-2017043000&sort_order=popularity&sort_direction=ascending", "searchKeyword");
                } else {
                    new FetchEventsAsyncTask().execute("http://api.eventful.com/json/events/search?...&keywords=" + eventKeywordSearch.getText().toString() + "&where=" + currentCoordinates.getLatitude() + "," + currentCoordinates.getLongitude() +
                            "&within=100&units=km&app_key=LGZXJ2LkPvTZQghJ&date=2017040700-2017043000&sort_order=popularity&sort_direction=ascending", "searchKeyword");
                }


            default:

                break;

        }
    }

    public void checkConnection() {
        ConnectivityManager connectMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //fetch data

            String networkType = networkInfo.getTypeName().toString();
            Toast.makeText(this, "Connected to " + networkType, Toast.LENGTH_LONG).show();
        } else {
            //display error
            Toast.makeText(this, "no network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Connect api client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); //Updates every 1 seconds

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

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //Capture the current lat and long
        currentCoordinates = location;


        //Toast.makeText(this, "Connected to onLocationChanged!", Toast.LENGTH_SHORT).show();
        //currentCitytxtView.setText(Double.toString(location.getLatitude()));

        //Find current city and display it below editText
        Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addressList.size() > 0) {
                //Toast.makeText(this, "Returns a list", Toast.LENGTH_SHORT).show();
                Address add = addressList.get(0);

                String locality = add.getLocality();
                //Toast.makeText(this, "Found " + locality, Toast.LENGTH_SHORT).show();

//            double lat = add.getLatitude();
//            double lng = add.getLongitude();
//            gotoLocation(lat, lng, 15);

                currentCitytxtView.setText(locality + ", " + add.getAdminArea() + ", " + add.getCountryName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class FetchEventsAsyncTask extends AsyncTask<String, Void, List<Event>> {
        String modes;

        @Override
        protected List<Event> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            publishProgress();

            modes = urls[1];
            eventResults = EventData.getEventInfo(urls[0]);
            return eventResults;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            eventKeywordSearch.getText().clear();

            if (modes.equals("mapView")) {
                Intent mapIntent = new Intent(MainActivity.this, MapView.class);
                System.out.println("Going into the map view intent results");

                //Pass the events for the markers on the map
                mapIntent.putExtra("event_map_markers", (Serializable) eventResults);
                mapIntent.putExtra("current_location_latitude", currentCoordinates.getLatitude());
                mapIntent.putExtra("current_location_longitude", currentCoordinates.getLongitude());
                startActivity(mapIntent);
            }
            else if (modes.equals("searchKeyword")) {
                //Go to the event activity and list events of that category
                Intent i = new Intent(MainActivity.this, EventActivity.class);
                i.putExtra("Class", "MainActivity");
                i.putExtra("event_list", (Serializable) eventResults);

                startActivity(i);

                System.out.println("We reach the post execute");
            }


        }
    }



}
