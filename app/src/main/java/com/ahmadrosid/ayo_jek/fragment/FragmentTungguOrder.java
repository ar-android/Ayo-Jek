package com.ahmadrosid.ayo_jek.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmadrosid.ayo_jek.R;
import com.ahmadrosid.ayo_jek.model.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ocittwo on 22/06/16.
 */
public class FragmentTungguOrder extends BaseFragment{

    private static final String TAG = "FragmentTungguOrder : ";
    private TextView text_one;
    private CountDownTimer count_down;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tunggu, container, false);
        text_one = (TextView)v.findViewById(R.id.text_one);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        count_down = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                text_one.setText(" " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                text_one.setText("0");
                getActivity().finish();
            }
        }.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage event){
        if (event.message.equals("order diterima")){
            if (count_down != null)
                count_down.cancel();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new FragmentOrderDiterima())
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
