package com.example.asus.freerun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.freerun.R;
import com.example.asus.freerun.data.EquipmentBean;
import com.example.asus.freerun.view.GameView;

/**
 * Created by ASUS on 2017/2/3.
 */
public class GameActivity extends Activity {

    private GameView gameView;
    private Button superShield;
    private Button superUpspeed;
    private Button superLaser;
    private Button superBomb;
    private TextView superShieldAmount;
    private TextView superUpspeedAmount;
    private TextView superLaserAmount;
    private TextView superBombAmount;

    private EquipmentBean equipmentBean;
    private int shieldAmount;
    private int upspeedAmount;
    private int laserAmount;
    private int bombAmount;
    private boolean isChange =false;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gameView =(GameView)findViewById(R.id.gameView);
        superShield =(Button)findViewById(R.id.shield);
        superUpspeed =(Button)findViewById(R.id.upspeed);
        superLaser =(Button)findViewById(R.id.laser);
        superBomb =(Button)findViewById(R.id.bomb);
        superShieldAmount =(TextView)findViewById(R.id.shieldAmount);
        superUpspeedAmount =(TextView)findViewById(R.id.upspeedAmount);
        superLaserAmount =(TextView)findViewById(R.id.laserAmount);
        superBombAmount =(TextView)findViewById(R.id.bombAmount);

        equipmentBean =new EquipmentBean(this);
        equipmentBean.query();
        shieldAmount =equipmentBean.getSuperShieldAmount();
        upspeedAmount =equipmentBean.getSuperUpspeedAmount();
        laserAmount =equipmentBean.getSuperLaserAmount();
        bombAmount =equipmentBean.getSuperBombAmount();

        superShieldAmount.setText(String.valueOf(shieldAmount));
        superUpspeedAmount.setText(String.valueOf(upspeedAmount));
        superLaserAmount.setText(String.valueOf(laserAmount));
        superBombAmount.setText(String.valueOf(bombAmount));

        superShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shieldAmount < 1)
                    return;
                if(!gameView.generateSuperShield())
                    return;
                shieldAmount --;
                superShieldAmount.setText(String.valueOf(shieldAmount));
                isChange =true;
            }
        });

        superUpspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upspeedAmount < 1)
                    return;
                if(!gameView.generateSuperUpspeed())
                    return;
                upspeedAmount --;
                superUpspeedAmount.setText(String.valueOf(upspeedAmount));
                isChange =true;
            }
        });

        superLaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                gameView.generateSuperLaser();
            }
        });

        superBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bombAmount < 1)
                    return;
                if(!gameView.generateSuperBomb())
                    return;
                bombAmount --;
                superBombAmount.setText(String.valueOf(bombAmount));
                isChange =true;
            }
        });

        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x =event.getX();
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (x < gameView.getScreenWidth() / 2 - 50)
                        return gameView.generateShield();
                    if (x > gameView.getScreenWidth() / 2 + 50)
                        return gameView.generateBullet();
                }
                return false;
            }
        });
        gameView.setOnGameoverListener(new GameView.OnGameoverListener() {
            @Override
            public void onGameover(GameView gv) {

                Intent intent =new Intent(GameActivity.this,GameoverActivity.class);
                intent.putExtra("grade",gameView.getGrade());
                intent.putExtra("golds",gameView.getGolds());
                startActivityForResult(intent,1);
                updateDataBase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    gameView.init();
                    isChange =false;
                }
                else if(resultCode == RESULT_CANCELED) {
                    gameView.gameExit();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.gameContinue:
                gameView.gameContinue();
                break;
            case R.id.gameReastart:
                gameView.init();
                break;
            case R.id.gameExit:
                gameView.gameExit();
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        gameView.gamePause();
        openOptionsMenu();
    }
    private void updateDataBase(){
        if(!isChange)
            return;
        isChange =false;
        equipmentBean.setSuperShieldAmount(shieldAmount);
        equipmentBean.setSuperUpspeedAmount(upspeedAmount);
        equipmentBean.setSuperLaserAmount(laserAmount);
        equipmentBean.setSuperBombAmount(bombAmount);
        equipmentBean.insert();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateDataBase();
    }
}
