package com.example.asus.freerun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ASUS on 2017/2/11.
 */
public class EquipmentBean {

    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private int superShieldAmount =0;
    private int superUpspeedAmount =0;
    private int superLaserAmount =0;
    private int superBombAmount =0;

    public EquipmentBean(Context context){
        dbHelper =new DbHelper(context,"RunData.db",null,dbHelper.version);
    }
    public int getSuperShieldAmount(){
        return superShieldAmount;
    }
    public void setSuperShieldAmount(int superShieldAmount){
        this.superShieldAmount =superShieldAmount;
    }
    public int getSuperUpspeedAmount(){
        return  superUpspeedAmount;
    }
    public void setSuperUpspeedAmount(int superUpspeedAmount){
        this.superUpspeedAmount =superUpspeedAmount;
    }
    public int getSuperLaserAmount(){
        return superLaserAmount;
    }
    public void setSuperLaserAmount(int superLaserAmount){
        this.superLaserAmount =superLaserAmount;
    }
    public int getSuperBombAmount(){
        return superBombAmount;
    }
    public void setSuperBombAmount(int superBombAmount){
        this.superBombAmount =superBombAmount;
    }
    public void query(){
        db =dbHelper.getReadableDatabase();
        Cursor cursor =query(db);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            superShieldAmount = cursor.getInt(1);
            superUpspeedAmount = cursor.getInt(2);
            superLaserAmount = cursor.getInt(3);
            superBombAmount = cursor.getInt(4);
        }
        else{
            superShieldAmount =5;
            superUpspeedAmount =5;
            superLaserAmount =5;
            superBombAmount =5;
        }
        db.close();
    }
    private Cursor query(SQLiteDatabase db){
        Cursor cursor =db.query("Equipment", null, null, null, null, null, null);
        return cursor;
    }
    public void insert(){
        db =dbHelper.getWritableDatabase();
        insert(db);
        db.close();
    }
    private void insert(SQLiteDatabase db){
        db.delete("Equipment",null,null);
        ContentValues value =new ContentValues();
        value.put("shieldAmount",superShieldAmount);
        value.put("upspeedAmount",superUpspeedAmount);
        value.put("laserAmount",superLaserAmount);
        value.put("bombAmount",superBombAmount);
//        db.update("Equipment", value, null, null);
        db.insert("Equipment",null,value);
    }
}
