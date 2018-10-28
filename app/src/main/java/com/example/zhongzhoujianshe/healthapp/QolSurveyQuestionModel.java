//this class is used for storing the questionnaire
//but if the questionnaire is not read from the database, then this class is useless
//so for now, it is useless. but this can be changed later

package com.example.zhongzhoujianshe.healthapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;


public class QolSurveyQuestionModel implements Parcelable {
    private String question;
    private String[] answerOption;
    private int questionId;
    private int answer;

    public QolSurveyQuestionModel() {
        super();
    }

    public QolSurveyQuestionModel(int questionId, String question, String[] answerOption) {
        super();
        this.questionId = questionId;
        this.question = question;
        this.answerOption = answerOption;

    }
    public int getQuestionId() {
        return questionId;
    }
    public void setQuestionId(int id){
        this.questionId = id;
    }
    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question
     *            the questionName to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    //return answerOption
    public String[] getAnswerOption() {
        return answerOption;
    }

    //set answerOption
    public void setAnswerOption(String[] answerOption) {
        this.answerOption = answerOption;
    }

    public void setAnswer(int answer){
        this.answer = answer;
    }
    public int getAnswer(){
        return answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    //write
    @Override
    public void writeToParcel(Parcel parcel, int j) {

        // TODO Auto-generated method stub
        // must write in order
        parcel.writeInt(questionId);
        parcel.writeString(question);
        //string array
        if(answerOption == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(answerOption.length);
            parcel.writeStringArray(answerOption);
        }
        parcel.writeInt(answer);


    }
    // 1.must implement Parcelable.Creator
     /*
     * read in order as write
     */
    private QolSurveyQuestionModel(Parcel source) {
        questionId = source.readInt();
        question = source.readString();
        //read array's length
        int length = source.readInt();
        //if lenght greater than 0, read
        if(length>0){
            answerOption = new String[length];
            source.readStringArray(answerOption);
        }
        answer = source.readInt();
    }
    public static final Parcelable.Creator<QolSurveyQuestionModel> CREATOR = new Creator(){

        @Override
        public QolSurveyQuestionModel createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new QolSurveyQuestionModel(source);
        }

        @Override
        public QolSurveyQuestionModel[] newArray(int size) {
            // TODO Auto-generated method stub
            return new QolSurveyQuestionModel[size];
        }
    };
}
