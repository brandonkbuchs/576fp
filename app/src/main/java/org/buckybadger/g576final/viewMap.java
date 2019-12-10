package org.buckybadger.g576final;

//Import statements for system utilities.

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import java.sql.Connection;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

//import statements for map fragment to work
//import statements for menu to work


public class viewMap extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener,
                                                GoogleApiClient.ConnectionCallbacks,
                                                GoogleApiClient.OnConnectionFailedListener,
                                                LocationListener {
    //Variable Declaration
    public static GoogleMap mMap;
    public static String report_id;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = viewMap.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private String latitude;
    private String longitude;


    //Method to open a new view
    public void startMyActivity(Intent intent) { startActivity(intent); }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
        Log.i(TAG, "Location Services connected.");

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);
        LatLng latlng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                //Start an activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        onMapReady(mMap);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) { //Create the view for the screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Adding connection capabilities to pull locations of the user
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
    }


    @Override
    public void onMapReady(GoogleMap map) { //Instantiate the Map.
        mMap = map;
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("tab_id", "1");
        AsyncHttpPost aSyncHttpPost = new AsyncHttpPost(data, mMap);
        aSyncHttpPost.execute("http://10.11.12.15:8080/Lab5_war_exploded/HttpServlet");



        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Display menu in action bar
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Select Menu Items
        switch(item.getItemId()) {
            case R.id.create_damage_report:
                Intent damageIntent = new Intent(viewMap.this, CreateDamageReport.class);
                damageIntent.putExtra("latitude", latitude);
                damageIntent.putExtra("longitude", longitude);
                startMyActivity(damageIntent);
                return true;
            case R.id.create_obstruction_report:
                Intent obstructionIntent = new Intent(viewMap.this, CreateObstructionReport.class);
                obstructionIntent.putExtra("latitude", latitude);
                obstructionIntent.putExtra("longitude", longitude);
                startMyActivity(obstructionIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void addMarkers(String title, String desc, Double lat, Double lng, String id) { //Add Markers to Map


        LatLng point = new LatLng(lat, lng);


        switch(title) {
            case "damage":
                mMap.addMarker(new MarkerOptions().position(point)
                    .title(title + ": " + desc)
                    .snippet(id)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hazard)));

                break;
            case "obstruction":
                mMap.addMarker(new MarkerOptions().position(point)
                    .title(title + ": " + desc)
                    .snippet(id)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.obstacle)));
                break;
            default:
                mMap.addMarker(new MarkerOptions().position(point)
                    .title(title + ": " + desc)
                    .snippet(id)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.damage)));
                break;
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        report_id = marker.getSnippet();

        //Create new intent (a new screen)
        Intent myIntent = new Intent(viewMap.this, resolveReport.class);
        myIntent.putExtra("report_id", report_id);
        startMyActivity(myIntent);
        return false;
    }
}

