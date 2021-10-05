package com.example.co_vax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegOtp extends AppCompatActivity {

    TextInputLayout et_reg_verify_otp;
    TextView txt_reg_otp_err;
    Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_otp);
        et_reg_verify_otp = findViewById(R.id.et_reg_verify_otp);
        txt_reg_otp_err = findViewById(R.id.txt_reg_otp_err);
        btn_reg = findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.REG_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "onResponse: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("200")){
                                Intent i = new Intent(RegOtp.this, UserDashboard.class);
                                startActivity(i);
                            } else if (obj.getString("status").equals("400")){
                                txt_reg_otp_err.setText(obj.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", "Error: " + error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("mobile_no", extras.getString("mobile_no"));
                        params.put("first_name", extras.getString("first_name"));
                        params.put("last_name", extras.getString("last_name"));
                        params.put("email", extras.getString("email"));
                        params.put("aadhar", extras.getString("aadhar"));
                        params.put("dob", extras.getString("dob"));
                        params.put("verify_otp", extras.getString("verify_otp"));
                        params.put("otp", et_reg_verify_otp.getEditText().getText().toString());
                        params.put("register", "true");
                        return params;
                    }
                };

                queue.add(request);
            }
        });
    }
}