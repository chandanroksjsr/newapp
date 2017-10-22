package hashbird.com.newapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import hashbird.com.newapp.Helper.SQLiteHandler;
import hashbird.com.newapp.Helper.Security;

public class new_patient extends AppCompatActivity {
SQLiteHandler db;
Button offline,online;
EditText name,email,phone,dob;
Switch dia,bp,hr,ep,tb,di;
TextView enc;
Security security;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      security = new Security();
        db = new SQLiteHandler(getApplicationContext());

        offline = (Button)findViewById(R.id.offline);
online = (Button)findViewById(R.id.online);
name = (EditText) findViewById(R.id.name);
email = (EditText) findViewById(R.id.email);
phone = (EditText) findViewById(R.id.phone);
dob = (EditText) findViewById(R.id.dob);
enc = (TextView) findViewById(R.id.enct);
dia  =(Switch)findViewById(R.id.diab);
bp  =(Switch)findViewById(R.id.bp);
hr  =(Switch)findViewById(R.id.hr);
ep  =(Switch)findViewById(R.id.ep);
tb  =(Switch)findViewById(R.id.tb);
di  =(Switch)findViewById(R.id.di);
        final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();


offline.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
String nam = name.getText().toString();
String ema = email.getText().toString();
String pho = phone.getText().toString();
String dobo = dob.getText().toString();
        String bpi = null;
        String hri = null;
        String epi = null;
        String tbi = null;
        String dis = null;
        String diab = null;




        if(dia.isChecked()){
            diab = "YES";
        }
        else{
            diab = "NO";
        }

        if(bp.isChecked()){
             bpi = "YES";
        }
        else{
             bpi = "NO";
        }
        if(hr.isChecked()){
             hri = "YES";
        }
        else{
             hri = "NO";
        }
        if(ep.isChecked()){
             epi = "YES";
        }
        else{
             epi = "NO";
        }
        if(tb.isChecked()){
             tbi = "YES";
        }
        else{
             tbi = "NO";
        }

        if(di.isChecked()){
             dis = "YES";
        }
        else{
             dis = "NO";
        }


        try {
            long time1= System.nanoTime();
            String enc_n = Security.bytesToHex(security.encrypt(nam,imei));
            String enc_e = Security.bytesToHex(security.encrypt(ema,imei));
            String enc_p = Security.bytesToHex(security.encrypt(pho,imei));
            String enc_dob = Security.bytesToHex(security.encrypt(dobo,imei));
            String enc_dia = Security.bytesToHex(security.encrypt(diab,imei));
            String enc_bp = Security.bytesToHex(security.encrypt(bpi,imei));
            String enc_ep = Security.bytesToHex(security.encrypt(epi,imei));
            String enc_tb = Security.bytesToHex(security.encrypt(tbi,imei));
            String enc_di = Security.bytesToHex(security.encrypt(dis,imei));
            String enc_hr = Security.bytesToHex(security.encrypt(hri,imei));
            long time2= System.nanoTime();

            db.addUser(enc_n,enc_e,enc_p,enc_dob,enc_dia,enc_bp,enc_hr,enc_ep,enc_tb,enc_di);


            enc.setText("Encryption time: "+String.valueOf((time2-time1)/1000)+" Micro Seconds");

            Toast.makeText(getApplicationContext(),"STORED OFFLINE",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }



     //   storeoffline(name,email,phone,);
    }
});





//;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
