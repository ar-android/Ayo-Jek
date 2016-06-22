package com.ahmadrosid.ayo_jek_driver.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ahmadrosid.ayo_jek_driver.BaseActivity;
import com.ahmadrosid.ayo_jek_driver.R;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity : ";

    private ImageView img_order;
    private ImageView img_profile;
    private ImageView img_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        castView();
    }

    private void castView(){
        img_order = (ImageView) findViewById(R.id.img_order);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_setting = (ImageView) findViewById(R.id.img_setting);

        setTouch(img_order, R.drawable.ic_order);
        setTouch(img_profile, R.drawable.ic_profile);
        setTouch(img_setting, R.drawable.ic_setting);
    }

    private void setTouch(final ImageView img, int bitmap){
        final Bitmap bitmapButtonMain = BitmapFactory.decodeResource(this.getResources(), bitmap);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bindImage(event, img, bitmapButtonMain);
                return true;
            }
        });
    }

    public void bindImage(MotionEvent event, ImageView img, Bitmap bitmapImage) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            Bitmap resized = Bitmap.createScaledBitmap(bitmapImage,
                    (int) (bitmapImage.getWidth() * 0.9),
                    (int) (bitmapImage.getHeight() * 0.9), true);
            img.setImageBitmap(resized);
        } else {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Bitmap resized = Bitmap.createScaledBitmap(bitmapImage,
                        bitmapImage.getWidth(),
                        bitmapImage.getHeight(), true);
                img.setImageBitmap(resized);

                switch (img.getId()) {
                    case R.id.img_order:
                        log("Img order touching");
                        break;
                    case R.id.img_profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        log("Img profile touching");
                        break;
                    case R.id.img_setting:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        log("Img pengaturan touching");
                        break;
                }
            }
        }
    }
}
