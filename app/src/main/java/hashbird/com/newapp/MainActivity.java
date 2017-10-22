package hashbird.com.newapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import hashbird.com.newapp.Helper.SQLiteHandler;

public class MainActivity extends AppCompatActivity {
ImageButton add,view;
TextView count;
SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
db = new SQLiteHandler(getApplicationContext());
    add = (ImageButton)findViewById(R.id.add);
    view = (ImageButton)findViewById(R.id.view);
    count = (TextView) findViewById(R.id.count);

        count.setText("TOTAL OFFLINE ENTRIES: "+db.getProfilesCount());

    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent myIntent = new Intent(MainActivity.this,
                    new_patient.class);
            startActivity(myIntent);
        }
    });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this,
                        ViewData.class);
                startActivity(myIntent);
            }
        });


    }

    @Override
    protected void onResume() {

        count.setText("TOTAL OFFLINE ENTRIES: "+db.getProfilesCount());
        super.onResume();
    }
}
