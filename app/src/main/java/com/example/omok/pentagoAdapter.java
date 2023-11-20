package com.example.omok;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Arrays;


public class pentagoAdapter extends BaseAdapter {
    private Context mContext;

    private int[][] stones;



    public pentagoAdapter(Context c) {
        mContext = c;

        stones = new int[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                stones[i][j] = Color.GRAY;
            }
        }


    }

    public int getCount() {
        return 36; //
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundColor(stones[position / 6][position % 6]);

        return imageView;
    }

    public void placeStone(int selectedRow, int selectedCol, int currentPlayer) {

        stones[selectedRow][selectedCol] = (currentPlayer == 0) ? Color.BLACK : Color.WHITE;
        notifyDataSetChanged();

    }

    public void placeRotationArea(int areaRow, int areaCol, int [][] rotateState) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (rotateState[i][j] == 0) {
                    stones[areaRow * 3 + i][areaCol * 3 + j] = Color.BLACK;
                } else if (rotateState[i][j] == 1) {
                    stones[areaRow * 3 + i][areaCol * 3 + j] = Color.WHITE;

                } else if (rotateState[i][j] == 10) {
                    stones[areaRow * 3 + i][areaCol * 3 + j] = Color.GRAY;

                }
                notifyDataSetChanged();
            }
        }

    }
}
