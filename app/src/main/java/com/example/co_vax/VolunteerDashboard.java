package com.example.co_vax;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolunteerDashboard extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button btn_get_patient, btn_vaccinated, btn_vol_logout;
    TextInputLayout et_vol_mobile_no;
    TextView txt_vol_name, txt_vol_aadhar, txt_vol_email;
    int app_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_dashboard);
        et_vol_mobile_no = findViewById(R.id.et_vol_mobile_no);
        btn_get_patient = findViewById(R.id.btn_get_patient);
        btn_vaccinated = findViewById(R.id.btn_vaccinated);
        txt_vol_name = findViewById(R.id.txt_vol_name);
        txt_vol_aadhar = findViewById(R.id.txt_vol_aadhar);
        txt_vol_email = findViewById(R.id.txt_vol_email);
        btn_vol_logout = findViewById(R.id.btn_vol_logout);


        sharedPreferences = getSharedPreferences("co_vax", MODE_PRIVATE);
        if (sharedPreferences.getString("user_id", "").equals("")) {
            Intent i = new Intent(VolunteerDashboard.this, MainActivity.class);
            startActivity(i);
        }

        btn_get_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_no = et_vol_mobile_no.getEditText().getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.USER_URL, response -> {
                    Log.d("response", "res: " + response);
                    if (!response.equals("[]")) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            app_id = Integer.parseInt(obj.getString("app_id"));
                            txt_vol_name.setText(obj.getString("first_name") + "-" + obj.getString("last_name"));
                            txt_vol_aadhar.setText(obj.getString("aadhar_no"));
                            txt_vol_email.setText(obj.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Log.d("response", "error: " + error.toString());
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("mobile_no", mobile_no);
                        params.put("vaccinated", "false");
                        return params;
                    }
                };
                queue.add(request);
            }
        });

        btn_vaccinated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app_id != 0) {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.APPOINTMENT_URL, response -> {
                        Log.d("response", "res: " + response);
                        if (response.equals("true")) {
                            Toast.makeText(getApplicationContext(), "User Vaccinated Successfully", Toast.LENGTH_SHORT).show();
                            txt_vol_name.setText("");
                            txt_vol_aadhar.setText("");
                            txt_vol_email.setText("");
                        }
                    }, error -> {
                        Log.d("response", "error: " + error.toString());
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("app_id", String.valueOf(app_id));
                            params.put("volunteer_id", sharedPreferences.getString("user_id", "0"));
                            params.put("vaccinated", "true");
                            return params;
                        }
                    };
                    queue.add(request);
                }
            }
        });

        btn_vol_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove("user_name");
                edit.remove("user_id");
                edit.remove("user_type");
                edit.apply();
                Intent i = new Intent(VolunteerDashboard.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}