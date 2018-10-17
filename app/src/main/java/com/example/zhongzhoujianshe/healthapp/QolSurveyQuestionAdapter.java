package com.example.zhongzhoujianshe.healthapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QolSurveyQuestionAdapter extends BaseAdapter{

    private static final int TYPE_4OPTION = 0; //define two tags
    private static final int TYPE_7OPTION = 1;
    private ArrayList<QolSurveyQuestionModel> qolQuestionList;
    private Context context;
    private QolSurveyQuestionModel currentQuestion = null;
    private String answer = null;
    private int questionId;
    private QolSurveyAnswerModel results;
    private ViewHolder1 holder1 = null;
    private ViewHolder2 holder2 = null;

    public QolSurveyQuestionAdapter(ArrayList<QolSurveyQuestionModel> qolQuestionList, Context context, QolSurveyAnswerModel result) {
        this.context = context;
        this.qolQuestionList = qolQuestionList;
        this.results = result;
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

    //to determine the layout
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

    @Override
    public int getViewTypeCount() {
        //for now, there are only two kinds of question model: 1/4 & 1/7
        return 2;
    }
    private Integer index = -1;
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //get the type of the item's layout
        int type = getItemViewType(position);

        if (view == null){ //when first initialize the view, it is null
            //initialize view according to the type
            switch (type){
                case TYPE_4OPTION:
                    holder1 = new ViewHolder1();
                    view = LayoutInflater.from(context).inflate(R.layout.qol_survey_listitem_4option,viewGroup,false);
                    holder1.txt_question4 = (TextView) view.findViewById(R.id.txt_question4);
                    holder1.mRadioGroup = (MyMultipleLineRadioGroup) view.findViewById(R.id.radioGroup);
                    holder1.radioButton_11 = (RadioButton) view.findViewById(R.id.rb_11);
                    holder1.radioButton_12 = (RadioButton) view.findViewById(R.id.rb_12);
                    holder1.radioButton_21 = (RadioButton) view.findViewById(R.id.rb_21);
                    holder1.radioButton_22 = (RadioButton) view.findViewById(R.id.rb_22);

                    //the tag is defined in strings.xml
                    view.setTag(R.id.Tag_4OPTION,holder1);
                    break;
                case TYPE_7OPTION:
                    holder2 = new ViewHolder2();
                    view = LayoutInflater.from(context).inflate(R.layout.qol_survey_listitem_7option,viewGroup,false);
                    holder2.txt_question7 = (TextView) view.findViewById(R.id.txt_question7);
                    holder2.radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup);
                    holder2.radioButton_11 = (RadioButton) view.findViewById(R.id.rb_1);
                    holder2.radioButton_12 = (RadioButton) view.findViewById(R.id.rb_2);
                    holder2.radioButton_13 = (RadioButton) view.findViewById(R.id.rb_3);
                    holder2.radioButton_14 = (RadioButton) view.findViewById(R.id.rb_4);
                    holder2.radioButton_15 = (RadioButton) view.findViewById(R.id.rb_5);
                    holder2.radioButton_16 = (RadioButton) view.findViewById(R.id.rb_6);
                    holder2.radioButton_17 = (RadioButton) view.findViewById(R.id.rb_7);

                    //the tag is defined in strings.xml
                    view.setTag(R.id.Tag_7OPTION,holder2);
                    break;
            }
        }else {//get holder
            switch (type){
                case TYPE_4OPTION:
                    holder1 = (ViewHolder1) view.getTag(R.id.Tag_4OPTION); //the Object stored in this view as a ta
                    break;
                case TYPE_7OPTION:
                    holder2 = (ViewHolder2) view.getTag(R.id.Tag_7OPTION);
                    break;
            }
        }

        //get the current item of the qolQuestionList
        currentQuestion = qolQuestionList.get(position);
        //get the id
        questionId = currentQuestion.getQuestionId();
        //get the question
        String question = currentQuestion.getQuestion();
        //get the answer options
        String[] answerOption = currentQuestion.getAnswerOption();

        //set values
        switch (type){
            case TYPE_4OPTION:
                if (currentQuestion != null && answerOption.length == 4){
                    holder1.txt_question4.setText(questionId + question);
                    holder1.radioButton_11.setText(answerOption[0]);
                    holder1.radioButton_12.setText(answerOption[1]);
                    holder1.radioButton_21.setText(answerOption[2]);
                    holder1.radioButton_22.setText(answerOption[3]);

                    //1.cancel the listener
                    holder1.mRadioGroup.setOnCheckedChangeListener(null);
                    //2.clear the checks
                    //holder1.mRadioGroup.clearCheck();
                    holder1.radioButton_11.setChecked(false);
                    holder1.radioButton_12.setChecked(false);
                    holder1.radioButton_21.setChecked(false);
                    holder1.radioButton_22.setChecked(false);
                    //3.check radiobtn according to the checked answer
                    switch (currentQuestion.getAnswer()) {
                        case 11:
                            holder1.mRadioGroup.check(R.id.rb_11);
                            break;
                        case 12:
                            holder1.mRadioGroup.check(R.id.rb_12);
                            break;
                        case 21:
                            holder1.mRadioGroup.check(R.id.rb_21);
                            break;
                        case 22:
                            holder1.mRadioGroup.check(R.id.rb_22);
                            break;
                        default:
                            break;
                    }

                    //4. re-register listener
                    //get the checked option of mRadioGroup in viewHolder1 (1/4)
                    if (holder1.mRadioGroup != null){
                        holder1.mRadioGroup.setOnCheckedChangeListener(new MyMultipleLineRadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(MyMultipleLineRadioGroup group, int checkedId) {

                                int p = 0;
                                switch (checkedId){
                                    case R.id.rb_11:
                                        p=11;
                                        break;
                                    case R.id.rb_12:
                                        p=12;
                                        break;
                                    case R.id.rb_21:
                                        p=21;
                                        break;
                                    case R.id.rb_22:
                                        p=22;
                                        break;
                                }
                                // record the selected answer,
                                // so that it can be set to radiogroup while sliding
                                qolQuestionList.get(position).setAnswer(p);
                                //test right or not
                                /*
                                for(QolSurveyQuestionModel title:qolQuestionList){
                                    if(title.getAnswer()>0)
                                        Log.e("TAG",title.getQuestionId()+"---"+title.getAnswer());
                                }*/
                                RadioButton selectedbtn = (RadioButton) group.findViewById(checkedId);
                                answer = selectedbtn.getText().toString();
                                questionId = qolQuestionList.get(position).getQuestionId();
                                String[] itemResult = new String[2];
                                itemResult[0] = String.valueOf(questionId);
                                itemResult[1] = answer;
                                //Toast.makeText(context, questionId+"你选了" + answer, Toast.LENGTH_LONG).show();
                                results.setResults(itemResult);

                            }

                        });

                    }
                }


                break;
            case TYPE_7OPTION:
                if(currentQuestion != null && answerOption.length == 7){
                    holder2.txt_question7.setText(questionId + question);
                    holder2.radioButton_11.setText(answerOption[0]);
                    holder2.radioButton_12.setText(answerOption[1]);
                    holder2.radioButton_13.setText(answerOption[2]);
                    holder2.radioButton_14.setText(answerOption[3]);
                    holder2.radioButton_15.setText(answerOption[4]);
                    holder2.radioButton_16.setText(answerOption[5]);
                    holder2.radioButton_17.setText(answerOption[6]);

                    //get the checked option of radioGroup1 in viewHolder2 (1/7)
                    holder2.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int selectedId) {
                            // int getPosition = (Integer) radioGroup.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                            // qolQuestionList.get(getPosition).setSelected(selectedId); // Set the value of checkbox to maintain its state.
                            RadioButton selectedbtn = (RadioButton) radioGroup.findViewById(selectedId);
                            answer = selectedbtn.getText().toString();
                            //Toast.makeText(context, qolQuestionList.get(position).getQuestionId()+"你选了" + answer, Toast.LENGTH_LONG).show();
                            questionId = qolQuestionList.get(position).getQuestionId();
                            String[] itemResult = new String[2];
                            itemResult[0] = String.valueOf(questionId);
                            itemResult[1] = answer;
                            results.setResults(itemResult);
                        }
                    });
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