package org.buckybadger.g576final;

//Import statements for system utilities.

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.util.HashMap;

import static android.content.ContentValues.TAG;

//import statements for map fragment to work
//import statements for menu to work


public class viewMap extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener {

    //Variable Declaration
    public static GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker myMarker;

    //Variables obtained from DB query in aSyncHttpPost
    public static String report_id;


    //Method to open a new view
    public void startMyActivity(Intent intent) { startActivity(intent); }
    public void resolveActivity(Intent intent, Bundle report_id) { startActivity(intent, report_id); }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Create the view for the screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //Logic to handle location object
                            Double lat = location.getLatitude();
                            Double lng = location.getLongitude();
                            LatLng latLng = new LatLng(lat, lng);

                            Log.d(TAG, "LastKnownUserLocation: " + latLng); //Display user's last known location in the log files for review
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        } else {
                            //Set Lat and Long to default for Augusta, GA
                            Double lat = 33.466;
                            Double lng = -81.9666;

                            LatLng latLng = new LatLng(lat, lng);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        }
                    }
                });
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
                startMyActivity(damageIntent);
                return true;
            case R.id.create_obstruction_report:
                Intent obstructionIntent = new Intent(viewMap.this, CreateObstructionReport.class);
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
        //get the current report id

        report_id = marker.getSnippet();


        //Create new intent (a new screen)
        Intent myIntent = new Intent(viewMap.this, resolveReport.class);

        startMyActivity(myIntent);
        return false;
    }
}

