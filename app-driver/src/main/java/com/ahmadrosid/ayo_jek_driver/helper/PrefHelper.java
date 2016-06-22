package com.ahmadrosid.ayo_jek_driver.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ocittwo on 20/06/16.
 */
public class PrefHelper {

    private static final String TAG = "PrefHelper";

    private final Context context;
    private SharedPreferences sharedpreferences;

    /***
     * New instance clas prefhelper
     * @param context
     */
    public PrefHelper(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(Constans.DATA_DRIVER, 0);
    }

    /***
     * Save data driver on local shared preference
     * @param uid
     * @param fcm_id
     * @param email
     * @param name
     * @param no_hp
     */
    public void saveDataDriver(String uid, String fcm_id, String email, String name, String no_hp, String plat){
        SharedPreferences.Editor driver = sharedpreferences.edit();
        driver.putString(Constans.UID, uid);
        driver.putString(Constans.FCM_ID, fcm_id);
        driver.putString(Constans.EMAIL, email);
        driver.putString(Constans.NAME, name);
        driver.putString(Constans.NO_HP, no_hp);
        driver.putString(Constans.PLAT, plat);
        driver.apply();
    }

    /**
     * Get Data driver from shared preferences
     * @return Map<String, Object>
     */
    public Map<String, Object> getDataDriver(){
        SharedPreferences data = context.getSharedPreferences(Constans.DATA_DRIVER, 0);
        Map<String, Object> profile = new HashMap<>();
        profile.put(Constans.UID, data.getString(Constans.UID, ""));
        profile.put(Constans.FCM_ID, data.getString(Constans.FCM_ID, ""));
        profile.put(Constans.EMAIL, data.getString(Constans.EMAIL, ""));
        profile.put(Constans.NAME, data.getString(Constans.NAME, ""));
        profile.put(Constans.NO_HP, data.getString(Constans.NO_HP, ""));
        profile.put(Constans.PLAT, data.getString(Constans.PLAT, ""));

        return profile;
    }

}
