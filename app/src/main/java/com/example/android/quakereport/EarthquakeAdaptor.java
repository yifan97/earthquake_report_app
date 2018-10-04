package com.example.android.quakereport;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class EarthquakeAdaptor extends ArrayAdapter<Earthquake> {
    private static final String LOG_TAG = EarthquakeAdaptor.class.getSimpleName();

    public EarthquakeAdaptor(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentearthquake = getItem(position);

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        magnitudeView.setText(currentearthquake.getMagnitude());

        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);

        locationTextView.setText(currentearthquake.getLocation());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);

        dateTextView.setText(currentearthquake.getDate());

        return listItemView;
    }
}
