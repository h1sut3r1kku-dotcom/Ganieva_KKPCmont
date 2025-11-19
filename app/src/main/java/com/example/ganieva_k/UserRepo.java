package com.example.ganieva_k;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class UserRepo {
    private final DbHelper helper;

    public UserRepo(Context context) {
        this.helper = new DbHelper(context.getApplicationContext());
    }

    public ContentValues getProfile() {
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor c = db.query(DbHelper.T_USER, null, "id=1", null, null, null, null)) {
            if (c.moveToFirst()) {
                ContentValues v = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(c, v);
                return v;
            }
            return null;
        }
    }

    public void saveProfile(String firstName, String lastName, String middleName, String email, String address) {
        ContentValues v = new ContentValues();
        v.put("first_name", firstName);
        v.put("last_name", lastName);
        v.put("middle_name", middleName);
        v.put("email", email);
        v.put("address", address);

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(DbHelper.T_USER, null, "id=1", null, null, null, null);
        boolean exists = c != null && c.getCount() > 0;
        if (c != null) c.close();

        if (exists) {
            db.update(DbHelper.T_USER, v, "id=1", null);
        } else {
            v.put("id", 1);
            db.insert(DbHelper.T_USER, null, v);
        }
    }
}