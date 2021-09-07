package com.example.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeData>> {

    private static final String EA_LOG_TAG = EarthquakeActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private EarthQuakeAdapter adapter;
    private ListView earthquakeListView;
    private TextView emptyTextView;
    private ProgressBar loadingSpinner;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.total_list);

        // Find a reference to the {@link TextView} in the layout
        emptyTextView = (TextView) findViewById(R.id.empty_text);
        earthquakeListView.setEmptyView(emptyTextView);

        // Find a reference to the {@link ProgressBar} in the layout
        loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new adapter that takes an empty list of earthquakes as input
        adapter = new EarthQuakeAdapter(this, new ArrayList<EarthquakeData>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthquakeData currentEarthQuake = adapter.getItem(i);
                Uri earthquakeUri = Uri.parse(currentEarthQuake.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                // Find an activity to hand the intent and start that activity.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d("ImplicitIntents", "Can't handle this intent!");
                }
            }
        });
        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        Log.i(EA_LOG_TAG,"TEST: LoaderManager().initLoader() called");


    }

    @NonNull
    @Override
    public Loader<List<EarthquakeData>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(EA_LOG_TAG,"TEST: onCreateLoader() is called");
        // Create a new loader for the given URL
        return new EarthquakeLoader(EarthquakeActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthquakeData>> loader, List<EarthquakeData> result) {
        Log.i(EA_LOG_TAG,"TEST: onLoadFinished() is called");
        loadingSpinner.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // Set empty state text to display "No earthquakes found."
        emptyTextView.setText(R.string.noEarthquakeData);

        //check internet connection
        if(result == null || !isNetworkAvailable(EarthquakeActivity.this)) {
            emptyTextView.setText(R.string.noInternetConnection);
        }

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (result != null && !result.isEmpty()) {
            adapter.addAll(result);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthquakeData>> loader) {
        Log.i(EA_LOG_TAG,"TEST: onLoadReset() is called");
        adapter.clear();
    }

    private static boolean isNetworkAvailable(Context context) {
        //checking internet connection of the user
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}