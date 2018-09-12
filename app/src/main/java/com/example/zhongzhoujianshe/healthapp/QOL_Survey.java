package com.example.zhongzhoujianshe.healthapp;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QOL_Survey extends AppCompatActivity implements
        ViewPager.OnPageChangeListener {
    //UI Objects
    private ViewPager viewPager;

    private QolSurveyFragmentPagerAdapter qolSurveyAdapter;

    //几个代表页面的常量
    //ssss
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qol__survey);
        qolSurveyAdapter = new QolSurveyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
    }

    private void bindViews() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(qolSurveyAdapter);
        viewPager.setCurrentItem(PAGE_ONE);
        viewPager.addOnPageChangeListener(this);
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_ONE:
                    viewPager.setCurrentItem(PAGE_ONE);
                    break;
                case PAGE_TWO:
                    viewPager.setCurrentItem(PAGE_TWO);
                    break;
            }
        }
    }
}
