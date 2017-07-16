package com.example.asus.cleardots.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.cleardots.R;
import com.example.asus.cleardots.data.GradeBean;
import com.example.asus.cleardots.view.GameView;
import com.example.asus.cleardots.view.GradeView;

/**
 * Created by ASUS on 2017/1/24.
 */
public class GameActivity extends Activity{

    private GradeBean gradeBean;
    private GameView gameView;
    private GradeView gradeView;
    private Button newgame;
    private Button remind;
    private Button back;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_game);

        gradeBean =new GradeBean(this);
        gameView =(GameView)findViewById(R.id.gameView);
        gradeView =(GradeView)findViewById(R.id.gradeView);
        newgame =(Button)findViewById(R.id.newgame);
        remind =(Button)findViewById(R.id.remind);
        back =(Button)findViewById(R.id.back);

        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGrade(gameView.getScore());
                gameView.init();
                gradeView.init();
                gradeView.invalidate();
                gradeView.draw(new Canvas());
            }
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean remind =gameView.remind();
                if(!remind) {
                    gameovers();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGrade(gameView.getScore());
                finish();
            }
        });
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x =(int)event.getX();
                int y =(int)event.getY();
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    boolean b =gameView.down(x,y);
                    return b;
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    boolean b =gameView.move(x,y);
                    return b;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    gameView.up();
                    gradeView.changeGrade(gameView.getScore());
                    gradeView.invalidate();
                    gradeView.draw(new Canvas());
                    return true;
                }
                else
                    return false;
            }
        });
        gameView.setOnGameoverListener(new GameView.OnGameoverListener() {
            @Override
            public void gameover() {
                Log.i("GameActivity","gameover");
                gameovers();
            }
        });
    }
    private void gameovers(){
        Toast.makeText(this,"游戏结束！",Toast.LENGTH_SHORT).show();
    }

    /**
     * 游戏结束时更新排行榜
     * @param grade
     */
    private void updateGrade(int grade){
        if(grade == 0)
            return;
        gradeBean.setGrade(grade);          //先更改grade的值
        gradeBean.insert();                 //再判断插入
    }

}
