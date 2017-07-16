package com.example.asus.freerun.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.asus.freerun.R;
import com.example.asus.freerun.enemies.Enemy;

/**
 * Created by ASUS on 2017/2/19.
 */
public class ImageLibrary {

    private Context context;

    private Bitmap field;
    private Bitmap road;
    private Bitmap shield;
    private Bitmap enemy1;

    private int shieldLocation;
    private int shieldHeight;
    private int maxShieldLocation =10;
    private int enemyHeight;


    public ImageLibrary(Context context){
        this.context =context;
        loadImage();
        shieldHeight =shield.getHeight();
        enemyHeight =enemy1.getHeight();
    }

    /**
     * 初始化绘图信息的值
     */
    public void init(){
        shieldLocation =0;
    }

    /**
     * 加载图片
     */
    private void loadImage(){
        field = BitmapFactory.decodeResource(context.getResources(),R.drawable.field);
        road = BitmapFactory.decodeResource(context.getResources(),R.drawable.road);
        shield = BitmapFactory.decodeResource(context.getResources(),R.drawable.shield);
        enemy1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy1);
    }

    /**
     * 暴露出用于画护盾的函数
     * @param canvas      画在哪个canvas上
     * @param dst         画在canvas的目标矩形上，在canvas上的位置
     * @param paint       画笔
     */
    public void drawShield(Canvas canvas,RectF dst,Paint paint){
        Rect src =new Rect(shieldLocation*shieldHeight,0,(shieldLocation+1)*shieldHeight,shieldHeight);
        canvas.drawBitmap(shield,src,dst,paint);
        shieldLocation ++;
        if(shieldLocation == maxShieldLocation)
            shieldLocation =0;
    }

    /**
     * 暴露出用于画背景的函数
     * @param canvas
     * @param dst
     * @param path
     * @param paint
     */
    public void drawBackground(Canvas canvas, RectF dst, Path path,Paint paint){
        Rect src =new Rect(0,0,field.getWidth(),field.getHeight());
        canvas.drawBitmap(field,src,dst,paint);
        BitmapShader fieldShader =new BitmapShader(road,BitmapShader.TileMode.REPEAT,BitmapShader.TileMode.REPEAT);
        paint.setShader(fieldShader);
        canvas.drawPath(path,paint);
    }
    public void drawEnemys(Canvas canvas,RectF dst,int location,Paint paint,int type){
        switch (type) {
            case Enemy.common_enemy:
                Rect src = new Rect(location * enemyHeight, 0, (location + 1) * enemyHeight, enemyHeight);
                canvas.drawBitmap(enemy1, src, dst, paint);
                break;
        }
    }
}
