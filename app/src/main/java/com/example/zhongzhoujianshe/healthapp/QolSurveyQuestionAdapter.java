package com.example.zhongzhoujianshe.healthapp;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhongzhoujianshe.healthapp.MyMultipleLineRadioGroup;
import com.example.zhongzhoujianshe.healthapp.MyMultipleLineRadioGroup.OnCheckedChangeListener;


import java.util.ArrayList;

public class QolSurveyQuestionAdapter extends BaseAdapter{
    //定义两个类别标志
    private static final int TYPE_4OPTION = 0;
    private static final int TYPE_7OPTION = 1;
    private ArrayList<QolSurveyQuestionModel> qolQuestionList;
    //String answer[];
    private Context context;
    private QolSurveyQuestionModel currentQuestion = null;
    private String answer = null;
    private boolean isChecking = true;
    private int mCheckedId = -1;
    private ViewHolder1 holder1 = null;
    private ViewHolder2 holder2 = null;


    public QolSurveyQuestionAdapter(ArrayList<QolSurveyQuestionModel> qolQuestionList, Context context) {
        this.context = context;
        this.qolQuestionList = qolQuestionList;
    }

    @Override
    public int getCount() {
        return qolQuestionList.size();
    }

    @Override
    public QolSurveyQuestionModel getItem(int position) {
        return qolQuestionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //多布局的核心，通过这个判断类别
    @Override
    public int getItemViewType(int position) {
        //get the current item of the qolQuestionList
        currentQuestion = qolQuestionList.get(position);
        //get the number of its answer options
        int numOfAnswer = currentQuestion.getAnswerOption().length;
        if (numOfAnswer == 4) {
            return TYPE_4OPTION;
        } else if (numOfAnswer == 7) {
            return TYPE_7OPTION;
        } else {
            return super.getItemViewType(position);
        }
    }
    //类别项目
    @Override
    public int getViewTypeCount() {
        //for now, there are only two kinds of question model: 1/4 & 1/7
        return 2;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //获取item视图类别
        int type = getItemViewType(position);

        if (view == null){ //当第一次加载ListView控件时  view为空
            //根据类别加载视图
            switch (type){
                case TYPE_4OPTION:
                    holder1 = new ViewHolder1();
                    view = LayoutInflater.from(context).inflate(R.layout.qol_survey_listitem_4option,viewGroup,false);
                    holder1.txt_question4 = (TextView) view.findViewById(R.id.txt_question4);
                    holder1.mRadioGroup = (MyMultipleLineRadioGroup) view.findViewById(R.id.mRadioGroup);
                    holder1.radioButton_11 = (RadioButton) view.findViewById(R.id.rb_11);
                    holder1.radioButton_12 = (RadioButton) view.findViewById(R.id.rb_12);
                    holder1.radioButton_21 = (RadioButton) view.findViewById(R.id.rb_21);
                    holder1.radioButton_22 = (RadioButton) view.findViewById(R.id.rb_22);

                    //get the checked option of mRadioGroup in viewHolder1 (1/4)
                    if (holder1.mRadioGroup != null){
                        holder1.mRadioGroup.setOnCheckedChangeListener(new MyMultipleLineRadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(MyMultipleLineRadioGroup group, int checkedId) {
                                RadioButton selectedbtn = (RadioButton) group.findViewById(checkedId);
                                answer = selectedbtn.getText().toString();
                                Toast.makeText(context, qolQuestionList.get(position).getQuestion()+"你选了" + answer, Toast.LENGTH_LONG).show();

                            }

                        });

                    }


                    //缓存ViewHolder1，使用的key是strings.xml中定义的Tag_APP
                    view.setTag(R.id.Tag_4OPTION,holder1); //为view设置标签
                    break;
                case TYPE_7OPTION:
                    holder2 = new ViewHolder2();
                    view = LayoutInflater.from(context).inflate(R.layout.qol_survey_listitem_7option,viewGroup,false);
                    holder2.txt_question7 = (TextView) view.findViewById(R.id.txt_question7);
                    holder2.radioGroup1 = (RadioGroup) view.findViewById(R.id.qol7radioGroup);
                    holder2.radioButton_11 = (RadioButton) view.findViewById(R.id.rb_1);
                    holder2.radioButton_12 = (RadioButton) view.findViewById(R.id.rb_2);
                    holder2.radioButton_13 = (RadioButton) view.findViewById(R.id.rb_3);
                    holder2.radioButton_14 = (RadioButton) view.findViewById(R.id.rb_4);
                    holder2.radioButton_15 = (RadioButton) view.findViewById(R.id.rb_5);
                    holder2.radioButton_16 = (RadioButton) view.findViewById(R.id.rb_6);
                    holder2.radioButton_17 = (RadioButton) view.findViewById(R.id.rb_7);

                    //get the checked option of radioGroup1 in viewHolder2 (1/7)
                    holder2.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int selectedId) {
                           // int getPosition = (Integer) radioGroup.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                           // qolQuestionList.get(getPosition).setSelected(selectedId); // Set the value of checkbox to maintain its state.
                            RadioButton selectedbtn = (RadioButton) radioGroup.findViewById(selectedId);
                            answer = selectedbtn.getText().toString();
                            Toast.makeText(context, qolQuestionList.get(position).getQuestion()+"你选了" + answer, Toast.LENGTH_LONG).show();

                        }
                    });

