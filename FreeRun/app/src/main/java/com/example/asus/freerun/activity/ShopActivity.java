package com.example.asus.freerun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.freerun.R;
import com.example.asus.freerun.data.EquipmentBean;
import com.example.asus.freerun.data.MaterialsBean;

/**
 * Created by ASUS on 2017/2/12.
 */
public class ShopActivity extends Activity {

    private TextView coinsAmount;
    private TextView superShieldAmount;
    private TextView superUpspeedAmount;
    private TextView superLaserAmount;
    private TextView superBombAmount;
    private Button back;
    private Button buyShield;
    private Button buyUpspeed;
    private Button buyLaser;
    private Button buyBomb;

    private MaterialsBean materialsBean;
    private EquipmentBean equipmentBean;
    private int coins;
    private int shieldAmount;
    private int upspeedAmount;
    private int laserAmount;
    private int bombAmount;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_shop);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        coinsAmount =(TextView)findViewById(R.id.coin_amount);
        superShieldAmount =(TextView)findViewById(R.id.shield_amount);
        superUpspeedAmount =(TextView)findViewById(R.id.upspeed_amount);
        superLaserAmount =(TextView)findViewById(R.id.laser_amount);
        superBombAmount =(TextView)findViewById(R.id.bomb_amount);
        back =(Button)findViewById(R.id.back);
        buyShield =(Button)findViewById(R.id.shield_buy);
        buyUpspeed =(Button)findViewById(R.id.upspeed_buy);
        buyLaser =(Button)findViewById(R.id.laser_buy);
        buyBomb =(Button)findViewById(R.id.bomb_buy);

        materialsBean =new MaterialsBean(this);
        equipmentBean =new EquipmentBean(this);

        initValue();
        setTexts();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buyShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coins < 200)
                    return;
                shieldAmount ++;
                coins -=200;
                setTexts();
            }
        });

        buyUpspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coins < 200)
                    return;
                upspeedAmount ++;
                coins -=200;
                setTexts();
            }
        });

        buyLaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coins < 200)
                    return;
                laserAmount ++;
                coins -=200;
                setTexts();
            }
        });

        buyBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coins < 200)
                    return;
                bombAmount ++;
                coins -=200;
                setTexts();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        materialsBean.setGolds(coins);
        materialsBean.insert();
        equipmentBean.setSuperShieldAmount(shieldAmount);
        equipmentBean.setSuperUpspeedAmount(upspeedAmount);
        equipmentBean.setSuperLaserAmount(laserAmount);
        equipmentBean.setSuperBombAmount(bombAmount);
        equipmentBean.insert();
    }

    private void initValue(){
        materialsBean.query();
        equipmentBean.query();
        coins =materialsBean.getGolds();
        shieldAmount =equipmentBean.getSuperShieldAmount();
        upspeedAmount =equipmentBean.getSuperUpspeedAmount();
        laserAmount =equipmentBean.getSuperLaserAmount();
        bombAmount =equipmentBean.getSuperBombAmount();
    }
    private void setTexts(){
        coinsAmount.setText("金币： "+coins);
        superShieldAmount.setText("拥有: "+shieldAmount);
        superUpspeedAmount.setText("拥有: "+upspeedAmount);
        superLaserAmount.setText("拥有: "+laserAmount);
        superBombAmount.setText("拥有: "+bombAmount);
    }
}
