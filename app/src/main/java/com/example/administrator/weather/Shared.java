package com.example.administrator.weather;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences saving data method
 */

public class Shared {
    public final static String SETTING = "Setting";
    public static void putValue(Context con, String key, int value) {
        Editor editor =  con.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putValue(Context con, String key, String value) {
        Editor editor =  con.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static int getValue(Context con, String key, int defValue) {
        SharedPreferences shared =  con.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        int value = shared.getInt(key, defValue);
        return value;
    }
    public static String getValue(Context con, String key, String defValue) {
        SharedPreferences shared =  con.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String value = shared.getString(key, defValue);
        return value;
    }
}