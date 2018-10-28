package com.example.zhongzhoujianshe.healthapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.AppCompatButton;
import android.widget.Toast;

/**reference：http://www.cnblogs.com/landptf/p/4562203.html
 * self-defined button
 * can set background color, text color, pressed_txt_color, pressed_background_color
 */
public class MyRoundCornerButton extends AppCompatButton {

    private GradientDrawable gradientDrawable;//style
    private String backColors = "";//background color: string type
    private int backColori = 0;//background color: int type
    private String backColorSelecteds = "";//pressed bg，String
    private int backColorSelectedi = 0;//pressed bg，int
    private int backGroundImage = 0;//bg pictures，id
    private int backGroundImageSeleted = 0;//pressed bg pictures，id
    private String textColors = "";//text color，String
    private int textColori = 0;//text color，int
    private String textColorSeleteds = "";//pressed text color，String
    private int textColorSeletedi = 0;//pressed text color，int
    private float radius = 15;//radius of round corner
    private int shape = 0;
    private Boolean fillet = false;//round corner?
    private int strokeColor = 0; //default: no stroke
    private int strokeWidth = 0;
    private int pressedStrokeColor = 0; //stroke color when being touched
    private int pressedStrokeWidth = 0;
    private boolean borderLeft = false; //if the button has left border or not?
    private boolean borderTop = false;
    private boolean borderRight = false;
    private boolean borderBottom = false;
    private int borderColor = 0;
    private float borderWidth = 0;

