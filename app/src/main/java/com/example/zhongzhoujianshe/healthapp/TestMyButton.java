package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class TestMyButton extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmybutton);

        //test
        MyRoundCornerButton myBtn = (MyRoundCornerButton) findViewById(R.id.aaa);
        myBtn.setPartRadius(15, 0, 15, 0);
        myBtn.setBackColor(getResources().getColor(R.color.wvDateTxt));
        myBtn.setBackColorSelected(getResources().getColor(R.color.gray));
        myBtn.setTextColori(getResources().getColor(R.color.wvInputGreen));
        myBtn.setTextColorSelected(getResources().getColor(R.color.yellow));
        //myBtn.setStroke(3, getResources().getColor(R.color.wvInputGreen));
        myBtn.setPressedStrokeWidthColor(3, getResources().getColor(R.color.yellow));
        myBtn.setBorderColor(Color.RED);
        myBtn.setBorderBottom(true);
        myBtn.setBorderWidth(7);

        //
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
    }
}


