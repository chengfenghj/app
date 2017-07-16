package com.example.asus.freerun.enemies;

/**
 * Created by ASUS on 2017/7/16.
 */

public class StrongEnemy extends Enemy {

    public StrongEnemy(){
        type =strong_enemy;
    }
    public void setState(float x,float y,float diameter,int location,float speed){
        this.x =x;
        this.y =y;
        this.location =location;
        this.diameter =diameter;
        this.speed =2*speed;
    }
}
