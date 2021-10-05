package com.example.co_vax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {

    Spinner sp_search_state, sp_search_city;
    Button btn_search;
    ListView lv_display_slots;
    ArrayList<String> states = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btn_search = findViewById(R.id.btn_search);
        sp_search_state = findViewById(R.id.sp_search_state);
        sp_search_city = findViewById(R.id.sp_search_city);
        lv_display_slots = findViewById(R.id.lv_display_slots);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.LOCATION_URL, response -> {
            Log.d("response", "onCreate: " + response);
            try {
                JSONArray s = new JSONArray(response);
                for (int i = 0; i < s.length(); i++) {
                    states.add(s.getString(i));
                }
                final ArrayAdapter ad = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, states);
                ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                sp_search_state.setAdapter(ad);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Log.d("response", "onCreate: " + error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("get_state", "true");
                return params;
            }
        };
        queue.add(request);

        sp_search_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.LOCATION_URL, response -> {
                    Log.d("response", "onCreate: " + response);
                    try {
                        JSONArray s = new JSONArray(response);
                        ArrayList<String> city = new ArrayList<>();
                        for (int i = 0; i < s.length(); i++) {
                            city.add(s.getString(i));
                        }
                        final ArrayAdapter ad = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, city);
                        ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        sp_search_city.setAdapter(ad);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    Log.d("response", "onCreate: " + error.toString());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("state_name", states.get(position));
                        params.put("get_city", "true");
                        return params;
                    }
                };
                queue.add(request);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.LOCATION_URL, response -> {
                    Log.d("response", "onCreate: " + response);
                    try {
                        JSONArray s = new JSONArray(response);
                        ArrayList<SlotItem> slot = new ArrayList<>();
                        for (int i = 0; i < s.length(); i++) {
                            JSONObject obj = s.getJSONObject(i);
                            slot.add(new SlotItem(
                                    obj.getInt("location_id"),
                                    obj.getString("location_name"),
                                    obj.getString("location_address"),
                                    obj.getString("vaccine_name"),
                                    obj.getString("date"),
                                    obj.getString("remaining_slot")
                            ));
                        }

                        final SlotItemAdapter ad = new SlotItemAdapter(getApplicationContext(), R.layout.activity_search_slot_item, slot);
                        lv_display_slots.setAdapter(ad);

                        lv_display_slots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("response", "onItemClick: " + slot.get(position).getLocation_id());
                                Intent i = new Intent(Search.this, Booking.class);
                                i.putExtra("location_id", String.valueOf(slot.get(position).getLocation_id()));
                                i.putExtra("vaccine_name", slot.get(position).getVaccine_name());
                                i.putExtra("date", slot.get(position).getDate());
                                startActivity(i);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    Log.d("response", "onCreate: " + error.toString());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("city_name", sp_search_city.getSelectedItem().toString());
                        params.put("get_location", "true");
                        return params;
                    }
                };
                queue.add(request);
            }
        });

    }
}