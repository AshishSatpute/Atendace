package com.kbpluse.atendance.atendace.Activity.AddTasks.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kbpluse.atendance.atendace.Activity.Activity.LoginActivity;
import com.kbpluse.atendance.atendace.Activity.AddTasks.adapters.TasksAdapter;
import com.kbpluse.atendance.atendace.Activity.AddTasks.dataModel.AddTasksDataModel;
import com.kbpluse.atendance.atendace.Activity.AddTasks.database.AddTasksDatabaseSource;
import com.kbpluse.atendance.atendace.Activity.AddTasks.listeners.OnNoteClickListener;
import com.kbpluse.atendance.atendace.Activity.Activity.Session;
import com.kbpluse.atendance.atendace.Activity.SharedPref.Pref;
import com.kbpluse.atendance.atendace.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddTasksHome extends AppCompatActivity implements OnNoteClickListener {
    private String TAG = AddTasksHome.class.getSimpleName();
    private Context context;
    private RecyclerView recyclerViewForNotesList;
    private ArrayList<AddTasksDataModel> addTasksDataModelArrayList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Button btnlogout;
    public static final String URL = "http://empattendance.kbplussoftwaresystem.com/logOut.html";
    TextView tvTime;
    Session session;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks_home);
        context = AddTasksHome.this;
        tvTime = findViewById(R.id.inTime);
        tvTime.setText(Pref.getLoginTime(context));
        btnlogout = findViewById(R.id.logout);
        imageView = findViewById(R.id.profilePic);

        SharedPreferences shared = getSharedPreferences("RegisterPref", MODE_PRIVATE);
        String photo = shared.getString("imagePreferance", null);
        if (photo != null) {
            Bitmap bm = decodeBase64(photo);
            imageView.setImageBitmap(bm);
        }


        session = new Session(this);
        if (!session.loggedin()) {
            Log.i(TAG, "!session.loggedin()");
            logout();
        }


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        initControls();
        refreshNotesList();

    }

    private void refreshNotesList() {
        Log.e(TAG, "refreshNotesList: ");
        AddTasksDatabaseSource addTasksDatabaseSource = new AddTasksDatabaseSource(context);
        addTasksDataModelArrayList = new ArrayList<>();
        addTasksDataModelArrayList = addTasksDatabaseSource.getAllNotes();
        TasksAdapter tasksAdapter = new TasksAdapter(this, addTasksDataModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewForNotesList.setLayoutManager(mLayoutManager);
        recyclerViewForNotesList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewForNotesList.setAdapter(tasksAdapter);
    }

    private void initControls() {
        Log.e(TAG, "initControls: ");
        recyclerViewForNotesList = findViewById(R.id.recyclerViewForNotesList);
        btnlogout = findViewById(R.id.logout);
        FloatingActionButton fabForAddNewItem = findViewById(R.id.fabForAddNewItem);
        fabForAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: ");
                Intent intent = new Intent(context, AddTasks.class);
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutNotesRecycler);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh: ");
                refreshNotesList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    @Override
    public void onNoteClick(int position) {
        Log.e(TAG, "onNoteClick: ");
        Gson gson = new Gson();
        Intent intent = new Intent(context, AddTasks.class);
        intent.putExtra("obj", gson.toJson(addTasksDataModelArrayList.get(position)));
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(int position) {
        Log.e(TAG, "onNoteLongClick: ");
        Toast.makeText(context, "Coming Soon.....", Toast.LENGTH_SHORT).show();
    }

    public void logout() {

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy  h:mm a");
        String dateString = sdf.format(date);

        Map<String, String> params = new HashMap();
        params.put("emp_id", Pref.getEmpId(context));
        params.put("login_id", Pref.getLoginId(context));
        params.put("logout_time", dateString);

        Log.i(TAG, "logout: " + Pref.getEmpId(context));
        Log.i(TAG, "logout: " + Pref.getLoginId(context));
        Log.i(TAG, "logout: " + dateString);


        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context,
                                "logout success", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,
                                "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "logout fail", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onErrorResponse: " + error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


        Intent intent = new Intent(this, LoginActivity.class);
        this.deleteDatabase("AddTasksHome.db");

        SharedPreferences sharedPreferences = getSharedPreferences("RegisterPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("loginDone");
        editor.remove("loginTime");
        editor.commit();
        Log.i(TAG, "logout123: " + sharedPreferences.getString("loginDone", null));
        Log.i(TAG, "logout123: " + sharedPreferences.getString("loginTime", null));
        session.setLoggedin(false);
        Log.i(TAG, "session.setLoggedin(false)");
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStop:
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }


    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
