package com.example.asus.freerun.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.asus.freerun.R;

/**
 * Created by ASUS on 2017/2/7.
 */
public class GradeView extends View {

    private final int max_grade =10;        //定义最高等级
    private int screenWidth;
    private int rectWidth;
    private int rectHeight;
    private int grade;             //要显示的等级

    public GradeView(Context context) {
        this(context, null, 0);
    }
    public GradeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        float scale =context.getResources().getDisplayMetrics().density;
        WindowManager wm =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth =wm.getDefaultDisplay().getWidth()-(int)(40*scale+0.5f);
        rectWidth =screenWidth/max_grade;
        rectHeight =rectWidth/5;
        grade =1;
    }

    public void onDraw(Canvas canvas){
        Paint paint =new Paint();
        RectF rect;
        for(int i =0;i<max_grade;i++){
            if(i < grade)
                paint.setColor(getColor(i));
            else
                paint.setColor(Color.GRAY);
            rect =new RectF(i * rectWidth, 0, (i + 1) * rectWidth, rectHeight);
            canvas.drawRoundRect(rect,rectHeight/3,rectHeight/3, paint);
        }
        Paint paint1 =new Paint();
        paint1.setColor(Color.WHITE);
        for(int i =0;i<max_grade-1;i++){
            canvas.drawLine((i+1)*rectWidth,0,(i+1)*rectWidth,rectHeight,paint1);
        }
    }
    public void setGrade(int grade){
        this.grade =grade;
    }
    private int getColor(int index){
        if(index < 3)
            return getContext().getResources().getColor(R.color.grade1);
        if(index < 6)
            return getContext().getResources().getColor(R.color.grade2);
        if(index < 8)
            return getContext().getResources().getColor(R.color.grade3);
        return getContext().getResources().getColor(R.color.grade4);
    }
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int mWidth =MeasureSpec.getSize(widthMeasureSpec);
        int mHeight =MeasureSpec.getSize(heightMeasureSpec);
        int widthMode =MeasureSpec.getMode(widthMeasureSpec);
        int heightMode =MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,rectHeight);
        else if(widthMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,mHeight);
        else if(heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(mWidth,rectHeight);
    }
}
