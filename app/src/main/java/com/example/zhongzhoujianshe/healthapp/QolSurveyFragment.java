package com.example.zhongzhoujianshe.healthapp;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class QolSurveyFragment extends Fragment {

    private ArrayList<QolSurveyQuestionModel> questionList = null;

    private QolSurveyQuestionAdapter mAdapter = null;
    private String title_qRange;
    private ListView list_question;
    private TextView txt_title;
    private QolSurveyAnswerModel allResult;


    public QolSurveyFragment() {
        // Required empty public constructor
    }

    // newInstance constructor for creating fragment with arguments
    public static QolSurveyFragment newInstance(String qRange, ArrayList<QolSurveyQuestionModel> qList, QolSurveyAnswerModel result) {
        QolSurveyFragment fragment = new QolSurveyFragment();
        Bundle args = new Bundle();
        args.putString("range", qRange);
        args.putParcelableArrayList("qList", qList);
        args.putParcelable("result", result);
        fragment.setArguments(args);
        return fragment;
    }
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_qRange = getArguments().getString("range");
        questionList = getArguments().getParcelableArrayList("qList");
        allResult = getArguments().getParcelable("result");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qol_survey_qustionlist, container, false);
        list_question = (ListView) view.findViewById(R.id.qol_survey_questionList);
        txt_title = (TextView) view.findViewById(R.id.textTitle);
        txt_title.setText(title_qRange);

        //the following data can be read from database
        //questionList = new ArrayList<QolSurveyQuestionModel>();

        /**
         * getActivity() in a Fragment returns the Activity the Fragment is currently associated with.
         返回一个和此fragment绑定的FragmentActivity或者其子类的实例。相反，如果此fragment绑定的是一个context的话，怎可能会返回null。
         因为getActivity()大部分都是在fragment中使用到，而fragment需要依赖于activity，所有我们在fragment里头需要做一些动作，比如启动一个activity，就需要拿到activity对象才可以启动，而fragment对象是没有startActivity()方法的。
         */

        mAdapter = new QolSurveyQuestionAdapter((ArrayList<QolSurveyQuestionModel>) questionList, getContext(), allResult);
        list_question.setAdapter(mAdapter);


        return view;
    }
}
