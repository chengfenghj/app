package com.example.asus.freerun.enemies;

import android.util.Log;

/**
 * Created by ASUS on 2017/7/16.
 */

public class TreasureBox extends Enemy {

    private String boxString;
    private int boxColor;

    public TreasureBox(){
        type =treasure_box;
    }

    /**
     *          只负责告诉你打开的是什么箱子
     *
     * @return
     */
    public int openBox(){
        Log.d("TreasureBox","随机开宝箱");
        int boxType =0;
        int open =(int)(Math.random()*23);
        switch (open){
            //开的是一个空宝箱，分数加50
            case 0:
            case 1:
            case 2:
                boxType =1;
                break;
            //开的是金币宝箱，金币加3
            case 3:
            case 4:
            case 5:
                boxType =2;
                break;
            //开的是金币宝箱，金币加6
            case 6:
            case 7:
                boxType =3;
                break;
            //开的是金币宝箱，金币加9
            case 8:
            case 9:
                boxType =4;
                break;
            //开的是金币宝箱，金币加12
            case 10:
                boxType =5;
                break;
            //开的是金币宝箱，金币加15
            case 11:
                boxType =6;
                break;
            //开的是能量宝箱，能量加50
            case 12:
            case 13:
            case 14:
                boxType =7;
                break;
            //开的是能量宝箱，能量加100
            case 15:
            case 16:
                boxType =8;
                break;
            //开的是能量宝箱，能量加150
            case 17:
                boxType =9;
                break;
            //开的是能量宝箱，能量加200
            case 18:
                boxType =10;
                break;
            //开的是道具宝箱，得到无敌
            case 19:
                boxType =11;
                break;
            //开的是道具宝箱，得到双倍得分
            case 20:
                boxType =12;
                break;
            //开的是道具宝箱，得到助跑
            case 21:
                boxType =13;
                break;
            //开的是道具宝箱，得到双倍能量收集
            case 22:
                boxType =14;
                break;
            default:
        }
        Log.d("TreasureBox","boxType ="+boxType);
        return boxType;
    }
}
