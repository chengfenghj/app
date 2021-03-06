package com.example.asus.freerun.enemies;

/**
 * Created by ASUS on 2017/2/3.
 */
public class Enemy {

    public static final int common_enemy =0;          //普通敌人
    public static final int strong_enemy =1;          //铁头敌人
    public static final int weak_enemy =2;            //弱鸡敌人
    public static final int crazy_enemy =3;           //吸蓝敌人
    public static final int treasure_box =4;          //珍宝箱子

    public static final int specialAmount =3;

    protected int type;
    protected float speed;
    protected float x;
    protected float y;
    protected int location;                //在哪一条道上
    protected float diameter;
    protected double pad;                  //当敌人靠近时对齐直径
    protected float speedX;               //当敌人靠近时对齐横坐标

    public Enemy(){
        pad =0;
    }
//    public Enemy(float x,float y,float diameter,int location,float speed,int type){
//        this.x =x;
//        this.y =y;
//        this.location =location;
//        this.diameter =diameter;
//        this.speed =speed;
//        this.type =type;
//        pad =0;
//    }
    public void setState(float x,float y,float diameter,int location,float speed){
        this.x =x;
        this.y =y;
        this.location =location;
        this.diameter =diameter;
        this.speed =speed;
    }
    public float getX(){
        return x;
    }
    public float getTailX(){
        return x+diameter;
    }
    public float getY(){
        return y;
    }
    public float getTailY(){
        return y+diameter;
    }
    public int getType(){
        return type;
    }
    public float getDiameter(){
        return diameter;
    }
    public void setSpeed(float speed){
        this.speed =speed;
    }
    public void setSpeedX(float speedX){
        this.speedX =speedX;
    }
    public void changePad(float dist1, float dist2, float road1, float road2, float screenHeight){
        pad =(dist2*2/3-dist1*2/3)/((screenHeight+dist1)/speed);
        speedX =((road1+dist1*location)-(road2+dist2*location))/((screenHeight+dist1)/speed);
    }
    public void fall(){
        x -= speedX;
        y +=speed;
        diameter +=pad;
    }

    /**
     * 检测是否发生碰撞
     * @param potx           传入小球的横坐标
     * @param poty           传入小球的纵坐标
     * @param potw           传入小球的直径
     * @return                碰撞则返回true，否则返回false
     */
    public boolean crash(float potx,float poty,float potw){
        //计算出圆心的距离
        double d =Math.hypot((double) ((x+diameter/2)-(potx + potw/2)), (double) ((y+diameter/2) - (poty+potw/2)));
        //如果圆心的距离超过两个圆半径的相加，则说明发生碰撞
        if(d*2 < potw+diameter)
            return true;
        return false;
    }
}
