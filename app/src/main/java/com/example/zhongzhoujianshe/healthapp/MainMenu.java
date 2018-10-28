package com.example.zhongzhoujianshe.healthapp;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //check for user authorization

        /*
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser == null){
            Intent intent = new Intent();
            intent.setClass(MainMenu.this, LoginActivity.class);
            startActivity(intent);
        }*/

        // set iconfont
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");//记得加上这句


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

        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainMenu.this, view);
                popup.setOnMenuItemClickListener(MainMenu.this);
                popup.inflate(R.menu.main_catalog_menu);
                popup.show();
            }
        });

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


        Calendar cal = Calendar.getInstance();
        String mMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
        TextView showdate= (TextView) findViewById(R.id.showdate);
        showdate.setText(mMonth);


    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent();
                intent.setClass(MainMenu.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;

        }
    }
}

