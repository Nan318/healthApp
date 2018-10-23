package com.example.zhongzhoujianshe.healthapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WvChartActivity extends AppCompatActivity
        implements OnChartValueSelectedListener,PopupMenu.OnMenuItemClickListener {
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
    private TextView txt_date_range;
    private TextView txt_overview;
    private TextView table_weight;
    private TextView table_height;
    private TextView table_bmi;
    private TextView table_min;
    private TextView table_max;
    private TextView table_avg;
    private TextView table_weight_min;
    private TextView table_weight_max;
    private TextView table_weight_avg;
    private TextView table_height_min;
    private TextView table_height_max;
    private TextView table_height_avg;
    private TextView table_bmi_min;
    private TextView table_bmi_max;
    private TextView table_bmi_avg;
    private TableLayout tableLayout;

    //firebase
    private String currentUserId;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //data
    private ArrayList<String> dateStr;
    private int[] dateId;  //line: x, converted from date
    private ArrayList<float[]> valuesY;   //line:  ; for the float[]: [0] is weight,[1] is height, [2] is bmi
    private float[] yline;//line: y    ; when click popup items, change this dataset ; get from valueWeek
    private int menu_Selected = 0; // default 0 means choose weight ; 1 for height; 2 for bmi




    // Double[0][0]: min of weight; [0][1]: max of weight; [0][2]: avg of weight
    // Double[1][0]: min of height; [1][1]: max of height; [1][2]: avg of height
    // Double[2][0]: min of bmi;    [2][1]: max of bmi;    [2][2]: avg of bmi
    private float[][] tableValues;
    //charts
    private List<Entry> lineEntry;

    //variables
    private String DateLastWeekMon;
    private String DateLastWeekSun;
    private String DateLastMonthFirst;
    private String DateLastMonthLast;

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

        /* * * * * initialize view  * * * * * */
        iniView();

        /* * * * * get date of today, last month and last week * * * * * */

        //get last week (date range)
        //Date date = new Date();
        String[] lastweek = TimeMethods.getDateLastWeek();
        DateLastWeekMon = lastweek[0];
        DateLastWeekSun = lastweek[1];

        //get last month
        String[] lastmonth = TimeMethods.getDateLastMonth();
        DateLastMonthLast = lastmonth[1];
        DateLastMonthFirst = lastmonth[0];


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
                    Log.e("TAG", "onAuthStateChanged:signed_in:" + currentUserId);
                    getDataLastWeek(); //get data for : dateWeek & typeIdWeek & barWeek
                   // getChartData();
                    setBtn1Click();   //same as default
                }else {
                    Log.e("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };


        //click
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                getDataLastWeek(); //get data for : dateWeek & typeIdWeek & barWeek
               // getChartData();
                setBtn1Click();   //same as default
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                getDataLastMonth(); //get data for : dateWeek & typeIdWeek & barWeek
                getChartData();

                btn2.setTextColori(getResources().getColor(R.color.chartDarkBlue));
                btn1.setTextColori(getResources().getColor(R.color.wvChartTopPink));
                btn3.setTextColori(getResources().getColor(R.color.wvChartTopPink));
                btn2.setBackColor(getResources().getColor(R.color.wvChartTopPink));
                btn1.setBackColor(getResources().getColor(R.color.chartDarkBlue));
                btn3.setBackColor(getResources().getColor(R.color.chartDarkBlue));



            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                getDataAll(); //get data for : dateWeek & typeIdWeek & barWeek
               // getChartData();

                btn3.setTextColori(getResources().getColor(R.color.chartDarkBlue));
                btn2.setTextColori(getResources().getColor(R.color.wvChartTopPink));
                btn1.setTextColori(getResources().getColor(R.color.wvChartTopPink));
                btn3.setBackColor(getResources().getColor(R.color.wvChartTopPink));
                btn2.setBackColor(getResources().getColor(R.color.chartDarkBlue));
                btn1.setBackColor(getResources().getColor(R.color.chartDarkBlue));

            }
        });



    }

    public void setBtn1Click(){
        btn1.setTextColori(getResources().getColor(R.color.chartDarkBlue));
        btn2.setTextColori(getResources().getColor(R.color.wvChartTopPink));
        btn3.setTextColori(getResources().getColor(R.color.wvChartTopPink));
        btn1.setBackColor(getResources().getColor(R.color.wvChartTopPink));
        btn2.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        btn3.setBackColor(getResources().getColor(R.color.chartDarkBlue));

        //lineChart = findViewById(R.id.lineChart);


    }

    public void getChartData(){
//        Log.e("getChartData", "is empty:"+ String.valueOf(dateStr.isEmpty()));

        if (!dateStr.isEmpty()){
            //set text for time ranges for bar chart

            txt_overview.setText(getResources().getString(R.string.overview));
            txt_date_range.setText(TimeMethods.getDateRange(dateStr, dateStr.size(), false));

            lineEntry = new ArrayList<>();

            dateId = TimeMethods.getDayDuration(dateStr, dateStr.size());

            if (valuesY.size() != 0 ){  //has available data
                yline = new float[valuesY.size()];
                for (int i = 0; i < valuesY.size(); i ++){
                    if (valuesY.get(i).length == 3){
                        switch (menu_Selected){
                            case 0:
                                yline[i] = valuesY.get(i)[0];
                                break;
                            case 1:
                                yline[i] = valuesY.get(i)[1];
                                break;
                            case 2:
                                yline[i] = valuesY.get(i)[2];
                                break;
                        }

                    }

                }
            }

            if (yline.length != 0 ){
                //dateIdWeek = getDurationEqual(dateWeek, dateWeek.size());
                for(int j = 0; j < dateId.length; j++){

                    List<String> label = TimeMethods.getEveryLabel(dateStr, dateStr.size(), true);

                    lineEntry.add(new Entry(dateId[j], yline[j],label.get(j)));

                    // lineWeek.add(new Entry(dateIdWeek[j], typeIdWeek.get(j)));
                }
            }


            //table
            tableValues = new float[3][3];
            getTableValues(tableValues);

            table_weight_min.setText(String.valueOf(tableValues[0][0]));
            table_weight_max.setText(String.valueOf(tableValues[0][1]));
            table_weight_avg.setText(String.valueOf(tableValues[0][2]));
            table_height_min.setText(String.valueOf(tableValues[1][0]));
            table_height_max.setText(String.valueOf(tableValues[1][1]));
            table_height_avg.setText(String.valueOf(tableValues[1][2]));
            table_bmi_min.setText(String.valueOf(tableValues[2][0]));
            table_bmi_max.setText(String.valueOf(tableValues[2][1]));
            table_bmi_avg.setText(String.valueOf(tableValues[2][2]));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                initLineChart(lineEntry);
            }
            lineChart.isDrawMarkersEnabled();
            lineChart.setDrawMarkers(true);
        }else {
            setNoDataView();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initLineChart(final List<Entry> lineData) {

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
        //String[] xStrs = new String[]{ "","what", "噢", "冬","sha","k","p","l","s","end"}; // 线图横坐标文字
        //myBarChartFormatter aoz = new myBarChartFormatter(xStrs);




        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        if (lineData.size() > 0) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    if ((int) value >= lineData.size()) {
                        return "";
                    } else {
                        return lineData.get((int) value).getData() + "";
                    }
                }
            });
        }

        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        //xAxis.setValueFormatter(aoz);
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
        txt_menu_cog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(WvChartActivity.this, view);
                popup.setOnMenuItemClickListener(WvChartActivity.this);
                popup.inflate(R.menu.wv_chart_menu);
                popup.show();
            }
        });

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

        btn1 = (MyRoundCornerButton) findViewById(R.id.btn1);
        btn2 = (MyRoundCornerButton) findViewById(R.id.btn2);
        btn3 = (MyRoundCornerButton) findViewById(R.id.btn3);
        //shape left to right: left corner, rectangle, right corner
        btn1.setFillet(true);
        btn1.setPartRadius(15, 0, 0, 15);
        btn3.setFillet(true);
        btn3.setPartRadius(0, 15, 15, 0);
        //border
        btn1.setStroke(3, getResources().getColor(R.color.wvChartTopPink));
        btn3.setStroke(3, getResources().getColor(R.color.wvChartTopPink));
        btn2.setBorderTop(true);
        btn2.setBorderBottom(true);
        btn2.setBorderWidth(5);
        btn2.setBorderColor(getResources().getColor(R.color.wvChartTopPink));
        //background color
        btn1.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        btn2.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        btn3.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        //text color
        btn1.setTextColori(getResources().getColor(R.color.wvChartTopPink));
        btn2.setTextColori(getResources().getColor(R.color.wvChartTopPink));
        btn3.setTextColori(getResources().getColor(R.color.wvChartTopPink));
        //text
        btn1.setText(getResources().getString(R.string.chart_week));
        btn2.setText(getResources().getString(R.string.chart_month));
        btn3.setText(getResources().getString(R.string.chart_all));

        //chart
        lineChart = findViewById(R.id.lineChart);

        txt_overview = findViewById(R.id.overview);
        txt_date_range = findViewById(R.id.date_range);

        //table
        table_min = findViewById(R.id.textView9);
        table_max = findViewById(R.id.textView8);
        table_avg = findViewById(R.id.textView7);
        table_weight = findViewById(R.id.textView14);
        table_height = findViewById(R.id.textView18);
        table_bmi = findViewById(R.id.textView22);
        table_weight_min = findViewById(R.id.weight_min);
        table_weight_max = findViewById(R.id.weight_max);
        table_weight_avg = findViewById(R.id.weight_avg);
        table_height_min = findViewById(R.id.height_min);
        table_height_max = findViewById(R.id.height_max);
        table_height_avg = findViewById(R.id.height_avg);
        table_bmi_min = findViewById(R.id.bmi_min);
        table_bmi_max = findViewById(R.id.bmi_max);
        table_bmi_avg = findViewById(R.id.bmi_avg);




    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //set view shows when no data available
    public void setNoDataView(){
        lineChart.setNoDataText("No chart data available");
        lineChart.setNoDataTextColor(getResources().getColor(R.color.white));
        txt_date_range.setText("");
        txt_overview.setText("");
        table_min.setText("");
        table_max.setText("");
        table_avg.setText("");
        table_weight.setText("");
        table_height.setText("");
        table_bmi.setText("");
        table_weight_min.setText("");
        table_weight_max.setText("");
        table_weight_avg.setText("");
        table_height_min.setText("");
        table_height_max.setText("");
        table_height_avg.setText("");
        table_bmi_min.setText("");
        table_bmi_max.setText("");
        table_bmi_avg.setText("");

    }

    //get data: last week
    private void getDataLastWeek() {
        Log.e("Start", "getDataLastWeek");
        // Get a reference to our record
        mRoot = FirebaseDatabase.getInstance().getReference();
        userRef = mRoot.child(currentUserId).child("wv");
        Query filter = userRef.orderByChild("time").startAt(DateLastWeekMon).endAt(DateLastWeekSun);
        // Attach an listener to read the data at our posts reference
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateStr = new ArrayList<>();
                valuesY = new ArrayList<>();
                Log.e("WEEK ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot wvSnapshot: snapshot.getChildren()) {

                        WvAnswerModel wvA = wvSnapshot.getValue(WvAnswerModel.class);
                        String answerDate = wvA.getTime();
                        String weight = wvA.getWeight();
                        String height = wvA.getHeight();
                        String bmi = wvA.getBmi();

                        dateStr.add(answerDate);
                        float weight_float = Float.parseFloat(weight);
                        float height_float = Float.parseFloat(height);
                        float bmi_float = Float.parseFloat(bmi);
                        float[] value = {weight_float, height_float, bmi_float};
                        valuesY.add(value);

                        Log.e("Go to", "getChartData");
                        getChartData();
                    }
                }else{
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
        Log.e("Start", "getDataLastMonth");

        // Get a reference to our record
        mRoot = FirebaseDatabase.getInstance().getReference();
        userRef = mRoot.child(currentUserId).child("wv");
        Query filter = userRef.orderByChild("time").startAt(DateLastMonthFirst).endAt(DateLastMonthLast);
        // Attach an listener to read the data at our posts reference
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateStr = new ArrayList<>();
                valuesY = new ArrayList<>();
                Log.e("MONTH ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot wvSnapshot: snapshot.getChildren()) {
                        WvAnswerModel wvA = wvSnapshot.getValue(WvAnswerModel.class);
                        String answerDate = wvA.getTime();
                        String weight = wvA.getWeight();
                        String height = wvA.getHeight();
                        String bmi = wvA.getBmi();

                        dateStr.add(answerDate);
                        float weight_float = Float.parseFloat(weight);
                        float height_float = Float.parseFloat(height);
                        float bmi_float = Float.parseFloat(bmi);
                        float[] value = {weight_float, height_float, bmi_float};
                        valuesY.add(value);

                        Log.e("Go to", "getChartData");
                        getChartData();
                    }
                }else{
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
        Log.e("Start", "getDataAll");

        mRoot = FirebaseDatabase.getInstance().getReference();
        userRef = mRoot.child(currentUserId).child("wv");
        // Get a reference to our record
        // Attach an listener to read the data at our posts reference
        Query filter = userRef.orderByChild("time");
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateStr = new ArrayList<>();
                valuesY = new ArrayList<>();
                Log.e("ALL ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot wvSnapshot: snapshot.getChildren()) {

                        WvAnswerModel wvA = wvSnapshot.getValue(WvAnswerModel.class);
                        String answerDate = wvA.getTime();
                        String weight = wvA.getWeight();
                        String height = wvA.getHeight();
                        String bmi = wvA.getBmi();

                       // Log.e("getDataAll", "time:"+answerDate);
                       // Log.e("getDataAll", "bmi:"+bmi);

                        dateStr.add(answerDate);
                        Log.e("getDataAll", "after add, dateStr size: " + String.valueOf(dateStr.size()));
                        float weight_float = Float.parseFloat(weight);
                        float height_float = Float.parseFloat(height);
                        float bmi_float = Float.parseFloat(bmi);
                        float[] value = {weight_float, height_float, bmi_float};
                        valuesY.add(value);
                        Log.e("getDataAll", "after get, valuesY size: " + String.valueOf(valuesY.size()));

                        Log.e("Go to", "getChartData");
                        getChartData();
                    }
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ",databaseError.getMessage());
            }

        });

        Log.e("getDataAll", "after get, dateStr size: " + String.valueOf(dateStr.size()));

    }

    //for the float[] in valuesY: [0] is weight,[1] is height, [2] is bmi
    // float[0][0]: min of weight; [0][1]: max of weight; [0][2]: avg of weight
    // float[1][0]: min of height; [1][1]: max of height; [1][2]: avg of height
    // float[2][0]: min of bmi;    [2][1]: max of bmi;    [2][2]: avg of bmi
    public void getTableValues(float[][] table){
        if(!valuesY.isEmpty()){
            float[] first = valuesY.get(0);
            //max and min
            float wmax = first[0], hmax = first[1], bmax = first[2];
            float wmin = first[0], hmin = first[1], bmin = first[2];
            for(int i = 1 ;i < valuesY.size() ; i++){
                float[] current = valuesY.get(i);
                if(wmax < current[0])
                    wmax = current[0];
                if(hmax < current[1])
                    hmax = current[1];
                if(bmax < current[2])
                    bmax = current[2];

                if(wmin > current[0])
                    wmin = current[0];
                if(hmin > current[1])
                    hmin = current[1];
                if(bmin > current[2])
                    bmin = current[2];
            }
            //sum
            float wsum = 0, hsum = 0 , bsum = 0;
            for(int i = 0 ; i < valuesY.size(); i++){
                float[] current = valuesY.get(i);
                wsum = wsum + current[0];
                hsum = hsum + current[1];
                bsum = bsum + current[2];
            }
            //avgerage
            float wavg = wsum / valuesY.size() ;
            float havg = hsum / valuesY.size() ;
            float bavg = bsum / valuesY.size() ;

            table[0][0] = wmin;
            table[0][1] = wmax;
            table[0][2] = wavg;
            table[1][0] = hmin;
            table[1][1] = hmax;
            table[1][2] = havg;
            table[2][0] = bmin;
            table[2][1] = bmax;
            table[2][2] = bavg;


        }




    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Log.e("WVCHART", "cog menu selected: " + menuItem.getTitle());
        switch (menuItem.getItemId()) {
            case R.id.weight_item:
                menu_Selected = 0;
                getDataLastWeek();
                setBtn1Click();
                return true;
            case R.id.height_item:
                menu_Selected = 1;
                getDataLastWeek();
                setBtn1Click();
                return true;
            case R.id.bmi_item:
                menu_Selected = 2;
                getDataLastWeek();
                setBtn1Click();
                return true;
            default:
                return false;
        }
    }
}
