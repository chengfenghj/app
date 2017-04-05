package com.example.asus.freerun.activity;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.freerun.R;
import com.example.asus.freerun.data.InitiateBean;
import com.example.asus.freerun.data.MaterialsBean;
import com.example.asus.freerun.view.GradeView;

/**
 * Created by ASUS on 2017/2/7.
 */
public class UpgradeActivity extends Activity {

    private TextView coinView;
    private GradeView energyGrade;
    private GradeView shieldGrade;
    private GradeView bulletGrade;
    private TextView energySpend;
    private TextView shieldSpend;
    private TextView bulletSpend;
    private Button energyUpgrade;
    private Button shieldUpgrade;
    private Button bulletUpgrade;
    private Button back;

    private InitiateBean initiateBean;
    private MaterialsBean materialsBean;
    private int initEnergy;                  //初始能量
    private int initShield;                  //护盾时间
    private int initBullet;                  //子弹消耗
    private int coins;                        //金币数量

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_upgrade);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        coinView =(TextView)findViewById(R.id.coins);
        energyGrade =(GradeView)findViewById(R.id.energyGrade);
        shieldGrade =(GradeView)findViewById(R.id.shieldGrade);
        bulletGrade =(GradeView)findViewById(R.id.bulletGrade);
        energySpend =(TextView)findViewById(R.id.energySpend);
        shieldSpend =(TextView)findViewById(R.id.shieldSpend);
        bulletSpend =(TextView)findViewById(R.id.bulletSpend);
        energyUpgrade =(Button)findViewById(R.id.energyButton);
        shieldUpgrade =(Button)findViewById(R.id.shieldButton);
        bulletUpgrade =(Button)findViewById(R.id.bulletButton);
        back =(Button)findViewById(R.id.back1);

        initiateBean =new InitiateBean(this);
        materialsBean =new MaterialsBean(this);

        initiateBean.query();
        initEnergy =initiateBean.getInitiateEnerge();
        initShield =initiateBean.getShieldTime();
        initBullet =initiateBean.getBulletEnerge();
        materialsBean.query();
        coins =materialsBean.getGolds();

        Log.d("UpgradeActivity","initEnergy="+initEnergy);
        Log.d("UpgradeActivity","initShield="+initShield);
        Log.d("UpgradeActivity","initBullet="+initBullet);

        changeText();
        changeGrade();

        //监听能量升级按钮
        energyUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果钱不够，不发生动作
                if (coins < getEnergySpend()) {
                    Toast.makeText(UpgradeActivity.this, "金币不足！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果等级满了，不发生动作
                if (getEnergyGrade() == 8) {
                    Toast.makeText(UpgradeActivity.this, "等级已满！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果等级没满

                //改变coinView的值
                coins -= getEnergySpend();
                coinView.setText("金币： " + coins);
                //改变initEnerg的值
                energyUpgrade();
                //改变energySpend的值
                energySpend.setText("花费金币：" + getEnergySpend());
                //改变enerGrade的值
                energyGrade.setGrade(getEnergyGrade());
                energyGrade.invalidate();
                energyGrade.draw(new Canvas());
                //更新数据库
                updateDataBase();
            }
        });

        //监听护盾升级按钮
        shieldUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果钱不够，不发生动作
                if(coins < getShieldSpend()) {
                    Toast.makeText(UpgradeActivity.this,"金币不足！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果等级满了，不发生动作
                if(getShieldGrade() == 8) {
                    Toast.makeText(UpgradeActivity.this,"等级已满！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果等级没满

                //改变coinView的值
                coins -=getShieldSpend();
                coinView.setText("金币： "+coins);
                //改变initShield的值
                shieldUpgrade();
                //改变shieldSpend的值
                shieldSpend.setText("花费金币："+getShieldSpend());
                //改变shieldGrade的值
                shieldGrade.setGrade(getShieldGrade());
                shieldGrade.invalidate();
                shieldGrade.draw(new Canvas());
                //更新数据库
                updateDataBase();
            }
        });

        //监听子弹升级按钮
        bulletUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果钱不够，不发生动作
                if(coins < getBulletSpend()) {
                    Toast.makeText(UpgradeActivity.this,"金币不足！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果等级满了，不发生动作
                if(getBulletGrade() == 8) {
                    Toast.makeText(UpgradeActivity.this,"等级已满！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果等级没满

                //改变coinView的值
                coins -=getBulletSpend();
                coinView.setText("金币： "+coins);
                //改变initShield的值
                bulletUpgrade();
                //改变shieldSpend的值
                bulletSpend.setText("花费金币："+getBulletSpend());
                //改变shieldGrade的值
                bulletGrade.setGrade(getBulletGrade());
                bulletGrade.invalidate();
                bulletGrade.draw(new Canvas());
                //更新数据库
                updateDataBase();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("UpgradeActivity","back...");
        finish();
    }

    /**
     * 改变textView显示的值
     */
    private void changeText(){
        coinView.setText("金币： "+coins);
        energySpend.setText("花费金币："+getEnergySpend());
        shieldSpend.setText("花费金币："+getShieldSpend());
        bulletSpend.setText("花费金币："+getBulletSpend());
    }

    /**
     * 改变GradeView显示的值
     */
    private void changeGrade(){
        energyGrade.setGrade(getEnergyGrade());
        energyGrade.invalidate();
        energyGrade.draw(new Canvas());

        shieldGrade.setGrade(getShieldGrade());
        shieldGrade.invalidate();
        shieldGrade.draw(new Canvas());

        bulletGrade.setGrade(getBulletGrade());
        bulletGrade.invalidate();
        bulletGrade.draw(new Canvas());
    }

    /**
     * 当有升级动作时及时更改数据库
     */
    private void updateDataBase(){
        initiateBean.setInitiateEnerge(initEnergy);
        initiateBean.setShieldTime(initShield);
        initiateBean.setBulletEnerge(initBullet);
        initiateBean.insert();
        materialsBean.setGolds(coins);
        materialsBean.insert();
    }

    /**
     * 计算能量升级所花费的金币数
     * @return     返回金币数
     */
    private int getEnergySpend(){
        switch (initEnergy){
            case 200:
                return 100;
            case 250:
                return 200;
            case 300:
                return 400;
            case 350:
                return 800;
            case 400:
                return 1600;
            case 450:
                return 3200;
            case 500:
                return 6400;
            case 600:
                return 12800;
            case 800:
                return 25000;
            default:
        }
        return 0;
    }

    /**
     * 计算护盾升级所需要的金币数
     * @return     返回金币数
     */
    private int getShieldSpend(){
        switch (initShield){
            case 20:
                return 100;
            case 25:
                return 200;
            case 30:
                return 400;
            case 35:
                return 800;
            case 40:
                return 1600;
            case 45:
                return 3200;
            case 50:
                return 6400;
            case 55:
                return 12800;
            case 60:
                return 25000;
            default:
        }
        return 0;
    }

    /**
     * 计算子弹升级所需的金币数
     * @return      返回金币数
     */
    private int getBulletSpend(){
        switch (initBullet){
            case 100:
                return 100;
            case 95:
                return 200;
            case 90:
                return 400;
            case 85:
                return 800;
            case 80:
                return 1600;
            case 75:
                return 3200;
            case 70:
                return 6400;
            case 65:
                return 12800;
            case 60:
                return 25000;
            default:
        }
        return 0;
    }
    /**
     * 计算当前能量的等级
     * @return     返回等级数
     */
    private int getEnergyGrade(){
        switch (initEnergy){
            case 200:
                return 1;
            case 250:
                return 2;
            case 300:
                return 3;
            case 350:
                return 4;
            case 400:
                return 5;
            case 450:
                return 6;
            case 500:
                return 7;
            case 600:
                return 8;
            case 800:
                return 9;
            case 1000:
                return 10;
            default:
        }
        return 0;
    }
    /**
     * 计算当前护盾的等级
     * @return     返回等级数
     */
    private int getShieldGrade(){
        switch (initShield){
            case 20:
                return 1;
            case 25:
                return 2;
            case 30:
                return 3;
            case 35:
                return 4;
            case 40:
                return 5;
            case 45:
                return 6;
            case 50:
                return 7;
            case 55:
                return 8;
            case 60:
                return 9;
            case 70:
                return 10;
            default:
        }
        return 0;
    }
    /**
     * 计算当前子弹的等级数
     * @return      返回等级数
     */
    private int getBulletGrade(){
        switch (initBullet){
            case 100:
                return 1;
            case 95:
                return 2;
            case 90:
                return 3;
            case 85:
                return 4;
            case 80:
                return 5;
            case 75:
                return 6;
            case 70:
                return 7;
            case 65:
                return 8;
            case 60:
                return 9;
            case 50:
                return 10;
            default:
        }
        return 0;
    }
    /**
     * 能量升级
     */
    private void energyUpgrade(){
        switch (initEnergy){
            case 200:
                initEnergy = 250;
                break;
            case 250:
                initEnergy = 300;
                break;
            case 300:
                initEnergy = 350;
                break;
            case 350:
                initEnergy = 400;
                break;
            case 400:
                initEnergy = 450;
                break;
            case 450:
                initEnergy = 500;
                break;
            case 500:
                initEnergy = 600;
                break;
            case 600:
                initEnergy = 800;
                break;
            case 800:
                initEnergy = 1000;
                break;
            default:
        }
    }
    /**
     * 护盾升级
     */
    private void  shieldUpgrade(){
        switch (initShield){
            case 20:
                initShield = 25;
                break;
            case 25:
                initShield = 30;
                break;
            case 30:
                initShield = 35;
                break;
            case 35:
                initShield = 40;
                break;
            case 40:
                initShield = 45;
                break;
            case 45:
                initShield = 50;
                break;
            case 50:
                initShield = 55;
                break;
            case 55:
                initShield = 60;
                break;
            case 60:
                initShield = 70;
            default:
        }
    }

    /**
     * 子弹升级
     */
    private void bulletUpgrade(){
        switch (initBullet){
            case 100:
                initBullet = 95;
                break;
            case 95:
                initBullet = 90;
                break;
            case 90:
                initBullet = 85;
                break;
            case 85:
                initBullet = 80;
                break;
            case 80:
                initBullet = 75;
                break;
            case 75:
                initBullet = 70;
                break;
            case 70:
                initBullet = 65;
                break;
            case 65:
                initBullet = 60;
                break;
            case 60:
                initBullet = 50;
                break;
            default:
        }
    }
}
