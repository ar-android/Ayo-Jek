package com.ahmadrosid.ayo_jek_driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ahmadrosid.ayo_jek_driver.BaseActivity;
import com.ahmadrosid.ayo_jek_driver.R;

/**
 * Created by ocittwo on 20/06/16.
 */
public class SplashScreen extends BaseActivity{

    private static final String TAG = "SplashScreen : ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        ImageView ivLoading = (ImageView) findViewById(R.id.ivLoading);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_rotate_imageview);
        if (ivLoading != null) {
            animation.setRepeatMode(Animation.INFINITE);
            ivLoading.startAnimation(animation);
        }else{
            Log.d(TAG, "onCreate: loading image null");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Sign.class));
                finish();
            }
        }, 3000);

    }
}
