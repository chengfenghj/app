package com.example.asus.freerun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ASUS on 2017/2/6.
 */
public class GradeBean {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private int maxGrade =0;

    public GradeBean(Context context){
        dbHelper =new DbHelper(context,"RunData.db",null,dbHelper.version);
    }
    public int getMaxGrade(){
        return maxGrade;
    }
    public void setMaxGrade(int grade){
        maxGrade =grade;
    }
    public void query(){
        db =dbHelper.getReadableDatabase();
        Cursor cursor =query(db);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            maxGrade = cursor.getInt(1);
        }
        db.close();
    }
    private Cursor query(SQLiteDatabase db){
        Cursor cursor =db.query("Grade", null, null, null, null, null, null);
        return cursor;
    }
    public void insert(){
        db =dbHelper.getWritableDatabase();
        insert(db);
        db.close();
    }
    private void insert(SQLiteDatabase db){
        db.delete("Grade",null,null);
        ContentValues value =new ContentValues();
        value.put("grade",maxGrade);
//        db.update("Grade", value, null, null);
        db.insert("Grade",null,value);
    }
}
