package com.example.asus.cleardots.model;

import android.content.Context;

import com.example.asus.cleardots.R;

/**
 * Created by ASUS on 2017/1/24.
 */
public class ColorLibrary {

    private int[] colors =new int[22];

    public ColorLibrary(Context context){
        loadColor(context);
    }
    private void loadColor(Context context){
        colors[0] =context.getResources().getColor(R.color.color0);
        colors[1] =context.getResources().getColor(R.color.color1);
        colors[2] =context.getResources().getColor(R.color.color2);
        colors[3] =context.getResources().getColor(R.color.color3);
        colors[4] =context.getResources().getColor(R.color.color4);
        colors[5] =context.getResources().getColor(R.color.color5);
        colors[6] =context.getResources().getColor(R.color.color6);
        colors[7] =context.getResources().getColor(R.color.color7);
        colors[8] =context.getResources().getColor(R.color.color8);
        colors[9] =context.getResources().getColor(R.color.color9);
        colors[10] =context.getResources().getColor(R.color.color10);
        colors[11] =context.getResources().getColor(R.color.background);
        colors[12] =context.getResources().getColor(R.color.rank1);
        colors[13] =context.getResources().getColor(R.color.rank2);
        colors[14] =context.getResources().getColor(R.color.rank3);
        colors[15] =context.getResources().getColor(R.color.rank4);
        colors[16] =context.getResources().getColor(R.color.rank4);
        colors[17] =context.getResources().getColor(R.color.rank4);
        colors[18] =context.getResources().getColor(R.color.rank4);
        colors[19] =context.getResources().getColor(R.color.rank4);
        colors[20] =context.getResources().getColor(R.color.rank4);
        colors[21] =context.getResources().getColor(R.color.rank4);
    }
    public int getColor(int index){
        return colors[index];
    }

}
