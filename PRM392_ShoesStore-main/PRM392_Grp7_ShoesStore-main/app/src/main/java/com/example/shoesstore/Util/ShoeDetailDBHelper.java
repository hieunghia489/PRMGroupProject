package com.example.shoesstore.Util;

import android.database.Cursor;

public class ShoeDetailDBHelper {
    private String CREATE_TABLE_SHOE_DETAIL = "CREATE TABLE IF NOT EXISTS ShoeDetail (" +
            "shoeDetailId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "shoeId INTEGER," +
            "isSold INTEGER DEFAULT 0," +
            "isDelete INTEGER DEFAULT 0)";
    private Database database;

    public ShoeDetailDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_SHOE_DETAIL);
    }

    public Cursor GetAllShoeDetail() {
        String selectQuery = "SELECT shoeDetailId, shoeId, isSold, isDelete FROM ShoeDetail";
        Cursor data = database.GetData(selectQuery);
        return data;
    }

    public void addRangeShoeDetail(int quantity, int shoeId) {
        for (int i = 0; i < quantity; i++) {
            String insertQuery = "INSERT INTO ShoeDetail VALUES (null, " + shoeId + ", 0, 0)";
            database.QueryData(insertQuery);
        }
    }

    public int getUnSoldShoe(int shoeId) {
        Cursor cursor = database.GetData("SELECT shoeDetailId FROM ShoeDetail WHERE isSold = 0 AND isDelete = 0 AND shoeId = " + shoeId + " LIMIT 1");
        int shoeDetailId = -1;
        if (cursor.moveToFirst()) {
            shoeDetailId = cursor.getInt(0);
        }
        cursor.close();
        return shoeDetailId;
    }

    public void setSoldShoe(int shoeDetailId) {
        database.QueryData("UPDATE ShoeDetail SET isSold = 1 WHERE shoeDetailId = " + shoeDetailId);
    }

}
