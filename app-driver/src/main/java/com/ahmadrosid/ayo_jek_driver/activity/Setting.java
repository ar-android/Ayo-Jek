package com.ahmadrosid.ayo_jek_driver.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ahmadrosid.ayo_jek_driver.BaseActivity;
import com.ahmadrosid.ayo_jek_driver.R;
import com.google.firebase.auth.FirebaseAuth;

public class Setting extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Setting : ";

    private TextView breaks;
    private TextView log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        castView();

    }

    private void castView() {
        breaks = (TextView) findViewById(R.id.t_break);
        log_out = (TextView) findViewById(R.id.t_logout);
        breaks.setOnClickListener(this);
        log_out.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();;
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_break:
                log(TAG + "Break clicked");
                break;
            case R.id.t_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
    }
}
