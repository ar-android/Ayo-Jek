package com.ahmadrosid.ayo_jek.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ahmadrosid.ayo_jek.BaseActivity;
import com.ahmadrosid.ayo_jek.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), SignActivity.class));
            }
        }, 3000);
    }
}
