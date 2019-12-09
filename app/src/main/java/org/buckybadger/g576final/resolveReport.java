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

        //Retrieve variables from viewMap.java, STEP 1
        Intent mIntent = getIntent();

        Bundle reportDetails = mIntent.getExtras();

        try {

            String reportType = reportDetails.getString("reportType");
            String reportDesc = reportDetails.getString("reportDesc");
            String reportID = reportDetails.getString("reportID");
            Double reportLat = reportDetails.getDouble("reportLat");
            Double reportLng = reportDetails.getDouble("reportLng");

            Log.v("Report Details", reportType + "\n" + reportDesc + "\n" + reportID + "\n"
                    + reportLat + "\n" + reportLng);
        } catch (Exception e) {
            Log.d("Bundle Error: ", e.toString());
        }






        /**TODO
         *
         *  1. !!Modify to Just pull the report that was clicked onMarkerClick
         *
         * 2. Add function to resolveButton to UPDATE
         *
         * 3. Return user to map after update, ensure no 'resolved' reports populate (done)
         * 6. Profit.
         */

        resolveReport = (Button)findViewById(R.id.resolve);

        resolveReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {//Step 2 of TODO
            }
        });

        returnToMap = (Button)findViewById(R.id.goback);

        returnToMap.setOnClickListener( new View.OnClickListener() { //Step 3 of the TODO
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(resolveReport.this, viewMap.class);
                startMyActivity(myIntent);
            }
        });



    }

}
