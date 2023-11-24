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
        holder.winsTextView.setText(String.valueOf(playerScore.getWins()));
        holder.lossesTextView.setText(String.valueOf(playerScore.getLosses()));
    }

    @Override
    public int getItemCount() {
        return playerScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView;
        TextView winsTextView;
        TextView lossesTextView;

        public ViewHolder(View view) {
            super(view);
            playerNameTextView = view.findViewById(R.id.playerNameTextView);
            winsTextView = view.findViewById(R.id.winsTextView);
            lossesTextView = view.findViewById(R.id.lossesTextView);
        }
    }
}
