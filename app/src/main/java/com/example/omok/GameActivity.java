package com.example.omok;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GridView gridView;

    private pentagoAdapter adapter;
    private int currentPlayer = 0; // 0은 흑, 1은 백
    private int selectedRow, selectedCol;
    private int position;
    private Button placeCompleteButton,onRotationSensorButton,offRotationSensorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        adapter = new pentagoAdapter(this, gridView);

        placeCompleteButton = findViewById(R.id.placeCompleteButton);
        onRotationSensorButton = findViewById(R.id.onRotationSensorButton);
        offRotationSensorButton = findViewById(R.id.offRotationSensorButton);

        placeCompleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                match();

            }
        });

        onRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        offRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });




        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setClickPosition(position);

            }
        });



    }

    private void setClickPosition(int position) {

        selectedRow = position / 6;
        selectedCol = position % 6;
        this.position = position;

    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
    }

    private void match() {
        adapter.placeStone(position, currentPlayer);
        togglePlayer();

    }



}

