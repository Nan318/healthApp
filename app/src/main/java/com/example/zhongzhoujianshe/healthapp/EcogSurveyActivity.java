package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EcogSurveyActivity extends AppCompatActivity {
    //UI objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    //toolbar part
    private Toolbar toolbar;
    private TextView txt_menu_back;
    private TextView txt_menu_send;
    //dody part
    private TextView txt_date;
    private RadioGroup radioGroup;
    private TextView scale_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecog_survey);

        /* * * * * toolbar * * * * * */

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        toolbar = (Toolbar) findViewById(R.id.bssNewToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //set icon-font: back
        txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setTypeface(font);
        //set icon-font: send
        txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
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
        txt_date = (TextView) findViewById(R.id.date_txt);
        txt_date.setTypeface(font);


        scale_note = (TextView) findViewById(R.id.scale_note);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_0:
                        scale_note.setVisibility(View.VISIBLE);
                        //scale_note.setText(getResources().getString(R.string.ecog_new_scale0));
                        scale_note.setText(R.string.ecog_new_scale0);
                        break;
                    case R.id.rb_1:
                        scale_note.setVisibility(View.VISIBLE);
                        //scale_note.setText(getResources().getString(R.string.ecog_new_scale0));
                        scale_note.setText(R.string.ecog_new_scale1);
                        break;
                    case R.id.rb_2:
                        scale_note.setVisibility(View.VISIBLE);
                        //scale_note.setText(getResources().getString(R.string.ecog_new_scale0));
                        scale_note.setText(R.string.ecog_new_scale2);
                        break;
                    case R.id.rb_3:
                        scale_note.setVisibility(View.VISIBLE);
                        //scale_note.setText(getResources().getString(R.string.ecog_new_scale0));
                        scale_note.setText(R.string.ecog_new_scale3);
                        break;
                    case R.id.rb_4:
                        scale_note.setVisibility(View.VISIBLE);
                        //scale_note.setText(getResources().getString(R.string.ecog_new_scale0));
                        scale_note.setText(R.string.ecog_new_scale4);
                        break;
                    default:
                        scale_note.setVisibility(View.INVISIBLE);
                }
            }



        });
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
        dialogMessage.setText(R.string.ecog_new_alertmsg);

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