    public MyRoundCornerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public MyRoundCornerButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyRoundCornerButton(Context context) {
        this(context, null);
    }
    private void init() {
        if (fillet) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
            }
            gradientDrawable.setColor(getResources().getColor(R.color.gray));
        }else {
            setBackgroundColor(getResources().getColor(R.color.gray));
        }
        //set default: round-rectangle
        //setRadius(radius);
        //setBackgroundDrawable(gradientDrawable);
        //set default bg color
        //setBackColor(getResources().getColor(R.color.gray));
        //set default pressed_bg
        //setBackColorSelected(getResources().getColor(R.color.wvDateBg));
        //set default text
        setText("TEXT");
        //set default txt color
        setTextColori(Color.BLACK);
        //default txt size
        setTextSize(14);
        //default position
        setGravity(Gravity.CENTER);
        //set touch event
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                //press, change style
                setColor(event);
                //return false: avoid hiding click event
                //if no click event, should return true. otherwise, can't catch up event
                return false;
            }
        });
        /*
        setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {

            }
        });*/
    }
    //改变样式
    private void setColor(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //press
                if (backColorSelectedi != 0) {
                    //set bg?
                    if (fillet) {
                        if (gradientDrawable == null) {
                            gradientDrawable = new GradientDrawable();
                        }
                        gradientDrawable.setColor(backColorSelectedi);
                    } else {
                        setBackgroundColor(backColorSelectedi);
                    }
                } else if (!backColorSelecteds.equals("")) {
                    if (fillet) {
                        if (gradientDrawable == null) {
                            gradientDrawable = new GradientDrawable();
                        }
                        gradientDrawable.setColor(Color.parseColor(backColorSelecteds));
                    } else {
                        setBackgroundColor(Color.parseColor(backColorSelecteds));
                    }
                }
                //set text color?
                if (textColorSeletedi != 0) {
                    setTextColor(textColorSeletedi);
                } else if (!textColorSeleteds.equals("")) {
                    setTextColor(Color.parseColor(textColorSeleteds));
                }
                //set bg picture?
                if (backGroundImageSeleted != 0) {
                    setBackgroundResource(backGroundImageSeleted);
                }
                //if set stroke?
                if (pressedStrokeColor != 0 && pressedStrokeWidth != 0){
                    if (gradientDrawable == null) {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setStroke(pressedStrokeWidth, pressedStrokeColor);
                }
                break;
            case MotionEvent.ACTION_UP:
                //up
                if (backColori == 0 && backColors.equals("")) {
                    //if didn't set bg, use default
                    if (fillet) {
                        if (gradientDrawable == null) {
                            gradientDrawable = new GradientDrawable();
                        }
                        gradientDrawable.setColor(getResources().getColor(R.color.gray));
                    } else {
                        setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                } else if (backColori != 0) {
                    if (fillet) {
                        if (gradientDrawable == null) {
                            gradientDrawable = new GradientDrawable();
                        }
                        gradientDrawable.setColor(backColori);
                    } else {
                        setBackgroundColor(backColori);
                    }
                } else {
                    if (fillet) {
                        if (gradientDrawable == null) {
                            gradientDrawable = new GradientDrawable();
                        }
                        gradientDrawable.setColor(Color.parseColor(backColors));
                    } else {
                        setBackgroundColor(Color.parseColor(backColors));
                    }
                }
                //if didn't set text color, use default
                if (textColori == 0 && textColors.equals("")) {
                    setTextColor(Color.BLACK);
                } else if (textColori != 0) {
                    setTextColor(textColori);
                } else {
                    setTextColor(Color.parseColor(textColors));
                }
                if (backGroundImage != 0) {
                    setBackgroundResource(backGroundImage);
                }
                //if set stroke?
                if (strokeColor != 0 && strokeWidth != 0){
                    if (gradientDrawable == null) {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setStroke(strokeWidth, strokeColor);
                } else {
                    if (gradientDrawable == null) {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setStroke(0, Color.BLACK);
                }
                break;
        }
    }
    /**
     * set bg
     * @param backColor
     */
    public void setBackColor(String backColor) {
        this.backColors = backColor;
        if (backColor.equals("")) {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(getResources().getColor(R.color.gray));
            }else {
                setBackgroundColor(getResources().getColor(R.color.gray));
            }
        }else {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(Color.parseColor(backColor));
            }else {
                setBackgroundColor(Color.parseColor(backColor));
            }
        }
    }
    /**
     *
     * @param backColor
     */
    public void setBackColor(int backColor) {
        this.backColori = backColor;
        if (backColori == 0) {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(getResources().getColor(R.color.gray));
            }else {
                setBackgroundColor(getResources().getColor(R.color.gray));
            }
        }else {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(backColor);
            }else {
                setBackgroundColor(backColor);
            }
        }
    }
    /**
     * set pressed bg
     * @param backColorSelected
     */
    public void setBackColorSelected(int backColorSelected) {
        this.backColorSelectedi = backColorSelected;
    }
    /**
     * set pressed bg
     * @param backColorSelected
     */
    public void setBackColorSelected(String backColorSelected) {
        this.backColorSelecteds = backColorSelected;
    }
    /**
     * set bg picture
     * @param backGroundImage
     */
    public void setBackGroundImage(int backGroundImage) {
        this.backGroundImage = backGroundImage;
        if (backGroundImage != 0) {
            setBackgroundResource(backGroundImage);
        }
    }
    /**
     * set bg picture
     * @param backGroundImageSeleted
     */
    public void setBackGroundImageSeleted(int backGroundImageSeleted) {
        this.backGroundImageSeleted = backGroundImageSeleted;
    }
    /**
     * set radius of round corner
     * @param radius
     */
    public void setRadius(float radius) {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
        }
        gradientDrawable.setCornerRadius(radius);
    }
    //can set each corner to be round or not
    /*
    Specifies radii for each of the 4 corners.
    For each corner, the array contains 2 values, [X_radius, Y_radius].
    The corners are ordered top-left, top-right, bottom-right, bottom-left.
    This property is honored only when the shape is of type RECTANGLE.
    */
    public void setPartRadius(float topL, float topR, float bottomR, float bottomL) {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
        }
        gradientDrawable.setCornerRadii(new float[] { topL, topL, topR, topR,
                bottomR, bottomR, bottomL, bottomL });
    }
    /**
     * set text color
     * @param textColor
     */
    public void setTextColors(String textColor) {
        this.textColors = textColor;
        setTextColor(Color.parseColor(textColor));
    }
    /**
     * set text color
     * @param textColor
     */
    public void setTextColori(int textColor) {
        this.textColori = textColor;
        setTextColor(textColor);
    }
    /**
     * set pressed text color
     * @param textColor
     */
    public void setTextColorSelected(String textColor) {
        this.textColorSeleteds = textColor;
    }
    /**
     * set pressed text color
     * @param textColor
     */
    public void setTextColorSelected(int textColor) {
        this.textColorSeletedi = textColor;
    }
    //set stroke
    //attribute:1. strokeColor, pressedStrokeColor
    //          2. strokeWidth, pressedStrokeWidth
    //if need, setStroke (int width, int color, float dashWidth, float dashGap) can be used to dash the stroke.
    public void setStroke(int width, int color){
        this.strokeWidth = width;
        this.strokeColor = color;
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
        }
        gradientDrawable.setStroke(width, color);
    }
    public void setPressedStrokeWidthColor(int pwidth, int pcolor){
        this.pressedStrokeWidth = pwidth;
        this.pressedStrokeColor = pcolor;

    }

    /*function:
    public void drawLine (float startX, float startY, float stopX, float stopY, Paint paint)
    Draw a line segment with the specified start and stop x,y coordinates, using the specified paint.
    paint.setStrokeWidth(float width);
    */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(borderWidth != 0 && borderColor != 0){
            // create paint
            Paint paint = new Paint();
            // get color
            int color = paint.getColor();
            // set color
            paint.setColor(borderColor);
            paint.setStrokeWidth(borderWidth);
            // if borders are true，all borders exist
            if (borderLeft) {
                // draw left border
                canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
            }
            if (borderTop) {
                //draw top border
                canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
            }
            if (borderRight) {
                // draw right border
                canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                        this.getHeight() - 1, paint);
            }
            if (borderBottom) {
                // draw bottom border
                canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                        this.getHeight() - 1, paint);
            }

            paint.setColor(color);
        }


    }
    public void setBorderLeft(boolean left){
        this.borderLeft = left;
    }
    public void setBorderRight(boolean right){
        this.borderRight = right;
    }
    public void setBorderTop(boolean top){
        this.borderTop = top;
    }
    public void setBorderBottom(boolean bottom){
        this.borderBottom = bottom;
    }
    public void setBorderColor(int color){
        this.borderColor = color;
    }
    public void setBorderWidth(float width){
        this.borderWidth = width;
    }

    /**
     * set shape
     * @param shape
     */
    public void setShape(int shape) {
        this.shape = shape;
    }
    /**
     * round corner?
     * @param fillet
     */
    @SuppressWarnings("deprecation")
    public void setFillet(Boolean fillet) {
        this.fillet = fillet;
        if (fillet) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
            }
            //GradientDrawable.RECTANGLE
            gradientDrawable.setShape(shape);
            gradientDrawable.setCornerRadius(radius);
            setBackgroundDrawable(gradientDrawable);
        }
    }
}
