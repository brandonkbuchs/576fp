package org.buckybadger.trailupdate;

//import statements for android.os.
import android.os.Bundle;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import statements for com.google.android.gms.maps
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

//import statements for android.app

import android.util.Log;
import android.view.MenuItem;

import static android.content.ContentValues.TAG;


public class viewMap extends AppCompatActivity implements OnMapReadyCallback {

    //Variable Declaration
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.create_damage_report:
                return true;
            case R.id.create_obstruction_report:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

