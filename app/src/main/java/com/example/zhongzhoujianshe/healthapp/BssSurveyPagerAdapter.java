package com.example.zhongzhoujianshe.healthapp;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class BssSurveyPagerAdapter extends PagerAdapter {
    private List<ImageView> imageViewList;
    public BssSurveyPagerAdapter(List<ImageView> imageViewList) {
        super();
        this.imageViewList = imageViewList;
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;  //so that users cannot see the border
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //container.addView(imageViewList.get(position));
        //return imageViewList.get(position);
        //container.addView(imageViewList.get(position % imageViewList.size()), 0);
        //return imageViewList.get(position % imageViewList.size());
        //对ViewPager页号求模取出View列表中要显示的项  
        position %= imageViewList.size();
        if(position<0){
            position = imageViewList.size()+position;
        }
        ImageView view = imageViewList.get(position);

        //avoid IllegalStateException
        //i.e. if the specified child already has a parent. You must call removeView() on the child's parent first.
        ViewParent vp =view.getParent();
        if(vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position % imageViewList.size()));

        //container.removeView(imageViewList.get(position));
    }
}
