package com.example.omok;


public class PlayerScore {
    private String playerName;
    private int wins;
    private int losses;

    // 생성자
    public PlayerScore(String playerName, int wins, int losses) {
        this.playerName = playerName;
        this.wins = wins;
        this.losses = losses;
    }

    // playerName의 getter
    public String getPlayerName() {
        return playerName;
    }

    // playerName의 setter
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    // wins의 getter
    public int getWins() {
        return wins;
    }

    // wins의 setter
    public void setWins(int wins) {
        this.wins = wins;
    }

    // losses의 getter
    public int getLosses() {
        return losses;
    }

    // losses의 setter
    public void setLosses(int losses) {
        this.losses = losses;
    }
}

