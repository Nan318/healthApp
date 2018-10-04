package com.example.zhongzhoujianshe.healthapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QolSurveyAnswerModel implements Parcelable {
    private String date;
    private ArrayList<String[]> results = new ArrayList<>(); //for each answer,the format is like: [questionId, answer]

    public QolSurveyAnswerModel() {
        super();
    }

    public QolSurveyAnswerModel(String date) {
        super();
        this.date = date;

    }
    public String getSurveyDate() {
        return date;
    }

    //get the answers
    public ArrayList<String[]> getResults() {
        return results;
    }
    //update the result list
    public void setResults(String[] updateData){
        if(updateData.length == 2){
            //find the target data or add the data
            boolean update = false;
            if(results.size()==0){  //first result
                results.add(updateData);
                update = true;
            }else{
                for(int i = 0; i < results.size(); i++){
                    String[] item = results.get(i);
                    int itemId = Integer.parseInt(item[0]);
                    int updateId = Integer.parseInt(updateData[0]);
                    if(item.length == 2){
                        if(itemId == updateId){ //update: true; update data
                            results.set(i, updateData);
                            update = true;
                            break;
                        }else if(itemId > updateId){ //update: true; insert data
                            results.add(i, updateData);
                            update = true;
                            break;
                        }
                    }
                }
            }
            if(!update){ //update: false; add data
                results.add(updateData);
            }
        }

    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        //arraylist
        parcel.writeInt(results.size());
        for (String[] array : results) {
            parcel.writeStringArray(array);
        }
    }
    private QolSurveyAnswerModel(Parcel source) {
        date = source.readString();
        //read arraylist<string[]>
        final int arraysCount = source.readInt();
        results = new ArrayList<String[]>(arraysCount);
        for (int i = 0; i < arraysCount; i++) {
            results.add(source.createStringArray());
        }
    }
    public static final Parcelable.Creator<QolSurveyQuestionModel> CREATOR = new Creator(){

        @Override
        public QolSurveyAnswerModel createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new QolSurveyAnswerModel(source);
        }

        @Override
        public QolSurveyAnswerModel[] newArray(int size) {
            // TODO Auto-generated method stub
            return new QolSurveyAnswerModel[size];
        }
    };
}
