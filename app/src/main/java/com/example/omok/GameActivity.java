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
    private int rotationDirection = 0; // Rotation 변수



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rotation = new Rotation(this);
        rotation.registerGyroscope();

        adapter = new pentagoAdapter(this); // gridView를 관리하는 Adapter과 현 Activity를 연결
        ActivityLayout = findViewById(R.id.linearLayout); // activity_game.xml의 LinearLayout id와 연결
        placeStoneButton = findViewById(R.id.placeStoneButton); // 위와 동일 위치의 Button id와 연결
        gridView = findViewById(R.id.gridView);

        resetBoardState(); // 보드 판을 초기화 시키는 메서드

        selectedRow = -1; // 첫 클릭을 반드시 실행하기 위해서 -1로 설정
        selectedCol = -1;

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

                if (selectedRow == -1 || selectedCol == -1) { // 아무것도 선택하지 않고 Place Stone을 했을 경우
                    Toast.makeText(GameActivity.this, "첫 좌표를 선택하세요.", Toast.LENGTH_SHORT).show();
                }


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

    private void addRotationButtons() { // 동적으로 버튼 추가하는 메서드

        onRotationSensorButton = new Button(this); // 버튼 생성
        offRotationSensorButton = new Button(this);

        onRotationSensorButton.setText("Rotation Sensor"); // 버튼 text 생성
        offRotationSensorButton.setText("Off Rotation Sensor");


        onRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rotationDirection = rotation.getRotationDirection();

                if (rotationDirection != 0) {
                    if (rotationDirection == 1) {
                        rotateRight90InGame();
                    } else if (rotationDirection == -1) {
                        rotateLeft90InGame();
                    }
                    rotation.setEventNeed(false);
                    checkWinnerInGame();
                }  else {
                    Log.d("Rotation", "Rotation Direction == 0");
                    Toast.makeText(GameActivity.this, "No direction Selected!!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        offRotationSensorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rotation.setEventNeed(false);
                checkWinnerInGame(); // 승자 검사하고
            }
        });

        ActivityLayout.addView(onRotationSensorButton); // 실제 xml 에 동적으로 추가
        //ActivityLayout.addView(offRotationSensorButton);
    }

    private void togglePlayer() { // 플레이어 변경
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
    }

    private void toggleButtonActive() { // 버튼 순서에 따른 안내문 검사를 위한 메서드
        buttonActive = (buttonActive == 0) ? 1 : 0;
    }

    private void resetBoardState() { // 처음에 보드 판 상태를 empty, gray로 초기화 하는 메서드

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                boardState[i][j] = 10;
            }
        }
    }


    private void updateSeletedPosition(int clickedPosition) { // 클릭한 포지션 값을 x, y로 변환하는 메서드

        selectedRow = clickedPosition / 6;
        selectedCol = clickedPosition % 6;
    }

    private void updatedSelectedArea(int selectedRow, int selectedCol) { // 클릭한 포지션 값을 사분면 좌표로 변환하는 메서드
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


    private void placeStoneInGame() { // Place Stone이 액티비티 내에서 실제 값을 변경하는 부분

        updateBoarState(selectedRow, selectedCol, currentPlayer); // boardState 업데이트
        adapter.placeStone(selectedRow, selectedCol, currentPlayer); // Adapter 업데이트
        rotation.setEventNeed(true);
        addRotationButtons(); // 버튼 동적 추가

    }

    private void updateBoarState(int selectedRow, int selectedCol, int currentPlayer) { // 흑, 백에 따라 boardState 값 업데이트
        boardState[selectedRow][selectedCol] = (currentPlayer == 0) ? 0 : 1;
    }


    private void rotateRight90InGame() { // 게임 내에서 오른쪽 90도로 회전시키는 메서드
        rotateStateRight90(); // Quarter State 생성, Rotate State 생성
        //showQuarterState();
        //showRotateState();
        applyChangedState(); // Rotate State 현재 BoardState에 적용

    }

    private void rotateLeft90InGame() { // 게임 내에서 왼쪽 90도로 회전시키는 메서드
        rotateStateLeft90(); // Quarter State 생성, Rotate State 생성
        //showQuarterState();
        //showRotateState();
        applyChangedState(); // Rotate State 현재 BoardState에 적용

    }

    private void rotateStateRight90() { // 90도 회전시켜 rotateState에 값 저장

        updateQuarterState(); // 사분면 좌표에 따라 QuaterState 생성

        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                rotateState[i][j] = quarterBoard[2 - j][i];
            }
        }

    }

    private void rotateStateLeft90() { // 90도 회전시켜 rotateState에 값 저장

        updateQuarterState();  // 사분면 좌표에 따라 QuaterState 생성

        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                rotateState[i][j] = quarterBoard[j][2 - i];
            }
        }

    }

    private void updateQuarterState() {  // 사분면 좌표에 따라 QuaterState 생성

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                quarterBoard[i][j] = boardState[areaRow * 3 + i][areaCol * 3 + j];
            }
        }
        //showQuarterState();
    }

    private void applyChangedState() { // 변경된 State 값 적용

        changedBoardState(); // 현재 boardState에 회전 시킨 값 업데이트
        adapter.placeRotationArea(areaRow, areaCol, rotateState); // Adapter에게 회전된 값을 전달

    }

    private void changedBoardState() { // 현재 boardState에 회전 시킨 값 업데이트
        //showBoardState();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[3 * areaRow + i][3 * areaCol + j] = rotateState[i][j];
            }
        }
        //showChangedBoardState();
    }




    private void checkWinnerInGame() {

        checkWinner(); // 가로 세로 대각선 승자 검사 메서드
        toggleButtonActive(); // 액티브 버튼 전환
        togglePlayer(); // 플레이어 전환
        removeRotationButtons(); // 추가된 동적 버튼 제거

    }



    private void removeRotationButtons() { // 동적으로 추가한 버튼 제거
        ActivityLayout.removeView(onRotationSensorButton);
        // ActivityLayout.removeView(offRotationSensorButton);
        Log.d("Debug", "removeButton() - Buttons removed from layout");
    }

    private void checkWinner() { // 가로 세로 대각선 승자 검출 메서드

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

    private void alertWinner(int winnerPlayer) { // 승자 검출 메서드

        String winnerColor;
        if (winnerPlayer == 0) { // 만약 승자가 0이라면
            winnerColor = "Black";
        } else if (winnerPlayer == 1) {
            winnerColor = "White"; // 만약 승자가 1이라면
        } else {
            return;
        }

        Toast.makeText(this, winnerColor + " is Winner!", Toast.LENGTH_SHORT).show(); // 승자 토스트 메서드 출력
        Log.d("BoardState", winnerColor);

        Intent intent = new Intent(this, MainActivity.class); // 해당 액티비티 종료
        startActivity(intent);
        finish();
    }



    private int checkHorizontalWinner() { // 가로 검사

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

    private int checkVertical() { // 세로 검사
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

    private int checkRightDiagonal() { // 오른쪽 대각선 검사

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

    private int checkLeftDiagonal() { // 왼쪽 대각선 검사

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

    // 실제 값이 잘 변경되었는지를 체크하기 위핸 LogCat 메서드
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
