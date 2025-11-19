package com.example.ganieva_k;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.util.*;

public class RequestRepo {
    private final DbHelper helper;

    public RequestRepo(Context context) {
        this.helper = new DbHelper(context.getApplicationContext());
    }

    public void createRequest(
            String firstName, String lastName, String middleName,
            String email, String address,
            String comment,
            String date, String time
    ) {
        ContentValues v = new ContentValues();
        v.put("id", UUID.randomUUID().toString());
        v.put("first_name", firstName);
        v.put("last_name", lastName);
        v.put("middle_name", middleName);
        v.put("email", email);
        v.put("address", address);
        v.put("comment", comment);
        v.put("status", "IN_PROGRESS");
        v.put("created_date", date);
        v.put("created_time", time);
        helper.getWritableDatabase().insert(DbHelper.T_REQUESTS, null, v);
    }

    public List<ContentValues> getAllRequests() {
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor c = db.query(DbHelper.T_REQUESTS, null, null, null, null, null, "created_date DESC, created_time DESC")) {
            List<ContentValues> list = new ArrayList<>();
            while (c.moveToNext()) {
                ContentValues v = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(c, v);
                list.add(v);
            }
            return list;
        }
    }
}