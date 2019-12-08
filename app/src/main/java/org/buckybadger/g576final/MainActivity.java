package org.buckybadger.g576final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //variable declaration
    private Button createReport;
    private Button viewMap;

    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Locate the buttons from activity_main.xml
        createReport = (Button)findViewById(R.id.createReport);
        viewMap = (Button)findViewById(R.id.viewMap);

        //create an OnClickListener for the Create Report button
        createReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, createReport.class);
                startMyActivity(myIntent);
            }
        });

        //create onClickListener for the View Map Button
        viewMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, viewMap.class);
                startActivity(myIntent);
            }
        });
    }
}
