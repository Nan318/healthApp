package com.example.zhongzhoujianshe.healthapp;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class BssSurveyActivity extends AppCompatActivity{
    //UI objects
    //toolbar part
    private Toolbar toolbar;
    private TextView txt_menu_back;
    private TextView txt_menu_send;
    //dody part
    private TextView txt_date;
    private ViewPager viewpager;
    private TickSeekBar bss_type;
    private TextView txt_type;
    private TextView txt_type_note;
    //firebase
    private String currentUserId;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //varialbe
    private int answer = -1;
    private EcogAndBssAnswerModel bssAnswer;
    //bss pictures to be selected
    private List<ImageView> imageViewList;
    private int[] imgId = {R.drawable.bsstype1, R.drawable.bsstype2, R.drawable.bsstype3,
            R.drawable.bsstype4, R.drawable.bsstype5, R.drawable.bsstype6, R.drawable.bsstype7};
    private int[] imgTitle = {R.string.bss_new_type1, R.string.bss_new_type2, R.string.bss_new_type3,
            R.string.bss_new_type4,R.string.bss_new_type5, R.string.bss_new_type6,
            R.string.bss_new_type7};
    private int[] imgTxt = {R.string.bss_new_type1_txt, R.string.bss_new_type2_txt, R.string.bss_new_type3_txt,
            R.string.bss_new_type4_txt, R.string.bss_new_type5_txt, R.string.bss_new_type6_txt,
            R.string.bss_new_type7_txt};

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
        setContentView(R.layout.activity_bss_survey);

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

        txt_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(answer != -1){
                    sendData();
                    BssSurveyActivity.this.finish();
                } else {
                }

            }
        });
        //back text_btn
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                BssSurveyActivity.this.finish();
            }
        });

        /* * * * * body * * * * * */

        //viewPager
        viewpager.addOnPageChangeListener(viewpagerListener);
        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());

        //seekbar
        bss_type.setOnSeekChangeListener(seekListener);

    }
    private void sendData(){
        String date = "2018-04-18-21-35";
        //send data
        mRoot = FirebaseDatabase.getInstance().getReference();
        //通过键名，获取数据库实例对象的子节点对象
        final DatabaseReference userRef = mRoot.child(currentUserId).child("bss");
        bssAnswer = new EcogAndBssAnswerModel();
        bssAnswer.setTime(date);
        bssAnswer.setType(answer);

        Query checkUnique = userRef.orderByChild("time").equalTo(date);
        checkUnique.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //update existing data
                    DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                    String key = nodeDataSnapshot.getKey(); // this key is the eid of the existing data
                    userRef.child(key).child("type").setValue(answer);
                    Toast.makeText(getApplicationContext() , "Updated ~" , Toast.LENGTH_SHORT).show();
                } else { //add new data
                    userRef.push().setValue(bssAnswer);
                    Toast.makeText(getApplicationContext() , "Added ~" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });

    }
    private void iniView(){
        /* * * * * toolbar * * * * * */

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");

        toolbar = (Toolbar) findViewById(R.id.bssNewToolbar);
        // Title
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //set icon-font: back
        txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setTypeface(font);
        //set icon-font: send
        txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
        txt_menu_send.setTypeface(font);

        /* * * * * body * * * * * */

        //set icon font for date_txt
        txt_date = (TextView) findViewById(R.id.date_txt);
        txt_date.setTypeface(font);
        //show date and time
        //TextClock mTextClock = (TextClock)findViewById(R.id.text_clock);

        //textview which shows the type id and note of bss images
        txt_type = (TextView) findViewById(R.id.type);
        txt_type_note = (TextView) findViewById(R.id.type_note);

        //viewPager: change pictures
        viewpager = (ViewPager) findViewById(R.id.viewPager);
        imageViewList = new ArrayList<>();
        for (int i = 0; i < imgId.length; i++) {
            ImageView imgview = new ImageView(this);
            imgview.setBackgroundResource(imgId[i]);
            imageViewList.add(imgview);
        }
        BssSurveyPagerAdapter adapter = new BssSurveyPagerAdapter(imageViewList);
        viewpager.setAdapter(adapter);
        //set the crrentItem position to be such big, so that we can continually slide it
        //e.g. if the current item is the last one,
        // when we continually slide it from right to left, the next item will be the first one
        viewpager.setCurrentItem((imageViewList.size()) * 100);
        //viewpager.setOffscreenPageLimit(7);
        /*setOffscreenPageLimit(int limit):
         * Set the number of pages that should be retained to either side of the current page
         * in the view hierarchy in an idle state.
         * */

        //seekbar
        bss_type = (TickSeekBar) findViewById(R.id.seekbar_bss_type);
    }

    //viewpager listener
    public ViewPager.OnPageChangeListener viewpagerListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int index = position % imageViewList.size();
            answer = index + 1;
            txt_type.setText(imgTitle[index]);
            txt_type_note.setText(imgTxt[index]);
            bss_type.setOnSeekChangeListener(null);
            bss_type.setProgress(index+1);
            bss_type.setOnSeekChangeListener(seekListener);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    //seekbar listener
    public OnSeekChangeListener seekListener = new OnSeekChangeListener(){
        @Override
        public void onSeeking(SeekParams seekParams) {
            String progress = seekParams.tickText;
            int progressi = Integer.parseInt(progress);
            answer = progressi;
            int index = progressi - 1;  //the real index of arrays (imgTitle[] & imgTxt[])
            int imgPosition = (imageViewList.size()) * 100; //set the position of the 1st image
            txt_type.setText(imgTitle[index]);
            txt_type_note.setText(imgTxt[index]);
            viewpager.addOnPageChangeListener(null);
            viewpager.setCurrentItem(imgPosition + index);
            viewpager.addOnPageChangeListener(viewpagerListener);

        }

        @Override
        public void onStartTrackingTouch(TickSeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(TickSeekBar seekBar) {

        }
    };

}