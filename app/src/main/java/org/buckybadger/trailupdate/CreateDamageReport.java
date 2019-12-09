package org.buckybadger.trailupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import org.buckybadger.g576final.R;
import org.json.JSONObject;


public class CreateDamageReport extends AppCompatActivity {

    private Button submitReport;
    private JSONObject reportInfo;
    private static final String TAG = "UserSubmissionData:";


    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }


    public void processReportDetails() {
        final EditText addMsg = (EditText) findViewById(R.id.add_msg);
        String add_msg = addMsg.getText().toString();

        final Spinner damageType = (Spinner) findViewById(R.id.SpinnerDamageType);
        String damage_type = damageType.getSelectedItem().toString();

        String [] reportInfo = {};
        Log.i(TAG, add_msg);
        Log.i(TAG, damage_type);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_damage_report);

        submitReport = (Button)findViewById(R.id.submit);



        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**TODO
                 * 1. Collect All Items From Form Submission ->JSONArray
                 * 2. Insert into DB
                 * 3. Offer success/failure option to user
                 */

                //Step 1 -- Collect all items from form submission, process into JSONArray
                //Convert Form submission information

                processReportDetails();

                //Step 4 -- Reload viewMap.class
                Intent myIntent = new Intent(CreateDamageReport.this, viewMap.class);
                startMyActivity(myIntent);
            }
        });
    }


}
