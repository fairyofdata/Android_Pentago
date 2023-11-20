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

    private int areaRow, areaCol;
    private int[][] boardState = new int[6][6];

    private int[][] quarterBoard = new int[3][3];
    private int[][] rotateState = new int[3][3];
    private Button placeCompleteButton,onRotationSensorButton,offRotationSensorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        adapter = new pentagoAdapter(this);

        placeCompleteButton = findViewById(R.id.placeCompleteButton);
        onRotationSensorButton = findViewById(R.id.onRotationSensorButton);
        offRotationSensorButton = findViewById(R.id.offRotationSensorButton);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boardState[i][j] = 10;
            }
        }

        placeCompleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                match();

            }
        });

        onRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rotateState90Right();
                adapter.placeRotationArea(areaRow, areaCol, rotateState);
                showQuarterState();
                showRotateState();



            }
        });

        offRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rotateState90Left();
                adapter.placeRotationArea(areaRow, areaCol, rotateState);
                showQuarterState();
                showRotateState();

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

    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
    }

    private void match() {
        turn();
        setRotationArea(selectedRow, selectedCol);
        togglePlayer();

    }

    private void turn() {
        adapter.placeStone(selectedRow, selectedCol, currentPlayer);
        updateBoarState(selectedRow, selectedCol, currentPlayer);
    }

    private void updateBoarState(int selectedRow, int selectedCol, int currentPlayer) {
        boardState[selectedRow][selectedCol] = (currentPlayer == 0) ? 0 : 1;
    }

    private void setRotationArea(int selectedRow, int selectedCol) {
        if (selectedRow >= 0 && selectedRow <= 2 && selectedCol >= 0 && selectedCol <= 2) {
            areaRow = 0;
            areaCol = 0;

        } else if (selectedRow >= 3 && selectedRow <= 5 && selectedCol >= 0 && selectedCol <= 2) {
            areaRow = 0;
            areaCol = 1;


        } else if (selectedRow >= 0 && selectedRow <= 2 && selectedCol >= 3 && selectedCol <= 5) {
            areaRow = 1;
            areaCol = 0;

        } else if (selectedRow >= 3 && selectedRow <= 5 && selectedCol >= 3 && selectedCol <= 5) {
            areaRow = 1;
            areaCol = 1;

        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                quarterBoard[i][j] = boardState[areaRow * 3 + i][areaCol * 3 + j];
            }
        }

    }

    private void rotateState90Right() {

        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                rotateState[i][j] = quarterBoard[2 - j][i];
            }
        }

    }

    private void rotateState90Left() {
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                rotateState[i][j] = quarterBoard[j][2 - i];
            }
        }

    }
    
    private void checkWinner() {

    }

    private void showQuarterState() {
        Log.d("QuarterState", "=====================");
        for (int i = 0; i < 3; i++) {
            StringBuilder row = new StringBuilder("| ");
            for (int j = 0; j < 3; j++) {
                row.append(quarterBoard[i][j]).append(" | ");
            }
            Log.d("QuarterState", row.toString());
        }
        Log.d("QuarterState", "=====================");
    }

    private void showRotateState() {
        Log.d("RotateState", "=====================");
        for (int i = 0; i < 3; i++) {
            StringBuilder row = new StringBuilder("| ");
            for (int j = 0; j < 3; j++) {
                row.append(rotateState[i][j]).append(" | ");
            }
            Log.d("RotateState", row.toString());
        }
        Log.d("RotateState", "=====================");

    }
}


