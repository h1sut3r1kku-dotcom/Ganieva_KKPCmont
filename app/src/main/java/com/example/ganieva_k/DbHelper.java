package com.example.ganieva_k;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "autofix.db";
    public static final int DB_VER = 1;
    public static final String T_USER = "user";
    public static final String T_REQUESTS = "requests";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db)  {
        // Таблица пользователя (одна запись)
        db.execSQL("CREATE TABLE " + T_USER + " (" +
                "id INTEGER PRIMARY KEY CHECK (id = 1), " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL, " +
                "middle_name TEXT, " +
                "email TEXT NOT NULL, " +
                "address TEXT NOT NULL" +
                ")");

        // Таблица заявок
        db.execSQL("CREATE TABLE " + T_REQUESTS + " (" +
                "id TEXT PRIMARY KEY, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL, " +
                "middle_name TEXT, " +
                "email TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "comment TEXT, " +
                "status TEXT NOT NULL DEFAULT 'IN_PROGRESS', " +
                "created_date TEXT NOT NULL, " +
                "created_time TEXT NOT NULL, " +
                "done_date TEXT, " +
                "done_time TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_USER);
        db.execSQL("DROP TABLE IF EXISTS " + T_REQUESTS);
        onCreate(db);
    }
}