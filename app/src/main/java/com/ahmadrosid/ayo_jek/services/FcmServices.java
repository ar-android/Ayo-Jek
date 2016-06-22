package com.ahmadrosid.ayo_jek.services;

import android.util.Log;

import com.ahmadrosid.ayo_jek.model.EventMessage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ocittwo on 6/11/16.
 */
public class FcmServices extends FirebaseMessagingService{

    private static final String TAG = "FcmServices";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        log(remoteMessage.getData().toString());

        String message = remoteMessage.getData().get("message").toString();
        String chanel = remoteMessage.getData().get("chanel").toString();
        EventBus.getDefault().post(new EventMessage(message, chanel));

    }

    /**
     * Logging
     * @param s
     */
    void log(String s){
        Log.d(TAG, "log: " + s);
    }
}
