package com.kbpluse.atendance.atendace.Activity.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    /*SharedPreferences sharedPreferences;

    void setLoginDetails() {

        var sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_STORE_USER_LOGIN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(EMAILID, emailId).apply()
        editor.putString(USER_NAME, userName).apply()
        editor.putString(MOBILENO, mobileNo).apply()
        editor.putString(PASS,pass).apply()
    }*/


    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", null);
    }

    public static String getPass(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("pass", null);
    }

    public static String getEmpId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("empId", null);
    }


    public static String getLoginId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("loginId", null);
    }


    public static void saveEmpId(Context context, String empId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("empId",empId).apply();
    }

    public static void saveLoginId(Context context, String loginId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginId",loginId).apply();
    }

    public static void saveLoginDone(Context context, String loginId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginDone",loginId).apply();
    }

    public static String getLoginDone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("loginDone", null);
    }

    public static String saveLoginTime(Context context, String loginTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginTime",loginTime).apply();
        return loginTime;
    }

    public static String getLoginTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("loginTime", null);
    }


}
