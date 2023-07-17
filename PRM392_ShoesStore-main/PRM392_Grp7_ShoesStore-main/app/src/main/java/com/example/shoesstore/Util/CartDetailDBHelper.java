package com.example.shoesstore.Util;

import android.database.Cursor;

public class CartDetailDBHelper {
    private String CREATE_TABLE_CART_DETAIL = "CREATE TABLE IF NOT EXISTS CartDetail (" +
            "cartDetailId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cartId INTEGER," +
            "shoeId INTEGER," +
            "quantity INTEGER," +
            "totalPrice REAL)";
    private Database database;

    public CartDetailDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_CART_DETAIL);
    }

    public Cursor GetAllCartDetail(int cartId) {
        String selectQuery = "SELECT cartDetailId, cartId, shoeId, quantity, totalPrice " +
                "FROM CartDetail " +
                "WHERE cartId = " + cartId;
        Cursor data = database.GetData(selectQuery);
        return data;
    }


    public void AddCart(int cartId, int shoeId, double price) {
        Cursor cursor = database.GetData("SELECT quantity\n" +
                "FROM CartDetail\n" +
                "WHERE cartId = " + cartId + " AND shoeId = " + shoeId + ";");
        if (cursor.moveToFirst()) {
            database.QueryData("UPDATE CartDetail\n" +
                    "SET quantity = quantity + 1,\n" +
                    "totalPrice = quantity * " + price + "\n" +
                    "WHERE cartId = " + cartId + " AND shoeId = " + shoeId + ";");
        } else {
            database.QueryData("INSERT INTO CartDetail (cartId, shoeId, quantity, totalPrice)\n" +
                    "VALUES (" + cartId + ", " + shoeId + ", 1, " + price + ");");
        }
        cursor.close();
    }

    public void RemoveCart(int cartId, int shoeId) {
        Cursor cursor = database.GetData("SELECT quantity FROM CartDetail WHERE cartId = " + cartId + " AND shoeId = " + shoeId + ";");
        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            if (currentQuantity > 1) {
                database.QueryData("UPDATE CartDetail SET quantity = quantity - 1 WHERE cartId = " + cartId + " AND shoeId = " + shoeId + ";");
            } else if (currentQuantity == 1) {
                database.QueryData("DELETE FROM CartDetail WHERE cartId = " + cartId + " AND shoeId = " + shoeId + ";");
            }
        }
        cursor.close();
    }

}
