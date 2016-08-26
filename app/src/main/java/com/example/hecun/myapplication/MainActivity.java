package com.example.hecun.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fang.flybanner.FlyBanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private List<FlyItem> mList = new ArrayList<>();
    private FlyBanner flyBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flyBanner = (FlyBanner) findViewById(R.id.flyBanner);
        initData();
        flyBanner.setDelayTime(2)
                .setIntervalTime(3)
                .setLeftScroll(true)
                .setStopWhenTouch(true).
                setMargins(100,0,50).
                setIndicator(R.drawable.rb_selector,30, 0,FlyBanner.LEFT)
                .setSource(mList)
                        .startScroll();
    }

    public void initData(){
        mList.add(new FlyItem(Constant.IMAGE_URL_ONE,"第一幅画"));
        mList.add(new FlyItem(Constant.IMAGE_URL_TWO,"第二幅画"));
        mList.add(new FlyItem(Constant.IMAGE_URL_THREE,"第三幅画"));
        mList.add(new FlyItem(Constant.IMAGE_URL_FOUR,"第四幅画"));
    }
}
