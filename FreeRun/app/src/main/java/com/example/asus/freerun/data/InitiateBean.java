package com.example.asus.freerun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ASUS on 2017/2/6.
 */
public class InitiateBean {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private int initiateEnerge =0;
    private int shieldTime =0;
    private int bulletEnerge =0;

    public InitiateBean(Context context){
        dbHelper =new DbHelper(context,"RunData.db",null,dbHelper.version);
    }
    public int getInitiateEnerge(){
        return initiateEnerge;
    }
    public void setInitiateEnerge(int initiateEnerge){
        this.initiateEnerge =initiateEnerge;
    }
    public int getShieldTime(){
        return shieldTime;
    }
    public void setShieldTime(int shieldTime){
        this.shieldTime =shieldTime;
    }
    public int getBulletEnerge(){
        return bulletEnerge;
    }
    public void setBulletEnerge(int bulletEnerge){
        this.bulletEnerge =bulletEnerge;
    }
    private void init(){
        initiateEnerge =200;
        shieldTime =20;
        bulletEnerge =100;
    }
    public void query(){
        db =dbHelper.getReadableDatabase();
        Cursor cursor =query(db);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            initiateEnerge = cursor.getInt(1);
            shieldTime = cursor.getInt(2);
            bulletEnerge = cursor.getInt(3);
        }
        else
            init();
        db.close();
    }
    private Cursor query(SQLiteDatabase db){
        Cursor cursor =db.query("Initiate", null, null, null, null, null, null);
        return cursor;
    }
    public void insert(){
        db =dbHelper.getWritableDatabase();
        insert(db);
        db.close();
    }
    private void insert(SQLiteDatabase db){
        db.delete("Initiate", null, null);
        ContentValues value =new ContentValues();
        value.put("initiateEnerge", initiateEnerge);
        value.put("shieldTime", shieldTime);
        value.put("bulletEnerge", bulletEnerge);
        db.insert("Initiate",null,value);
    }
}
