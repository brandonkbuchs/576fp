package org.buckybadger.g576final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class createReport extends AppCompatActivity {

    private Button viewMain;

    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_report);

        viewMain = (Button)findViewById(R.id.viewMain);

        viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(createReport.this, MainActivity.class);
                startMyActivity(myIntent);
            }
        });
    }


}
