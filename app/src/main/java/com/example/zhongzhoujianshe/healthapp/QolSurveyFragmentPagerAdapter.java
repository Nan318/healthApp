package com.example.zhongzhoujianshe.healthapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.support.v4.app.FragmentManager;

public class QolSurveyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 2;
    private QolSurveyFragment1 qolSurveyFragment1 = null;
    private QolSurveyFragment2 qolSurveyFragment2 = null;


    public QolSurveyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        qolSurveyFragment1 = new QolSurveyFragment1();
        qolSurveyFragment2 = new QolSurveyFragment2();
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
            case QOL_Survey.PAGE_ONE:
                fragment = qolSurveyFragment1;
                break;
            case QOL_Survey.PAGE_TWO:
                fragment = qolSurveyFragment2;
                break;
        }
        return fragment;
    }

}
