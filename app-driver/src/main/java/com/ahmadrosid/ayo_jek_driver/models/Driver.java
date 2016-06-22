package com.ahmadrosid.ayo_jek_driver.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ocittwo on 20/06/16.
 */
@IgnoreExtraProperties
public class Driver {

    public String uid;
    public String fcm_id;
    public String email;
    public String name;
    public String no_hp;
    public String plat;

    public Driver() {
    }

    public Driver(String uid, String fcm_id, String email, String name, String no_hp, String plat) {
        this.uid = uid;
        this.fcm_id = fcm_id;
        this.email = email;
        this.name = name;
        this.no_hp = no_hp;
        this.plat = plat;
    }

    @Exclude
    public Map toMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("fcm_id", fcm_id);
        data.put("email", email);
        data.put("name", name);
        data.put("no_hp", no_hp);
        data.put("plat", plat);

        return data;
    }
}
