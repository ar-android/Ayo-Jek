package com.ahmadrosid.ayo_jek.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ocittwo on 6/18/16.
 */
public class PreferenceHelper {

    private static final String TAG = "PreferenceHelper";

    private final Context context;
    private SharedPreferences sharedpreferences;

    /**
     * Instance Helper SharedPreferences
     * @param context
     */
    public PreferenceHelper(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(Constans.DATA_USER, 0);
    }

    /**
     * Save data profile to sharedpreferences
     * @param full_name
     * @param no_hp
     * @param email
     * @param uid
     * @param fcm_id
     */
    public void saveDataProfile(String full_name, String no_hp, String email, String uid, String fcm_id){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constans.FULL_NAME, full_name);
        editor.putString(Constans.NO_HP, no_hp);
        editor.putString(Constans.EMAIL, email);
        editor.putString(Constans.UID, uid);
        editor.putString(Constans.FCM_ID, fcm_id);
        editor.apply();
        log("Data profile have been saved!");
    }

    /**
     * Get data profile
     */
    public Map<String, Object> getDataProfile(){
        SharedPreferences data = context.getSharedPreferences(Constans.DATA_USER, 0);
        Map<String, Object> profile = new HashMap<>();
        profile.put(Constans.FULL_NAME, data.getString(Constans.FULL_NAME, ""));
        profile.put(Constans.NO_HP, data.getString(Constans.NO_HP, ""));
        profile.put(Constans.EMAIL, data.getString(Constans.EMAIL, ""));
        profile.put(Constans.UID, data.getString(Constans.UID, ""));
        profile.put(Constans.FCM_ID, data.getString(Constans.FCM_ID, ""));
        log("GET data executed!");
        return profile;
    }

    /**
     * Logging
     * @param s
     */
    private void log(String s){
        Log.d(TAG, "log: " + s);
    }
}
