package com.example.zhongzhoujianshe.healthapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class QolChartActivity extends AppCompatActivity
        implements OnChartValueSelectedListener,PopupMenu.OnMenuItemClickListener {
    @RequiresApi(api = Build.VERSION_CODES.N)

    //UI objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    //toolbar part
    private Toolbar toolbar;
    private TextView txt_menu_back;
    private TextView txt_menu_new;
    //body
    private MaterialCalendarView calendarView;
    private MyRoundCornerButton btn1;
    private MyRoundCornerButton btn2;
    private MyRoundCornerButton btn3;
    private HorizontalBarChart hBarChart;
    private LineChart lineChart;
    private TextView txt_date_range;
    private TextView txt_overview;
    private TextView txt_much;
    private TextView txt_little;
    private TextView txt_bit;
    private TextView txt_not;

    //firebase
    private String currentUserId;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //data
    private ArrayList<String> dateStr;
    private int[] dateId;  //line: x, converted from dateStr
    private ArrayList<String> valuesY;   //line:  string
    private int[] yline;//line: y, converted from valuesY    ; when click popup items, change this dataset ; get from valueWeek
    private int menu_Selected = 1; // default 1 means choose question1 ...

    //charts
    private List<Entry> lineEntry;
    private ArrayList<BarEntry> barEntry;
    private int[] barCount;
    //variables
    private String submitTime;
    private String DateLastWeekMon;
    private String DateLastWeekSun;
    private String DateLastMonthFirst;
    private String DateLastMonthLast;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        setContentView(R.layout.activity_qol_chart);

        /* * * * * initialize view  * * * * * */
        //default: current time
        Date date = new Date();
        submitTime = TimeMethods.getDateStringForDb(date); //format: "yyyy-MM-dd"
        Log.e("submitTime", submitTime);

        iniView();

        /* * * * * get date of today, last month and last week * * * * * */

        //get today's date
        //DateToday = TimeMethods.getDateToday();
        //Log.e("today","----"+DateToday);

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

                btn2.setTextColori(getResources().getColor(R.color.chartDarkBlue));
                btn1.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
                btn3.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
                btn2.setBackColor(getResources().getColor(R.color.qolChartTopPurple));
                btn1.setBackColor(getResources().getColor(R.color.chartDarkBlue));
                btn3.setBackColor(getResources().getColor(R.color.chartDarkBlue));

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                getDataAll(); //get data for : dateWeek & typeIdWeek & barWeek
                // getChartData();

                btn3.setTextColori(getResources().getColor(R.color.chartDarkBlue));
                btn2.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
                btn1.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
                btn3.setBackColor(getResources().getColor(R.color.qolChartTopPurple));
                btn2.setBackColor(getResources().getColor(R.color.chartDarkBlue));
                btn1.setBackColor(getResources().getColor(R.color.chartDarkBlue));

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)

    public void setBtn1Click(){
        btn1.setTextColori(getResources().getColor(R.color.chartDarkBlue));
        btn2.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
        btn3.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
        btn1.setBackColor(getResources().getColor(R.color.qolChartTopPurple));
        btn2.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        btn3.setBackColor(getResources().getColor(R.color.chartDarkBlue));
    }

    /**
     * 初始化折线图控件属性
     */
    private void initLineChart(final List<Entry> lineData) {

        lineChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
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
        //String[] xStrs = new String[]{ "","what", "噢", "冬","sha","k","cat","l","s","end"}; // 线图横坐标文字
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
        IAxisValueFormatter patint = new MyAxisValueFormatter();

        YAxis leftAxis = lineChart.getAxisLeft();

        leftAxis.setValueFormatter(patint);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(-0.5f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

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




        LineDataSet setComp1 = new LineDataSet(lineData, " question ");
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
        hBarChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
        hBarChart.setDrawBarShadow(false);
        hBarChart.setDrawValueAboveBar(true);
        hBarChart.setMaxVisibleValueCount(60);
        hBarChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);


        //自定义坐标轴适配器，设置在X轴
        String[] xStrs = new String[]{ "","Not at All", "A Little", "Quite a Bit","Very Much"}; // 线图横坐标文字
        int[] fourColor = new int[]{Color.rgb(102, 205, 0), Color.rgb(162, 205, 90),Color.rgb(205, 190, 112),Color.rgb(238, 180, 34),Color.rgb(255, 130, 71)};
        int[] sevenColor = new int[]{Color.rgb(0, 238, 0),Color.rgb(0, 205, 0),Color.rgb(102, 205, 0), Color.rgb(162, 205, 90),Color.rgb(205, 190, 112),Color.rgb(238, 180, 34),Color.rgb(255, 130, 71)};





        //自定义适配器，适配于X轴
        myBarChartFormatter xAxisFormatter = new myBarChartFormatter(xStrs);// 自定义y轴
        XAxis xl = hBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);
        xl.setAxisMinimum(0f);
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
        l.setFormSize(8f);
        l.setXEntrySpace(4f);


        setHBarChartData(barDataset,fourColor);
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
            data.setValueTextColor(Color.WHITE);
            data.setBarWidth(0.9f);
            data.setValueTextSize(10f);
            set1.setColors(fourColor);










            hBarChart.setData(data);
        }
    }

    /**
     * 设置折线图的数据
     */

    @Override
    public void onNothingSelected() {

    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        //set click event
        txt_menu_cog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(QolChartActivity.this, view);
                popup.setOnMenuItemClickListener(QolChartActivity.this);
                popup.inflate(R.menu.qol_chart_menu);
                popup.show();
            }
        });
        txt_menu_new.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent=new Intent(QolChartActivity.this,QolSurveyActivity.class);
                intent.putExtra("submitTime", submitTime); // send the time to survey
                startActivity(intent);

            }
        });
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                QolChartActivity.this.finish();
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
        btn1.setStroke(3, getResources().getColor(R.color.qolChartTopPurple));
        btn3.setStroke(3, getResources().getColor(R.color.qolChartTopPurple));
        btn2.setBorderTop(true);
        btn2.setBorderBottom(true);
        btn2.setBorderWidth(5);
        btn2.setBorderColor(getResources().getColor(R.color.qolChartTopPurple));
        //background color
        btn1.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        btn2.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        btn3.setBackColor(getResources().getColor(R.color.chartDarkBlue));
        //text color
        btn1.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
        btn2.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
        btn3.setTextColori(getResources().getColor(R.color.qolChartTopPurple));
        //text
        btn1.setText(getResources().getString(R.string.chart_week));
        btn2.setText(getResources().getString(R.string.chart_month));
        btn3.setText(getResources().getString(R.string.chart_all));

        TextView txt_date = (TextView) findViewById(R.id.txt_date);
        txt_date.setTypeface(font);
        //set click event
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showCalendar();
                //Toast.makeText(getApplicationContext() , "choose date" , Toast.LENGTH_SHORT).show();
            }
        });

        //chart
        hBarChart = findViewById(R.id.hBarChart);
        lineChart = findViewById(R.id.lineChart);

        txt_overview = findViewById(R.id.overview);
        txt_date_range = findViewById(R.id.date_range);
        txt_much = findViewById(R.id.textView3);
        txt_little = findViewById(R.id.textView5);
        txt_bit = findViewById(R.id.textView4);
        txt_not = findViewById(R.id.textView6);
    }

    private void showCalendar() {
        //initialize layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.calendar,null);
        TextView tv_chart = (TextView) dialogView.findViewById(R.id.tv_chart);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        tv_chart.setTypeface(font);

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(getString(R.string.icon_warning));
        builder.setView(dialogView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alert = builder.create();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alert.show();
        }


        tv_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    alert.dismiss();
                }
            }
        });

        calendarView = (MaterialCalendarView) dialogView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(
                    @NonNull MaterialCalendarView materialCalendarView,
                    @NonNull CalendarDay calendarDay,
                    boolean b) {
                submitTime = FORMATTER.format(calendarDay.getDate()); //format: "yyyy-MM-dd"
                Log.e("submitTime", submitTime);
            }
        });
    }

    //set view shows when no data available
    public void setNoDataView(){
        lineChart.setNoDataText("No chart data available");
        lineChart.setNoDataTextColor(getResources().getColor(R.color.white));
        txt_date_range.setText("");
        txt_overview.setText("");
        txt_much.setText("");
        txt_little.setText("");
        txt_bit.setText("");
        txt_not.setText("");

    }

    //get data: last week
    private void getDataLastWeek() {
        Log.e("Start", "getDataLastWeek");
        // Get a reference to our record
        mRoot = FirebaseDatabase.getInstance().getReference();
        userRef = mRoot.child(currentUserId).child("qol");
        Query filter = userRef.orderByChild("date").startAt(DateLastWeekMon).endAt(DateLastWeekSun);
        // Attach an listener to read the data at our posts reference
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateStr = new ArrayList<>();
                valuesY = new ArrayList<>();
                Log.e("WEEK ", "-- There are " + String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot wvSnapshot: snapshot.getChildren()) {

                        Map<String, String> qolA = (Map)wvSnapshot.getValue();
                        String answerDate = qolA.get("date");

                        dateStr.add(answerDate);

                        String answerForSelectedQ = qolA.get(String.valueOf(menu_Selected));

                        valuesY.add(answerForSelectedQ);

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
        userRef = mRoot.child(currentUserId).child("qol");
        Query filter = userRef.orderByChild("date").startAt(DateLastMonthFirst).endAt(DateLastMonthLast);
        // Attach an listener to read the data at our posts reference
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateStr = new ArrayList<>();
                valuesY = new ArrayList<>();
                Log.e("MONTH ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot wvSnapshot: snapshot.getChildren()) {
                        Map<String, String> qolA = (Map)wvSnapshot.getValue();
                        String answerDate = qolA.get("date");

                        dateStr.add(answerDate);

                        String answerForSelectedQ = qolA.get(String.valueOf(menu_Selected));

                        valuesY.add(answerForSelectedQ);

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
        userRef = mRoot.child(currentUserId).child("qol");
        // Get a reference to our record
        // Attach an listener to read the data at our posts reference
        Query filter = userRef.orderByChild("date");
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dateStr = new ArrayList<>();
                valuesY = new ArrayList<>();
                Log.e("ALL ", "-- There are " +String.valueOf(snapshot.getChildrenCount()));
                if (snapshot.exists()){
                    for (DataSnapshot wvSnapshot: snapshot.getChildren()) {

                        Map<String, String> qolA = (Map)wvSnapshot.getValue();
                        String answerDate = qolA.get("date");

                        dateStr.add(answerDate);

                        String answerForSelectedQ = qolA.get(String.valueOf(menu_Selected));

                        valuesY.add(answerForSelectedQ);

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



    public void getChartData(){
//        Log.e("getChartData", "is empty:"+ String.valueOf(dateStr.isEmpty()));

        if (!dateStr.isEmpty()){
            //set text for time ranges for bar chart

            txt_overview.setText(getResources().getString(R.string.overview));
            txt_date_range.setText(TimeMethods.getDateRange(dateStr, dateStr.size(), false));
            txt_much.setText(getResources().getString(R.string.qol_surAnswer_much));
            txt_little.setText(getResources().getString(R.string.qol_surAnswer_little));
            txt_bit.setText(getResources().getString(R.string.qol_surAnswer_bit));
            txt_not.setText(getResources().getString(R.string.qol_surAnswer_not));

            lineEntry = new ArrayList<>();
            barEntry = new ArrayList<>();
            barCount = new int[]{0,0,0,0,0};

            dateId = TimeMethods.getDayDuration(dateStr, dateStr.size());

            if (valuesY.size() != 0 ){  //has available data
                yline = new int[valuesY.size()];
                for (int i = 0; i < valuesY.size(); i ++){
                    switch (valuesY.get(i)){
                        case "Not at All":
                            yline[i] = 0;
                            break;
                        case "A Little":
                            yline[i] = 1;
                            break;
                        case "Quite a Bit":
                            yline[i] = 2;
                            break;
                        case "Very Much":
                            yline[i] = 3;
                            break;
                    }

                }
            }
            if (yline.length != 0 ){
                //dateIdWeek = getDurationEqual(dateWeek, dateWeek.size());
                for(int j = 0; j < dateId.length; j++){

                    List<String> label = TimeMethods.getEveryLabel(dateStr, dateStr.size(), false);

                    lineEntry.add(new Entry(dateId[j], yline[j],label.get(j)));

                    // lineWeek.add(new Entry(dateIdWeek[j], typeIdWeek.get(j)));

                    for(int k = 1; k < barCount.length; k++){
                        if(yline[j] == k - 1){
                            barCount[k] = barCount[k] + 1;
                        }
                    }
                }
                for (int k = 1; k < barCount.length; k++){
                    barEntry.add(new BarEntry(k, barCount[k]));
                }
            }

            initHBarChart(barEntry);  // initialize bar chart

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                initLineChart(lineEntry); // initialize line chart
            }
            lineChart.isDrawMarkersEnabled();
            lineChart.setDrawMarkers(true);
        }else {
            lineChart.setNoDataText("No chart data available");
            lineChart.setNoDataTextColor(getResources().getColor(R.color.white));
            txt_date_range.setText("");
            txt_overview.setText("");
            txt_much.setText("");
            txt_little.setText("");
            txt_bit.setText("");
            txt_not.setText("");
        }
    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Log.e("WVCHART", "cog menu selected: " + menuItem.getTitle());
        switch (menuItem.getItemId()) {
            case R.id.q1:
                menu_Selected = 1;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q2:
                menu_Selected = 2;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q3:
                menu_Selected = 3;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q4:
                menu_Selected = 4;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q5:
                menu_Selected = 5;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q6:
                menu_Selected = 6;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q7:
                menu_Selected = 7;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q8:
                menu_Selected = 8;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q9:
                menu_Selected = 9;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q10:
                menu_Selected = 10;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q11:
                menu_Selected = 11;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q12:
                menu_Selected = 12;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q13:
                menu_Selected = 13;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q14:
                menu_Selected = 14;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q15:
                menu_Selected = 15;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q16:
                menu_Selected = 16;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q17:
                menu_Selected = 17;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q18:
                menu_Selected = 18;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q19:
                menu_Selected = 19;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q20:
                menu_Selected = 20;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q21:
                menu_Selected = 21;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q22:
                menu_Selected = 22;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q23:
                menu_Selected = 23;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q24:
                menu_Selected = 24;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q25:
                menu_Selected = 25;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q26:
                menu_Selected = 26;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q27:
                menu_Selected = 27;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q28:
                menu_Selected = 28;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q29:
                menu_Selected = 29;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q30:
                menu_Selected = 30;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q31:
                menu_Selected = 31;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q32:
                menu_Selected = 32;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q33:
                menu_Selected = 33;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q34:
                menu_Selected = 34;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q35:
                menu_Selected = 35;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q36:
                menu_Selected = 36;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q37:
                menu_Selected = 37;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q38:
                menu_Selected = 38;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q39:
                menu_Selected = 39;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q40:
                menu_Selected = 40;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q41:
                menu_Selected = 41;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q42:
                menu_Selected = 42;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q43:
                menu_Selected = 43;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q44:
                menu_Selected = 44;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q45:
                menu_Selected = 45;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q46:
                menu_Selected = 46;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q47:
                menu_Selected = 47;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q48:
                menu_Selected = 48;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q49:
                menu_Selected = 49;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q50:
                menu_Selected = 50;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            case R.id.q51:
                menu_Selected = 51;
                getDataLastWeek();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setBtn1Click();
                }
                return true;
            default:
                return false;
        }
    }
}
