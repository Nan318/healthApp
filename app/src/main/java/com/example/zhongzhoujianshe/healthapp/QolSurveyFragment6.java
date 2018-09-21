package com.example.zhongzhoujianshe.healthapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class QolSurveyFragment6 extends Fragment {
    private ArrayList<QolSurveyQuestionModel> questionList6 = null;

    private QolSurveyQuestionAdapter mAdapter = null;
    private ListView list_question;
    private TextView txt_title;
   // private static final int TYPE_4OPTION = 0;
   // private static final int TYPE_7OPTION = 1;

    public QolSurveyFragment6() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qol_survey_qustionlist, container, false);
        list_question = (ListView) view.findViewById(R.id.qol_survey_questionList);
        txt_title = (TextView) view.findViewById(R.id.textTitle);
        txt_title.setText(R.string.qol_q50_51);

        //the following data can be read from database
        questionList6 = new ArrayList<QolSurveyQuestionModel>();
        String[] answerOption = {"1","2","3","4","5","6","7"};
        questionList6.add(new QolSurveyQuestionModel(getString(R.string.qol_q50), answerOption));
        questionList6.add(new QolSurveyQuestionModel(getString(R.string.qol_q51), answerOption));

        mAdapter = new QolSurveyQuestionAdapter((ArrayList<QolSurveyQuestionModel>) questionList6, getContext());
        list_question.setAdapter(mAdapter);


        return view;
    }
}
