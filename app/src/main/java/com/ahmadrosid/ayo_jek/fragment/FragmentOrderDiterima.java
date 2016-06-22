package com.ahmadrosid.ayo_jek.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ahmadrosid.ayo_jek.R;

/**
 * Created by ocittwo on 22/06/16.
 */
public class FragmentOrderDiterima extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "FragmentOrderDiterima";

    private Button profile_ojek;
    private Button track_ojek;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diterima, container, false);
        profile_ojek = (Button) view.findViewById(R.id.profile_ojek);
        track_ojek = (Button) view.findViewById(R.id.track_ojek);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profile_ojek.setOnClickListener(this);
        track_ojek.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_ojek:
                break;
            case R.id.track_ojek:
                break;
        }
    }
}
