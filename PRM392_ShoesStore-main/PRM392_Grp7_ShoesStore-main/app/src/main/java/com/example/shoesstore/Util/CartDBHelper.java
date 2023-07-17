package com.example.shoesstore.Util;

import android.database.Cursor;

import com.example.shoesstore.Models.Cart;

public class CartDBHelper {

    public static String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS Cart (" +
            "cartId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "userId INTEGER," +
            "isDelete INTEGER DEFAULT 0)";

    private Database database;

    public CartDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_CART);
    }

    public int AddCart(int userId) {
        int cartId = -1;
        String selectQuery = "SELECT cartId FROM Cart WHERE isDelete = 0 AND userId = " + userId;
        Cursor cursor = database.GetData(selectQuery);

        if (cursor.moveToFirst()) {
            cartId = cursor.getInt(0);
        } else {
            String insertQuery = "INSERT INTO Cart (userId, isDelete) VALUES (" + userId + ", 0)";
            database.QueryData(insertQuery);

            cursor = database.GetData(selectQuery);
            if (cursor.moveToFirst()) {
                cartId = cursor.getInt(0);
            }
        }

        cursor.close();
        return cartId;
    }


    public void DeleteDatabase(int id) {
        String updateQuery = "UPDATE Cart SET isDelete = 1 WHERE cartId = " + id + " AND isDelete = 0";
        database.QueryData(updateQuery);
    }


    public Cursor GetDatabaseByUserId(int id) {
        String selectQuery = "SELECT cartId, userId, isDelete FROM Cart WHERE userId = " + id + " AND isDelete = 0";
        Cursor data = database.GetData(selectQuery);
        return data;
    }
    public Cart getCartById(int cartId) {
        Cursor cursor = database.GetData("SELECT cartId, userId, isDelete FROM Cart WHERE isDelete = 0 AND cartId = " + cartId);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int userId = cursor.getInt(1);
            boolean isDelete = cursor.getInt(2) == 1;

            return new Cart(id, userId, isDelete);
        }

        return null; // If no matching cart found
    }

}
