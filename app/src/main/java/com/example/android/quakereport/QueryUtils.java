package com.example.android.quakereport;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static ArrayList<Earthquake> extractEarthquakesFromUrl(String urlString){
        Log.i(LOG_TAG,"TEST: fetchcall");
        URL url = createUrl(urlString);


        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(makeHttpRequest(url));
            JSONArray feature_array = json.getJSONArray("features");
            for (int i = 0; i < feature_array.length(); i++) {
                JSONObject temp_object = feature_array.getJSONObject(i).getJSONObject("properties");
                //String mag = temp_object.getString("mag");
                String loc = temp_object.getString("place");
//                String date  = temp_object.getString("time");
//                Long date_long = Long.parseLong(date);
                double mag_double = temp_object.getDouble("mag");
                long date_long = temp_object.getLong("time");
                String earthquake_url = temp_object.getString("url");
                Date dateObject = new Date(date_long);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
                String formatted_date = dateFormatter.format(dateObject);
                Earthquake earthquake = new Earthquake(mag_double, loc, date_long, earthquake_url);
                earthquakes.add(earthquake);
            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (IOException e){
            Log.e(LOG_TAG, "Problem getting HTTP response", e);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }

    private static URL createUrl(String APIEndpt){
        URL url = null;
        try {
            url = new URL(APIEndpt);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null) return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(15000);

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

    private static String readFromInputStream(InputStream inputStream) throws IOException{
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