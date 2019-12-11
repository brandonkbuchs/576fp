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
import org.json.JSONObject;
import java.util.Date;
import static org.buckybadger.g576final.viewMap.mMap;



public class CreateObstructionReport extends AppCompatActivity {

    private JSONObject reportInfo;
    private static final String TAG = "UserSubmissionData:";
    private static String latitude;
    private static String longitude;



    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }

    //Step 2 and 3--Process Data and input into DB
    public void processReportDetails() {
        //Set variables that are not dependent on the user input
        String timestamp =
                new java.text.SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());

        String reporter_id = "4"; //Once User Authentication Service Created, this changes
        String report_type = "obstruction";

        final EditText addMsg = (EditText) findViewById(R.id.add_msg);
        String add_msg = addMsg.getText().toString();

        final Spinner obstructionType = (Spinner) findViewById(R.id.SpinnerObstructionType);
        String obstruction_type = obstructionType.getSelectedItem().toString();
        Log.i("obsType", obstruction_type);

        try {
            HashMap<String, String> data = new HashMap<>();

            data.put("tab_id", "0");
            data.put("reporter_id", reporter_id);
            data.put("report_type", report_type);
            data.put("obstruction_type", obstruction_type);
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
            aSyncHttpPost.execute("http://10.11.12.17:8080/Lab5_war_exploded/HttpServlet");

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_obstruction_report);
        Button submitReport;
        Button viewMain;
        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");
        Log.i("latitude", latitude);
        Log.i("longitude", longitude);



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
