package com.example.zhongzhoujianshe.healthapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class QolSurveyFragment4 extends Fragment {
    private ArrayList<QolSurveyQuestionModel> questionList4 = null;

    private QolSurveyQuestionAdapter mAdapter = null;
    private ListView list_question;
    private TextView txt_title;
    private static final int TYPE_4OPTION = 0;
    private static final int TYPE_7OPTION = 1;

    public QolSurveyFragment4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qol_survey_qustionlist, container, false);
        list_question = (ListView) view.findViewById(R.id.qol_survey_questionList);
        txt_title = (TextView) view.findViewById(R.id.textTitle);
        txt_title.setText(R.string.qol_q31_40);

        //the following data can be read from database
        questionList4 = new ArrayList<QolSurveyQuestionModel>();
        String[] answerOption = {"Not at All","A Little","Quite a Bit","Very Much"};
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q31), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q32), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q33), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q34), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q35), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q36), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q37), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q38), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q39), answerOption));
        questionList4.add(new QolSurveyQuestionModel(getString(R.string.qol_q40), answerOption));
        /**
         * getActivity() in a Fragment returns the Activity the Fragment is currently associated with.
         返回一个和此fragment绑定的FragmentActivity或者其子类的实例。相反，如果此fragment绑定的是一个context的话，怎可能会返回null。
         因为getActivity()大部分都是在fragment中使用到，而fragment需要依赖于activity，所有我们在fragment里头需要做一些动作，比如启动一个activity，就需要拿到activity对象才可以启动，而fragment对象是没有startActivity()方法的。
         */

        mAdapter = new QolSurveyQuestionAdapter((ArrayList<QolSurveyQuestionModel>) questionList4, getContext());
        list_question.setAdapter(mAdapter);


        return view;
    }
}
