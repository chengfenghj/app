package com.example.asus.freerun.enemies;

/**
 * Created by ASUS on 2017/7/16.
 */

public class CrazyEnemy extends Enemy {

    public CrazyEnemy(){
        type =crazy_enemy;
    }
    public boolean crazy(float dx,float dy,float dw,float dist2){
        //求出这个敌人与主角的距离
        double d =Math.hypot((double) ((x+diameter/2)-(dx + dw /2)), (double) ((y+diameter/2) - (dy + dw /2)));
        //如果距离小于dist2，则crazy
        if(d < dw /2+diameter/2+dist2)
            return true;
        return false;
    }
    public void goCrazy(float dx,float dy,float dw,float nspeed){
        float ex =x+diameter/2;
        float ey =y+diameter/2;
        float cx =dx + dw /2;
        float cy =dy + dw /2;
        double d =Math.hypot((double) (ex-cx), (double) (ey -cy));
        setSpeed((float) (Math.abs(ey - cy) * 3 * nspeed / d));
        setSpeedX((float) ((ex - cx) * 3 * nspeed / d));

    }
}
