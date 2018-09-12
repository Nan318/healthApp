package com.example.zhongzhoujianshe.healthapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QolSurveyFragment2 extends Fragment {
    public QolSurveyFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qol_survey_layout1,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.textQ1_10);
        txt_content.setText(getString(R.string.qol_q11_20));
        TextView q11 = (TextView) view.findViewById(R.id.q1);
        q11.setText(getString(R.string.qol_q11));

        return view;
    }
}
