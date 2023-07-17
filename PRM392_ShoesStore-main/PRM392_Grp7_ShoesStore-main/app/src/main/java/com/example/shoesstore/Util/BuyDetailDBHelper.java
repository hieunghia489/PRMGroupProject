package com.example.shoesstore.Util;

import android.database.Cursor;

public class BuyDetailDBHelper {
    private String CREATE_TABLE_BUY_DETAIL = "CREATE TABLE IF NOT EXISTS BuyDetail (" +
            "buyDetailId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "buyId INTEGER," +
            "shoeId INTEGER," +
            "shoeDetailId INTEGER," +
            "shoePriceWhenBuy REAL," +
            "isDelete INTEGER DEFAULT 0)";
    private Database database;

    public BuyDetailDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_BUY_DETAIL);
    }

    public Cursor GetAllBuyDetail() {
        String selectQuery = "SELECT buyDetailId, buyId, shoeId, shoeDetailId, shoePriceWhenBuy, isDelete " +
                "FROM BuyDetail";
        Cursor data = database.GetData(selectQuery);
        return data;
    }

    public void CreateBuyDetail(int buyId, int shoeId, int shoeDetailId, double price) {
        String insertQuery = "INSERT INTO BuyDetail VALUES (null, " + buyId + ", " + shoeId + ", " + shoeDetailId + ", " + price + ", 0)";
        database.QueryData(insertQuery);
    }
    public Cursor getBuyDetailByBuyId(int buyId) {
        String selectQuery = "SELECT buyDetailId, buyId, shoeId, shoeDetailId, shoePriceWhenBuy, isDelete " +
                "FROM BuyDetail " +
                "WHERE isDelete = 0 AND buyId = " + buyId;
        Cursor data = database.GetData(selectQuery);
        return data;
    }
}
