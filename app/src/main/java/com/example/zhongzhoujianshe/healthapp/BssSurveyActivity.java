package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import org.w3c.dom.Text;

public class BssSurveyActivity extends AppCompatActivity {
    //UI Objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private String result = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bss_survey);

        //toolbar

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.bssNewToolbar);
        // Title
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //set icon-font: back
        TextView txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setTypeface(font);
        //set icon-font: send
        TextView txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
        txt_menu_send.setTypeface(font);

        //set click event
        txt_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showSendDialog();
            }
        });
        //back text_btn
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext() , "backBtn" , Toast.LENGTH_SHORT).show();
            }
        });

        //body

        //set icon font for date_txt
        TextView date_txt = (TextView) findViewById(R.id.date_txt);
        date_txt.setTypeface(font);
        //show date and time
        //TextClock mTextClock = (TextClock)findViewById(R.id.text_clock);

        //seekbar
        final TickSeekBar bss_type = (TickSeekBar) findViewById(R.id.seekbar_bss_type);
        bss_type.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                //Toast.makeText(getApplicationContext() , seekParams.tickText , Toast.LENGTH_SHORT).show();
                result = seekParams.tickText;
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {

            }

        });

        Button btn = (Button) findViewById(R.id.btn);
        final TextView txt =(TextView) findViewById(R.id.txt_result);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
               // bss_type.setProgress(5);
                txt.setText(result);
            }
        });

    }
    private void showSendDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_onebtn_dialog,null);
        Button dialogBtnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.txt_warning);
        TextView dialogMessage = (TextView) dialogView.findViewById(R.id.txt_content);
        //reset the button's color: red
        dialogBtnOk.setBackgroundResource(R.drawable.rectangle_round_darkred);
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
