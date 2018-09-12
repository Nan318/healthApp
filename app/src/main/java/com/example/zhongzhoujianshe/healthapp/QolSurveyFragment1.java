package com.example.zhongzhoujianshe.healthapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class QolSurveyFragment1 extends Fragment {
    public QolSurveyFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qol_survey_layout1, container, false);
        return view;
    }
}
