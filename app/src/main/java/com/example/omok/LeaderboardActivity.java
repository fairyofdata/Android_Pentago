package com.example.omok;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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
        if (winnerName != null) {
            // 데이터베이스에 승자 점수 추가 또는 업데이트
            databaseHelper.addOrUpdatePlayerScore(winnerName); // 점수를 1로 가정
            // 데이터베이스에서 최신 데이터 가져오기
            playerScores = databaseHelper.getAllScores();
        }

        // 리사이클러 뷰 설정
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardAdapter = new LeaderboardAdapter(playerScores);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);
    }
}
