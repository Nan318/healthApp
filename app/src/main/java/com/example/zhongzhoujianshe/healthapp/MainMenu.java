package com.example.zhongzhoujianshe.healthapp;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //check for user authorization
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser == null){
            Intent intent = new Intent();
            intent.setClass(MainMenu.this, LoginActivity.class);
            //intent.putExtra("Name", "feng88724");
            startActivity(intent);
        }


        // set iconfont
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");//记得加上这句

        /* Button bbb = (Button) findViewById(R.id.button);
        bbb.setTypeface(font);*/

        TextView run = (TextView) findViewById(R.id.runtext);
        run.setTypeface(font);
        TextView poo = (TextView) findViewById(R.id.pootext);
        poo.setTypeface(font);
        TextView heart = (TextView) findViewById(R.id.hearttext);
        heart.setTypeface(font);
        TextView dice= (TextView) findViewById(R.id.dicetext);
        dice.setTypeface(font);
        TextView catalog= (TextView) findViewById(R.id.catalog);
        catalog.setTypeface(font);
        TextView inbox= (TextView) findViewById(R.id.inbox);
        inbox.setTypeface(font);

        /* * * * * go to function pages * * * * * */

        ConstraintLayout qol = (ConstraintLayout) findViewById(R.id.first);
        qol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, QolChartActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout bss = (ConstraintLayout) findViewById(R.id.second);
        bss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, BssChartActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout wv = (ConstraintLayout) findViewById(R.id.third);
        wv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, WvChartActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout ecog = (ConstraintLayout) findViewById(R.id.fourth);
        ecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, EcogChartActivity.class);
                startActivity(intent);
            }
        });



        // reserve iconpont
        TextView runa= (TextView) findViewById(R.id.runtext);
        runa.setScaleY(-1);

        //get time

        SimpleDateFormat   formatter   =   new   SimpleDateFormat("HH");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        int hour = Integer.parseInt(str);
        if (hour>=18 && hour<24){
            TextView showtime= (TextView) findViewById(R.id.showtime);
            showtime.setText("Good Evening!");
        }
        else if (hour<12&&hour>=6){
            TextView showtime= (TextView) findViewById(R.id.showtime);
            showtime.setText("Good Morning!");
        }
        else if (6>hour && hour >=0){
            TextView showtime= (TextView) findViewById(R.id.showtime);
            showtime.setText("Good Night!");
        }
        else{
            TextView showtime= (TextView) findViewById(R.id.showtime);
            showtime.setText("Good Afternoon!");
        }

        // get date
        /*SimpleDateFormat   dateFormat   =   new   SimpleDateFormat   ("yyyy-MM-dd");
        Date cur = new Date(System.currentTimeMillis());
        String da = dateFormat.format(cur);

        TextView showdate= (TextView) findViewById(R.id.showdate);
        showdate.setText(da);*/

        Calendar cal = Calendar.getInstance();
        String mMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        TextView showdate= (TextView) findViewById(R.id.showdate);
        showdate.setText(mMonth);


    }

}

