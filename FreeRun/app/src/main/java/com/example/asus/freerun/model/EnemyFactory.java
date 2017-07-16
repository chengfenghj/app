package com.example.asus.freerun.model;

import com.example.asus.freerun.enemies.CommonEnemy;
import com.example.asus.freerun.enemies.CrazyEnemy;
import com.example.asus.freerun.enemies.Enemy;
import com.example.asus.freerun.enemies.StrongEnemy;
import com.example.asus.freerun.enemies.TreasureBox;
import com.example.asus.freerun.enemies.WeakEnemy;

/**
 * Created by ASUS on 2017/5/12.
 */

public class EnemyFactory {

    public EnemyFactory(){
    }

    /**
     *
     * @return
     */
    public Enemy getEnemy(int type){
        Enemy enemy =null;
        switch (type){
            case Enemy.common_enemy:
                enemy =new CommonEnemy();
                break;
            case Enemy.strong_enemy:
                enemy =new StrongEnemy();
                break;
            case Enemy.weak_enemy:
                enemy =new WeakEnemy();
                break;
            case Enemy.crazy_enemy:
                enemy =new CrazyEnemy();
                break;
            case Enemy.treasure_box:
                enemy =new TreasureBox();
                break;
            default:
        }
        return enemy;
    }
}
