package com.example.asus.cleardots.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.asus.cleardots.data.GradeBean;
import com.example.asus.cleardots.model.ColorLibrary;

/**
 * Created by ASUS on 2017/2/1.
 */
public class ListView extends View {

    private final int LENGHT =10;
    private ColorLibrary colors;
    private GradeBean gradeBean;
    private float scale;                  //屏幕像素密度
    private int screenWidth;
    private int screenHeight;
    private int rankingWidth;           //要显示的数字大小
    private int itemWidth;               //分数项显示宽度
    private int itemHeight;              //分数项显示高度
    private int dotWidth;               //要显示一个小球的宽度
    private int[] grades;

    public ListView(Context context){
        this(context,null,0);
    }
    public ListView(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }
    public ListView(Context context,AttributeSet attrs,int defstyle){
        super(context, attrs, defstyle);

        scale =context.getResources().getDisplayMetrics().density;
        WindowManager wm =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth =wm.getDefaultDisplay().getWidth()-(int)(40*scale+0.5f);
        screenHeight =wm.getDefaultDisplay().getHeight()-(int)(40*scale+0.5f);
        rankingWidth =screenWidth/9;
        itemWidth =screenWidth-rankingWidth;
        itemHeight =screenHeight/11;
        dotWidth =itemWidth/10;

        gradeBean =new GradeBean(context);
        colors =new ColorLibrary(context);
        grades =new int[LENGHT];
        gradeBean.query();
    }
    public void onDraw(Canvas canvas){
        Paint paint =new Paint();
        for(int i =0;i<LENGHT;i++){
            paint.setColor(colors.getColor(i + 12));
            paint.setTextSize(80);
            if(i == 9)
                canvas.drawText(String.valueOf(i+1), -40, i *itemHeight+rankingWidth, paint);        //画数字
            else
                canvas.drawText(String.valueOf(i+1), 0, i *itemHeight+rankingWidth, paint);        //画数字
            grades =gradeBean.getAgrade(i);
            for(int j =0;j<LENGHT;j++){
                RectF rect =new RectF(rankingWidth+j* dotWidth,i*itemHeight,
                            rankingWidth+(j+1)* dotWidth,i*itemHeight+ dotWidth);
                paint.setColor(colors.getColor(grades[j]));
                canvas.drawOval(rect,paint);
            }
        }
    }

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth =MeasureSpec.getSize(widthMeasureSpec);
        int mHeight =MeasureSpec.getSize(heightMeasureSpec);
        int widthMode =MeasureSpec.getMode(widthMeasureSpec);
        int heightMode =MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,screenHeight);
        else if(widthMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,mHeight);
        else if(heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(mWidth,screenHeight);
    }
}
