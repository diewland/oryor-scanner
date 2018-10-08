package com.diewland.oryorscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diewland.lib.Oryor;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "result";

    private TextView tv_oy_no;
    private TextView tv_type;
    private TextView tv_food;
    private TextView tv_product_th;
    private TextView tv_product_en;
    private TextView tv_product_status;
    private TextView tv_license_name;
    private TextView tv_place_name;
    private TextView tv_location;
    private TextView tv_place_license_status;

    private Button btn_scan;
    private Button btn_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // init screen objects
        tv_oy_no = (TextView)findViewById(R.id.oy_no);
        tv_type = (TextView)findViewById(R.id.type);
        tv_food = (TextView)findViewById(R.id.food);
        tv_product_th = (TextView)findViewById(R.id.product_th);
        tv_product_en = (TextView)findViewById(R.id.product_en);
        tv_product_status = (TextView)findViewById(R.id.product_status);
        tv_license_name = (TextView)findViewById(R.id.license_name);
        tv_place_name = (TextView)findViewById(R.id.place_name);
        tv_location = (TextView)findViewById(R.id.location);
        tv_place_license_status = (TextView)findViewById(R.id.place_license_status);
        btn_scan = (Button)findViewById(R.id.btn_scan);
        btn_report = (Button)findViewById(R.id.btn_report);

        // get oryor number from scanner
        String oy_no = getIntent().getStringExtra("oy_no");
        Log.d("TAG", oy_no);
        queryDataToScreen(oy_no);

        // bind scan button
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // bind report button
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://oryor.com/oryor2015/complain.php";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        // show loading message
        Toast.makeText(this, "กำลังหาข้อมูล..", Toast.LENGTH_SHORT).show();
    }

    private void queryDataToScreen(String oy_no){
        // prepare api
        String api = "http://porta.fda.moph.go.th/FDA_SEARCH_ALL/PRODUCT/FRM_PRODUCT_FOOD.aspx?fdpdtno=%s";
        String url = String.format(api, oy_no.replaceAll("-", ""));

        // call api
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String, String> result = Oryor.parseHash(response);
                        Log.d(TAG, result.toString());
                        updateTable(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                Log.d(TAG, error.toString());
            }
        });
        queue.add(stringRequest);
    }

    private void updateTable(HashMap<String, String> result){
        tv_oy_no.setText(result.get("Product No"));
        tv_type.setText(result.get("Type #1"));
        tv_food.setText(result.get("Type #2"));
        tv_product_th.setText(result.get("Name (TH)"));
        tv_product_en.setText(result.get("Name (EN)"));
        tv_product_status.setText(result.get("License Status #1"));
        tv_license_name.setText(result.get("License Name"));
        tv_place_name.setText(result.get("Location Name"));
        tv_location.setText(result.get("Location Address"));
        tv_place_license_status.setText(result.get("License Status #2"));
    }
}
