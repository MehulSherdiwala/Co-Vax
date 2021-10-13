package com.example.co_vax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginOtp extends AppCompatActivity {

    TextInputLayout et_verify_otp;
    Button btn_login;
    TextView txt_otp_err;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        et_verify_otp = findViewById(R.id.et_verify_otp);
        btn_login = findViewById(R.id.btn_login);
        txt_otp_err = findViewById(R.id.txt_otp_err);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.LOGIN_OTP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "onResponse: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("200")){
                                JSONObject userData = new JSONObject(obj.getString("data"));
                                sharedPreferences = getSharedPreferences("co_vax", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_id", userData.getString("user_id"));
                                editor.putString("user_name", userData.getString("first_name") + " " + userData.getString("last_name"));
                                editor.putString("user_type", userData.getString("user_type"));
                                editor.apply();
                                if (userData.getString("user_type").equals("2")) {
                                    Intent i = new Intent(LoginOtp.this, VolunteerDashboard.class);
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(LoginOtp.this, UserDashboard.class);
                                    startActivity(i);
                                }
                            } else if (obj.getString("status").equals("400")){
                                txt_otp_err.setText(obj.getString("error"));
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
                        params.put("verify_otp", extras.getString("verify_otp"));
                        params.put("otp", et_verify_otp.getEditText().getText().toString());
                        params.put("login", "true");
                        return params;
                    }
                };

                queue.add(request);
            }
        });
    }
}