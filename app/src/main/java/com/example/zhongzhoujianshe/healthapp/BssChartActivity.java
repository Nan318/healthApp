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
    private FirebaseAuth.AuthStateListener mAuthListener;

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


    }
}
