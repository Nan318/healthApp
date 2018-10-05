package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TestMyButton extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmybutton);

        //第一行按钮
        MyRoundCornerButton myBtn = (MyRoundCornerButton) findViewById(R.id.aaa);
        myBtn.setPartRadius(15, 0, 15, 0);
        myBtn.setBackColor(getResources().getColor(R.color.wvDateTxt));
        myBtn.setBackColorSelected(getResources().getColor(R.color.gray));
        myBtn.setTextColori(getResources().getColor(R.color.wvInputGreen));
        myBtn.setTextColorSelected(getResources().getColor(R.color.dialog_yellow));
        //myBtn.setStroke(3, getResources().getColor(R.color.wvInputGreen));
        myBtn.setPressedStrokeWidthColor(3, getResources().getColor(R.color.dialog_yellow));
        myBtn.setBorderColor(Color.RED);
        myBtn.setBorderBottom(true);
        myBtn.setBorderWidth(7);

        // 第二行按钮
        MyRoundCornerButton btn1 = (MyRoundCornerButton) findViewById(R.id.btn1);
        MyRoundCornerButton btn2 = (MyRoundCornerButton) findViewById(R.id.btn2);
        MyRoundCornerButton btn3 = (MyRoundCornerButton) findViewById(R.id.btn3);
        //shape left to right: left corner, rectangle, right corner
        btn1.setFillet(true);
        btn1.setPartRadius(15, 0, 0, 15);
        btn3.setFillet(true);
        btn3.setPartRadius(0, 15, 15, 0);
        //border
        btn1.setStroke(3, getResources().getColor(R.color.qolChartTopBtn1));
        btn3.setStroke(3, getResources().getColor(R.color.qolChartTopBtn1));
        btn2.setBorderTop(true);
        btn2.setBorderBottom(true);
        btn2.setBorderWidth(5);
        btn2.setBorderColor(getResources().getColor(R.color.qolChartTopBtn1));
        //background color
        btn1.setBackColor(getResources().getColor(R.color.qolChartTopBtn2));
        btn2.setBackColor(getResources().getColor(R.color.qolChartTopBtn2));
        btn3.setBackColor(getResources().getColor(R.color.qolChartTopBtn2));
        btn1.setBackColorSelected(getResources().getColor(R.color.qolChartTopBtn1));
        btn2.setBackColorSelected(getResources().getColor(R.color.qolChartTopBtn1));
        btn3.setBackColorSelected(getResources().getColor(R.color.qolChartTopBtn1));
        //text color
        btn1.setTextColori(getResources().getColor(R.color.qolChartTopBtn1));
        btn2.setTextColori(getResources().getColor(R.color.qolChartTopBtn1));
        btn3.setTextColori(getResources().getColor(R.color.qolChartTopBtn1));
        btn1.setTextColorSelected(getResources().getColor(R.color.qolChartTopBtn2));
        btn2.setTextColorSelected(getResources().getColor(R.color.qolChartTopBtn2));
        btn3.setTextColorSelected(getResources().getColor(R.color.qolChartTopBtn2));
        //text
        btn1.setText(getResources().getString(R.string.chart_week));
        btn2.setText(getResources().getString(R.string.chart_month));
        btn3.setText(getResources().getString(R.string.chart_all));
        //click
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click btn1", Toast.LENGTH_LONG).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click btn2", Toast.LENGTH_LONG).show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click btn3", Toast.LENGTH_SHORT).show();
            }
        });



        // 第三行按钮
        MyRoundCornerButton btn4 = (MyRoundCornerButton) findViewById(R.id.btn4);
        MyRoundCornerButton btn5 = (MyRoundCornerButton) findViewById(R.id.btn5);
        MyRoundCornerButton btn6 = (MyRoundCornerButton) findViewById(R.id.btn6);
        //shape left to right: left corner, rectangle, right corner
        btn4.setFillet(true);
        btn4.setPartRadius(15, 0, 0, 15);
        btn6.setFillet(true);
        btn6.setPartRadius(0, 15, 15, 0);
        //border
        btn4.setStroke(3, getResources().getColor(R.color.qolChartTopBtn1));
        btn6.setStroke(3, getResources().getColor(R.color.qolChartTopBtn1));
        btn5.setBorderTop(true);
        btn5.setBorderBottom(true);
        btn5.setBorderWidth(5);
        btn5.setBorderColor(getResources().getColor(R.color.qolChartTopBtn1));
        //background color
        btn4.setBackColor(getResources().getColor(R.color.qolChartTopBtn2));
        btn5.setBackColor(getResources().getColor(R.color.qolChartTopBtn2));
        btn6.setBackColor(getResources().getColor(R.color.qolChartTopBtn2));
        btn4.setBackColorSelected(getResources().getColor(R.color.qolChartTopBtn1));
        btn5.setBackColorSelected(getResources().getColor(R.color.qolChartTopBtn1));
        btn6.setBackColorSelected(getResources().getColor(R.color.qolChartTopBtn1));
        //text color
        btn4.setTextColori(getResources().getColor(R.color.qolChartTopBtn1));
        btn5.setTextColori(getResources().getColor(R.color.qolChartTopBtn1));
        btn6.setTextColori(getResources().getColor(R.color.qolChartTopBtn1));
        btn1.setTextColorSelected(getResources().getColor(R.color.qolChartTopBtn2));
        btn5.setTextColorSelected(getResources().getColor(R.color.qolChartTopBtn2));
        btn6.setTextColorSelected(getResources().getColor(R.color.qolChartTopBtn2));
        //text
        btn4.setText(getResources().getString(R.string.chart_week));
        btn5.setText(getResources().getString(R.string.chart_month));
        btn6.setText(getResources().getString(R.string.chart_all));
        //click
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click btn4", Toast.LENGTH_LONG).show();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click btn5", Toast.LENGTH_LONG).show();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click btn6", Toast.LENGTH_SHORT).show();
            }
        });
        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        TextView txt_date = (TextView) findViewById(R.id.txt_date);
        txt_date.setTypeface(font);
        //set click event
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getApplicationContext() , "choose date" , Toast.LENGTH_SHORT).show();
            }
        });

    }
}


