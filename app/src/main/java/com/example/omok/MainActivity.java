package com.example.omok;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String YOUTUBE_ID = "y0kBs4-DNe8";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        setTitle("펜타고");

        Button tutorialButton = findViewById(R.id.Tutorial);
        tutorialButton.setOnClickListener(this::onClickTutorial);
        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this::onClickGameButton);
        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(this::onClickLeaderboardButton);

        EditText player1EditText = findViewById(R.id.player1);
        EditText player2EditText = findViewById(R.id.player2);
    }

    private void onClickTutorial(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + YOUTUBE_ID));
        intent.putExtra("VIDEO_ID", YOUTUBE_ID);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + YOUTUBE_ID));
            startActivity(intent);
        }
    }

    private void onClickGameButton(View view) {
        EditText player1EditText = findViewById(R.id.player1);
        EditText player2EditText = findViewById(R.id.player2);

        String player1Name = player1EditText.getText().toString().trim();
        String player2Name = player2EditText.getText().toString().trim();

        // 입력값이 비어있는지 확인
        if (TextUtils.isEmpty(player1Name) || TextUtils.isEmpty(player2Name)) {
            // 플레이어 이름을 모두 입력하도록 메시지 표시
            Toast.makeText(this, "플레이어 이름을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 입력값이 있는 경우 GameActivity로 전달
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("PLAYER1_NAME", player1Name);
            intent.putExtra("PLAYER2_NAME", player2Name);
            Log.d("MainActivity", "Player1_NAME : " + player1Name);
            Log.d("MainActivity", "Player2_NAME : " + player2Name);
            startActivity(intent);
        }
    }
    private void onClickLeaderboardButton(View view) {
        Intent intent = new Intent(getApplicationContext(), LeaderboardActivity.class);
        startActivity(intent);
    }

}