package hashbird.com.newapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hashbird.com.newapp.Helper.AppConfig;
import hashbird.com.newapp.Helper.AppController;
import hashbird.com.newapp.Helper.DataModel;
import hashbird.com.newapp.Helper.RecycleAdapter;
import hashbird.com.newapp.Helper.SQLiteHandler;
import hashbird.com.newapp.Helper.Security;

import static android.widget.Toast.LENGTH_LONG;

public class ViewData extends AppCompatActivity {
SQLiteHandler db;
    RecyclerView recyclerView;
    RecycleAdapter recycler;
    List<DataModel> datamodel;
    SearchView searchView = null;
    ProgressDialog pDialog;
    Security security;
    private static final String TAG = ViewData.class.getSimpleName();
    ImageButton ime,key,rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        db = new SQLiteHandler(getApplicationContext());
        datamodel =new ArrayList<DataModel>();
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        ime = (ImageButton) findViewById(R.id.imei);
        key = (ImageButton) findViewById(R.id.key);
        rand = (ImageButton) findViewById(R.id.random);
        security = new Security();




        ime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync_data();

            }
        });

        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync_datap();

            }
        });

        rand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync_datar();

            }
        });






        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);

view_data();




    }

void view_data(){

    datamodel=  db.getdata();
    recycler =new RecycleAdapter(datamodel);


    Log.i("All Data",""+datamodel);
    RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(reLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(recycler);

}

    @Override public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.view_data_menu, menu);

        // Get Search item from action bar and Get Search service
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchManager searchManager =
//                (SearchManager) ViewData.this.getSystemService(Context.SEARCH_SERVICE);
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(
//                    searchManager.getSearchableInfo(ViewData.this.getComponentName()));
//            searchView.setIconified(true);
//        }

       // MenuItem enai = menu.findItem(R.id.action_add_note);


MenuItem clear = menu.findItem(R.id.clear);
clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        db.deleteUsers();

        view_data();
        Toast.makeText(getApplicationContext(),"DATA CLEARED SUCCESSFULLY", LENGTH_LONG).show();

        return false;
    }
});

