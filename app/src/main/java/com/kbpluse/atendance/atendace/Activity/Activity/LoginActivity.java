package com.kbpluse.atendance.atendace.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kbpluse.atendance.atendace.Activity.AddTasks.activity.AddTasksHome;
import com.kbpluse.atendance.atendace.Activity.Domain.LoginDomain;
import com.kbpluse.atendance.atendace.Activity.SharedPref.Pref;
import com.kbpluse.atendance.atendace.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editTextPass;
    TextView tvRegister;
    Button buttonDoLogin;
    ImageView ivProfile;
    private Context context;
    private String TAG = LoginActivity.class.getSimpleName();
    public static final String wifiOriganlName = "02:00:00:00:00:00";
    public static final String URL = "http://empattendance.kbplussoftwaresystem.com/empLogin.html";
    //public static final String URL = "http://192.168.1.2:8080/Emp_Attendance/empLogin.html";
    private Session session;
    Integer PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate: ");
        context = LoginActivity.this;
        session = new Session(this);
        buttonDoLogin = findViewById(R.id.doLogin);
        tvRegister = findViewById(R.id.tvRegistration);
        editTextPass = findViewById(R.id.etpass);
        editEmail = findViewById(R.id.etLoginEmail);
        ivProfile = findViewById(R.id.iv);

        SharedPreferences sharedPreferences = getSharedPreferences("RegisterPref", MODE_PRIVATE);
        String pic = sharedPreferences.getString("imagePreferance", null);
        if (pic != null) {
            ivProfile.setImageBitmap(decodeBase64(pic));
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ivProfile");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture")
                        , PICK_IMAGE_REQUEST);
            }
        });

        if (session.loggedin()) {
            Log.i(TAG, "session.loggedin()");
            startActivity(new Intent(this, AddTasksHome.class));
            finish();
        }

        buttonDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();
                Log.i(TAG, "name & pass" + name + "&" + pass);

                SharedPreferences prefs = getSharedPreferences("RegisterPref", MODE_PRIVATE);
                String restoredEmail = prefs.getString("name", null);
                String restorePass = prefs.getString("pass", null);
                Log.i(TAG, "restoredEmail & estorePass " + restoredEmail + " & " + restorePass);

                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String wifiName = wifiInfo.getMacAddress();
                Log.i(TAG, "onClick: wifiName :" + wifiName);
                Log.i(TAG, "onClick: wifiOriganlName :" + wifiOriganlName);

                if (restoredEmail != null/*&& wifiOriganlName.equals(wifiName)*/) {
                    Log.i(TAG, "onClick: connected with same wifi");
                    login();

                } else {
                    Toast.makeText(context, "check wifi connection", Toast.LENGTH_LONG).show();
                }
            }
        });


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: by tvRegister");
                startActivity(new Intent(context, Registration.class));
            }
        });

    }


    private void login() {

        final LoginDomain loginDomain = new LoginDomain();
        loginDomain.setEmpEmail(editEmail.getText().toString());
        loginDomain.setPassword(editTextPass.getText().toString());

        Map<String, String> params = new HashMap();
        params.put("emp_email", loginDomain.getEmpEmail());
        params.put("password", loginDomain.getPassword());
        params.put("login_time", timer());

        JSONObject parameters = new JSONObject(params);
        Log.i(TAG, "login: parameters" + parameters);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i(TAG, "onResponse: " + response.getString("emp_name") + " "
                                    + response.getString("emp_email") + ", "
                                    + response.getString("designation") + ", "
                                    + response.getString("password") + " "
                                    + response.getString("phone") + ", "
                                    + response.getString("date_of_joining") + ", "
                                    + response.getString("date_of_birth") + ", "
                                    + response.getString("emp_id") + ", "
                                    + response.getString("loginId"));

                            Pref.saveEmpId(context, response.getString("emp_id"));
                            Pref.saveLoginId(context, response.getString("loginId"));

                            String emailId = Pref.getEmpId(context);
                            String loginId = Pref.getLoginId(context);
                            Log.i(TAG, "Pref.getEmail: & Pref.getLoginId: " + emailId + ", " + loginId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "onResponse: " + response);

                        Intent intent = new Intent(context, AddTasksHome.class);
                        session.setLoggedin(true);
                        Log.i(TAG, "session.setLoggedin(true)");
                        String logintime = Pref.saveLoginTime(context, timer());
                        startActivity(intent);
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private String timer() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy  h:mm a");
        String dateString = sdf.format(date);
        return dateString;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Log.i(TAG, "onActivityResult: chacking conditions");
            Uri uri = data.getData();

            try {
                Log.i(TAG, "onActivityResult: getting data");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.iv);
                imageView.setImageBitmap(bitmap);

                SharedPreferences sharedPreferences = getSharedPreferences("RegisterPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("imagePreferance",encodeTobase64(bitmap));
                editor.commit();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("Image Log:", imageEncoded);
        return imageEncoded;
    }
    
    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
