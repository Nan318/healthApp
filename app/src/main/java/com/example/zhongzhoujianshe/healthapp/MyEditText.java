package com.example.zhongzhoujianshe.healthapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongzhoujianshe.healthapp.R;

public class MyEditText extends LinearLayout {

    private TextView tv_top;
    private TextView tv_left_icon;
    private EditText et_input;
    private int color_title = getResources().getColor(R.color.dialog_gray);
    private int color_enter = getResources().getColor(R.color.wvDateTxt);//default: blue
    private int color_invalid = getResources().getColor(R.color.wvInputInvalidRed);//default: red
    private int color_green = getResources().getColor(R.color.wvInputGreen);
    private int color_lightgray = getResources().getColor(R.color.gray);
    private String top_title;
    private String top_note_normal;
    private String top_invalid = getResources().getString(R.string.wv_new_top_invalid);
    private String left_icon;
    private String input_hint;
    private boolean editable;

    public MyEditText(Context context) {
        super(context);
        initView(context);
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initView(context);

    }
    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MyEditText);
        top_title = mTypedArray.getString(R.styleable.MyEditText_top_title);
        top_note_normal = mTypedArray.getString(R.styleable.MyEditText_top_note_normal);
        left_icon = mTypedArray.getString(R.styleable.MyEditText_left_icon);
        input_hint = mTypedArray.getString(R.styleable.MyEditText_input_hint);
        editable = mTypedArray.getBoolean(R.styleable.MyEditText_editable, true);
        //获取资源后要及时回收
        mTypedArray.recycle();
    }
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.my_edit_text, this, true);
        tv_top = (TextView) findViewById(R.id.top_note);
        tv_left_icon = (TextView) findViewById(R.id.left_icon);
        et_input = (EditText) findViewById(R.id.input);
        final View bottom_line = (View) findViewById(R.id.bottom_line);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf");
        setLeftIcons(font);
        setInputHint(input_hint);
        bottom_line.getLayoutParams().height = 2;  //bottom border: thin, gray
        if(!editable){
            et_input.setEnabled(false);
            tv_left_icon.setTextColor(color_title);
            tv_top.setTextColor(color_lightgray); //light gray
        }else{
            et_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        bottom_line.getLayoutParams().height = 4;  //bottom border: thick, blue
                        v.requestLayout();//must have, otherwise the line will not change
                        //tv_top.setTextColor(color_enter); //blue note: enter....
                        //setTopNoteNormal(top_note_normal); //e.g. ENTER YOUR HEIGHT
                        if(tv_top.getText().toString().equals(top_invalid)){
                            tv_left_icon.setTextColor(color_invalid); //red icon
                            bottom_line.setBackgroundColor(color_invalid);
                        }else {
                            bottom_line.setBackgroundColor(color_enter);
                            tv_left_icon.setTextColor(color_green); //green icon
                        }

                    } else {
                        bottom_line.getLayoutParams().height = 2;  //bottom border: thin, gray
                        v.requestLayout(); //must have, otherwise the line will not change
                        if(tv_top.getText().toString().equals(top_invalid)){
                            //when top shows INVALID NUMBER, set bottom line to be red
                            bottom_line.setBackgroundColor(color_invalid);
                        }else{ //else, set the line to be blue
                            bottom_line.setBackgroundColor(color_title);
                        }
                        //tv_top.setTextColor(color_title); //gray
                        //setTopTitle(top_title); //e.g. HEIGHT (CM)
                        if(tv_top.getText().toString().equals(top_invalid)){
                            tv_left_icon.setTextColor(color_invalid); //red icon
                        }else {
                            tv_left_icon.setTextColor(color_enter); //blue icon
                        }

                        String input_txt=et_input.getText().toString().trim();
                        if(input_txt.length() != 0){ //show top title
                            tv_top.setTextColor(color_title); //gray
                            setTopTitle(top_title); //e.g. HEIGHT (CM)
                        }
                    }
                }
            });
            //change state by the number of input characters
            et_input.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!TextUtils.isEmpty(s.toString())){
                        //when input something
                        //bottom_line.getLayoutParams().height = 4;  //bottom border: thick
                        bottom_line.setBackgroundColor(color_enter); //blue bottom line
                        tv_top.setTextColor(color_enter); //blue top note
                        setTopNoteNormal(top_note_normal); //e.g. ENTER YOUR HEIGHT
                        tv_left_icon.setTextColor(color_green); //green icon
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(TextUtils.isEmpty(editable.toString())){
                        //edit, then all delete
                        // bottom_line.getLayoutParams().height = 4;  //bottom border
                        bottom_line.setBackgroundColor(color_invalid); //red bottom line
                        tv_top.setTextColor(color_invalid); //red top note:invalid number
                        tv_top.setText(top_invalid);
                        tv_left_icon.setTextColor(color_invalid); //red icon
                    }
                }
            });
        }


    }

    public void setLeftIcons(Typeface font) {
        if (!TextUtils.isEmpty(left_icon)) {
            tv_left_icon.setTextColor(color_enter);
            tv_left_icon.setTypeface(font);

            tv_left_icon.setText(left_icon);

        }
    }
    public void setTopTitle(String top_title) {
        tv_top.setText(top_title);
    }
    public void setTopNoteNormal(String top_note_normal) {
        if (!TextUtils.isEmpty(top_note_normal)) {
            tv_top.setText(top_note_normal);
        }
    }
    public void setInputHint(String input_hint) {
        if (!TextUtils.isEmpty(input_hint)) {
            et_input.setHint(input_hint);
        }
    }

    public void setEtText(String bmi) {//used to set bmi
        if(!editable){
            et_input.setText(bmi);
        }
    }
    public String getEtText(){
        return et_input.getText().toString();

    }


}
