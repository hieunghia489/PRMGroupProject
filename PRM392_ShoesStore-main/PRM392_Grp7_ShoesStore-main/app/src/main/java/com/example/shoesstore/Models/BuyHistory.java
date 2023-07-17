package com.example.shoesstore.Models;

import java.util.Date;

public class BuyHistory {
    private int buyId;
    private Date buyDate;
    private int userId;
    private double totalPrice;
    private boolean isDelete;
    private User User;

    public BuyHistory(int buyId, Date buyDate, int userId, double totalPrice, boolean isDelete) {
        this.buyId = buyId;
        this.buyDate = buyDate;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.isDelete = isDelete;
    }

    public int getBuyId() {
        return buyId;
    }

    public void setBuyId(int buyId) {
        this.buyId = buyId;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public com.example.shoesstore.Models.User getUser() {
        return User;
    }

    public void setUser(com.example.shoesstore.Models.User user) {
        User = user;
    }
}
