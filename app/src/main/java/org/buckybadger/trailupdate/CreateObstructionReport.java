package org.buckybadger.trailupdate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateObstructionReport extends AppCompatActivity {

    private Button viewMain;

    private void startMyActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_damage_report);

        viewMain = (Button)findViewById(R.id.submit);

        viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CreateObstructionReport.this, MainActivity.class);
                startMyActivity(myIntent);
            }
        });
    }


}
