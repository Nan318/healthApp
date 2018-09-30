package com.example.zhongzhoujianshe.healthapp;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class QolSurvey extends AppCompatActivity implements
        ViewPager.OnPageChangeListener {
    //UI Objects
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private ViewPager viewPager;
    private QolSurveyFragmentPagerAdapter qolSurveyAdapter;

    //几个代表页面的常量
    //ssss
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;
    public static final int PAGE_SIX = 5;

    //private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qol__survey);

        //used for setting icon-font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.qolSurveyToolbar);
        // Title
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //set icon-font: send
        TextView txt_menu_send = (TextView) toolbar.findViewById(R.id.toolbar_send);
        txt_menu_send.setTypeface(font);
        //set click event
        txt_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showSendDialog();
            }
        });
        //back text_btn
        TextView txt_menu_back = (TextView) toolbar.findViewById(R.id.toolbar_back);
        txt_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showBackDialog();
            }
        });

        qolSurveyAdapter = new QolSurveyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.survey_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_send);
        //getActionView()返回你自定义的菜单布局，设置单击事件的目的是，让其单击时执行onOptionsItemSelected，从而只需统一在onOptionsItemSelected处理即可

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");//记得加上这句
         //   mTitle.setTypeface(font);
      //  TextView menusend = (TextView) menu.findItem(R.id.menu_send);
        item.setTypeface(font);

        return true;


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_send:
                Toast.makeText(this , "haha" , Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    private void bindViews() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(qolSurveyAdapter);
        viewPager.setCurrentItem(PAGE_ONE);
        viewPager.addOnPageChangeListener(this);
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 3) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_ONE:
                    viewPager.setCurrentItem(PAGE_ONE);
                    break;
                case PAGE_TWO:
                    viewPager.setCurrentItem(PAGE_TWO);
                    break;
                case PAGE_THREE:
                    viewPager.setCurrentItem(PAGE_THREE);
                    break;
                case PAGE_FOUR:
                    viewPager.setCurrentItem(PAGE_FOUR);
                    break;
                case PAGE_FIVE:
                    viewPager.setCurrentItem(PAGE_FIVE);
                    break;
                case PAGE_SIX:
                    viewPager.setCurrentItem(PAGE_SIX);
                    break;
            }
        }
    }
    private void showBackDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_threebtn_dialog,null);
        Button dialogBtnSave = (Button) dialogView.findViewById(R.id.btn_save);
        Button dialogBtnDelete = (Button) dialogView.findViewById(R.id.btn_delete);
        Button dialogBtnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

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
                Toast.makeText(QolSurvey.this,"sendBtn！！",Toast.LENGTH_SHORT).show();
            }
        });
        dialogBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QolSurvey.this,"deleteBtn！！",Toast.LENGTH_SHORT).show();
            }
        });
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "对话框已关闭~", Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });


    }
    private void showSendDialog() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.qol_survey_onebtn_dialog,null);
        Button dialogBtnOk = (Button) dialogView.findViewById(R.id.btn_ok);

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
                Toast.makeText(getApplicationContext(), "对话框已关闭~", Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });
    }
}
