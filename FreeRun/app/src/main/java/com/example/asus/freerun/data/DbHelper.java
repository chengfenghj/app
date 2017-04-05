package com.example.asus.freerun.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 2017/2/5.
 */
public class DbHelper extends SQLiteOpenHelper{

    public static final int version =6;

    private static final String grade_table ="create table Grade ("+                   //创建数据库表Grade
            "id integer primary key autoincrement,"+                                       //id
            "grade integer)";                                                                 //成绩
    private static final String materials_table ="create table Materials ("+         //创建数据库表Materials
            "id integer primary key autoincrement,"+                                      //id
            "golds integer)";                                                                 //金币数量
    private static final String initiate_table ="create table Initiate ("+           //创建数据库表Initiate
            "id integer primary key autoincrement,"+                                      //id
            "initiateEnerge integer,"+                                                      //初始能量
            "shieldTime interge," +                                                         //护盾时间
            "bulletEnerge interge)";                                                        //子弹消耗能量
    private static final String equipment_table ="create table Equipment ("+        //创建数据库表Initiate
            "id integer primary key autoincrement,"+                                      //id
            "shieldAmount integer,"+                                                        //超级护盾的数量
            "upspeedAmount interge," +                                                      //超级加速的数量
            "laserAmount interge," +                                                        //超级激光的数量
            "bombAmount intergr)";                                                          //超级炸弹的数量


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(grade_table);
        db.execSQL(materials_table);
        db.execSQL(initiate_table);
        db.execSQL(equipment_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                db.execSQL(equipment_table);
            case 6:
        }
//        db.execSQL("drop table if exists Grade");
//        db.execSQL("drop table if exists Materials");
//        db.execSQL("drop table if exists Initiate");
//        db.execSQL("drop table if exists Equipment");
//        db.execSQL(equipment_table);
    }
}
