package com.kbpluse.atendance.atendace.Activity.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.kbpluse.atendance.atendace.Activity.Domain.RegisterDomain;
import com.kbpluse.atendance.atendace.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private static final String KEY_ID = "id";
    EditText etname, etEmail, etPass, etMobileNo, etDesignation, etdate_of_joining, etdate_of_birth;
    Button btnRegitration;
    TextView tvlogin;
    private Context context;
    private String TAG = Registration.class.getSimpleName();
    private AwesomeValidation awesomeValidation;
    SharedPreferences sharedPreferences;
    private int mYear, mMonth, mDay, mHour, mMinute;


    public static final String url = "http://empattendance.kbplussoftwaresystem.com/saveEmpInfo.html";
    //public static final String url = "http://192.168.1.10:8080/Emp_Attendance/saveEmpInfo.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = Registration.this;
        Log.i(TAG, "onCreate: ");

        SharedPreferences prefs = getSharedPreferences("RegisterPref", MODE_PRIVATE);
        String registerDone = prefs.getString("registerDone", null);
        if (registerDone != null) {
            Log.i(TAG, "registerDone != null");
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

        etname = findViewById(R.id.user_name);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etUserPass);
        etMobileNo = findViewById(R.id.etUserNumber);
        etDesignation = findViewById(R.id.etDesignation);
        btnRegitration = findViewById(R.id.btnRegister);
        etdate_of_birth = findViewById(R.id.etdate_of_birth);
        etdate_of_joining = findViewById(R.id.etdate_of_joining);
        etdate_of_joining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDateOfJoining();
            }
        });
        etdate_of_birth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                addDateOfBirth();
            }
        });
        etdate_of_joining.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                addDateOfJoining();
            }
        });
        tvlogin = findViewById(R.id.tvLogin);
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.user_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.email_error);
        awesomeValidation.addValidation(this, R.id.etUserPass, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.password_error);
        awesomeValidation.addValidation(this, R.id.etDesignation, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.designation_error);
        //awesomeValidation.addValidation(this, R.id.etUserNumber, "^[2-9]{2}[0-9]{8}$", R.string.number_error);

        btnRegitration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegistration();
            }

            private void doRegistration() {

                String name = etname.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPass.getText().toString();
                String num = etMobileNo.getText().toString();
                String designation = etDesignation.getText().toString();
                String DOB = etdate_of_birth.getText().toString();
                String DOJ = etdate_of_joining.getText().toString();

                if (awesomeValidation.validate()) {
                    Toast.makeText(Registration.this, "Validation Success", Toast.LENGTH_SHORT).show();

                    sharedPreferences = getSharedPreferences("RegisterPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("pass", pass);
                    editor.putString("num", num);
                    editor.putString("desig", designation);
                    editor.putString("dob", DOB);
                    editor.putString("doj", DOJ);
                    editor.putString("registerDone", "registerDone");
                    editor.commit();

                    try {
                        sendVolleyData();
                        startActivity(new Intent(context, LoginActivity.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }


    private void addDateOfBirth() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etdate_of_birth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void addDateOfJoining() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        etdate_of_joining.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void sendVolleyData() throws JSONException {


        Map<String, String> params = new HashMap();

        RegisterDomain registerDomain = new RegisterDomain();
        registerDomain.setEmpName(etname.getText().toString());
        registerDomain.setEmpEmail(etEmail.getText().toString());
        registerDomain.setDesignation(etDesignation.getText().toString());
        registerDomain.setPhone(etMobileNo.getText().toString());
        registerDomain.setPassword(etPass.getText().toString());
        registerDomain.setDateOfBirth(etdate_of_birth.getText().toString());
        registerDomain.setDateOfJoining(etdate_of_joining.getText().toString());


        params.put("emp_name", registerDomain.getEmpName());
        params.put("emp_email", registerDomain.getEmpEmail());
        params.put("designation", registerDomain.getDesignation());
        params.put("phone", registerDomain.getPhone());
        params.put("password", registerDomain.getPassword());
        params.put("date_of_joining", registerDomain.getDateOfJoining());
        params.put("date_of_birth", registerDomain.getDateOfBirth());


        JSONObject parameters = new JSONObject(params);

        Log.i(TAG, "sendVolleyData:parameters " + parameters);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
