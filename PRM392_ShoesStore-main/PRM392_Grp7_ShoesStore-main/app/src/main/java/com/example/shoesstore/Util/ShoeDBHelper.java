package com.example.shoesstore.Util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shoesstore.Models.Shoe;

import java.util.ArrayList;
import java.util.List;

public class ShoeDBHelper {
    public String CREATE_TABLE_SHOE = "CREATE TABLE IF NOT EXISTS Shoe (" +
            "shoeId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "shoeName TEXT," +
            "imgName TEXT," +
            "shoeDescription TEXT," +
            "shoePrimaryPrice REAL," +
            "quantity INTEGER," +
            "isDelete INTEGER DEFAULT 0)";


    private Database database;

    public ShoeDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_SHOE);
    }

    public void InsertShoeData() {
        // Inserting the first row
        String query1 = "INSERT INTO Shoe (shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete) " +
                "VALUES ('Nike Air Max', 'nike_air_max', 'Comfortable running shoes', 129.99, 10, 0)";
        database.QueryData(query1);

        // Inserting the second row
        String query2 = "INSERT INTO Shoe (shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete) " +
                "VALUES ('Adidas Superstar', 'adidas_superstar', 'Classic sneakers', 89.99, 5, 0)";
        database.QueryData(query2);

        // Inserting the third row
        String query3 = "INSERT INTO Shoe (shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete) " +
                "VALUES ('Puma Suede', 'puma_suede', 'Stylish casual shoes', 79.99, 8, 0)";
        database.QueryData(query3);

        // Inserting the fourth row
        String query4 = "INSERT INTO Shoe (shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete) " +
                "VALUES ('New Balance 574', 'new_balance_574', 'Versatile lifestyle shoes', 109.99, 3, 0)";
        database.QueryData(query4);

        // Inserting the fifth row
        String query5 = "INSERT INTO Shoe (shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete) " +
                "VALUES ('Vans Old Skool', 'vans_old_skoo', 'Skateboarding sneakers', 69.99, 12, 0)";
        database.QueryData(query5);

        database.close();
    }

    public void AddNewShoe(Shoe shoe) {
        database.QueryData("INSERT INTO Shoe VALUES(null, '" + shoe.getShoeName() + "', '" + shoe.getImgName() + "', '" + shoe.getShoeDescription() + "', " + shoe.getShoePrimaryPrice() + ", " + shoe.getQuantity() + ", 0)");
    }

    public void UpdateShoe(Shoe shoe) {
        database.QueryData("UPDATE Shoe " +
                "SET shoeName = '" + shoe.getShoeName() + "' , imgName = '" + shoe.getImgName() + "' , shoeDescription = '" + shoe.getShoeDescription() + "' , shoePrimaryPrice = " + shoe.getShoePrimaryPrice() + ", quantity = " + shoe.getQuantity() + ", isDelete = " + shoe.isDelete() + " " +
                "WHERE shoeId = " + shoe.getShoeId() + " AND isDelete = 0");
    }

    public void DeleteShoe(int id) {
        database.QueryData("UPDATE Shoe SET isDelete = 1 WHERE shoeId = " + id + " AND isDelete = 0");
    }

    public Cursor GetAllShoe() {
        Cursor data = database.GetData("SELECT shoeId, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete " +
                "FROM Shoe WHERE isDelete = 0");
        return data;
    }

    public Shoe getShoeById(int shoeId) {
        Cursor cursor = database.GetData("SELECT shoeId, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete FROM Shoe WHERE isDelete = 0 AND shoeId = " + shoeId);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String shoeName = cursor.getString(1);
            String imgName = cursor.getString(2);
            String shoeDescription = cursor.getString(3);
            double shoePrimaryPrice = cursor.getDouble(4);
            int quantity = cursor.getInt(5);
            boolean isDelete = cursor.getInt(6) == 1;

            return new Shoe(id, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete);
        }

        return null; // If no matching shoe found
    }

    public Shoe getShoeByBuyId(int shoeId) {
        Cursor cursor = database.GetData("SELECT shoeId, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete FROM Shoe WHERE shoeId = " + shoeId);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String shoeName = cursor.getString(1);
            String imgName = cursor.getString(2);
            String shoeDescription = cursor.getString(3);
            double shoePrimaryPrice = cursor.getDouble(4);
            int quantity = cursor.getInt(5);
            boolean isDelete = cursor.getInt(6) == 1;

            return new Shoe(id, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete);
        }

        return null; // If no matching shoe found
    }

    public void DeletaData() {
        database.QueryData("Delete from Shoe");
    }

    public Cursor GetShoeByShoeName(String name){
        Cursor cursor = database.GetData("SELECT shoeId, shoeName, imgName, shoeDescription, shoePrimaryPrice, quantity, isDelete FROM Shoe WHERE shoeName LIKE  '%" + name + "%'");
        return cursor;
    }
}
