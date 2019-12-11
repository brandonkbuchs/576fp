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

import static org.buckybadger.g576final.viewMap.mMap;
import static org.buckybadger.g576final.viewMap.report_id;

public class resolveReport extends AppCompatActivity {

    private Button resolveReport;
    private Button returnToMap;

    public void startMyActivity(Intent intent) { startActivity(intent); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolve_report);

        //Retrieve variables from viewMap.java, STEP 1


        try {


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
            public void onClick(View view) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("tab_id", "2");
                data.put("report_id", report_id);
                AsyncHttpPost aSyncHttpPost = new AsyncHttpPost(data, mMap);
                aSyncHttpPost.execute("http://10.11.12.16:8080/Lab5_war_exploded/HttpServlet");

                Intent myIntent = new Intent(resolveReport.this, viewMap.class);
                startMyActivity(myIntent);
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
