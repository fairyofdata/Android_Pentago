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
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE PlayerScores (id INTEGER PRIMARY KEY AUTOINCREMENT, playerName TEXT, score INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 업그레이드 로직
        // 예시: 기존 테이블을 삭제하고 새 테이블을 생성
        db.execSQL("DROP TABLE IF EXISTS PlayerScores");
        onCreate(db);
    }

    public void addOrUpdatePlayerScore(String playerName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 현재 점수를 조회합니다.
        Cursor cursor = db.query("PlayerScores", new String[]{"score"}, "playerName = ?", new String[]{playerName}, null, null, null);
        int currentScore = 0;
        if (cursor.moveToFirst()) {
            currentScore = cursor.getInt(cursor.getColumnIndex("score"));
        }
        cursor.close();

        // 새로운 점수 값
        int newScore = currentScore + 1;

        ContentValues values = new ContentValues();
        values.put("playerName", playerName);
        values.put("score", newScore);

        // 업데이트를 시도하고, 결과가 0이면 새로운 행 추가
        int rows = db.update("PlayerScores", values, "playerName = ?", new String[]{playerName});
        if (rows == 0) {
            db.insert("PlayerScores", null, values);
        }
        db.close();
    }

    public List<PlayerScore> getAllScores() {
        List<PlayerScore> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("PlayerScores", new String[]{"playerName", "score"}, null, null, null, null, "score DESC");
        if (cursor.moveToFirst()) {
            do {
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow("playerName"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                scores.add(new PlayerScore(playerName, score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return scores;
    }
}


