package com.ahmadrosid.ayo_jek.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ahmadrosid.ayo_jek.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ocittwo on 22/06/16.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    public void log(String s) {
        Log.d(TAG, "log: " + s);
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
