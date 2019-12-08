package org.buckybadger.g576final;

//Import statements for system utilities.

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class resolveReport extends AppCompatActivity {

    private Button resolveReport;
    private Button returnToMap;

    public void startMyActivity(Intent intent) { startActivity(intent); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolve_report);

        /**TODO
         * 1. Query All Reports in DB
         * 2. Add Radio Buttons next to each id
         * 3. Build function to run query on the selected radio button
         * 3a. Add check to make sure radio button is selected if resolve clicked
         * 4. Send UPDATE report to database
         * 5. Return user to map after update, ensure no 'resolved' reports populate
         * 6. Profit.
         */

        resolveReport = (Button)findViewById(R.id.resolve);

        resolveReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {//TODO CODE HERE
            }
        });

        returnToMap = (Button)findViewById(R.id.goback);

        returnToMap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(resolveReport.this, viewMap.class);
                startMyActivity(myIntent);
            }
        });



    }

}
