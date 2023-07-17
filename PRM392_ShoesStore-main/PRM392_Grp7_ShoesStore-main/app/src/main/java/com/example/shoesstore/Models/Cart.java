package com.example.shoesstore.Models;

public class Cart {
    private int cartId;
    private int userId;
    private boolean isDelete;
    private User User;

    public Cart(int cartId, int userId, boolean isDelete) {
        this.cartId = cartId;
        this.userId = userId;
        this.isDelete = isDelete;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
