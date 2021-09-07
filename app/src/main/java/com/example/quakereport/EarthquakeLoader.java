package com.example.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;
import java.util.logging.Handler;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeData>>  {

    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    public String url;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST: onStartLoading()");
        forceLoad();
    }


    /**
     * This is on a background thread.
     */
    @Override
    public List<EarthquakeData> loadInBackground() {
        Log.i(LOG_TAG,"TEST: Working on a background thread");
        if(url == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<EarthquakeData> result = QueryUtils.fetchEarthquakeData(url);
        return result;
    }
}
