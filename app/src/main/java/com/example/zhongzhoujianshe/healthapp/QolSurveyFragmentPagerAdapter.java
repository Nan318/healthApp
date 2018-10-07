package com.example.zhongzhoujianshe.healthapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;

public class QolSurveyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final int PAGER_COUNT = 6;
    String[] answerOption4 = {"Not at All","A Little","Quite a Bit","Very Much"};
    String[] answerOption7 = {"1","2","3","4","5","6","7"};
    private String fragment1_title;
    private String fragment2_title;
    private String fragment3_title;
    private String fragment4_title;
    private String fragment5_title;
    private String fragment6_title;
    private ArrayList<QolSurveyQuestionModel> questionList1 = new ArrayList<QolSurveyQuestionModel>();
    private ArrayList<QolSurveyQuestionModel> questionList2 = new ArrayList<QolSurveyQuestionModel>();
    private ArrayList<QolSurveyQuestionModel> questionList3 = new ArrayList<QolSurveyQuestionModel>();
    private ArrayList<QolSurveyQuestionModel> questionList4 = new ArrayList<QolSurveyQuestionModel>();
    private ArrayList<QolSurveyQuestionModel> questionList5 = new ArrayList<QolSurveyQuestionModel>();
    private ArrayList<QolSurveyQuestionModel> questionList6 = new ArrayList<QolSurveyQuestionModel>();
    private QolSurveyAnswerModel result;

    public QolSurveyFragmentPagerAdapter(FragmentManager fm, Context context, QolSurveyAnswerModel result) {
        super(fm);
        this.mContext = context;
        this.result = result;
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
        switch (position) {
            case 0: // Fragment # 0 - This will show the first page
                return QolSurveyFragment.newInstance(fragment1_title, questionList1, result);
            case 1: // Fragment # 1 - This will show the second page
                return QolSurveyFragment.newInstance(fragment2_title, questionList2, result);
            case 2: // Fragment # 2 - This will show the third page
                return QolSurveyFragment.newInstance(fragment3_title, questionList3, result);
            case 3: // Fragment # 3 - This will show the fourth page
                return QolSurveyFragment.newInstance(fragment4_title, questionList4, result);
            case 4: // Fragment # 4 - This will show the fifth page
                return QolSurveyFragment.newInstance(fragment5_title, questionList5, result);
            case 5: // Fragment # 5 - This will show the sixth page
                return QolSurveyFragment.newInstance(fragment6_title, questionList6, result);
            default:
                return null;
        }

    }

    public void setData(){
        fragment1_title = mContext.getString(R.string.qol_q1_10);
        questionList1.add(new QolSurveyQuestionModel(1, mContext.getString(R.string.qol_q1), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(2, mContext.getString(R.string.qol_q2), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(3, mContext.getString(R.string.qol_q3), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(4, mContext.getString(R.string.qol_q4), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(5, mContext.getString(R.string.qol_q5), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(6, mContext.getString(R.string.qol_q6), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(7, mContext.getString(R.string.qol_q7), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(8, mContext.getString(R.string.qol_q8), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(9, mContext.getString(R.string.qol_q9), answerOption4));
        questionList1.add(new QolSurveyQuestionModel(10, mContext.getString(R.string.qol_q10), answerOption4));
        fragment2_title = mContext.getString(R.string.qol_q11_20);
        questionList2.add(new QolSurveyQuestionModel(11, mContext.getString(R.string.qol_q11), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(12, mContext.getString(R.string.qol_q12), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(13, mContext.getString(R.string.qol_q13), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(14, mContext.getString(R.string.qol_q14), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(15, mContext.getString(R.string.qol_q15), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(16, mContext.getString(R.string.qol_q16), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(17, mContext.getString(R.string.qol_q17), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(18, mContext.getString(R.string.qol_q18), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(19, mContext.getString(R.string.qol_q19), answerOption4));
        questionList2.add(new QolSurveyQuestionModel(20, mContext.getString(R.string.qol_q20), answerOption4));
        fragment3_title = mContext.getString(R.string.qol_q21_30);
        questionList3.add(new QolSurveyQuestionModel(21, mContext.getString(R.string.qol_q21), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(22, mContext.getString(R.string.qol_q22), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(23, mContext.getString(R.string.qol_q23), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(24, mContext.getString(R.string.qol_q24), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(25, mContext.getString(R.string.qol_q25), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(26, mContext.getString(R.string.qol_q26), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(27, mContext.getString(R.string.qol_q27), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(28, mContext.getString(R.string.qol_q28), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(29, mContext.getString(R.string.qol_q29), answerOption4));
        questionList3.add(new QolSurveyQuestionModel(30, mContext.getString(R.string.qol_q30), answerOption4));
        fragment4_title = mContext.getString(R.string.qol_q31_40);
        questionList4.add(new QolSurveyQuestionModel(31, mContext.getString(R.string.qol_q31), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(32, mContext.getString(R.string.qol_q32), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(33, mContext.getString(R.string.qol_q33), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(34, mContext.getString(R.string.qol_q34), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(35, mContext.getString(R.string.qol_q35), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(36, mContext.getString(R.string.qol_q36), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(37, mContext.getString(R.string.qol_q37), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(38, mContext.getString(R.string.qol_q38), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(39, mContext.getString(R.string.qol_q39), answerOption4));
        questionList4.add(new QolSurveyQuestionModel(40, mContext.getString(R.string.qol_q40), answerOption4));
        fragment5_title = mContext.getString(R.string.qol_q41_49);
        questionList5.add(new QolSurveyQuestionModel(41, mContext.getString(R.string.qol_q41), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(42, mContext.getString(R.string.qol_q42), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(43, mContext.getString(R.string.qol_q43), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(44, mContext.getString(R.string.qol_q44), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(45, mContext.getString(R.string.qol_q45), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(46, mContext.getString(R.string.qol_q46), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(47, mContext.getString(R.string.qol_q47), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(48, mContext.getString(R.string.qol_q48), answerOption4));
        questionList5.add(new QolSurveyQuestionModel(49, mContext.getString(R.string.qol_q49), answerOption4));
        fragment6_title = mContext.getString(R.string.qol_q50_51);
        questionList6.add(new QolSurveyQuestionModel(50, mContext.getString(R.string.qol_q50), answerOption7));
        questionList6.add(new QolSurveyQuestionModel(51, mContext.getString(R.string.qol_q51), answerOption7));
    }
}
