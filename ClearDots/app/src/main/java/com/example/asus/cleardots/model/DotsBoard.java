package com.example.asus.cleardots.model;

import java.util.List;

/**
 * Created by ASUS on 2017/1/24.
 */
public class DotsBoard {

    private final int LENGHT =10;

    private int[][] board;         //用来记录点的二维数组
    private int dif;            //难度，最多有多少种颜色的点
    private int maxScore;       //用来记录该难度下对应的最大分数
    private int score;         //用来记录分数
    private boolean gameover;     //标记游戏是否结束
    private Dot remindDot;

    public DotsBoard(){
        board =new int[LENGHT][LENGHT];
        remindDot =new Dot(0,0);
        dif =4;
        init();
    }

    /**
     * 初始化数组，随机产生难度为4的点矩阵
     */
    public void init(){
        gameover =false;
        maxScore =16;
        score =0;
        dif =4;
        for(int i =0;i<LENGHT;i++){
            for(int j=0;j<LENGHT;j++){
                board[i][j] = generateDot(dif);
            }
        }
    }

    /**
     * 吃掉那些被选中的点
     * @param dots     选中的点序列
     */
    public void eat(List<Dot> dots){
        for(int i = 0; i< dots.size(); i++){
            setValue(dots.get(i).x, dots.get(i).y,0);
        }
        score += dots.size();
        if(score >= maxScore){
            dif++;
            maxScore *=4;
        }
    }

    /**
     * 将点往下落一个格子
     */
    public void fall(){
        boolean fall;
        for(int i =LENGHT-1;i>=0;i--){
            fall =false;
            for(int j = LENGHT-1;j>=0;j--){
                if(board[i][j] == 0)
                    fall =true;
                if(fall == true && j>0) {
                    board[i][j] = board[i][j - 1];
                    board[i][j-1] =0;
                }
            }
            if(board[i][0] == 0) {
                board[i][0] = generateDot(dif);
            }
        }
    }

    /**
     * 判断游戏是否结束
     */
    public void gameover(){
        int value;
        int num;
        gameover =true;
        for(int i =0;i<LENGHT;i++){
            for(int j =0;j<LENGHT;j++){
                num =0;
                value =board[i][j];
                if(isSameColor(i,j-1,value))
                    num++;
                if(isSameColor(i,j+1,value))
                    num++;
                if(isSameColor(i-1,j,value))
                    num++;
                if(isSameColor(i+1,j,value))
                    num++;
                if(num >= 2) {
                    gameover =false;
                    remindDot.x =i;
                    remindDot.y =j;
                    return;
                }
            }
        }
    }

    /**
     * 判断指定位置是否是指定的颜色
     * @param x
     * @param y
     * @param color          传入衍颜色
     * @return       如果是返回true，否则返回false
     */
    private boolean isSameColor(int x,int y,int color){
        if(x<0 || y<0 || x>=LENGHT || y>=LENGHT)
            return false;
        if(board[x][y] == color)
            return true;
        return false;
    }

    /**
     * 获取提示的点
     * @return
     */
    public Dot getRemindDot(){
        return remindDot;
    }

    /**
     * 用来获取数组中指定坐标的点的值
     * @param x     数组的横坐标
     * @param y     数组的纵坐标
     */
    public int getValue(int x,int y){
        return board[x][y];
    }
    /**
     * 用来设置数组中指定坐标的点的值
     * @param x     数组的横坐标
     * @param y     数组的纵坐标
     * @return
     */
    public void setValue(int x,int y,int value){
        board[x][y] =value;
    }

    /**
     * 用来获取数组的宽度
     * @return
     */
    public int getLENGHT(){
        return LENGHT;
    }
    public boolean getGameover(){
        return gameover;
    }

    /**
     * 用来获取成绩
     * @return
     */
    public int getScore(){
        return score;
    }
    /**
     * 用来随机产生一个点。
     * @param difficulty   难度，数值越大，产生的点可能性越多
     * @return        返回所产生的点，范围在1~difficulty之间
     */
    private int generateDot(int difficulty){
        int dot;
        dot =(int)(Math.random()*difficulty)+1;
        return dot;
    }

}
