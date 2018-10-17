package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WvSurveyActivity extends AppCompatActivity{
    //UI Objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private MyEditText et_weight;
    private MyEditText et_height;
    private MyEditText et_bmi;
    private EditText etw;
    private EditText eth;
    private TextView txt_menu_back;
    private TextView txt_menu_send;
    //varialbes
    private String weight;
    private String height;
    private Double bmi = 0.0;
    private String bmi_s;
    private DecimalFormat df = new DecimalFormat("###.00");
    private String time;
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
        setContentView(R.layout.activity_wv_survey);//toolbar

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

        /* * * * * set event listener  * * * * * */
        txt_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (et_weight.isEmpty() || et_height.isEmpty() || et_bmi.isEmpty()){
                    showSendDialog();
                }else { //save data
                    sendData();
                    WvSurveyActivity.this.finish();
                    Log.e("activity","wvSurvey close after sending data");
                }

            }
        });
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                WvSurveyActivity.this.finish();
                Log.e("activity","wvSurvey close because of back_btn");
            }
        });

        //editText for weight
        etw.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable.toString())){
                    et_bmi.setTopTitle("");
                    et_bmi.setEtText("");
                    et_bmi.setInputHint("BMI");
                }else{
                    weight = et_weight.getEtText(); //kg
                    height = et_height.getEtText();  //cm
                    bmi = bmiCalculate(weight, height);
                    if(bmi != 0.0){
                        String bmis = df.format(bmi);
                        et_bmi.setTopTitle("BMI");
                        et_bmi.setEtText(bmis);
                    }else{
                        et_bmi.setTopTitle("");
                        et_bmi.setInputHint("BMI");
                    }
                }
            }
        });
        //editText for height
        eth.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable.toString())){
                    et_bmi.setTopTitle("");
                    et_bmi.setEtText("");
                    et_bmi.setInputHint("BMI");
                }else{
                    weight = et_weight.getEtText(); //kg
                    height = et_height.getEtText();  //cm
                    bmi = bmiCalculate(weight, height);
                    if(bmi != 0.0){
                        String bmis = df.format(bmi);
                        et_bmi.setTopTitle("BMI");
                        et_bmi.setEtText(bmis);
                    }else{
                        et_bmi.setTopTitle("");
                        et_bmi.setInputHint("BMI");
                    }
                }

            }
        });

    }

    private void iniView() {
        /* * * * * toolbar * * * * * */

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.wvNewToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //set icon-font
        txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setTypeface(font);
        txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
        txt_menu_send.setTypeface(font);

        /* * * * * body * * * * * */

        //set icon font for date_txt
        TextView txt_date = (TextView) findViewById(R.id.date_txt);
        txt_date.setTypeface(font);

        //input part
        et_weight = (MyEditText) findViewById(R.id.et_weight);
        et_height = (MyEditText) findViewById(R.id.et_height);
        et_bmi = (MyEditText) findViewById(R.id.et_bmi);
        etw = (EditText) et_weight.findViewById(R.id.input);
        eth = (EditText) et_height.findViewById(R.id.input);

    }

    private void sendData(){
        //date = qolResult.getDate();
        time = "2018-10-10-13-23";
        weight = et_weight.getEtText();
        height = et_height.getEtText();
        bmi_s = et_bmi.getEtText();

        //send data
        mRoot = FirebaseDatabase.getInstance().getReference();
        //通过键名，获取数据库实例对象的子节点对象
        final DatabaseReference userRef = mRoot.child(currentUserId).child("wv");

        Query checkUnique = userRef.orderByChild("time").equalTo(time);
        checkUnique.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //update existing data
                    DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                    String key = nodeDataSnapshot.getKey(); // this key is the eid of the existing data
                    userRef.child(key).child("weight").setValue(weight);
                    userRef.child(key).child("height").setValue(height);
                    userRef.child(key).child("BMI").setValue(bmi_s);
                    Log.e("Update","wv: " + time);
                    Toast.makeText(getApplicationContext() , "Updated ~" , Toast.LENGTH_SHORT).show();
                } else { //add new data
                    Map<String, String> wv = new HashMap<String, String>();
                    wv.put("time", time);
                    wv.put("weight", weight);
                    wv.put("height", height);
                    wv.put("BMI", bmi_s);
                    userRef.push().setValue(wv);
                    Log.e("Add","wv: " + time);
                    Toast.makeText(getApplicationContext() , "Added ~" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });

    }

    //calculate bmi according to weight and height
    //body mass index (BMI): kg/m2
    private Double bmiCalculate(String weight, String height){
        if(!weight.isEmpty() && !height.isEmpty()){
            Double weightkg = Double.parseDouble(weight);
            Double heightcm = Double.parseDouble(height);
            Double heightm = heightcm / 100;
            Double bmi = weightkg / (heightm * heightm);
            return bmi;
        }else{
            return 0.0;
        }

    }
    private void showSendDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_onebtn_dialog,null);
        MyRoundCornerButton dialogBtnOk = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_ok);
        dialogBtnOk.setFillet(true);
        dialogBtnOk.setRadius(13);
        dialogBtnOk.setBackColor(getResources().getColor(R.color.wv_dialog_red));
        dialogBtnOk.setBackColorSelected(getResources().getColor(R.color.wv_dialog_red_press));
        dialogBtnOk.setTextColori(getResources().getColor(R.color.white));
        dialogBtnOk.setText(getResources().getString(R.string.ok));

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.txt_warning);
        TextView dialogMessage = (TextView) dialogView.findViewById(R.id.txt_content);
        //reset the title
        dialogTitle.setText(R.string.incomplete);
        //reset the message
        dialogMessage.setText(R.string.incomplete_txt);

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(getString(R.string.icon_warning));
        builder.setView(dialogView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        alert = builder.create();
        alert.show();

        //设置组件
        dialogBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Log.e("dialog","dialog close");
                //Toast.makeText(getApplicationContext(), "对话框已关闭~", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
