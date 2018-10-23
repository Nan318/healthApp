package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QolSurveyActivity extends AppCompatActivity{
    //UI Objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private ViewPager viewPager;
    private QolSurveyFragmentPagerAdapter qolSurveyAdapter;
    private TextView txt_menu_send;
    private TextView txt_menu_back;
    private Toolbar toolbar;
    private ListView listView;
    //firebase
    private String currentUserId;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //varialbes
    private QolSurveyAnswerModel qolResult = new QolSurveyAnswerModel();
    private int minIndex = 0; //start from the first question
    private int minId = 1;
    private String date;

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
        setContentView(R.layout.activity_qol_survey);

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

        /* * * * * set click event * * * * * */
        //send btn
        txt_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(qolResult.getResults().size()<51){
                    //Toast.makeText(getApplicationContext(), "finish"+qolResult.getResults().size(), Toast.LENGTH_LONG).show();
                    showSendDialog();
                }else if(qolResult.getResults().size() == 51){
                    showConfirmDialog();
                }

            }
        });
        //back btn
        txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showBackDialog();
            }
        });


    }

    private void iniView() {
        /* * * * * toolbar * * * * */
        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");

        toolbar = (Toolbar) findViewById(R.id.qolSurveyToolbar);
        // Title
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //set icon-font: send
        txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
        txt_menu_send.setTypeface(font);

        /* * * * * body * * * * */
        qolSurveyAdapter = new QolSurveyFragmentPagerAdapter(getSupportFragmentManager(),this, qolResult);
        qolSurveyAdapter.setData();
        bindViews();
    }

    private void bindViews() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(qolSurveyAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(6);
        //viewPager.addOnPageChangeListener(this);
    }


    private void showBackDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_threebtn_dialog,null);
        MyRoundCornerButton dialogBtnSave = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_save);
        MyRoundCornerButton dialogBtnDelete = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_delete);
        MyRoundCornerButton dialogBtnCancel = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_cancel);

        //set layout for save btn: blue
        dialogBtnSave.setFillet(true);
        dialogBtnSave.setRadius(13);
        dialogBtnSave.setBackColor(getResources().getColor(R.color.dialog_blue));
        dialogBtnSave.setBackColorSelected(getResources().getColor(R.color.dialog_blue_press));
        dialogBtnSave.setTextColori(getResources().getColor(R.color.white));
        dialogBtnSave.setText(getResources().getString(R.string.save));
        //set layout for delete btn: red
        dialogBtnDelete.setFillet(true);
        dialogBtnDelete.setRadius(13);
        dialogBtnDelete.setBackColor(getResources().getColor(R.color.dialog_red));
        dialogBtnDelete.setBackColorSelected(getResources().getColor(R.color.dialog_red_press));
        dialogBtnDelete.setTextColori(getResources().getColor(R.color.white));
        dialogBtnDelete.setText(getResources().getString(R.string.delete));
        //set layout for cancel btn: gray
        dialogBtnCancel.setFillet(true);
        dialogBtnCancel.setRadius(13);
        dialogBtnCancel.setBackColor(getResources().getColor(R.color.dialog_gray));
        dialogBtnCancel.setBackColorSelected(getResources().getColor(R.color.dialog_gray_press));
        dialogBtnCancel.setTextColori(getResources().getColor(R.color.white));
        dialogBtnCancel.setText(getResources().getString(R.string.cancel));

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(getString(R.string.icon_warning));
        builder.setView(dialogView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        alert = builder.create();
        alert.show();

        //设置组件
        dialogBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QolSurveyActivity.this,"saveBtn！！",Toast.LENGTH_SHORT).show();

            }
        });
        dialogBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                QolSurveyActivity.this.finish();
            }
        });
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "对话框已关闭~", Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });


    }
    private void showSendDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_onebtn_dialog,null);
        MyRoundCornerButton dialogBtnOk = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_ok);
        dialogBtnOk.setFillet(true);
        dialogBtnOk.setRadius(13);
        dialogBtnOk.setBackColor(getResources().getColor(R.color.dialog_yellow));
        dialogBtnOk.setBackColorSelected(getResources().getColor(R.color.dialog_yellow_press));
        dialogBtnOk.setTextColori(getResources().getColor(R.color.black));
        dialogBtnOk.setText(getResources().getString(R.string.ok));

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
                //Toast.makeText(getApplicationContext(), "close", Toast.LENGTH_SHORT).show();
                alert.dismiss();
                //get current answer union
                ArrayList<String[]> answers = qolResult.getResults();
                //get the unanswered item which has the smallest id
                //check which question hasn't been answered
                //i.e. minId means the question with questionId(minId) should be answered first
                if (answers.size() != 0){
                    while (minIndex < answers.size()){
                        String[] itemResult = answers.get(minIndex);
                        int itemId = Integer.parseInt(itemResult[0]);
                        if(itemId != minId){
                            break;  //find the target item now
                        }else{
                            minIndex = minIndex + 1;
                            minId = minId + 1;
                        }
                    }
                    for(String[] itemR:answers){
                        if(itemR.length == 2){
                            Log.e("TAG",itemR[0]+"---"+itemR[1]); //test answers
                        }
                    }
                }
                Log.e("minid",String.valueOf(minId));  //test minId
                //scroll to the target position
                if (minId >= 1 && minId <=10){
                    viewPager.setCurrentItem(0);
                    listView = viewPager.findViewById(R.id.qol_survey_questionList);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPositionFromTop(minId - 1, 0);
                        }
                    });

                }else if (minId >= 11 && minId <= 20){ //scroll not work
                    viewPager.setCurrentItem(1);
                    listView = viewPager.findViewById(R.id.qol_survey_questionList);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPositionFromTop(minId - 11, 0);
                        }
                    });
                }else if (minId >= 21 && minId <= 30){//scroll not work
                    viewPager.setCurrentItem(2);
                    listView = viewPager.findViewById(R.id.qol_survey_questionList);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPositionFromTop(minId - 21, 0);
                        }
                    });
                }else if (minId >= 31 && minId <= 40){//scroll not work
                    viewPager.setCurrentItem(3);
                    listView = viewPager.findViewById(R.id.qol_survey_questionList);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPositionFromTop(minId - 31, 0);
                        }
                    });
                }else if (minId >= 41 && minId <= 49){//scroll not work
                    viewPager.setCurrentItem(4);
                    listView = viewPager.findViewById(R.id.qol_survey_questionList);
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPositionFromTop(minId - 41, 0);
                        }
                    });
                }else {
                    viewPager.setCurrentItem(5); //only two item, no need to scroll

                }


            }
        });
    }
    private void showConfirmDialog(){
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_twobtn_dialog,null);
        MyRoundCornerButton dialogBtnConfirm = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_confirm);
        MyRoundCornerButton dialogBtnCancel = (MyRoundCornerButton) dialogView.findViewById(R.id.btn_cancel);

        //set layout for save btn: blue
        dialogBtnConfirm.setFillet(true);
        dialogBtnConfirm.setRadius(13);
        dialogBtnConfirm.setBackColor(getResources().getColor(R.color.dialog_blue));
        dialogBtnConfirm.setBackColorSelected(getResources().getColor(R.color.dialog_blue_press));
        dialogBtnConfirm.setTextColori(getResources().getColor(R.color.white));
        dialogBtnConfirm.setText(getResources().getString(R.string.save));
        //set layout for cancel btn: gray
        dialogBtnCancel.setFillet(true);
        dialogBtnCancel.setRadius(13);
        dialogBtnCancel.setBackColor(getResources().getColor(R.color.dialog_gray));
        dialogBtnCancel.setBackColorSelected(getResources().getColor(R.color.dialog_gray_press));
        dialogBtnCancel.setTextColori(getResources().getColor(R.color.white));
        dialogBtnCancel.setText(getResources().getString(R.string.cancel));
        //set text for textview:txt_content
        //i.e. to show all results
        TextView txt_content = (TextView) dialogView.findViewById(R.id.txt_content);
        String allA_str = "";
        ArrayList<String[]> answers = qolResult.getResults();
        for (int i=0;i<answers.size();i++){
            String[] itemResult = answers.get(i);
            String itemId = itemResult[0];
            String itemAnswer = itemResult[1];
            allA_str = allA_str + "Q" + itemId +": " + itemAnswer + "\n";
        }
        txt_content.setText(allA_str);

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(getString(R.string.icon_warning));
        builder.setView(dialogView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        alert = builder.create();
        alert.show();

        //设置组件
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(QolSurveyActivity.this,"saveBtn！！",Toast.LENGTH_SHORT).show();
                sendData();
                alert.dismiss();
                QolSurveyActivity.this.finish();
            }
        });
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "对话框已关闭~", Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });
    }
    private void sendData(){
        date = "2018-10-24";
        //date = qolResult.getDate();
        //send data
        mRoot = FirebaseDatabase.getInstance().getReference();
        //通过键名，获取数据库实例对象的子节点对象
        final DatabaseReference userRef = mRoot.child(currentUserId).child("qol");

        Query checkUnique = userRef.orderByChild("date").equalTo(date);
        checkUnique.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String[]> answers = qolResult.getResults();
                if (dataSnapshot.exists()) { //update existing data
                    DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                    String key = nodeDataSnapshot.getKey(); // this key is the eid of the existing data
                    for (int i=0; i< answers.size();i++){
                        String[] itemResult = answers.get(i);
                        String itemId = itemResult[0];
                        String itemAnswer = itemResult[1];
                        userRef.child(key).child(itemId).setValue(itemAnswer);
                    }
                    Toast.makeText(getApplicationContext() , "Updated ~" , Toast.LENGTH_SHORT).show();
                } else { //add new data
                    Map<String, String> qolR = new HashMap<String, String>();
                    qolR.put("date", date);
                    for (int i=0; i< answers.size();i++){
                        String[] itemResult = answers.get(i);
                        String itemId = itemResult[0];
                        String itemAnswer = itemResult[1];
                        qolR.put(itemId, itemAnswer);
                    }
                    userRef.push().setValue(qolR);
                    Toast.makeText(getApplicationContext() , "Added ~" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });

    }
}
