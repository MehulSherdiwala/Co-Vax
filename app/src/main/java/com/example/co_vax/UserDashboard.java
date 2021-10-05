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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserDashboard extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button btn_dash_logout, btn_dose1, btn_dose2;
    TextView txt_dash_username, txt_dash_mobile_no, txt_dash_year;
    TextView txt_dash_dose1_vaccine, txt_dash_dose2_vaccine, txt_dash_dose1_address, txt_dash_dose2_address, txt_dash_dose1_date, txt_dash_dose2_date, txt_dash_dose1_slot, txt_dash_dose2_slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        txt_dash_username = findViewById(R.id.txt_dash_username);
        txt_dash_mobile_no = findViewById(R.id.txt_dash_mobile_no);
        txt_dash_year = findViewById(R.id.txt_dash_year);
        txt_dash_dose1_vaccine = findViewById(R.id.txt_dash_dose1_vaccine);
        txt_dash_dose2_vaccine = findViewById(R.id.txt_dash_dose2_vaccine);
        txt_dash_dose1_address = findViewById(R.id.txt_dash_dose1_address);
        txt_dash_dose2_address = findViewById(R.id.txt_dash_dose2_address);
        txt_dash_dose1_date = findViewById(R.id.txt_dash_dose1_date);
        txt_dash_dose2_date = findViewById(R.id.txt_dash_dose2_date);
        txt_dash_dose1_slot = findViewById(R.id.txt_dash_dose1_slot);
        txt_dash_dose2_slot = findViewById(R.id.txt_dash_dose2_slot);
        btn_dose1 = findViewById(R.id.btn_dose1);
        btn_dose2 = findViewById(R.id.btn_dose2);

        btn_dash_logout = findViewById(R.id.btn_dash_logout);
        sharedPreferences = getSharedPreferences("co_vax", MODE_PRIVATE);
        if (!sharedPreferences.getString("user_id", "").equals("")) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.USER_URL, response -> {
                Log.d("response", response);
                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject user_data = res.getJSONObject("user_data");
                    JSONArray user_doses = res.getJSONArray("user_doses");
                    txt_dash_username.setText(user_data.getString("first_name") + " " + user_data.getString("last_name"));
                    txt_dash_mobile_no.setText(user_data.getString("mobile_no"));
                    txt_dash_year.setText(user_data.getString("dob").split("-")[0]);


                    if (user_doses.length() >= 1){
                        JSONObject dose = user_doses.getJSONObject(0);
                        txt_dash_dose1_vaccine.setText(dose.getString("vaccine_name"));
                        txt_dash_dose1_address.setText(dose.getString("location_name"));
                        txt_dash_dose1_date.setText(", " + dose.getString("date"));
                        txt_dash_dose1_slot.setText(", " + dose.getString("start_time") +"-" + dose.getString("end_time"));
                        if (dose.getString("status").equals("0")){
                            btn_dose1.setText("Booked");
                        } else if (dose.getString("status").equals("1")){
                            btn_dose1.setText("Vaccinated");
                        }
                        btn_dose1.setClickable(false);
                    } else {
                        txt_dash_dose1_vaccine.setText("");
                        txt_dash_dose1_address.setText("");
                        txt_dash_dose1_date.setText("");
                        txt_dash_dose1_slot.setText("");
                        btn_dose1.setText("Pending");
                    }
                    if (user_doses.length() == 2){
                        JSONObject dose = user_doses.getJSONObject(1);
                        txt_dash_dose2_vaccine.setText(dose.getString("vaccine_name"));
                        txt_dash_dose2_address.setText(dose.getString("location_name"));
                        txt_dash_dose2_date.setText(", " + dose.getString("date"));
                        txt_dash_dose2_slot.setText(", " + dose.getString("start_time") +"-" + dose.getString("end_time"));
                        if (dose.getString("status").equals("0")){
                            btn_dose2.setText("Booked");
                        } else if (dose.getString("status").equals("1")){
                            btn_dose2.setText("Vaccinated");
                        }
                        btn_dose2.setClickable(false);
                    } else {
                        txt_dash_dose2_vaccine.setText("");
                        txt_dash_dose2_address.setText("");
                        txt_dash_dose2_date.setText("");
                        txt_dash_dose2_slot.setText("");
                        btn_dose2.setText("Pending");
                    }
                    Log.d("response", user_doses.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Log.d("Response", "Error: " + error.toString())) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_id", sharedPreferences.getString("user_id", ""));
                    params.put("user_doses", "true");
                    return params;
                }
            };
            queue.add(request);
        } else {
            Intent i = new Intent(UserDashboard.this, MainActivity.class);
            startActivity(i);
        }


        Log.d("response", "onCreate: " + sharedPreferences.getString("user_name", ""));
        Log.d("response", "onCreate: " + sharedPreferences.getString("user_id", ""));
        Log.d("response", "onCreate: " + sharedPreferences.getString("user_type", ""));

        btn_dash_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove("user_name");
                edit.remove("user_id");
                edit.remove("user_type");
                edit.apply();
            }
        });

        btn_dose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashboard.this, Search.class);
                startActivity(i);
            }
        });
        btn_dose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashboard.this, Search.class);
                startActivity(i);
            }
        });
    }
}