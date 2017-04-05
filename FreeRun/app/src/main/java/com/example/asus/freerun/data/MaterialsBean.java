package com.example.asus.freerun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ASUS on 2017/2/6.
 */
public class MaterialsBean {


    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private int golds =0;

    public MaterialsBean(Context context){
        dbHelper =new DbHelper(context,"RunData.db",null,dbHelper.version);
    }
    public int getGolds(){
        return golds;
    }
    public void setGolds(int golds){
        this.golds = golds;
    }
    public void query(){
        db =dbHelper.getReadableDatabase();
        Cursor cursor =query(db);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            golds = cursor.getInt(1);
        }
        db.close();
    }
    private Cursor query(SQLiteDatabase db){
        Cursor cursor =db.query("Materials", null, null, null, null, null, null);
        return cursor;
    }
    public void insert(){
        db =dbHelper.getWritableDatabase();
        insert(db);
        db.close();
    }
    private void insert(SQLiteDatabase db){
        db.delete("Materials",null,null);
        ContentValues value =new ContentValues();
        value.put("golds", golds);
//        db.update("Materials",value,null,null);
        db.insert("Materials",null,value);
    }
}
