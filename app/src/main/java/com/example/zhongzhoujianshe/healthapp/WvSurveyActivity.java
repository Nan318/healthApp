package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import java.text.DecimalFormat;

public class WvSurveyActivity extends AppCompatActivity{
    //UI Objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    //private String result = "1";
    private MyEditText et_weight;
    private MyEditText et_height;
    private MyEditText et_bmi;
    private String weight;
    private String height;
    private Double bmi = 0.0;
    private DecimalFormat df = new DecimalFormat("###.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wv_survey);//toolbar

        /* * * * * toolbar * * * * * */

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.wvNewToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //set icon-font
        TextView txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setTypeface(font);
        TextView txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
        txt_menu_send.setTypeface(font);

        //set click event
        txt_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showSendDialog();
            }
        });
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext() , "backBtn" , Toast.LENGTH_SHORT).show();
            }
        });

        /* * * * * body * * * * * */

        //set icon font for date_txt
        TextView txt_date = (TextView) findViewById(R.id.date_txt);
        txt_date.setTypeface(font);

        //input part
        et_weight = (MyEditText) findViewById(R.id.et_weight);
        et_height = (MyEditText) findViewById(R.id.et_height);
        et_bmi = (MyEditText) findViewById(R.id.et_bmi);
        EditText etw = (EditText) et_weight.findViewById(R.id.input);
        EditText eth = (EditText) et_height.findViewById(R.id.input);
        etw.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable.toString())){
                    et_bmi.setTopTitle("");
                    et_bmi.setEtText("");
                    et_bmi.setInputHint("BMI");
                }else{
                    weight = et_weight.getEtText(); //kg
                    height = et_height.getEtText();  //cm
                    bmi = bmiCalculate(weight, height);
                    if(bmi != 0.0){
                        String bmis = df.format(bmi);
                        et_bmi.setTopTitle("BMI");
                        et_bmi.setEtText(bmis);
                    }else{
                        et_bmi.setTopTitle("");
                        et_bmi.setInputHint("BMI");
                    }
                }
            }
        });
        eth.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable.toString())){
                    et_bmi.setTopTitle("");
                    et_bmi.setEtText("");
                    et_bmi.setInputHint("BMI");
                }else{
                    weight = et_weight.getEtText(); //kg
                    height = et_height.getEtText();  //cm
                    bmi = bmiCalculate(weight, height);
                    if(bmi != 0.0){
                        String bmis = df.format(bmi);
                        et_bmi.setTopTitle("BMI");
                        et_bmi.setEtText(bmis);
                    }else{
                        et_bmi.setTopTitle("");
                        et_bmi.setInputHint("BMI");
                    }
                }

            }
        });

    }
    private Double bmiCalculate(String weight, String height){
        //body mass index (BMI): kg/m2
        if(!weight.isEmpty() && !height.isEmpty()){
            Double weightkg = Double.parseDouble(weight);
            Double heightcm = Double.parseDouble(height);
            Double heightm = heightcm / 100;
            Double bmi = weightkg / (heightm * heightm);
            return bmi;
        }else{
            return 0.0;
        }

    }
    private void showSendDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_onebtn_dialog,null);
        MyRoundCornerButton dialogBtnOk = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_ok);
        dialogBtnOk.setFillet(true);
        dialogBtnOk.setRadius(13);
        dialogBtnOk.setBackColor(getResources().getColor(R.color.wv_dialog_red));
        dialogBtnOk.setBackColorSelected(getResources().getColor(R.color.wv_dialog_red_press));
        dialogBtnOk.setTextColori(getResources().getColor(R.color.white));
        dialogBtnOk.setText(getResources().getString(R.string.ok));

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.txt_warning);
        TextView dialogMessage = (TextView) dialogView.findViewById(R.id.txt_content);
        //reset the title
        dialogTitle.setText(R.string.incomplete);
        //reset the message
        dialogMessage.setText(R.string.incomplete_txt);

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(getString(R.string.icon_warning));
        builder.setView(dialogView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        alert = builder.create();
        alert.show();

        //设置组件
        dialogBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Toast.makeText(getApplicationContext(), "对话框已关闭~", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
