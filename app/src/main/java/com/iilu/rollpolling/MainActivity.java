package com.iilu.rollpolling;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.iilu.lib_rollpolling.RollPollingFragment;

public class MainActivity extends AppCompatActivity implements RollPollingFragment.OnRollPollingClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RollPollingFragment rollPollingFragment = (RollPollingFragment) fragmentManager.findFragmentById(R.id.fragment_roll_polling);
        rollPollingFragment.setIndicatorGravity(Gravity.RIGHT | Gravity.BOTTOM)
                .startCarousel(initImgAddressarr(), R.drawable.selector_indicator);
        rollPollingFragment.setOnClickListener(this);
    }

    private String[] initImgAddressarr() {
        String[] imgAddressArr = new String[7];
        imgAddressArr[0] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937748.jpg";
        imgAddressArr[1] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937662.jpg";
        imgAddressArr[2] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[3] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[4] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[5] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[6] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        return imgAddressArr;
    }

    @Override
    public void onRollPollingClick(int position) {
        Toast.makeText(this, position + "", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
