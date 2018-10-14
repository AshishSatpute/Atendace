package com.kbpluse.atendance.atendace.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.kbpluse.atendance.atendace.R;

public class SplashActivity extends AppCompatActivity {


    private Context context;
    private String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_splash_screen);
        context = SplashActivity.this;
        Log.i(TAG, "onCreate: ");
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Log.i(TAG, "run: Ok");
                    Intent intent = new Intent(context, Registration.class);
                    Log.i(TAG, "Registration.class");
                    startActivity(intent);
                    finish();
                }
            }
        };

        timerThread.start();

    }
}
