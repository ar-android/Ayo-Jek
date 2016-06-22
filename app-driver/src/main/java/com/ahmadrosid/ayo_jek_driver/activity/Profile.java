package com.ahmadrosid.ayo_jek_driver.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ahmadrosid.ayo_jek_driver.BaseActivity;
import com.ahmadrosid.ayo_jek_driver.R;
import com.ahmadrosid.ayo_jek_driver.helper.Constans;
import com.ahmadrosid.ayo_jek_driver.helper.PrefHelper;

import java.util.Map;

/**
 * Created by ocittwo on 20/06/16.
 */
public class Profile extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Profile : ";

    private TextView driver_name;
    private TextView driver_no_hp;
    private TextView driver_plat;
    private TextView driver_email;
    private Button btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        castView();
    }

    private void castView() {
        driver_name = (TextView) findViewById(R.id.driver_name);
        driver_no_hp = (TextView) findViewById(R.id.driver_no_hp);
        driver_plat = (TextView) findViewById(R.id.driver_plat);
        driver_email = (TextView) findViewById(R.id.driver_email);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        loadDataDriver();
    }

    private  void loadDataDriver(){
        PrefHelper prefHelper = new PrefHelper(this);
        Map<String, Object> data = prefHelper.getDataDriver();
        driver_name.setText(data.get(Constans.NAME).toString());
        driver_no_hp.setText(data.get(Constans.NO_HP).toString());
        driver_plat.setText(data.get(Constans.PLAT).toString());
        driver_email.setText(data.get(Constans.EMAIL).toString());

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
            case R.id.btn_back:
                onBackPressed();
                finish();
                break;
        }
    }
}
