/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        EarthquakeSyncTask task = new EarthquakeSyncTask();
        task.execute();

    }

    private void lauchEarthquakeAdaptor(ArrayList<Earthquake> earthquakeList){

        final EarthquakeAdaptor earthquakeAdapter = new EarthquakeAdaptor(this, earthquakeList);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        //earthquakeListView.setAdapter(earthquakeAdapter);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = earthquakeAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    class EarthquakeSyncTask extends AsyncTask<URL, Void, ArrayList<Earthquake>> {


        @Override
        protected ArrayList<Earthquake> doInBackground(URL... urls) {
            ArrayList<Earthquake> earthquakes;
            URL url = createUrl("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10");
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            earthquakes = QueryUtils.extractEarthquakes(jsonResponse);

            return earthquakes;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquake_list) {
            if (earthquake_list == null) {
                return;
            }

            lauchEarthquakeAdaptor(earthquake_list);

        }

        private URL createUrl(String APIEndpt){
            URL url = null;
            try {
                url = new URL(APIEndpt);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if(url == null) return jsonResponse;
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromInputStream(inputStream);
                }else{
                    Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
                }
            }catch (IOException e){
                Log.e(LOG_TAG, "Error in I/O",  e);
            }finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromInputStream(InputStream inputStream) throws IOException{
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
                reader.close();
            }
            return output.toString();
        }
    }
}
