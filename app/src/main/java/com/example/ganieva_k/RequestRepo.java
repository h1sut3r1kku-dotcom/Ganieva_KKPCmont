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

    // Создание заявки
    public void createRequest(
            String firstName, String lastName, String middleName,
            String phone, String model, String email, String address,
            String comment, String category, String date, String time
    ) {
        ContentValues v = new ContentValues();
        v.put("id", UUID.randomUUID().toString());
        v.put("first_name", firstName);
        v.put("last_name", lastName);
        v.put("middle_name", middleName);
        v.put("phone", phone);
        v.put("model", model);
        v.put("email", email);
        v.put("address", address);
        v.put("comment", comment);
        v.put("category", category);
        v.put("status", "IN_PROGRESS");
        v.put("created_date", date);
        v.put("created_time", time);
        helper.getWritableDatabase().insert(DbHelper.T_REQUESTS, null, v);
    }

    // Получение всех заявок по категории с сортировкой
    public List<ContentValues> listByCategory(String category, String orderBy) {
        String order = (orderBy == null || orderBy.trim().isEmpty())
                ? "created_date DESC, created_time DESC"
                : orderBy;
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor c = db.query(DbHelper.T_REQUESTS, null, "category=?", new String[]{category}, null, null, order)) {
            return copyCursor(c);
        }
    }

    // Подсчет заявок по категории
    public int countByCategory(String category) {
        return countByCategoryAndStatus(category, null);
    }

    public int countByCategoryAndStatus(String category, String status) {
        String where;
        String[] args;
        if (status == null) {
            where = "category = ?";
            args = new String[]{category};
        } else {
            where = "category = ? AND status = ?";
            args = new String[]{category, status};
        }
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + DbHelper.T_REQUESTS + " WHERE " + where, args)) {
            c.moveToFirst();
            return c.getInt(0);
        }
    }

    // Отметить как выполненную
    public void markDone(String id, String doneDate, String doneTime) {
        ContentValues v = new ContentValues();
        v.put("status", "DONE");
        v.put("done_date", doneDate);
        v.put("done_time", doneTime);
        helper.getWritableDatabase().update(DbHelper.T_REQUESTS, v, "id=?", new String[]{id});
    }

    // Удалить заявку
    public void delete(String id) {
        helper.getWritableDatabase().delete(DbHelper.T_REQUESTS, "id=?", new String[]{id});
    }

    // Получить заявку по ID
    public ContentValues getById(String id) {
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor c = db.query(DbHelper.T_REQUESTS, null, "id=?", new String[]{id}, null, null, null)) {
            if (c.moveToFirst()) {
                ContentValues v = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(c, v);
                return v;
            }
            return null;
        }
    }

    // Обновление заявки
    public void updateRequest(
            String id, String firstName, String lastName, String middleName,
            String phone, String model, String email, String address, String comment, String category
    ) {
        ContentValues v = new ContentValues();
        v.put("first_name", firstName);
        v.put("last_name", lastName);
        v.put("middle_name", middleName);
        v.put("phone", phone);
        v.put("model", model);
        v.put("email", email);
        v.put("address", address);
        v.put("comment", comment);
        v.put("category", category);
        helper.getWritableDatabase().update(DbHelper.T_REQUESTS, v, "id=?", new String[]{id});
    }

    private List<ContentValues> copyCursor(Cursor c) {
        List<ContentValues> list = new ArrayList<>();
        while (c.moveToNext()) {
            ContentValues v = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(c, v);
            list.add(v);
        }
        return list;
    }
}