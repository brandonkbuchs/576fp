package org.buckybadger.trailupdate;

//Import statements for system utilities.
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



//import statements for map fragment to work
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import com.google.android.gms.tasks.OnSuccessListener;

//import statements for menu to work
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.buckybadger.g576final.R;

import static android.content.ContentValues.TAG;


public class viewMap extends AppCompatActivity implements OnMapReadyCallback {

    //Variable Declaration
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    //Method to open a new view
    public void startMyActivity(Intent intent) { startActivity(intent); }

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
}

