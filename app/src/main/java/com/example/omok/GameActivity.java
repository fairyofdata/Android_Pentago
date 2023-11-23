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
    private int buttonActive = 0; // Place Complete 버튼을 활성화 시키는 변수, 0: 활성화, 1: 비활성화
    private int selectedRow, selectedCol; // 사용자가 선택한 좌표

    private int areaRow, areaCol; // 사용자가 돌릴 4개의 영역 좌표
    private int[][] boardState = new int[6][6]; // 숫자로 동작하는 실제 6X6 보드판, 0: Black, 1: White, 10: Gray (empty)

    private int[][] quarterBoard = new int[3][3]; // 사용자가 돌릴 3X3 테이블, boardState의 4분의 1
    private int[][] rotateState = new int[3][3]; // 사용자가 돌린 3X3 테이블

    private Button placeStoneButton; // 돌의 위치를 확정 짓는 버튼
    private Button onRotationSensorButton;
    private Button offRotationSensorButton;
    private LinearLayout ActivityLayout; // 동적으로 버튼을 생성해 xml과 연결하기 위해 필요한 선언

    private Rotation rotation;      // Rotation.java 선언



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        adapter = new pentagoAdapter(this); // gridView를 관리하는 Adapter과 현 Activity를 연결
        ActivityLayout = findViewById(R.id.linearLayout); // activity_game.xml의 LinearLayout id와 연결
        placeStoneButton = findViewById(R.id.placeStoneButton); // 위와 동일 위치의 Button id와 연결
        gridView = findViewById(R.id.gridView);

        resetBoardState(); // 보드 판을 초기화 시키는 메서드


        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // 6X6 보드 판을 클릭할 시

                updateSeletedPosition(position); // 현재 좌표를 변수에 저장
                updatedSelectedArea(selectedRow, selectedCol); // 현재 회전 시킬 영역을 선택

            }
        });

        placeStoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (boardState[selectedRow][selectedCol] == 10 && buttonActive == 0) { // 선택한 위치가 empty일 경우, buttonActivie가 0일 경우 실행
                    placeStoneInGame();
                    toggleButtonActive();
                } else if (buttonActive == 1){ // Place Complete 버튼을 누른 뒤, Rotation을 실행 하지 않은 경우
                    Toast.makeText(GameActivity.this, "반드시 보드의 4영역 중 한 영역을 회전 시켜야합니다.", Toast.LENGTH_SHORT).show();
                } else { // boardState가 empty가 아닐 경우
                    Toast.makeText(GameActivity.this, "빈 좌표가 아닙니다. 다른 좌표를 선택하세요.", Toast.LENGTH_SHORT).show();

                }
            }
        });




    }

    private void updateSeletedPosition(int clickedPosition) {

        selectedRow = clickedPosition / 6;
        selectedCol = clickedPosition % 6;

    }


    private void addRotationButtons() {

        onRotationSensorButton = new Button(this);
        offRotationSensorButton = new Button(this);

        onRotationSensorButton.setText("On Rotation Sensor");
        offRotationSensorButton.setText("Off Rotation Sensor");


        onRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rotateRight90InGame();
                checkWinnerInGame();


            }
        });



        offRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rotateLeft90InGame();
                checkWinnerInGame();

            }
        });



        ActivityLayout.addView(onRotationSensorButton);
        ActivityLayout.addView(offRotationSensorButton);


    }

    private void resetBoardState() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boardState[i][j] = 10;
            }
        }

    }

    private void rotateRight90InGame() {
        rotateState90Right();
        //showQuarterState();
        //showRotateState();
        applyChangedState();

    }

    private void rotateLeft90InGame() {
        rotateState90Left();
        //showQuarterState();
        //showRotateState();
        applyChangedState();

    }

    private void checkWinnerInGame() {
        checkWinner();
        toggleButtonActive();
        togglePlayer();
        removeRotationButtons();

    }

    private void applyChangedState() {
        adapter.placeRotationArea(areaRow, areaCol, rotateState);
        changedBoardState();

    }


    private void removeRotationButtons() {
        ActivityLayout.removeView(onRotationSensorButton);
        ActivityLayout.removeView(offRotationSensorButton);
        Log.d("Debug", "removeButton() - Buttons removed from layout");
    }


    private void togglePlayer() {
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
    }

    private void toggleButtonActive() {
        buttonActive = (buttonActive == 0) ? 1 : 0;
    }

    private void placeStoneInGame() {
        adapter.placeStone(selectedRow, selectedCol, currentPlayer);
        updateBoarState(selectedRow, selectedCol, currentPlayer);
        addRotationButtons();

    }

    private void updateQuarterState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                quarterBoard[i][j] = boardState[areaRow * 3 + i][areaCol * 3 + j];
            }
        }
        showQuarterState();
    }




    private void updateBoarState(int selectedRow, int selectedCol, int currentPlayer) {
        boardState[selectedRow][selectedCol] = (currentPlayer == 0) ? 0 : 1;
    }

    private void updatedSelectedArea(int selectedRow, int selectedCol) {
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



    }

    private void rotateState90Right() {

        updateQuarterState();

        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                rotateState[i][j] = quarterBoard[2 - j][i];
            }
        }

    }

    private void rotateState90Left() {

        updateQuarterState();

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

        for (int i = 0; i < 2; i++) {
            for (int j = 4; j < 6; j++) {
                checkSum = 0;
                for (int k = 0; k < 5; k++) {
                    checkSum += boardState[i + k][j - k];
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
