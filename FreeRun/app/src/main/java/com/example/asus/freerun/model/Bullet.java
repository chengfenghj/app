package com.example.asus.freerun.model;

/**
 * Created by ASUS on 2017/2/4.
 */
public class Bullet {

    private float x;
    private float y;
    private float radius;
    private int speed =20;
    private float pad;

    public Bullet(float x,float y,float radius){
        this.x =x;
        this.y =y;
        this.radius =radius;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getRadius(){
        return radius;
    }
    public void changePad(float road1,float road2,float dist1,float dist2,float screenHeight){
        int location =getLocation(road2,dist2);
        float dx1 =road1+location*dist1;
        float dx2 =road2+location*dist2;
        float endX =((2*dx1-dx2)*y+screenHeight*x)/(y+screenHeight);
        pad =(endX-x)/(y/speed);
    }
    public void shoot(){
        x +=pad;
        y -=speed;
    }
    private int getLocation(float road2,float dist2){
        int i;
        for(i =0;i<5;i++){
            if(road2+i*dist2 > x)
                break;
        }
        if(i != 0)
         return i - 1;
        return 0;
    }
}
