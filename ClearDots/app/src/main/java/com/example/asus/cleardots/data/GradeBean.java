package com.example.asus.cleardots.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ASUS on 2017/2/1.
 */
public class GradeBean {

    private DbHelper dbHelper;
    private final int LENGHT =10;
    private int grade;
    private int[] grades;
    private int[] agrade;

    public GradeBean(Context context){
        grades =new int[LENGHT];
        agrade =new int[LENGHT];
        dbHelper =new DbHelper(context,1);
    }

    public void setGrade(int grade){
        this.grade =grade;
    }
    public int getGrade(){
        return grade;
    }
    public void setAgrade(int[] agrade){
        this.agrade =agrade;
    }
    public int[] getAgrade(int index){
        changePots(grades[index]);
        return agrade;
    }

    /**
     * 从数据库中取出结果后更新到grades中
     * @param cursor
     */
    private void updateGrades(Cursor cursor){
        boolean b =true;
        cursor.moveToFirst();
        if(cursor.getCount() == 0)
            b =false;
        for(int i =0;i<LENGHT;i++){
            if(b){
                grades[i] =cursor.getInt(1);
                b =cursor.moveToNext();
                continue;
            }
            grades[i]=0;
        }
    }

    private void changePots(int grade){
        for(int i =0;i<LENGHT;i++){
            agrade[i] =dif(grade);
            grade =grade - maxGrade(agrade[i]);
            if(grade < 0)
                grade =0;
        }
    }

    /**
     * 计算指定分数对应的难度
     * @param grade        传入分数
     * @return             返回难度
     */
    private int dif(int grade){
        int g =1;
        int d =0;
        while (g - grade <= 0){
            g *=4;
            d++;
        }
        return d;
    }

    /**
     * 计算指定难度对应的最大分数
     * @param dif        传入难度
     * @return           返回分数
     */
    private int maxGrade(int dif){
        int g =1;
        for(int i =0;i<dif-1;i++)
            g *=4;
        return g;
    }

    /**
     * 比较是否可以插入
     * @return     可以插入返回true，否则返回false
     */
    private boolean compare(){
        boolean insert =false;
        int t;
        for(int i =0;i<LENGHT;i++){
            if(grade > grades[i]){
                t =grades[i];
                grades[i] =grade;
                grade =t;
                insert =true;
            }
        }
        return insert;
    }

    /**
     * 查询数据库,view获取数据时调用
     */
    public void query(){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        Cursor cursor =query(db);
        updateGrades(cursor);
        db.close();
    }

    /**
     * 查询整张数据库表
     * @param db
     * @return      返回所有查询到的值
     */
    private Cursor query(SQLiteDatabase db){
        Cursor cursor;
        cursor =db.query("Grade",null,null,null,null,null,null);
        return cursor;
    }

    /**
     * 插入数据库，activity更新数据时调用
     */
    public void insert(){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        Cursor cursor =query(db);
        updateGrades(cursor);
        if(compare())
            insert(db);
        db.close();
    }

    /**
     * 将新的成绩表插入数据库
     * @param db
     */
    private void insert(SQLiteDatabase db){
        db.delete("Grade",null,null);
        ContentValues values =new ContentValues();
        for(int i=0;i<LENGHT;i++){
            values.clear();
            values.put("grade",grades[i]);
            db.insertWithOnConflict("Grade",null,values,SQLiteDatabase.CONFLICT_IGNORE);
        }
    }
}
