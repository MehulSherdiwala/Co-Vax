package com.example.co_vax;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextInputLayout et_login_mobile_no;
    Button btn_get_otp;
    TextView txt_login_err, txt_login_reg_link;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_login_mobile_no = findViewById(R.id.et_login_mobile_no);
        btn_get_otp = findViewById(R.id.btn_get_otp);
        txt_login_err = findViewById(R.id.txt_login_err);
        txt_login_reg_link = findViewById(R.id.txt_login_reg_link);
        sharedPreferences = getSharedPreferences("co_vax", MODE_PRIVATE);

        if (!sharedPreferences.getString("user_id", "").equals("")){
            if (sharedPreferences.getString("user_type", "").equals("3")){
                Intent i = new Intent(MainActivity.this, UserDashboard.class);
                startActivity(i);
            } else {
                Intent i = new Intent(MainActivity.this, VolunteerDashboard.class);
                startActivity(i);
            }
        }

        btn_get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, RequestUrls.LOGIN_OTP_URL, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("Response", "onResponse: " + response);
//                                try {
//                                    if (response.get("status") == "200") {
//                                        Intent i = new Intent(MainActivity.this, LoginOtp.class);
//                                        i.putExtra("mobile_no", et_login_mobile_no.getEditText().getText().toString());
//                                        startActivity(i);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        },
                                error -> {
                                    // TODO: Handle error
                                    Log.d("Response", "Error: " + error.toString());

                                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("mobile_no", et_login_mobile_no.getEditText().getText().toString());
                        params.put("generate_otp", "true");
                        return params;
                    }
                };*/

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.LOGIN_OTP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "onResponse: " + response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("200")){
                                Intent i = new Intent(MainActivity.this, LoginOtp.class);
                                i.putExtra("mobile_no", et_login_mobile_no.getEditText().getText().toString());
                                i.putExtra("verify_otp", obj.getString("otp"));
                                startActivity(i);
                            } else if (obj.getString("status").equals("400")){
                                txt_login_err.setText(obj.getString("error"));
                            }
                            Log.d("Response", "onResponse: " + obj.getString("status"));
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
                        params.put("mobile_no", et_login_mobile_no.getEditText().getText().toString());
                        params.put("generate_otp", "true");
                        return params;
                    }
                };

                queue.add(request);

            }
        });
        txt_login_reg_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserRegistration.class);
                startActivity(i);
            }
        });
    }
}