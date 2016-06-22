package com.ahmadrosid.ayo_jek.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ocittwo on 6/11/16.
 */
@IgnoreExtraProperties
public class User {

    public String full_name;
    public String no_hp;
    public String email;
    public String uid;
    public String fcm_id;

    public User() {
    }

    public User(String full_name, String no_hp, String email, String uid, String fcm_id) {
        this.full_name = full_name;
        this.no_hp = no_hp;
        this.email = email;
        this.uid = uid;
        this.fcm_id = fcm_id;
    }

    @Exclude
    public Map toMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("full_name", full_name);
        data.put("no_hp", no_hp);
        data.put("email", email);
        data.put("uid", uid);
        data.put("fcm_id", fcm_id);

        return data;
    }
}
