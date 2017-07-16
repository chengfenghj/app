package com.example.asus.cleardots.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 2017/2/1.
 */
public class DbHelper extends SQLiteOpenHelper{

    private Context context;
    private static final String sql ="create table Grade ("+           //创建数据库表Grade
            "id integer primary key autoincrement,"+                   //id
            "grade integer)";                                               //成绩

    public DbHelper(Context context,int version){
        super(context,"GradeList.db",null,version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Grade");
        onCreate(db);
    }
}
