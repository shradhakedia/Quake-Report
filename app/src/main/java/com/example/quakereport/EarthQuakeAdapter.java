package com.example.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthQuakeAdapter extends ArrayAdapter<EarthquakeData> {

    public EarthQuakeAdapter(@NonNull Context context, List<EarthquakeData> earthquake) {
        super(context, 0, earthquake);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

            // Get the {@link EarthQuakeData} object located at this position in the list
            EarthquakeData currentEarthquake = getItem(position);

            // Find the TextView in the list_item.xml layout with the ID magnitude
            TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);

            // Get the magnitude from the current EarthQuakeData object,
            double mag = currentEarthquake.getMagnitude();

            // Set the proper background color on the magnitude circle.
            // Fetch the background from the TextView, which is a GradientDrawable.
            GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
            // Get the appropriate background color based on the current earthquake magnitude
            int magnitudeColor = getMagnitudeColor(mag);

            // Set the color on the magnitude circle
            magnitudeCircle.setColor(magnitudeColor);


            // set this text on the magnitude TextView
            magnitude.setText(formatMagnitude(mag));

            // Get the location from the current EarthQuakeData object
            String location = currentEarthquake.getPlace();

            // Find the TextView in the list_item.xml layout with the ID primary location and offset location
            TextView primary = (TextView) listItemView.findViewById(R.id.primaryLocation);
            TextView offset = (TextView) listItemView.findViewById(R.id.offsetLocation);

            //split location accordingly for better UI experience and set this text on the primary and offset TextViews
            if(location.contains("of")) {
                String[] pairLoc = location.split("of ");
                offset.setText(String.format("%sof", pairLoc[0]));
                primary.setText(pairLoc[1]);
            }
            else {
                offset.setText(R.string.nearThe);
                primary.setText(location);
            }

            // Get the time from the current EarthQuakeData object and convert in readable format
            long time = currentEarthquake.getTime();
            Date dateObject = new Date(time);

            // Find the TextView in the list_item.xml layout with the ID date
            TextView date = (TextView) listItemView.findViewById(R.id.date);
            // set this text on the date TextView
            date.setText(formatDate(dateObject));

            // Find the TextView in the list_item.xml layout with the ID time
            TextView displayTime = (TextView) listItemView.findViewById(R.id.time);
            // set this text on the time TextView
            displayTime.setText(formatTime(dateObject));

            return listItemView;

    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the color for magnitude according to tha magnitude value in integer forma.
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        switch((int) magnitude) {
            case 0 :
            case 1 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            }
            case 2 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            }
            case 3 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            }
            case 4 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            }
            case 5 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            }
            case 6 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            }
            case 7 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            }
            case 8 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            }
            case 9 : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            }
            default : {
                magnitudeColorResourceId = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                break;
            }

        }

        return magnitudeColorResourceId;
    }

}
