package org.buckybadger.g576final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Date;
import static org.buckybadger.g576final.viewMap.mMap;


public class CreateDamageReport extends AppCompatActivity {

    private Button submitReport;

    private static final String TAG = "UserSubmissionData:";
    private String latitude;
    private String longitude;


    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }

    //Step 2 and 3--Process Data and input into DB
    //Step 2 and 3--Process Data and input into DB
    public void processReportDetails() {
        //Set variables that are not dependent on the user input
        String timestamp =
                new java.text.SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());

        String reporter_id = "4"; //Once User Authentication Service Created, this changes
        String report_type = "obstruction";

        final EditText addMsg = (EditText) findViewById(R.id.add_msg);
        String add_msg = addMsg.getText().toString();

        final Spinner damageType = (Spinner) findViewById(R.id.SpinnerDamageType);
        String damage_type = damageType.getSelectedItem().toString();
        Log.i("damType", damage_type);

        try {
            HashMap<String, String> data = new HashMap<>();

            data.put("tab_id", "0");
            data.put("reporter_id", reporter_id);
            data.put("report_type", report_type);
            data.put("damage_type", damage_type);
            data.put("timestamp", timestamp);
            data.put("add_msg", add_msg);
            data.put("latitude", latitude);
            data.put("longitude", longitude);

            //check values in 'data'
            String dataString;
            dataString = data.toString();
            Log.v(TAG, "data.toString() is : " + dataString);

            //Execute AsyncHttpPost to INSERT INTO report
            AsyncHttpPost aSyncHttpPost = new AsyncHttpPost(data, mMap);
            aSyncHttpPost.execute("http://10.11.12.16:8080/Lab5_war_exploded/HttpServlet");

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_damage_report);
        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");


        //add variable for Submit Button
        submitReport = (Button)findViewById(R.id.submit);

        submitReport.setOnClickListener(new View.OnClickListener() { //click listener
            @Override
            public void onClick(View view) {

                //Step 1 -- Collect all items from form submission, process into JSONArray
                processReportDetails();

                //Step 4 -- Reload viewMap.class
                Intent myIntent = new Intent(CreateDamageReport.this, viewMap.class);
                startMyActivity(myIntent);
            }
        });

    }


}
