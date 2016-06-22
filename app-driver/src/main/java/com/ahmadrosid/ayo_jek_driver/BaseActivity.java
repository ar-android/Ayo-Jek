package com.ahmadrosid.ayo_jek_driver;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ocittwo on 20/06/16.
 */
public class BaseActivity extends AppCompatActivity{

    private static final String TAG = "BaseActivity";

    public void log(String s) {
        Log.d(TAG, "log: " + s);
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
