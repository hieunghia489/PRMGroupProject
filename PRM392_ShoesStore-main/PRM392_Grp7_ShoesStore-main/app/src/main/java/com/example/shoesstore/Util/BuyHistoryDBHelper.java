package com.example.shoesstore.Util;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyHistoryDBHelper {
    private String CREATE_TABLE_BUY_HISTORY = "CREATE TABLE IF NOT EXISTS BuyHistory (" +
            "buyId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "buyDate TEXT," +
            "userId INTEGER," +
            "totalPrice REAL," +
            "isDelete INTEGER DEFAULT 0)";
    private Database database;

    public BuyHistoryDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_BUY_HISTORY);
    }

    public Cursor GetAllBuyHistory() {
        String selectQuery = "SELECT buyId, buyDate, userId, totalPrice, isDelete " +
                "FROM BuyHistory";
        Cursor data = database.GetData(selectQuery);
        return data;
    }

    public int AddBuy(int userId, double totalPrice) {
        String pattern = "dd-MM-yyyy";
        String dateInString = new SimpleDateFormat(pattern).format(new Date());

        String insertQuery = "INSERT INTO BuyHistory VALUES (null, '" + dateInString + "', " + userId + ", " + totalPrice + ", 0)";
        database.QueryData(insertQuery);

        String selectQuery = "SELECT buyId FROM BuyHistory WHERE buyDate = '" + dateInString + "' AND userId = " + userId + " AND isDelete = 0";
        Cursor cursor = database.GetData(selectQuery);
        int buyId = -1;
        if (cursor.moveToFirst()) {
            buyId = cursor.getInt(0);
        }
        cursor.close();
        return buyId;
    }
    public Cursor GetBuyHistoryByUserId(int userId) {
        String selectQuery = "SELECT buyId, buyDate, userId, totalPrice, isDelete " +
                "FROM BuyHistory " +
                "WHERE userId = " + userId + " AND isDelete = 0";
        Cursor data = database.GetData(selectQuery);
        return data;
    }

}
