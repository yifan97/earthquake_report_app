package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String murl;

    public EarthquakeLoader(Context context, String url){
        super(context);
        murl = url;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG,"TEST: loadinback");
        if(murl == null) return null;
        return QueryUtils.extractEarthquakesFromUrl(murl);
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST: on startcall");
        forceLoad();
    }
}