package org.buckybadger.g576final;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.Date;

public class CreateObstructionReport extends AppCompatActivity {

    //private Button viewMain;
    private Button submitReport;
    private JSONObject reportInfo;
    private static final String TAG = "UserSubmissionData:";
    private FusedLocationProviderClient fusedLocationClient;
    private Double lat;
    private Double lng;

    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }

    //Step 2 and 3--Process Data and input into DB
    public void processReportDetails() {
        String timestamp =
                new java.text.SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());
        final EditText addMsg = (EditText) findViewById(R.id.add_msg);
        String add_msg = addMsg.getText().toString();
        add_msg = "'" + add_msg + "'";


        final Spinner obstructionType = (Spinner) findViewById(R.id.SpinnerObstructionType);
        String obstruction_type = obstructionType.getSelectedItem().toString();
        obstruction_type = "'" + obstruction_type + "'";
        try {


        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_obstruction_report);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //Logic to handle location object
                            lat = location.getLatitude();
                            lng = location.getLongitude();


                            Log.d(TAG, "LastKnownUserLocation: " + lat + ", " + lng);
                            //Display user's last known location in the log files for review

                        } else {
                            //Set Lat and Long to default for Augusta, GA
                            lat = 33.466;
                            lng = -81.9666;

                        }
                    }
                });

        //add variable for Submit Button
        submitReport = (Button)findViewById(R.id.submit);

        submitReport.setOnClickListener(new View.OnClickListener() { //click listener
            @Override
            public void onClick(View view) {

                //Step 1 -- Collect all items from form submission, process into JSONArray
                processReportDetails();

                //Step 4 -- Reload viewMap.class
                Intent myIntent = new Intent(CreateObstructionReport.this, viewMap.class);
                startMyActivity(myIntent);
            }
        });

    }


}
