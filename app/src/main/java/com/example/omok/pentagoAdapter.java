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
    private GridView mGridView;
    private int[] stones;



    public pentagoAdapter(Context c, GridView gridView) {
        mContext = c;
        mGridView = gridView;
        stones = new int[getCount()];
        Arrays.fill(stones, Color.GRAY);


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

        imageView.setBackgroundColor(stones[position]);

        return imageView;
    }

    public void placeStone(int position, int currentPlayer) {


     /*   if (currentPlayer == 0) {
            imageView.setBackgroundColor(Color.BLACK);
        } else if (currentPlayer == 1) {
            imageView.setBackgroundColor(Color.WHITE);
        }
        notifyDataSetChanged();
    }*/

        stones[position] = (currentPlayer == 0) ? Color.BLACK : Color.WHITE;
        notifyDataSetChanged();

    }

    public void placeRotationArea() {

    }
}
