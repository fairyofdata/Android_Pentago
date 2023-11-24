package com.example.omok;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Leaderboard.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE PlayerScores (id INTEGER PRIMARY KEY AUTOINCREMENT, playerName TEXT, wins INTEGER, losses INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 업그레이드 로직
        // 예시: 기존 테이블을 삭제하고 새 테이블을 생성
        db.execSQL("DROP TABLE IF EXISTS PlayerScores");
        onCreate(db);
    }

    public void addWin(String playerName) {
        addOrUpdatePlayerScore(playerName, true);
    }

    public void addLoss(String playerName) {
        addOrUpdatePlayerScore(playerName, false);
    }

    private void addOrUpdatePlayerScore(String playerName, boolean isWin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String column = isWin ? "wins" : "losses";
        values.put(column, 1); // 1점 추가

        int rowsAffected = db.update("PlayerScores", values, "playerName = ?", new String[]{playerName});
        if (rowsAffected == 0) {
            // 없으면 새로운 행 추가
            values.put("playerName", playerName);
            values.put("wins", isWin ? 1 : 0);
            values.put("losses", isWin ? 0 : 1);
            db.insert("PlayerScores", null, values);
        }
        db.close();
    }

    public List<PlayerScore> getAllScores() {
        List<PlayerScore> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PlayerScores", new String[]{"playerName", "wins", "losses"}, null, null, null, null, "wins DESC, losses ASC");
        if (cursor.moveToFirst()) {
            do {
                String playerName = cursor.getString(cursor.getColumnIndex("playerName"));
                int wins = cursor.getInt(cursor.getColumnIndex("wins"));
                int losses = cursor.getInt(cursor.getColumnIndex("losses"));
                scores.add(new PlayerScore(playerName, wins, losses));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return scores;
    }

}


