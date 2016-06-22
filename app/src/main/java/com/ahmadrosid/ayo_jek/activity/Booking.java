package com.ahmadrosid.ayo_jek.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ahmadrosid.ayo_jek.BaseActivity;
import com.ahmadrosid.ayo_jek.R;
import com.ahmadrosid.ayo_jek.fragment.FragmentTungguOrder;
import com.ahmadrosid.ayo_jek.helper.Constans;
import com.ahmadrosid.ayo_jek.helper.PreferenceHelper;
import com.ahmadrosid.ayo_jek.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Booking extends BaseActivity {

    private static final String TAG = "Booking : ";

    private String latitude_origin;
    private String longitude_origin;
    private String latitude_destination;
    private String longitude_destination;
    private String location_name_origin;
    private String location_name_destination;
    private DatabaseReference mDatabase;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        view = findViewById(android.R.id.content);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        latitude_origin = getIntent().getStringExtra(Constans.LATITUDE_ORIGIN);
        longitude_origin = getIntent().getStringExtra(Constans.LONGITUDE_ORIGIN);
        latitude_destination = getIntent().getStringExtra(Constans.LATITUDE_DESTINATION);
        longitude_destination = getIntent().getStringExtra(Constans.LONGITUDE_DESTINATION);
        location_name_origin = getIntent().getStringExtra(Constans.LOCATION_NAME_ORIGIN);
        location_name_destination = getIntent().getStringExtra(Constans.LOCATION_NAME_DESTINATION);

        writeNewOrder();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new FragmentTungguOrder())
                .commit();
    }

    /**
     * Write new order
     */
    private void writeNewOrder(){
        PreferenceHelper preferenceHelper = new PreferenceHelper(getApplicationContext());
        Map<String, Object> data = preferenceHelper.getDataProfile();
        String uid_user = getUid();
        String email_user = data.get(Constans.EMAIL).toString();
        String name = data.get(Constans.FULL_NAME).toString();
        sendNotificationOrders(name, location_name_origin);
        // Write on model
        Order order = new Order(
                location_name_origin,
                location_name_destination,
                latitude_origin,
                longitude_origin,
                latitude_destination,
                longitude_destination,
                uid_user,
                email_user);

        // Save to database
        mDatabase.child("orders").child(uid_user).setValue(order.toMap());

        log("Write new user executed!");
    }

    private void sendNotificationOrders(String name, String origin){
        OkHttpClient client = new OkHttpClient();
        String json = "{\n  \"to\": \"/topics/order\",\n  \"data\": {\n    \"title\": \"" + name + "\",\n    \"origin\":\"" + origin + "\"}\n}";
        log(json);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "key=AIzaSyC0OrOeHf3On17SYbjhvUOzlL0p7gSA_HY")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "3504e0d5-8f50-3cdf-e0d6-40aef920b4ed")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            log(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Snackbar snack = Snackbar.make(view, "Anda Sedang Melakukan Pemesanan!", Snackbar.LENGTH_LONG);

        snack.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public boolean canDisplaySnackbar;

            @Override
            public void onViewAttachedToWindow(View v) {
                canDisplaySnackbar = false;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        canDisplaySnackbar = false;
                    }
                }.start();
            }
        });
        snack.show();
    }
}
