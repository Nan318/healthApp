package com.example.zhongzhoujianshe.healthapp;

public class WvAnswerModel {
    private String time;
    private String bmi;
    private String height;
    private String weight;
    // Getter Methods

    public WvAnswerModel(){}

    public WvAnswerModel(String time, String weight, String height, String bmi){
        this.time = time;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height){
        this.height = height;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }

    public String getTime() {
        return time;
    }

    public String getBmi() {
        return bmi;
    }

    // Setter Methods

    public void setTime(String time) {
        this.time = time;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }
}
