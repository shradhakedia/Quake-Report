package com.example.quakereport;

import android.net.UrlQuerySanitizer;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an {@link List<EarthquakeData>} object to represent a single earthquake.
     */
    public static List<EarthquakeData> fetchEarthquakeData(String requestUrl) {
        //Create Url Object
        URL url = createUrl(requestUrl);

        //make an http request with that url and receive the JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create an {@link List<EarthquakeData>} object
        List<EarthquakeData> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds */);
            urlConnection.setConnectTimeout(15000 /*milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        if(inputStream != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
        }
        return sb.toString();
    }


    /**
     * Return a list of {@link EarthquakeData} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<EarthquakeData> extractEarthquakes(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty List where we can start adding earthquakes
        List<EarthquakeData> earthquakes = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray features = root.getJSONArray("features");

            // For each earthquake in the earthquakeArray, create an {@link EarthquakeData} object
            for(int i = 0; i < features.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject feature = features.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = feature.getJSONObject("properties");

                // Extract the value for the key called "mag"
                double mag = properties.getDouble("mag");

                // Extract the value for the key called "place"
                String place = properties.getString("place");

                // Extract the value for the key called "time"
                long time = properties.getLong("time");

                // Extract the value for the key called "url"
                String url = properties.getString("url");

                // Create a new {@link EarthquakeData} object with the magnitude, location, time,
                // and url from the JSON response and Add the new {@link EarthquakeData} to the list of earthquakes.
                earthquakes.add(new EarthquakeData(mag, place, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}