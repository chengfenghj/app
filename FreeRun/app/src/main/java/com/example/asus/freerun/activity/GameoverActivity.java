package com.example.asus.freerun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.freerun.R;
import com.example.asus.freerun.data.GradeBean;
import com.example.asus.freerun.data.MaterialsBean;

/**
 * Created by ASUS on 2017/2/6.
 */
public class GameoverActivity extends Activity {

    private TextView gradeView;
    private TextView maxGradeView;
    private TextView coinView;
    private Button restart;
    private Button exit;

    private GradeBean gradeBean;
    private MaterialsBean materialsBean;
    private int grade;
    private int maxGrade;
    private int golds;
    private int coins;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_gameover);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent =getIntent();
        grade =intent.getIntExtra("grade",1);
        golds =intent.getIntExtra("golds",1);
        coins =grade/151+golds;
        gradeBean =new GradeBean(this);
        materialsBean =new MaterialsBean(this);

        gradeView =(TextView)findViewById(R.id.grade);
        maxGradeView =(TextView)findViewById(R.id.max_grade);
        coinView =(TextView)findViewById(R.id.coin);
        exit =(Button)findViewById(R.id.exit);
        restart =(Button)findViewById(R.id.again);

        changeGrade();
        gradeView.setText("分数：" + grade);
        maxGradeView.setText("最高分数：" + maxGrade);
        coinView.setText("金 币： +"+ coins);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    public void onBackPressed(){
        Intent intent =new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * 取出最高分
     */
    private void changeGrade(){
        gradeBean.query();
        maxGrade =gradeBean.getMaxGrade();
        if(maxGrade < grade) {
            gradeBean.setMaxGrade(grade);
            gradeBean.insert();
            maxGrade = grade;
        }
        materialsBean.query();
        materialsBean.setGolds(coins + materialsBean.getGolds());
        materialsBean.insert();
    }
}
