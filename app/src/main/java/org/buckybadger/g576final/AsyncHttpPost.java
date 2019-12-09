package org.buckybadger.g576final;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AsyncHttpPost extends AsyncTask<String, Void, JSONArray> {
    private HashMap<String, String> mData;// post data
    private GoogleMap mMap;


    public AsyncHttpPost(HashMap<String, String> data, GoogleMap map) {
        mData = data;
        mMap = map;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray arr = new JSONArray();
        HttpClient client = new DefaultHttpClient();

        try {
            HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL

            // set up post data
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));

            HttpResponse response = client.execute(post);

            byte[] result = EntityUtils.toByteArray(response.getEntity());
            String str = new String(result, "UTF-8");
            arr = new JSONArray(str);
        }
        catch (UnsupportedEncodingException e) {
            android.util.Log.v("UEE Error: ", e.toString());
        }
        catch (Exception e) {
            android.util.Log.v("Regular Exception: ", e.toString());
        }
        return arr;
    }

    @Override
    protected void onPostExecute(JSONArray Result) {
        if (mData.get("tab_id").equalsIgnoreCase("1")) {
            onQueryReportExecute(Result);
        }
    }

    private void onQueryReportExecute(JSONArray Result) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng point;

        for (int i = 0 ; i < Result.length(); i++) { //iterate through all the DB entries
            try {

                JSONObject report = Result.getJSONObject(i);
                Double lng = Double.parseDouble(report.getString("longitude"));
                Double lat = Double.parseDouble(report.getString("latitude"));
                //We need to pass this information to resolveReport for resolution
                String id = report.getString("report_id");
                point = new LatLng(lat, lng);
                builder.include(point);

                String reportType = report.getString("report_type");

                switch(reportType) {
                    case "damage": //For damage reports
                        viewMap.addMarkers(reportType, report.getString("damage_type"), lat, lng, id);
                        break;
                    case "obstruction": //For obstruction reports
                        viewMap.addMarkers(reportType, report.getString("obstruction_type"), lat, lng, id);
                        break;
                    default: //For an odd report
                        viewMap.addMarkers(reportType, "NFI", lat, lng, id);
                        break;
                }

            } catch (JSONException e) {
                Log.v("INFO", e.toString());
            }
        }

        if (Result.length() > 0) {
            LatLngBounds mapBounds = builder.build();

            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(mapBounds, padding);

            mMap.moveCamera(cu);
        }


    }
}
