package com.example.omok;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<PlayerScore> playerScores;

    public LeaderboardAdapter(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerScore playerScore = playerScores.get(position);
        holder.playerNameTextView.setText(playerScore.getPlayerName());
        holder.scoreTextView.setText(String.valueOf(playerScore.getScore()));
    }

    @Override
    public int getItemCount() {
        return playerScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView;
        TextView scoreTextView;

        public ViewHolder(View view) {
            super(view);
            playerNameTextView = view.findViewById(R.id.playerNameTextView);
            scoreTextView = view.findViewById(R.id.scoreTextView);
        }
    }
}