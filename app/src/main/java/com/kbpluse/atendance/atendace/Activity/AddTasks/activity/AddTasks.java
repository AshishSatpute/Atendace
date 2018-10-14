package com.kbpluse.atendance.atendace.Activity.AddTasks.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kbpluse.atendance.atendace.Activity.AddTasks.dataModel.AddTasksDataModel;
import com.kbpluse.atendance.atendace.Activity.AddTasks.database.AddTasksDatabaseSource;
import com.kbpluse.atendance.atendace.Activity.SharedPref.Pref;
import com.kbpluse.atendance.atendace.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;

public class AddTasks extends AppCompatActivity {
    private String TAG = AddTasks.class.getSimpleName();
    private EditText etTitle;
    private EditText etMessage;
    private Context context;
    private Button btnSave;
    private Button btnUpdate;
    private AddTasksDataModel addTasksDataModel;
    public static final String url = "http://empattendance.kbplussoftwaresystem.com/saveEmpTask.html";
    //public static final String url = "http://192.168.1.10:8080/Emp_Attendance/saveEmpTask.html";

    public AddTasks() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        context = AddTasks.this;
        Log.e(TAG, "onCreate: ");
        initControls();
        getIntentValues();
    }

    private void getIntentValues() {
        Log.e(TAG, "getIntentValues: ");
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("obj");
        addTasksDataModel = gson.fromJson(strObj, AddTasksDataModel.class);
        if (addTasksDataModel != null) {
            etTitle.setText(addTasksDataModel.getTasksName());
            etMessage.setText(addTasksDataModel.getTasksDecription());
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);

        }
    }

    private void initControls() {
        Log.e(TAG, "initControls: ");
        etTitle = findViewById(R.id.etTitle);
        etMessage = findViewById(R.id.etMessage);
        btnSave = findViewById(R.id.btnSave);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: ");
                if (validateForm(etTitle.getText().toString(), etMessage.toString())) {
                   addTask();
                }
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: ");
                if (validateForm(etTitle.getText().toString(), etMessage.toString())) {
                    addTasksDataModel.setTasksName(etTitle.getText().toString());
                    addTasksDataModel.setTasksDecription(etMessage.getText().toString());
                    AddTasksDatabaseSource addTasksDatabaseSource = new AddTasksDatabaseSource(context);
                    addTasksDatabaseSource.updateNote(addTasksDataModel);
                    clearControls();
                }
            }
        });
    }

    private void addTask() {

        AddTasksDataModel addTasksDataModel = new AddTasksDataModel();
        addTasksDataModel.setTasksName(etTitle.getText().toString());
        addTasksDataModel.setTasksDecription(etMessage.getText().toString());

        Map<String, String> params = new HashMap();
        params.put("task_title",addTasksDataModel.getTasksName());
        params.put("description",addTasksDataModel.getTasksDecription());
        params.put("emp_id", Pref.getEmpId(context));


        Log.i(TAG, "onClick: task title"+addTasksDataModel.getTasksName());
        Log.i(TAG, "onClick: task desc"+addTasksDataModel.getTasksDecription());
        Log.i(TAG, "onClick: task desc"+Pref.getEmpId(context));


        JSONObject parameters = new JSONObject(params);

        Log.i(TAG, "onClick: parameters"+parameters);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        /*RegisterDomain registerDomain1 = new Gson().fromJson(String.valueOf(response), new TypeToken<RegisterDomain>() {
                        }.getType());

                        Log.i(TAG, "onResponse: "+registerDomain1);*/
                        Log.i(TAG, "onResponse: "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);


        AddTasksDatabaseSource addTasksDatabaseSource = new AddTasksDatabaseSource(context);
        addTasksDatabaseSource.insertNote(addTasksDataModel);
        clearControls();
        startActivity(new Intent(context,AddTasksHome.class));
    }

    private void clearControls() {
        Log.e(TAG, "clearControls: ");
        etTitle.setText("");
        etMessage.setText("");
        etTitle.requestFocus();
    }

    private boolean validateForm(String title, String message) {
        Log.e(TAG, "validateForm: ");
        if (title.isEmpty()) {
            Toast.makeText(context, "Please enter Tasks first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (message.isEmpty()) {
            Toast.makeText(context, "Please enter Decripation first", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,AddTasksHome.class));
    }
}
