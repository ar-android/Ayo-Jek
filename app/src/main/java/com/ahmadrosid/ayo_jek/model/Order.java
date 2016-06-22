package com.ahmadrosid.ayo_jek.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ocittwo on 22/06/16.
 */
@IgnoreExtraProperties
public class Order {

    public String location_name_origin;
    public String location_name_destination;
    public String latitude_origin;
    public String longitude_origin;
    public String latitude_destination;
    public String longitude_destination;
    public String uid_user;
    public String email_user;

    public Order() {
    }

    public Order(String location_name_origin, String location_name_destination, String latitude_origin, String longitude_origin, String latitude_destination, String longitude_destination, String uid_user, String email_user) {
        this.location_name_origin = location_name_origin;
        this.location_name_destination = location_name_destination;
        this.latitude_origin = latitude_origin;
        this.longitude_origin = longitude_origin;
        this.latitude_destination = latitude_destination;
        this.longitude_destination = longitude_destination;
        this.uid_user = uid_user;
        this.email_user = email_user;
    }

    @Exclude
    public Map toMap(){
        Map<String, String> data = new HashMap<>();
        data.put("location_name_origin", location_name_origin);
        data.put("location_name_destination", location_name_destination);
        data.put("latitude_origin", latitude_origin);
        data.put("longitude_origin", longitude_origin);
        data.put("latitude_destination", latitude_destination);
        data.put("longitude_destination", longitude_destination);
        data.put("uid_user", uid_user);
        data.put("email_user", email_user);
        return data;
    }
}
