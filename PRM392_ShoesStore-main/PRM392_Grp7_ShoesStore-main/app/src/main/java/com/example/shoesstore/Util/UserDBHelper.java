package com.example.shoesstore.Util;

import android.database.Cursor;

import com.example.shoesstore.Models.User;

public class UserDBHelper {
    private String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS User (" +
            "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT," +
            "password TEXT," +
            "role INTEGER," +
            "isDelete INTEGER DEFAULT 0)";
    private Database database;

    public UserDBHelper(Database database) {
        this.database = database;
        AddTable();
    }

    public void AddTable() {
        database.QueryData(CREATE_TABLE_USER);
    }

    public void deleteData() {
        // Create a SQL DELETE statement
        String sql = "DELETE FROM User";
        database.QueryData(sql);
    }

    public void addAdmin() {
        String sql1 = "Select * FROM User Where role = 0";
        Cursor cursor = database.GetData(sql1);
        if(!cursor.moveToFirst()){
            String sql = "INSERT INTO User (username, password,role,isDelete) VALUES ('admin', '1',0,0)";
            database.QueryData(sql);
        }
    }

    public User checkLogin(String username, String password) {
        String sql = "SELECT userId,username,password,role,isDelete From User Where username LIKE '"
                + username + "' AND password LIKE '" + password + "';";
        Cursor cursor = database.GetData(sql);
        User user = null;
        while (cursor.moveToNext()) {
            boolean isDelete = false;
            if (cursor.getInt(4) == 1) {
                isDelete = true;
            } else {
                isDelete = false;
            }
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), isDelete);
        }
        cursor.close();
        return user;
    }

    public void regisUser(String username, String password) {
        String sql = "INSERT INTO User (username, password,role,isDelete) VALUES ('" + username + "', '" + password + "',2,0)";
        database.QueryData(sql);
    }

    public boolean checkExist(String username) {
        String sql = "SELECT userId,username,password,role,isDelete FROM User WHERE username = '" + username + "'";
        Cursor cursor = database.GetData(sql);
        User user = null;
        while (cursor.moveToNext()) {
            boolean isDelete = false;
            if (cursor.getInt(4) == 1) {
                isDelete = true;
            } else {
                isDelete = false;
            }
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), isDelete);
        }
        cursor.close();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor GetAllUser() {
        Cursor data = database.GetData("SELECT userId, username, password, role, isDelete " +
                "FROM User");
        return data;
    }
}
