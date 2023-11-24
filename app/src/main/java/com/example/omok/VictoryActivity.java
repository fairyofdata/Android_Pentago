package com.example.omok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class VictoryActivity extends AppCompatActivity {

    private EditText winnerNameEditText;
    private Button endGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        winnerNameEditText = findViewById(R.id.winnerNameEditText);
        endGameButton = findViewById(R.id.endGameButton);

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 승자의 이름을 가져옵니다
                String winnerName = winnerNameEditText.getText().toString();

                // LeaderboardActivity를 시작하고 승자의 이름을 전달합니다.
                Intent intent = new Intent(VictoryActivity.this, LeaderboardActivity.class);
                intent.putExtra("WINNER_NAME", winnerName);
                startActivity(intent);
            }
        });
    }
}

