package com.example.co_vax;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Booking extends AppCompatActivity {

    TextView txt_booking_address, txt_booking_date;
    Button btn_book;
    RadioGroup rg_booking;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        txt_booking_date = findViewById(R.id.txt_booking_date);
        txt_booking_address = findViewById(R.id.txt_booking_address);
        btn_book = findViewById(R.id.btn_book);
        rg_booking = findViewById(R.id.rg_booking);

        Bundle extra = getIntent().getExtras();

        Log.d("response", "extra: " + extra.getString("date"));
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.APPOINTMENT_URL, response -> {
            Log.d("response", "response: " + response);
            try {
                JSONArray s = new JSONArray(response);
                if (s.length() > 0) {
                    JSONObject object = s.getJSONObject(0);
                    txt_booking_address.setText(object.getString("location_name") + " " + object.getString("location_address"));
                    txt_booking_date.setText(object.getString("date"));

                    rg_booking.removeAllViews();
                    for (int i = 0; i < s.length(); i++) {
                        JSONObject obj = s.getJSONObject(i);
                        RadioButton rbtn = new RadioButton(this);
                        rbtn.setId(Integer.parseInt(obj.getString("slot_id")));
                        rbtn.setText(obj.getString("start_time") + "-" + obj.getString("end_time"));
                        rbtn.setPadding(10, 10, 10, 10);
                        rg_booking.addView(rbtn);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("response", "Error: " + error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("location_id", extra.getString("location_id"));
                params.put("vaccine_name", extra.getString("vaccine_name"));
                params.put("date", extra.getString("date"));
                params.put("get_slot", "true");
                return params;
            }
        };
        queue.add(request);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("co_vax", MODE_PRIVATE);
                int slot_id = rg_booking.getCheckedRadioButtonId();
                Log.d("response", "onClick: " + slot_id);
                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.APPOINTMENT_URL, response -> {
                    Log.d("response", "response: " + response);
                    if (response.equals("true")){
                        Toast.makeText(getApplicationContext(), "Appointment Booked successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Booking.this, UserDashboard.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Appointment Booking unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }, error -> {
                    Log.d("response", "Error: " + error.toString());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("slot_id", String.valueOf(slot_id));
                        params.put("user_id", sharedPreferences.getString("user_id", ""));
                        params.put("book_slot", "true");
                        return params;
                    }
                };
                queue.add(request);
            }
        });
    }
}