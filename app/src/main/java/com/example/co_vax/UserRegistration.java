package com.example.co_vax;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserRegistration extends AppCompatActivity {

    DatePickerDialog picker;
    TextInputLayout et_reg_first_name, et_reg_last_name, et_reg_email, et_reg_mobile_no,et_reg_aadhar,et_reg_birth_date;
    Button add_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        add_user = findViewById(R.id.add_user);
        et_reg_first_name = findViewById(R.id.et_reg_first_name);
        et_reg_last_name = findViewById(R.id.et_reg_last_name);
        et_reg_email = findViewById(R.id.et_reg_email);
        et_reg_mobile_no = findViewById(R.id.et_reg_mobile_no);
        et_reg_aadhar = findViewById(R.id.et_reg_aadhar);
        et_reg_birth_date = findViewById(R.id.et_reg_birth_date);
        et_reg_birth_date.getEditText().setInputType(InputType.TYPE_NULL);

        et_reg_birth_date.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar today = Calendar.getInstance();
                int day = today.get(Calendar.DAY_OF_MONTH);
                int month = today.get(Calendar.MONTH);
                int year = today.get(Calendar.YEAR);

                picker = new DatePickerDialog(UserRegistration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dob = Calendar.getInstance();
                        dob.set(year, month, dayOfMonth);
                        int age =  today.get(Calendar.YEAR) - year;
                        et_reg_birth_date.getEditText().setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.POST, RequestUrls.REG_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "onResponse: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("200")){
                                Intent i = new Intent(UserRegistration.this, RegOtp.class);
                                i.putExtra("first_name", et_reg_first_name.getEditText().getText().toString());
                                i.putExtra("last_name", et_reg_last_name.getEditText().getText().toString());
                                i.putExtra("email", et_reg_email.getEditText().getText().toString());
                                i.putExtra("mobile_no", et_reg_mobile_no.getEditText().getText().toString());
                                i.putExtra("aadhar", et_reg_aadhar.getEditText().getText().toString());
                                i.putExtra("dob", et_reg_birth_date.getEditText().getText().toString());
                                i.putExtra("verify_otp", obj.getString("otp"));
                                startActivity(i);
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
                        params.put("first_name", et_reg_first_name.getEditText().getText().toString());
                        params.put("last_name", et_reg_last_name.getEditText().getText().toString());
                        params.put("email", et_reg_email.getEditText().getText().toString());
                        params.put("generate_otp", "true");
                        return params;
                    }
                };

                queue.add(request);
            }
        });
    }
}