package com.example.zhongzhoujianshe.healthapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BssChartActivity extends AppCompatActivity implements OnChartValueSelectedListener  {
    //UI objects
    //toolbar part
    private Toolbar toolbar;
    private TextView txt_menu_back;
    private TextView txt_menu_new;
    private MyRoundCornerButton btn1;
    private MyRoundCornerButton btn2;
    private MyRoundCornerButton btn3;
    private HorizontalBarChart hBarChart;
    private LineChart lineChart;
    private TextView textView;
    private TextView textView2;
    //firebase
    private String currentUserId;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //data
    private ArrayList<String> dateAll = new ArrayList<>();
    private ArrayList<String> dateWeek = new ArrayList<>();
    private ArrayList<String> dateMonth = new ArrayList<>();
    private int[] dateIdAll;  //line: x, converted from dateAll
    private int[] dateIdMonth;  //line: x, converted from dateMonth
    private int[] dateIdWeek; //line: x, converted from dateWeek
    private ArrayList<Integer> typeIdAll;   //line: y
    private ArrayList<Integer> typeIdMonth;  //line: y
    private ArrayList<Integer> typeIdWeek; //line: y
    private int[] barAll = new int[]{0,0,0,0,0,0,0};  //value of bar chart, i.e. count
    private int[] barMonth = new int[]{0,0,0,0,0,0,0};
    private int[] barWeek = new int[]{0,0,0,0,0,0,0};
    //charts
    private List<Entry> lineWeek = new ArrayList<>();
    private List<Entry> lineMonth = new ArrayList<>();
    private List<Entry> lineAll = new ArrayList<>();
    private ArrayList<BarEntry> barWeekEntry = new ArrayList<BarEntry>();
    private ArrayList<BarEntry> barMonthEntry = new ArrayList<BarEntry>();
    private ArrayList<BarEntry> barAllEntry = new ArrayList<BarEntry>();

    //variables
    private String DateToday;
    private String DateLastWeekMon;
    private String DateLastWeekSun;
    private String DateLastMonthFirst;
    private String DateLastMonthLast;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

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
        setContentView(R.layout.activity_bss_chart);

        /* * * * * initialize view  * * * * * */
        iniView();

        /* * * * * firebase * * * * * */

        //get today's date
        Date date = new Date();
        DateToday = dateFormat.format(date);
        //Log.e("today","----"+DateToday);

        //get last week (date range)
        //Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date start = c.getTime();
        DateLastWeekMon = dateFormat.format(start);
        c.add(Calendar.DATE, 6);
        Date end = c.getTime();
        DateLastWeekSun = dateFormat.format(end);

        //get last month
        c.set(Calendar.DATE, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDateOfPreviousMonth = c.getTime();
        DateLastMonthLast = dateFormat.format(lastDateOfPreviousMonth);
        c.set(Calendar.DATE, 1);
        Date firstDateOfPreviousMonth = c.getTime();
        DateLastMonthFirst = dateFormat.format(firstDateOfPreviousMonth);

        //when first open the activity, display data of last week
        //get last week

        //get Uid
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    currentUserId = user.getUid();
                    Log.e("TAG", "onAuthStateChanged:signed_in:" + currentUserId);
                    mRoot = FirebaseDatabase.getInstance().getReference();
                    userRef = mRoot.child(currentUserId).child("bss");

                    getDataLastWeek(); //get data for : dateWeek & typeIdWeek & barWeek
                    getDataLastMonth();
                    getDataAll();

                    //test date id & type id
                    //i.e. test values for x and y
                    dateIdAll = getMinsDuration(dateAll, dateAll.size());
                    dateIdMonth = getMinsDuration(dateMonth, dateMonth.size());



                }else{
                    Log.e("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };



        /* * * * * initial line data * * * * * */



        //innitial line data

        List<Entry> valsComp1 = new ArrayList<>();


        valsComp1.add(new Entry(1, 2));
        valsComp1.add(new Entry(2, 0));
        valsComp1.add(new Entry(3, 0));
        valsComp1.add(new Entry(6, 1));
        valsComp1.add(new Entry(7, 2));
        valsComp1.add(new Entry(8, 3));




        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(1, 3));
        yVals1.add(new BarEntry(2, 4));
        yVals1.add(new BarEntry(3, 5));
        yVals1.add(new BarEntry(4, 6));
        yVals1.add(new BarEntry(5, 10));
        yVals1.add(new BarEntry(6, 10));
        yVals1.add(new BarEntry(7, 10));


//connect
        hBarChart = findViewById(R.id.hBarChart);
        lineChart = findViewById(R.id.lineChart);


        initHBarChart(yVals1);  //统计完以后插入纵坐标就好
        initLineChart(valsComp1);
        lineChart.isDrawMarkersEnabled();
        lineChart.setDrawMarkers(true);


        //click
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //lineChart = findViewById(R.id.lineChart);

                getDataLastWeek(); //get data for : dateWeek & typeIdWeek & barWeek
                if (!dateWeek.isEmpty()){
                    //bar value


                    dateIdWeek = getMinsDuration(dateWeek, dateWeek.size());
                    for(int j = 0; j < dateIdWeek.length; j++){
                        lineWeek.add(new Entry(dateIdWeek[j], typeIdWeek.get(j)));
                    }
                    for (int k = 0; k < barWeek.length; k++){
                        barWeekEntry.add(new BarEntry(k + 1, 0));
                    }

                    initHBarChart(barWeekEntry);  //统计完以后插入纵坐标就好
                    initLineChart(lineWeek);
                    lineChart.isDrawMarkersEnabled();
                    lineChart.setDrawMarkers(true);
                }else {

                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                lineChart = findViewById(R.id.lineChart);
                int[] lastweekX = {1,2,3};
                int[] lastweekY = {2,4,5};

                List<Entry> valsComp2 = new ArrayList<>();
                for(int i =0; i<lastweekX.length;i++){
                    valsComp2.add(new Entry(lastweekX[i], lastweekY[i]));
                }
                initLineChart(valsComp2);

                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

                yVals2.add(new BarEntry(1, 12));
                yVals2.add(new BarEntry(2, 4));
                yVals2.add(new BarEntry(3, 5));
                yVals2.add(new BarEntry(4, 6));
                yVals2.add(new BarEntry(5, 1));
                hBarChart = findViewById(R.id.hBarChart);
                initHBarChart(yVals2);  //统计完以后插入纵坐标就好
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                lineChart = findViewById(R.id.lineChart);

                List<Entry> valsComp2 = new ArrayList<>();


                valsComp2.add(new Entry(1, 2));
                valsComp2.add(new Entry(2, 0));
                valsComp2.add(new Entry(3, 0));

                initLineChart(valsComp2);

                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

                yVals2.add(new BarEntry(1, 12));
                yVals2.add(new BarEntry(2, 4));
                yVals2.add(new BarEntry(3, 5));
                yVals2.add(new BarEntry(4, 6));
                yVals2.add(new BarEntry(5, 1));
                hBarChart = findViewById(R.id.hBarChart);
                initHBarChart(yVals2);  //统计完以后插入纵坐标就好
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)



    /**
     * initialize the attributes of the line chart
     */
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
        String[] xStrs = new String[]{ "","就来", "阿建", "阿四","阿海","k","p","l","s","end"}; // 线图横坐标文字
        myBarChartFormatter aoz = new myBarChartFormatter(xStrs);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(aoz);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        //xAxis.setLabelCount(xStrs.size(),true);
        //xAxis.setLabelCount(3);
        // xAxis.setAxisMinimum(1f);  // from which data

        //自定义适配器，适配于Y轴
        IAxisValueFormatter patint = new MyAxisValueFormatter();

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

        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setTextColor(Color.WHITE);



        setLineChartData(lineData);
    }

    private void setLineChartData(List<Entry> lineData) {
        LineDataSet setComp1 = new LineDataSet(lineData, " Score ");
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

    private void initHBarChart(ArrayList<BarEntry> barDataset) {
        hBarChart.setOnChartValueSelectedListener(this);
        hBarChart.setDrawBarShadow(false);
        hBarChart.setDrawValueAboveBar(true);
        hBarChart.setMaxVisibleValueCount(60);
        hBarChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);


        //自定义坐标轴适配器，设置在X轴
        String[] xStrs = new String[]{ "","Type1", "Type2", "Type3","Type4","Type5","Type6","Type7"}; // 线图横坐标文字
        int[] fourColor = new int[]{Color.rgb(102, 205, 0), Color.rgb(162, 205, 90),Color.rgb(205, 190, 112),Color.rgb(238, 180, 34),Color.rgb(255, 130, 71)};
        int[] sevenColor = new int[]{Color.rgb(0, 238, 0),Color.rgb(0, 205, 0),Color.rgb(102, 205, 0), Color.rgb(162, 205, 90),Color.rgb(205, 190, 112),Color.rgb(238, 180, 34),Color.rgb(255, 130, 71)};

        //自定义适配器，适配于X轴
        myBarChartFormatter xAxisFormatter = new myBarChartFormatter(xStrs);// 自定义y轴
        XAxis xl = hBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);
        xl.setAxisMinimum(0.2f);
        xl.setValueFormatter(xAxisFormatter);

        xl.setTextColor(Color.WHITE);
        xl.setAxisLineColor(Color.WHITE);
        //xl.setDrawLabels(false);   // 不要y轴的标签！


        //对Y轴进行设置
        YAxis yl = hBarChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setGranularity(1f);
        yl.setAxisMinimum(0f);
        yl.setTextColor(Color.WHITE);
        yl.setAxisLineColor(Color.WHITE);



        // this replaces setStartAtZero(true)
        // yl.setInverted(true);

        hBarChart.getAxisRight().setEnabled(false);

        //hBarChart.getAxisRight().setDrawGridLines(false);
        //  hBarChart.getAxisRight().setDrawGridLines(false);


        //图例设置
        Legend l = hBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTextColor(Color.WHITE);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);


        setHBarChartData(barDataset,sevenColor);
        hBarChart.setFitBars(true);
        hBarChart.getDescription().setEnabled(false);
        hBarChart.animateY(2500);  // animation
    }


    /**
     * 设置水平柱形图数据的方法
     */
    private void setHBarChartData(ArrayList<BarEntry> barDataset,int[] fourColor) {
        //填充数据，在这里换成自己的数据源


        BarDataSet set1;


        if (hBarChart.getData() != null &&
                hBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) hBarChart.getData().getDataSetByIndex(0);
            set1.setValues(barDataset);

            hBarChart.getData().notifyDataChanged();
            hBarChart.notifyDataSetChanged();
        } else {

            set1 = new BarDataSet(barDataset, "Type");

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.9f);
            data.setValueTextSize(10f);
            set1.setColors(fourColor);

            hBarChart.setData(data);
        }
    }

    private void iniView() {
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
        txt_menu_new = (TextView) toolbar.findViewById(R.id.toolbar_new);
        txt_menu_new.setTypeface(font);

        //set click event
        txt_menu_new.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BssChartActivity.this, BssSurveyActivity.class);
                startActivity(intent);

            }
        });
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                BssChartActivity.this.finish();
            }
        });

        /* * * * * body * * * * * */

        // 第二行按钮
        btn1 = (MyRoundCornerButton) findViewById(R.id.btn1);
        btn2 = (MyRoundCornerButton) findViewById(R.id.btn2);
        btn3 = (MyRoundCornerButton) findViewById(R.id.btn3);
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

        //chart
        hBarChart = findViewById(R.id.hBarChart);
        lineChart = findViewById(R.id.lineChart);

        textView2 = findViewById(R.id.textView2);
        textView = findViewById(R.id.textView);


    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //get data: last week

    private void getDataLastWeek() {
        // Get a reference to our record

        Query filter = userRef.orderByChild("time").startAt(DateLastWeekMon).endAt(DateLastWeekSun);
        // Attach an listener to read the data at our posts reference
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateWeek = new ArrayList<>();
                typeIdWeek = new ArrayList<>();
                Log.e("WEEK ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot bssSnapshot: snapshot.getChildren()) {
                        EcogAndBssAnswerModel bssA = bssSnapshot.getValue(EcogAndBssAnswerModel.class);
                        String answerDate = bssA.getTime();
                        dateWeek.add(answerDate);
                        int type = bssA.getType();
                        typeIdWeek.add(type);
                        //bar data
                        //barWeek[type-1] = barWeek[type-1]+1;
                        //Log.e("WEEK ", answerDate +": "+type);
                    }
                }else{
                    /*
                    lineChart.setNoDataText("No chart data available");
                    lineChart.setNoDataTextColor(R.color.white);
                    hBarChart.setNoDataText("");
                    textView.setText("");
                    textView2.setText("");*/
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ",databaseError.getMessage());
            }

        });


    }
    //get data: last month
    private void getDataLastMonth() {
        // Get a reference to our record
        Query filter = userRef.orderByChild("time").startAt(DateLastMonthFirst).endAt(DateLastMonthLast);
        // Attach an listener to read the data at our posts reference
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateMonth = new ArrayList<>();
                typeIdMonth = new ArrayList<>();
                Log.e("MONTH ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                for (DataSnapshot bssSnapshot: snapshot.getChildren()) {
                    //add the data to the arraylist
                    EcogAndBssAnswerModel bssA = bssSnapshot.getValue(EcogAndBssAnswerModel.class);
                    String answerDate = bssA.getTime();
                    dateMonth.add(answerDate);
                    int type = bssA.getType();
                    typeIdMonth.add(type);
                    //bar data
                    barWeek[type-1] = barWeek[type-1]+1;
                    //Log.e("MONTH ", answerDate +": "+type);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ",databaseError.getMessage());
            }

        });
    }
    //get data: all
    private void getDataAll() {
        // Get a reference to our record
        // Attach an listener to read the data at our posts reference
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateAll = new ArrayList<>();
                typeIdAll = new ArrayList<>();
                Log.e("ALL ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                for (DataSnapshot bssSnapshot: snapshot.getChildren()) {
                    //add the data to the arraylist
                    EcogAndBssAnswerModel bssA = bssSnapshot.getValue(EcogAndBssAnswerModel.class);
                    String answerDate = bssA.getTime();
                    dateAll.add(answerDate);
                    int type = bssA.getType();
                    typeIdAll.add(type);
                    //bar data
                    barWeek[type-1] = barWeek[type-1]+1;
                    // Log.e("ALL ", answerDate +": "+type);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ",databaseError.getMessage());
            }

        });
    }

    public int[] getMinsDuration(ArrayList<String> dateUnion, int length){
        int[] dateIds = new int[length];
        Log.e("TAG", "getMinsDuration:" + dateUnion.isEmpty());
        if(length != 0){
            if (length == 1){
                dateIds[0] = 0;
            }else if (length > 1){
                dateIds[0] = 0;
                //calculate from the second date
                for (int i = 1; i < length; i++){
                    String time1 = dateUnion.get(i-1);
                    String time2 = dateUnion.get(i);
                    try{
                        Date previousDate = timeFormat.parse(time1);
                        Date currentDate = timeFormat.parse(time2);
                        //Note that the different is in milliseconds,
                        // it needs to be divided by 1000 to get the number of seconds.
                        long difference = currentDate.getTime() - previousDate.getTime();
                        long durationInSec = difference/1000;
                        long durationInMinus = durationInSec/60;
                        dateIds[i] = (int) durationInMinus;
//                        dateIds.add(i, (int) durationInMinus);

                    }catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return dateIds;
    }


}
