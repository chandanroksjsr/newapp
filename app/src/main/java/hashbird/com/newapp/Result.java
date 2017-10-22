package hashbird.com.newapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Result extends AppCompatActivity {
TextView adt,aet,sdt,dc,sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String adt1= getIntent().getStringExtra("adt");
        String aet1= getIntent().getStringExtra("aet");
        String sdt1= getIntent().getStringExtra("sdt");
        String dc1= getIntent().getStringExtra("dc");
        String sql1= getIntent().getStringExtra("sql");


        adt = (TextView)findViewById(R.id.adt);
        aet = (TextView)findViewById(R.id.aet);
        sdt = (TextView)findViewById(R.id.sdt);
        sql = (TextView)findViewById(R.id.sql);
        dc = (TextView)findViewById(R.id.dc);


        adt.setText(adt1);
        aet.setText(aet1);
        sdt.setText(sdt1);
        sql.setText(sql1);
        dc.setText(dc1);

    }
}