MenuItem sync = menu.findItem(R.id.sync);
sync.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        sync_data();

        return false;
    }

});
        MenuItem add = menu.findItem(R.id.add);
        add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent = new Intent(ViewData.this,new_patient.class);

                startActivity(intent);
                finish();

                return false;
            }

        });
        return true;
    }




    void sync_data(){

        final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getDeviceId();

        String tag_string_req = "req_sync";

        pDialog.setMessage("Syncing "+db.getProfilesCount()+" Data TO SERVER");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SYNC, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
               /// Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session


                        // Now store the user in SQLite
                        String adt = jObj.getString("Android_Decryption_Time");
                        String aet = jObj.getString("Android_Encryption_Time");
                        String sdt = jObj.getString("Server_Decryption_Time");
                        String dc = jObj.getString("Data_Count");
                        String sql = jObj.getString("SQL_Execution_Time");
                        String error_msg = jObj.getString("error_msg");

//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
//                        String created_at = user.getString("created_at");
//                        String updated_at = user.getString("updated_at");
//                        String pass = user.getString("password");

                        // Inserting row in users table
//                        db.addUser(name, email, uid, created_at, updated_at,pass);
                        Toast.makeText(getApplicationContext(), error_msg, LENGTH_LONG).show();
                        // Launch main activity
                        Intent intent = new Intent(ViewData.this,Result.class);
                        intent.putExtra("adt",adt);
                        intent.putExtra("aet",aet);
                        intent.putExtra("sdt",sdt);
                        intent.putExtra("dc",dc);
                        intent.putExtra("sql",sql);
                        startActivity(intent);
                        finish();
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// MainActivity.class);
//                        startActivity(intent);
                        //finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Cursor res = db.gettt();
                String names[] = new String[db.getProfilesCount()];
                String emails[] =new String[db.getProfilesCount()];
                String phones[] = new String[db.getProfilesCount()];
                String dob[] = new String[db.getProfilesCount()];
                String dia[] = new String[db.getProfilesCount()];
                String bp[] = new String[db.getProfilesCount()];
                String hr[] = new String[db.getProfilesCount()];
                String ep[] = new String[db.getProfilesCount()];
                String tb[] = new String[db.getProfilesCount()];
                String di[] = new String[db.getProfilesCount()];
                long et = 0;
                long dt=0;
                if(res.getCount()==0){

                    // textView.setText("no data found");
                }
                else {


                    StringBuffer buffer = new StringBuffer();
                    // while(){
                    int i=0;
                   final long t1 = System.nanoTime();
                    while(res.moveToNext()){
                          try {
                         names[i] = new String(security.decrypt(res.getString(1),imei));
                        emails[i] =new String(security.decrypt(res.getString(2),imei));
                            phones[i] = new String(security.decrypt(res.getString(3),imei));
                            dob[i] = new String(security.decrypt(res.getString(4),imei));
                            dia[i] = new String(security.decrypt(res.getString(5),imei));
                            bp[i] = new String(security.decrypt(res.getString(6),imei));
                            hr[i] = new String(security.decrypt(res.getString(7),imei));
                            ep[i] = new String(security.decrypt(res.getString(8),imei));
                            tb[i] = new String(security.decrypt(res.getString(9),imei));
                            di[i] = new String(security.decrypt(res.getString(10),imei));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        i++;
                    }

                    final long t2 = System.nanoTime();
                    dt = (t2-t1)/1000;
                    new Thread()
                    {
                        public void run()
                        {
                            ViewData.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {

                                    Toast.makeText(ViewData.this,"DECRYPTION TIME FOR "+db.getProfilesCount()+" Data is : "+String.valueOf(((t2-t1)/1000))+"MICRO SECONDS",LENGTH_LONG).show();

                                    //Do your UI operations like dialog opening or Toast here
                                }
                            });
                        }
                    }.start();

                    final long et1 = System.nanoTime();
                    for (int j=0;j<db.getProfilesCount();j++){

                        try {
                            names[j]= Security.bytesToHex(security.encrypt(names[j],imei));
                            emails[j]= Security.bytesToHex(security.encrypt(emails[j],imei));
                            phones[j]= Security.bytesToHex(security.encrypt(phones[j],imei));
                            dob[j] = Security.bytesToHex(security.encrypt(dob[j],imei));
                            dia[j] = Security.bytesToHex(security.encrypt(dia[j],imei));
                            bp[j] = Security.bytesToHex(security.encrypt(bp[j],imei));
                            hr[j] = Security.bytesToHex(security.encrypt(hr[j],imei));
                            ep[j] = Security.bytesToHex(security.encrypt(ep[j],imei));
                            tb[j] = Security.bytesToHex(security.encrypt(tb[j],imei));
                            di[j] = Security.bytesToHex(security.encrypt(di[j],imei));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    final long et2 = System.nanoTime();



                    et=(et2-et1)/1000;













                  // json_enc
                    //Toast.makeText(getApplicationContext(), Arrays.toString(names)+Arrays.toString(emails), LENGTH_LONG).show();
                }

                    //}

               //     textView.setText(buffer.toString());
for(int k=0;k<db.getProfilesCount();k++) {
    params.put("name["+ k +"]", names[k]);
    params.put("email["+ k +"]",emails[k]);
    params.put("phone[" + k + "]",phones[k]);
    params.put("dob[" + k + "]",dob[k]);
    params.put("dia[" + k + "]",dia[k]);
    params.put("bp[" + k + "]",bp[k]);
    params.put("hr[" + k + "]",hr[k]);
    params.put("ep[" + k + "]",ep[k]);
    params.put("tb[" + k + "]",tb[k]);
    params.put("di[" + k + "]",di[k]);
    params.put("di[" + k + "]",di[k]);
    params.put("imei",imei);
}
                params.put("aet", String.valueOf(et));
                params.put("adt", String.valueOf(dt));
                params.put("dc", String.valueOf(db.getProfilesCount()));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }



    void sync_datar(){

       final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String imei1 = telephonyManager.getDeviceId();

        Random rand = new Random();
        final String   imei = String.valueOf(rand.nextInt(50) + 1);

        String tag_string_req = "req_sync";

        pDialog.setMessage("Syncing "+db.getProfilesCount()+" Data TO SERVER");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SYNC, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
                /// Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session


                        // Now store the user in SQLite
                        String adt = jObj.getString("Android_Decryption_Time");
                        String aet = jObj.getString("Android_Encryption_Time");
                        String sdt = jObj.getString("Server_Decryption_Time");
                        String dc = jObj.getString("Data_Count");
                        String sql = jObj.getString("SQL_Execution_Time");
                        String error_msg = jObj.getString("error_msg");

//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
//                        String created_at = user.getString("created_at");
//                        String updated_at = user.getString("updated_at");
//                        String pass = user.getString("password");

                        // Inserting row in users table
//                        db.addUser(name, email, uid, created_at, updated_at,pass);
                        Toast.makeText(getApplicationContext(), error_msg, LENGTH_LONG).show();
                        // Launch main activity
                        Intent intent = new Intent(ViewData.this,Result.class);
                        intent.putExtra("adt",adt);
                        intent.putExtra("aet",aet);
                        intent.putExtra("sdt",sdt);
                        intent.putExtra("dc",dc);
                        intent.putExtra("sql",sql);
                        startActivity(intent);
                        finish();
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// MainActivity.class);
//                        startActivity(intent);
                        //finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Cursor res = db.gettt();
                String names[] = new String[db.getProfilesCount()];
                String emails[] =new String[db.getProfilesCount()];
                String phones[] = new String[db.getProfilesCount()];
                String dob[] = new String[db.getProfilesCount()];
                String dia[] = new String[db.getProfilesCount()];
                String bp[] = new String[db.getProfilesCount()];
                String hr[] = new String[db.getProfilesCount()];
                String ep[] = new String[db.getProfilesCount()];
                String tb[] = new String[db.getProfilesCount()];
                String di[] = new String[db.getProfilesCount()];
                long et = 0;
                long dt=0;
                if(res.getCount()==0){

                    // textView.setText("no data found");
                }
                else {


                    StringBuffer buffer = new StringBuffer();
                    // while(){
                    int i=0;
                    final long t1 = System.nanoTime();
                    while(res.moveToNext()){
                        try {
                            names[i] = new String(security.decrypt(res.getString(1),imei1));
                            emails[i] =new String(security.decrypt(res.getString(2),imei1));
                            phones[i] = new String(security.decrypt(res.getString(3),imei1));
                            dob[i] = new String(security.decrypt(res.getString(4),imei1));
                            dia[i] = new String(security.decrypt(res.getString(5),imei1));
                            bp[i] = new String(security.decrypt(res.getString(6),imei1));
                            hr[i] = new String(security.decrypt(res.getString(7),imei1));
                            ep[i] = new String(security.decrypt(res.getString(8),imei1));
                            tb[i] = new String(security.decrypt(res.getString(9),imei1));
                            di[i] = new String(security.decrypt(res.getString(10),imei1));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        i++;
                    }


                    final long t2 = System.nanoTime();
                    dt = (t2-t1)/1000;
                    new Thread()
                    {
                        public void run()
                        {
                            ViewData.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {

                                    Toast.makeText(ViewData.this,"DECRYPTION TIME FOR "+db.getProfilesCount()+" Data is : "+String.valueOf(((t2-t1)/1000))+"MICRO SECONDS",LENGTH_LONG).show();

                                    //Do your UI operations like dialog opening or Toast here
                                }
                            });
                        }
                    }.start();

                    final long et1 = System.nanoTime();
                    for (int j=0;j<db.getProfilesCount();j++){

                        try {
                            names[j]= Security.bytesToHex(security.encrypt(names[j],imei));
                            emails[j]= Security.bytesToHex(security.encrypt(emails[j],imei));
                            phones[j]= Security.bytesToHex(security.encrypt(phones[j],imei));
                            dob[j] = Security.bytesToHex(security.encrypt(dob[j],imei));
                            dia[j] = Security.bytesToHex(security.encrypt(dia[j],imei));
                            bp[j] = Security.bytesToHex(security.encrypt(bp[j],imei));
                            hr[j] = Security.bytesToHex(security.encrypt(hr[j],imei));
                            ep[j] = Security.bytesToHex(security.encrypt(ep[j],imei));
                            tb[j] = Security.bytesToHex(security.encrypt(tb[j],imei));
                            di[j] = Security.bytesToHex(security.encrypt(di[j],imei));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    final long et2 = System.nanoTime();



                    et=(et2-et1)/1000;













                    // json_enc
                    //Toast.makeText(getApplicationContext(), Arrays.toString(names)+Arrays.toString(emails), LENGTH_LONG).show();
                }

                //}

                //     textView.setText(buffer.toString());
                for(int k=0;k<db.getProfilesCount();k++) {
                    params.put("name["+ k +"]", names[k]);
                    params.put("email["+ k +"]",emails[k]);
                    params.put("phone[" + k + "]",phones[k]);
                    params.put("dob[" + k + "]",dob[k]);
                    params.put("dia[" + k + "]",dia[k]);
                    params.put("bp[" + k + "]",bp[k]);
                    params.put("hr[" + k + "]",hr[k]);
                    params.put("ep[" + k + "]",ep[k]);
                    params.put("tb[" + k + "]",tb[k]);
                    params.put("di[" + k + "]",di[k]);
                    params.put("di[" + k + "]",di[k]);
                    params.put("imei",imei);
                }
                params.put("aet", String.valueOf(et));
                params.put("adt", String.valueOf(dt));
                params.put("dc", String.valueOf(db.getProfilesCount()));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }





    void sync_datap(){

        final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String imei1 = telephonyManager.getDeviceId();
        final String[] imei = {null};

        String tag_string_req = "req_sync";

        pDialog.setMessage("Syncing "+db.getProfilesCount()+" Data TO SERVER");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SYNC, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
                /// Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session


                        // Now store the user in SQLite
                        String adt = jObj.getString("Android_Decryption_Time");
                        String aet = jObj.getString("Android_Encryption_Time");
                        String sdt = jObj.getString("Server_Decryption_Time");
                        String dc = jObj.getString("Data_Count");
                        String sql = jObj.getString("SQL_Execution_Time");
                        String error_msg = jObj.getString("error_msg");

//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
//                        String created_at = user.getString("created_at");
//                        String updated_at = user.getString("updated_at");
//                        String pass = user.getString("password");

                        // Inserting row in users table
//                        db.addUser(name, email, uid, created_at, updated_at,pass);
                        Toast.makeText(getApplicationContext(), error_msg, LENGTH_LONG).show();
                        // Launch main activity
                        Intent intent = new Intent(ViewData.this,Result.class);
                        intent.putExtra("adt",adt);
                        intent.putExtra("aet",aet);
                        intent.putExtra("sdt",sdt);
                        intent.putExtra("dc",dc);
                        intent.putExtra("sql",sql);
                        startActivity(intent);
                        finish();
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// MainActivity.class);
//                        startActivity(intent);
                        //finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Cursor res = db.gettt();
                String names[] = new String[db.getProfilesCount()];
                String emails[] =new String[db.getProfilesCount()];
                String phones[] = new String[db.getProfilesCount()];
                String dob[] = new String[db.getProfilesCount()];
                String dia[] = new String[db.getProfilesCount()];
                String bp[] = new String[db.getProfilesCount()];
                String hr[] = new String[db.getProfilesCount()];
                String ep[] = new String[db.getProfilesCount()];
                String tb[] = new String[db.getProfilesCount()];
                String di[] = new String[db.getProfilesCount()];
                long et = 0;
                long dt=0;
                if(res.getCount()==0){

                    // textView.setText("no data found");
                }
                else {


                    StringBuffer buffer = new StringBuffer();
                    // while(){
                    int i=0;
                    final long t1 = System.nanoTime();
                    while(res.moveToNext()){
                        try {
                            names[i] = new String(security.decrypt(res.getString(1),imei1));
                            emails[i] =new String(security.decrypt(res.getString(2),imei1));
                            phones[i] = new String(security.decrypt(res.getString(3),imei1));
                            dob[i] = new String(security.decrypt(res.getString(4),imei1));
                            dia[i] = new String(security.decrypt(res.getString(5),imei1));
                            bp[i] = new String(security.decrypt(res.getString(6),imei1));
                            hr[i] = new String(security.decrypt(res.getString(7),imei1));
                            ep[i] = new String(security.decrypt(res.getString(8),imei1));
                            tb[i] = new String(security.decrypt(res.getString(9),imei1));
                            di[i] = new String(security.decrypt(res.getString(10),imei1));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        i++;
                    }
                     imei[0] = phones[0];
                    final long t2 = System.nanoTime();
                    dt = (t2-t1)/1000;
                    new Thread()
                    {
                        public void run()
                        {
                            ViewData.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {

                                    Toast.makeText(ViewData.this,"DECRYPTION TIME FOR "+db.getProfilesCount()+" Data is : "+String.valueOf(((t2-t1)/1000))+"MICRO SECONDS",LENGTH_LONG).show();

                                    //Do your UI operations like dialog opening or Toast here
                                }
                            });
                        }
                    }.start();

                    final long et1 = System.nanoTime();
                    for (int j=0;j<db.getProfilesCount();j++){

                        try {
                            names[j]= Security.bytesToHex(security.encrypt(names[j], imei[0]));
                            emails[j]= Security.bytesToHex(security.encrypt(emails[j], imei[0]));
                            phones[j]= Security.bytesToHex(security.encrypt(phones[j], imei[0]));
                            dob[j] = Security.bytesToHex(security.encrypt(dob[j], imei[0]));
                            dia[j] = Security.bytesToHex(security.encrypt(dia[j], imei[0]));
                            bp[j] = Security.bytesToHex(security.encrypt(bp[j], imei[0]));
                            hr[j] = Security.bytesToHex(security.encrypt(hr[j], imei[0]));
                            ep[j] = Security.bytesToHex(security.encrypt(ep[j], imei[0]));
                            tb[j] = Security.bytesToHex(security.encrypt(tb[j], imei[0]));
                            di[j] = Security.bytesToHex(security.encrypt(di[j], imei[0]));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    final long et2 = System.nanoTime();



                    et=(et2-et1)/1000;













                    // json_enc
                    //Toast.makeText(getApplicationContext(), Arrays.toString(names)+Arrays.toString(emails), LENGTH_LONG).show();
                }

                //}

                //     textView.setText(buffer.toString());
                for(int k=0;k<db.getProfilesCount();k++) {
                    params.put("name["+ k +"]", names[k]);
                    params.put("email["+ k +"]",emails[k]);
                    params.put("phone[" + k + "]",phones[k]);
                    params.put("dob[" + k + "]",dob[k]);
                    params.put("dia[" + k + "]",dia[k]);
                    params.put("bp[" + k + "]",bp[k]);
                    params.put("hr[" + k + "]",hr[k]);
                    params.put("ep[" + k + "]",ep[k]);
                    params.put("tb[" + k + "]",tb[k]);
                    params.put("di[" + k + "]",di[k]);
                    params.put("di[" + k + "]",di[k]);
                    params.put("imei", imei[0]);
                }
                params.put("aet", String.valueOf(et));
                params.put("adt", String.valueOf(dt));
                params.put("dc", String.valueOf(db.getProfilesCount()));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
