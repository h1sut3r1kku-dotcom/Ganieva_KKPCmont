package com.example.ganieva_k;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "autofix.db";
    public static final int DB_VER = 3;
    public static final String T_REQUESTS = "requests";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_REQUESTS + " (" +
                "id TEXT PRIMARY KEY, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL, " +
                "middle_name TEXT, " +
                "phone TEXT NOT NULL, " +
                "model TEXT, " +
                "email TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "comment TEXT, " +
                "category TEXT NOT NULL, " + // TECH / PC / MASTER
                "status TEXT NOT NULL DEFAULT 'IN_PROGRESS', " +
                "created_date TEXT NOT NULL, " +
                "created_time TEXT NOT NULL, " +
                "done_date TEXT, " +
                "done_time TEXT" +
                ")");
        db.execSQL("CREATE INDEX idx_category ON " + T_REQUESTS + "(category)");
        db.execSQL("CREATE INDEX idx_status ON " + T_REQUESTS + "(status)");
        db.execSQL("CREATE INDEX idx_created ON " + T_REQUESTS + "(created_date, created_time)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Просто пересоздаём таблицу при любом обновлении
        db.execSQL("DROP TABLE IF EXISTS " + T_REQUESTS);
        onCreate(db);
    }
}