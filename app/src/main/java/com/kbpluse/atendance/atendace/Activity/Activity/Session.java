package com.kbpluse.atendance.atendace.Activity.Activity;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context cx;

    public Session(Context cx) {
        this.cx = cx;
        prefs = cx.getSharedPreferences("session", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(Boolean loggedin) {
        editor.putBoolean("loggedinmode", loggedin);
        editor.commit();
    }

    public Boolean loggedin() {
        return prefs.getBoolean("loggedinmode", false);
    }

    public void setRegisterDone(Boolean register ){
        editor.putBoolean("registermode",register);
        editor.commit();

    }

    public  Boolean register(){
        return prefs.getBoolean("registermode",false);
    }
}
