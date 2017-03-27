package ca.sfu.iat381.reachout_app.model;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Pulls the information from the eventful API and stores it into an Event
 * object
 */

public final class EventData {





    private EventData() {

    }


    public static ArrayList<Event> getEventInfo(String url) {

        String JSONEvents = null;

        //Create the URL object
        URL mURL = makeURL(url);

        //Perform an HTTP request to the eventful API and get the entire query as a string
        try {
            JSONEvents = makeHttpRequest(mURL);
        } catch (IOException e) {
            Log.e("EVENT_DATA", "Can't make HTTP request", e);
        }


        ArrayList<Event> events = getEventFromJSON(JSONEvents);


        return events;

    }

    private static ArrayList<Event> getEventFromJSON(String jsonEvents) {
        ArrayList<Event> events = new ArrayList<>();

        //Check for an empty String
        if (TextUtils.isEmpty(jsonEvents)) {
            return null;
        }

        //Parse the JSON string
        try {

            //Get the root of the entire JSON returned query
            JSONObject rootObject = new JSONObject(jsonEvents);

            JSONObject allEvents = rootObject.getJSONObject("events");

            JSONArray eventJSONObjects = allEvents.getJSONArray("event");

            for (int i = 0; i < eventJSONObjects.length(); i++) {

                JSONObject currentEvent = eventJSONObjects.getJSONObject(i);

                String title = currentEvent.getString("title");
                String venue = currentEvent.getString("venue_name");
                String location = currentEvent.getString("city_name");
                String time = currentEvent.getString("start_time");
                double longitude = currentEvent.getDouble("longitude");
                double latitude = currentEvent.getDouble("latitude");

                Event event = new Event(title, location, time, venue, longitude, latitude);

                events.add(event);
            }

        }catch(JSONException e) {
            Log.e("EVENT_DATA", "Problem parsing the event JSON", e);
        }

        return events;
    }

    private static URL makeURL(String url) {
        URL query = null;
        try {
            query = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("EVENT_DATA_BAD_URL", "URL cannot be resolved");
        }

        return query;
    }

    private static String makeHttpRequest(URL queryURL) throws IOException {
        String eventListResponse = "";

        //Check if url is valid
        if (queryURL == null) {
            return eventListResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) queryURL.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Check for successful get request to the URL
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();

                //Parse the stream and return a complete JSON string with event results
                eventListResponse = readInputStream(inputStream);
            }
            else {
                Log.e("EVENT_DATA", "Error with response");
            }

        } catch(IOException e) {
            Log.e("EVENT_DATA_BAD_URL", "JSON was not able to be parsed successfully", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return eventListResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }
}
