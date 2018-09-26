//this class is used for storing the questionnaire
//but if the questionnaire is not read from the database, then this class is useless
//so for now, it is useless. but this can be changed later

package com.example.zhongzhoujianshe.healthapp;

import java.util.ArrayList;

public class QolSurveyQuestionModel {
    private String question;
    private String[] answerOption;
    private int selected;

    public QolSurveyQuestionModel() {
    }

    public QolSurveyQuestionModel(String question, String[] answerOption) {
        super();
        this.question = question;
        this.answerOption = answerOption;
        this.selected = -1;
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



}
