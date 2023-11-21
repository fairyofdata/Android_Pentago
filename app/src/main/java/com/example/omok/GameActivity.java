package com.example.omok;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GridView gridView;

    private pentagoAdapter adapter;
    private int currentPlayer = 0; // 0은 흑, 1은 백
    private int matchFlag = 0;
    private int selectedRow, selectedCol;

    private int areaRow, areaCol;
    private int[][] boardState = new int[6][6];

    private int[][] quarterBoard = new int[3][3];
    private int[][] rotateState = new int[3][3];
    private Button placeCompleteButton;
    private LinearLayout ActivityLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        adapter = new pentagoAdapter(this);
        ActivityLayout = findViewById(R.id.linearLayout);
        placeCompleteButton = findViewById(R.id.placeCompleteButton);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boardState[i][j] = 10;
            }
        }

        placeCompleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (boardState[selectedRow][selectedCol] == 10 && matchFlag == 0) {
                    matchPlaceStone();
                    toggleFlag();
                } else if (matchFlag == 1){
                    Toast.makeText(GameActivity.this, "무조건 4개의 판 중 하나를 돌려야합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "돌이 놓여있습니다. 다른 좌표를 선택하세요.", Toast.LENGTH_SHORT).show();

                }
            }
        });


        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setClickPosition(position);
                setRotationArea(selectedRow, selectedCol);

            }
        });



    }

    private void setClickPosition(int position) {

        selectedRow = position / 6;
        selectedCol = position % 6;

    }

    private void turn() {
        adapter.placeStone(selectedRow, selectedCol, currentPlayer);
        updateBoarState(selectedRow, selectedCol, currentPlayer);
        addButton();
        Log.d("Debug", "turn() - Button added");
    }

    private void addButton() {

        Button onRotationSensorButton = new Button(this);
        Button offRotationSensorButton = new Button(this);

        onRotationSensorButton.setText("On Rotation Sensor");
        offRotationSensorButton.setText("Off Rotation Sensor");


        onRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                rotateState90Right();
                adapter.placeRotationArea(areaRow, areaCol, rotateState);
                showQuarterState();
                showRotateState();
                changedBoardState();
                checkWinner();
                toggleFlag();
                removeButton(onRotationSensorButton, offRotationSensorButton);


            }
        });



        offRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                rotateState90Left();
                adapter.placeRotationArea(areaRow, areaCol, rotateState);
                showQuarterState();
                showRotateState();
                changedBoardState();
                checkWinner();
                toggleFlag();
                removeButton(onRotationSensorButton, offRotationSensorButton);





            }
        });



        ActivityLayout.addView(onRotationSensorButton);
        ActivityLayout.addView(offRotationSensorButton);


    }

    private void removeButton(Button onRotationSensorButton, Button offRotationSensorButton) {
        ActivityLayout.removeView(onRotationSensorButton);
        ActivityLayout.removeView(offRotationSensorButton);
        Log.d("Debug", "removeButton() - Buttons removed from layout");
    }

    private void addOffRotationButton() {


    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
    }

    private void toggleFlag() {
        matchFlag = (matchFlag == 0) ? 1 : 0;
    }

    private void matchPlaceStone() {
        turn();
        togglePlayer();

    }

    private void matchRotateStone() {

    }



    private void updateBoarState(int selectedRow, int selectedCol, int currentPlayer) {
        boardState[selectedRow][selectedCol] = (currentPlayer == 0) ? 0 : 1;
    }

    private void setRotationArea(int selectedRow, int selectedCol) {
        if (selectedRow >= 0 && selectedRow <= 2 && selectedCol >= 0 && selectedCol <= 2) {
            areaRow = 0;
            areaCol = 0;

        } else if (selectedRow >= 0 && selectedRow <= 2 && selectedCol >= 3 && selectedCol <= 5) {
            areaRow = 0;
            areaCol = 1;


        } else if (selectedRow >= 3 && selectedRow <= 5 && selectedCol >= 0 && selectedCol <= 2) {
            areaRow = 1;
            areaCol = 0;

        } else if (selectedRow >= 3 && selectedRow <= 5 && selectedCol >= 3 && selectedCol <= 5) {
            areaRow = 1;
            areaCol = 1;

        }

        Log.d("BoardState", "areaRow, areaCol :" + areaRow + ", " + areaCol );

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                quarterBoard[i][j] = boardState[areaRow * 3 + i][areaCol * 3 + j];
            }
        }
        showQuarterState();

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



        int winnerPlayer = 10;


        try {
            Log.d("BoardState", "First " + winnerPlayer);
            showBoardState();

            winnerPlayer = checkHorizontalWinner();
            Log.d("BoardState", "Second " + winnerPlayer);
            alertWinner(winnerPlayer);


        } catch (Exception e) {
            Log.d("Debug1", "Error horizontal " + e);
        }

        try {
            winnerPlayer = checkVertical();
            alertWinner(winnerPlayer);


        } catch (Exception e) {
            Log.d("Debug1", "Error vertical " + e);
        }

        try {
            winnerPlayer = checkLeftDiagonal();
            alertWinner(winnerPlayer);


        } catch (Exception e) {
            Log.d("Debug1", "Error left diagonal " + e);
        }

        try {

            winnerPlayer = checkRightDiagonal();
            alertWinner(winnerPlayer);

        } catch (Exception e) {
            Log.d("Debug1", "Error right diagonal " + e);
        }


    }



    private void alertWinner(int winnerPlayer) {

        String winnerColor;
        if (winnerPlayer == 0) {
            winnerColor = "Black";
        } else if (winnerPlayer == 1) {
            winnerColor = "White";
        } else {
            return;
        }

        Toast.makeText(this, winnerColor + " is Winner!", Toast.LENGTH_SHORT).show();
        Log.d("BoardState", winnerColor);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private int checkHorizontalWinner() {

        int checkSum = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                checkSum = 0;
                Log.d("BoardState", "iterater i, j:" + i + "," + j);
                for (int k = 0; k < 5; k++) {
                    checkSum += boardState[i][j + k];

                }
                Log.d("BoardState", "checkSum" +  checkSum);
                if (checkSum == 0) {
                    return 0;
                } else if (checkSum == 5) {
                    return 1;
                }
            }
        }
        return 10;

    }

    private int checkVertical() {
        int checkSum = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                checkSum = 0;
                for (int k = 0; k < 5; k++) {
                    checkSum += boardState[j + k][i];
                }
                if (checkSum == 0) {
                    return 0;
                } else if (checkSum == 5) {
                    return 1;
                }
            }
        }
        return 10;

    }

    private int checkRightDiagonal() {

        int checkSum = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                checkSum = 0;
                for (int k = 0; k < 5; k++) {
                    checkSum += boardState[i + k][j + k];


                }
                if (checkSum == 0) {
                    return 0;
                } else if (checkSum == 5) {
                    return 1;
                }
            }
        }

        return 10;

    }

    private int checkLeftDiagonal() {

        int checkSum = 0;

        for (int i = 4; i < 6; i++) {
            for (int j = 4; j < 6; j++) {
                checkSum = 0;
                for (int k = 0; k < 5; k++) {
                    checkSum += boardState[i - k][j - k];
                }
                if (checkSum == 0) {
                    return 0;
                } else if (checkSum == 5) {
                    return 1;
                }
            }
        }

        return 10;

    }

    private void changedBoardState() {
        //showBoardState();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[3 * areaRow + i][3 * areaCol + j] = rotateState[i][j];
            }
        }
        //showChangedBoardState();
    }

    private void showBoardState() {
        Log.d("BoardState", "=====================");
        for (int i = 0; i < 6; i++) {
            StringBuilder row = new StringBuilder("| ");
            for (int j = 0; j < 6; j++) {
                row.append(boardState[i][j]).append(" | ");
            }
            Log.d("BoardState", row.toString());
        }
        Log.d("BoardState", "=====================");


    }

    private void showChangedBoardState() {
        Log.d("ChangedBoardState", "=====================");
        for (int i = 0; i < 6; i++) {
            StringBuilder row = new StringBuilder("| ");
            for (int j = 0; j < 6; j++) {
                row.append(boardState[i][j]).append(" | ");
            }
            Log.d("ChangedBoardState", row.toString());
        }
        Log.d("ChangedBoardState", "=====================");

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


