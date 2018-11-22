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
        rollPollingFragment.setImgHeight(160) // set img height, default 150dp
                .setImgMargin(8) // set img left and right margin, default 10dp
                .setCornerRadius(4) // set img round corner radius, default 5dp，if need not round corner, can set 0
                .setIndicatorPadding(4) // set indicator padding, default 5dp
                .setIndicatorGravity(Gravity.RIGHT | Gravity.BOTTOM) // set indicator position, default Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                .setRollPollingPeriod(3800) // set roll polling period, unit is ms, default 3000
                .setStartPosition(2) // set start roll polling position, default 1
                .startRollPolling(initImgAddressarr(), R.drawable.selector_indicator);
        rollPollingFragment.setOnRollPollingClickListener(this); // set click picture callback
    }

    private String[] initImgAddressarr() {
        String[] imgAddressArr = new String[10];
        imgAddressArr[0] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937748.jpg";
        imgAddressArr[1] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937662.jpg";
        imgAddressArr[2] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[3] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937748.jpg";
        imgAddressArr[4] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937662.jpg";
        imgAddressArr[5] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[6] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937748.jpg";
        imgAddressArr[7] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937662.jpg";
        imgAddressArr[8] = "https://goss.veer.com/creative/vcg/veer/800water/veer-324281040.jpg";
        imgAddressArr[9] = "https://goss.veer.com/creative/vcg/veer/800water/veer-306937662.jpg";
        return imgAddressArr;
    }

    @Override
    public void onRollPollingClick(int position) {
        // position begin 1，not 0
        Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
