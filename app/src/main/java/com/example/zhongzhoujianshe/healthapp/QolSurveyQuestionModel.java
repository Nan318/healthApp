//this class is used for storing the questionnaire
//but if the questionnaire is not read from the database, then this class is useless
//so for now, it is useless. but this can be changed later

package com.example.zhongzhoujianshe.healthapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

// Parcelable,定义了将数据写入Parcel，和从Parcel中读出的接口。一个实体（用类来表示），如果需要封装到消息中去，就必须实现这一接口，实现了这一接口，该实体就成为“可打包的”了。
/*
Android序列化对象主要有两种方法：
    1.实现Serializable接口,实现Serializable接口是JavaSE本身就支持的;
    2.实现Parcelable接口,Parcelable是Android特有的功能，效率比实现Serializable接口高，像用于Intent数据传递也都支持，而且还可以用在进程间通信(IPC)，
      除了基本类型外，只有实现了Parcelable接口的类才能被放入Parcel中。
 */

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
    //内容描述接口，基本不用管
    @Override
    public int describeContents() {
        return 0;
    }
    //写入接口函数，打包
    @Override
    public void writeToParcel(Parcel parcel, int j) {

        // TODO Auto-generated method stub
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错 
        // 2.序列化对象 
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
    // 1.必须实现Parcelable.Creator接口
     /*
     * 反序列化,开始读对象的流顺序要和上面写的一样
     */
    private QolSurveyQuestionModel(Parcel source) {
        questionId = source.readInt();
        question = source.readString();
        //开始读数组的长度
        int length = source.readInt();
        //如果数组长度大于0，那么就读数组， 所有数组的操作都可以这样。
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
