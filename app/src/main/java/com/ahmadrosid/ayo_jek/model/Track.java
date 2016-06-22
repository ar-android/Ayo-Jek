package com.ahmadrosid.ayo_jek.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ocittwo on 22/06/16.
 */
@IgnoreExtraProperties
public class Track {
    public String chanel;
    public double latitude;
    public double longitude;

    public Track() {
    }

    public Track(String chanel, double latitude, double longitude) {
        this.chanel = chanel;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
