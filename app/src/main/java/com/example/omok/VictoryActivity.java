package com.example.omok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class VictoryActivity extends AppCompatActivity {

    private EditText winnerNameEditText, loserNameEditText;
    private Button endGameButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        winnerNameEditText = findViewById(R.id.winnerNameEditText);
        loserNameEditText = findViewById(R.id.loserNameEditText);
        endGameButton = findViewById(R.id.endGameButton);

        databaseHelper = new DatabaseHelper(this);

        // 인텐트에서 승자와 패자 이름 가져오기
        Intent intent = getIntent();
        String winnerName = intent.getStringExtra("WINNER_NAME");
        String loserName = intent.getStringExtra("LOSER_NAME");

        if (winnerName != null) {
            databaseHelper.addWin(winnerName);
        }
        if (loserName != null) {
            databaseHelper.addLoss(loserName);
        }

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 승자와 패자의 이름을 가져옵니다
                String winnerName = winnerNameEditText.getText().toString();
                String loserName = loserNameEditText.getText().toString();

                // 데이터베이스에 승자와 패자 기록
                databaseHelper.addWin(winnerName);
                databaseHelper.addLoss(loserName);

                // LeaderboardActivity를 시작하고 승자의 이름을 전달합니다.
                Intent intent = new Intent(VictoryActivity.this, LeaderboardActivity.class);
                intent.putExtra("WINNER_NAME", winnerName);
                intent.putExtra("LOSER_NAME", loserName);
                startActivity(intent);
            }
        });
    }
}
