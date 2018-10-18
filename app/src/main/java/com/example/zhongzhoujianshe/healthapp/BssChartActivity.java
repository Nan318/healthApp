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

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class BssChartActivity extends AppCompatActivity {
    //UI objects
    //toolbar part
    private Toolbar toolbar;
    private TextView txt_menu_back;
    private TextView txt_menu_new;
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
    private int[] dateIdAll;  //x, converted from dateAll
    private int[] dateIdMonth;  //x, converted from dateMonth
    private int[] dateIdWeek; //x, converted from dateWeek
    private ArrayList<Integer> typeIdAll;   //y
    private ArrayList<Integer> typeIdMonth;  //y
    private ArrayList<Integer> typeIdWeek; //y

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
                    getDataLastWeek();
                    getDataLastMonth();
                    getDataAll();

                    //test date id & type id
                    //i.e. test values for x and y
                    dateIdAll = getMinsDuration(dateAll, dateAll.size());
                    dateIdMonth = getMinsDuration(dateMonth, dateMonth.size());
                    dateIdWeek = getMinsDuration(dateWeek, dateWeek.size());
                    for(int i=0; i<dateIdAll.length;i++){
                        Log.e("ALL", dateIdAll[i] +": " + typeIdAll.get(i));
                    }
                    for(int i=0; i<dateIdMonth.length;i++){
                        Log.e("MONTH", dateIdMonth[i] +": " + typeIdMonth.get(i));
                    }
                    for(int i=0; i<dateIdWeek.length;i++){
                        Log.e("WEEK", dateIdWeek[i] +": " + typeIdWeek.get(i));
                    }

                }else{
                    Log.e("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };







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
                //Log.e("today","----"+DateToday);
                //Log.e("lastweek",DateLastWeekMon + " , " + DateLastWeekSun);
                //Log.e("lastmonth",DateLastMonthFirst + " , " + DateLastMonthLast);
                BssChartActivity.this.finish();
            }
        });

        /* * * * * body * * * * * */


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
                        //Log.e("WEEK ", answerDate +": "+type);
                    }
                }else{
                    //Log.e("WEEK ", "none");
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
                dateIds[0] = 1;
            }else if (length > 1){
                dateIds[0] = 1;
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