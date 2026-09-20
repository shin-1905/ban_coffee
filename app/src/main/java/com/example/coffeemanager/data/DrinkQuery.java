package com.example.coffeemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl.SQlite;

import java.util.ArrayList;
import java.util.List;

public class DrinkQuery {

    private final SQlite dbHelper;

    public DrinkQuery(Context context) {
        dbHelper = new SQlite(context);
    }

    public List<Drink> getAllDrinks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaDU, TenDoUong, DonGia, MoTa, HinhAnh, COALESCE(DanhMuc, ''), COALESCE(ConBan, 1) " +
                        "FROM DoUong ORDER BY TenDoUong ASC",
                null
        );
        return docDanhSachDoUong(cursor);
    }

    public List<Drink> searchDrinks(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String keyword = "%" + query.trim() + "%";
        Cursor cursor = db.rawQuery(
                "SELECT MaDU, TenDoUong, DonGia, MoTa, HinhAnh, COALESCE(DanhMuc, ''), COALESCE(ConBan, 1) " +
                        "FROM DoUong WHERE TenDoUong LIKE ? OR COALESCE(DanhMuc, '') LIKE ? ORDER BY TenDoUong ASC",
                new String[]{keyword, keyword}
        );
        return docDanhSachDoUong(cursor);
    }

    public Drink getDrinkById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaDU, TenDoUong, DonGia, MoTa, HinhAnh, COALESCE(DanhMuc, ''), COALESCE(ConBan, 1) " +
                        "FROM DoUong WHERE MaDU = ?",
                new String[]{String.valueOf(id)}
        );
        try {
            if (cursor.moveToFirst()) {
                return taoDoUong(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public long insertDrink(Drink drink) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.insert("DoUong", null, taoGiaTriDoUong(drink));
    }

    public int updateDrink(Drink drink) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.update(
                "DoUong",
                taoGiaTriDoUong(drink),
                "MaDU = ?",
                new String[]{String.valueOf(drink.getId())}
        );
    }

    public int deleteDrink(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("DoUong", "MaDU = ?", new String[]{String.valueOf(id)});
    }

    private List<Drink> docDanhSachDoUong(Cursor cursor) {
        List<Drink> danhSach = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                danhSach.add(taoDoUong(cursor));
            }
            return danhSach;
        } finally {
            cursor.close();
        }
    }

    private Drink taoDoUong(Cursor cursor) {
        return new Drink(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6) == 1
        );
    }

    private ContentValues taoGiaTriDoUong(Drink drink) {
        ContentValues values = new ContentValues();
        values.put("TenDoUong", drink.getName());
        values.put("DonGia", drink.getPrice());
        values.put("MoTa", drink.getDescription());
        values.put("HinhAnh", drink.getImageUri());
        values.put("DanhMuc", drink.getCategory());
        values.put("ConBan", drink.isAvailable() ? 1 : 0);
        return values;
    }
}
