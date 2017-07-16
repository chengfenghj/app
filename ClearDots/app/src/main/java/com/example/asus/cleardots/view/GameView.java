package com.example.asus.cleardots.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.asus.cleardots.model.ColorLibrary;
import com.example.asus.cleardots.model.Dot;
import com.example.asus.cleardots.model.DotsBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/1/24.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private OnGameoverListener gameoverListener;
    private boolean first =true;          //标记游戏是否第一次结束
    private ColorLibrary colors;
    private DotsBoard board;               //点点盘的实例化
    private SurfaceHolder holder;
    private boolean running;              //监视线程的运行

    private int screenWidth;             //屏幕的宽度
    private int dotsLenght;               //一个点在屏幕上显示的宽度
    private float scale;                   //屏幕像素密度
    private int pad =5;                   //点与点之间的间距
    private List<Dot> dots;              //用来存放被吃掉的点的集合
    private int dotColor =0;              //用来记录选中的点的颜色
    private boolean remind;

    private boolean isFall;          //标记是否有下落的点
    private int fall;                //用来记录下落的距离
    private int fallY;               //实际用来下落的距离
    private int dist;                //每次下落的距离

    private Path path;                   //手指划过的路径
    private Path lastRect;             //记录最后划过的路
    private int startX;                //开始按下的点的横坐标
    private int startY;                //开始按下的点的纵坐标
    private int endX;                 //当前触点位置的横坐标
    private int endY;                 //当前触点位置的纵坐标
    private boolean drawPath =false;           //标记是否要把路径画出来
    private int pad1 =5;                  //显示路径的矩形半径
    private int pad2;                     //用于计算点的圆心

    public GameView(Context context){
        this(context,null,0);
    }
    public GameView(Context context,AttributeSet attrs){
        this(context, attrs, 0);
    }
    public GameView(Context context,AttributeSet attrs,int defstyle){
        super(context, attrs, defstyle);
        holder =getHolder();
        surfaceCreated(holder);

        colors =new ColorLibrary(context);
        board =new DotsBoard();
        dots =new ArrayList<Dot>();
        path =new Path();
        lastRect =new Path();

        scale =context.getResources().getDisplayMetrics().density;
        WindowManager wm =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth =wm.getDefaultDisplay().getWidth();
        dotsLenght =screenWidth/board.getLENGHT();
        dist = dotsLenght /3;
        pad2 = dotsLenght /2;
    }

    /**
     * 画图函数
     * @param canvas
     */
    public void render(Canvas canvas){
        Paint paint1 =new Paint();
        paint1.setColor(colors.getColor(11));
        canvas.drawRect(0, 0, screenWidth, screenWidth, paint1);

        //提示可消除的点的位置
        if(remind) {
            Dot rDot = board.getRemindDot();
            Paint paint2 =new Paint();
            paint2.setColor(Color.WHITE);
            Rect rect =new Rect(rDot.x* dotsLenght,rDot.y* dotsLenght,(rDot.x+1)* dotsLenght,(rDot.y+1)* dotsLenght);
            canvas.drawRect(rect,paint2);
        }

        //把画过的路径画出来
        if(drawPath){
            Paint paint3 =new Paint();
            paint3.setColor(Color.RED);
            canvas.drawPath(path, paint3);           //先画被选中的点
            canvas.drawPath(lastRect,paint3);        //再画未选中点的路径
        }

        //判断是否有空位置，有则下落
        isFall =false;
        List<Dot> emptyDots =new ArrayList<Dot>();       //用来暂存所有空点
        for(int i =0;i<board.getLENGHT();i++){
            for(int j =0;j<board.getLENGHT();j++){
                //如果盘上有一个空位置，说明有下落
                if(board.getValue(i,j) == 0) {
                    Dot dot =new Dot(i,j);
                    emptyDots.add(dot);
                    isFall =true;
                }
            }
        }

        //画出所有的点
        Paint paint =new Paint();
        RectF oval;
        for(int i =0;i<board.getLENGHT();i++){
            for(int j =0;j<board.getLENGHT();j++){
                if(board.getValue(i,j) == 0)
                    continue;
                fallY =0;
                //判断这一点是否是下落的点，如果是，增加下落偏移值
                for(int k = 0; k< emptyDots.size(); k++){
                    if(emptyDots.get(k).x == i && emptyDots.get(k).y > j) {
                        fallY =fall;
                        break;
                    }
                }
                paint.setColor(colors.getColor(board.getValue(i, j)));
                oval =new RectF(i* dotsLenght +pad,j* dotsLenght +pad+fallY,(i+1)* dotsLenght -pad,(j+1)* dotsLenght -pad+fallY);
                canvas.drawOval(oval,paint);
            }
        }
        //判断
        if(isFall)
            fall();
        board.gameover();
        if(board.getGameover())
            gameover();
    }

    /**
     * 初始化
     */
    public void init(){
        first =true;
        fall =0;
        board.init();
    }

    /**
     * 判断是否下落了一个位置
     */
    private void fall(){
        //设置3像素的计算误差，
        //如果下落的距离与小球的大小差别小于这个值，
        //说明下落了一个格子
        //通知棋盘进行下落更新
        if(Math.abs(fall- dotsLenght) < 3){
            fall =0;
            board.fall();
        }
        fall +=dist;
    }

    /**
     * 提示游戏结束或者指出可以消掉的点
     * @return    游戏结束返回false，否则返回true
     */
    public boolean remind(){
        if(isFall)
            return true;
        if(board.getGameover())
            return false;
        remind =true;
        return true;
    }
    public int getScore(){
        return board.getScore();
    }
    public boolean getGameover(){
        return board.getGameover();
    }

    public boolean down(int x,int y){
        if(!isClickOnDot(x,y))
            return false;
        int px =x/ dotsLenght;
        int py =y/ dotsLenght;
        startX =px* dotsLenght +pad2;
        startY =py* dotsLenght +pad2;
        endX =startX;
        endY =startY;
        Dot dot =new Dot(px,py);
        dots.add(dot);
        dotColor =board.getValue(px,py);
        drawPath =true;
        return true;
    }
    public boolean move(int x,int y){
        if(x >= screenWidth || y >= screenWidth)
            return false;
        lastRect.reset();
        if(isClickOnDot(x,y)){
            int px =x/ dotsLenght;
            int py =y/ dotsLenght;
            if(board.getValue(px,py) != dotColor) {
                dots.clear();
                drawPath =false;
                path.reset();
                lastRect.reset();
                return false;
            }
            endX =px* dotsLenght +pad2;
            endY =py* dotsLenght +pad2;
            if(effective(px,py)){
                Dot dot =new Dot(px,py);
                dots.add(dot);
                changPath();
                startX =endX;
                startY =endY;
            }
        }
        else {
            endX =x;
            endY =y;
            changeLastRect();
        }
        return true;
    }
    public void up(){
        if(dots.size() >= 3) {
            board.eat(dots);
            remind =false;
        }
        dots.clear();
        drawPath =false;
        path.reset();
        lastRect.reset();
    }

    /**
     * 查看是否点击在点上
     * @param x      点击位置的横坐标
     * @param y      点击位置的纵坐标
     * @return       如果点击到点上，返回true，否则返回false
     */
    private boolean isClickOnDot(int x, int y){
        int px =x% dotsLenght;
        int py =y% dotsLenght;
        if(px>pad && px< dotsLenght -pad && py>pad && py< dotsLenght -pad)
            return true;
        return false;
    }

    /**
     *     判断选中的点是否是一个有效点
     *     它是否与最后一个点相邻，它是否未被选中过,它是否和选中的点颜色相同
     * @param x      点击处的横坐标
     * @param y      点击处的纵坐标
     * @return     如果有效，返回true，否则返回false
     */
    private boolean effective(int x,int y){
        if(dots.size() == 0)
            return false;
        for(int i = 0; i< dots.size(); i++){
            if(dots.get(i).x == x && dots.get(i).y == y)
                return false;
        }
        Dot dot = dots.get(dots.size()-1);
        if((dot.x == x && Math.abs(dot.y-y) ==1)||(Math.abs(dot.x-x) == 1 && dot.y == y))
            return true;
        return false;
    }

    /**
     * 用来改变划过的路径
     */
    private void changPath(){
        RectF  rect;
//        System.out.println("startX,Y=("+startX+","+startY+");endX,Y=("+endX+","+endY+")");
        //往下滑
        if((startX == endX) && (startY < endY))
            rect = new RectF(startX - pad1, startY - pad1, endX + pad1, endY + pad1);
        //往上滑
        else if((startX == endX) && (startY > endY))
            rect = new RectF(endX - pad1, endY - pad1,startX + pad1 ,startY + pad1 );
        //往左滑
        else if((startX > endX) && (startY == endY))
            rect = new RectF(endX - pad1,endY - pad1 ,startX + pad1 , startY + pad1);
        //往右滑
        else if((startX < endX) && (startY == endY))
            rect = new RectF(startX - pad1, startY - pad1, endX + pad1, endY + pad1);
        else {
            return;
        }
        path.addRect(rect, Path.Direction.CW);
    }

    /**
     * 记录最后划过的一段
     */
    private void changeLastRect(){
        if(startX > endX){
            if(startY > endY){
                lastRect.moveTo(startX,startY+pad1);
                lastRect.lineTo(startX+pad1,startY);
                lastRect.lineTo(endX,endY-pad1);
                lastRect.lineTo(endX-pad1,endY);
            }
            else{
                lastRect.moveTo(startX,startY-pad1);
                lastRect.lineTo(startX+pad1,startY);
                lastRect.lineTo(endX,endY+pad1);
                lastRect.lineTo(endX-pad1,endY);
            }
        }
        else{
            if(startY > endY){
                lastRect.moveTo(startX-pad1,startY);
                lastRect.lineTo(startX,startY+pad1);
                lastRect.lineTo(endX+pad1,endY);
                lastRect.lineTo(endX,endY-pad1);
            }
            else{
                lastRect.moveTo(startX-pad1,startY);
                lastRect.lineTo(startX,startY-pad1);
                lastRect.lineTo(endX+pad1,endY);
                lastRect.lineTo(endX,endY+pad1);
            }
        }
        lastRect.setFillType(Path.FillType.EVEN_ODD);
    }

    /**
     * 游戏结束时触发监听器
     */
    private void gameover(){
        if(!first)
            return;
        first =false;
        gameoverListener.gameover();
    }
    /**
     *设置游戏结束监听器
     * @param onGameoverListener
     */
    public void setOnGameoverListener(OnGameoverListener onGameoverListener){
        gameoverListener =onGameoverListener;
    }

    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int mWidth =MeasureSpec.getSize(widthMeasureSpec);
        int mHeight =MeasureSpec.getSize(heightMeasureSpec);
        int widthMode =MeasureSpec.getMode(widthMeasureSpec);
        int heightMode =MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,screenWidth);
        else if(widthMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(screenWidth,mHeight);
        else if(heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(mWidth,screenWidth);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running =true;
        new GameThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running =false;
    }

    class GameThread extends Thread implements Runnable{

        public void run(){
            Canvas canvas =null;
            while(running){
                try{
                    sleep(60);
                    canvas =holder.lockCanvas();
                    synchronized (holder){
                        render(canvas);
                    }
                }catch (Exception e){}
                finally {
                    if(canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    public interface OnGameoverListener{
        void gameover();
    }
}
