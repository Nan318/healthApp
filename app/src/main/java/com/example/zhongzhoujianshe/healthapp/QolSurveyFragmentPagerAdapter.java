package com.example.zhongzhoujianshe.healthapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

public class QolSurveyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 6;
    private QolSurveyFragment1 qolSurveyFragment1 = null;
    private QolSurveyFragment2 qolSurveyFragment2 = null;
    private QolSurveyFragment3 qolSurveyFragment3 = null;
    private QolSurveyFragment4 qolSurveyFragment4 = null;
    private QolSurveyFragment5 qolSurveyFragment5 = null;
    private QolSurveyFragment6 qolSurveyFragment6 = null;


    public QolSurveyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        qolSurveyFragment1 = new QolSurveyFragment1();
        qolSurveyFragment2 = new QolSurveyFragment2();
        qolSurveyFragment3 = new QolSurveyFragment3();
        qolSurveyFragment4 = new QolSurveyFragment4();
        qolSurveyFragment5 = new QolSurveyFragment5();
        qolSurveyFragment6 = new QolSurveyFragment6();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
       return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case QolSurvey.PAGE_ONE:
                fragment = qolSurveyFragment1;
                break;
            case QolSurvey.PAGE_TWO:
                fragment = qolSurveyFragment2;
                break;
            case QolSurvey.PAGE_THREE:
                fragment = qolSurveyFragment3;
                break;
            case QolSurvey.PAGE_FOUR:
                fragment = qolSurveyFragment4;
                break;
            case QolSurvey.PAGE_FIVE:
                fragment = qolSurveyFragment5;
                break;
            case QolSurvey.PAGE_SIX:
                fragment = qolSurveyFragment6;
                break;
        }
        return fragment;
    }

}
