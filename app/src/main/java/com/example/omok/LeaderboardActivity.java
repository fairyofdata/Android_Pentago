package com.example.omok;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<PlayerScore> playerScores;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        databaseHelper = new DatabaseHelper(this);
        playerScores = databaseHelper.getAllScores();

        // 인텐트에서 승자 이름 가져오기
        String winnerName = getIntent().getStringExtra("WINNER_NAME");
        String loserName = getIntent().getStringExtra("LOSER_NAME");

        if (winnerName != null) {
            databaseHelper.addWin(winnerName);
        }

        if (loserName != null) {
            databaseHelper.addLoss(loserName);
        }

        // 리사이클러 뷰 설정
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardAdapter = new LeaderboardAdapter(playerScores);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);
    }
}

