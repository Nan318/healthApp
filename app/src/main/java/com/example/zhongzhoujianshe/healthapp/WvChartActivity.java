package com.example.zhongzhoujianshe.healthapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.DateIntervalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WvChartActivity extends AppCompatActivity implements OnChartValueSelectedListener{
    //UI objects
    //toolbar part
    private Toolbar toolbar;
    private TextView txt_menu_back;
    private TextView txt_menu_new;
    //firebase
    private String currentUserId;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private LineChart lineChart;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wv_chart);

        /* * * * * firebase * * * * * */
        //get Uid
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    currentUserId = user.getUid();
                }
            }
        };

        /* * * * * initialize view  * * * * * */
        iniView();


        List<Entry> valsComp1 = new ArrayList<>();


        valsComp1.add(new Entry(1, 2));
        valsComp1.add(new Entry(2, 0));
        valsComp1.add(new Entry(3, 0));
        valsComp1.add(new Entry(6, 1));
        valsComp1.add(new Entry(7, 2));
        valsComp1.add(new Entry(8, 3));

        lineChart = findViewById(R.id.lineChart);



        initLineChart(valsComp1);
        lineChart.isDrawMarkersEnabled();
        lineChart.setDrawMarkers(true);

        Button lastweek =(Button)findViewById(R.id.btn1);
        lastweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lineChart = findViewById(R.id.lineChart);

                List<Entry> valsComp2 = new ArrayList<>();


                valsComp2.add(new Entry(1, 2));
                valsComp2.add(new Entry(2, 0));
                valsComp2.add(new Entry(3, 0));

                initLineChart(valsComp2);


            }
        });



        // last month button
        Button lastmonth  =(Button)findViewById(R.id.btn2);
        lastmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineChart = findViewById(R.id.lineChart);
                int[] lastweekX = {1,2,3};
                int[] lastweekY = {2,4,5};

                List<Entry> valsComp2 = new ArrayList<>();
                for(int i =0; i<lastweekX.length;i++){
                    valsComp2.add(new Entry(lastweekX[i], lastweekY[i]));
                }




                initLineChart(valsComp2);



            }
        });


        // last year button
        Button lastyear  =(Button)findViewById(R.id.btn3);
        lastyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineChart = findViewById(R.id.lineChart);

                List<Entry> valsComp2 = new ArrayList<>();


                valsComp2.add(new Entry(1, 2));
                valsComp2.add(new Entry(2, 0));
                valsComp2.add(new Entry(3, 0));

                initLineChart(valsComp2);

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initLineChart(List<Entry> lineData) {

        lineChart.setOnChartValueSelectedListener(this);
        // 设置是否可以缩放图表
        lineChart.setScaleEnabled(true);
        // 设置是否可以用手指移动图表
        lineChart.setDragEnabled(true);

        lineChart.setNoDataText("No chart data available.");  //没数据后的显示
        lineChart.setNoDataTextColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false); //禁用右侧y轴
        lineChart.getDescription().setEnabled(false);  // 不显示标签
        lineChart.animateY(2500);
        lineChart.animateX(1200);


        //自定义适配器，适配于X轴
        String[] xStrs = new String[]{ "","what", "噢", "冬","sha","k","p","l","s","end"}; // 线图横坐标文字
        myBarChartFormatter aoz = new myBarChartFormatter(xStrs);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(aoz);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(3);
        // xAxis.setAxisMinimum(1f);  // from which data

        //自定义适配器，适配于Y轴
        DecimalFormatter patint = new DecimalFormatter();
        //IAxisValueFormatter patint = new MyAxisValueFormatter();

        YAxis leftAxis = lineChart.getAxisLeft();

        leftAxis.setValueFormatter(patint);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);

        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);



        setLineChartData(lineData);
    }

    private void setLineChartData(List<Entry> lineData) {




        LineDataSet setComp1 = new LineDataSet(lineData, " weight ");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColor(Color.WHITE);
        setComp1.setDrawCircles(true);
        setComp1.setDrawValues(false);

        setComp1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        List<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(setComp1);

        LineData lineChartData = new LineData(dataSets);

        lineChart.setData(lineChartData);

        lineChart.invalidate();




    }



    private void iniView() {
        /* * * * * toolbar * * * * * */
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

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        toolbar = (Toolbar) findViewById(R.id.bssNewToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //set icon-font: back
        txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setTypeface(font);
        //set icon-font: send
        txt_menu_new = (TextView) toolbar.findViewById(R.id.toolbar_new);
        txt_menu_new.setTypeface(font);
        //set icon-font: cog
        TextView txt_menu_cog = (TextView) toolbar.findViewById(R.id.toolbar_cog);
        txt_menu_cog.setTypeface(font);

        //set click event
        txt_menu_new.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WvChartActivity.this, WvSurveyActivity.class);
                startActivity(intent);

            }
        });
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                WvChartActivity.this.finish();
            }
        });

        /* * * * * body * * * * * */


    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
