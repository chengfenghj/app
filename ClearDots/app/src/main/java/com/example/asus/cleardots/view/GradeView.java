package com.example.asus.cleardots.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.asus.cleardots.model.ColorLibrary;

/**
 * Created by ASUS on 2017/1/26.
 */
public class GradeView extends View {

    private ColorLibrary colors;                //实例化颜色库
    private int screenWidth;                 //屏幕宽度
    private int potsWidth;                    //一个点的宽度
    private float scale;
    private int pad =6;                        //点与点之间的距离
    private int grade;                      //分数
    private int[] pots =new int[10];       //要显示的点的数组

    public GradeView(Context context){
        this(context,null,0);
    }
    public GradeView(Context context,AttributeSet attrs){
        this(context, attrs, 0);
    }
    public GradeView(Context context,AttributeSet attrs,int destyle){
        super(context, attrs, destyle);

        scale =context.getResources().getDisplayMetrics().density;
        WindowManager wm =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth =wm.getDefaultDisplay().getWidth()-(int)(60*scale+0.5f);
        potsWidth =screenWidth/10;

        colors =new ColorLibrary(context);
        init();
    }
    public void init(){
        grade =0;
        for(int i =0;i<10;i++){
            pots[i] =0;
        }
    }
    public void onDraw(Canvas canvas){

        Paint paint =new Paint();
        RectF rect;
        for(int i =0;i<10;i++){
            paint.setColor(colors.getColor(pots[i]));
            rect =new RectF(i*potsWidth+pad,pad,(i+1)*potsWidth-pad,potsWidth-pad);
            canvas.drawOval(rect,paint);
        }
    }
    public void changeGrade(int grade){
        this.grade =grade;
        changePots(grade);
    }
    private void changePots(int grade){
        for(int i =0;i<10;i++){
            pots[i] =dif(grade);
            grade =grade - maxGrade(pots[i]);
            if(grade < 0)
                grade =0;
        }
    }

    /**
     * 计算指定分数对应的难度
     * @param grade        传入分数
     * @return             返回难度
     */
    private int dif(int grade){
        int g =1;
        int d =0;
        while (g - grade <= 0){
            g *=4;
            d++;
        }
        return d;
    }

    /**
     * 计算指定难度对应的最大分数
     * @param dif        传入难度
     * @return           返回分数
     */
    private int maxGrade(int dif){
        int g =1;
        for(int i =0;i<dif-1;i++)
            g *=4;
        return g;
    }

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int mWidth =MeasureSpec.getSize(widthMeasureSpec);
        int mHeight =MeasureSpec.getSize(heightMeasureSpec);
        int widthMode =MeasureSpec.getMode(widthMeasureSpec);
        int heightMode =MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,potsWidth);
        else if(widthMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,mHeight);
        else if(heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(mWidth,potsWidth);
    }
}
