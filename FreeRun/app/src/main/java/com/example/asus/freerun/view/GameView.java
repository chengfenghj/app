package com.example.asus.freerun.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.asus.freerun.data.InitiateBean;
import com.example.asus.freerun.enemies.CrazyEnemy;
import com.example.asus.freerun.enemies.Enemy;
import com.example.asus.freerun.enemies.TreasureBox;
import com.example.asus.freerun.model.Bullet;
import com.example.asus.freerun.model.EnemyFactory;
import com.example.asus.freerun.model.ImageLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/2/2.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,SensorEventListener{

    private WindowManager wm;                  //窗口管理器
    private SensorManager sm;                  //传感器管理器
    private ImageLibrary il;
    private EnemyFactory enemyFactory;
    private SurfaceHolder surfaceHolder;
    private boolean running;                 //标记线程是否继续
    private int sleepTime =40;               //线程休眠时间
    private Bitmap drawBitmap;               //用于取消闪烁的双缓冲位图
    private Canvas drawCanvas;               //用于取消闪烁的双缓冲画布
    private OnGameoverListener gameoverListener;
    private boolean first =true;

    private float screenWidth;             //屏幕的宽度
    private float screenHeight;            //屏幕的高度
    private float road1;                    //第一条道上面的点（y == 0）的横坐标
    private float road2;                    //第一条道下面的点（y == screenHeight）的横坐标
    private float dist1;                    //屏幕上方（y == 0）道与道之间的距离
    private float dist2;                    //屏幕下方（y == screenHeight）道与道之间的距离

    private float dotWidth;                //己方圆点的直径
    private int speed =0;                   //在X方向上的速度，随手机的摇晃而改变
    private float dotX;                     //己方圆点的横坐标
    private float dotY;                     //己方圆点的纵坐标
    private float pad;                      //左边的屏幕距离，己方圆点不能移动超过的地方
    private int shieldCount;
    private boolean shield =false;        //标记是否带有护盾
    private boolean bullet =false;        //标记场上是否存在子弹

    private InitiateBean initBean;         //初始状态数据
    private int initiateEnerge;           //初始能量
    private int shieldTime;               //护盾时间
    private int bulletConsume;            //子弹消耗
    private int invincibleTime =30;       //无敌时间
    private int doubleGradeTime =80;     //双倍得分时间
    private int speedUpTime =100;          //助跑时间
    private int doubleEnergyTime =80;    //加倍能量收集时间

    private boolean pause =false;          //标记游戏是否暂停
    private boolean gameover =false;       //标记游戏是否结束
    private float enemySpeed =10;           //敌人的速度，随难度的增加而增加
    private int maxScore =500;              //最大分数，分数超过这个值则难度增加
    private int grade =0;                    //分数
    private int extraGrade =0;              //额外的分数，不会改变游戏难度
    private int energy;                     //能量,初始两百
    private int scale =1;                   //分数增长的速度
    private int energyScale =12;           //能量增长的速度
    private int energiesCount =11;        //能量增长计数
    private int interval =40;              //出现一个新敌人的时间间隔
    private int specialIntv =120;          //出现一个特殊敌人的时间间隔
    private int treasure =286;             //出现一个宝箱的时间间隔
    private int fall =40;                  //下落帧数计数
    private int specialFall =60;          //下落帧数计数
    private int treasureFall =80;         //下落帧数计数

    private boolean openBox =false;        //标记宝箱是否被打开
    private boolean invincible =false;    //标记是否处于无敌状态
    private boolean doubleGrade =false;   //标记是否处于加倍得分状态
    private boolean speedUp =false;       //标记是否处于助跑状态
    private boolean doubleEnergy =false; //标记是否处于加倍能量收集状态
    private String boxString ="";          //提示宝箱里的文字
    private int boxCount =40;              //宝箱提示时间计数
    private int boxColor;                  //宝箱提示文字的颜色
    private int golds =0;                  //通过开宝箱攒的金币
    private int invincibleCount;         //无敌时间计数
    private int doubleGradeCount;        //加倍得分时间计数
    private int speedUpCount;            //助跑时间计数
    private int doubleEnergyCount;      //加倍能量收集时间计数

    private boolean superShield =false;    //标记是否处在超级护盾状态
    private boolean superUpspeed =false;   //标记是否处在超级加速状态
    private boolean superLaser =false;     //标记是否处在超级激光状态
    private boolean superBomb =false;      //标记是否处在超级炸弹状态
    private int superShieldTime =160;       //超级护盾持续时间
    private int superUpspeedTime =600;     //超级加速持续时间
    private int superLaserTime =160;        //超级激光持续时间
    private int superBombTime =25;         //超级炸弹提示时间
    private int superShieldCount;         //超级护盾持续时间计数
    private int superUpspeedCount;        //超级加速持续时间计数
    private int superLaserCount;           //超级激光持续时间计数
    private int superBombCount;           //超级炸弹提示时间计数

    private List<Enemy> enemies;           //敌人的集合
    private List<Bullet> bullets;          //子弹的集合

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取屏幕的宽高并初始化一些值
        wm =(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth =wm.getDefaultDisplay().getWidth();
        screenHeight =wm.getDefaultDisplay().getHeight();
        drawBitmap =Bitmap.createBitmap((int) screenWidth, (int) screenHeight, Bitmap.Config.RGB_565);
        drawCanvas =new Canvas(drawBitmap);
        road1 =screenWidth*3/10;
        road2 =screenWidth/10;
        dotWidth =screenWidth/12;
        dotX =screenWidth/2;
        dotY =screenHeight - dotWidth -10;
        pad =road2+ dotWidth /4;
        dist1 =4*screenWidth/50;
        dist2 =8*screenWidth/50;

        //注册重力感应监听器
        sm =(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        il =new ImageLibrary(context);
        il.init();
        enemyFactory =new EnemyFactory();

        enemies =new ArrayList<Enemy>();
        bullets =new ArrayList<Bullet>();

        initBean =new InitiateBean(context);
        initBean.query();
        initiateEnerge =initBean.getInitiateEnerge();
        shieldTime =initBean.getShieldTime();
        bulletConsume =initBean.getBulletEnerge();

        surfaceHolder =getHolder();
        surfaceCreated(surfaceHolder);
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context) {
        this(context, null, 0);
    }

    public void init(){
        first =true;
        pause =false;
        gameover =false;
        shield =false;
        openBox =false;
        invincible =false;
        doubleEnergy =false;
        doubleGrade =false;
        speedUp =false;
        dotX =screenWidth/2;
        golds =0;
        maxScore =500;                   //最大分数，分数超过这个值则难度增加
        grade =0;                        //分数
        extraGrade =0;
        energy =initiateEnerge;       //能量
        energyScale =12;               //能量增长速度
        scale =1;                       //分数增长的速度
        enemySpeed =10;                //敌人的速度，随难度的增加而增加
        interval =40;                  //出现一个新敌人的时间间隔
        treasure =286;                 //出现宝箱的时间间隔
        fall =20;                      //下落帧数计数
        //屏幕清空
        enemies.clear();
        bullets.clear();
    }
    public int getGrade(){
        return grade+extraGrade;
    }
    public float getScreenWidth(){
        return screenWidth;
    }
    public int getGolds(){
        return golds;
    }

    /**
     * 实际绘图的地方
     * @param canvas
     */
    public void render(Canvas canvas){
        canvas.drawBitmap(drawBitmap, 0, 0, null);

        for(int i =0;i<bullets.size();i++) {
            //如果有颗子弹超出了屏幕外，就删掉
            if (bullets.get(i).getY() < -dotWidth)
                bullets.remove(i);
        }

        for(int i =0;i<enemies.size();i++) {
            //如果有个敌人超出了屏幕外，就把他删掉
            if (enemies.get(i).getY() > screenHeight) {
                enemies.remove(i);
                continue;
            }
        }

        float ex,ey,ed;
        if(superShield){
            for(int i =0;i<enemies.size();i++){
                ex =enemies.get(i).getX();
                ey =enemies.get(i).getY();
                ed =enemies.get(i).getDiameter();
                if(superShieldKill(ex,ey,ed)){
                    enemies.remove(i);
                    extraGrade +=50;
                    golds +=3;
                }
            }
        }
        changeHard();
    }

    /**
     * 双缓冲的实现
     * 先在drawBitmap上绘图，再将所绘好的位图一次性画在屏幕画布上
     */
    private void draw(){
        //如果游戏暂停，什么都不画
        if(pause)
            return;
        //如果游戏结束，发送监听事件
        if(gameover) {
            gameover();
            return;
        }
        //如果处于助跑状态，计数
        if(speedUp)
            speedUpCount();
        //如果处于超级助跑状态，计数
        if(superUpspeed)
            superUpspeedCount();

        //画背景
        Paint paint1 =new Paint();
        paint1.setColor(Color.WHITE);
        drawCanvas.drawRect(0, 0, screenWidth, screenHeight, paint1);

        //画出两条线
        Paint paint2 =new Paint();
        paint2.setColor(Color.BLACK);

        drawCanvas.drawLine(road1,0,road2,screenHeight,paint2);
        drawCanvas.drawLine(road1+5*dist1,0,road2+5*dist2,screenHeight,paint2);

        //更新分数和能量并画出来
        grade +=scale;
        //如果处于双倍得分状态，再加一遍分数，并计数
        if(doubleGrade) {
            extraGrade += scale;
            doubleGradeCount --;
            if(doubleGradeCount < 0)
                doubleGrade =false;
        }
        changeEnergy();
        //如果处于双倍能量收集状态，再加一遍能量，并计数
        if(doubleEnergy) {
            changeEnergy();
            doubleEnergyCount --;
            if(doubleEnergyCount < 0)
                doubleEnergy =false;
        }
        Paint paint4 =new Paint();
        paint4.setTextSize(50);
        paint4.setColor(Color.BLACK);
        drawCanvas.drawText("分数：" + (grade+extraGrade), 0, 50, paint4);
        paint4.setColor(Color.BLUE);
        drawCanvas.drawText("能量：" + energy, 0, 100, paint4);
        paint4.setColor(Color.RED);
        if(doubleGrade)
            drawCanvas.drawText("×2",300,50,paint4 );
        if(doubleEnergy)
            drawCanvas.drawText("×2",300,100,paint4 );

        //当有子弹存在时画子弹
        if(bullet){
            Paint paint5 =new Paint();
            paint5.setColor(Color.BLACK);
            for(int i =0;i<bullets.size();i++){
                drawCanvas.drawCircle(bullets.get(i).getX(),bullets.get(i).getY(),bullets.get(i).getRadius(),paint5);
                bullets.get(i).shoot();
            }
            if(bullets.size() == 0)
                bullet =false;
        }

        changeLocation();
        Paint paint =new Paint();
        //如果有护盾
        if(shield){
            shieldCount();
            paint.setColor(Color.MAGENTA);
            drawCanvas.drawCircle(dotX + dotWidth /2, dotY + dotWidth /2, dotWidth/2 +10,paint);
        }
        //如果有超级护盾
        if(superShield){
            superShieldCount();
            paint.setColor(Color.MAGENTA);
            drawCanvas.drawCircle(dotX + dotWidth /2, dotY + dotWidth /2,dist2,paint);
        }
        //更新己方小圆的位置并画出来
        paint.setColor(Color.RED);
        //如果处于无敌状态
        if(invincible) {
            paint.setColor(Color.MAGENTA);
            invincibleCount();
        }
        RectF rect = new RectF(dotX, dotY, dotX + dotWidth, dotY + dotWidth);
        drawCanvas.drawOval(rect, paint);
        //如果有宝箱被打开，显示提示文字
        if(openBox) {
            boxCount();
            paint.setColor(boxColor);
            paint.setTextSize(30);
            drawCanvas.drawText(boxString, dotX, dotY - 50, paint);
        }


        if(superBomb){
            superBombCount();
            Paint paint7 =new Paint();
            paint7.setColor(Color.RED);
            paint7.setTextSize(400);
            drawCanvas.drawText("BOMB!!",0,screenHeight/2+200,paint7);
        }

        //更新计数值，如果计数到0则产生一个敌方小圆
        fall();
        //产生普通的敌人
        if(fall == 0){
            int location =generateEnemy();
            Enemy enemy =enemyFactory.getEnemy(Enemy.common_enemy);
            enemy.setState(road1+location*dist1+dist1/6,-dist1,dist1*2/3,location,enemySpeed);
            enemy.changePad(dist1, dist2, road1 + dist1 / 6, road2 + dist2/6,screenHeight);
            enemies.add(enemy);
        }
        //产生特殊的敌人
        if(specialFall == 0) {
            int type = generateSpecialEnemy();         //产生敌人的类型
            int location = generateEnemy();            //产生敌人的位置
            Enemy enemy =enemyFactory.getEnemy(type);
            enemy.setState(road1+location*dist1+dist1/6,-dist1,dist1*2/3,location,enemySpeed);
            enemy.changePad(dist1, dist2, road1 + dist1 / 6, road2 + dist2/6,screenHeight);
            enemies.add(enemy);
        }
        if(treasureFall == 0){
            int location = generateEnemy();
            Enemy treasure =enemyFactory.getEnemy(Enemy.treasure_box);
            treasure.setState(road1+location*dist1+dist1/6,-dist1,dist1*2/3,location,enemySpeed);
            treasure.changePad(dist1, dist2, road1 + dist1 / 6, road2 + dist2 / 6, screenHeight);
            enemies.add(treasure);
        }

        //画出所有敌人
        float ex,ey;                     //一个圆左上横纵坐标
        float etx,ety;                   //一个圆右下横纵坐标
        int type;
        Paint paint3 =new Paint();
        for(int i =0;i<enemies.size();i++){
            //让敌人靠近
            enemies.get(i).fall();
            ex =enemies.get(i).getX();
            ey =enemies.get(i).getY();
            etx =enemies.get(i).getTailX();
            ety =enemies.get(i).getTailY();
            type =enemies.get(i).getType();
            RectF rect1 =new RectF(ex,ey,etx,ety);
            switch (type){
                //如果是宝箱，画出黄色的
                case Enemy.treasure_box:
                    paint3.setColor(Color.YELLOW);
                    drawCanvas.drawRoundRect(rect1,10,10, paint3);
                    break;
                //如果是普通敌人,画出蓝色的
                case Enemy.common_enemy:
                    paint3.setColor(Color.BLUE);
                    drawCanvas.drawOval(rect1,paint3);
                    break;
                //如果是Strong敌人，画出黑色的
                case Enemy.strong_enemy:
                    paint3.setColor(Color.BLACK);
                    drawCanvas.drawOval(rect1,paint3);
                    break;
                //如果是Weak敌人，画出灰色的
                case Enemy.weak_enemy:
                    paint3.setColor(Color.GRAY);
                    drawCanvas.drawOval(rect1,paint3);
                    break;
                //如果是Crazy敌人，画出浅蓝色的
                case Enemy.crazy_enemy:
                    paint3.setColor(Color.CYAN);
                    drawCanvas.drawOval(rect1, paint3);
                    if(((CrazyEnemy)enemies.get(i)).crazy(dotX,dotY,dotWidth,dist2)){
                        ((CrazyEnemy)enemies.get(i)).goCrazy(dotX,dotY,dotWidth,enemySpeed);
                    }
                    break;
                default:
            }
            if(bullet){
                float bx,by,br;
                for(int j =0;j<bullets.size();j++){
                    br =bullets.get(j).getRadius();
                    bx =bullets.get(j).getX()-br;
                    by =bullets.get(j).getY()-br;
                    //如果有子弹撞到了敌人
                    if(enemies.get(i).crash(bx,by,br*2)){
                        bullets.remove(j);
                        if(type == Enemy.weak_enemy || type == Enemy.crazy_enemy) {
                            enemies.remove(i);
                            extraGrade += 100;
                            golds +=3;
                        }
                    }
                }
            }
            //碰撞无效
            boolean invalid =speedUp || superShield || superLaser || superUpspeed;
            //如果有碰撞,但是不处在碰撞无效状态
            if(enemies.get(i).crash(dotX, dotY, dotWidth) && !invalid) {
                //如果是被crazy敌人撞到，损失200能量
                if(type == Enemy.crazy_enemy){
                    if(!invincible) {
                        energy -= 200;
                        enemies.remove(i);
                        extraGrade += 50;
                        if (energy < 0)
                            energy = 0;
                    }
                }
                else if(type == Enemy.treasure_box){
                    Log.d("GameView","开宝箱");
                    int boxType =((TreasureBox)enemies.get(i)).openBox();
                    Log.d("GameView","宝箱被打开");
                    openBox(boxType);
                    Log.d("GameView","反应结束");
                    enemies.remove(i);
                }
                else {
                    //如果是被黑球砸中，或者没带护盾，游戏结束
                    if ((type == 1 || !shield) && !invincible)
                        gameover = true;
                }
            }
        }

    }

    /**
     * 用于判断crazy敌人有没有发疯
     * @param ex          传入敌人的位置的横坐标
     * @param ey          传入敌人的位置的纵坐标
     * @param ed          传入敌人的直径
     * @param eType       传入敌人的类型
     * @return         返回敌人与己方的距离，如果距离小于dist2，则发疯
     */
    private double crazing(float ex,float ey,float ed,float eType){
        if(eType != Enemy.crazy_enemy)
            return 0;
        double d =Math.hypot((double) ((ex+ed/2)-(dotX + dotWidth /2)), (double) ((ey+ed/2) - (dotY + dotWidth /2)));
        if(d < dotWidth /2+ed/2+dist2)
            return d;
        return 0;
    }

    /**
     * 根据打开箱子类型做出相应的动作
     */
    private void openBox(int boxtype){
        Log.d("GameView","宝箱动作");
        openBox =true;
        boxCount =20;
        switch (boxtype){
            //开的是一个空宝箱，分数加50
            case 1:
                extraGrade +=50;
                boxString ="空箱子";
                boxColor =Color.BLACK;
                break;
            //开的是金币宝箱，金币加3
            case 2:
                golds +=3;
                boxString ="金币 +3";
                boxColor =Color.YELLOW;
                break;
            //开的是金币宝箱，金币加6
            case 3:
                golds +=6;
                boxString ="金币 +6";
                boxColor =Color.YELLOW;;
                break;
            //开的是金币宝箱，金币加9
            case 4:
                golds +=9;
                boxString ="金币 +9";
                boxColor =Color.YELLOW;
                break;
            //开的是金币宝箱，金币加12
            case 5:
                golds +=12;
                boxString ="金币 +12";
                boxColor =Color.YELLOW;
                break;
            //开的是金币宝箱，金币加15
            case 6:
                golds +=15;
                boxString ="金币 +15";
                boxColor =Color.YELLOW;
                break;
            //开的是能量宝箱，能量加50
            case 7:
                energy +=50;
                boxString ="能量 +50";
                boxColor =Color.BLUE;
                break;
            //开的是能量宝箱，能量加100
            case 8:
                energy +=100;
                boxString ="能量 +100";
                boxColor =Color.BLUE;
                break;
            //开的是能量宝箱，能量加150
            case 9:
                energy +=150;
                boxString ="能量 +150";
                boxColor =Color.BLUE;
                break;
            //开的是能量宝箱，能量加200
            case 10:
                energy +=200;
                boxString ="能量 +200";
                boxColor =Color.BLUE;
                break;
            //开的是道具宝箱，得到无敌
            case 11:
                invincible =true;
                invincibleCount =invincibleTime;
                boxString =" 无 敌";
                boxColor =Color.GREEN;
                break;
            //开的是道具宝箱，得到双倍得分
            case 12:
                doubleGrade =true;
                doubleGradeCount =doubleGradeTime;
                boxString ="双倍得分";
                boxColor =Color.GREEN;
                break;
            //开的是道具宝箱，得到助跑
            case 13:
                speedUp =true;
                speedUpCount =speedUpTime;
                sleepTime =sleepTime/2;
                boxString =" 助 跑";
                boxColor =Color.GREEN;
                break;
            //开的是道具宝箱，得到双倍能量收集
            case 14:
                doubleEnergy =true;
                doubleEnergyCount =doubleEnergyTime;
                boxString ="双倍能量";
                boxColor =Color.GREEN;
                break;
            default:
        }
    }

    /**
     * 宝箱提示文字计数
     */
    private void boxCount(){
        boxCount--;
        if(boxCount == 0)
            openBox =false;
    }

    /**
     * 无敌时间计数
     */
    private void invincibleCount(){
        invincibleCount --;
        if(invincibleCount == 0)
            invincible =false;
    }

    /**
     * 助跑时间计数
     */
    private void speedUpCount(){
        speedUpCount --;
        if(speedUpCount == 0) {
            speedUp = false;
            sleepTime =40;
            shield =true;
            shieldCount =40;
        }
    }

    /**
     * 护盾产生响应，能量足够则产生护盾，不够则不产生
     * @return       能量够的话返回true，否则返回false
     */
    public boolean generateShield(){
        if(energy < 100)
            return false;
        if(shield || superShield || superUpspeed)
            return false;
        shield =true;
        shieldCount =shieldTime;
        energy -=100;
        return true;
    }

    /**
     * 护盾存在时间计数
     */
    private void shieldCount(){
        shieldCount --;
        if(shieldCount == 0)
            shield =false;
    }

    /**
     * 子弹产生响应
     * @return
     */
    public boolean generateBullet(){
        if(energy < bulletConsume)
            return false;
        if(superLaser)
            return false;
        energy -=bulletConsume;
        Bullet bullet =new Bullet(dotX + dotWidth /2, dotY, dotWidth /8);
        bullet.changePad(road1,road2,dist1,dist2,screenHeight);
        bullets.add(bullet);
        this.bullet =true;
        return true;
    }

    /**
     * 超级护盾产生响应
     */
    public boolean generateSuperShield(){
        if(energy < 300)
            return false;
        if(superShield)
            return false;
        superShield =true;
        superShieldCount =superShieldTime;
        energy -=300;
        return true;
    }

    /**
     * 超级护盾持续时间计数
     */
    private void superShieldCount(){
        superShieldCount --;
        if(superShieldCount == 0)
            superShield =false;
    }

    /**
     * 判断指定敌人是否被超级护盾杀死
     * @param ex
     * @param ey
     * @param ed
     * @return          被杀死则返回true，否则返回false
     */
    private boolean superShieldKill(float ex,float ey,float ed){
        double d =Math.hypot((double) ((ex+ed/2)-(dotX + dotWidth /2)), (double) ((ey+ed/2) - (dotY + dotWidth /2)));
        if(d < dotWidth /2+ed/2+dist2)
            return true;
        return false;
    }

    /**
     * 超级激光产生响应
     */
    public boolean generateSuperLaser(){
        if(energy < 300)
            return false;
        if(superLaser)
            return false;
        superLaser =true;
        superLaserCount =superLaserTime;
        energy -=300;
        return true;
    }

    /**
     * 超级激光持续时间计数
     */
    private void superLaserCount(){
        superLaserCount --;
        if(superLaserCount == 0)
            superLaser =false;
    }

    /**
     * 超级炸弹产生响应
     */
    public boolean generateSuperBomb(){
        if(energy < 300)
            return false;
        superBomb =true;
        superBombCount =superBombTime;
        energy -=300;
        extraGrade +=enemies.size()*100;
        golds +=enemies.size()*3;
        enemies.clear();
        //一段时间内不产生敌人
        fall =50;
        specialFall +=50;
        treasureFall +=20;
        return true;
    }

    /**
     * 超级炸弹提示时间计时
     */
    private void superBombCount(){
        superBombCount --;
        if(superBombCount == 0)
            superBomb =false;
    }

    /**
     * 超级加速产生响应
     */
    public boolean generateSuperUpspeed(){
        if(energy < 300)
            return false;
        if(superUpspeed)
            return false;
        superUpspeed =true;
        superUpspeedCount =superUpspeedTime;
        energy -=300;
        sleepTime =10;
        return true;
    }

    /**
     * 超级加速持续时间计数
     */
    private void superUpspeedCount(){
        superUpspeedCount --;
        if(superUpspeedCount == 0){
            superUpspeed =false;
            sleepTime =40;
            shield =true;
            shieldCount =40;
        }
    }

    /**
     * 能量增加
     */
    private void changeEnergy(){
        energiesCount --;
        if(energiesCount < 0){
                energy ++;
            energiesCount =energyScale;
        }
    }
    /**
     * 下落计数，计数到0则产生一个新的敌人
     */
    private void fall(){
        fall --;
        if(fall < 0)
            fall =interval;      //重新计数
        //分数少于500之前不产生特殊敌人
        if(grade < 500)
            return;
        specialFall --;
        if(specialFall < 0)
            specialFall =specialIntv;
        //分数少于1500之前不产生宝箱
        if(grade < 1500)
            return;
        treasureFall --;
        if(treasureFall < 0)
            treasureFall =treasure;
    }

    /**
     * 改变己方小球的位置，并对左右最大位移距离加以限制
     */
    private void changeLocation(){
        dotX +=speed;
        //不得超出最大右方限制距离
        if(dotX > screenWidth- dotWidth -pad)
            dotX =screenWidth- dotWidth -pad;
        //不得超出最大左方限制距离
        if(dotX < pad)
            dotX =pad;
    }

    /**
     * 当难度发生改变时调用
     */
    private void changeHard(){
        //若分数没有高于最大分数，难度不变
        if(grade < maxScore)
            return;
        scale ++;                     //得分倍数增加
        if(energyScale > 1)
            energyScale =12/(scale-1);      //能量收集速度加快
        maxScore +=scale*500;        //最大分数增加
        if(interval > 20)
            interval -= 5;
        else if(interval > 10)
            interval -= 3;
        else if(interval > 5)
            interval --;                 //产生一个新的敌人的时间间隔减少
        treasure -=(scale-3)*2;
        enemySpeed +=2;               //敌人速度增加
    }

    /**
     * 随机在一条道上产生新的敌人
     * @return        返回敌人产生在哪一条道上
     */
    private int generateEnemy(){
        return (int)(Math.random()*5);
    }
    private int generateSpecialEnemy(){
        return (int)(Math.random()*Enemy.specialAmount) +1;
    }

    /**
     * 游戏结束时调用
     */
    private void gameover(){
        if(!first)
            return;
        first =false;
        if(gameoverListener != null)
            gameoverListener.onGameover(this);
    }

    /**
     * 游戏暂停时调用
     */
    public void gamePause(){
        pause =true;
    }

    /**
     * 游戏继续时调用
     */
    public void gameContinue(){
        pause =false;
    }

    /**
     * 游戏退出时调用
     */
    public void gameExit(){
        running =false;
    }

    public void setOnGameoverListener(OnGameoverListener gl){
        gameoverListener =gl;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running =true;
        new RenderThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running =false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 重力感应发生改变时调用
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        speed =(int)(event.values[SensorManager.DATA_Y]*6);
    }

    /**
     * 对view进行测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int mWidth =MeasureSpec.getSize(widthMeasureSpec);
        int mHeight =MeasureSpec.getSize(heightMeasureSpec);
        int widthMode =MeasureSpec.getMode(widthMeasureSpec);
        int heightMode =MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension((int)screenWidth,(int)screenHeight);
        else if(widthMode == MeasureSpec.AT_MOST)
            setMeasuredDimension((int)screenWidth,mHeight);
        else if(heightMode == MeasureSpec.AT_MOST)
            setMeasuredDimension(mWidth,(int)screenHeight);
    }

    /**
     * 绘图线程
     */
    private class RenderThread extends Thread implements Runnable{
        public void run(){
            Canvas canvas =null;
            while (running){
                try{
                    sleep(sleepTime);
                    canvas =surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder){
                        draw();                    //双缓冲消除闪烁
                        render(canvas);
                    }
                }catch (Exception e){}
                finally {
                    if(canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            drawBitmap.recycle();
        }
    }
    //定义自定义监听器接口
    public interface OnGameoverListener{
        void onGameover(GameView gv);
    }
}