                    //缓存ViewHolder2，使用的key是strings.xml中定义的Tag_
                    view.setTag(R.id.Tag_7OPTION,holder2);
                  //  view.setTag(R.id.Tag_7OPTION_ANSWER, holder2.radioGroup1);
                    break;
            }
        }else {//取出holder
            switch (type){
                case TYPE_4OPTION:
                    holder1 = (ViewHolder1) view.getTag(R.id.Tag_4OPTION); //the Object stored in this view as a tag
                    break;
                case TYPE_7OPTION:
                    holder2 = (ViewHolder2) view.getTag(R.id.Tag_7OPTION);
                    break;
            }
        }


        //get the current item of the qolQuestionList
        currentQuestion = qolQuestionList.get(position);
        //get the question
        String question = currentQuestion.getQuestion();
        //get the answer options
        String[] answerOption = currentQuestion.getAnswerOption();
        //设置控件的值
        switch (type){
            case TYPE_4OPTION:

                if (currentQuestion != null && answerOption.length == 4){
                    holder1.txt_question4.setText(question);
                    holder1.radioButton_11.setText(answerOption[0]);
                    holder1.radioButton_12.setText(answerOption[1]);
                    holder1.radioButton_21.setText(answerOption[2]);
                    holder1.radioButton_22.setText(answerOption[3]);
                }
                break;
            case TYPE_7OPTION:
                if(currentQuestion != null && answerOption.length == 7){
                    holder2.txt_question7.setText(question);
                    holder2.radioButton_11.setText(answerOption[0]);
                    holder2.radioButton_12.setText(answerOption[1]);
                    holder2.radioButton_13.setText(answerOption[2]);
                    holder2.radioButton_14.setText(answerOption[3]);
                    holder2.radioButton_15.setText(answerOption[4]);
                    holder2.radioButton_16.setText(answerOption[5]);
                    holder2.radioButton_17.setText(answerOption[6]);
                }
                break;
        }




        return view;
    }

   //两个不同的ViewHolder
    private class ViewHolder1 {
        //for 4 options
        TextView txt_question4;
        MyMultipleLineRadioGroup mRadioGroup; //self-defined radioGroup
       RadioButton radioButton_11;
        RadioButton radioButton_12;
        RadioButton radioButton_21;
        RadioButton radioButton_22;

    }

    private class ViewHolder2 {
        //for 7 options
        TextView txt_question7;
        RadioGroup radioGroup1;
        RadioButton radioButton_11;
        RadioButton radioButton_12;
        RadioButton radioButton_13;
        RadioButton radioButton_14;
        RadioButton radioButton_15;
        RadioButton radioButton_16;
        RadioButton radioButton_17;


    }




}